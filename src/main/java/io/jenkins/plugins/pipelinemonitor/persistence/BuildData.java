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

package io.jenkins.plugins.pipelinemonitor.persistence;

import hudson.model.AbstractBuild;
import hudson.model.TaskListener;
import hudson.model.Run;

import java.util.Date;

import net.sf.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * POJO for mapping build info to JSON.
 *
 * @author Shelin Xiao
 * @since 1.0.0
 */
public class BuildData {

  private String url;

  // Freestyle project build
  public BuildData(AbstractBuild<?, ?> build, Date currentTime) {
    url = build.getUrl();
  }

  // Pipeline project build
  public BuildData(Run<?, ?> build, Date currentTime) {
    url = build.getUrl();
  }

  @Override
  public String toString() {
    Gson gson = new GsonBuilder().create();
    return gson.toJson(this);
  }

  public JSONObject toJson() {
    String data = toString();
    return JSONObject.fromObject(data);
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
