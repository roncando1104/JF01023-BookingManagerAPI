/**
 * {@link com.jfcm.manda.bookingmanagerapi.dao.request.SignUpRequest}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.dao.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jfcm.manda.bookingmanagerapi.model.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignUpRequest {

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
  @Column(name = "email_add")
  private String emailAdd;
  @JsonProperty(required = true)
  @NonNull
  @Column(name = "contact_number")
  private String contactNumber;
  @JsonProperty(required = true)
  @NonNull
  private String address;
  @JsonProperty(required = true)
  @NonNull
  private String birthday;
  @JsonProperty(required = true)
  @NonNull
  @Enumerated(EnumType.STRING)
  private Role role;
  @JsonProperty(required = true)
  @NonNull
  private String status;
  @JsonProperty(required = true)
  @NonNull
  private String cluster;
  @JsonProperty(required = true)
  @NonNull
  @Column(name = "cluster_code")
  private String clusterCode;
  @JsonProperty(required = true)
  @NonNull
  @Column(name = "simbahay_name")
  private String simbahayName;
  @JsonProperty(required = true)
  @NonNull
  @Column(name = "simbahay_code")
  private String simbahayCode;
  @JsonProperty(required = true)
  @NonNull
  @Column(name = "user_name")
  private String userName;
  @JsonProperty(required = true)
  @NonNull
  private String password;

}