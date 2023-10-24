/*
 *  UsersResources.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.resources;

import com.jfcm.manda.bookingmanagerapi.constants.Constants;
import com.jfcm.manda.bookingmanagerapi.model.Users;
import com.jfcm.manda.bookingmanagerapi.repository.UsersRepository;
import com.jfcm.manda.bookingmanagerapi.utils.ResponseUtil;
import com.jfcm.manda.bookingmanagerapi.utils.Utilities;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersResources {

  @Autowired
  private UsersRepository usersRepository;
  @Autowired
  private Utilities utilities;

  @GetMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getAllUsers() {
    List<Users> data = usersRepository.findAll();

    return ResponseUtil.generateResponse(String.format("Data retrieved successfully. %s record(s)", data.size()), HttpStatus.OK, data, Constants.TRANSACTION_SUCCESS);
  }

  @GetMapping(value = "/user/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> getUserById(@PathVariable(value = "id") String id) {
    Optional<Users> data = usersRepository.findById(id);

    return ResponseUtil.generateResponse(String.format("data with id %s successfully retrieved", id), HttpStatus.OK, data, Constants.TRANSACTION_SUCCESS);
  }

  @PostMapping(value = "/add-user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> addUser(@RequestBody String user) throws IOException {
    Users data = utilities.readfromInput(user, Users.class);
    String generatedId = utilities.getGeneratedId();
    data.setId(generatedId);
    usersRepository.save(data);

    return ResponseUtil.generateResponse("New user successfully created!", HttpStatus.OK, data, Constants.TRANSACTION_SUCCESS);
  }

  @DeleteMapping(value = "/delete-user/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> deleteMember(@PathVariable(value = "id") String id) {
    var data = usersRepository.findById(id);
    usersRepository.deleteById(id);

    return ResponseUtil.generateResponse(String.format("User with id %s has been deleted", id), HttpStatus.OK, data, Constants.TRANSACTION_SUCCESS);
  }

  @PutMapping(value = "/update-user/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> updateUserById(@RequestBody Users users, @PathVariable String id) {

    Optional<Users> user = usersRepository.findById(id);

    if (user.isEmpty()) {
      return ResponseUtil.generateResponse(String.format("User with id %s doesn't exist", id), HttpStatus.NOT_FOUND, String.format("id %s not found", id), Constants.TRANSACTION_FAILED);
    }
    users.setId(id);
    usersRepository.save(users);

    return ResponseUtil.generateResponse(String.format("User with id %s has been updated", id), HttpStatus.OK, users, Constants.TRANSACTION_SUCCESS);
  }

}