package io.jenkins.plugins.pipelinemonitor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.CheckForNull;

import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.kohsuke.stapler.StaplerRequest;

import org.apache.http.client.utils.URIBuilder;
import hudson.Extension;
import hudson.init.InitMilestone;
import hudson.init.Initializer;
import jenkins.model.GlobalConfiguration;

import io.jenkins.plugins.pipelinemonitor.configuration.ElasticSearch;
import io.jenkins.plugins.pipelinemonitor.configuration.RemoteServer;

import io.jenkins.plugins.pipelinemonitor.persistence.RemoteServerDao;

import net.sf.json.JSONObject;

import io.jenkins.plugins.pipelinemonitor.PipelineMonitorInstallation.Descriptor;
import io.jenkins.plugins.pipelinemonitor.persistence.RemoteServerDao.ServerType;

@Extension
public class PipelineMonitorConfiguration extends GlobalConfiguration {
  private static final Logger LOGGER =
      Logger.getLogger(PipelineMonitorConfiguration.class.getName());

  private RemoteServer<?> remoteServer;
  private Boolean enabled;
  private boolean dataMigrated = false;
  private boolean enableGlobally = false;
  private transient RemoteServer<?> activeServer;

  public PipelineMonitorConfiguration() {
    // load();
    if (enabled == null) {
      if (remoteServer == null) {
        enabled = false;
      } else {
        enabled = true;
      }
    }
    activeServer = remoteServer;
  }

  public boolean isEnabled() {
    return enabled == null ? false : enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isEnableGlobally() {
    return enableGlobally;
  }

  public void setEnableGlobally(boolean enableGlobally) {
    this.enableGlobally = enableGlobally;
  }

  public RemoteServer<?> getRemoteServer() {
    return remoteServer;
  }

  public void setRemoteServer(RemoteServer<?> remoteServer) {
    this.remoteServer = remoteServer;
  }

  @CheckForNull
  public RemoteServerDao getRemoteInstance() {
    if (activeServer != null) {
      return activeServer.getInstance();
    }
    return null;
  }


  // for testing only
  @Restricted(NoExternalUse.class)
  void setActiveServer(RemoteServer<?> activeServer) {
    this.activeServer = activeServer;
  }

  public List<?> getServerTypes() {
    return RemoteServer.all();
  }

  @SuppressWarnings("deprecation")
  @Initializer(after = InitMilestone.JOB_LOADED)
  public void migrateData() {
    LOGGER.log(Level.INFO, "migrateData......" + dataMigrated);
    if (!dataMigrated) {
      Descriptor descriptor = PipelineMonitorInstallation.getPipelineMonitorDescriptor();
      LOGGER.log(Level.INFO, "descriptor......" + descriptor);
      if (descriptor != null && descriptor.getType() != null) {
        ServerType type = descriptor.getType();
        switch (type) {
          case ELASTICSEARCH:
            LOGGER.log(Level.INFO, "Migrating pipeline monitor configuration for Elastic Search");
            URI uri;
            try {
              uri = (new URIBuilder(descriptor.getHost())).setPort(descriptor.getPort())
                  .setPath("/" + descriptor.getKey()).build();
              ElasticSearch es = new ElasticSearch();
              es.setUri(uri);
              es.setUsername(descriptor.getUsername());
              es.setPassword(descriptor.getPassword());
              remoteServer = es;
              enabled = true;
            } catch (URISyntaxException e) {
              enabled = false;
              LOGGER.log(Level.INFO,
                  "Migrating pipeline monitor configuration for Elastic Search failed: "
                      + e.toString());
            }
            break;
          default:
            LOGGER.log(Level.INFO, "descriptor is null or descriptor type is null");
            enabled = false;
            break;
        }
        activeServer = remoteServer;
      }
      LOGGER.log(Level.INFO, "descriptor is null or descriptor type is null");
      dataMigrated = true;
      save();
    }
  }

  @Override
  public boolean configure(StaplerRequest staplerRequest, JSONObject json) throws FormException {

    // when we bind the stapler request we get a new instance of remoteServer.
    // remoteServer is holder for the dao instance.
    // To avoid that we get a new dao instance in case there was no change in configuration
    // we compare it to the currently active configuration.
    staplerRequest.bindJSON(this, json);

    try {
      // validate
      remoteServer.validate();
    } catch (Exception ex) {
      // You are here which means user is trying to save invalid indexer configuration.
      // Exception will be thrown here so that it gets displayed on UI.
      // But before that revert back to original configuration (in-memory)
      // so that when user refreshes the configuration page, last saved settings will be displayed
      // again.
      remoteServer = activeServer;
      throw new IllegalArgumentException(ex);
    }

    if (!Objects.equals(remoteServer, activeServer)) {
      activeServer = remoteServer;
    }

    save();
    return true;
  }

  public static PipelineMonitorConfiguration getInstance() {
    return GlobalConfiguration.all().get(PipelineMonitorConfiguration.class);
  }
}
