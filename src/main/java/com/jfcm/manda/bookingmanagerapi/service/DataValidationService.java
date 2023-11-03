package com.jfcm.manda.bookingmanagerapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface DataValidationService {

  void validateDataAlreadyExist(String firstName, String lastName, String birthday) throws JsonProcessingException;
}
