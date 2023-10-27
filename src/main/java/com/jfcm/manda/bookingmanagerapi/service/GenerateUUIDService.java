/*
 *  GenerateUUIDService.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.service;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class GenerateUUIDService {

  public String generateUUID() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString();
  }

}