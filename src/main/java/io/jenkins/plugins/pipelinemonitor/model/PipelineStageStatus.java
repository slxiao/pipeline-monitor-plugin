package io.jenkins.plugins.pipelinemonitor.model;

import java.util.Date;

/**
 * Data class holding Jenkins pipeline stage status Created by Shelwin.
 */
public class PipelineStageStatus {

  private String jenkinsUrl;

  private String jobName;

  private int number;

  private String name;

  private Date startTime;

  private String result;

  private long duration;

  /**
   * Construnctor of Pipeline stage status.
   * 
   * @param jenkinsUrl jenkins URL.
   * @param jobName    jenkins job name.
   * @param number     jenkins build number.
   * @param name       stage name.
   * @param startTime  stage start time.
   * @param result     stage result.
   * @param duration   stage duraiton.
   */
  public PipelineStageStatus(String jenkinsUrl, String jobName, int number, String name,
      Date startTime, String result, long duration) {
    this.jenkinsUrl = jenkinsUrl;
    this.jobName = jobName;
    this.number = number;
    this.name = name;
    this.startTime = new Date(startTime.getTime());
    this.result = result;
    this.duration = duration;
  }

  /**
   * Construnctor of Pipeline stage status.
   */
  public PipelineStageStatus() {
    this.jenkinsUrl = "";
    this.jobName = "";
    this.number = 0;
    this.name = "";
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
