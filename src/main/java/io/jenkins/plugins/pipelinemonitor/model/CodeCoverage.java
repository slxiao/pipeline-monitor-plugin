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

import hudson.plugins.cobertura.CoberturaBuildAction;
import hudson.plugins.cobertura.Ratio;
import hudson.plugins.cobertura.targets.CoverageMetric;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;


public class CodeCoverage {

  float conditionals;
  float classes;
  float files;
  float lines;
  float methods;
  float packages;
  float instructions;

  /**
   * generate code coverage result from Cobertura report.
   * 
   * @param coberturaAction Cobertura action.
   * @return code coverage result.
   */
  public static CodeCoverage fromCobertura(@Nullable CoberturaBuildAction coberturaAction) {
    if (coberturaAction == null) {
      return null;
    }
    CodeCoverage codeCoverage = new CodeCoverage();
    Map<CoverageMetric, Ratio> results = coberturaAction.getResults();
    codeCoverage.setInstructions(-1f);
    if (results != null) {
      codeCoverage.setConditionals(results.get(CoverageMetric.CONDITIONAL));
      codeCoverage.setClasses(results.get(CoverageMetric.CLASSES));
      codeCoverage.setFiles(results.get(CoverageMetric.FILES));
      codeCoverage.setLines(results.get(CoverageMetric.LINE));
      codeCoverage.setMethods(results.get(CoverageMetric.METHOD));
      codeCoverage.setPackages(results.get(CoverageMetric.PACKAGES));
    }

    return codeCoverage;
  }

  public float getConditionals() {
    return conditionals;
  }

  /**
   * set conditional coverage.
   * 
   * @param coverage conditional coverage rate in Ratio.
   */
  public void setConditionals(@Nullable Ratio coverage) {
    if (coverage != null) {
      conditionals = coverage.getPercentageFloat();
    }
  }

  /**
   * set conditional coverage.
   * 
   * @param conditionals conditional coverage rate in float.
   */
  public void setConditionals(float conditionals) {
    this.conditionals = conditionals;
  }

  public float getFiles() {
    return files;
  }

  /**
   * set file coverage rate.
   * 
   * @param coverage file coverage rate in Ratio.
   */
  public void setFiles(@Nullable Ratio coverage) {
    if (coverage != null) {
      files = coverage.getPercentageFloat();
    }
  }

  /**
   * set file coverage rate.
   * 
   * @param files file coverage rate in float.
   */
  public void setFiles(float files) {
    this.files = files;
  }

  public float getLines() {
    return lines;
  }

  /**
   * set line coverage.
   * 
   * @param coverage line coverage rate in Ratio.
   */
  public void setLines(@Nullable Ratio coverage) {
    if (coverage != null) {
      lines = coverage.getPercentageFloat();
    }
  }

  public void setLines(float lines) {
    this.lines = lines;
  }

  public float getClasses() {
    return classes;
  }

  /**
   * set class coverage rate.
   * 
   * @param coverage class coverage rate in Ratio.
   */
  public void setClasses(@Nullable Ratio coverage) {
    if (coverage != null) {
      classes = coverage.getPercentageFloat();
    }
  }

  public void setClasses(float classes) {
    this.classes = classes;
  }

  public float getMethods() {
    return methods;
  }

  /**
   * set method coverage rate.
   * 
   * @param coverage method coverage rate in Ratio.
   */
  public void setMethods(@Nullable Ratio coverage) {
    if (coverage != null) {
      methods = coverage.getPercentageFloat();
    }
  }

  public void setMethods(float methods) {
    this.methods = methods;
  }

  public float getPackages() {
    return packages;
  }

  /**
   * set package coverage rate.
   * 
   * @param coverage package coverage rate in Ratio.
   */
  public void setPackages(@Nullable Ratio coverage) {
    if (coverage != null) {
      packages = coverage.getPercentageFloat();
    }
  }

  public void setPackages(float packages) {
    this.packages = packages;
  }

  public float getInstructions() {
    return instructions;
  }

  public void setInstructions(float instructions) {
    this.instructions = instructions;
  }
}
