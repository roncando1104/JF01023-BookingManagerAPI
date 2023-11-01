/*
 *  RecordNotFoundException.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.exception;

public class RecordNotFoundException extends RuntimeException{

  public RecordNotFoundException() {
  }

  public RecordNotFoundException(String message) {
    super(message);
  }

  public RecordNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public RecordNotFoundException(Throwable cause) {
    super(cause);
  }
}