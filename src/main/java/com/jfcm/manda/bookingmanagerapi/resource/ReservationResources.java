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
import com.jfcm.manda.bookingmanagerapi.model.Reservation;
import com.jfcm.manda.bookingmanagerapi.model.ReservationStatusEnum;
import com.jfcm.manda.bookingmanagerapi.repository.ReservationRepository;
import com.jfcm.manda.bookingmanagerapi.service.GenerateUUIDService;
import com.jfcm.manda.bookingmanagerapi.service.RequestDataService;
import com.jfcm.manda.bookingmanagerapi.utils.ResponseUtil;
import com.jfcm.manda.bookingmanagerapi.utils.Utilities;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
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
  private ReservationRepository reservationRepository;
  @Autowired
  private Utilities utilities;
  @Autowired
  private RequestDataService requestDataService;
  @Autowired GenerateUUIDService generateUUIDService;

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
    Reservation data = utilities.readfromInput(input, Reservation.class);
    String bookingId = generateUUIDService.generateUUID();
    String totalFee;
    String clientId = requestDataService.getClientIdByName(data.getBookedBy());

    data.setId(bookingId);
    data.setBookingDate(LocalDate.now());

    data.setClientId(clientId);
    boolean withFee = data.isWithFee();
    if(withFee){
      totalFee = Utilities.formatNumber(1500.00);
      data.setTotalFee(new BigDecimal(totalFee));
    } else {
      totalFee = Utilities.formatNumber(0.00);
      data.setTotalFee(new BigDecimal(totalFee));
    }

    data.setStatus(ReservationStatusEnum.FOR_APPROVAL);
    reservationRepository.save(data);

    return ResponseUtil.generateResponse(String.format("Reservation has been made for id %s, with status %s", bookingId, data.getStatus()), HttpStatus.OK, data, Constants.TRANSACTION_SUCCESS);
  }
}