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
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "availability_calendar")
public class AvailabilityCalendarEntity {

  @Id
  private String id;
  @Column(name = "dates")
  private String availableDate;
}