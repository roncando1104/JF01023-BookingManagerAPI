/**
 * {@link com.jfcm.manda.bookingmanagerapi.service.impl.CSVServiceImpl}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
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