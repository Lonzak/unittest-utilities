package net.lonzak.common.unittest.examples.classes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import net.lonzak.common.unittest.examples.enums.LineOfColor;

public class Constructor {

  public Constructor() {}

  public Constructor(int i) {
    if (i != 1) {
      throw new IllegalArgumentException("Wrong int constructor called: " + i);
    }
  }

  public Constructor(Integer i) {
    if (i != -1) {
      throw new IllegalArgumentException("Wrong Integer constructor called: " + i);
    }
  }

  public Constructor(long i) {
    if (i != 10000000000L) {
      throw new IllegalArgumentException("Wrong long constructor called: " + i);
    }
  }

  public Constructor(Long i) {
    if (i.longValue() != -10000000000L) {
      throw new IllegalArgumentException("Wrong Long constructor called: " + i);
    }
  }

  public Constructor(float i) {
    if (i - 2 > 0.00001f) {
      throw new IllegalArgumentException("Wrong float constructor called: " + i);
    }
  }

  public Constructor(Float i) {
    if (i.floatValue() - 3 > 0.00001f) {
      throw new IllegalArgumentException("Wrong Float constructor called: " + i);
    }
  }

  public Constructor(double i) {
    if (i - 10 > 0.00001d) {
      throw new IllegalArgumentException("Wrong double constructor called: " + i);
    }
  }

  public Constructor(Double i) {
    if (i.doubleValue() - 11 > 0.00001d) {
      throw new IllegalArgumentException("Wrong Double constructor called: " + i);
    }
  }

  public Constructor(short i) {
    if (i != 1) {
      throw new IllegalArgumentException("Wrong short constructor called: " + i);
    }
  }

  public Constructor(Short i) {
    if (i != -1) {
      throw new IllegalArgumentException("Wrong Short constructor called: " + i);
    }
  }

  public Constructor(char i) {
    if (i != 'c') {
      throw new IllegalArgumentException("Wrong char constructor called: " + i);
    }
  }

  public Constructor(Character i) {
    if (i.charValue() != 'C') {
      throw new IllegalArgumentException("Wrong Char constructor called: " + i);
    }
  }

  public Constructor(byte i) {
    if (i > 0) {
      throw new IllegalArgumentException("Wrong byte constructor called: " + i);
    }
  }

  public Constructor(Byte i) {
    if (i < 0) {
      throw new IllegalArgumentException("Wrong Byte constructor called: " + i);
    }
  }

  public Constructor(boolean i) {
    if (i == false) {
      throw new IllegalArgumentException("Wrong boolean constructor called: " + i);
    }
  }

  public Constructor(Boolean i) {
    if (Boolean.FALSE.equals(i)) {
      throw new IllegalArgumentException("Wrong Boolean constructor called: " + i);
    }
  }

  public Constructor(String test) {
    // match not only numbers
    if (!test.matches("(?!^\\d+$)^.+$")) {
      throw new IllegalArgumentException("Wrong String constructor called: " + test);
    }
  }

  public Constructor(int[] i) {
    if (i[0] != 1) {
      throw new IllegalArgumentException("Wrong int constructor called: " + Arrays.toString(i));
    }
  }

  public Constructor(Integer[] i) {
    if (i[0] != -1) {
      throw new IllegalArgumentException("Wrong Integer constructor called: " + Arrays.toString(i));
    }
  }

  public Constructor(long[] i) {
    if (i[0] != 10000000000L) {
      throw new IllegalArgumentException("Wrong long constructor called: " + Arrays.toString(i));
    }
  }

  public Constructor(Long[] i) {
    if (i[0].longValue() != -10000000000L) {
      throw new IllegalArgumentException("Wrong Long constructor called: " + Arrays.toString(i));
    }
  }

  public Constructor(float[] i) {
    if (i[0] - 2 > 0.00001f) {
      throw new IllegalArgumentException("Wrong float constructor called: " + Arrays.toString(i));
    }
  }

  public Constructor(Float[] i) {
    if (i[0].floatValue() - 3 > 0.00001f) {
      throw new IllegalArgumentException("Wrong Float constructor called: " + Arrays.toString(i));
    }
  }

  public Constructor(double[] i) {
    if (i[0] - 10 > 0.00001d) {
      throw new IllegalArgumentException("Wrong double constructor called: " + Arrays.toString(i));
    }
  }

  public Constructor(Double[] i) {
    if (i[0].doubleValue() - 11 > 0.00001d) {
      throw new IllegalArgumentException("Wrong Double constructor called: " + Arrays.toString(i));
    }
  }

  public Constructor(short[] i) {
    if (i[0] != 1) {
      throw new IllegalArgumentException("Wrong short constructor called: " + Arrays.toString(i));
    }
  }

  public Constructor(Short[] i) {
    if (i[0] != -1) {
      throw new IllegalArgumentException("Wrong Short constructor called: " + Arrays.toString(i));
    }
  }

  public Constructor(char[] i) {
    if (i[0] != 'c') {
      throw new IllegalArgumentException("Wrong char constructor called: " + Arrays.toString(i));
    }
  }

  public Constructor(Character[] i) {
    if (i[0].charValue() != 'C') {
      throw new IllegalArgumentException("Wrong Char constructor called: " + Arrays.toString(i));
    }
  }

  public Constructor(byte[] i) {
    if (i[0] > 0) {
      throw new IllegalArgumentException("Wrong byte constructor called: " + Arrays.toString(i));
    }
  }

  public Constructor(Byte[] i) {
    if (i[0] < 0) {
      throw new IllegalArgumentException("Wrong Byte constructor called: " + Arrays.toString(i));
    }
  }

  public Constructor(boolean[] i) {
    if (i[0] == false) {
      throw new IllegalArgumentException("Wrong boolean constructor called: " + Arrays.toString(i));
    }
  }

  public Constructor(Boolean[] i) {
    if (Boolean.FALSE.equals(i[0])) {
      throw new IllegalArgumentException("Wrong Boolean constructor called: " + Arrays.toString(i));
    }
  }

  public Constructor(LineOfColor loc) {
    if (loc != LineOfColor.GRAY) {
      throw new IllegalArgumentException("Wrong Enum constructor called: " + loc);
    }
  }

  public Constructor(ArrayList<Integer> i) {
    if (i.get(0) != -1) {
      throw new IllegalArgumentException("Wrong ArrayList constructor called: " + i);
    }
  }

  public Constructor(HashMap<Long, Boolean> i) {
    if (!i.get(-10000000000L).equals(Boolean.TRUE)) {
      throw new IllegalArgumentException("Wrong HashMap constructor called: " + i);
    }
  }

  public Constructor(BigDecimal b) {
    if (b.compareTo(BigDecimal.valueOf(10)) != 0) {
      throw new IllegalArgumentException("Wrong BigDecimal constructor called: " + b);
    }
  }

}
