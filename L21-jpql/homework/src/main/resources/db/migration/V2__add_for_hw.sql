create sequence address_SEQ start with 1 increment by 1;
create sequence phone_SEQ start with 1 increment by 1;

create table address
(
    id     bigint not null primary key,
    street varchar(50)
);

create table phone
(
    phone_id  bigint not null primary key,
    number    varchar(50),
    client_id bigint
);

alter table if exists client
    add constraint fk_client_address
    foreign key (address_id)
    references address;

alter table if exists phone
    add constraint fk_phone_client
    foreign key (client_id)
    references client;