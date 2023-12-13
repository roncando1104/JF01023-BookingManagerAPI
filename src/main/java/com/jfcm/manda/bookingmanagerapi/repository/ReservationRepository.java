/**
 * {@link com.jfcm.manda.bookingmanagerapi.repository.ReservationRepository}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential
 * and proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.repository;

import com.jfcm.manda.bookingmanagerapi.model.ReservationEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, String> {

  Optional<ReservationEntity> findById(String id);

  @Query(nativeQuery = true,
      value = "SELECT * FROM reservation WHERE event_date LIKE '%'||:date||'%'")
  List<ReservationEntity> findByEventDate(@Param("date") String date);

  List<ReservationEntity> findAll();

  void deleteById(String id);


}
