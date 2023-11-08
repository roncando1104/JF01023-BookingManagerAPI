DELETE FROM CLUSTERS;
DELETE FROM RESERVATION;
DELETE FROM SIMBAHAY_GRP;
DELETE FROM USERS;
//DELETE FROM AVAILABILITY_CALENDAR;

INSERT INTO clusters (cluster_code, cluster_name, cluster_leader1, cluster_leader2, total_simbahay) VALUES('cluster-001', 'Cluster 1', 'Jojo Miranda', 'Victor Lim', 4);
INSERT INTO clusters (cluster_code, cluster_name, cluster_leader1, cluster_leader2, total_simbahay) VALUES('cluster-002', 'Cluster 2', 'Ronald Cando', 'Dennis Tuando', 4);
INSERT INTO clusters (cluster_code, cluster_name, cluster_leader1, cluster_leader2, total_simbahay) VALUES('cluster-003', 'Cluster 3', 'Colo Jayona', 'Romeo Bunag', 4);
INSERT INTO clusters (cluster_code, cluster_name, cluster_leader1, cluster_leader2, total_simbahay) VALUES('cluster-004', 'Cluster 4', 'Jordan Panagsagan', 'Ting Santos', 4);

INSERT INTO reservation(id, booking_date, room, group_name, group_code, activity, booked_by, client_id, with_fee, total_fee, status) VALUES('5cb57724-32fd-4b3f-a21d-f0804349c81c', '2023-10-24', 'Room 1', 'Cluster 1', 'cluster-001', 'fellowship', 'Ronald Cando', 'JF-172755', true, 1500, 'FOR_APPROVAL');
INSERT INTO reservation(id, booking_date, room, group_name, group_code, activity, booked_by, client_id, with_fee, total_fee, status) VALUES('3c1333aa-373d-4f13-ab02-7c447446f8b2', '2023-10-25', 'Room 2', 'Cluster 2', 'cluster-002', 'fellowship', 'Jojo Miranda', 'JF-548943', false, 0, 'DENIED');
INSERT INTO reservation(id, booking_date, room, group_name, group_code, activity, booked_by, client_id, with_fee, total_fee, status) VALUES('3b050af2-2e25-4ecd-b6f9-cad3b741b945', '2023-10-28', 'Room 3', 'Cluster 3', 'cluster-003', 'fellowship', 'Colo Jayona', 'JF-166788', true, 1500, 'APPROVED');
INSERT INTO reservation(id, booking_date, room, group_name, group_code, activity, booked_by, client_id, with_fee, total_fee, status) VALUES('79fd7616-fa76-4052-a5d2-e9b12b37598d', '2023-10-29', 'Room 4', 'Cluster 4', 'cluster-004', 'fellowship', 'Victor Lim', 'JF-112134', false, 0, 'FOR_APPROVAL');

INSERT INTO simbahay_grp(id, simbahay_name, simbahay_sched, cluster, cluster_code, simbahay_leader1, simbahay_leader2, total_members, location, status) VALUES('SMBHY-034211', 'Saturday Group', 'Saturday 4:30PM', 'Cluster 1', 'cluster-001', 'Ronald Cando', 'Dennis Tuando', 12, 'Mandaluyong', 'active' );
INSERT INTO simbahay_grp(id, simbahay_name, simbahay_sched, cluster, cluster_code, simbahay_leader1, simbahay_leader2, total_members, location, status) VALUES('SMBHY-034212', 'Monday Group', 'Monday 7:30PM', 'Cluster 2', 'cluster-002', 'John Bautista', 'Jojo Miranda', 12, 'Mandaluyong', 'active' );
INSERT INTO simbahay_grp(id, simbahay_name, simbahay_sched, cluster, cluster_code, simbahay_leader1, simbahay_leader2, total_members, location, status) VALUES('SMBHY-023123', 'Tuesday Group', 'Tuesday 6:30PM', 'Cluster 3', 'cluster-003', 'Colo Jayona', 'Tony Star', 12, 'Mandaluyong', 'active' );
INSERT INTO simbahay_grp(id, simbahay_name, simbahay_sched, cluster, cluster_code, simbahay_leader1, simbahay_leader2, total_members, location, status) VALUES('SMBHY-099231', 'Thursday Group', 'Thursday 8:30PM', 'Cluster 4', 'cluster-004', 'Jordan Panagsagan', 'Victor Lim', 12, 'Mandaluyong', 'active' );

INSERT INTO users(id, first_name, middle_name, last_name, email_add, contact_number, address, birthday, role, status, cluster, cluster_code, simbahay_name, simbahay_code, user_name, password) VALUES('JF-172775', 'Ronald', 'Collado', 'Cando', 'ron.cando04@gmail.com', '09279471440', '4775 Guadalcanal st. Sta. Mesa, Manila', '1985-11-04', 'AE', 'ACTIVE', 'Cluster 1', 'cluster-001', 'Saturday Group', 'smbhy-007', 'ron1104', 'ron1104');
INSERT INTO users(id, first_name, middle_name, last_name, email_add, contact_number, address, birthday, role, status, cluster, cluster_code, simbahay_name, simbahay_code, user_name, password) VALUES('JF-548943', 'Jojo', 'A', 'Miranda', 'jojomiranda@gmail.com', '09162341111', '111 Boni Ave. Mandaluyong', '1985-2-21', 'AE', 'ACTIVE', 'Cluster 2', 'cluster-002', 'Monday Group', 'smbhy-001', 'jojo1111', 'jojo1111');
INSERT INTO users(id, first_name, middle_name, last_name, email_add, contact_number, address, birthday, role, status, cluster, cluster_code, simbahay_name, simbahay_code, user_name, password) VALUES('JF-166788', 'Colo', 'C', 'Jayona', 'colo@gmail.com', '09171112233', '123 Capitolyo, Pasig', '1982-05-12', 'AE', 'ACTIVE', 'Cluster 3', 'cluster-003', 'Thursday Group', 'smbhy-004', 'colo222', 'colo222');
INSERT INTO users(id, first_name, middle_name, last_name, email_add, contact_number, address, birthday, role, status, cluster, cluster_code, simbahay_name, simbahay_code, user_name, password) VALUES('JF-112134', 'Victor', 'M', 'Lim', 'victorlim@gmail.com', '09193334444', '12 Sampaloc Manila', '1964-11-02', 'AE', 'ACTIVE', 'Cluster 4', 'cluster-004', 'Wednesday Group', 'smbhy-003', 'victor02', 'victor02');

//INSERT INTO availability_calendar(id, dates, sow_room1, sow_room2, room_1, room_2) VALUES ( '20231129', '2023-11-31', 'available', 'available', 'available', 'available' );