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
package net.lonzak.common.unittest;


public class InternalException extends RuntimeException {

  private static final long serialVersionUID = 9218252003006131300L;

  public InternalException(String message) {
    super(message);
  }

  public InternalException(String message, Throwable cause) {
    super(message, cause);
  }

  public InternalException(Throwable cause) {
    super(cause);
  }

}
