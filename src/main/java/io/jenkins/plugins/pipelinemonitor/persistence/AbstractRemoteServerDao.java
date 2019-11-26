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

import java.util.Calendar;

import net.sf.json.JSONObject;

import org.apache.commons.lang.time.FastDateFormat;

import io.jenkins.plugins.pipelinemonitor.util.JsonUtil;

/**
 * Abstract data access object for Logstash indexers.
 *
 * @author Rusty Gerard
 * @since 1.0.0
 */
public abstract class AbstractRemoteServerDao implements RemoteServerDao {

  @Override
  public JSONObject buildPayload(BuildData buildData, Object object) {
    JSONObject payload = new JSONObject();
    payload.put("build", buildData.toJson());
    if (object != null) {
      payload.put(object.getClass().getName().split(".")[-1], JsonUtil.convertToJson(object));
    }
    payload.put("source", "jenkins");
    payload.put("@timestamp", FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        .format(Calendar.getInstance().getTime()));
    payload.put("@version", 1);
    return payload;
  }

}
