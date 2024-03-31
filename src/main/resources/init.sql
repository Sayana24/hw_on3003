drop table if exists users;
create table users(
                      id int primary key generated always as identity,
                      name varchar(255) not null,
                      password varchar(255) not null
);