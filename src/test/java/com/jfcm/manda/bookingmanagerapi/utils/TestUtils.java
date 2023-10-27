/*
 *  TestUtils.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;

public class TestUtils {

  public static InputStream readFileAsStream(String path) {
    return TestUtils.class.getClassLoader().getResourceAsStream(path);
  }

  public static <T> T readFileValue(ObjectMapper objectMapper, String path, Class<T> valueType) {
    try {
      return objectMapper.readValue(readFileAsStream(path), valueType);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}