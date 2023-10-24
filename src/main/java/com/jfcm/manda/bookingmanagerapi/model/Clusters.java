/*
 *  clusters.java
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
@Table(name = "clusters")
public class Clusters {

  /*
  cluster_code varchar [primary key]
  cluster_name varchar
  cluster_leader1 varchar
  cluster_leader2 varchar
  total_simbahay int
  */

  @Id
  @Column(name = "cluster_code")
  private String clusterCode;
  @Column(name = "cluster_name")
  private String clusterName;
  @Column(name = "cluster_leader1")
  private String clusterLeader1;
  @Column(name = "cluster_leader2")
  private String clusterLeader2;
  @Column(name = "total_simbahay")
  private int totalSimbahay;
}