create table if not exists Report
(
    id                    bigint generated always as identity primary key,
    diet                  varchar(256) not null,
    report_about_feelings text         not null,
    report_about_habits   text         not null,
    photo_pet             bytea        not null,
    pet_id                bigint,
    constraint fk_pet_id foreign key (pet_id) references pet (id)
);