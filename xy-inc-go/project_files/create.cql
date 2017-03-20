CREATE KEYSPACE IF NOT EXISTS xy_inc WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

CREATE TABLE IF NOT EXISTS xy_inc.product (
	id uuid,
	name text,
	description text,
	price float,
	category text,
	PRIMARY KEY (id)
);
