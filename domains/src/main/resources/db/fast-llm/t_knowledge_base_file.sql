create table t_knowledge_base_file
(
    id                  bigint auto_increment
        primary key,
    knowledge_base_id   bigint                                                       not null,
    name                varchar(255)                                                 not null,
    extension           varchar(64)                                                  not null,
    download_url        text                                                         not null,
    vec_collection_name varchar(64)                                                  null,
    vec_collection_id   varchar(64)                                                  null,
    vec_partition_key   varchar(64)                                                  null,
    vec_partition_value varchar(64)                                                  null,
    status              enum ('active', 'process', 'wait') default 'wait'            not null,
    deleted             tinyint(1)                         default 0                 not null,
    create_time         timestamp                          default CURRENT_TIMESTAMP null,
    update_time         timestamp                          default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

