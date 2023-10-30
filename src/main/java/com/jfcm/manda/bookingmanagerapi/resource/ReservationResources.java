/*
 *  ReservationResources.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfcm.manda.bookingmanagerapi.constants.Constants;
import com.jfcm.manda.bookingmanagerapi.model.ReservationEntity;
import com.jfcm.manda.bookingmanagerapi.repository.ReservationRepository;
import com.jfcm.manda.bookingmanagerapi.service.CreateReservationData;
import com.jfcm.manda.bookingmanagerapi.service.GenerateUUIDService;
import com.jfcm.manda.bookingmanagerapi.service.LoggingService;
import com.jfcm.manda.bookingmanagerapi.utils.ResponseUtil;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationResources {

  @Autowired
  GenerateUUIDService generateUUIDService;
  @Autowired
  private ReservationRepository reservationRepository;
  @Autowired
  private CreateReservationData createReservationData;
  @Autowired
  private LoggingService LOG;

  /*
  {
    "id": "",
    "bookingDate": "",
    "room": "room1",
    "groupName": "cluster 1",
    "groupCode": "cluster-001",
    "activity": "fellowship",
    "bookedBy": "Ronald Cando",
    "clientId": "JF-000001",
    "withFee": "false",
    "totalFee": "",
    "status": ""
  }
  */
  @PostMapping(value = "/add-reservation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> addReservation(@RequestBody String input) throws JsonProcessingException {
    ReservationEntity data = createReservationData.reservationData(input);

    Executor executor = Executors.newCachedThreadPool();
    CompletableFuture.supplyAsync(() -> reservationRepository.save(data), executor);

    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(),
        String.format("Reservation has been made for id %s, with status %s", data.getId(), data.getStatus()), Constants.TRANSACTION_SUCCESS);

    return ResponseUtil.generateResponse(String.format("Reservation has been made for id %s, with status %s", data.getId(), data.getStatus()), HttpStatus.OK,
        data, Constants.TRANSACTION_SUCCESS);
  }
}