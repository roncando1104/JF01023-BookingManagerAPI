/**
 * {@link com.jfcm.manda.bookingmanagerapi.utils.Utilities}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and proprietary
 * information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jfcm.manda.bookingmanagerapi.constants.Constants;
import com.jfcm.manda.bookingmanagerapi.repository.UsersRepository;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class Utilities {

  @Autowired
  private UsersRepository usersRepository;

  @Value("${booking.allowable.event-days}")
  private String allowedDaysToBook;

  public boolean isAllowedOnGivenDays(LocalDate localDate) {
    String[] listOfAllowedDaysToBook = allowedDaysToBook.split(",");
    String dayOfDate = localDate.getDayOfWeek().toString();

    //if dayOfDate is within the list of days, this will be true. Otherwise, false.
    return Arrays.stream(listOfAllowedDaysToBook).anyMatch(day -> day.trim().equals(dayOfDate));
  }

  public static String checkAndFixInvalidJson(String input) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      mapper.readTree(input);
      return input;
    } catch (IOException e) {
      input = input.replace('\u00A0', ' ');
    }
    return input;
  }

  public static String formatNumber(double value) {
    DecimalFormat df = new DecimalFormat("###,###,###.00");

    return df.format(value);
  }

  public String getRandomGeneratedId() {
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

  public String splitFullName(String fullName, String namePart) {
    String lastName = fullName.split(" ")[fullName.split(" ").length - 1];
    String firstName = fullName.substring(0, fullName.length() - lastName.length());

    if (namePart.equalsIgnoreCase("lastName")) {
      return lastName.trim();
    } else {
      return firstName.trim();
    }
  }
}