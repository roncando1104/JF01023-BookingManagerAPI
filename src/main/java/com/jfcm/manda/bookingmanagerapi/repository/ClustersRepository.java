/**
 * {@link com.jfcm.manda.bookingmanagerapi.repository.ClustersRepository}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.repository;

import com.jfcm.manda.bookingmanagerapi.model.ClusterGroupsEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClustersRepository extends JpaRepository<ClusterGroupsEntity, String> {

  Optional<ClusterGroupsEntity> findById(String id);
  List<ClusterGroupsEntity> findAll();
  void deleteById(String id);
}
