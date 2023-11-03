/*
 *  LoggingService.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingService {

  private static final Logger logger = LoggerFactory.getLogger(LoggingService.class);

  private String stringMap;

  private String logMap(String uuid, String component, String message, String info) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    Map<String, String> map = new LinkedHashMap<>();

    if (StringUtils.isNotEmpty(uuid)) {
      map.put("uuid", uuid);
    }
    if (StringUtils.isNotEmpty(uuid)) {
      map.put("component", component);
    }
    if (StringUtils.isNotEmpty(uuid)) {
      map.put("message", message);
    }
    if (StringUtils.isNotEmpty(uuid)) {
      map.put(info.contains("TRN") ? "tranCode" : "additionalInfo", info);
    }

    return mapper.writeValueAsString(map);
  }

  public void info(String uuid, String component, String message, String info) throws JsonProcessingException {
    stringMap = logMap(uuid, component, message, info);
    logger.info(stringMap);
  }

  public void error(String uuid, String component, String message, String info) throws JsonProcessingException {
    stringMap = logMap(uuid, component, message, info);
    logger.error(stringMap);
  }

  public void debug(String uuid, String component, String message, String info) throws JsonProcessingException {
    stringMap = logMap(uuid, component, message, info);
    logger.debug(stringMap);
  }

  public void warn(String uuid, String component, String message, String info) throws JsonProcessingException {
    stringMap = logMap(uuid, component, message, info);
    logger.warn(stringMap);
  }

  public void trace(String uuid, String component, String message, String info) throws JsonProcessingException {
    stringMap = logMap(uuid, component, message, info);
    logger.trace(stringMap);
  }
}