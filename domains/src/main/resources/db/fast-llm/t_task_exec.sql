create table t_task_exec
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

