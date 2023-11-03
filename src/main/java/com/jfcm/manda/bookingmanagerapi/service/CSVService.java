package com.jfcm.manda.bookingmanagerapi.service;

import com.jfcm.manda.bookingmanagerapi.model.ReservationEntity;
import java.util.List;

@FunctionalInterface
public interface CSVService {
  void writeCSV(List<ReservationEntity> reservationTransactions);
}
