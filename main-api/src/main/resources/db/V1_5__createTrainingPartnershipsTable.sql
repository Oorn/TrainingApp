drop table if exists training_app.training_partnerships;
create table training_app.training_partnerships
(
    id         bigserial
        constraint training_partnerships_pk
            primary key,
    trainer_fk bigint not null
        constraint training_partnerships_trainers_id_fk
            references training_app.trainers
            on delete cascade,
    trainee_fk bigint not null
        constraint training_partnerships_trainees_id_fk
            references training_app.trainees
            on delete cascade,
    is_removed boolean default false
);

drop index if exists training_partnerships_id_uindex;
create unique index training_partnerships_id_uindex
    on training_app.training_partnerships (id);

drop index if exists training_partnerships_trainer_fk_trainee_fk_index;
create index training_partnerships_trainer_fk_trainee_fk_index
    on training_app.training_partnerships (trainer_fk, trainee_fk);

drop index if exists training_partnerships_trainee_fk_index;
create index training_partnerships_trainee_fk_index
    on training_app.training_partnerships (trainee_fk);
