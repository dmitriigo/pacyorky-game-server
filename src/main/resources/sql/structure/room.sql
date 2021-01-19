create table room
(
    id            bigint  not null,
    capacity      bigint,
    password      varchar(255),
    private_room  boolean not null,
    time_to_start bigint,
    with_computer boolean not null,
    game_id       bigint,
    primary key (id)
)