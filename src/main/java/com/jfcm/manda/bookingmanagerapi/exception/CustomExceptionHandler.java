/*
 *  ControllerAdvisor.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.exception;

import com.jfcm.manda.bookingmanagerapi.constants.Constants;
import com.jfcm.manda.bookingmanagerapi.dao.response.ErrorResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
  private final LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("Asia/Manila"));

  @ExceptionHandler(DataAlreadyExistException.class)
  public ResponseEntity<ErrorResponse> handlesDataAlreadyExistException(DataAlreadyExistException ex, WebRequest request) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    String dateNow = formatter.format(dateTime);

    var error = ErrorResponse.builder()
        .timestamp(dateNow)
        .status(HttpStatus.BAD_REQUEST.value())
        .responsecode(Constants.TRANSACTION_FAILED)
        .message(ex.getMessage())
        .path(request.getDescription(false))
        .build();

    return ResponseEntity.badRequest().body(error);
  }

//  @Override
//  public ResponseEntity<Object> handleMethodArgumentNotValid(
//      DataAlreadyExistException ex, HttpHeaders headers,
//      HttpStatusCode status, WebRequest request) {
//
//    List<String> errors = ex.getBindingResult()
//        .getFieldErrors()
//        .stream()
//        .map(DefaultMessageSourceResolvable::getDefaultMessage)
//        .collect(Collectors.toList());
//
//
//    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
//  }
}