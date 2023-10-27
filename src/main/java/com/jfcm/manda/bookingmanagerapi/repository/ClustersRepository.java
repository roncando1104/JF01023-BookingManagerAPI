package com.jfcm.manda.bookingmanagerapi.repository;

import com.jfcm.manda.bookingmanagerapi.model.Clusters;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClustersRepository extends JpaRepository<Clusters, String> {

  Optional<Clusters> findById(String id);
  List<Clusters> findAll();
  void deleteById(String id);
}
