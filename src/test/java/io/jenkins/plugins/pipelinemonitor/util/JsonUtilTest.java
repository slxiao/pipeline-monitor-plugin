package io.jenkins.plugins.pipelinemonitor.util;

import io.jenkins.plugins.pipelinemonitor.model.BuildStatus;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by mcharron on 2016-06-27.
 */
public class JsonUtilTest {

  private BuildStatus testObject = new BuildStatus();

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Test
  public void givenObject_whenToJson_thenReturnJson() {
    testObject.setJenkinsUrl("http://test.com");
    testObject.setJobName("blop");
    testObject.setNumber(123);
    String expectedJson = "{\"jenkinsUrl\":\"http://test.com\",\"jobName\":\"blop\",\"number\":123";
    String jsonString = JsonUtil.convertToJson(testObject);

    assertTrue(String.format("json String %s does not contain %s", jsonString, expectedJson),
        jsonString.startsWith(expectedJson));
  }

  @Test
  public void givenInvalidObject_whenToJson_thenReturnEmptyJson() {
    JSONObject object = new JSONObject();
    String json = JsonUtil.convertToJson(object);
    assertEquals("", json);
  }

  @Test
  public void givenJsonArray_whenConvertToList_thenReturnList() {
    JSONArray array = new JSONArray();
    array.put("test");
    List<String> result = JsonUtil.convertJsonArrayToList(array);
    assertEquals(1, result.size());
    assertEquals("test", result.get(0));
  }

  @Test
  public void givenNull_whenConvertToList_thenReturnEmptyList() {
    List<String> result = JsonUtil.convertJsonArrayToList(null);
    assertEquals(0, result.size());
  }

  @Test
  public void givenJsonObjectWithCategories_whenConvertBuildFailureToMap_thenReturnValidMap() {
    JSONArray array = new JSONArray();
    array.put("test");
    JSONObject object = new JSONObject();
    object.put("categories", array);
    Map<String, Object> result = JsonUtil.convertBuildFailureToMap(object);
    assertEquals(1, result.size());
    assertEquals(1, ((List) result.get("categories")).size());
    assertEquals("test", ((List) result.get("categories")).get(0));
  }

  @Test
  public void givenJsonObjectWithoutCategories_whenConvertBuildFailureToMap_thenReturnValidMap() {
    JSONObject object = new JSONObject();
    object.put("stuff", "testString");
    Map<String, Object> result = JsonUtil.convertBuildFailureToMap(object);
    assertEquals(1, result.size());
    assertEquals("testString", result.get("stuff"));
  }

  @Test(expected = IllegalAccessError.class)
  public void givenProtectedConstructor_whenNew_throwIllegalAccess() {
    new JsonUtil();
  }
}
