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
package net.lonzak.common.unittest.examples.exceptions;

/**
 * Example for a specific technical exception.
 *
 */
public class ServiceNotAvailableException extends TechnicalException {

  private static final long serialVersionUID = -1047762760696764530L;
  public final static int ERRORNUMBER = 101;

  /**
   * Constructs a default {@link ServiceNotAvailableException} with the following message: "The service is not
   * available"
   */
  public ServiceNotAvailableException() {
    super("The service is not available", ERRORNUMBER);
  }

  /**
   * Constructs a default {@link ServiceNotAvailableException} with the following message: "The service 'serviceName' is
   * not available"
   * 
   * @param serviceName the service which is not available
   */
  public ServiceNotAvailableException(String serviceName) {
    super("The service " + serviceName + " is not available!", ERRORNUMBER);
  }
}
