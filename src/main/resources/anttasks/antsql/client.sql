drop table if exists client cascade;
create table client (
	client_id serial,
	client_name text not null
);
