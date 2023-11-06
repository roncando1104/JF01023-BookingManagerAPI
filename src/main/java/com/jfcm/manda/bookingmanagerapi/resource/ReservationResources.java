/**
 * {@link com.jfcm.manda.bookingmanagerapi.resource.ReservationResources}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfcm.manda.bookingmanagerapi.constants.Constants;
import com.jfcm.manda.bookingmanagerapi.dao.response.JwtAuthenticationResponse;
import com.jfcm.manda.bookingmanagerapi.model.ReservationEntity;
import com.jfcm.manda.bookingmanagerapi.repository.ReservationRepository;
import com.jfcm.manda.bookingmanagerapi.service.CreateReservationData;
import com.jfcm.manda.bookingmanagerapi.service.impl.GenerateUUIDService;
import com.jfcm.manda.bookingmanagerapi.service.impl.LoggingService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking-api/v1/reservations")
public class ReservationResources {

  private final LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("Asia/Manila"));
  @Autowired
  GenerateUUIDService generateUUIDService;
  @Autowired
  private ReservationRepository reservationRepository;
  @Autowired
  private CreateReservationData createReservationData;
  @Autowired
  private LoggingService LOG;

  /**
   * This method accepts below payload (this is just a sample)
   * {
   *     "id": "",
   *     "bookingDate": "",
   *     "room": "room1",
   *     "groupName": "cluster 1",
   *     "groupCode": "cluster-001",
   *     "activity": "fellowship",
   *     "bookedBy": "Ronald Cando",
   *     "clientId": "JF-000001",
   *     "withFee": "false",
   *     "totalFee": "",
   *     "status": ""
   *   }
   * @param input string
   * @id is auto generated as UUID
   * @return ResponseEntity OK
   * @throws JsonProcessingException if an error during JSON processing occurs
   */

  @PostMapping(value = "/add-reservation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> addReservation(@RequestBody String input) throws JsonProcessingException {
    ReservationEntity data = createReservationData.getReservationData(input);

    /**
     * TODO:  Check below items for implementation before saving the booking
     * 1. Check if the event date is still available
     * 2. Needs to update the availability_calendar table (availability will be checked against this table)
     * 3. Create a logic that prohibits a user from booking multiple dates in 1 or 2 months
     */
    Executor executor = Executors.newCachedThreadPool();
    CompletableFuture.supplyAsync(() -> reservationRepository.save(data), executor);

    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(),
        String.format("Reservation has been made for id %s, with status %s", data.getId(), data.getStatus()), Constants.TRANSACTION_SUCCESS);

    var response = getJwtAuthenticationResponse(data, HttpStatus.CREATED.value(),
        Constants.TRANSACTION_SUCCESS, String.format("Reservation has been made for id %s, with status %s", data.getId(), data.getStatus()));

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