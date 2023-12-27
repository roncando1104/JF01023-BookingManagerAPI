/**
 * {@link com.jfcm.manda.bookingmanagerapi.exception.CustomExceptionHandler}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential
 * and proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfcm.manda.bookingmanagerapi.constants.Constants;
import com.jfcm.manda.bookingmanagerapi.dao.response.ErrorResponse;
import com.jfcm.manda.bookingmanagerapi.dao.response.TokenExpirationResponse;
import com.jfcm.manda.bookingmanagerapi.service.impl.GenerateUUIDService;
import com.jfcm.manda.bookingmanagerapi.service.impl.LoggingService;
import io.jsonwebtoken.ExpiredJwtException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

  private final LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("Asia/Manila"));
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
  private final String dateNow = formatter.format(dateTime);

  @Autowired
  private LoggingService LOG;
  @Autowired
  private GenerateUUIDService generateUUIDService;

  @ExceptionHandler(DataAlreadyExistException.class)
  public ResponseEntity<ErrorResponse> handlesDataAlreadyExistException(DataAlreadyExistException ex, WebRequest request) {

    var error = ErrorResponse.builder()
        .timestamp(dateNow)
        .status(HttpStatus.BAD_REQUEST.value())
        .responsecode(Constants.TRANSACTION_FAILED)
        .message(ex.getMessage())
        .path(request.getDescription(false))
        .build();

    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(InvalidInputException.class)
  public ResponseEntity<ErrorResponse> handlesInvalidInputException(InvalidInputException ex, WebRequest request) {

    var error = ErrorResponse.builder()
        .timestamp(dateNow)
        .status(HttpStatus.FORBIDDEN.value())
        .responsecode(Constants.TRANSACTION_FAILED)
        .message(ex.getMessage())
        .path(request.getDescription(false))
        .build();

    return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(RecordNotFoundException.class)
  public ResponseEntity<ErrorResponse> handlesRecordNotFoundException(RecordNotFoundException ex, WebRequest request) {

    var error = ErrorResponse.builder()
        .timestamp(dateNow)
        .status(HttpStatus.NOT_FOUND.value())
        .responsecode(Constants.TRANSACTION_FAILED)
        .message(ex.getMessage())
        .path(request.getDescription(false))
        .build();

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MultipleBookingException.class)
  public ResponseEntity<ErrorResponse> handlesMultipleBookingsException(MultipleBookingException ex, WebRequest request) {

    var error = ErrorResponse.builder()
        .timestamp(dateNow)
        .status(HttpStatus.FORBIDDEN.value())
        .responsecode(Constants.TRANSACTION_FAILED)
        .message(ex.getMessage())
        .path(request.getDescription(false))
        .build();

    return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(GenericBookingException.class)
  public ResponseEntity<ErrorResponse> handlesGenericBookingException(GenericBookingException ex, WebRequest request) {

    var error = ErrorResponse.builder()
        .timestamp(dateNow)
        .status(HttpStatus.FORBIDDEN.value())
        .responsecode(Constants.TRANSACTION_FAILED)
        .message(ex.getMessage())
        .path(request.getDescription(false))
        .build();

    return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex, WebRequest request) {

    var error = ErrorResponse.builder()
        .timestamp(dateNow)
        .status(HttpStatus.FORBIDDEN.value())
        .responsecode(Constants.TRANSACTION_FAILED)
        .message(ex.getMessage() + " | " + ex)
        .path(request.getDescription(false))
        .build();

    return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<ErrorResponse> handlesExpiredJwtTokenException(ExpiredJwtException ex, WebRequest request) throws JsonProcessingException {

    LOG.error(generateUUIDService.generateUUID(), this.getClass().toString(),
        String.format("Token is already expired with message: %s", ex.getMessage()),
        Constants.TRANSACTION_FAILED);

    var error = ErrorResponse.builder()
        .timestamp(dateNow)
        .status(HttpStatus.UNAUTHORIZED.value())
        .responsecode(Constants.TRANSACTION_FAILED)
        .message("Token is expired | " + ex.getLocalizedMessage())
        .path(request.getDescription(false))
        .build();

    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
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