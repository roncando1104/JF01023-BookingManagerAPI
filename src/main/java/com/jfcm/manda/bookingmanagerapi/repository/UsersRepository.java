/*
 *  Repository.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.repository;

import com.jfcm.manda.bookingmanagerapi.model.Users;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {

  Optional<Users> findById(String id);
  List<Users> findAll();
  void deleteById(String id);

  @Query(nativeQuery = true,
  value = "SELECT id from users WHERE first_name = :firstName AND last_name = :lastName")
  String findUserIdByFullName(@Param("firstName") String firstName, @Param("lastName") String lastName);
}