create table t_task_log
(
    id            bigint auto_increment
        primary key,
    task_id       bigint                                                                  not null,
    status        enum ('wait', 'running', 'complete', 'error') default 'wait'            not null,
    complete_time timestamp                                                               null,
    log           longtext                                                                null,
    deleted       tinyint(1)                                    default 0                 not null,
    create_time   timestamp                                     default CURRENT_TIMESTAMP null,
    update_time   timestamp                                     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 03:48:49', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 03:50:07', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:18:28', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:18:29', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:18:30', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:18:30', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:18:31', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:18:31', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:18:32', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:18:33', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:18:34', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:18:34', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:18:34', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:18:35', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:18:35', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:18:38', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:18:38', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:39', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:40', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:41', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:41', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:42', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:42', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:42', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:42', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:43', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:43', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:43', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:43', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:44', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:44', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:44', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:44', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:44', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:44', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:45', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:45', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:45', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:45', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:45', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:45', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:46', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:46', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:46', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:46', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:46', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:47', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:47', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:47', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:47', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:47', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:48', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:48', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:48', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:48', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 04:24:48', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 05:13:39', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 05:13:56', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 05:13:58', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 1, '2024-12-15 05:13:59', '2024-12-15 05:22:08');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (28, 'wait', null, null, 0, '2024-12-15 05:36:06', '2024-12-15 05:36:06');
INSERT INTO `fast-llm`.t_task_exec (task_id, status, complete_time, log, deleted, create_time, update_time) VALUES (29, 'wait', null, null, 0, '2024-12-15 05:36:09', '2024-12-15 05:36:09');
