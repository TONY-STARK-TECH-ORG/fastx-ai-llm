create table t_organization
(
    id          bigint auto_increment
        primary key,
    name        varchar(128)                         not null,
    deleted     tinyint(1) default 0                 not null,
    create_time timestamp  default CURRENT_TIMESTAMP null,
    update_time timestamp  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint name
        unique (name)
);

