/**
 * {@link com.jfcm.manda.bookingmanagerapi.service.impl.GenerateUUIDService}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.service.impl;

import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class GenerateUUIDService {

  public String generateUUID() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString();
  }
}