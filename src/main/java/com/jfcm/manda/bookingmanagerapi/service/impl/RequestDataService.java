/**
 * {@link com.jfcm.manda.bookingmanagerapi.service.impl.RequestDataService}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfcm.manda.bookingmanagerapi.exception.InvalidInputException;
import com.jfcm.manda.bookingmanagerapi.model.BookedEventsEntity;
import com.jfcm.manda.bookingmanagerapi.model.ReservationEntity;
import com.jfcm.manda.bookingmanagerapi.repository.BookedEventsRepository;
import com.jfcm.manda.bookingmanagerapi.repository.ReservationRepository;
import com.jfcm.manda.bookingmanagerapi.repository.UsersRepository;
import com.jfcm.manda.bookingmanagerapi.utils.Utilities;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestDataService {

  @Autowired
  private Utilities utilities;
  @Autowired
  private UsersRepository usersRepository;
  @Autowired
  private BookedEventsRepository bookedEventsRepository;
  @Autowired
  private LoggingService LOG;

  public String getClientIdByName(String fullName) {
    String fName = utilities.splitFullName(fullName, "firstName");
    String lName = utilities.splitFullName(fullName, "lastName");

    return usersRepository.findUserIdByFullName(fName, lName);
  }

  public List<BookedEventsEntity> getBookedEventDateByClientId(String clientId) {

    var bookedEventsFromThePastByAClientId = bookedEventsRepository.findReservationBookedEventDateByClientId(clientId);
    bookedEventsFromThePastByAClientId.stream().filter(data -> data.getClientId().equals(clientId)).findFirst()
        .orElseThrow(() -> new InvalidInputException(String.format("No Booking found for id %s", clientId)));
    return bookedEventsFromThePastByAClientId;
  }

  public long checkIfMultipleBookings(String clientId, LocalDate month, String bookedBy, String groupCode){
    List<BookedEventsEntity> result = getBookedEventDateByClientId(clientId);
    //System.out.println("Count: " + result);
    return result.stream().filter(data -> data.getClientId().equals(clientId) && data.getBookedBy().equals(bookedBy)
        && data.getGroupCode().equals(groupCode) && data.getEventDate().getMonthValue() == month.getMonthValue()).count();
  }

//  public void noBookingInSpecifiedNumberOfMonthsFilter(LocalDate date) {
//    if (LocalDate.now().getMonthValue() == date.getMonthValue()) {
//
//    }
//  }
}