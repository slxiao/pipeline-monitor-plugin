/*
 * The MIT License
 *
 * Copyright 2014 Rusty Gerard
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

import hudson.Extension;
import hudson.tools.ToolDescriptor;
import hudson.tools.ToolProperty;
import hudson.tools.ToolInstallation;

import java.util.List;

import jenkins.model.Jenkins;
import io.jenkins.plugins.pipelinemonitor.persistence.RemoteServerDao.ServerType;

import org.kohsuke.stapler.DataBoundConstructor;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PipelineMonitorInstallation extends ToolInstallation {
  private static final Logger LOGGER =
      Logger.getLogger(PipelineMonitorConfiguration.class.getName());

  private static final long serialVersionUID = -1730780734005293851L;

  @DataBoundConstructor
  public PipelineMonitorInstallation(String name, String home,
      List<? extends ToolProperty<?>> properties) {
    super(name, home, properties);
  }

  public static Descriptor getPipelineMonitorDescriptor() {
    Boolean instanceIsNull = Jenkins.getInstance() == null;
    Boolean descriptorIsNull =
        Jenkins.getInstance().getDescriptor(PipelineMonitorInstallation.class) == null;
    LOGGER.log(Level.INFO, "" + instanceIsNull + descriptorIsNull);

    return (Descriptor) Jenkins.getInstance().getDescriptor(PipelineMonitorInstallation.class);
  }

  @Extension
  public static final class Descriptor extends ToolDescriptor<PipelineMonitorInstallation> {
    private transient ServerType type;
    private transient String host;
    private transient Integer port = -1;
    private transient String username;
    private transient String password;
    private transient String key;

    public Descriptor() {
      super();
      load();
    }

    @Override
    public String getDisplayName() {
      return Messages.DisplayName();
    }

    public ServerType getType() {
      return type;
    }

    public String getHost() {
      return host;
    }

    public Integer getPort() {
      return port;
    }

    public String getUsername() {
      return username;
    }

    public String getPassword() {
      return password;
    }

    public String getKey() {
      return key;
    }
  }
}
