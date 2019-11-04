package io.jenkins.plugins.pipelinemonitor.util;

import com.mashape.unirest.http.Unirest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Unirest.class})
public class RestClientUtilTest {

  @Before
  public void setup() {
    mockStatic(Unirest.class);
  }

  @Test
  public void whenRestApiFails_thenShouldContinue() throws Exception {
    when(Unirest.post(anyString())).thenThrow(new IllegalArgumentException("Some error occurred"));

    try {
      RestClientUtil.postToService("http://foo", new String("bar"));
    } catch (Throwable e) {
      fail("Should not have thrown any exception here");
    }
  }
}
