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

INSERT INTO `fast-llm`.t_application (name, type, description, icon_url, status, organization_id, deleted, create_time, update_time) VALUES ('1', 'agent', '2', 'https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png', 'inactive', 1, 1, null, null);
INSERT INTO `fast-llm`.t_application (name, type, description, icon_url, status, organization_id, deleted, create_time, update_time) VALUES ('2', 'agent', '4', 'https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png', 'inactive', 1, 1, null, null);
INSERT INTO `fast-llm`.t_application (name, type, description, icon_url, status, organization_id, deleted, create_time, update_time) VALUES ('3', 'agent', '4', 'https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png', 'inactive', 1, 1, null, null);
INSERT INTO `fast-llm`.t_application (name, type, description, icon_url, status, organization_id, deleted, create_time, update_time) VALUES ('1', 'playground', '123', 'https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png', 'inactive', 1, 1, null, null);
INSERT INTO `fast-llm`.t_application (name, type, description, icon_url, status, organization_id, deleted, create_time, update_time) VALUES ('1', 'playground', '123', 'https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png', 'inactive', 1, 1, null, null);
INSERT INTO `fast-llm`.t_application (name, type, description, icon_url, status, organization_id, deleted, create_time, update_time) VALUES ('1', 'playground', '123', 'https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png', 'inactive', 1, 1, null, null);
INSERT INTO `fast-llm`.t_application (name, type, description, icon_url, status, organization_id, deleted, create_time, update_time) VALUES ('2', 'agent', '4', 'https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png', 'inactive', 1, 1, null, null);
INSERT INTO `fast-llm`.t_application (name, type, description, icon_url, status, organization_id, deleted, create_time, update_time) VALUES ('5', 'agent', '7', 'https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png', 'inactive', 1, 1, null, null);
INSERT INTO `fast-llm`.t_application (name, type, description, icon_url, status, organization_id, deleted, create_time, update_time) VALUES ('客服机器人2', 'agent', '7', 'https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png', 'inactive', 1, 1, null, null);
INSERT INTO `fast-llm`.t_application (name, type, description, icon_url, status, organization_id, deleted, create_time, update_time) VALUES ('1', 'playground', '2', 'https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png', 'inactive', 1, 1, null, null);
INSERT INTO `fast-llm`.t_application (name, type, description, icon_url, status, organization_id, deleted, create_time, update_time) VALUES ('2', 'agent', '4', 'https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png', 'inactive', 1, 1, null, null);
INSERT INTO `fast-llm`.t_application (name, type, description, icon_url, status, organization_id, deleted, create_time, update_time) VALUES ('智能体', 'agent', '智能体测试', 'https://oss.fastx-ai.com/fastx-ai-llm/123/logo.png', 'inactive', 1, 0, '2024-12-15 05:28:41', '2024-12-15 05:28:41');
