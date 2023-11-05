/*
 *  ClustersResources.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfcm.manda.bookingmanagerapi.constants.Constants;
import com.jfcm.manda.bookingmanagerapi.model.ClusterGroupsEntity;
import com.jfcm.manda.bookingmanagerapi.repository.ClustersRepository;
import com.jfcm.manda.bookingmanagerapi.service.impl.GenerateUUIDService;
import com.jfcm.manda.bookingmanagerapi.service.impl.LoggingService;
import com.jfcm.manda.bookingmanagerapi.utils.ResponseUtil;
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

  @Autowired
  private ClustersRepository clustersRepository;
  @Autowired
  private LoggingService LOG;
  @Autowired
  private GenerateUUIDService generateUUIDService;

  @GetMapping(value = "/all-cluster-groups", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getAllClusterGroups() {
    List<ClusterGroupsEntity> data = clustersRepository.findAll();

    return ResponseUtil.generateResponse(String.format("Data retrieved successfully. %s record(s)", data.size()), HttpStatus.OK, data, Constants.TRANSACTION_SUCCESS);
  }

  @GetMapping(value = "/cluster/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getClusterById(@PathVariable(value = "id") String id) {
    var isIdExist = clustersRepository.existsById(id);
    Optional<ClusterGroupsEntity> data = clustersRepository.findById(id);

    if (isIdExist) {
      return ResponseUtil.generateResponse(String.format("data with id %s successfully retrieved", id), HttpStatus.OK, data, Constants.TRANSACTION_SUCCESS);
    } else {
      return ResponseUtil.generateResponse(String.format("data with id %s doesn't exist", id), HttpStatus.NOT_FOUND, data, Constants.TRANSACTION_FAILED);
    }
    }

  @PostMapping(value = "/add-cluster", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> addClusterGroup(@RequestBody ClusterGroupsEntity data) {
    clustersRepository.save(data);

    return ResponseUtil.generateResponse("New cluster group successfully created!", HttpStatus.CREATED, data, Constants.TRANSACTION_SUCCESS);
  }

  @DeleteMapping(value = "/delete-cluster-group/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> deleteClusterGroup(@PathVariable(value = "id") String id) {
    var data = clustersRepository.findById(id);
    clustersRepository.deleteById(id);

    return ResponseUtil.generateResponse(String.format("User with id %s has been deleted", id), HttpStatus.OK, data, Constants.TRANSACTION_SUCCESS);
  }

  @PutMapping(value = "/update-cluster-group/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> updateUserById(@RequestBody ClusterGroupsEntity data, @PathVariable String id) throws JsonProcessingException {

    Optional<ClusterGroupsEntity> user = clustersRepository.findById(id);

    if (user.isEmpty()) {
      LOG.error(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("Cluster with id %s doesn't exist", id), Constants.TRANSACTION_FAILED);
      return ResponseUtil.generateResponse(String.format("Cluster with id %s doesn't exist", id), HttpStatus.NOT_FOUND, data, Constants.TRANSACTION_FAILED);
    }
    data.setClusterCode(id);
    clustersRepository.save(data);
    LOG.info(generateUUIDService.generateUUID(), this.getClass().toString(), String.format("Cluster with id %s has been updated", id), Constants.TRANSACTION_SUCCESS);

    return ResponseUtil.generateResponse(String.format("Cluster with id %s has been updated", id), HttpStatus.OK, data, Constants.TRANSACTION_SUCCESS);
  }
}