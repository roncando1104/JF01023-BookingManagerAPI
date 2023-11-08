/**
 * {@link com.jfcm.manda.bookingmanagerapi.resource.AvailableDatesResources}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential
 * and proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfcm.manda.bookingmanagerapi.constants.Constants;
import com.jfcm.manda.bookingmanagerapi.dao.response.JwtAuthenticationResponse;
import com.jfcm.manda.bookingmanagerapi.model.AvailabilityCalendarEntity;
import com.jfcm.manda.bookingmanagerapi.model.RoomStatusEnum;
import com.jfcm.manda.bookingmanagerapi.repository.AvailableDateRepository;
import com.jfcm.manda.bookingmanagerapi.repository.AvailableRoomOnDateRepository;
import com.jfcm.manda.bookingmanagerapi.service.impl.GenerateUUIDService;
import com.jfcm.manda.bookingmanagerapi.service.impl.LoggingService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking-api/v1/available-dates")
public class AvailableDatesResources {

  private final LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("Asia/Manila"));
  @Autowired
  private AvailableDateRepository availableDateRepository;
  @Autowired
  private LoggingService LOG;
  @Autowired
  private GenerateUUIDService generateUUIDService;

  @GetMapping(value = "/all-dates", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getAllDates() throws JsonProcessingException {
    List<AvailabilityCalendarEntity> data = availableDateRepository.findAll();

    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("Cluster groups retrieved successfully. %s record(s)", data.size()),
        Constants.TRANSACTION_SUCCESS);
    var response = getJwtAuthenticationResponse(data, HttpStatus.OK.value(),
        String.format("All %s day(s) has been retrieved", data.size()));
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "/available-room/{date}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getAvailableRoomsOnAGivenDate(@PathVariable(value = "date") String date) {
    List<AvailabilityCalendarEntity> availableRooms = availableDateRepository.checkRoomsAvailableOnAGivenDate(date);
    //NOTE: This is the sample query of getting the status of a room availability on a given date.
    // Will use this to check if the room a user is booking on a given date is still available from availability_calendar table
    // Use this logic below in ReservationResources class before saving the reservation
    //String result = availableRoomOnDateRepository.checkIfRoomIsAvailableOnAGivenDate("room1", "available", "2023-11-04");
    //System.out.println("RES: " + result);

    var response = getJwtAuthenticationResponse(availableRooms, HttpStatus.OK.value(),
        String.format("Check the availability: room1 is %s | room2 is %s | sow room1 is %s | sow room2 is %s",
            availableRooms.stream().findAny().map(AvailabilityCalendarEntity::getRoom1).orElse(RoomStatusEnum.not_define),
            availableRooms.stream().findAny().map(AvailabilityCalendarEntity::getRoom2).orElse(RoomStatusEnum.not_define),
            availableRooms.stream().findAny().map(AvailabilityCalendarEntity::getSowRoom1).orElse(RoomStatusEnum.not_define),
            availableRooms.stream().findAny().map(AvailabilityCalendarEntity::getSowRoom2).orElse(RoomStatusEnum.not_define)));
    return ResponseEntity.ok(response);
  }

  //TODO: Create method that takes room column name as parameter and check its availability
  private JwtAuthenticationResponse getJwtAuthenticationResponse(Object data, int status, String msg) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    String dateTokenCreated = formatter.format(dateTime);

    return JwtAuthenticationResponse.builder()
        .timestamp(dateTokenCreated)
        .data(data)
        .status(status)
        .responsecode(Constants.TRANSACTION_SUCCESS)
        .message(msg)
        .build();
  }
}