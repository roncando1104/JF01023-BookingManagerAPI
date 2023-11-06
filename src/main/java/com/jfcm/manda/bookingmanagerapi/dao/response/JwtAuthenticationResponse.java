/**
 * {@link com.jfcm.manda.bookingmanagerapi.dao.response.JwtAuthenticationResponse}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
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
public class JwtAuthenticationResponse {

  private String timestamp;
  private String token;
  private String expiration;
  private Object data;
  private int status;
  private String responsecode;
  private String message;
}