create table event_day (
    id      bigint  not null,
    holiday boolean not null,
    name    varchar(255),
    season  bigint,
    primary key (id)
)