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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RequestDataService {

  @Autowired
  private Utilities utilities;
  @Autowired
  private UsersRepository usersRepository;
  @Autowired
  private BookedEventsRepository bookedEventsRepository;

  @Value("#{new Long('${booking.allowable.number-of-months}')}")
  private long numberOfMonthsUntilNextBooking;

  public String getClientIdByName(String fullName) {
    String fName = utilities.splitFullName(fullName, "firstName");
    String lName = utilities.splitFullName(fullName, "lastName");

    return usersRepository.findUserIdByFullName(fName, lName);
  }

  public List<BookedEventsEntity> getBookedEventDateByClientId(String clientId, String groupCode) {

    var bookedEventsFromThePastByAClientId = bookedEventsRepository.findReservationBookedEventDateByClientId(clientId);
    List<BookedEventsEntity> resultList = new ArrayList<>();
    for (BookedEventsEntity data : bookedEventsFromThePastByAClientId) {
      if (data.getClientId().equals(clientId) && data.getGroupCode().equals(groupCode)) {
        resultList.add(data);
      }
    }
    return resultList;
//    return bookedEventsFromThePastByAClientId.stream().filter(data -> data.getClientId().equals(clientId)
//    && data.getGroupCode().equals(groupCode)).collect(Collectors.toList());
        //findFirst().orElseThrow(() -> new InvalidInputException(String.format("No Booking found for id %s", clientId)));
    //we can still do the above but don't throw exception if no booking has found
    //System.out.println("FF: " + bookedEventsFromThePastByAClientId);
    //return bookedEventsFromThePastByAClientId;
  }

  public long checkIfMultipleBookings(String clientId, LocalDate month, String bookedBy, String groupCode){
    List<BookedEventsEntity> result = getBookedEventDateByClientId(clientId, groupCode);
    //System.out.println("Count: " + result);
    return result.stream().filter(data -> data.getClientId().equals(clientId) && data.getBookedBy().equals(bookedBy)
        && data.getGroupCode().equals(groupCode) && data.getEventDate().getMonthValue() == month.getMonthValue()).count();
  }


  public boolean noBookingInSpecifiedNumberOfMonthsFilter(String clientId, LocalDate date, String groupCode) {
    List<BookedEventsEntity> result = getBookedEventDateByClientId(clientId, groupCode);
    // For Testing
    //TODO: Removed once testing is verified
//    result.stream().filter(data -> data.getGroupCode().equals(groupCode) && data.getEventDate().getMonth().equals(date.getMonth())).forEach(i -> {
//      System.out.println("D: " + i.getEventDate());
//      System.out.println("D After: " + i.getEventDate().plusMonths(1));
//      System.out.println("T or F: " + i.getEventDate().plusMonths(1).isAfter(date.plusMonths(2)));
//    });
    //This will check for booking that matches the ff:
    // client id from DB == client id from payload
    // group code from DB == group code from payload
    // event date month from DB == event month from payload (this will get the not just the exact date, but month if has the same from DB)
    // the last date of booking from db will be added with # of months, and check if the event date from payload is after it.
    // Event date from payload isAfter Event date from DB + 2 months. If false, throw error.
    var value = result.stream().filter(data -> data.getClientId().equals(clientId) && data.getGroupCode().equals(groupCode)
    && data.getEventDate().getMonth().equals(date.getMonth())).findFirst();
    if (!ObjectUtils.isEmpty(value)) {
      String lastBookingDate = value.stream().map(BookedEventsEntity::getEventDate).toList().toString().
          replaceAll("[\\[\\](){}]", "");
      LocalDate resultStrParsedToLocalDate = LocalDate.parse(lastBookingDate);
      return date.isAfter(resultStrParsedToLocalDate.plusMonths(numberOfMonthsUntilNextBooking));
      //BELOW IS OLD IMPLEMENTATION
      //String lastBookingDate = value.stream().map(BookedEventsEntity::getEventDate).toList().toString();
      //String resultStr = lastBookingDate.replaceAll("[\\[\\](){}]", "");
    } else {
      return true;
    }



//    return result.stream().anyMatch(data -> data.getClientId().equals(clientId)
//        && data.getGroupCode().equals(groupCode)
//        && data.getEventDate().getMonth().equals(date.getMonth())
//        && date.isAfter(data.getEventDate().plusMonths(numberOfMonthsUntilNextBooking)));
  }
}