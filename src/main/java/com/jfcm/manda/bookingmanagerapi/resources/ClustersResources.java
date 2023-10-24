/*
 *  ClustersResources.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.resources;

import com.jfcm.manda.bookingmanagerapi.constants.Constants;
import com.jfcm.manda.bookingmanagerapi.model.Clusters;
import com.jfcm.manda.bookingmanagerapi.repository.ClustersRepository;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClustersResources {

  @Autowired
  private ClustersRepository clustersRepository;

  @GetMapping(value = "/all-cluster-groups", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getAllClusterGroups() {
    List<Clusters> data = clustersRepository.findAll();

    return ResponseUtil.generateResponse(String.format("Data retrieved successfully. %s record(s)", data.size()), HttpStatus.OK, data, Constants.TRANSACTION_SUCCESS);
  }

  @GetMapping(value = "/cluster/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getClusterById(@PathVariable(value = "id") String id) {
    Optional<Clusters> data = clustersRepository.findById(id);

    return ResponseUtil.generateResponse(String.format("data with id %s successfully retrieved", id), HttpStatus.OK, data, Constants.TRANSACTION_SUCCESS);
  }

  @PostMapping(value = "/add-cluster", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> addClusterGroup(@RequestBody Clusters data) {
    clustersRepository.save(data);

    return ResponseUtil.generateResponse("New cluster group successfully created!", HttpStatus.OK, data, Constants.TRANSACTION_SUCCESS);
  }

  @DeleteMapping(value = "/delete-cluster-group/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> deleteClusterGroup(@PathVariable(value = "id") String id) {
    var data = clustersRepository.findById(id);
    clustersRepository.deleteById(id);

    return ResponseUtil.generateResponse(String.format("User with id %s has been deleted", id), HttpStatus.OK, data, Constants.TRANSACTION_SUCCESS);
  }

  @PutMapping(value = "/update-cluster-group/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> updateUserById(@RequestBody Clusters data, @PathVariable String id) {

    Optional<Clusters> user = clustersRepository.findById(id);

    if (user.isEmpty()) {
      return ResponseUtil.generateResponse(String.format("User with id %s doesn't exist", id), HttpStatus.NOT_FOUND, String.format("id %s not found", id), Constants.TRANSACTION_FAILED);
    }
    data.setClusterCode(id);
    clustersRepository.save(data);

    return ResponseUtil.generateResponse(String.format("User with id %s has been updated", id), HttpStatus.OK, data, Constants.TRANSACTION_SUCCESS);
  }

}