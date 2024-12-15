create table t_task
(
    id              bigint auto_increment
        primary key,
    name            varchar(64)                                           not null,
    description     text                                                  null,
    organization_id bigint                                                not null,
    cron            varchar(128)                                          not null,
    workflow_id     bigint                                                not null,
    type            varchar(64)                                           not null comment 'task type',
    status          enum ('active', 'inactive') default 'inactive'        not null,
    deleted         tinyint(1)                  default 0                 not null,
    create_time     timestamp                   default CURRENT_TIMESTAMP null,
    update_time     timestamp                   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

INSERT INTO `fast-llm`.t_task (name, description, organization_id, cron, workflow_id, type, status, deleted, create_time, update_time) VALUES ('LLM 学习资料::fileProcess', '2412.09618v1.pdf', 1, '-1', 99999999999990, 'knowledge', 'inactive', 0, '2024-12-15 02:40:12', '2024-12-15 05:22:32');
INSERT INTO `fast-llm`.t_task (name, description, organization_id, cron, workflow_id, type, status, deleted, create_time, update_time) VALUES ('LLM 学习资料::fileProcess', '2412.09618v1.pdf', 1, '-1', 99999999999990, 'knowledge', 'inactive', 0, '2024-12-15 05:28:19', '2024-12-15 05:28:19');
