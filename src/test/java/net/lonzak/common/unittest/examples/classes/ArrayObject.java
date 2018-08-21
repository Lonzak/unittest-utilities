package net.lonzak.common.unittest.examples.classes;

import java.util.Arrays;

public class ArrayObject {

  private int[] iArray;
  private long[] lArray;


  public ArrayObject(int[] iArray, long[] lArray) {
    super();
    this.iArray = iArray;
    this.lArray = lArray;
  }

  public ArrayObject(int[] iArray) {
    super();
    this.iArray = iArray;
    this.lArray = new long[0];
  }

  public ArrayObject(long[] lArray) {
    super();
    this.lArray = lArray;
    this.iArray = new int[0];
  }

  public int[] getiArray() {
    return this.iArray;
  }

  public void setiArray(int[] iArray) {
    this.iArray = iArray;
  }

  public long[] getlArray() {
    return this.lArray;
  }

  public void setlArray(long[] lArray) {
    this.lArray = lArray;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("ArrayObject [");
    if (this.iArray != null) {
      builder.append("iArray=");
      builder.append(Arrays.toString(this.iArray));
      builder.append(", ");
    }
    if (this.lArray != null) {
      builder.append("lArray=");
      builder.append(Arrays.toString(this.lArray));
    }
    builder.append("]");
    return builder.toString();
  }



}
