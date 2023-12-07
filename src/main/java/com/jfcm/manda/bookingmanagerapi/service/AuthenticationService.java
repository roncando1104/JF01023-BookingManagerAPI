package com.jfcm.manda.bookingmanagerapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfcm.manda.bookingmanagerapi.dao.request.SignUpRequest;
import com.jfcm.manda.bookingmanagerapi.dao.request.SigninRequest;
import com.jfcm.manda.bookingmanagerapi.dao.response.JwtAuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthenticationService {

  JwtAuthenticationResponse signup(SignUpRequest request) throws JsonProcessingException;

  JwtAuthenticationResponse signin(SigninRequest request) throws JsonProcessingException;

  JwtAuthenticationResponse updatePassword(String request, String id) throws JsonProcessingException;

  void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
