/*
 *  TokenExpiredException.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.exception;

import io.jsonwebtoken.ExpiredJwtException;

public class TokenExpiredException extends RuntimeException {

  public TokenExpiredException() {
  }

  public TokenExpiredException(String message) {
    super(message);
  }

  public TokenExpiredException(String message, Throwable cause) {
    super(message, cause);
  }

  public TokenExpiredException(Throwable cause) {
    super(cause);
  }
}