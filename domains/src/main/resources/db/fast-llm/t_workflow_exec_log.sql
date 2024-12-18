create table t_workflow_exec_log
(
    id                  bigint auto_increment
        primary key,
    workflow_version_id bigint                               not null,
    input_data          json                                 null,
    output_data         json                                 null,
    exec_data           json                                 null,
    extra_data          json                                 null,
    deleted             tinyint(1) default 0                 not null,
    create_time         timestamp  default CURRENT_TIMESTAMP null,
    update_time         timestamp  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

