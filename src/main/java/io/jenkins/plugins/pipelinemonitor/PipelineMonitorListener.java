package io.jenkins.plugins.pipelinemonitor;

import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

/**
 * Implements {@link RunListener} extension point to
 * provide job status information to subscribers as jobs complete.
 *
 * @author Jeff Pearce (GitHub jeffpearce)
 */
@Extension
public class PipelineMonitorListener extends RunListener<Run<?, ?>> {

    @Override
    public void onStarted(Run<?, ?> run, TaskListener listener) {
        super.onStarted(run, listener);

        listener.getLogger().println("Hello, pipeline listener!");
    }

}
