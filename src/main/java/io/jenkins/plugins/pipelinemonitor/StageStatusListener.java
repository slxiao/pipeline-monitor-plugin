package io.jenkins.plugins.pipelinemonitor;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jenkinsci.plugins.pipeline.StageStatus;
import org.jenkinsci.plugins.workflow.actions.ErrorAction;
import org.jenkinsci.plugins.workflow.actions.LabelAction;
import org.jenkinsci.plugins.workflow.actions.StageAction;
import org.jenkinsci.plugins.workflow.actions.TagsAction;
import org.jenkinsci.plugins.workflow.actions.ThreadNameAction;
import org.jenkinsci.plugins.workflow.actions.TimingAction;
import org.jenkinsci.plugins.workflow.cps.nodes.StepAtomNode;
import org.jenkinsci.plugins.workflow.cps.nodes.StepEndNode;
import org.jenkinsci.plugins.workflow.cps.nodes.StepStartNode;
import org.jenkinsci.plugins.workflow.flow.GraphListener;
import org.jenkinsci.plugins.workflow.graph.FlowNode;

import hudson.Extension;


/**
 * GraphListener implementation which provides status (pending, error or
 * success) and timing information for each stage in a build.
 *
 * @author Jeff Pearce (GitHub jeffpeare)
 */
@Extension
public class StageStatusListener implements GraphListener {

    /**
     * Evaluates if we can provide stats on a node.
     *
     * @param fn a node in workflow
     */
    @Override
    public void onNewHead(FlowNode fn) {
        try {
            if (isStage(fn)) {
                log(Level.WARNING, "checkEnableBuildStatus");
            } else if (fn instanceof StepAtomNode) {

                // We don't need to look at atom nodes for declarative pipeline jobs, because
                // they have a nice model containing all the stages
                if (isDeclarativePipelineJob(fn)) {
                    return;
                }

                ErrorAction errorAction = fn.getError();
                String nodeName = null;

                if (errorAction == null) {
                    return;
                }

                List<? extends FlowNode> enclosingBlocks = fn.getEnclosingBlocks();
                boolean isInStage = false;

                for (FlowNode encosingNode : enclosingBlocks) {
                    if (isStage(encosingNode)) {
                        isInStage = true;
                    }
                }

                if (isInStage) {
                    return;
                }

                // We have a non-declarative atom that isn't in a stage, which has failed.
                // Since normal processing is via stages, we'd normally miss this failure;
                // send an out of band error notification to make sure it's recordded by any
                // interested notifiers
                log(Level.WARNING, "Non stage error: " + fn.getDisplayName());

            } else if (fn instanceof StepEndNode) {
                log(Level.WARNING, "checkEnableBuildStatus");

                String startId = ((StepEndNode) fn).getStartNode().getId();
                FlowNode startNode = fn.getExecution().getNode(startId);
                if (null == startNode) {
                    return;
                }

                ErrorAction errorAction = fn.getError();
                String nodeName = null;

                long time = getTime(startNode, fn);
                LabelAction label = startNode.getAction(LabelAction.class);

                if (label != null) {
                    nodeName = label.getDisplayName();
                } else if (null != errorAction && startNode instanceof StepStartNode) {
                    nodeName = ((StepStartNode) startNode).getStepName();
                }

                if (nodeName != null) {
                    String buildState = buildStateForStage(startNode, errorAction);
                    log(Level.WARNING, "nodeName: " + nodeName + " buildState: " + buildState + " time: " + time);

                }
            }
        } catch (IOException ex) {
            log(Level.WARNING, "IOException");
        }
    }

    /**
     * Determines the appropriate state for a stage
     *
     * @param flowNode The stage start node
     * @param errorAction The error action from the stage end node
     * @return Stage state
     */
    static String buildStateForStage(FlowNode flowNode, ErrorAction errorAction) {
        String buildState = errorAction == null ? "CompletedSuccess" : "CompletedError";
        TagsAction tags = flowNode.getAction(TagsAction.class);
        if (tags != null) {
            String status = tags.getTagValue(StageStatus.TAG_NAME);
            if (status != null) {
                if (status.equals(StageStatus.getSkippedForFailure())) {
                    return "SkippedFailure";
                } else if (status.equals(StageStatus.getSkippedForUnstable())) {
                    return "SkippedUnstable";
                } else if (status.equals(StageStatus.getSkippedForConditional())) {
                    return "SkippedConditional";
                } else if (status.equals(StageStatus.getFailedAndContinued())) {
                    return "CompletedError";
                }
            }
        }
        return buildState;
    }

    /**
     * Gets the execution time of a block defined by startNode and endNode.
     *
     * @param startNode startNode of a block
     * @param endNode endNode of a block
     * @return Execution time of the block
     */
    static long getTime(FlowNode startNode, FlowNode endNode) {
        TimingAction startTime = startNode.getAction(TimingAction.class);
        TimingAction endTime = endNode.getAction(TimingAction.class);

        if (startTime != null && endTime != null) {
            return endTime.getStartTime() - startTime.getStartTime();
        }
        return 0;
    }

    /**
     * Determines if a {@link FlowNode} describes a stage.
     *
     * Note: this check is copied from PipelineNodeUtil.java in blueocean-plugin
     *
     * @param node node of a workflow
     * @return true if it's a stage node; false otherwise
     */
    private static boolean isStage(FlowNode node) {
        return node != null && ((node.getAction(StageAction.class) != null)
                || (node.getAction(LabelAction.class) != null && node.getAction(ThreadNameAction.class) == null));
    }

    /**
     * Determines if the node belongs to a declarative pipeline.
     *
     * @param fn node of a workflow
     * @return true/false
     */
    private static boolean isDeclarativePipelineJob(FlowNode fn) {
       log(Level.WARNING, "isDeclarativePipelineJob: false");
        return false;
    }

    /**
     * Prints to stdout or stderr.
     *
     * @param level INFO/WARNING/ERROR
     * @param format String that formats the log
     * @param args arguments for the formated log string
     */
    private static void log(Level level, String format, Object... args) {
        getLogger().log(level, String.format(format, args));
    }

    /**
     * Gets the logger for the listener.
     *
     * @return logger object
     */
    private static Logger getLogger() {
        return Logger.getLogger(StageStatusListener.class.getName());
    }
}
