/*
 *  PersistYearlyDates.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfcm.manda.bookingmanagerapi.constants.Constants;
import com.jfcm.manda.bookingmanagerapi.exception.DataAlreadyExistException;
import com.jfcm.manda.bookingmanagerapi.model.AvailabilityCalendarEntity;
import com.jfcm.manda.bookingmanagerapi.repository.AvailableDateRepository;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PersistYearlyDates {

  @Autowired
  private AvailableDateRepository availableDateRepository;
  @Autowired
  private LoggingService LOG;
  @Autowired
  private GenerateUUIDService generateUUIDService;

  //@Scheduled(cron = "@annually") //"0 0 1 1 *"
  //@Scheduled(cron = "0 */1 * * * *")
  @Scheduled(cron = "0 0 0 1 1 *")
  public void generateYearDates() throws JsonProcessingException {

    List<String> allMonths = Arrays.stream(Month.values()).map(Enum::name).toList();
    JSONArray jsonArray = new JSONArray();

    for (String months : allMonths) {
      YearMonth yearMonth = YearMonth.of(YearMonth.now().getYear(), Month.valueOf(months));
      LocalDate firstOfMonth = yearMonth.atDay(1);
      LocalDate firstOfFollowingMonth = yearMonth.plusMonths(1).atDay(1);
      List<String> daysOfAMonth = firstOfMonth.datesUntil(firstOfFollowingMonth).map(LocalDate::toString).toList();

      for (String eachDay : daysOfAMonth) {
        Map<String, String> map = new LinkedHashMap<>();
        String dateId = eachDay.replace("-", "");

        Optional<AvailabilityCalendarEntity> findDateId = availableDateRepository.findById(dateId);

        if (findDateId.isPresent()) {
          LOG.error(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("Dates already exist for year %s", YearMonth.now().getYear()),
              Constants.TRANSACTION_FAILED);
          throw new DataAlreadyExistException("Data already Exist");
        } else {
          map.put("id", dateId);
          map.put("availableDate", eachDay);
          jsonArray.put(map);

          availableDateRepository.saveAll(List.of(new AvailabilityCalendarEntity(dateId, eachDay)));
        }
      }
    }
    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(),
        String.format("New set of dates added with count %s for year %s", jsonArray.length(), YearMonth.now().getYear()), Constants.TRANSACTION_SUCCESS);
  }
}