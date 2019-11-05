package io.jenkins.plugins.pipelinemonitor;

import org.junit.*;

import static org.mockito.Mockito.*;

import org.jenkinsci.plugins.workflow.actions.ErrorAction;
import org.jenkinsci.plugins.workflow.cps.nodes.StepStartNode;
import org.jenkinsci.plugins.workflow.actions.TagsAction;
import io.jenkins.plugins.pipelinemonitor.StageStatusListener;

import static org.junit.Assert.assertEquals;

public class StageStatusListenerTest {
  @Test
  public void testBuildStateForStageWithNullTag() {
    StepStartNode flowNodeMock = mock(StepStartNode.class);
    // ErrorAction errorActionMock = mock(ErrorAction.class);
    when(flowNodeMock.getAction(TagsAction.class)).thenReturn(null);

    String state = StageStatusListener.buildStateForStage(flowNodeMock, null);
    assertEquals(state, "CompletedSuccess");
  }


}
