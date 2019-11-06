package io.jenkins.plugins.pipelinemonitor.configuration;

import javax.annotation.Nonnull;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import hudson.DescriptorExtensionList;
import hudson.ExtensionPoint;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.model.ReconfigurableDescribable;
import hudson.model.Descriptor.FormException;
import hudson.util.FormValidation;
import jenkins.model.Jenkins;
import io.jenkins.plugins.pipelinemonitor.persistence.AbstractRemoteServerDao;
import net.sf.json.JSONObject;

/**
 * Extension point for logstash indexers. This extension point provides the configuration for the
 * indexer. You also have to implement the actual indexer in a separate class extending
 * {@link AbstractRemoteServerDao}.
 *
 * @param <T> The class implementing the push to the indexer
 */
public abstract class RemoteServer<T extends AbstractRemoteServerDao>
    extends AbstractDescribableImpl<RemoteServer<?>>
    implements ExtensionPoint, ReconfigurableDescribable<RemoteServer<?>> {
  protected transient T instance;

  /**
   * Gets the instance of the actual {@link AbstractRemoteServerDao} that is represented by this
   * configuration.
   *
   * @return {@link AbstractRemoteServerDao} instance
   */
  @Nonnull
  public synchronized T getInstance() {
    if (instance == null) {
      instance = createIndexerInstance();
    }
    return instance;
  }

  /**
   * Purpose of this method is to validate the inputs (if required) and if found erroneous throw an
   * exception so that it will be bubbled up to the UI.
   *
   * @throws Exception
   */
  public void validate() throws Exception {
  }



  /**
   * Creates a new {@link AbstractRemoteServerDao} instance corresponding to this configuration.
   *
   * @return {@link AbstractRemoteServerDao} instance
   */
  protected abstract T createIndexerInstance();


  @SuppressWarnings("unchecked")
  public static DescriptorExtensionList<RemoteServer<?>, Descriptor<RemoteServer<?>>> all() {
    return (DescriptorExtensionList<RemoteServer<?>, Descriptor<RemoteServer<?>>>) Jenkins
        .getInstance().getDescriptorList(RemoteServer.class);
  }

  public static abstract class RemoteServerDescriptor extends Descriptor<RemoteServer<?>> {
    /*
     * Form validation methods
     */
    public FormValidation doCheckPort(@QueryParameter("value") String value) {
      try {
        Integer.parseInt(value);
      } catch (NumberFormatException e) {
        return FormValidation.error("error!");
      }

      return FormValidation.ok();
    }

    public FormValidation doCheckHost(@QueryParameter("value") String value) {
      if (StringUtils.isBlank(value)) {
        return FormValidation.warning("error!");
      }

      return FormValidation.ok();
    }

    public abstract int getDefaultPort();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteServer<T> reconfigure(StaplerRequest req, JSONObject form) throws FormException {
    req.bindJSON(this, form);
    return this;
  }
}
