/**
 * {@link com.jfcm.manda.bookingmanagerapi.service.impl.AuthenticationServiceImpl}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfcm.manda.bookingmanagerapi.constants.Constants;
import com.jfcm.manda.bookingmanagerapi.dao.request.SignUpRequest;
import com.jfcm.manda.bookingmanagerapi.dao.request.SigninRequest;
import com.jfcm.manda.bookingmanagerapi.dao.request.UpdatePasswordRequest;
import com.jfcm.manda.bookingmanagerapi.dao.response.JwtAuthenticationResponse;
import com.jfcm.manda.bookingmanagerapi.exception.InvalidInputException;
import com.jfcm.manda.bookingmanagerapi.exception.RecordNotFoundException;
import com.jfcm.manda.bookingmanagerapi.model.UsersEntity;
import com.jfcm.manda.bookingmanagerapi.repository.UsersRepository;
import com.jfcm.manda.bookingmanagerapi.service.AuthenticationService;
import com.jfcm.manda.bookingmanagerapi.service.DataValidationService;
import com.jfcm.manda.bookingmanagerapi.service.JwtService;
import com.jfcm.manda.bookingmanagerapi.utils.Utilities;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
        //TODO: Add column in data base which will hold the encrypted password
        .build();

    usersRepository.save(user);
    var jwt = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    var tokenExpiry = jwtService.extractExpiration(jwt);

    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(),
        String.format("You have been added as new user with %s role and id %s", user.getRole(), user.getId()),
        Constants.TRANSACTION_SUCCESS);

    return getAuthenticationResponse(tokenExpiry, jwt, refreshToken, user, String.format("You have been added as new user with role %s and id %s", user.getRole(), user.getId()));
  }

  @Override
  public JwtAuthenticationResponse signin(SigninRequest request) throws JsonProcessingException {
    var user = usersRepository.findByUserName(request.getUserName())
        .orElseThrow(() -> new InvalidInputException("Invalid username or password"));
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));

    var jwt = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    var tokenExpiry = jwtService.extractExpiration(jwt);

    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(),
        String.format("User %s %s has successfully login.", user.getFirstName(), user.getLastName()),
        Constants.TRANSACTION_SUCCESS);

    return getAuthenticationResponse(tokenExpiry, jwt, refreshToken, user, String.format("User %s %s has successfully login.", user.getFirstName(), user.getLastName()));
  }

  @Override
  public JwtAuthenticationResponse updatePassword(String request, String id) throws JsonProcessingException {
    var data = utilities.readfromInput(request, UpdatePasswordRequest.class);
    //authenticate the user with old password and username before finding if the id exists
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(data.getUserName(), data.getOldPassword()));

    var user = usersRepository.findById(id)
        .orElseThrow(() -> new RecordNotFoundException(String.format("User with id %s doesn't exist", id)));
    //update password with the new password
    usersRepository.updatePassword(passwordEncoder.encode(data.getNewPassword()), id);
    //TODO: Add column in data base which will hold the encrypted password
    var jwt = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    var tokenExpiry = jwtService.extractExpiration(jwt);

    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("User with id %s has successfully updated password", user.getId()),
        Constants.TRANSACTION_SUCCESS);

    return getAuthenticationResponse(tokenExpiry, jwt, refreshToken, user, String.format("User with id %s has successfully updated password", user.getId()));
  }

  public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userName;

    if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, Constants.PREFIX_TOKEN_BEARER)) {
      return;
    }

    refreshToken = authHeader.split(" ")[1].trim();
    userName = jwtService.extractUserName(refreshToken);

    if (StringUtils.isNotEmpty(userName)) {
      var userDetails = this.usersRepository.findByUserName(userName).orElseThrow();

      if (jwtService.isTokenValid(refreshToken, userDetails)) {
       var accessToken = jwtService.generateToken(userDetails);
       var authResponse = JwtAuthenticationResponse.builder()
           .accessToken(accessToken)
           .refreshToken(refreshToken)
           .build();

       new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }

  private JwtAuthenticationResponse getAuthenticationResponse(Date tokenExpiry, String accessToken, String refreshToken, Object user, String msg) {

    LocalDateTime tokenExpiryStr = LocalDateTime.ofInstant(tokenExpiry.toInstant(), ZoneId.systemDefault());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    String dateTokenCreated = formatter.format(dateTime);
    String tokenExpiryDate = formatter.format(tokenExpiryStr);

    return JwtAuthenticationResponse.builder()
        .timestamp(dateTokenCreated)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .expiration(tokenExpiryDate)
        .info(user)
        .status(HttpStatus.OK.value())
        .responseCode(Constants.TRANSACTION_SUCCESS)
        .message(msg)
        .build();
  }
}