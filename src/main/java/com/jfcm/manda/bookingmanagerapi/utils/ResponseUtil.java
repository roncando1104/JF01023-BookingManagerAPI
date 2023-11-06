/**
 * {@link com.jfcm.manda.bookingmanagerapi.utils.ResponseUtil}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtil {

  public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object respObj,
      String responseCode) {
    Map<String, Object> map = new HashMap<>();
    map.put("timestamp", Timestamp.valueOf(LocalDateTime.now()));
    map.put("message", message);
    map.put("status", status.value());
    map.put("data", respObj);
    map.put("responseCode", responseCode);

    return new ResponseEntity<>(map, status);
  }
}