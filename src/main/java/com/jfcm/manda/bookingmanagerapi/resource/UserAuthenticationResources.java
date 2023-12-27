/**
 * {@link com.jfcm.manda.bookingmanagerapi.resource.UserAuthenticationResources}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the
 * confidential and proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfcm.manda.bookingmanagerapi.dao.request.SignUpRequest;
import com.jfcm.manda.bookingmanagerapi.dao.request.SigninRequest;
import com.jfcm.manda.bookingmanagerapi.dao.request.TokenRequest;
import com.jfcm.manda.bookingmanagerapi.dao.response.JwtAuthenticationResponse;
import com.jfcm.manda.bookingmanagerapi.exception.InvalidInputException;
import com.jfcm.manda.bookingmanagerapi.service.AuthenticationService;
import com.jfcm.manda.bookingmanagerapi.service.JwtService;
import com.jfcm.manda.bookingmanagerapi.utils.Utilities;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

  /**
   * @request
   * This method accepts below payload as {@link RequestBody}
  {
  "firstName": "",
  "middleName": "",
  "lastName": "",
  "emailAdd": "",
  "contactNumber": "",
  "address": "",
  "birthday": "",
  "role": "",
  "status": "",
  "cluster": "",
  "clusterCode": "",
  "simbahayName": "",
  "simbahayCode": "",
  "userName": "",
  "password": ""
  }
   * @implNote id is auto generated
   * @implNote token will be generated
   * @param request as {@link RequestBody}
   * @implNote JSON payload is checked and fixed if contains @nbsp
   * @return ResponseEntity OK
   * @throws JsonProcessingException if an error during JSON processing occurs
   * @throws InvalidInputException if ID is provided.  ID is auto generated
   * @throws com.jfcm.manda.bookingmanagerapi.exception.DataAlreadyExistException if user exist.
   * @implNote User is queried using first name, last name, and birthday. Check authentication.signup() call (line 59).
   */
  @PostMapping(value = "/signup", headers = {"content-type=*/*"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody String request) throws JsonProcessingException {
    String processedRequest = Utilities.checkAndFixInvalidJson(request);
    SignUpRequest data = utilities.readfromInput(processedRequest, SignUpRequest.class);
    //check if id has value, set it if empty (payload should not contain id.  it will be set automatically)
    if (StringUtils.isEmpty(data.getId())) {
      String generatedId = utilities.getRandomGeneratedId();
      data.setId(generatedId);
    } else {
      throw new InvalidInputException("ID should be blank.  It will be set by the system for you.");
    }

    return ResponseEntity.ok(authenticationService.signup(data));
  }

  /**
   * @request
   * This method accepts below payload as {@link RequestBody}
  {
  "userName": "",
  "password": ""
  }
   * @implNote token will be generated
   * @param request as {@link RequestBody}
   * @implNote JSON payload is checked and fixed if contains @nbsp
   * @return ResponseEntity OK
   * @throws JsonProcessingException if an error during JSON processing occurs
   * @throws InvalidInputException if user or password is invalid
   */
  @PostMapping(value = "/signin", headers = {"content-type=*/*"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody String request) throws JsonProcessingException {
    String processedRequest = Utilities.checkAndFixInvalidJson(request);
    SigninRequest signinData = utilities.readfromInput(processedRequest, SigninRequest.class);
    return ResponseEntity.ok(authenticationService.signin(signinData));
  }

  // request - we get the authorization header that holds the refresh-token
  // response - will help us to re-inject or send back the response to the user
  @PostMapping(value = "/refresh-token", headers = {"content-type=*/*"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    authenticationService.refreshToken(request, response);
  }

  @PostMapping(value = "/check-token", headers = {"content-type=*/*"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public void isTokenStillValid(@RequestBody String token, HttpServletResponse response) throws IOException {
    TokenRequest tokenData = utilities.readfromInput(token, TokenRequest.class);
    authenticationService.isTokenExpired(tokenData.getToken(), response);
  }

  /**
   * @request
   * This method accepts below payload as {@link RequestBody}
  {
  "userName": "",
  "oldPassword": "",
  "newPassword": "",
  }
   * @implNote token will be generated
   * @param id as {@link PathVariable}
   * @param request as {@link RequestBody}
   * @implNote JSON payload is checked and fixed if contains @nbsp
   * @return ResponseEntity OK
   * @throws JsonProcessingException if an error during JSON processing occurs
   * @throws com.jfcm.manda.bookingmanagerapi.exception.RecordNotFoundException is user doesn't exist
   * @implNote User is queried using first name, last name, and birthday. Check authentication.signup() call (line 59).
   */
  @PutMapping(value = "/update-password/{id}", headers = {"content-type=*/*"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JwtAuthenticationResponse> updatePassword(@RequestBody String request, @PathVariable String id) throws JsonProcessingException {
    String processedRequest = Utilities.checkAndFixInvalidJson(request);
    return ResponseEntity.ok(authenticationService.updatePassword(processedRequest, id));
  }
}