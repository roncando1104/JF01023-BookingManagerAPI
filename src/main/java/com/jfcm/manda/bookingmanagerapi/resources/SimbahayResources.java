/*
 *  SimbahayResources.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.resources;

import com.jfcm.manda.bookingmanagerapi.constants.Constants;
import com.jfcm.manda.bookingmanagerapi.model.SimbahayGroups;
import com.jfcm.manda.bookingmanagerapi.repository.SimbahayRepository;
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
public class SimbahayResources {

  @Autowired
  private SimbahayRepository simbahayRepository;

  @GetMapping(value = "/simbahay", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getAllSimbahays() {
    List<SimbahayGroups> data = simbahayRepository.findAll();

    return ResponseUtil.generateResponse(String.format("Data retrieved successfully. %s record(s)", data.size()), HttpStatus.OK, data, Constants.TRANSACTION_SUCCESS);
  }

  @GetMapping(value = "/simbahay/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getSimbahayById(@PathVariable(value = "id") String id) {
    Optional<SimbahayGroups> data = simbahayRepository.findById(id);

    return ResponseUtil.generateResponse(String.format("Simbahay with id %s successfully retrieved", id), HttpStatus.OK, data, Constants.TRANSACTION_SUCCESS);
  }

  @PostMapping(value = "/add-simbahay", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> addSimbahayGroup(@RequestBody SimbahayGroups data) {
    simbahayRepository.save(data);

    return ResponseUtil.generateResponse("New simbahay group successfully created!", HttpStatus.OK, data, Constants.TRANSACTION_SUCCESS);
  }

  @DeleteMapping(value = "/delete-simbahay/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> deleteSimbahay(@PathVariable(value = "id") String id) {
    var data = simbahayRepository.findById(id);
    simbahayRepository.deleteById(id);

    return ResponseUtil.generateResponse(String.format("Simbahay with id %s has been deleted", id), HttpStatus.OK, data, Constants.TRANSACTION_SUCCESS);
  }

  @PutMapping(value = "/update-simbahay/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> updateSimbahayById(@RequestBody SimbahayGroups simbahayGrp, @PathVariable String id) {

    Optional<SimbahayGroups> simbahayById = simbahayRepository.findById(id);

    if (simbahayById.isEmpty()) {
      return ResponseUtil.generateResponse(String.format("Simbahay with id %s doesn't exist", id), HttpStatus.NOT_FOUND, String.format("id %s not found", id), Constants.TRANSACTION_FAILED);
    }
    simbahayGrp.setId(id);
    simbahayRepository.save(simbahayGrp);

    return ResponseUtil.generateResponse(String.format("Simbahay with id %s has been updated", id), HttpStatus.OK, simbahayGrp, Constants.TRANSACTION_SUCCESS);
  }

}