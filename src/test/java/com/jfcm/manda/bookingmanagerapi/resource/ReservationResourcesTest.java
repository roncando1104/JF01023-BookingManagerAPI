/**
 * {@link com.jfcm.manda.bookingmanagerapi.resource.ReservationResourcesTest}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential
 * and proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.resource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jfcm.manda.bookingmanagerapi.model.ReservationEntity;
import com.jfcm.manda.bookingmanagerapi.utils.TestUtils;
import com.jfcm.manda.bookingmanagerapi.utils.Utilities;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlConfig.TransactionMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class ReservationResourcesTest {

  @Autowired
  private ReservationResources reservationResources;
  private ObjectMapper mapper;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private Utilities utilities;

  @Test
  @Sql(scripts = {"/availability-data.sql"}, config = @SqlConfig(encoding = "utf-8", transactionMode = TransactionMode.ISOLATED))
  @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
  void testAddReservation_return200() throws Exception {
    String date = String.valueOf(LocalDate.now().plusDays(2));
    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    //setup
    ReflectionTestUtils.setField(utilities, "allowedDaysToBook", "SUNDAY,MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY");
    ReflectionTestUtils.setField(reservationResources, "numberOfAllowedWeeks", 0);
    String replacement = "DELETE FROM AVAILABILITY_CALENDAR;\n"
        + "INSERT INTO availability_calendar(id, dates, sow_room1, sow_room2, room_1, room_2) VALUES ( '" + date.replace("-", "") + "', " + "'" + date + "'"
        + ", 'available', 'available', 'available', 'available' );";

    TestUtils.fileReaderAndWriter("src/test/resources", "availability-data.sql", replacement);
    ReservationEntity reservationData = TestUtils.readFileValue(mapper,
        "json/test-data/reservation/reservation-data.json", ReservationEntity.class);

    reservationData.setEventDate(LocalDate.now().plusDays(2));
    //action
    var result = mockMvc.perform(post("/booking-api/v1/reservations/add-reservation")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(reservationData))
            .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE)).andReturn();

    assertNotNull(result.getResponse().getContentAsString());
  }

  @Test
  @Sql(scripts = {"/availability-data.sql"}, config = @SqlConfig(encoding = "utf-8", transactionMode = TransactionMode.ISOLATED))
  @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
  void testAddReservation_dateFallsIntoADayNotAllowedToBook_return403_throwsInvalidInputException() throws Exception {
    String date = String.valueOf(LocalDate.now().plusDays(2));
    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    //setup
    ReflectionTestUtils.setField(utilities, "allowedDaysToBook", "mock1,mock2");
    String replacement = "DELETE FROM AVAILABILITY_CALENDAR;\n"
        + "INSERT INTO availability_calendar(id, dates, sow_room1, sow_room2, room_1, room_2) VALUES ( '" + date.replace("-", "") + "', " + "'" + date + "'"
        + ", 'available', 'available', 'available', 'available' );";

    TestUtils.fileReaderAndWriter("src/test/resources", "availability-data.sql", replacement);
    ReservationEntity reservationData = TestUtils.readFileValue(mapper,
        "json/test-data/reservation/reservation-data.json", ReservationEntity.class);

    reservationData.setEventDate(LocalDate.now().plusDays(2));
    //action
    var result = mockMvc.perform(post("/booking-api/v1/reservations/add-reservation")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(reservationData))
            .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isForbidden())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE)).andReturn();

    assertNotNull(result.getResponse().getContentAsString());
  }

  @Test
  @Sql(scripts = {"/availability-data.sql"}, config = @SqlConfig(encoding = "utf-8", transactionMode = TransactionMode.ISOLATED))
  @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
  void testAddReservation_dateIsWithinWeeksNotAllowedToBook_return403_throwsInvalidInputException() throws Exception {
    String date = String.valueOf(LocalDate.now().plusDays(2));
    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    //setup
    //change the allowable week to book to 2 weeks
    ReflectionTestUtils.setField(reservationResources, "numberOfAllowedWeeks", 2);
    String replacement = "DELETE FROM AVAILABILITY_CALENDAR;\n"
        + "INSERT INTO availability_calendar(id, dates, sow_room1, sow_room2, room_1, room_2) VALUES ( '" + date.replace("-", "") + "', " + "'" + date + "'"
        + ", 'available', 'available', 'available', 'available' );";

    TestUtils.fileReaderAndWriter("src/test/resources", "availability-data.sql", replacement);
    ReservationEntity reservationData = TestUtils.readFileValue(mapper,
        "json/test-data/reservation/reservation-data.json", ReservationEntity.class);

    reservationData.setEventDate(LocalDate.now().plusDays(2));
    //action
    var result = mockMvc.perform(post("/booking-api/v1/reservations/add-reservation")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(reservationData))
            .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isForbidden())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE)).andReturn();

    assertNotNull(result.getResponse().getContentAsString());
  }

  @Test
  @Sql(scripts = {"/availability-data.sql"}, config = @SqlConfig(encoding = "utf-8", transactionMode = TransactionMode.ISOLATED))
  @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
  void testAddReservation_dateIsNoLongerAvailable_return400() throws Exception {
    String date = String.valueOf(LocalDate.now().plusDays(2));
    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    //setup
    //change the allowable week to book to 2 weeks
    ReflectionTestUtils.setField(reservationResources, "numberOfAllowedWeeks", 0);
    String replacement = "DELETE FROM AVAILABILITY_CALENDAR;\n"
        + "INSERT INTO availability_calendar(id, dates, sow_room1, sow_room2, room_1, room_2) VALUES ( '" + date.replace("-", "") + "', " + "'" + date + "'"
        + ", 'available', 'available', 'available', 'available' );";

    TestUtils.fileReaderAndWriter("src/test/resources", "availability-data.sql", replacement);
    ReservationEntity reservationData = TestUtils.readFileValue(mapper,
        "json/test-data/reservation/reservation-data.json", ReservationEntity.class);
    //this date will not be found in database, hence it's not available
    reservationData.setEventDate(LocalDate.now().plusDays(15));
    //action
    var result = mockMvc.perform(post("/booking-api/v1/reservations/add-reservation")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(reservationData))
            .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE)).andReturn();

    assertNotNull(result.getResponse().getContentAsString());
  }

  @Test
  @Sql(scripts = {"/availability-data.sql"}, config = @SqlConfig(encoding = "utf-8", transactionMode = TransactionMode.ISOLATED))
  @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
  void testAddReservation_noBookingFoundForClientId_return400() throws Exception {
    String date = String.valueOf(LocalDate.now().plusDays(2));
    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    //setup
    //change the allowable week to book to 2 weeks
    ReflectionTestUtils.setField(reservationResources, "numberOfAllowedWeeks", 0);
    String replacement = "DELETE FROM AVAILABILITY_CALENDAR;\n"
        + "INSERT INTO availability_calendar(id, dates, sow_room1, sow_room2, room_1, room_2) VALUES ( '" + date.replace("-", "") + "', " + "'" + date + "'"
        + ", 'available', 'available', 'available', 'available' );";

    TestUtils.fileReaderAndWriter("src/test/resources", "availability-data.sql", replacement);
    ReservationEntity reservationData = TestUtils.readFileValue(mapper,
        "json/test-data/reservation/reservation-data-not-found.json", ReservationEntity.class);
    //this date will not be found in database, hence it's not available
    reservationData.setEventDate(LocalDate.now().plusDays(15));
    //action
    var result = mockMvc.perform(post("/booking-api/v1/reservations/add-reservation")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(reservationData))
            .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isForbidden())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE)).andReturn();

    assertNotNull(result.getResponse().getContentAsString());
  }

  //NOTE: This method fails the test.  If it fails, just build the project and execute mvn clean install
//  @AfterAll
//  static void afterAllTest() {
//    String oldString = "DELETE FROM AVAILABILITY_CALENDAR;\n"
//        + "INSERT INTO availability_calendar(id, dates, sow_room1, sow_room2, room_1, room_2) VALUES ( 'dateId', 'date', 'available', 'available', 'available', 'available' );";
//    fileReaderAndWriter("src/test/resources", "availability-data.sql", oldString);
//  }
}