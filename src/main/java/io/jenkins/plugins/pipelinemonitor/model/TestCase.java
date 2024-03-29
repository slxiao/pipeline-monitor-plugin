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

package io.jenkins.plugins.pipelinemonitor.model;

import hudson.tasks.junit.CaseResult;

import java.util.Objects;


public class TestCase {

  public enum TestCaseResult {
    Passed, Skipped, Failed
  }

  private String name;
  private boolean failed;
  private boolean passed;
  private boolean skipped;

  private TestCaseResult result;

  /**
   * get test case result from Junit CaseResult.
   * 
   * @param caseResult Junit case result.
   * @return test case result.
   */
  public static TestCase fromCaseResult(CaseResult caseResult) {
    TestCase testCase = new TestCase();

    if (caseResult.isPassed()) {
      testCase.setPassed(true);
    }
    if (caseResult.isSkipped()) {
      testCase.setSkipped(true);
    }
    if (caseResult.isFailed()) {
      testCase.setFailed(true);
    }
    testCase.setName(caseResult.getFullName());
    return testCase;

  }

  public boolean isPassed() {
    return passed;
  }

  /**
   * set case as passed or not.
   * 
   * @param passed passed or not
   */
  public void setPassed(boolean passed) {
    this.passed = passed;
    if (this.passed) {
      result = TestCaseResult.Passed;
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isFailed() {
    return failed;
  }

  /**
   * set case as failed or not.
   * 
   * @param failed failed or not.
   */
  public void setFailed(boolean failed) {
    this.failed = failed;
    if (this.failed) {
      result = TestCaseResult.Failed;
    }
  }

  public boolean isSkipped() {
    return skipped;
  }

  /**
   * set case as skipped or not.
   * 
   * @param skipped skipped or not.
   */
  public void setSkipped(boolean skipped) {
    this.skipped = skipped;
    if (this.skipped) {
      result = TestCaseResult.Skipped;
    }
  }

  public int getPassedCount() {
    return passed ? 1 : 0;
  }

  public int getSkippedCount() {
    return skipped ? 1 : 0;
  }

  public int getFailedCount() {
    return failed ? 1 : 0;
  }

  public TestCaseResult getResult() {
    return result;
  }

}
