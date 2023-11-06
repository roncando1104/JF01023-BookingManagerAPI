/**
 * {@link com.jfcm.manda.bookingmanagerapi.repository.SimbahayRepository}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.repository;

import com.jfcm.manda.bookingmanagerapi.model.SimbahayGroupsEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimbahayRepository extends JpaRepository<SimbahayGroupsEntity, String> {

  Optional<SimbahayGroupsEntity> findById(String id);

  List<SimbahayGroupsEntity> findAll();

  void deleteById(String id);
}