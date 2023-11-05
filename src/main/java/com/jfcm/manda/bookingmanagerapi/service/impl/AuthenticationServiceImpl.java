/*
 *  AuthenticationServiceImpl.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfcm.manda.bookingmanagerapi.constants.Constants;
import com.jfcm.manda.bookingmanagerapi.dao.request.SignUpRequest;
import com.jfcm.manda.bookingmanagerapi.dao.request.SigninRequest;
import com.jfcm.manda.bookingmanagerapi.dao.request.UpdatePasswordRequest;
import com.jfcm.manda.bookingmanagerapi.dao.response.JwtAuthenticationResponse;
import com.jfcm.manda.bookingmanagerapi.exception.UserNotFoundException;
import com.jfcm.manda.bookingmanagerapi.model.UsersEntity;
import com.jfcm.manda.bookingmanagerapi.repository.UsersRepository;
import com.jfcm.manda.bookingmanagerapi.service.AuthenticationService;
import com.jfcm.manda.bookingmanagerapi.service.DataValidationService;
import com.jfcm.manda.bookingmanagerapi.service.JwtService;
import com.jfcm.manda.bookingmanagerapi.utils.Utilities;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UsersRepository usersRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("Asia/Manila"));

  @Autowired
  private Utilities utilities;
  @Autowired
  private DataValidationService dataValidationService;
  @Autowired
  private GenerateUUIDService generateUUIDService;
  @Autowired
  private LoggingService LOG;

  @Override
  public JwtAuthenticationResponse signup(SignUpRequest request) throws JsonProcessingException {

    dataValidationService.validateDataAlreadyExist(request.getFirstName(), request.getLastName(), request.getBirthday());

    var user = UsersEntity.builder()
        .id(request.getId())
        .firstName(request.getFirstName())
        .middleName(request.getMiddleName())
        .lastName(request.getLastName())
        .emailAdd(request.getEmailAdd())
        .contactNumber(request.getContactNumber())
        .address(request.getAddress())
        .birthday(request.getBirthday())
        .role(request.getRole())
        .status(request.getStatus())
        .cluster(request.getCluster())
        .clusterCode(request.getClusterCode())
        .simbahayName(request.getSimbahayName())
        .simbahayCode(request.getSimbahayCode())
        .userName(request.getUserName())
        .password(passwordEncoder.encode(request.getPassword()))
        .build();

    usersRepository.save(user);
    var jwt = jwtService.generateToken(user);
    var tokenExpiry = jwtService.extractExpiration(jwt);

    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(),
        String.format("You have been added as new user with role %s and id %s", user.getRole(), user.getId()),
        Constants.TRANSACTION_SUCCESS);

    return getAuthenticationResponse(tokenExpiry, jwt, user, String.format("You have been added as new user with role %s and id %s", user.getRole(), user.getId()));
  }

  @Override
  public JwtAuthenticationResponse signin(SigninRequest request) throws JsonProcessingException {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));

    var user = usersRepository.findByUserName(request.getUserName())
        .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

    var jwt = jwtService.generateToken(user);
    var tokenExpiry = jwtService.extractExpiration(jwt);

    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(),
        String.format("User %s %s has successfully login.", user.getFirstName(), user.getLastName()),
        Constants.TRANSACTION_SUCCESS);

    return getAuthenticationResponse(tokenExpiry, jwt, user, String.format("User %s %s has successfully login.", user.getFirstName(), user.getLastName()));
  }

  @Override
  public JwtAuthenticationResponse updatePassword(String request, String id) throws JsonProcessingException {
    var data = utilities.readfromInput(request, UpdatePasswordRequest.class);
    //authenticate the user with old password and username before finding if the id exists
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(data.getUserName(), data.getOldPassword()));

    var user = usersRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(String.format("User with id %s doesn't exist", id)));
    //update password with the new password
    usersRepository.updatePassword(passwordEncoder.encode(data.getNewPassword()), id);
    var jwt = jwtService.generateToken(user);
    var tokenExpiry = jwtService.extractExpiration(jwt);

    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("User with id %s has successfully updated password", user.getId()),
        Constants.TRANSACTION_SUCCESS);

    return getAuthenticationResponse(tokenExpiry, jwt, user, String.format("User with id %s has successfully updated password", user.getId()));
  }

  private JwtAuthenticationResponse getAuthenticationResponse(Date tokenExpiry, String jwt, Object user, String msg) {

    LocalDateTime tokenExpiryStr = LocalDateTime.ofInstant(tokenExpiry.toInstant(), ZoneId.systemDefault());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    String dateTokenCreated = formatter.format(dateTime);
    String tokenExpiryDate = formatter.format(tokenExpiryStr);

    return JwtAuthenticationResponse.builder()
        .timestamp(dateTokenCreated)
        .token(jwt)
        .expiration(tokenExpiryDate)
        .data(user)
        .status(HttpStatus.OK.value())
        .responsecode(Constants.TRANSACTION_SUCCESS)
        .message(msg)
        .build();
  }
}