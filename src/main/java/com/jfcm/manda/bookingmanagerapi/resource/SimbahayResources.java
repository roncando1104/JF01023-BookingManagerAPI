/*
 *  SimbahayResources.java
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
import com.jfcm.manda.bookingmanagerapi.model.SimbahayGroupsEntity;
import com.jfcm.manda.bookingmanagerapi.repository.SimbahayRepository;
import com.jfcm.manda.bookingmanagerapi.service.impl.GenerateUUIDService;
import com.jfcm.manda.bookingmanagerapi.service.impl.LoggingService;
import com.jfcm.manda.bookingmanagerapi.utils.ResponseUtil;
import java.sql.Timestamp;
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

  @GetMapping(value = "/simbahay", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JwtAuthenticationResponse> getAllSimbahays() throws JsonProcessingException {
    List<SimbahayGroupsEntity> data = simbahayRepository.findAll();
    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("Data retrieved successfully. %s record(s)", data.size()),
        Constants.TRANSACTION_SUCCESS);

    var response = getJwtAuthenticationResponse(data, HttpStatus.OK.value(),
        Constants.TRANSACTION_SUCCESS, String.format("Data retrieved successfully. %s record(s)", data.size()));

    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "/simbahay/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getSimbahayById(@PathVariable(value = "id") String id) throws JsonProcessingException {
    Optional<SimbahayGroupsEntity> data = simbahayRepository.findById(id);

    if (data.isEmpty()) {
      LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("Simbahay with id %s doesn't exist.", id),
          Constants.TRANSACTION_FAILED);
      var response = getJwtAuthenticationResponse(data, HttpStatus.NOT_FOUND.value(),
          Constants.TRANSACTION_FAILED, String.format("Simbahay with id %s doesn't exist.", id));

      return ResponseEntity.badRequest().body(response);
    }

    var response = getJwtAuthenticationResponse(data, HttpStatus.OK.value(),
        Constants.TRANSACTION_SUCCESS, String.format("Simbahay with id %s was successfully retrieved.", id));
    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("Simbahay with id %s was successfully retrieved.", id),
        Constants.TRANSACTION_SUCCESS);

    return ResponseEntity.ok(response);
  }

  @PostMapping(value = "/add-simbahay", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> addSimbahayGroup(@RequestBody SimbahayGroupsEntity data) {
    simbahayRepository.save(data);

    return ResponseUtil.generateResponse("New simbahay group successfully created!", HttpStatus.OK, data, Constants.TRANSACTION_SUCCESS);
  }

  @DeleteMapping(value = "/delete-simbahay/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> deleteSimbahay(@PathVariable(value = "id") String id) {
    var data = simbahayRepository.findById(id);
    simbahayRepository.deleteById(id);

    return ResponseUtil.generateResponse(String.format("Simbahay with id %s has been deleted", id), HttpStatus.OK, data, Constants.TRANSACTION_SUCCESS);
  }

  @PutMapping(value = "/update-simbahay/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> updateSimbahayById(@RequestBody SimbahayGroupsEntity simbahayGrp, @PathVariable String id) {

    Optional<SimbahayGroupsEntity> simbahayById = simbahayRepository.findById(id);

    if (simbahayById.isEmpty()) {
      return ResponseUtil.generateResponse(String.format("Simbahay with id %s doesn't exist", id), HttpStatus.NOT_FOUND, String.format("id %s not found", id), Constants.TRANSACTION_FAILED);
    }
    simbahayGrp.setId(id);
    simbahayRepository.save(simbahayGrp);

    return ResponseUtil.generateResponse(String.format("Simbahay with id %s has been updated", id), HttpStatus.OK, simbahayGrp, Constants.TRANSACTION_SUCCESS);
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