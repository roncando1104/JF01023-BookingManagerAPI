/*
 *  CSVServiceImpl.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.service.impl;

import com.jfcm.manda.bookingmanagerapi.model.ReservationEntity;
import com.jfcm.manda.bookingmanagerapi.service.CSVService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CSVServiceImpl implements CSVService {

  @Override
  public void writeCSV(List<ReservationEntity> reservationTransactions) {
      //TODO: Implement CSV Report generator
  }
}