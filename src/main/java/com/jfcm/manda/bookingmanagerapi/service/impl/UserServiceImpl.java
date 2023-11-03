/*
 *  UserServiceImpl.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.service.impl;

import com.jfcm.manda.bookingmanagerapi.exception.UserNotFoundException;
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
        .orElseThrow(() -> new UserNotFoundException(String.format("User %s not found", username)));
  }

//  @Override
//  public UserDetailsService userDetailsService() {
//    return new UserDetailsService() {
//      @Override
//      public UserDetails loadUserByUsername(String username) {
//        return usersRepository.findByUserName(username)
//            .orElseThrow(() -> new UserNotFoundException(String.format("User %s not found", username)));
//      }
//    };
//  }
}