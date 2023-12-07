/**
 * {@link com.jfcm.manda.bookingmanagerapi.resource.ReservationResources}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfcm.manda.bookingmanagerapi.constants.Constants;
import com.jfcm.manda.bookingmanagerapi.dao.response.CommonResponse;
import com.jfcm.manda.bookingmanagerapi.exception.GenericBookingException;
import com.jfcm.manda.bookingmanagerapi.exception.InvalidInputException;
import com.jfcm.manda.bookingmanagerapi.exception.MultipleBookingException;
import com.jfcm.manda.bookingmanagerapi.model.ReservationEntity;
import com.jfcm.manda.bookingmanagerapi.model.RoomStatusEnum;
import com.jfcm.manda.bookingmanagerapi.repository.AvailableRoomOnDateRepository;
import com.jfcm.manda.bookingmanagerapi.repository.ReservationRepository;
import com.jfcm.manda.bookingmanagerapi.service.CreateReservationData;
import com.jfcm.manda.bookingmanagerapi.service.impl.GenerateUUIDService;
import com.jfcm.manda.bookingmanagerapi.service.impl.LoggingService;
import com.jfcm.manda.bookingmanagerapi.service.impl.RequestDataService;
import com.jfcm.manda.bookingmanagerapi.utils.Utilities;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking-api/v1/reservations")
public class ReservationResources {

  private final LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("Asia/Manila"));
  @Autowired
  GenerateUUIDService generateUUIDService;
  @Autowired
  private ReservationRepository reservationRepository;
  @Autowired
  private AvailableRoomOnDateRepository availableRoomOnDateRepository;
  @Autowired
  private CreateReservationData createReservationData;
  @Autowired
  private RequestDataService requestDataService;
  @Autowired
  private LoggingService LOG;
  @Autowired
  private Utilities utilities;

  @Value("#{new Long('${booking.allowable.number-of-weeks}')}")
  private long numberOfAllowedWeeks;
  @Value("#{new Long('${booking.allowable.number-of-months}')}")
  private long numberOfMonthsUntilNextBooking;
  //@Value("${booking.allowable.filter-flag.enabled}")
  //private boolean isFilterAllowed;

  /**
   * @request
   * This method accepts below payload
   * {
   *     "id": "",
   *     "bookingDate": "",
   *     "eventDate": "",
   *     "room": "",
   *     "groupName": "",
   *     "groupCode": "",
   *     "activity": "",
   *     "bookedBy": "",
   *     "clientId": "",
   *     "withFee": "",
   *     "totalFee": "",
   *     "status": ""
   *   }
   * @param input string
   * @id is auto generated as UUID.  It should be blank in request payload.
   * @return ResponseEntity OK
   * @throws JsonProcessingException if an error during JSON processing occurs
   */

  @PostMapping(value = "/add-reservation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> addReservation(@RequestBody String input) throws JsonProcessingException {
    ReservationEntity data = createReservationData.getReservationData(input);
    //This implementation was changed.  Checked if it still works properly.  Removed if ok.
    //var isLastBookingMadeAfterNumberOfMonths = requestDataService.noBookingInSpecifiedNumberOfMonthsFilter(data.getClientId(), data.getEventDate(), data.getGroupCode());
//    System.out.println("CON: " + isLastBookingMadeAfterNumberOfMonths + " " + isFilterAllowed);
//    if (!isLastBookingMadeAfterNumberOfMonths && isFilterAllowed) {
//      throw new GenericBookingException(String.format("Denied -- You are booking on %s, but you just booked from last %s months(s)", data.getEventDate(), numberOfMonthsUntilNextBooking));
//    }
    checkIfBookingIsAllowed(data);

    var monthlyBookingCount = requestDataService.checkIfMultipleBookings(data.getClientId(), data.getEventDate(), data.getBookedBy(), data.getGroupCode());
    //Will prohibit a Client with same group to make multiple bookings in same month
    if (monthlyBookingCount == 1) {
      throw new MultipleBookingException("You are only allowed to book once a month from the selected month.");
    }
    /**
     * TODO:  Check below items for implementation before saving the booking
     * 1. Check if the event date is still available and not date from the pass - DONE
     * 2. Needs to update the availability_calendar table (availability will be checked against this table) - DONE
     * 3. Create a logic that prohibits a user from booking multiple dates in 1 or 2 months - DONE configurable using numberOfMonthsUntilNextBooking
     */
    //check if a roomtype is available on a given date
    String result = availableRoomOnDateRepository.checkIfRoomIsAvailableOnAGivenDate(data.getRoom(), RoomStatusEnum.available.name(), String.valueOf(data.getEventDate()));
    String resultStr = result.replaceAll("[\\[\\](){}]", "");
    CommonResponse response;

    //check if the date is within the allowed days to book an event
    boolean isDayAllowed = utilities.isAllowedOnGivenDays(data.getEventDate());
    if (!isDayAllowed) {
      //if the day is not within the allowed list of days, isDayAllowed return falls and throws below exception
      throw new InvalidInputException(String.format("%s falls on %s.  You cannot book on this day!", data.getEventDate(), data.getEventDate().getDayOfWeek().toString()));
    }
    //this will check if the year of date is not this year.
    if (data.getEventDate().getYear() == LocalDate.now().plusYears(1).getYear()) {
      throw new InvalidInputException(String.format("Date %s falls for next year.  Those dates are not yet available.", data.getEventDate()));
    }
    //check if date is not from the pass and within # of weeks period. throw exception if true
    if (data.getEventDate().isBefore(LocalDate.now().plusWeeks(numberOfAllowedWeeks))) {
      throw new InvalidInputException(String.format("Event date should be future dated or not within %s week(s) period. Event date %s doesn't meet the condition.", numberOfAllowedWeeks, data.getEventDate()));
    } else {
      //if date is future, check if a room has value of 'available'
      if (StringUtils.equalsIgnoreCase(resultStr, RoomStatusEnum.available.name())) {
        //if available, then persist reservation to reservation and availability calendar tables
        Executor executor = Executors.newCachedThreadPool();
        CompletableFuture.supplyAsync(() -> reservationRepository.save(data), executor);
        CompletableFuture.supplyAsync(() -> availableRoomOnDateRepository.updateRoomStatusOnAGivenDate
            (data.getRoom(), RoomStatusEnum.reserved.name(), String.valueOf(data.getEventDate())), executor);

        LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(),
            String.format("Reservation has been made for id %s, with status %s on %s", data.getId(),
                data.getStatus(), data.getEventDate()), Constants.TRANSACTION_SUCCESS);

        response = getResponse(data, HttpStatus.CREATED.value(),
            Constants.TRANSACTION_SUCCESS, String.format("Reservation has been made for id %s, with status %s on %s", data.getId(), data.getStatus(), data.getEventDate()));

      } else {
        //if room is no longer available on a given date, return bad request to client
        LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(),
            String.format("Request Denied -- %s is no longer available on %s", data.getRoom(), data.getEventDate()), Constants.TRANSACTION_FAILED);

        response = getResponse(data, HttpStatus.BAD_REQUEST.value(),
            Constants.TRANSACTION_FAILED, String.format("Request Denied -- %s is no longer available on %s", data.getRoom(), data.getEventDate()));
        return ResponseEntity.badRequest().body(response);
      }
    }

    return ResponseEntity.ok(response);
  }

  @ConditionalOnProperty(value = "booking.allowable.filter-flag.enabled", havingValue = "true")
  public void checkIfBookingIsAllowed(ReservationEntity data) {
    var isLastBookingMadeAfterNumberOfMonths = requestDataService.noBookingInSpecifiedNumberOfMonthsFilter(data.getClientId(),
        data.getEventDate(), data.getGroupCode());

    if (Boolean.FALSE.equals(isLastBookingMadeAfterNumberOfMonths)) {
      throw new GenericBookingException(String.format("Denied -- You are booking on %s, but you just booked from last %s months(s)",
          data.getEventDate(), numberOfMonthsUntilNextBooking));
    }
  }

  /**
   * If a delete method will be created, consider the ff:
   * - Deleting a reservation should also reset the room involved to available in availability_calendar table
   * - Deleting or updating a room availability in availability_calendar table should not be allowed
   */
  private CommonResponse getResponse(Object data, int status, String respCode, String msg) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    String dateTokenCreated = formatter.format(dateTime);

    return CommonResponse.builder()
        .timestamp(dateTokenCreated)
        .info(data)
        .status(status)
        .responseCode(respCode)
        .message(msg)
        .build();
  }
}