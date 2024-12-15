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

INSERT INTO `fast-llm`.t_workflow (name, status, organization_id, deleted, create_time, update_time) VALUES ('我的工作流', 'inactive', 1, 0, '2024-12-15 08:35:28', '2024-12-15 08:35:28');
INSERT INTO `fast-llm`.t_workflow (name, status, organization_id, deleted, create_time, update_time) VALUES ('1', 'inactive', 1, 0, '2024-12-15 08:36:56', '2024-12-15 08:36:56');
INSERT INTO `fast-llm`.t_workflow (name, status, organization_id, deleted, create_time, update_time) VALUES ('2', 'inactive', 1, 0, '2024-12-15 08:37:01', '2024-12-15 08:37:01');
