drop table if exists profile;
create table profile (
    id serial primary key,
    email varchar(32) not null unique,
    password varchar(32) not null,
    name varchar(32),
    dob date,
    number varchar(16)
);
