package com.jfcm.manda.bookingmanagerapi.repository;

import com.jfcm.manda.bookingmanagerapi.model.ReservationEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, String> {

  Optional<ReservationEntity> findById(String id);
  List<ReservationEntity> findAll();
  void deleteById(String id);
}
