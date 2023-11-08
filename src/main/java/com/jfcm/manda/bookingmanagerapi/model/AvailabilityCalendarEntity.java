/**
 * {@link com.jfcm.manda.bookingmanagerapi.model.AvailabilityCalendarEntity}.java
 * Copyright © 2023 JFCM. All rights reserved. This software is the confidential
 * and proprietary information of JFCM Mandaluyong
 *
 * @author Ronald Cando
 */
package com.jfcm.manda.bookingmanagerapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "availability_calendar")
@EnableJpaRepositories
public class AvailabilityCalendarEntity {

  @Id
  private String id;
  @Column(name = "dates")
  private String availableDate;

  @Column(name = "sow_room1")
  @Enumerated(EnumType.STRING)
  private RoomStatusEnum sowRoom1;

  @Column(name = "sow_room2")
  @Enumerated(EnumType.STRING)
  private RoomStatusEnum sowRoom2;

  @Column(name = "room_1")
  @Enumerated(EnumType.STRING)
  private RoomStatusEnum room1;

  @Column(name = "room_2")
  @Enumerated(EnumType.STRING)
  private RoomStatusEnum room2;
}