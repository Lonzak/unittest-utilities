package net.lonzak.examples.classes;

import java.util.ArrayList;
import net.lonzak.examples.dtos.RedNumber;
import net.lonzak.examples.enums.LineOfColor;

public class Constructor2 {

  public Constructor2() {}

  public Constructor2(int i, Double d, byte[] array, String string, ArrayList<Character> list, LineOfColor color,
      RedNumber<Integer> number) {
    if (i != 1) {
      throw new IllegalArgumentException("Wrong int parameter called: " + i);
    }
    if (d - 2 > 0.00001) {
      throw new IllegalArgumentException("Wrong double parameter called: " + d);
    }
    if (!new String(array).equals("byteArrayXY")) {
      throw new IllegalArgumentException("Wrong byte[] parameter called: " + i);
    }
    if (!string.equals("StringXY")) {
      throw new IllegalArgumentException("Wrong String parameter called: " + i);
    }
    if (!list.get(0).equals(new Character('*'))) {
      throw new IllegalArgumentException("Wrong ArrayList<Character> parameter called: " + i);
    }
    if (color != LineOfColor.SEPIA) {
      throw new IllegalArgumentException("Wrong LineOfColor enum parameter called: " + i);
    }
    if (!number.equals(new RedNumber<>(1, 12, 1))) {
      throw new IllegalArgumentException("Wrong RedNumber parameter called: " + i);
    }
  }
}
