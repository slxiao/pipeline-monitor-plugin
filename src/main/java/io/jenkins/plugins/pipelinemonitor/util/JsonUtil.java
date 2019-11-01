package io.jenkins.plugins.pipelinemonitor.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by hthakkallapally on 3/17/2015.
 */
public class JsonUtil {

  private static final Logger LOGGER = Logger.getLogger(RestClientUtil.class.getName());

  protected JsonUtil() {
    throw new IllegalAccessError("Utility class");
  }

  /**
   * covert object to json.
   * 
   * @param object object.
   * @return json.
   */
  public static String convertToJson(Object object) {
    ObjectMapper mapper = new ObjectMapper();
    String convertedJson = "";
    try {
      convertedJson = mapper.writeValueAsString(object);
      return convertedJson;
    } catch (JsonProcessingException e) {
      LOGGER.log(Level.WARNING, "Json conversion failed for object " + object, e);
    }
    return convertedJson;
  }

  /**
   * convert build failure to map.
   * 
   * @param jsonObject json build failure object.
   * @return map failure result.
   */
  public static Map<String, Object> convertBuildFailureToMap(JSONObject jsonObject) {
    Map<String, Object> map = new HashMap<>();
    Iterator<?> keys = jsonObject.keys();

    while (keys.hasNext()) {
      String key = (String) keys.next();
      if ("categories".equals(key)) {
        List<String> value = convertJsonArrayToList(jsonObject.getJSONArray(key));
        map.put(key, value);
      } else {
        String value = jsonObject.getString(key);
        map.put(key, value);
      }
    }
    return map;
  }

  /**
   * convert json array to list.
   * 
   * @param jsonArray json array.
   * @return list.
   */
  public static List<String> convertJsonArrayToList(JSONArray jsonArray) {
    List<String> listdata = new ArrayList<>();
    if (jsonArray != null) {
      for (int i = 0; i < jsonArray.length(); i++) {
        listdata.add(jsonArray.get(i).toString());
      }
    }
    return listdata;
  }
}
