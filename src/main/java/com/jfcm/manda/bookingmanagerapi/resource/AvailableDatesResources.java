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
import com.jfcm.manda.bookingmanagerapi.dao.response.CommonResponse;
import com.jfcm.manda.bookingmanagerapi.model.AvailabilityCalendarEntity;
import com.jfcm.manda.bookingmanagerapi.model.RoomStatusEnum;
import com.jfcm.manda.bookingmanagerapi.repository.AvailableDateRepository;
import com.jfcm.manda.bookingmanagerapi.service.JwtService;
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
  @Autowired
  private JwtService jwtService;

  /**
   * @implNote This resource will only retrieve dates from availability_calendar.
   * @implNote Deleting, updating, & posting is not allowed as it is managed in reservation endpoints
   * @implNote Populating the availability_calendar with dates is handled through
   * {@link com.jfcm.manda.bookingmanagerapi.service.impl.AvailabilityCalendarServiceImpl}
   */

  @GetMapping(value = "/all-dates", headers = {"content-type=*/*"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getAllDates() throws JsonProcessingException {
    List<AvailabilityCalendarEntity> data = availableDateRepository.findAll();

    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("Availability dates retrieved successfully. %s record(s)", data.size()),
        Constants.TRANSACTION_SUCCESS);
    var response = getResponse(data, HttpStatus.OK.value(),
        String.format("All %s day(s) has been retrieved", data.size()));
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "/available-room/{date}", headers = {"content-type=*/*"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getAvailableRoomsOnAGivenDate(@PathVariable(value = "date") String date) {
    List<AvailabilityCalendarEntity> availableRooms = availableDateRepository.checkRoomsAvailableOnAGivenDate(date);
    //NOTE:
    // Will use this to check if the room a user is booking on a given date is still available from availability_calendar table
    // Use this logic below in ReservationResources class before saving the reservation
    //String result = availableRoomOnDateRepository.checkIfRoomIsAvailableOnAGivenDate("room1", "available", "2023-11-04");

    var response = getResponse(availableRooms, HttpStatus.OK.value(),
        String.format("Check the availability: Room-1 is %s | Room-2 is %s | Sow Room-1 is %s | Sow Room-2 is %s",
            availableRooms.stream().findAny().map(AvailabilityCalendarEntity::getRoom1).orElse(RoomStatusEnum.not_define),
            availableRooms.stream().findAny().map(AvailabilityCalendarEntity::getRoom2).orElse(RoomStatusEnum.not_define),
            availableRooms.stream().findAny().map(AvailabilityCalendarEntity::getSowRoom1).orElse(RoomStatusEnum.not_define),
            availableRooms.stream().findAny().map(AvailabilityCalendarEntity::getSowRoom2).orElse(RoomStatusEnum.not_define)));
    return ResponseEntity.ok(response);
  }

  private CommonResponse getResponse(Object data, int status, String msg) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    String dateTokenCreated = formatter.format(dateTime);

    return CommonResponse.builder()
        .timestamp(dateTokenCreated)
        .info(data)
        .status(status)
        .responseCode(Constants.TRANSACTION_SUCCESS)
        .message(msg)
        .build();
  }
}