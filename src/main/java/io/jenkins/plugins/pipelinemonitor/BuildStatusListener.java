package io.jenkins.plugins.pipelinemonitor;

import hudson.Extension;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.listeners.RunListener;
import hudson.model.TaskListener;
import hudson.plugins.cobertura.CoberturaBuildAction;
import hudson.tasks.junit.TestResultAction;
import io.jenkins.plugins.pipelinemonitor.model.BuildStatus;
import io.jenkins.plugins.pipelinemonitor.model.CodeCoverage;
import io.jenkins.plugins.pipelinemonitor.model.TestResults;
import io.jenkins.plugins.pipelinemonitor.util.RestClientUtil;
import jenkins.model.Jenkins;

import java.io.PrintStream;

/**
 * Implements {@link RunListener} extension point to provide job status information to subscribers
 * as jobs complete.
 *
 * @author Jeff Pearce (GitHub jeffpearce)
 */
@Extension
public class BuildStatusListener extends RunListener<Run<?, ?>> {

  /**
   * Collect build data and send for notification.
   *
   * @param run the run build
   */
  @Override
  public void onCompleted(final Run<?, ?> run, TaskListener listener) {

    PrintStream errorStream = listener.getLogger();
    PipelineMonitorWriter pmWrite =
        new PipelineMonitorWriter(run, errorStream, listener, run.getCharset());
    pmWrite.write();

    /*
     * final String buildResult; Result result = run.getResult(); if (result == null) { buildResult
     * = "ONGOING"; } else { buildResult = result.toString(); }
     * 
     * BuildStatus build = new BuildStatus();
     * build.setJenkinsUrl(Jenkins.getInstance().getRootUrl());
     * build.setJobName(run.getParent().getName()); build.setNumber(run.getNumber());
     * build.setResult(buildResult); build.setDuration(run.getDuration());
     * 
     * RestClientUtil.postToService("http://10.183.42.147:8080", build);
     * 
     * TestResultAction testResultAction = run.getAction(TestResultAction.class); TestResults
     * testResults = TestResults.fromJUnitTestResults(testResultAction);
     * RestClientUtil.postToService("http://10.183.42.147:8080", testResults);
     * 
     * CoberturaBuildAction coberturaAction = run.getAction(CoberturaBuildAction.class);
     * CodeCoverage codeCoverage = CodeCoverage.fromCobertura(coberturaAction);
     * RestClientUtil.postToService("http://10.183.42.147:8080", codeCoverage);
     */
  }

}
