/*
 *  UserAuthenticationResources.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfcm.manda.bookingmanagerapi.constants.Constants;
import com.jfcm.manda.bookingmanagerapi.dao.request.SignUpRequest;
import com.jfcm.manda.bookingmanagerapi.dao.request.SigninRequest;
import com.jfcm.manda.bookingmanagerapi.dao.request.UpdatePasswordRequest;
import com.jfcm.manda.bookingmanagerapi.dao.response.JwtAuthenticationResponse;
import com.jfcm.manda.bookingmanagerapi.model.UsersEntity;
import com.jfcm.manda.bookingmanagerapi.service.AuthenticationService;
import com.jfcm.manda.bookingmanagerapi.utils.ResponseUtil;
import com.jfcm.manda.bookingmanagerapi.utils.Utilities;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking-api/v1/auth")
@RequiredArgsConstructor
public class UserAuthenticationResources {

  private final AuthenticationService authenticationService;

  @Autowired
  private Utilities utilities;

  @PostMapping("/signup")
  public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody String request) throws JsonProcessingException {
    SignUpRequest data = utilities.readfromInput(request, SignUpRequest.class);
    //check if id has value, set it if empty (payload should not contain id.  it will be set automatically)
    if (StringUtils.isEmpty(data.getId())) {
      String generatedId = utilities.getRandomGeneratedId();
      data.setId(generatedId);
    } else {
      throw new IllegalArgumentException("ID should be blank.  It will be set by the system for you.");
    }

    return ResponseEntity.ok(authenticationService.signup(data));
  }

  @PostMapping("/signin")
  public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) throws JsonProcessingException {
    return ResponseEntity.ok(authenticationService.signin(request));
  }

  @PutMapping("/update-password/{id}")
  public ResponseEntity<JwtAuthenticationResponse> updatePassword(@RequestBody String request, @PathVariable String id) throws JsonProcessingException {
   return ResponseEntity.ok(authenticationService.updatePassword(request, id));
  }
}