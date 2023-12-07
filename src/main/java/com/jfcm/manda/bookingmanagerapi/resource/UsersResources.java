/**
 * {@link com.jfcm.manda.bookingmanagerapi.resource.UsersResources}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfcm.manda.bookingmanagerapi.constants.Constants;
import com.jfcm.manda.bookingmanagerapi.dao.response.CommonResponse;
import com.jfcm.manda.bookingmanagerapi.exception.InvalidInputException;
import com.jfcm.manda.bookingmanagerapi.exception.RecordNotFoundException;
import com.jfcm.manda.bookingmanagerapi.model.UsersEntity;
import com.jfcm.manda.bookingmanagerapi.repository.UsersRepository;
import com.jfcm.manda.bookingmanagerapi.service.DataValidationService;
import com.jfcm.manda.bookingmanagerapi.service.impl.GenerateUUIDService;
import com.jfcm.manda.bookingmanagerapi.service.impl.LoggingService;
import com.jfcm.manda.bookingmanagerapi.utils.Utilities;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping(value = "/booking-api/v1/records")
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

  /**
   * @implNote this method retrieves all the users from database
   * @return ResponseEntity OK
   * @throws JsonProcessingException if an error during JSON processing occurs
   */
  @GetMapping(value = "/users", headers = {"content-type=*/*"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CommonResponse> getAllUsers() throws JsonProcessingException {
    List<UsersEntity> data = usersRepository.findAll();
    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("Data retrieved successfully. %s record(s)", data.size()),
        Constants.TRANSACTION_SUCCESS);

    var response = getResponse(data, HttpStatus.OK.value(),
        Constants.TRANSACTION_SUCCESS, String.format("Data retrieved successfully. %s record(s)", data.size()));

    return ResponseEntity.ok(response);
  }

  /**
   * @implNote this method retrieves a user based on given id from database
   * @param id as {@link PathVariable}
   * @return ResponseEntity OK
   * @throws JsonProcessingException if an error during JSON processing occurs
   * @throws RecordNotFoundException if user doesn't exist
   */
  @GetMapping(value = "/user/{id}", headers = {"content-type=*/*"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CommonResponse> getUserById(@PathVariable(value = "id") String id) throws JsonProcessingException {
    Optional<UsersEntity> data = usersRepository.findById(id);

    if (data.isEmpty()) {
      LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("User with id %s doesn't exist.", id),
          Constants.TRANSACTION_FAILED);
      throw new RecordNotFoundException(String.format("User with id %s doesn't exist.", id));
    }

    var response = getResponse(data, HttpStatus.OK.value(),
        Constants.TRANSACTION_SUCCESS, String.format("User with id %s was successfully retrieved.", id));
    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("User with id %s was successfully retrieved.", id),
        Constants.TRANSACTION_SUCCESS);

    return ResponseEntity.ok(response);
  }

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
   * @param input as {@link RequestBody}
   * @implNote JSON payload is checked and fixed if contains @nbsp
   * @return ResponseEntity OK
   * @throws JsonProcessingException if an error during JSON processing occurs
   * @throws InvalidInputException if ID is provided.  ID is auto generated
   * @throws com.jfcm.manda.bookingmanagerapi.exception.DataAlreadyExistException if user exist.
   * @implNote User is queried using first name, last name, and birthday (line 133)
   */
  @PostMapping(value = "/add-user", headers = {"content-type=*/*"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CommonResponse> addUser(@RequestBody String input) throws IOException {
    String processedInput = Utilities.checkAndFixInvalidJson(input);
    UsersEntity data = utilities.readfromInput(processedInput, UsersEntity.class);
    //validate if user already exists
    dataValidationService.validateDataAlreadyExist(data.getFirstName(), data.getLastName(), data.getBirthday());
    //ID is generated automatically
    if (StringUtils.isEmpty(data.getId())) {
      String generatedId = utilities.getRandomGeneratedId();
      data.setId(generatedId);
    } else {
      throw new InvalidInputException("ID should be blank. It will be set by the system for you.");
    }

    var newUser = usersRepository.save(data);
    var response = getResponse(data, HttpStatus.CREATED.value(),
        Constants.TRANSACTION_SUCCESS, String.format("New user with id %s successfully created!", newUser.getId()));
    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("New user with id %s successfully created!", newUser.getId()),
        Constants.TRANSACTION_SUCCESS);

    return ResponseEntity.ok(response);
  }

  /**
   * @implNote this method deletes a user based on given id from database
   * @param id as {@link PathVariable}
   * @return ResponseEntity OK
   * @throws JsonProcessingException if an error during JSON processing occurs
   * @throws RecordNotFoundException if user doesn't exist
   */
  @DeleteMapping(value = "/delete-user/{id}", headers = {"content-type=*/*"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CommonResponse> deleteMember(@PathVariable(value = "id") String id) throws JsonProcessingException {
    var data = usersRepository.findById(id);

    if (data.isEmpty()) {
      LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("User with id %s doesn't exist.", id),
          Constants.TRANSACTION_FAILED);
      throw new RecordNotFoundException(String.format("User with id %s doesn't exist.", id));
    }

    usersRepository.deleteById(id);
    var response = getResponse(data, HttpStatus.OK.value(),
        Constants.TRANSACTION_SUCCESS, String.format("User with id %s was deleted", id));
    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("User with id %s was deleted", id),
        Constants.TRANSACTION_SUCCESS);

    return ResponseEntity.ok(response);
  }

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
   * @param input as {@link RequestBody}
   * @param id as {@link PathVariable}
   * @implNote JSON payload is checked and fixed if contains @nbsp
   * @return ResponseEntity OK
   * @throws JsonProcessingException if an error during JSON processing occurs
   * @throws RecordNotFoundException if user doesn't exist
   */
  @PutMapping(value = "/update-user/{id}", headers = {"content-type=*/*"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CommonResponse> updateUserById(@RequestBody String input, @PathVariable String id) throws JsonProcessingException {
    String processedInput = Utilities.checkAndFixInvalidJson(input);
    UsersEntity user = utilities.readfromInput(processedInput, UsersEntity.class);
    Optional<UsersEntity> userById = usersRepository.findById(id);

    if (userById.isEmpty()) {
      LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("User with id %s doesn't exist.", id),
          Constants.TRANSACTION_FAILED);
      throw new RecordNotFoundException(String.format("User with id %s doesn't exist.", id));
    }

    user.setId(id);
    usersRepository.save(user);
    var response = getResponse(user, HttpStatus.OK.value(),
        Constants.TRANSACTION_SUCCESS, String.format("User with id %s has been updated", id));
    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("User with id %s has been updated", id),
        Constants.TRANSACTION_SUCCESS);

    return ResponseEntity.ok(response);
  }

  private CommonResponse getResponse(Object data, int status, String respCode, String msg) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    String dateTokenCreated = formatter.format(dateTime);

    return CommonResponse.builder()
        .timestamp(dateTokenCreated)
        .info(data)
        .status(status)
        .responseCode(respCode)
        .message(msg)
        .build();
  }
}