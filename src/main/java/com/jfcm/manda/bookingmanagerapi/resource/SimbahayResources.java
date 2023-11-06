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
import com.jfcm.manda.bookingmanagerapi.dao.response.JwtAuthenticationResponse;
import com.jfcm.manda.bookingmanagerapi.exception.RecordNotFoundException;
import com.jfcm.manda.bookingmanagerapi.model.SimbahayGroupsEntity;
import com.jfcm.manda.bookingmanagerapi.repository.SimbahayRepository;
import com.jfcm.manda.bookingmanagerapi.service.impl.GenerateUUIDService;
import com.jfcm.manda.bookingmanagerapi.service.impl.LoggingService;
import com.jfcm.manda.bookingmanagerapi.utils.Utilities;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
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
public class SimbahayResources {

  private final LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("Asia/Manila"));
  @Autowired
  private SimbahayRepository simbahayRepository;
  @Autowired
  private LoggingService LOG;
  @Autowired
  private GenerateUUIDService generateUUIDService;
  @Autowired
  private Utilities utilities;

  /**
   * @implNote this method retrieves all the simbahay groups from database
   * @return ResponseEntity OK
   * @throws JsonProcessingException if an error during JSON processing occurs
   */
  @GetMapping(value = "/simbahay", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JwtAuthenticationResponse> getAllSimbahays() throws JsonProcessingException {
    List<SimbahayGroupsEntity> data = simbahayRepository.findAll();
    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("Simbahay groups retrieved successfully. %s record(s)", data.size()),
        Constants.TRANSACTION_SUCCESS);

    var response = getJwtAuthenticationResponse(data, HttpStatus.OK.value(),
        Constants.TRANSACTION_SUCCESS, String.format("Simbahay groups retrieved successfully. %s record(s)", data.size()));

    return ResponseEntity.ok(response);
  }

  /**
   * @implNote this method retrieves a simbahay group based on given id from database
   * @param id as {@link PathVariable}
   * @return ResponseEntity OK
   * @throws JsonProcessingException if an error during JSON processing occurs
   * @throws RecordNotFoundException if user doesn't exist
   */
  @GetMapping(value = "/simbahay/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getSimbahayById(@PathVariable(value = "id") String id) throws JsonProcessingException {
    Optional<SimbahayGroupsEntity> data = simbahayRepository.findById(id);

    if (data.isEmpty()) {
      LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("Simbahay with id %s doesn't exist.", id),
          Constants.TRANSACTION_FAILED);
      throw new RecordNotFoundException(String.format("Simbahay with id %s doesn't exist.", id));
    }

    var response = getJwtAuthenticationResponse(data, HttpStatus.OK.value(),
        Constants.TRANSACTION_SUCCESS, String.format("Simbahay with id %s was successfully retrieved.", id));
    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("Simbahay with id %s was successfully retrieved.", id),
        Constants.TRANSACTION_SUCCESS);

    return ResponseEntity.ok(response);
  }

  /**
   * @request
   * This method accepts below payload as {@link RequestBody}
  {
  "id": "",
  "simbahayName": "",
  "simbahaySchedule": "",
  "cluster": "",
  "clusterCode": "",
  "simbahayLeader1": "",
  "simbahayLeader2": "",
  "totalMembers": "",
  "location": "",
  "status": ""
  }
   * @implNote id should be assigned
   * @param input as {@link RequestBody}
   * @implNote JSON payload is checked and fixed if contains @nbsp
   * @return ResponseEntity OK
   * @throws JsonProcessingException if an error during JSON processing occurs
   */
  @PostMapping(value = "/add-simbahay", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> addSimbahayGroup(@RequestBody String input) throws JsonProcessingException {
    String processedInput = Utilities.checkAndFixInvalidJson(input);
    SimbahayGroupsEntity data = utilities.readfromInput(processedInput, SimbahayGroupsEntity.class);

    simbahayRepository.save(data);

    var response = getJwtAuthenticationResponse(data, HttpStatus.CREATED.value(),
        Constants.TRANSACTION_SUCCESS, String.format("New simbahay group with id %s successfully created!", data.getId()));
    return ResponseEntity.ok(response);
  }

  /**
   * @implNote this method deletes a simbahay group based on given id from database
   * @param id as {@link PathVariable}
   * @return ResponseEntity OK
   * @throws JsonProcessingException if an error during JSON processing occurs
   * @throws RecordNotFoundException if simbahay group doesn't exist
   */
  @DeleteMapping(value = "/delete-simbahay/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> deleteSimbahay(@PathVariable(value = "id") String id) throws JsonProcessingException {
    var data = simbahayRepository.findById(id);

    if (data.isEmpty()) {
      LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("Simbahay group with id %s doesn't exist.", id),
          Constants.TRANSACTION_FAILED);
      throw new RecordNotFoundException(String.format("Simbahay group with id %s doesn't exist.", id));
    }
    simbahayRepository.deleteById(id);

    var response = getJwtAuthenticationResponse(data, HttpStatus.OK.value(),
        Constants.TRANSACTION_SUCCESS, String.format("Simbahay with id %s has been deleted", id));
    return ResponseEntity.ok(response);
  }

  /**
   * @request
   * This method accepts below payload as {@link RequestBody}
  {
  "id": "",
  "simbahayName": "",
  "simbahaySchedule": "",
  "cluster": "",
  "clusterCode": "",
  "simbahayLeader1": "",
  "simbahayLeader2": "",
  "totalMembers": "",
  "location": "",
  "status": ""
  }
   * @implNote id should be assigned
   * @param input as {@link RequestBody}
   * @param id as {@link PathVariable}
   * @implNote JSON payload is checked and fixed if contains @nbsp
   * @return ResponseEntity OK
   * @throws JsonProcessingException if an error during JSON processing occurs
   * @throws RecordNotFoundException if Simbahay group already exist
   */
  @PutMapping(value = "/update-simbahay/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> updateSimbahayById(@RequestBody String input, @PathVariable String id) throws JsonProcessingException {

    String processedInput = Utilities.checkAndFixInvalidJson(input);
    SimbahayGroupsEntity simbahayGrp = utilities.readfromInput(processedInput, SimbahayGroupsEntity.class);

    Optional<SimbahayGroupsEntity> simbahayById = simbahayRepository.findById(id);

    if (simbahayById.isEmpty()) {
      throw new RecordNotFoundException(String.format("Simbahay group with id %s doesn't exist.", id));
    }
    simbahayGrp.setId(id);
    var updatedData = simbahayRepository.save(simbahayGrp);

    var response = getJwtAuthenticationResponse(updatedData, HttpStatus.OK.value(),
        Constants.TRANSACTION_SUCCESS, String.format("Simbahay with id %s has been updated", id));
    return ResponseEntity.ok(response);
  }

  private JwtAuthenticationResponse getJwtAuthenticationResponse(Object data, int status, String respCode, String msg) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    String dateTokenCreated = formatter.format(dateTime);

    return JwtAuthenticationResponse.builder()
        .timestamp(dateTokenCreated)
        .data(data)
        .status(status)
        .responsecode(respCode)
        .message(msg)
        .build();
  }
}