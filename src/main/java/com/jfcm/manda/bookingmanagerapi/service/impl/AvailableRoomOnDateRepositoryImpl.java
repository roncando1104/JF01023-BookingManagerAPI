/**
 * {@link com.jfcm.manda.bookingmanagerapi.service.impl.AvailableRoomOnDateRepositoryImpl}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.service.impl;

import com.jfcm.manda.bookingmanagerapi.model.AvailabilityCalendarEntity;
import com.jfcm.manda.bookingmanagerapi.repository.AvailableRoomOnDateCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AvailableRoomOnDateRepositoryImpl implements AvailableRoomOnDateCustom {

  @PersistenceContext
  private EntityManager entityManager;

  /**
   *
   * @param roomType as {@link String}
   * @param roomStatus as {@link String}
   * @param date as {@link String}
   * @return available or reserved
   * @implNote This query is to select the column using variable as input from user (DB: columnName -> User Input: columnName)
   * @apiNote It implements {@link AvailableRoomOnDateCustom} which is extended
   * from {@link com.jfcm.manda.bookingmanagerapi.repository.AvailableRoomOnDateRepository}
   */
  @Override
  public String checkIfRoomIsAvailableOnAGivenDate(String roomType, String roomStatus, String date) {
    String query = String.format("SELECT t.%s FROM AvailabilityCalendarEntity t WHERE t.%s = '%s' AND t.availableDate = '%s'", roomType, roomType, roomStatus, date);
    return entityManager.createQuery(query, AvailabilityCalendarEntity.class).getResultList().toString();
  }

  @Transactional
  @Override
  public int updateRoomStatusOnAGivenDate(String roomType, String status, String id) {
    String updateQuery = String.format("UPDATE AvailabilityCalendarEntity t SET t.%s = '%s' WHERE t.id = '%s'", roomType, status, id.replace("-", ""));
    return entityManager.createQuery(updateQuery).executeUpdate();
  }

  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }
}