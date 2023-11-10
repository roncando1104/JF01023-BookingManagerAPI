/*
 *  BookedEventsEntity.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookedEventsEntity {

  @Id
  private String id;
  @Column(name = "event_date")
  private LocalDate eventDate;
  private String room;
  @Column(name = "group_name")
  private String groupName;
  @Column(name = "group_code")
  private String groupCode;
  @Column(name = "booked_by")
  private String bookedBy;
  @Column(name = "client_id")
  private String clientId;
}