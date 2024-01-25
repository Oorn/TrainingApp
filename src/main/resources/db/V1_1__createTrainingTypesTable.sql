drop table if exists training_app.training_types;
create table training_app.training_types
(
    id   bigserial
        constraint training_types_pk
            primary key,
    training_type_name varchar(100) not null
);

drop index if exists training_types_name_uindex;
create unique index training_types_name_uindex
    on training_app.training_types (training_type_name);

