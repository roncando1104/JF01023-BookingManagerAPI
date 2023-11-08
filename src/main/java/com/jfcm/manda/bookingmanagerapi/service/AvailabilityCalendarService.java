package com.jfcm.manda.bookingmanagerapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface AvailabilityCalendarService {
  void generateYearDates() throws JsonProcessingException;

  void deletePreviousYearDates() throws JsonProcessingException;
}
