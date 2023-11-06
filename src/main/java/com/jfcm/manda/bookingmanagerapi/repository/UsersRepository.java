/**
 * {@link com.jfcm.manda.bookingmanagerapi.repository.UsersRepository}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.repository;

import com.jfcm.manda.bookingmanagerapi.model.UsersEntity;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, String> {

  Optional<UsersEntity> findById(String id);

  Optional<UsersEntity> findByUserName(String userName);

  List<UsersEntity> findAll();

  void deleteById(String id);

  @Query(nativeQuery = true,
      value = "SELECT id from users WHERE first_name = :firstName AND last_name = :lastName")
  String findUserIdByFullName(@Param("firstName") String firstName, @Param("lastName") String lastName);

  @Modifying
  @Transactional
  @Query(nativeQuery = true,
      value = "UPDATE users SET password = :password WHERE id = :id")
  void updatePassword(@Param("password") String password, @Param("id") String id);

  @Query(nativeQuery = true,
      value = "SELECT id, first_name, last_name, birthday from users WHERE first_name = :firstName AND last_name = :lastName AND birthday = :birthday")
  String verifyUserAlreadyExist(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("birthday") String birthday);

}