/*
 *  SignUpRequest.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.dao.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jfcm.manda.bookingmanagerapi.model.Role;
//import com.jfcm.manda.bookingmanagerapi.utils.CustomAuthorityDeserializerUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignUpRequest /*implements UserDetails*/ {
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

  //@JsonDeserialize(using = CustomAuthorityDeserializerUtil.class)
//  @Override
//  public Collection<? extends GrantedAuthority> getAuthorities() {
//    return List.of(new SimpleGrantedAuthority(role.name()));
//  }
//
//  @Override
//  public String getUsername() {
//    return userName;
//  }
//
//  @Override
//  public boolean isAccountNonExpired() {
//    return true;
//  }
//
//  @Override
//  public boolean isAccountNonLocked() {
//    return true;
//  }
//
//  @Override
//  public boolean isCredentialsNonExpired() {
//    return true;
//  }
//
//  @Override
//  public boolean isEnabled() {
//    if (StringUtils.equalsIgnoreCase(getStatus(), "ACTIVE")) {
//      return true;
//    } else {
//      return false;
//    }
//  }
}