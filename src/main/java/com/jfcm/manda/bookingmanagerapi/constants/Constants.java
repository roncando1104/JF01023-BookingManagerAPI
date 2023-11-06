/**
 * {@link com.jfcm.manda.bookingmanagerapi.constants.Constants}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.constants;

public class Constants {

  public static final String JF_ID_PREFIX = "JF-";
  public static final String TRANSACTION_SUCCESS = "TRN-000";
  public static final String TRANSACTION_FAILED = "TRN-001";
  public static final String HEADER_AUTHORIZATION = "Authorization";
  public static final String PREFIX_TOKEN_BEARER = "Bearer ";
  public static final String JWT_SIGNING_KEY = "q+QgId0EKAQ53YG3dNelFqlqTteekylCqjRDj3tb71E=";

  public static final String[] AUTH_WHITE_LIST = {
      //Private Endpoints
      "/booking-api/v1/auth/**",
      //"/booking-api/v1/records/**",
      //Swagger UI
      "/swagger-ui-custom.html",
      "/swagger-ui/index.html",
      "/swagger-ui/**",
      "/api-docs",
      "/api-docs.yaml"
  };
}