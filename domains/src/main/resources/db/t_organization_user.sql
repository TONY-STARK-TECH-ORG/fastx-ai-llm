create table t_organization_user
(
    id              bigint auto_increment
        primary key,
    organization_id bigint                               not null,
    user_id         bigint                               not null,
    deleted         tinyint(1) default 0                 not null,
    create_time     timestamp  default CURRENT_TIMESTAMP null,
    update_time     timestamp  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint unique_organization_user
        unique (organization_id, user_id)
);

INSERT INTO `fast-llm`.t_organization_user (organization_id, user_id, deleted, create_time, update_time) VALUES (1, 1, 0, null, '2024-12-11 05:30:09');
INSERT INTO `fast-llm`.t_organization_user (organization_id, user_id, deleted, create_time, update_time) VALUES (2, 3, 0, '2024-12-15 07:42:05', '2024-12-15 07:42:05');
INSERT INTO `fast-llm`.t_organization_user (organization_id, user_id, deleted, create_time, update_time) VALUES (1, 3, 0, '2024-12-15 07:49:18', '2024-12-15 07:49:34');
