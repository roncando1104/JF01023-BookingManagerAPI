package com.jfcm.manda.bookingmanagerapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfcm.manda.bookingmanagerapi.dao.request.SignUpRequest;
import com.jfcm.manda.bookingmanagerapi.dao.request.SigninRequest;
import com.jfcm.manda.bookingmanagerapi.dao.request.UpdatePasswordRequest;
import com.jfcm.manda.bookingmanagerapi.dao.response.JwtAuthenticationResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
  JwtAuthenticationResponse signup(SignUpRequest request) throws JsonProcessingException;
  JwtAuthenticationResponse signin(SigninRequest request) throws JsonProcessingException;

  JwtAuthenticationResponse updatePassword(String request, String id) throws JsonProcessingException;
}
