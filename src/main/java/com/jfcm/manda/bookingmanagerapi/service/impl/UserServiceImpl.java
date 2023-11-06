/**
 * {@link com.jfcm.manda.bookingmanagerapi.service.impl.UserServiceImpl}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.service.impl;

import com.jfcm.manda.bookingmanagerapi.exception.RecordNotFoundException;
import com.jfcm.manda.bookingmanagerapi.repository.UsersRepository;
import com.jfcm.manda.bookingmanagerapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UsersRepository usersRepository;

  @Override
  public UserDetailsService userDetailsService() {
    return username -> usersRepository.findByUserName(username)
        .orElseThrow(() -> new RecordNotFoundException(String.format("User %s not found", username)));
  }
}