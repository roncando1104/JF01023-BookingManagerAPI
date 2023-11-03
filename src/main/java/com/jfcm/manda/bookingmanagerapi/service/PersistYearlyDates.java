package com.jfcm.manda.bookingmanagerapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;

@FunctionalInterface
public interface PersistYearlyDates {
  void generateYearDates() throws JsonProcessingException;
}
