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

INSERT INTO `fast-llm`.t_user (email, password, role, status, username, last_login, profile_image_url, bio, expertise_areas, preferred_language, social_links, auth_provider, auth_open_id, deleted, create_time, update_time) VALUES ('x.stark.dylan@gmail.com', 'e10adc3949ba59abbe56e057f20f883e', 'normal', 'wait', 'starkdylan', null, '', null, null, null, null, 'local', null, 0, null, '2024-12-11 05:30:09');
INSERT INTO `fast-llm`.t_user (email, password, role, status, username, last_login, profile_image_url, bio, expertise_areas, preferred_language, social_links, auth_provider, auth_open_id, deleted, create_time, update_time) VALUES ('admin@gmail.com', 'e10adc3949ba59abbe56e057f20f883e', 'normal', 'wait', null, null, null, null, null, null, null, 'local', null, 0, '2024-12-15 07:42:05', '2024-12-15 07:42:05');
