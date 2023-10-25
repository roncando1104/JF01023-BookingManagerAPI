/*
 *  getClientIdService.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.service;

import com.jfcm.manda.bookingmanagerapi.repository.UsersRepository;
import com.jfcm.manda.bookingmanagerapi.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestDataService {

  @Autowired
  private Utilities utilities;
  @Autowired
  private UsersRepository usersRepository;

  public String getClientIdByName(String fullName) {
    String fName = utilities.splitFullName(fullName, "firstName");
    String lName = utilities.splitFullName(fullName, "lastName");

    return usersRepository.findUserIdByFullName(fName, lName);
  }
}