/*
 *  Utilities.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jfcm.manda.bookingmanagerapi.constants.Constants;
import com.jfcm.manda.bookingmanagerapi.repository.UsersRepository;
import java.text.DecimalFormat;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Utilities {

  @Autowired
  private UsersRepository usersRepository;

  public String getGeneratedId() {
    Random rnd = new Random();
    int randomNum = 100000 + rnd.nextInt(999999);
    String generatedId;

    do {
      generatedId = Constants.JF_ID_PREFIX + randomNum;
    } while (usersRepository.existsById(generatedId));

    return generatedId;
  }

  public <T> T readfromInput(String input, Class<T> object) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper.readValue(input, object);
  }

  public static String formatNumber(double value) {
    DecimalFormat df = new DecimalFormat("#,##0.00");

    return df.format(value);
  }

  public String splitFullName(String fullName, String namePart) {
    String lastName = fullName.split(" ")[fullName.split(" ").length-1];
    String firstName = fullName.substring(0, fullName.length() - lastName.length());

    if (namePart.equalsIgnoreCase("lastName")){
      return lastName.trim();
    } else {
      return firstName.trim();
    }
  }
}