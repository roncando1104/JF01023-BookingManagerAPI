/*
 *  CreateReservationData.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfcm.manda.bookingmanagerapi.model.ReservationEntity;
import com.jfcm.manda.bookingmanagerapi.model.ReservationStatusEnum;
import com.jfcm.manda.bookingmanagerapi.utils.Utilities;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateReservationData {

  @Autowired
  private Utilities utilities;
  @Autowired
  private GenerateUUIDService generateUUIDService;
  @Autowired
  private RequestDataService requestDataService;

  public ReservationEntity reservationData(String input) throws JsonProcessingException {
    ReservationEntity data = utilities.readfromInput(input, ReservationEntity.class);
    String bookingId = generateUUIDService.generateUUID();
    String clientId = requestDataService.getClientIdByName(data.getBookedBy());

    data.setId(bookingId);
    data.setBookingDate(LocalDate.now());

    data.setClientId(clientId);
    String totalFee = data.isWithFee() ? Utilities.formatNumber(1500) : Utilities.formatNumber(0);
    data.setTotalFee(totalFee);

    data.setStatus(ReservationStatusEnum.FOR_APPROVAL);
    return data;
  }

}