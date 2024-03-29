package io.jenkins.plugins.pipelinemonitor.model;

import java.util.Date;

/**
 * Data class holding Jenkins build status Created by Shelwin.
 */
public class BuildStatus {

  private String jenkinsUrl;

  private String jobName;

  private int number;

  private Date startTime;

  private String result;

  private long duration;

  /**
   * Constructor of Buildstatus.
   */
  public BuildStatus(String jenkinsUrl, String jobName, int number, Date startTime, String result,
      long duration) {
    this.jenkinsUrl = jenkinsUrl;
    this.jobName = jobName;
    this.number = number;
    this.startTime = new Date(startTime.getTime());
    this.result = result;
    this.duration = duration;
  }

  /**
   * Constructor of Buildstatus.
   */
  public BuildStatus() {
    this.jenkinsUrl = "";
    this.jobName = "";
    this.number = 0;
    this.startTime = new Date();
    this.result = "";
    this.duration = 0;
  }

  public String getJenkinsUrl() {
    return jenkinsUrl;
  }

  public void setJenkinsUrl(String jenkinsUrl) {
    this.jenkinsUrl = jenkinsUrl;
  }

  public String getJobName() {
    return jobName;
  }

  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public Date getStartTime() {
    return new Date(startTime.getTime());
  }

  public void setStartTime(Date startTime) {
    this.startTime = new Date(startTime.getTime());
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public long getDuration() {
    return duration;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }
}
