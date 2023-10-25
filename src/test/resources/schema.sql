CREATE TABLE 'clusters'(
    'cluster_code' varchar,
    'cluster_name' varchar(255),
    'cluster_leader1' varchar(255),
    'cluster_leader2' varchar(255),
    'total_simbahay' int,
    primary key ('cluster_code')
);

CREATE TABLE 'reservation'(
    'id' varchar,
    'booking_date' varchar(255),
    'room' varchar(255),
    'group_name' varchar(255),
    'group_code' varchar(255),
    'activity' varchar(255),
    'booked_by' varchar(255),
    'client_id' varchar(255),
    'with_fee' varchar(255),
    'total_fee' int,
    'status' varchar,
    primary key ('id')
);

CREATE TABLE 'simbahay_grp'(
    'id' varchar,
    'simbahay_name' varchar(255),
    'simbahay_sched' varchar(255),
    'cluster' varchar(255),
    'cluster_code' varchar(255),
    'simbahay_leader1' varchar(255),
    'simbahay_leader2' varchar(255),
    'total_members' int,
    'location' varchar(255),
    'status' varchar(255),
    primary key ('id')
);

CREATE TABLE 'users'(
    'id' varchar,
    'first_name' varchar(255),
    'middle_name' varchar(255),
    'last_name' varchar(255),
    'role' varchar(255),
    'cluster' varchar(255),
    'cluster_code' varchar(255),
    primary key ('id')
);