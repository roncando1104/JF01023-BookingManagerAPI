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

  public static void fileReaderAndWriter(String folderPath, String fileName, String strReplacement) {
    File file = new File(folderPath);
    String absolutePath = file.getAbsolutePath();
    FileWriter fileWriter = null;
    String oldContent = "";
    try {
      FileReader fileReader = new FileReader(absolutePath + "/" + fileName);
      BufferedReader bufferReader = new BufferedReader(fileReader);
      String fileData;

      try {
        while ((fileData = bufferReader.readLine()) != null) {
          oldContent = oldContent + fileData + System.lineSeparator();
        }
        fileWriter = new FileWriter(absolutePath + "/" + fileName);
        fileWriter.write(strReplacement);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        try {
          //Closing the resources
          fileWriter.close();
          fileReader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}