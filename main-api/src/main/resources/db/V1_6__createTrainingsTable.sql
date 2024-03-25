drop table if exists training_app.trainings;
create table training_app.trainings
(
    id                      bigserial
        constraint trainings_pk
            primary key,
    training_name           varchar(100) default '' not null,
    timestamp_from               timestamp                    not null,
    timestamp_to                 timestamp                    not null,
    training_partnership_fk bigint                  not null
        constraint trainings_training_partnerships_id_fk
            references training_app.training_partnerships
            on delete cascade,
    is_removed              boolean      default false
);

drop index if exists trainings_date_from_date_to_index;
create index trainings_date_from_date_to_index
    on training_app.trainings (timestamp_from, timestamp_to);

drop index if exists trainings_date_to_index;
create index trainings_date_to_index
    on training_app.trainings (timestamp_to);

drop index if exists trainings_id_uindex;
create unique index trainings_id_uindex
    on training_app.trainings (id);

drop index if exists trainings_training_partnership_fk_index;
create index trainings_training_partnership_fk_index
    on training_app.trainings (training_partnership_fk);

