/*
 *  SimbahayGroups.java
 *
 *  Copyright © 2023 ING Group. All rights reserved.
 *
 *  This software is the confidential and proprietary information of
 *  ING Group ("Confidential Information").
 */
package com.jfcm.manda.bookingmanagerapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "simbahay_grp")
public class SimbahayGroups {

  /*
  id integer [primary key]
  simbahay_name varchar
  simbahay_sched varchar
  cluster varchar
  cluster_code varchar
  simbahay_leader1 varchar
  simbahay_leader2 varchar
  total_members integer
  location varchar
  status varchar
  */

  @Id
  private String id;
  @Column(name = "simbahay_name")
  private String simbahayName;
  @Column(name = "simbahay_sched")
  private String simbahaySchedule;
  private String cluster;
  @Column(name = "cluster_code")
  private String clusterCode;
  @Column(name = "simbahay_leader1")
  private String simbahayLeader1;
  @Column(name = "simbahay_leader2")
  private String simbahayLeader2;
  @Column(name = "total_members")
  private int totalMembers;
  private String location;
  private String status;

}