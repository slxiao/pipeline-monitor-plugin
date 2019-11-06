/*
 * The MIT License
 *
 * Copyright 2014 Rusty Gerard and Liam Newman
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.jenkins.plugins.pipelinemonitor;


import hudson.model.AbstractBuild;
import hudson.model.TaskListener;
import hudson.model.Run;
import jenkins.model.Jenkins;
import io.jenkins.plugins.pipelinemonitor.persistence.BuildData;
import io.jenkins.plugins.pipelinemonitor.persistence.RemoteServerDao;
import net.sf.json.JSONObject;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * A writer that wraps all PipelineMonitor DAOs. Handles error reporting and per build connection
 * state. Each call to write sends a PipelineMonitor payload to the DAO. If any write fails, writer
 * will not attempt to send any further messages to PipelineMonitor during this build.
 *
 * @author Rusty Gerard
 * @author Liam Newman
 * @since 1.0.5
 */
public class PipelineMonitorWriter {

  private final OutputStream errorStream;
  private final Run<?, ?> build;
  private final TaskListener listener;
  private final BuildData buildData;
  private final RemoteServerDao dao;
  private boolean connectionBroken;
  private Charset charset;

  public PipelineMonitorWriter(Run<?, ?> run, OutputStream error, TaskListener listener,
      Charset charset) {
    this.errorStream = error != null ? error : System.err;
    this.build = run;
    this.listener = listener;
    this.charset = charset;
    this.dao = this.getDaoOrNull();
    if (this.dao == null) {
      this.buildData = null;
    } else {
      this.buildData = getBuildData();
    }
  }

  public boolean isConnectionBroken() {
    return connectionBroken || build == null || dao == null || buildData == null;
  }

  BuildData getBuildData() {
    if (build instanceof AbstractBuild) {
      return new BuildData((AbstractBuild<?, ?>) build, new Date(), listener);
    } else {
      return new BuildData(build, new Date(), listener);
    }
  }

  String getJenkinsUrl() {
    return Jenkins.getInstance().getRootUrl();
  }

  public void write() {
    JSONObject payload = dao.buildPayload(buildData);
    try {
      dao.push(payload.toString());
    } catch (IOException e) {
      String msg = "[pipeline-monitor-plugin]: Failed to send log data: " + dao.getDescription()
          + ".\n" + "[pipeline-monitor-plugin]: No Further logs will be sent to "
          + dao.getDescription() + ".\n" + ExceptionUtils.getStackTrace(e);
      logErrorMessage(msg);
    }
  }

  private RemoteServerDao getDaoOrNull() {
    try {
      RemoteServerDao dao = PipelineMonitorConfiguration.getInstance().getRemoteInstance();
      if (dao == null) {
        logErrorMessage(
            "[pipeline-monitor-plugin]: Unable to instantiate RemoteServerDao with current configuration.\n");
      }
      return dao;
    } catch (IllegalArgumentException e) {
      String msg = ExceptionUtils.getMessage(e) + "\n"
          + "[pipeline-monitor-plugin]: Unable to instantiate RemoteServerDao with current configuration.\n";

      logErrorMessage(msg);
    }
    return null;
  }

  /**
   * Write error message to errorStream and set connectionBroken to true.
   */
  private void logErrorMessage(String msg) {
    try {
      connectionBroken = true;
      errorStream.write(msg.getBytes(charset));
      errorStream.flush();
    } catch (IOException ex) {
      // This should never happen, but if it does we just have to let it go.
      ex.printStackTrace();
    }
  }

}
