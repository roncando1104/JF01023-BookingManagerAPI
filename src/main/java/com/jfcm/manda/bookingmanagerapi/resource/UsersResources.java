/*
 *  UsersResources.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfcm.manda.bookingmanagerapi.constants.Constants;
import com.jfcm.manda.bookingmanagerapi.dao.response.JwtAuthenticationResponse;
import com.jfcm.manda.bookingmanagerapi.model.UsersEntity;
import com.jfcm.manda.bookingmanagerapi.repository.UsersRepository;
import com.jfcm.manda.bookingmanagerapi.service.DataValidationService;
import com.jfcm.manda.bookingmanagerapi.service.impl.GenerateUUIDService;
import com.jfcm.manda.bookingmanagerapi.service.impl.LoggingService;
import com.jfcm.manda.bookingmanagerapi.utils.Utilities;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking-api/v1/records")
public class UsersResources {

  private final LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("Asia/Manila"));
  @Autowired
  private UsersRepository usersRepository;
  @Autowired
  private Utilities utilities;
  @Autowired
  private DataValidationService dataValidationService;
  @Autowired
  private LoggingService LOG;
  @Autowired
  private GenerateUUIDService generateUUIDService;

  @GetMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JwtAuthenticationResponse> getAllUsers() throws JsonProcessingException {
    List<UsersEntity> data = usersRepository.findAll();
    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("Data retrieved successfully. %s record(s)", data.size()),
        Constants.TRANSACTION_SUCCESS);

    var response = getJwtAuthenticationResponse(data, HttpStatus.OK.value(),
        Constants.TRANSACTION_SUCCESS, String.format("Data retrieved successfully. %s record(s)", data.size()));

    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "/user/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JwtAuthenticationResponse> getUserById(@PathVariable(value = "id") String id) throws JsonProcessingException {
    Optional<UsersEntity> data = usersRepository.findById(id);

    if (data.isEmpty()) {
      LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("User with id %s doesn't exist.", id),
          Constants.TRANSACTION_FAILED);
      var response = getJwtAuthenticationResponse(data, HttpStatus.NOT_FOUND.value(),
          Constants.TRANSACTION_FAILED, String.format("User with id %s doesn't exist.", id));

      return ResponseEntity.badRequest().body(response);
    }

    var response = getJwtAuthenticationResponse(data, HttpStatus.OK.value(),
        Constants.TRANSACTION_SUCCESS, String.format("User with id %s was successfully retrieved.", id));
    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("User with id %s was successfully retrieved.", id),
        Constants.TRANSACTION_SUCCESS);

    return ResponseEntity.ok(response);
  }

  @PostMapping(value = "/add-user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JwtAuthenticationResponse> addUser(@RequestBody String user) throws IOException {
    UsersEntity data = utilities.readfromInput(user, UsersEntity.class);
    //validate if user already exists
    dataValidationService.validateDataAlreadyExist(data.getFirstName(), data.getLastName(), data.getBirthday());
    //ID is generated automatically
    if (StringUtils.isEmpty(data.getId())) {
      String generatedId = utilities.getRandomGeneratedId();
      data.setId(generatedId);
    } else {
      throw new IllegalArgumentException("ID should be blank.  It will be set by the system for you.");
    }

    var newUser = usersRepository.save(data);
    var response = getJwtAuthenticationResponse(data, HttpStatus.OK.value(),
        Constants.TRANSACTION_SUCCESS, String.format("New user with id %s successfully created!", newUser.getId()));
    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("New user with id %s successfully created!", newUser.getId()),
        Constants.TRANSACTION_SUCCESS);

    return ResponseEntity.ok(response);
  }

  @DeleteMapping(value = "/delete-user/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JwtAuthenticationResponse> deleteMember(@PathVariable(value = "id") String id) throws JsonProcessingException {
    var data = usersRepository.findById(id);

    if (data.isEmpty()) {
      LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("User with id %s doesn't exist.", id),
          Constants.TRANSACTION_FAILED);
      var response = getJwtAuthenticationResponse(data, HttpStatus.NOT_FOUND.value(),
          Constants.TRANSACTION_FAILED, String.format("User with id %s doesn't exist.", id));

      return ResponseEntity.badRequest().body(response);
    }

    usersRepository.deleteById(id);
    var response = getJwtAuthenticationResponse(data, HttpStatus.OK.value(),
        Constants.TRANSACTION_SUCCESS, String.format("User with id %s was deleted", id));
    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("User with id %s was deleted", id),
        Constants.TRANSACTION_SUCCESS);

    return ResponseEntity.ok(response);
  }

  @PutMapping(value = "/update-user/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JwtAuthenticationResponse> updateUserById(@RequestBody UsersEntity users, @PathVariable String id) throws JsonProcessingException {

    Optional<UsersEntity> user = usersRepository.findById(id);

    if (user.isEmpty()) {
      LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("User with id %s doesn't exist.", id),
          Constants.TRANSACTION_FAILED);
      var response = getJwtAuthenticationResponse(user, HttpStatus.NOT_FOUND.value(),
          Constants.TRANSACTION_FAILED, String.format("User with id %s doesn't exist.", id));

      return ResponseEntity.badRequest().body(response);
    }

    users.setId(id);
    usersRepository.save(users);
    var response = getJwtAuthenticationResponse(users, HttpStatus.OK.value(),
        Constants.TRANSACTION_SUCCESS, String.format("User with id %s has been updated", id));
    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("User with id %s has been updated", id),
        Constants.TRANSACTION_SUCCESS);

    return ResponseEntity.ok(response);
  }

  private JwtAuthenticationResponse getJwtAuthenticationResponse(Object data, int status, String respCode, String msg) {
    return JwtAuthenticationResponse.builder()
        //.timestamp(dateTime)
        .data(data)
        .status(status)
        .responsecode(respCode)
        .message(msg)
        .build();
  }
}