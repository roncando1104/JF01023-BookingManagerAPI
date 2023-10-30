/*
 *  DataAlreadyExistException.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.exception;

public class DataAlreadyExistException extends RuntimeException{

  public DataAlreadyExistException() {
  }

  public DataAlreadyExistException(String message) {
    super(message);
  }

  public DataAlreadyExistException(String message, Throwable cause) {
    super(message, cause);
  }

  public DataAlreadyExistException(Throwable cause) {
    super(cause);
  }
}