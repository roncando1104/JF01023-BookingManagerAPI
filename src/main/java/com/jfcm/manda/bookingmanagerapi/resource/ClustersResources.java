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
import com.jfcm.manda.bookingmanagerapi.exception.RecordNotFoundException;
import com.jfcm.manda.bookingmanagerapi.model.ClusterGroupsEntity;
import com.jfcm.manda.bookingmanagerapi.repository.ClustersRepository;
import com.jfcm.manda.bookingmanagerapi.service.impl.GenerateUUIDService;
import com.jfcm.manda.bookingmanagerapi.service.impl.LoggingService;
import com.jfcm.manda.bookingmanagerapi.utils.Utilities;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking-api/v1/records")
public class ClustersResources {

  private final LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("Asia/Manila"));
  @Autowired
  private ClustersRepository clustersRepository;
  @Autowired
  private LoggingService LOG;
  @Autowired
  private GenerateUUIDService generateUUIDService;
  @Autowired
  private Utilities utilities;


  /**
   * @implNote this method retrieves all the cluster groups from database
   * @return ResponseEntity OK
   * @throws JsonProcessingException if an error during JSON processing occurs
   */
  @GetMapping(value = "/all-cluster-groups", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getAllClusterGroups() throws JsonProcessingException {
    List<ClusterGroupsEntity> data = clustersRepository.findAll();

    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("Cluster groups retrieved successfully. %s record(s)", data.size()),
        Constants.TRANSACTION_SUCCESS);
    var response = getResponse(data, HttpStatus.OK.value(),
        String.format("Cluster groups retrieved successfully. %s record(s)", data.size()));
    return ResponseEntity.ok(response);
  }

  /**
   * @implNote this method retrieves a cluster group based on given id from database
   * @param id as {@link PathVariable}
   * @return ResponseEntity OK
   * @throws JsonProcessingException if an error during JSON processing occurs
   * @throws RecordNotFoundException if user doesn't exist
   */
  @GetMapping(value = "/cluster/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getClusterById(@PathVariable(value = "id") String id) throws JsonProcessingException {
    var isIdExist = clustersRepository.existsById(id);
    Optional<ClusterGroupsEntity> data = clustersRepository.findById(id);

    if (isIdExist) {
      LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("Cluster group with id %s successfully retrieved", id),
          Constants.TRANSACTION_SUCCESS);

      var response = getResponse(data, HttpStatus.OK.value(),
          String.format("Cluster group with id %s successfully retrieved", id));
      return ResponseEntity.ok(response);
    } else {
      LOG.error(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("Cluster with id %s doesn't exist", id),
          Constants.TRANSACTION_FAILED);
      throw new RecordNotFoundException(String.format("Cluster group with id %s doesn't exist", id));
    }
  }

  /**
   * @request
   * This method accepts below payload as {@link RequestBody}
  {
  "clusterCode": "",
  "clusterName": "",
  "clusterLeader1": "",
  "clusterLeader2": "",
  "totalSimbahay": ""
  }
   * @implNote id should be assigned
   * @param input as {@link RequestBody}
   * @implNote JSON payload is checked and fixed if contains @nbsp
   * @return ResponseEntity OK
   * @throws JsonProcessingException if an error during JSON processing occurs
   */
  @PostMapping(value = "/add-cluster", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> addClusterGroup(@RequestBody String input) throws JsonProcessingException {
    String processedInput = Utilities.checkAndFixInvalidJson(input);
    ClusterGroupsEntity data = utilities.readfromInput(processedInput, ClusterGroupsEntity.class);

    clustersRepository.save(data);
    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(),
        String.format("New cluster group with id %s successfully created!", data.getClusterCode()),
        Constants.TRANSACTION_SUCCESS);

    var response = getResponse(data, HttpStatus.CREATED.value(),
        String.format("New cluster group with id %s successfully created!", data.getClusterCode()));
    return ResponseEntity.ok(response);
  }

  /**
   * @implNote this method deletes a cluster group based on given id from database
   * @param id as {@link PathVariable}
   * @return ResponseEntity OK
   * @throws JsonProcessingException if an error during JSON processing occurs
   * @throws RecordNotFoundException if cluster group doesn't exist
   */
  @DeleteMapping(value = "/delete-cluster-group/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> deleteClusterGroup(@PathVariable(value = "id") String id) throws JsonProcessingException {
    var data = clustersRepository.findById(id);

    if (data.isEmpty()) {
      LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("Cluster group with id %s doesn't exist.", id),
          Constants.TRANSACTION_FAILED);
      throw new RecordNotFoundException(String.format("Cluster group with id %s doesn't exist.", id));
    }
    clustersRepository.deleteById(id);

    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("Cluster group with id %s has been deleted", id),
        Constants.TRANSACTION_SUCCESS);

    var response = getResponse(data, HttpStatus.OK.value(),
        String.format("Cluster group with id %s has been deleted", id));
    return ResponseEntity.ok(response);
  }

  /**
   * @request
   * This method accepts below payload as {@link RequestBody}
  {
  "clusterCode": "",
  "clusterName": "",
  "clusterLeader1": "",
  "clusterLeader2": "",
  "totalSimbahay": ""
  }
   * @implNote id should be assigned
   * @param input as {@link RequestBody}
   * @implNote JSON payload is checked and fixed if contains @nbsp
   * @return ResponseEntity OK
   * @throws JsonProcessingException if an error during JSON processing occurs
   * @throws RecordNotFoundException if a cluster doesn't exist
   */
  @PutMapping(value = "/update-cluster-group/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> updateUserById(@RequestBody String input, @PathVariable String id) throws JsonProcessingException {
    String processedInput = Utilities.checkAndFixInvalidJson(input);
    ClusterGroupsEntity data = utilities.readfromInput(processedInput, ClusterGroupsEntity.class);
    Optional<ClusterGroupsEntity> user = clustersRepository.findById(id);

    if (user.isEmpty()) {
      LOG.error(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("Cluster with id %s doesn't exist", id),
          Constants.TRANSACTION_FAILED);
      throw new RecordNotFoundException(String.format("Cluster group with id %s doesn't exist", id));
    }
    data.setClusterCode(id);
    clustersRepository.save(data);

    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("Cluster group with id %s has been updated", id),
        Constants.TRANSACTION_SUCCESS);

    var response = getResponse(data, HttpStatus.OK.value(),
        String.format("Cluster group with id %s has been updated", id));
    return ResponseEntity.ok(response);
  }

  private CommonResponse getResponse(Object data, int status, String msg) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    String dateTokenCreated = formatter.format(dateTime);

    return CommonResponse.builder()
        .timestamp(dateTokenCreated)
        .info(data)
        .status(status)
        .responseCode(Constants.TRANSACTION_SUCCESS)
        .message(msg)
        .build();
  }
}