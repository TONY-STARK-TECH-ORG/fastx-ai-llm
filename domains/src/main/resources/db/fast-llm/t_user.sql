create table t_user
(
    id                 bigint auto_increment
        primary key,
    email              varchar(100)                                                             null,
    password           varchar(255)                                                             null,
    role               enum ('admin', 'normal', 'visitor')            default 'normal'          not null,
    status             enum ('ban', 'active', 'wait')                 default 'wait'            not null,
    username           varchar(50)                                                              null,
    last_login         datetime                                                                 null,
    profile_image_url  varchar(255)                                                             null,
    bio                text                                                                     null,
    expertise_areas    json                                                                     null,
    preferred_language varchar(20)                                                              null,
    social_links       json                                                                     null,
    auth_provider      enum ('local', 'google', 'facebook', 'github') default 'local'           null,
    auth_open_id       varchar(64)                                                              null,
    deleted            tinyint(1)                                     default 0                 not null,
    create_time        timestamp                                      default CURRENT_TIMESTAMP null,
    update_time        timestamp                                      default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint email
        unique (email)
);

