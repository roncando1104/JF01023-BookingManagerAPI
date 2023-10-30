/*
 *  users.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class UsersEntity {

  /*
  id integer [primary key]
  first_name varchar
  middle_name varchar
  last_name varchar
  role varchar
  cluster varchar
  cluster_code varchar */

  @Id
  private String id;
  @JsonProperty(required = true)
  @NonNull
  @Column(name = "first_name")
  private String firstName;
  @Column(name = "middle_name")
  private String middleName;
  @JsonProperty(required = true)
  @NonNull
  @Column(name = "last_name")
  private String lastName;
  @JsonProperty(required = true)
  @NonNull
  private String role;
  @JsonProperty(required = true)
  @NonNull
  private String cluster;
  @JsonProperty(required = true)
  @NonNull
  @Column(name = "cluster_code")
  private String clusterCode;

}