package io.jenkins.plugins.pipelinemonitor.util;

import io.jenkins.plugins.pipelinemonitor.model.BuildStatus;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertTrue;

import net.sf.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.Mockito.*;


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

    JSONObject jsonObject = JsonUtil.convertToJson(testObject);

    assertTrue(jsonObject != null);
  }

  @Test(expected = IllegalAccessError.class)
  public void testIllegalAccessError() {

    JsonUtil jsonUtil = new JsonUtil();

  }

  @Test
  public void TestJsonProcesingException() throws JsonProcessingException {
    ObjectMapper mockedObjectWrapper = mock(ObjectMapper.class);
    Object object = new Object();
    when(mockedObjectWrapper.writeValueAsString(object))
        .thenThrow(new JsonProcessingException("Error") {
        });
    JSONObject jsonObject = JsonUtil.convertToJson(object);

  }

}
