/**
 * {@link com.jfcm.manda.bookingmanagerapi.service.impl.RequestDataService}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.service.impl;

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