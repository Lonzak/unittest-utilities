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
package net.lonzak.common.unittest.examples.enums;

/**
 * Some Enum
 */
public enum ClassOfColor {
  H1(100, LineOfColor.SEPIA), H2(200, LineOfColor.MONOCHROME), H3(300, LineOfColor.GRAY), H4(400,
      LineOfColor.BLACKANDWHITE), H5(900, LineOfColor.COLOR);

  private int numValue;
  private LineOfColor colorLine;

  ClassOfColor(int val, LineOfColor lineOfColor) {
    this.numValue = val;
    this.colorLine = lineOfColor;
  }

  public int getNumValue() {
    return this.numValue;
  }

  public LineOfColor getLineOfColor() {
    return this.colorLine;
  }

  public static ClassOfColor map(int valueToMap) {
    for (ClassOfColor art : ClassOfColor.values()) {
      if (art.numValue == valueToMap) {
        return art;
      }
    }
    return null;
  }
}
