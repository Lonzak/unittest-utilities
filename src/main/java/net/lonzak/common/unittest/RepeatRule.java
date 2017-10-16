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