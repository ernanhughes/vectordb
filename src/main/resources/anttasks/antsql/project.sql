drop table if exists project cascade;
create table project (
	project_id serial,
	project_name text not null
);