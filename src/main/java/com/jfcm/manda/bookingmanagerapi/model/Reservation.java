/*
 *  reservations.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "reservation")
public class Reservation {

  /*
  id integer [primary key]
  booking_date varchar
  room varchar
  cluster_code varchar
  booked_by varchar
  with_fee varchar
  total_fee int
  status varchar
  */

  @Id
  private String id;
  @Column(name = "booking_date")
  private LocalDate bookingDate;
  private String room;
  @Column(name = "group_name")
  private String groupName;
  @Column(name = "group_code")
  private String groupCode;
  private String activity;
  @Column(name = "booked_by")
  private String bookedBy;
  @Column(name = "client_id")
  private String clientId;
  @Column(name = "with_fee")
  private boolean withFee;
  @Column(name = "total_fee")
  private BigDecimal totalFee;
  @Enumerated(EnumType.STRING)
  private ReservationStatusEnum status;

}