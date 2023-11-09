/*
 *  BookedEventsRepository.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.repository;

import com.jfcm.manda.bookingmanagerapi.model.BookedEventsEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookedEventsRepository extends JpaRepository<BookedEventsEntity, String> {

  @Query(nativeQuery = true,
      value = "SELECT * from reservation WHERE client_id = :clientId")
  List<BookedEventsEntity> findReservationBookedEventDateByClientId(@Param("clientId") String clientId);
}