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

INSERT INTO `fast-llm`.t_organization (name, deleted, create_time, update_time) VALUES ('x.stark.dylan@gmail.com', 0, null, '2024-12-11 05:30:09');
INSERT INTO `fast-llm`.t_organization (name, deleted, create_time, update_time) VALUES ('admin@gmail.com', 0, '2024-12-15 07:42:05', '2024-12-15 07:42:05');
