//package com.jfcm.manda.bookingmanagerapi.repository;
//
//import com.jfcm.manda.bookingmanagerapi.dao.request.SignUpRequest;
//import com.jfcm.manda.bookingmanagerapi.model.UsersEntity;
//import jakarta.transaction.Transactional;
//import java.util.Optional;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface SignUpRequestRepository extends JpaRepository<SignUpRequest, String> {
//
//  Optional<SignUpRequest> findById(String id);
//  Optional<SignUpRequest> findByUserName(String userName);
//  @Modifying
//  @Transactional
//  @Query(nativeQuery = true,
//      value = "UPDATE users SET password = :password WHERE id = :id")
//  void updatePassword(@Param("password") String password, @Param("id") String id);
//}
