package io.jenkins.plugins.pipelinemonitor;

import java.io.IOException;



import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.Builder;
import jenkins.tasks.SimpleBuildStep;

public class PipelineMonitorBuilder extends Builder implements SimpleBuildStep {

    private final String name;

    public PipelineMonitorBuilder(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {
        listener.getLogger().println("Hello, " + name + "!");
    }

}