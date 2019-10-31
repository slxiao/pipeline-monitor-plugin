package io.jenkins.plugins.pipelinemonitor;

import hudson.Extension;
import hudson.model.Run;
import hudson.model.listeners.RunListener;
import io.jenkins.plugins.pipelinemonitor.model.BuildStatus;
import io.jenkins.plugins.pipelinemonitor.util.RestClientUtil;
import jenkins.model.Jenkins;

/**
 * Implements {@link RunListener} extension point to
 * provide job status information to subscribers as jobs complete.
 *
 * @author Jeff Pearce (GitHub jeffpearce)
 */
@Extension
public class BuildStatusListener extends RunListener<Run<?, ?>> {

    @Override
    public void onFinalized(final Run<?, ?> run) {

        final String buildResult = run.getResult() == null ?
                    "ONGOING": run.getResult().toString();

        BuildStatus build = new BuildStatus();
        build.setJenkinsUrl(Jenkins.getInstance().getRootUrl());
        build.setJobName(run.getParent().getName());
        build.setNumber(run.getNumber());
        build.setResult(buildResult);
        build.setDuration(run.getDuration());

        RestClientUtil.postToService("http://10.183.42.147:8080", build);

    }

}
