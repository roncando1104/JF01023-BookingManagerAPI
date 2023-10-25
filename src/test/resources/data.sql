INSERT INTO clusters (cluster_code, cluster_name, cluster_leader1, cluster_leader2, total_simbahay) VALUES('cluster-001', 'Cluster 1', 'Jojo Miranda', 'Victor Lim', 4);
INSERT INTO clusters (cluster_code, cluster_name, cluster_leader1, cluster_leader2, total_simbahay) VALUES('cluster-002', 'Cluster 2', 'Ronald Cando', 'Dennis Tuando', 4);
INSERT INTO clusters (cluster_code, cluster_name, cluster_leader1, cluster_leader2, total_simbahay) VALUES('cluster-003', 'Cluster 3', 'Colo Jayona', 'Romeo Bunag', 4);
INSERT INTO clusters (cluster_code, cluster_name, cluster_leader1, cluster_leader2, total_simbahay) VALUES('cluster-004', 'Cluster 4', 'Jordan Panagsagan', 'Ting Santos', 4);

INSERT INTO reservation(id, booking_date, room, group_name, group_code, activity, booked_by, client_id, with_fee, total_fee, status) VALUES('5cb57724-32fd-4b3f-a21d-f0804349c81c', '2023-10-24', 'Room 1', 'Cluster 1' 'cluster-001', 'fellowship', 'Ronald Cando', 'JF-172755', true, 1500, 'FOR_APPROVAL');

INSERT INTO simbahay_grp(id, simbahay_name, simbahay_sched, cluster, cluster_code, simbahay_leader1, simbahay_leader2, total_members, location, status) VALUES('SMBHY-034211', 'Saturday Group', 'Saturday 4:30PM', 'Cluster 1', 'cluster-001', 'Ronald Cando', 'Dennis Tuando', 12, 'Mandaluyong', 'active' );

INSERT INTO users(id, first_name, middle_name, last_name, role, cluster, cluster_code) VALUES('JF-172775', 'Ronald', 'Collado', 'Cando', 'AE', 'Cluster 1', 'cluster-001');
