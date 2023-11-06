/**
 * {@link com.jfcm.manda.bookingmanagerapi.exception.RecordNotFoundException}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
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