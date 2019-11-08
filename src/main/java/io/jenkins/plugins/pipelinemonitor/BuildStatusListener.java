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
import jenkins.model.Jenkins;

import java.io.PrintStream;

import java.util.logging.Level;
import java.util.logging.Logger;

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
    PipelineMonitorConfiguration configuration = PipelineMonitorConfiguration.getInstance();
    if (!configuration.isEnabled()) {
      log(Level.WARNING, "pipeline monitor plugin disabled!");
      return;
    }

    log(Level.INFO, "start pipeline monitor writer");

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

  /**
   * Prints to stdout or stderr.
   *
   * @param level  INFO/WARNING/ERROR
   * @param format String that formats the log
   * @param args   arguments for the formated log string
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
    return Logger.getLogger(BuildStatusListener.class.getName());
  }

}
