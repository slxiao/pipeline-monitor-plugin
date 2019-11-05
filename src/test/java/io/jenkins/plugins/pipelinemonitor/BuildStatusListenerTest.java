/*
 * The MIT License
 *
 * Copyright 2018 jxpearce.
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

package io.jenkins.plugins.pipelinemonitor;

import jenkins.model.Jenkins;
import hudson.model.AbstractBuild;
import hudson.model.Job;
import hudson.model.Result;
import hudson.model.TaskListener;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import org.powermock.core.classloader.annotations.PrepareForTest;

import io.jenkins.plugins.pipelinemonitor.model.BuildStatus;
import org.mockito.Mock;
import io.jenkins.plugins.pipelinemonitor.util.RestClientUtil;

import hudson.tasks.junit.TestResultAction;
import hudson.plugins.cobertura.CoberturaBuildAction;

import io.jenkins.plugins.pipelinemonitor.model.CodeCoverage;
import io.jenkins.plugins.pipelinemonitor.model.TestResults;


/**
 *
 * @author Jeff Pearce (GitHub jeffpearce)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RestClientUtil.class, Jenkins.class, TestResults.class, CodeCoverage.class})
public class BuildStatusListenerTest {
  @Mock
  Jenkins jenkinsMock;

  public BuildStatusListenerTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
    PowerMockito.mockStatic(Jenkins.class);
    PowerMockito.mockStatic(RestClientUtil.class);
    PowerMockito.mockStatic(TestResults.class);
    PowerMockito.mockStatic(CodeCoverage.class);
  }

  @After
  public void tearDown() {
  }

  /**
   * Verifies BuildStatusJobListener onCompleted can be called when there's no build action
   */
  @Test
  public void testOnCompletedNoBuildAction() throws Exception {

    AbstractBuild run = Mockito.mock(AbstractBuild.class);
    Mockito.when(run.getResult()).thenReturn(null);
    Mockito.when(run.getNumber()).thenReturn(1);
    Mockito.when(run.getDuration()).thenReturn((long) 100);

    Job job = Mockito.mock(Job.class);
    Mockito.when(run.getParent()).thenReturn(job);
    Mockito.when(job.getName()).thenReturn("test");

    Mockito.when(Jenkins.getInstance()).thenReturn(jenkinsMock);
    Mockito.when(jenkinsMock.getRootUrl()).thenReturn("http://test.cn");

    Mockito.when(run.getAction(TestResultAction.class)).thenReturn(null);
    Mockito.when(run.getAction(CoberturaBuildAction.class)).thenReturn(null);

    BuildStatus buildStatusMock = Mockito.mock(BuildStatus.class);
    Mockito.doNothing().when(buildStatusMock).setJenkinsUrl(Mockito.anyString());
    Mockito.doNothing().when(buildStatusMock).setJobName(Mockito.anyString());
    Mockito.doNothing().when(buildStatusMock).setNumber(Mockito.anyInt());
    Mockito.doNothing().when(buildStatusMock).setResult(Mockito.anyString());
    Mockito.doNothing().when(buildStatusMock).setDuration(Mockito.anyLong());

    PowerMockito.when(TestResults.class, "fromJUnitTestResults", Mockito.any())
        .thenReturn(Mockito.mock(TestResults.class));

    PowerMockito.when(CodeCoverage.class, "fromCobertura", Mockito.any())
        .thenReturn(Mockito.mock(CodeCoverage.class));

    PowerMockito.doNothing().when(RestClientUtil.class, "postToService", Mockito.anyString(),
        Mockito.any());

    BuildStatusListener instance = new BuildStatusListener();
    instance.onFinalized(run);

  }
}
