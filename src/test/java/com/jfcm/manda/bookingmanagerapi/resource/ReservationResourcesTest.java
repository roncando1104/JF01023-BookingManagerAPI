/*
 *  ReservationResourcesTest.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.resource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jfcm.manda.bookingmanagerapi.model.ReservationEntity;
import com.jfcm.manda.bookingmanagerapi.service.impl.GenerateUUIDService;
import com.jfcm.manda.bookingmanagerapi.utils.TestUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class ReservationResourcesTest {

  @Autowired
  GenerateUUIDService generateUUIDService;
  private ObjectMapper mapper;
  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
  void testAddReservation_return200() throws Exception {
    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    ReservationEntity reservationData = TestUtils.readFileValue(mapper,
        "json/test-data/reservation-data.json", ReservationEntity.class);

    var result = mockMvc.perform(post("/booking-api/v1/reservations/add-reservation")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(reservationData))
            .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE)).andReturn();

    assertNotNull(result.getResponse().getContentAsString());
  }
}