create table cities
(
    id   bigserial not null
        constraint city_pkey primary key,
    name varchar(255)
        constraint city_name_ukey unique,
    info text      not null
);