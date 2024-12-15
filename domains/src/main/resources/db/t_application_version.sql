create table t_application_version
(
    id             bigint auto_increment
        primary key,
    application_id bigint                                                not null,
    version        varchar(64)                                           not null,
    status         enum ('active', 'inactive') default 'inactive'        not null,
    version_data   json                                                  null,
    deleted        tinyint(1)                  default 0                 not null,
    create_time    timestamp                   default CURRENT_TIMESTAMP null,
    update_time    timestamp                   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint version
        unique (version, application_id)
);

INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (12, '1.0.0', 'inactive', '{"version": "1.0.0"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (13, '1.0.1', 'active', '{"version": "1.0.1"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (13, '1.0.2', 'inactive', '{"version": "1.0.2"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (13, '1.0.3', 'inactive', '{"version": "1.0.3"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (13, '1.0.4', 'inactive', '{"version": "1.0.4"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (13, '1.0.5', 'inactive', '{"version": "1.0.5"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (13, '1.0.6', 'inactive', '{"version": "1.0.6"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (13, '1.0.7', 'inactive', '{"version": "1.0.7"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (13, '1.0.8', 'active', '{"version": "1.0.8"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (13, '1.0.9', 'inactive', '{"version": "1.0.9"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (13, '1.1.0', 'active', '{"version": "1.1.0"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (13, '1.1.1', 'inactive', '{"version": "1.1.1"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (13, '1.1.2', 'inactive', '{"version": "1.1.2"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (13, '1.1.3', 'active', '{"version": "1.1.3"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (13, '1.0.22', 'active', '{"version": "1.0.22"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (13, '1.23', 'active', '{"version": "1.23"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (13, '1.0.88', 'inactive', '{"version": "1.0.88"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (13, '1.0.0ll', 'inactive', '{"version": "1.0.0ll"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (13, '1.0.04567', 'inactive', '{"version": "1.0.04567"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (13, '1.0.65', 'inactive', '{"version": "1.0.65"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (13, '1.0.653', 'inactive', '{"version": "1.0.653"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (13, '1.0.65344', 'inactive', '{"version": "1.0.65344"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (13, '1.0.653442', 'inactive', '{"version": "1.0.653442"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (14, '1.0.0', 'inactive', '{"version": "1.0.0"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (14, '1.0.1', 'inactive', '{"version": "1.0.1"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (14, '1.0.2', 'active', '{"version": "1.0.2"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (14, '1.0.3', 'inactive', '{"version": "1.0.3"}', 1, null, null);
INSERT INTO `fast-llm`.t_application_version (application_id, version, status, version_data, deleted, create_time, update_time) VALUES (15, '1.0.0', 'active', '{"version": "1.0.0"}', 0, '2024-12-15 05:28:56', '2024-12-15 05:28:56');
