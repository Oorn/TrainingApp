drop table if exists training_app.trainers;
create table training_app.trainers
(
    id            bigserial
        constraint trainers_pk
            primary key,
    user_fk       bigint not null
        constraint trainers_users_id_fk
            references training_app.users
            on delete cascade,
    training_type_fk       bigint not null
        constraint trainers_training_types_id_fk
            references training_app.training_types
            on delete cascade,
    is_removed boolean      default false
);

drop index if exists trainers_id_uindex;
create unique index trainers_id_uindex
    on training_app.trainers (id);

drop index if exists trainers_user_fk_uindex;
create unique index trainers_user_fk_uindex
    on training_app.trainers (user_fk);