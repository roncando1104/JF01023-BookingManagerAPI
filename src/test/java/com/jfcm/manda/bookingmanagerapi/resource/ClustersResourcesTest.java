/**
 * {@link com.jfcm.manda.bookingmanagerapi.resource.ClustersResourcesTest}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential
 * and proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.resource;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfcm.manda.bookingmanagerapi.model.ClusterGroupsEntity;
import com.jfcm.manda.bookingmanagerapi.repository.ClustersRepository;
import com.jfcm.manda.bookingmanagerapi.utils.TestUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
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
class ClustersResourcesTest {

  private final ObjectMapper mapper = new ObjectMapper();
  @InjectMocks
  private ClustersResources clustersResources;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ClustersRepository clustersRepository;

  @Test
  @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
  void testGetAllClusterGroups_return200() throws Exception {
    List<ClusterGroupsEntity> clustersData = TestUtils.readFileValue(mapper,
        "json/test-data/cluster/cluster-data.json", List.class);

    mockMvc.perform(get("/booking-api/v1/records/all-cluster-groups")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("data").value(clustersData))
        .andExpect(jsonPath("status", is(200)))
        .andExpect(jsonPath("responsecode", is("TRN-000")));

    assert (clustersRepository.findById("cluster-001").isPresent());
    assert (clustersRepository.findAll().size() == 4);
  }

  @Test
  void testEndpoint_return403_whenUrlIsNotAuthenticated() throws Exception {
    mockMvc.perform(get("/not-included/all-cluster-groups")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isForbidden());
  }

  @ParameterizedTest
  @ValueSource(strings = {"cluster-001", "cluster-000"})
  @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
  void testGetAllClusterGroupsById_return_200_and_404(String id) throws Exception {
    ClusterGroupsEntity clustersData = null;
    String clusterId;
    String transactionMessage;
    String tranCode;
    int responseCode;
    ResultMatcher resultMatcher;
    if (id.equals("cluster-001")) {
      clustersData = TestUtils.readFileValue(mapper,
          "json/test-data/cluster/single-cluster-data.json", ClusterGroupsEntity.class);
      assert (clustersRepository.findById("cluster-001").isPresent());
      clusterId = "cluster-001";
      transactionMessage = "successfully retrieved";
      tranCode = "TRN-000";
      responseCode = 200;
      resultMatcher = status().isOk();
    } else {
      //clustersData = null;
      clusterId = "cluster-000";
      transactionMessage = "doesn't exist";
      tranCode = "TRN-001";
      responseCode = 404;
      resultMatcher = status().isNotFound();
      assert (clustersRepository.findById("cluster-000").isEmpty());
    }

    mockMvc.perform(get("/booking-api/v1/records/cluster/" + id)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(resultMatcher)
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
        //.andExpect(jsonPath("data").value(clustersData))
        .andExpect(jsonPath("status", is(responseCode)))
        .andExpect(jsonPath("message", is(String.format("Cluster group with id %s %s", clusterId, transactionMessage))))
        .andExpect(jsonPath("responsecode", is(tranCode)));
  }

  @Test
  @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
  void testAddClusterGroup_return200() throws Exception {
    ClusterGroupsEntity clustersData = TestUtils.readFileValue(mapper,
        "json/test-data/cluster/single-cluster-data.json", ClusterGroupsEntity.class);

    mockMvc.perform(post("/booking-api/v1/records/add-cluster")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(clustersData))
            .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("data").value(clustersData))
        .andExpect(jsonPath("status", is(201)))
        .andExpect(jsonPath("responsecode", is("TRN-000")));

    assertEquals("cluster-001", clustersData.getClusterCode());
    assert (clustersRepository.findById("cluster-001").isPresent());
  }

  @Test
  @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
  void testDeleteClusterGroup_return200() throws Exception {
    ClusterGroupsEntity clustersData = TestUtils.readFileValue(mapper,
        "json/test-data/cluster/single-cluster-data.json", ClusterGroupsEntity.class);

    mockMvc.perform(delete("/booking-api/v1/records/delete-cluster-group/cluster-001")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("data").value(clustersData))
        .andExpect(jsonPath("status", is(200)))
        .andExpect(jsonPath("responsecode", is("TRN-000")));

    assert (clustersRepository.findById("cluster-001").isEmpty());
  }

  @ParameterizedTest
  @ValueSource(strings = {"cluster-001", "cluster-000"})
  @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
  void testUpdateUserById_return200(String id) throws Exception {
    ClusterGroupsEntity clustersData = null;
    String clusterId;
    String transactionMessage;
    String tranCode;
    int responseCode;
    ResultMatcher resultMatcher;
    if (id.equals("cluster-001")) {
      clustersData = TestUtils.readFileValue(mapper,
          "json/test-data/cluster/single-cluster-data.json", ClusterGroupsEntity.class);
      assert (clustersRepository.findById("cluster-001").isPresent());
      clusterId = "cluster-001";
      transactionMessage = "has been updated";
      tranCode = "TRN-000";
      responseCode = 200;
      resultMatcher = status().isOk();
    } else {
      //clustersData = TestUtils.readFileValue(mapper,
      //  "json/test-data/single-cluster-data-for-update.json", ClusterGroupsEntity.class);
      clusterId = "cluster-000";
      transactionMessage = "doesn't exist";
      tranCode = "TRN-001";
      responseCode = 404;
      resultMatcher = status().isNotFound();
      assert (clustersRepository.findById("cluster-000").isEmpty());
    }

    mockMvc.perform(put("/booking-api/v1/records/update-cluster-group/" + id)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(clustersData)))
        .andExpect(resultMatcher)
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
        //.andExpect(jsonPath("data", is(clustersData)))
        .andExpect(jsonPath("status", is(responseCode)))
        .andExpect(jsonPath("message", is(String.format("Cluster group with id %s %s", clusterId, transactionMessage))))
        .andExpect(jsonPath("responsecode", is(tranCode)));

  }

}