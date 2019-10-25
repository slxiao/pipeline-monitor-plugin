package io.jenkins.plugins.pipelinemonitor.model;

/**
 * Data class holding Jenkins build status
 * Created by Shelwin
 */
public class BuildStatus {

    private String jenkinsUrl;

    public BuildStatus(String jenkinsUrl) {
        this.jenkinsUrl = jenkinsUrl;
    }

    public BuildStatus() {
        this.jenkinsUrl = "";
    }

    public String getJenkinsUrl() {
        return jenkinsUrl;
    }

    public void setJenkinsUrl(String jenkinsUrl) {
        this.jenkinsUrl = jenkinsUrl;
    }
}