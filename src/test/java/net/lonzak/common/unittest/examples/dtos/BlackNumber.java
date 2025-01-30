/*
 * =========================================== unittest-utilities ===========================================
 *
 * Project Info: https://github.com/Lonzak/unittest-utilities
 * 
 * (C) Copyright 2012-2025 nepatec GmbH
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

/**
 * Example for a colored number: Black
 */
public class BlackNumber implements ColorNumber {

  /**
   * Black Numbers consists just of one sequential number
   */
  private final long id;


  public BlackNumber(long id) {
    super();
    this.id = id;
  }


  public Long getLong() {
    return this.id;
  }

  @Override
  public String toIdentificationString() {
    return Long.toString(this.id);
  }

  @Override
  public String toFormattedString() {
    String result = "00000000" + Long.toString(this.id);
    return "#" + result.substring(result.length() - 9, result.length() - 6) + "-"
        + result.substring(result.length() - 6, result.length() - 3) + "-"
        + result.substring(result.length() - 3, result.length());
  }

  @Override
  public String toString() {
    return "Number Black [" + this.toIdentificationString() + "]";
  }

}
