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

