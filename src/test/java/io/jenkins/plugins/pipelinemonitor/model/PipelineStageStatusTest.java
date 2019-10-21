package io.jenkins.plugins.pipelinemonitor.model;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.Date;


public class PipelineStageStatusTest {

  public PipelineStageStatusTest() {
  }

  @Test
  public void testDefaultConstructor() {
    PipelineStageStatus status = new PipelineStageStatus();
    assertEquals(status.getJenkinsUrl(), "");
    assertEquals(status.getJobName(), "");
    assertEquals(status.getNumber(), 0);
    assertTrue(status.getStartTime() != null);
    assertEquals(status.getResult(), "");
    assertEquals(status.getDuration(), 0);
  }


  @Test
  public void testConstructor() {
    Date date = mock(Date.class);
    when(date.getTime()).thenReturn((long) 10);
    PipelineStageStatus status = new PipelineStageStatus("", "", 0, "", date, "", 0);
    assertEquals(status.getJenkinsUrl(), "");
    assertEquals(status.getJobName(), "");
    assertEquals(status.getNumber(), 0);
    assertEquals(status.getName(), "");
    assertTrue(status.getStartTime() != null);
    assertEquals(status.getResult(), "");
    assertEquals(status.getDuration(), 0);
    status.setJenkinsUrl("foo");
    status.setJobName("foo");
    status.setNumber(1);
    status.setName("foo");
    status.setStartTime(date);
    status.setResult("foo");
    status.setDuration(1);
    assertEquals(status.getJenkinsUrl(), "foo");
    assertEquals(status.getJobName(), "foo");
    assertEquals(status.getNumber(), 1);
    assertTrue(status.getStartTime() != null);
    assertEquals(status.getResult(), "foo");
    assertEquals(status.getDuration(), 1);
  }
}
