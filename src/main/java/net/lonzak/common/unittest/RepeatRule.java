/*
 * ===========================================
 * unittest-utilities
 * ===========================================
 *
 * Project Info:  https://github.com/Lonzak/unittest-utilities
 * 
 * (C) Copyright 2012-2017 nepatec GmbH & Co. KG
 *
 *  This file is part of unittest-utilities
 *
    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.
    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.
    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package net.lonzak.common.unittest;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * This can be used to run junit test serveral times.
 * Usage:
 * 
 * <pre>
 * {@code}
 * &#064;Rule
 * public RepeatRule repeatRule = new RepeatRule();
 * ...
 * &#064;Repeat(100)
 * public void testMethodTest(){...}
 * 
 * </pre>
 * <p>
 * @author 225010
 *
 */
public class RepeatRule implements TestRule {

  private static class RepeatStatement extends Statement {
      private final Statement statement;
      private final int repeat;    

      public RepeatStatement(Statement statement, int repeat) {
          this.statement = statement;
          this.repeat = repeat;
      }

      @Override
      public void evaluate() throws Throwable {
          for (int i = 0; i < this.repeat; i++) {
              this.statement.evaluate();
          }
      }

  }

  /**
   * Here's an example usage:
   *
   * <pre>
   * {@code}
   * &#064;IFaceAnnotation(value="")
   * public interface IFace {
   *
   *     &#064;MethodAnnotation("")
   *     public String Method();
   * }
   * </pre>
   */
  @Override
  public Statement apply(Statement statement, Description description) {
      Statement result = statement;
      Repeat repeat = description.getAnnotation(Repeat.class);
      if (repeat != null) {
          int times = repeat.value();
          result = new RepeatStatement(statement, times);
      }
      return result;
  }
}