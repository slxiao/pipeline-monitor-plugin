package io.jenkins.plugins.pipelinemonitor.model;

import hudson.plugins.cobertura.CoberturaBuildAction;
import hudson.plugins.cobertura.Ratio;
import hudson.plugins.cobertura.targets.CoverageMetric;
import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CoberturaBuildAction.class})
public class CodeCoverageTest {
  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testFromCoberturaNull() {
    assertNull(CodeCoverage.fromCobertura(null));
  }

  @Test
  public void testFromCobertura() {
    CoberturaBuildAction action = PowerMockito.mock(CoberturaBuildAction.class);
    Map<CoverageMetric, Ratio> results = new HashMap<>();
    results.put(CoverageMetric.CONDITIONAL, Ratio.create(1, 10));
    results.put(CoverageMetric.CLASSES, Ratio.create(2, 10));
    results.put(CoverageMetric.FILES, Ratio.create(3, 10));
    results.put(CoverageMetric.LINE, Ratio.create(4, 10));
    results.put(CoverageMetric.METHOD, Ratio.create(5, 10));
    results.put(CoverageMetric.PACKAGES, Ratio.create(6, 10));

    when(action.getResults()).thenReturn(results);

    CodeCoverage coverage = CodeCoverage.fromCobertura(action);
    assertNotNull(coverage);
    assertEquals(10, coverage.getConditionals(), 0);
    assertEquals(20, coverage.getClasses(), 0);
    assertEquals(30, coverage.getFiles(), 0);
    assertEquals(40, coverage.getLines(), 0);
    assertEquals(50, coverage.getMethods(), 0);
    assertEquals(60, coverage.getPackages(), 0);
    assertEquals(-1f, coverage.getInstructions(), 0);

  }

}
