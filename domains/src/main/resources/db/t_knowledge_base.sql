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

INSERT INTO `fast-llm`.t_knowledge_base (name, description, organization_id, status, deleted, create_time, update_time) VALUES ('法律尝试', '法', 1, 'active', 1, null, null);
INSERT INTO `fast-llm`.t_knowledge_base (name, description, organization_id, status, deleted, create_time, update_time) VALUES ('法律常识', '法律常识知识库包含了我国基本的法律内容', 1, 'active', 1, null, null);
INSERT INTO `fast-llm`.t_knowledge_base (name, description, organization_id, status, deleted, create_time, update_time) VALUES ('法律尝试2', '法', 1, 'active', 1, null, null);
INSERT INTO `fast-llm`.t_knowledge_base (name, description, organization_id, status, deleted, create_time, update_time) VALUES ('2', '2', 1, 'active', 1, null, '2024-12-15 02:18:19');
INSERT INTO `fast-llm`.t_knowledge_base (name, description, organization_id, status, deleted, create_time, update_time) VALUES ('3', '4', 1, 'active', 1, '2024-12-14 10:08:36', '2024-12-15 02:18:21');
INSERT INTO `fast-llm`.t_knowledge_base (name, description, organization_id, status, deleted, create_time, update_time) VALUES ('4', '4', 1, 'active', 1, '2024-12-14 10:09:36', '2024-12-15 02:18:16');
INSERT INTO `fast-llm`.t_knowledge_base (name, description, organization_id, status, deleted, create_time, update_time) VALUES ('LLM 学习资料', '大模型学习资料，论文', 1, 'inactive', 0, '2024-12-15 02:18:44', '2024-12-15 08:35:55');
