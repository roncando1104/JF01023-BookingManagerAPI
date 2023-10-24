package com.jfcm.manda.bookingmanagerapi.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReservationStatusEnum {

  FOR_APPROVAL("FOR_APPROVAL"),
  APPROVED("APPROVED"),
  DENIED("DENIED"),
  CANCELLED("CANCELLED"),
  RESCHEDULED("RESCHEDULED");

  public final String value;

  ReservationStatusEnum(String value){
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ReservationStatusEnum fromValue(String value) {
    for (ReservationStatusEnum b : ReservationStatusEnum.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}
