/*
 * =========================================== unittest-utilities ===========================================
 *
 * Project Info: https://github.com/Lonzak/unittest-utilities
 * 
 * (C) Copyright 2012-2018 nepatec GmbH
 *
 * This file is part of unittest-utilities
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details. You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package net.lonzak.common.unittest.examples.dtos;

import net.lonzak.common.unittest.examples.enums.ClassOfColor;

/**
 * Example for a colored number: Red
 */
public class RedNumber<T> implements ColorNumber {

  private final int type;
  private final ClassOfColor co;
  private final int seqNo;

  public RedNumber(int type, ClassOfColor co, int seqNo) {
    super();
    this.type = type;
    this.co = co;
    this.seqNo = seqNo;
  }

  public RedNumber(int type, String co, int seqNo) {
    super();
    this.type = type;
    this.co = ClassOfColor.H1;
    this.seqNo = seqNo;
  }

  public RedNumber(int type, T co, int seqNo) {
    super();
    this.type = type;
    this.co = ClassOfColor.H1;
    this.seqNo = seqNo;
  }

  /**
   * @param color character String as a number
   */
  public RedNumber(String color) {

    if (color == null || color.length() < 13 || color.length() > 13 || color.contains(" ")) {
      throw new IllegalArgumentException("Invalid color code'" + color + "' given.");
    }

    String tmp = color.substring(0, 2);
    this.type = Integer.parseInt(tmp);
    tmp = color.substring(2, 5);
    this.co = ClassOfColor.map(Integer.parseInt(tmp));
    tmp = color.substring(5, 10);
    this.seqNo = Integer.parseInt(tmp);
    tmp = color.substring(10, 12);
  }

  /**
   * @return int
   */
  public int getType() {
    return this.type;
  }

  /**
   * @return ClassOfColor
   */
  public ClassOfColor getCo() {
    return this.co;
  }

  /**
   * @return int
   */
  public int getSeqNo() {
    return this.seqNo;
  }

  public String getColor() {
    return String.format("%1$02d%2$03d%3$05d%4$02d%5$01d", this.type, this.co.getNumValue(), this.seqNo);
  }

  @Override
  public String toFormattedString() {
    return String.format("%1$02d-%2$03d-%3$05d", this.type, this.co.getNumValue(), this.seqNo);
  }

  @Override
  public String toIdentificationString() {
    StringBuilder builder = new StringBuilder();
    builder.append(this.type);
    builder.append(this.co.getNumValue());
    builder.append(this.seqNo);

    return builder.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.co == null) ? 0 : this.co.hashCode());
    result = prime * result + this.seqNo;
    result = prime * result + this.type;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    RedNumber<T> other = (RedNumber<T>) obj;
    if (this.co != other.co) {
      return false;
    }
    if (this.seqNo != other.seqNo) {
      return false;
    }
    if (this.type != other.type) {
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Red [getFormatted()=" + this.toFormattedString() + "]";
  }
}
