package com.jfcm.manda.bookingmanagerapi.resource;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfcm.manda.bookingmanagerapi.model.Clusters;
import com.jfcm.manda.bookingmanagerapi.repository.ClustersRepository;
import com.jfcm.manda.bookingmanagerapi.utils.TestUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class ClustersResourcesTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ClustersRepository clustersRepository;

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  void testGetAllClusterGroups() throws Exception {
    List<Clusters> clustersData = TestUtils.readFileValue(mapper,
        "json/test-data/cluster-data.json", List.class);

    mockMvc.perform(get("/all-cluster-groups")
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("data").value(clustersData))
        .andExpect(jsonPath("status", is(200)))
        .andExpect(jsonPath("responseCode", is("TRN-000")));

    assert(clustersRepository.findById("cluster-001").isPresent());
    assert(clustersRepository.findAll().size() == 4);
  }

  @ParameterizedTest
  @ValueSource(strings = {"cluster-001", "cluster-000"})
  void testGetAllClusterGroupsById(String id) throws Exception {
    Clusters clustersData;
    String clusterId;
    String transactionMessage;
    String tranCode;
    int responseCode;
    ResultMatcher resultMatcher;
    if (id.equals("cluster-001")) {
       clustersData = TestUtils.readFileValue(mapper,
          "json/test-data/single-cluster-data.json", Clusters.class);
      assert(clustersRepository.findById("cluster-001").isPresent());
      clusterId = "cluster-001";
      transactionMessage = "successfully retrieved";
      tranCode = "TRN-000";
      responseCode = 200;
      resultMatcher = status().isOk();
    } else {
      clustersData = null;
      clusterId = "cluster-000";
      transactionMessage = "doesn't exist";
      tranCode = "TRN-001";
      responseCode = 404;
      resultMatcher = status().isNotFound();
      assert(clustersRepository.findById("cluster-000").isEmpty());
    }

    mockMvc.perform(get("/cluster/" + id)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(resultMatcher)
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("data").value(clustersData))
        .andExpect(jsonPath("status", is(responseCode)))
        .andExpect(jsonPath("message", is(String.format("data with id %s %s", clusterId, transactionMessage))))
        .andExpect(jsonPath("responseCode", is(tranCode)));
  }

  @Test
  void testAddClusterGroup() throws Exception {
    Clusters clustersData = TestUtils.readFileValue(mapper,
        "json/test-data/single-cluster-data.json", Clusters.class);

    mockMvc.perform(post("/add-cluster")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(clustersData))
            .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("data").value(clustersData))
        .andExpect(jsonPath("status", is(201)))
        .andExpect(jsonPath("responseCode", is("TRN-000")));

    assertEquals("cluster-001", clustersData.getClusterCode());
    assert(clustersRepository.findById("cluster-001").isPresent());
  }

  @Test
  void testDeleteClusterGroup() throws Exception {
    Clusters clustersData = TestUtils.readFileValue(mapper,
        "json/test-data/single-cluster-data.json", Clusters.class);

    mockMvc.perform(delete("/delete-cluster-group/cluster-001")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("data").value(clustersData))
        .andExpect(jsonPath("status", is(200)))
        .andExpect(jsonPath("responseCode", is("TRN-000")));

    assert(clustersRepository.findById("cluster-001").isEmpty());
  }

  @ParameterizedTest
  @ValueSource(strings = {"cluster-001", "cluster-000"})
  void testUpdateUserById(String id) throws Exception {
    Clusters clustersData;
    String clusterId;
    String transactionMessage;
    String tranCode;
    int responseCode;
    ResultMatcher resultMatcher;
    if (id.equals("cluster-001")) {
      clustersData = TestUtils.readFileValue(mapper,
          "json/test-data/single-cluster-data.json", Clusters.class);
      assert(clustersRepository.findById("cluster-001").isPresent());
      clusterId = "cluster-001";
      transactionMessage = "has been updated";
      tranCode = "TRN-000";
      responseCode = 200;
      resultMatcher = status().isOk();
    } else {
      clustersData = TestUtils.readFileValue(mapper,
          "json/test-data/single-cluster-data-for-update.json", Clusters.class);
      clusterId = "cluster-000";
      transactionMessage = "doesn't exist";
      tranCode = "TRN-001";
      responseCode = 404;
      resultMatcher = status().isNotFound();
      assert(clustersRepository.findById("cluster-000").isEmpty());
    }

    mockMvc.perform(put("/update-cluster-group/" + id)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(mapper.writeValueAsString(clustersData)))
        .andExpect(resultMatcher)
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("data").value(clustersData))
        .andExpect(jsonPath("status", is(responseCode)))
        .andExpect(jsonPath("message", is(String.format("Cluster with id %s %s", clusterId, transactionMessage))))
        .andExpect(jsonPath("responseCode", is(tranCode)));

  }

}