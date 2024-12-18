create table t_task_node_exec
(
    id            bigint auto_increment
        primary key,
    task_exec_id  bigint                                                       not null,
    node_id       varchar(255)                                                 not null,
    parent_node_ids text                                                         null,
    start_time    timestamp                                                    null,
    end_time      timestamp                                                    null,
    status        enum ('wait', 'running', 'finish') default 'wait'            not null,
    inputs        json                                                         null,
    outputs       json                                                         null,
    config        json                                                         null,
    deleted       int                                default 0                 not null,
    create_time   timestamp                          default CURRENT_TIMESTAMP not null,
    update_time   timestamp                          default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);

