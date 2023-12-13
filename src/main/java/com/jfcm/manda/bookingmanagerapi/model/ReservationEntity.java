/**
 * {@link com.jfcm.manda.bookingmanagerapi.model.ReservationEntity}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential and
 * proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "reservation")
public class ReservationEntity {

  /**
   id integer [primary key]
   booking_date varchar
   event_date varchar
   room varchar
   group_name varchar
   group_code varchar
   activity varchar
   booked_by varchar
   client_id varchar
   with_fee varchar
   total_fee int
   status varchar
   */

  @Id
  private String id;
  @JsonFormat(pattern="yyyy-MM-dd")
  @Column(name = "booking_date")
  private LocalDate bookingDate;
  @JsonFormat(pattern="yyyy-MM-dd")
  @Column(name = "event_date")
  private LocalDate eventDate;
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
  private String totalFee;
  @Enumerated(EnumType.STRING)
  private ReservationStatusEnum status;

}