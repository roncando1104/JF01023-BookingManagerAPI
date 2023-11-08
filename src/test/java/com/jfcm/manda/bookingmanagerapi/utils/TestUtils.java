/**
 * {@link com.jfcm.manda.bookingmanagerapi.utils.TestUtils}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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