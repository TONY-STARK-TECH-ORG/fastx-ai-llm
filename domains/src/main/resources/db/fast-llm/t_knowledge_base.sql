create table t_knowledge_base
(
    id              bigint auto_increment
        primary key,
    name            varchar(64)                                           null,
    description     text                                                  null,
    organization_id bigint                                                not null,
    status          enum ('active', 'inactive') default 'inactive'        not null,
    deleted         tinyint(1)                  default 0                 not null,
    create_time     timestamp                   default CURRENT_TIMESTAMP null,
    update_time     timestamp                   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

