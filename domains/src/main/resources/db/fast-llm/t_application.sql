create table t_application
(
    id              bigint auto_increment
        primary key,
    name            varchar(128)                                           not null,
    type            enum ('agent', 'playground') default 'agent'           not null,
    description     text                                                   null,
    icon_url        varchar(255)                                           null,
    status          enum ('active', 'inactive')  default 'inactive'        not null,
    organization_id bigint                                                 not null,
    deleted         tinyint(1)                   default 0                 not null,
    create_time     timestamp                    default CURRENT_TIMESTAMP null,
    update_time     timestamp                    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

