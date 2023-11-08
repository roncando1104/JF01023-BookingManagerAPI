/**
 * {@link com.jfcm.manda.bookingmanagerapi.repository.AvailableRoomOnDateRepository}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.repository;

import com.jfcm.manda.bookingmanagerapi.model.AvailabilityCalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailableRoomOnDateRepository extends JpaRepository<AvailabilityCalendarEntity, String>, AvailableRoomOnDateCustom {

  /**
   * Implemented by AvailableRoomOnDateRepositoryImpl.java
   * */
}