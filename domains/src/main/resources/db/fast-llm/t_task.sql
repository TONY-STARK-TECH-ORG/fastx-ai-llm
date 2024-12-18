create table `fast-llm`.t_task
(
    id                  bigint auto_increment
        primary key,
    name                varchar(64)                                           not null,
    description         text                                                  null,
    organization_id     bigint                                                not null,
    cron                varchar(128)                                          not null,
    workflow_version_id bigint                                                not null,
    type                varchar(64)                                           not null comment 'task type',
    status              enum ('active', 'inactive') default 'inactive'        not null,
    deleted             tinyint(1)                  default 0                 not null,
    create_time         timestamp                   default CURRENT_TIMESTAMP null,
    update_time         timestamp                   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

