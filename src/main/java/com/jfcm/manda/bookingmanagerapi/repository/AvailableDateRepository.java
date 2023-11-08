/**
 * {@link com.jfcm.manda.bookingmanagerapi.repository.AvailableDateRepository}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the
 * confidential and proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.repository;

import com.jfcm.manda.bookingmanagerapi.model.AvailabilityCalendarEntity;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailableDateRepository extends JpaRepository<AvailabilityCalendarEntity, String> {

  @Query(nativeQuery = true,
      value = "SELECT * FROM availability_calendar WHERE dates LIKE '%'||:date||'%' AND id LIKE '%'||:id||'%'")
  List<AvailabilityCalendarEntity> getAllOldDatesFromLastYear(@Param("date") String date, @Param("id") String id);

  @Query(nativeQuery = true,
      value = "SELECT * FROM availability_calendar WHERE dates = :date")
  List<AvailabilityCalendarEntity> checkRoomsAvailableOnAGivenDate(@Param("date") String date);

  @Modifying
  @Transactional
  @Query(nativeQuery = true,
      value = "DELETE FROM availability_calendar WHERE dates LIKE '%'||:date||'%' AND id LIKE '%'||:id||'%'")
  void deleteByDateYear(@Param("date") String date, @Param("id") String id);
}
