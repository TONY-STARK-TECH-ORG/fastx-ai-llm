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

INSERT INTO `fast-llm`.t_organization_tools (organization_id, tool_code, tool_version, config_data, custom, custom_code, deleted, create_time, update_time) VALUES (1, 'openai.chat', '1.0.0', '{"apiKey": "sk-02dOKWbd6R3oZ9CoBCbxELB348OnjJ6iFePyPqzQBm9lLv7x", "baseUrl": "https://open.xiaojingai.com/v1", "streaming": true, "organizationId": 1}', 0, null, 0, '2024-12-15 01:01:08', '2024-12-15 01:01:08');
