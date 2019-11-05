package io.jenkins.plugins.pipelinemonitor;

import org.junit.*;

import static org.mockito.Mockito.*;

import io.jenkins.plugins.pipelinemonitor.StageStatusListener;

import org.jenkinsci.plugins.workflow.actions.ErrorAction;
import org.jenkinsci.plugins.workflow.cps.nodes.StepStartNode;
import org.jenkinsci.plugins.workflow.actions.TagsAction;
import org.jenkinsci.plugins.workflow.actions.LabelAction;
import org.jenkinsci.plugins.workflow.actions.StageAction;
import org.jenkinsci.plugins.workflow.actions.ThreadNameAction;
import org.jenkinsci.plugins.workflow.actions.TimingAction;
import org.jenkinsci.plugins.workflow.cps.nodes.StepAtomNode;
import org.jenkinsci.plugins.workflow.cps.nodes.StepEndNode;
import org.jenkinsci.plugins.workflow.flow.FlowExecution;
import org.jenkinsci.plugins.workflow.flow.GraphListener;
import org.jenkinsci.plugins.workflow.graph.FlowNode;
import org.jenkinsci.plugins.workflow.flow.FlowExecutionOwner;

import hudson.model.AbstractBuild;
import org.jenkinsci.plugins.pipeline.modeldefinition.actions.ExecutionModelAction;
import org.jenkinsci.plugins.pipeline.modeldefinition.ast.ModelASTStage;
import org.jenkinsci.plugins.pipeline.modeldefinition.ast.ModelASTStages;

import static org.junit.Assert.assertEquals;

import org.jenkinsci.plugins.pipeline.StageStatus;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({StageStatus.class})
public class StageStatusListenerTest {
  @Before
  public void setUp() {
    PowerMockito.mockStatic(StageStatus.class);
  }

  @Test
  public void testBuildStateForStageWithNullErrorAction() throws Exception {
    StepStartNode flowNodeMock = mock(StepStartNode.class);
    // ErrorAction errorActionMock = mock(ErrorAction.class);
    when(flowNodeMock.getAction(TagsAction.class)).thenReturn(null);

    String state = StageStatusListener.buildStateForStage(flowNodeMock, null);
    assertEquals(state, "CompletedSuccess");
  }

  @Test
  public void testBuildStateForStageWithErrorAction() throws Exception {
    StepStartNode flowNodeMock = mock(StepStartNode.class);
    ErrorAction errorActionMock = mock(ErrorAction.class);
    when(flowNodeMock.getAction(TagsAction.class)).thenReturn(null);

    String state = StageStatusListener.buildStateForStage(flowNodeMock, errorActionMock);
    assertEquals(state, "CompletedError");
  }

  @Test
  public void testBuildStateForStageWithTags() throws Exception {
    StepStartNode flowNodeMock = mock(StepStartNode.class);
    ErrorAction errorActionMock = mock(ErrorAction.class);
    TagsAction tagsActionMock = mock(TagsAction.class);
    when(flowNodeMock.getAction(TagsAction.class)).thenReturn(tagsActionMock);

    when(tagsActionMock.getTagValue(StageStatus.TAG_NAME)).thenReturn("SKIPPED_FOR_FAILURE");
    PowerMockito.when(StageStatus.class, "getSkippedForFailure").thenReturn("SKIPPED_FOR_FAILURE");
    String stateSkipped = StageStatusListener.buildStateForStage(flowNodeMock, errorActionMock);
    assertEquals(stateSkipped, "SkippedFailure");

    when(tagsActionMock.getTagValue(StageStatus.TAG_NAME)).thenReturn("SKIPPED_FOR_UNSTABLE");
    PowerMockito.when(StageStatus.class, "getSkippedForUnstable")
        .thenReturn("SKIPPED_FOR_UNSTABLE");
    String stateUnstable = StageStatusListener.buildStateForStage(flowNodeMock, errorActionMock);
    assertEquals(stateUnstable, "SkippedUnstable");

    when(tagsActionMock.getTagValue(StageStatus.TAG_NAME)).thenReturn("SKIPPED_FOR_CONDITIONAL");
    PowerMockito.when(StageStatus.class, "getSkippedForConditional")
        .thenReturn("SKIPPED_FOR_CONDITIONAL");
    String stateSkipCon = StageStatusListener.buildStateForStage(flowNodeMock, errorActionMock);
    assertEquals(stateSkipCon, "SkippedConditional");

    when(tagsActionMock.getTagValue(StageStatus.TAG_NAME)).thenReturn("FAILED_AND_CONTINUED");
    PowerMockito.when(StageStatus.class, "getFailedAndContinued")
        .thenReturn("FAILED_AND_CONTINUED");
    String stateFail = StageStatusListener.buildStateForStage(flowNodeMock, errorActionMock);
    assertEquals(stateFail, "CompletedError");
  }

}
