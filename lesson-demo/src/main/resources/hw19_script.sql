create schema hw19;
set schema 'hw19';

create table "user"
(
    id       bigserial,
    email    varchar(100) not null,
    username varchar(100) not null,
    constraint user_pk primary key (id),
    constraint user_email_uq unique (email),
    constraint user_username_uq unique (username)
);

create type "advisor_role" as enum ('associate', 'partner', 'senior');

create table "advisor"
(
    user_id bigint,
    role    advisor_role not null,
    constraint advisor_pk primary key (user_id),
    constraint advisor_user_fk foreign key (user_id) references "user"
);

create table "applicant"
(
    user_id    bigint,
    first_name varchar(100) not null,
    last_name  varchar(100) not null,
    ssn        bigint       not null,
    constraint applicant_pk primary key (user_id),
    constraint applicant_user_fk foreign key (user_id) references "user",
    constraint applicant_ssn_uq unique (ssn)
);

create type "application_status" as enum ('new', 'assigned', 'on_hold', 'approved', 'canceled', 'declined');

create table "application"
(
    id           bigserial,
    amount       int                not null,
    status       application_status not null,
    created_at   timestamp          not null default now(),
    assigned_at  timestamp,
    applicant_id bigint             not null,
    advisor_id   bigint,
    constraint application_pk primary key (id),
    constraint application_applicant_fk foreign key (applicant_id) references applicant,
    constraint application_advisor_fk foreign key (advisor_id) references advisor
);

create index application_applicant_id_idx ON application (applicant_id);
create index application_advisor_id_idx ON application (advisor_id);

create table "address"
(
    id           bigserial,
    city         varchar(100) not null,
    street       varchar(100) not null,
    number       int          not null,
    zip          int          not null,
    apt          int,
    applicant_id bigint,
    constraint address_pk primary key (id),
    constraint address_applicant_fk foreign key (applicant_id) references applicant
);

create index address_applicant_id_idx ON address (applicant_id);

create type "phone_type" as enum ('home', 'work', 'mobile');

create table "phone_numbers"
(
    id           bigserial,
    phone_number bigint     not null,
    phone_type   phone_type not null,
    applicant_id bigint,
    constraint phone_numbers_pk primary key (id),
    constraint phone_numbers_applicant_fk foreign key (applicant_id) references applicant
);

create index phone_numbers_applicant_id_idx ON phone_numbers (applicant_id);
