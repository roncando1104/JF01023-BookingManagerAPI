/**
 * {@link com.jfcm.manda.bookingmanagerapi.exception.DataAlreadyExistException}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
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