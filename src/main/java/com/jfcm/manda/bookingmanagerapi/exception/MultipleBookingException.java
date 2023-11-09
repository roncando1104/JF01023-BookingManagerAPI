/*
 *  MultipleBookingException.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.exception;

public class MultipleBookingException extends RuntimeException{

  public MultipleBookingException() {
  }

  public MultipleBookingException(String message) {
    super(message);
  }

  public MultipleBookingException(String message, Throwable cause) {
    super(message, cause);
  }

  public MultipleBookingException(Throwable cause) {
    super(cause);
  }
}