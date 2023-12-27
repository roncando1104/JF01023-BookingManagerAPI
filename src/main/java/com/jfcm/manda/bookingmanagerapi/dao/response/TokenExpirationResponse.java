/*
 *  TokenExpirationResponse.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.dao.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenExpirationResponse {

  private String timestamp;
  private String token;
  private String tokenExpiration;
  private boolean isExpired;
  private String user;
  private int status;
  private String responseCode;
  private String message;
}