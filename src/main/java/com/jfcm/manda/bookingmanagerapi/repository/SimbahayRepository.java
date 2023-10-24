/*
 *  SimbahayRepository.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.repository;

import com.jfcm.manda.bookingmanagerapi.model.SimbahayGroups;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimbahayRepository extends CrudRepository<SimbahayGroups, String> {

  Optional<SimbahayGroups> findById(String id);
  List<SimbahayGroups> findAll();
  void deleteById(String id);
}