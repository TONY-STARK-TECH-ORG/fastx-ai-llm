create table t_organization_tools
(
    id              bigint auto_increment
        primary key,
    organization_id bigint                               not null,
    tool_code       varchar(128)                         not null,
    tool_version    varchar(64)                          not null,
    config_data     json                                 null,
    custom          tinyint    default 0                 null,
    custom_code     text                                 null,
    deleted         tinyint(1) default 0                 not null,
    create_time     timestamp  default CURRENT_TIMESTAMP null,
    update_time     timestamp  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint t_organization_tools_pk
        unique (organization_id, tool_code, tool_version)
);

