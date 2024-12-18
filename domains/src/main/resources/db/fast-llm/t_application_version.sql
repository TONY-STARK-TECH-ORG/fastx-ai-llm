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

