/**
 * {@link com.jfcm.manda.bookingmanagerapi.resource.ReservationResourcesTest}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class ReservationResourcesTest {

  private ObjectMapper mapper;
  @Autowired
  private MockMvc mockMvc;

  public void fileReaderAndWriter(String folderPath, String fileName, String strReplacement) {
    File file = new File(folderPath);
    String absolutePath = file.getAbsolutePath();
    FileWriter fileWriter = null;
    String oldContent = "";
    try {
      FileReader fileReader = new FileReader(absolutePath + "/" + fileName);
      BufferedReader bufferReader = new BufferedReader(fileReader);
      String fileData;

      try {
        while ((fileData = bufferReader.readLine()) != null) {
          oldContent = oldContent + fileData + System.lineSeparator();
        }
        fileWriter = new FileWriter(absolutePath + "/" + fileName);
        fileWriter.write(strReplacement);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        try {
          //Closing the resources
          fileWriter.close();
          fileReader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  @Test
  @Sql(scripts = {"/availability-data.sql"}, config = @SqlConfig(encoding = "utf-8", transactionMode = TransactionMode.ISOLATED))
  @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
  void testAddReservation_return200() throws Exception {
    String date = String.valueOf(LocalDate.now().plusDays(2));
    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    //setup
    String replacement = "DELETE FROM AVAILABILITY_CALENDAR;\n"
        + "INSERT INTO availability_calendar(id, dates, sow_room1, sow_room2, room_1, room_2) VALUES ( '" + date.replace("-", "") + "', " + "'" + date + "'"
        + ", 'available', 'available', 'available', 'available' );";

    fileReaderAndWriter("src/test/resources", "availability-data.sql", replacement);
    ReservationEntity reservationData = TestUtils.readFileValue(mapper,
        "json/test-data/reservation-data.json", ReservationEntity.class);

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

  //NOTE: This method fails the test.  The fileReaderAndWriter cannot be static.  If it fails, just build the project and execute mvn clean install
//  @AfterAll
//  static void afterAllTest() {
//    String oldString = "DELETE FROM AVAILABILITY_CALENDAR;\n"
//        + "INSERT INTO availability_calendar(id, dates, sow_room1, sow_room2, room_1, room_2) VALUES ( 'dateId', 'date', 'available', 'available', 'available', 'available' );";
//    fileReaderAndWriter("src/test/resources", "availability-data.sql", oldString);
//  }
}