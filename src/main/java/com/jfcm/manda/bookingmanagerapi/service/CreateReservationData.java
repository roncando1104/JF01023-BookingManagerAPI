package com.jfcm.manda.bookingmanagerapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfcm.manda.bookingmanagerapi.model.ReservationEntity;

@FunctionalInterface
public interface CreateReservationData {
  ReservationEntity getReservationData(String input) throws JsonProcessingException;

}
