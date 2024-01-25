drop table if exists training_app.trainees;
create table training_app.trainees
(
    id            bigserial
        constraint trainees_pk
            primary key,
    date_of_birth date,
    address       varchar(200),
    user_fk       bigint not null
        constraint trainees_users_id_fk
            references training_app.users
            on delete cascade,
    is_removed boolean      default false
);

drop index if exists trainees_id_uindex;
create unique index trainees_id_uindex
    on training_app.trainees (id);

drop index if exists trainees_user_fk_uindex;
create unique index trainees_user_fk_uindex
    on training_app.trainees (user_fk);

