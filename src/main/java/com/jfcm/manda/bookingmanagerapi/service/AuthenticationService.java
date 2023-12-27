package com.jfcm.manda.bookingmanagerapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfcm.manda.bookingmanagerapi.dao.request.SignUpRequest;
import com.jfcm.manda.bookingmanagerapi.dao.request.SigninRequest;
import com.jfcm.manda.bookingmanagerapi.dao.response.JwtAuthenticationResponse;
import com.jfcm.manda.bookingmanagerapi.dao.response.TokenExpirationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthenticationService {

  JwtAuthenticationResponse signup(SignUpRequest request) throws JsonProcessingException;

  JwtAuthenticationResponse signin(SigninRequest request) throws JsonProcessingException;

  JwtAuthenticationResponse updatePassword(String request, String id) throws JsonProcessingException;

  String updatePasswordForAccountUpdate(String password);

  void authenticateUser(String username, String password) throws JsonProcessingException;

  void isTokenExpired(String token, HttpServletResponse response) throws IOException;

  void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
