package com.jfcm.manda.bookingmanagerapi.service;

import java.util.Date;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
  String extractUserName(String token);
  String generateToken(UserDetails userDetails);
  String generateRefreshToken(UserDetails userDetails);
  boolean isTokenValid(String token, UserDetails userDetails);
  Date extractExpiration(String token);

}
