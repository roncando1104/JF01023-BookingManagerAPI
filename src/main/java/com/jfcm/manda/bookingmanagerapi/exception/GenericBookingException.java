/*
 *  GenericBookingException.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.exception;

public class GenericBookingException extends RuntimeException{

  public GenericBookingException() {
  }

  public GenericBookingException(String message) {
    super(message);
  }

  public GenericBookingException(String message, Throwable cause) {
    super(message, cause);
  }

  public GenericBookingException(Throwable cause) {
    super(cause);
  }
}