/*
 *  UsersResourcesTest.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.resource;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfcm.manda.bookingmanagerapi.model.UsersEntity;
import com.jfcm.manda.bookingmanagerapi.repository.UsersRepository;
import com.jfcm.manda.bookingmanagerapi.service.impl.GenerateUUIDService;
import com.jfcm.manda.bookingmanagerapi.service.impl.LoggingService;
import com.jfcm.manda.bookingmanagerapi.utils.TestUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class UsersResourcesTest {

  private final ObjectMapper mapper = new ObjectMapper();
  @Autowired
  private MockMvc mockMvc;
  @Mock
  private UsersRepository usersRepository;
  @Mock
  private GenerateUUIDService generateUUIDService;
  @Mock
  private LoggingService loggingService;
  @Mock
  private UsersResources usersResources;

  @Test
  @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
  void testGetAllUsers_return200() throws Exception {
    List<UsersEntity> clustersData = TestUtils.readFileValue(mapper,
        "json/test-data/users-data.json", List.class);

    List<UsersEntity> data = usersRepository.findAll();
    when(usersRepository.findAll()).thenReturn(data);

    mockMvc.perform(get("/booking-api/v1/records/users")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("data").value(clustersData))
        .andExpect(jsonPath("status", is(200)))
        .andExpect(jsonPath("responsecode", is("TRN-000")));

    verify(usersRepository, times(1)).findAll();
  }

  @ParameterizedTest
  @ValueSource(strings = {"JF-172775", "JF-000000"})
  @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
  void testGetAllClusterGroupsById_return200(String id) throws Exception {
    //String userData;
    String userId;
    String transactionMessage;
    String tranCode;
    int responseCode;
    ResultMatcher resultMatcher;

    Optional<UsersEntity> data = usersRepository.findById(id);
    when(usersRepository.findById(id)).thenReturn(data);
    JSONObject jsonObject;

    if (id.equals("JF-172775")) {
      //userData = "{id=JF-172775, firstName=Ronald, middleName=Collado, lastName=Cando, emailAdd=ron.cando04@gmail.com, contactNumber=09279471440, address=4775 Guadalcanal st. Sta. Mesa, Manila, birthday=1985-11-04, role=AE, status=ACTIVE, cluster=Cluster 1, clusterCode=cluster-001, simbahayName=Saturday Group, simbahayCode=smbhy-007, userName=ron1104, password=ron1104, enabled=true, authorities=[{\"authority\":\"AE\"}], username=ron1104, accountNonExpired=true, accountNonLocked=true, credentialsNonExpired=true}";

      userId = "JF-172775";
      transactionMessage = "was successfully retrieved.";
      tranCode = "TRN-000";
      responseCode = 200;
      resultMatcher = status().isOk();
    } else {
      //userData = null;
      userId = "JF-000000";
      transactionMessage = "doesn't exist.";
      tranCode = "TRN-001";
      responseCode = 404;
      resultMatcher = status().isBadRequest();
      assert (usersRepository.findById("JF-000000").isEmpty());
    }

    mockMvc.perform(get("/booking-api/v1/records/user/" + id)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        //.andDo(print())
        .andExpect(resultMatcher)
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
        //.andExpect(jsonPath("data", is(userData)))
        .andExpect(jsonPath("status", is(responseCode)))
        .andExpect(jsonPath("message", is(String.format("User with id %s %s", userId, transactionMessage))))
        .andExpect(jsonPath("responsecode", is(tranCode)));
  }
}