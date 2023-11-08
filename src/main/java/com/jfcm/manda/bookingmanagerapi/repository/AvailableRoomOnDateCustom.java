/**
 * {@link com.jfcm.manda.bookingmanagerapi.repository.AvailableRoomOnDateCustom}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.repository;

public interface AvailableRoomOnDateCustom {
  String checkIfRoomIsAvailableOnAGivenDate(String roomType, String roomStatus, String date);

  int updateRoomStatusOnAGivenDate(String roomType, String status, String id);
}