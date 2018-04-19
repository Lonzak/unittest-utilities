/*
 * =========================================== unittest-utilities ===========================================
 *
 * Project Info: https://github.com/Lonzak/unittest-utilities
 * 
 * (C) Copyright 2012-2017 nepatec GmbH & Co. KG
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
package net.lonzak.examples.enums;

/**
 * Some other enum
 */
public enum LineOfColor {

  BLACKANDWHITE(0), SEPIA(1), GRAY(2), MONOCHROME(3), COLOR(9);

  private int numValue;

  LineOfColor(int val) {
    this.numValue = val;
  }

  public int getNumValue() {
    return this.numValue;
  }

  public static net.lonzak.examples.enums.LineOfColor map(int valueToMap) {
    for (net.lonzak.examples.enums.LineOfColor art : net.lonzak.examples.enums.LineOfColor.values()) {
      if (art.numValue == valueToMap) {
        return art;
      }
    }
    return null;
  }
}
