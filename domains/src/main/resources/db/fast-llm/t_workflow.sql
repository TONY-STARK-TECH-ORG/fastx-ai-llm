create table t_workflow
(
    id              bigint auto_increment
        primary key,
    name            varchar(128)                                          not null,
    status          enum ('active', 'inactive') default 'inactive'        not null,
    organization_id bigint                                                not null,
    deleted         tinyint(1)                  default 0                 not null,
    create_time     timestamp                   default CURRENT_TIMESTAMP null,
    update_time     timestamp                   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

