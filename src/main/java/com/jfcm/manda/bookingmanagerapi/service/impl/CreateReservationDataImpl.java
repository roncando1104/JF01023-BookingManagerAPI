/**
 * {@link com.jfcm.manda.bookingmanagerapi.service.impl.CreateReservationDataImpl}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfcm.manda.bookingmanagerapi.model.ReservationEntity;
import com.jfcm.manda.bookingmanagerapi.model.ReservationStatusEnum;
import com.jfcm.manda.bookingmanagerapi.service.CreateReservationData;
import com.jfcm.manda.bookingmanagerapi.utils.Utilities;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateReservationDataImpl implements CreateReservationData {

  @Autowired
  private Utilities utilities;
  @Autowired
  private GenerateUUIDService generateUUIDService;
  @Autowired
  private RequestDataService requestDataService;

  @Override
  public ReservationEntity getReservationData(String input) throws JsonProcessingException {
    ReservationEntity data = utilities.readfromInput(input, ReservationEntity.class);
    String bookingId = generateUUIDService.generateUUID();
    String clientId = requestDataService.getClientIdByName(data.getBookedBy());

    data.setId(bookingId);
    data.setBookingDate(Utilities.formatDate(LocalDate.now()));

    data.setClientId(clientId);
    var activity = data.getActivity();
    List<String> listOfActivityWithNoFee = Arrays.asList("Fellowship", "Elders Meeting", "Ministry Meeting", "Cluster Meeting");
    boolean matchedActivityWithNoFee = listOfActivityWithNoFee.stream().anyMatch(list -> list.equalsIgnoreCase(activity));
    if (matchedActivityWithNoFee) {
      data.setWithFee(false);
    } else {
      data.setWithFee(true);
    }
    String totalFee = data.isWithFee() ? Utilities.formatNumber(1500) : "NO FEE";
    data.setTotalFee(totalFee);

    data.setStatus(ReservationStatusEnum.FOR_APPROVAL);
    return data;
  }

}