package io.jenkins.plugins.pipelinemonitor.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;


public class JsonUtil {

  private static final Logger LOGGER = Logger.getLogger(JsonUtil.class.getName());

  protected JsonUtil() {
    throw new IllegalAccessError("Utility class");
  }

  /**
   * covert object to json.
   * 
   * @param object object.
   * @return json.
   */
  public static JSONObject convertToJson(Object object) {
    ObjectMapper mapper = new ObjectMapper();
    String convertedJson = "{}";
    try {
      convertedJson = mapper.writeValueAsString(object);
      return JSONObject.fromObject(convertedJson);
    } catch (JsonProcessingException e) {
      LOGGER.log(Level.WARNING, "Json conversion failed for object " + object, e);
    }
    return JSONObject.fromObject(convertedJson);
  }

}
