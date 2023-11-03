/*
 *  DataValidationServiceImpl.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfcm.manda.bookingmanagerapi.constants.Constants;
import com.jfcm.manda.bookingmanagerapi.exception.DataAlreadyExistException;
import com.jfcm.manda.bookingmanagerapi.repository.UsersRepository;
import com.jfcm.manda.bookingmanagerapi.service.DataValidationService;
import com.jfcm.manda.bookingmanagerapi.utils.Utilities;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataValidationServiceImpl implements DataValidationService {

  @Autowired
  private UsersRepository usersRepository;
  @Autowired
  private GenerateUUIDService generateUUIDService;
  @Autowired
  private LoggingService LOG;

  @Override
  public void validateDataAlreadyExist(String firstName, String lastName, String birthday) throws JsonProcessingException {
    String newUser = usersRepository.verifyUserAlreadyExist(firstName, lastName, birthday);
    if (!StringUtils.isEmpty(newUser)) {
      String[] newUserArray = newUser.split(",");

      if (StringUtils.equalsIgnoreCase(firstName, newUserArray[1]) && StringUtils.equalsIgnoreCase(lastName, newUserArray[2])
          && StringUtils.equalsIgnoreCase(birthday, newUserArray[3])) {
        throw new DataAlreadyExistException(String.format("User with name %s %s, and birthday %s already exists. Id is %s.",
            firstName, lastName, birthday, newUserArray[0]));
      }
    } else {
      LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), "New User will be added", Constants.TRANSACTION_SUCCESS);
    }

  }
}