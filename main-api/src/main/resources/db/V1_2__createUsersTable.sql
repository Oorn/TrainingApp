drop table if exists training_app.users;
create table training_app.users
(
    id         bigserial
        constraint users_pk
            primary key,
    first_name varchar(50)  default '' not null,
    last_name  varchar(50)  default '' not null,
    username   varchar(110)            not null,
    password_hash   varchar(100) default '' not null,
    password_salt   varchar(100) default '' not null,
    is_active  boolean      default true,
    is_removed boolean      default false
);

drop index if exists users_id_uindex;
create unique index users_id_uindex
    on training_app.users (id);

drop index if exists users_username_uindex;
create unique index users_username_uindex
    on training_app.users (username);