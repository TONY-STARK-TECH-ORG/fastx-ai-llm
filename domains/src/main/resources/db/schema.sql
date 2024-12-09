create table if not exists t_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 用户的唯一标识符，主键，自增

    email VARCHAR(100) NOT NULL UNIQUE,    -- 电子邮件地址，唯一且不为空
    password VARCHAR(255) NOT NULL,        -- 加密后的用户密码，不为空
    role ENUM('admin', 'normal', 'visitor') NOT NULL DEFAULT 'normal', -- 用户角色
    status ENUM('ban', 'active', 'wait') NOT NULL DEFAULT 'wait', -- 用户状态

    username VARCHAR(50) NOT NULL,          -- 用户名
    last_login DATETIME,                    -- 用户最后登录时间，可选
    profile_image_url VARCHAR(255),         -- 用户头像图片URL，可选
    bio TEXT,                               -- 用户个人简介或描述，可选
    expertise_areas JSON,                   -- 用户擅长的领域，JSON格式，可选
    preferred_language VARCHAR(20),         -- 用户偏好的语言，可选
    social_links JSON,                      -- 用户的社交媒体链接，JSON格式，可选

    auth_provider ENUM('local', 'google', 'facebook', 'github') DEFAULT 'local', -- 用户认证提供者
    auth_open_id varchar(64), -- 三方用户唯一标识

    deleted TINYINT(1) NOT NULL DEFAULT 0,   -- 删除标识，逻辑删除
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 账号创建时间，默认当前时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 自动更新
);

create table if not exists t_organization (
    id bigint auto_increment primary key,
    `name` varchar(128) not null unique, -- when create user, auto create a same name organization

    deleted TINYINT(1) NOT NULL DEFAULT 0,   -- 删除标识，逻辑删除
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 账号创建时间，默认当前时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 自动更新
);

create table if not exists t_organization_user (
    id bigint auto_increment primary key,
    organization_id bigint not null, -- unique index with user_id
    user_id bigint not null, -- unique index with organization_id

    deleted TINYINT(1) NOT NULL DEFAULT 0,   -- 删除标识，逻辑删除
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 账号创建时间，默认当前时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 自动更新

    UNIQUE KEY unique_organization_user (organization_id, user_id)
);

create table if not exists t_application (
    id bigint auto_increment primary key,
    `name` varchar(128) not null unique,
    `type` enum('agent', 'playground') not null default 'agent',
    description text,
    icon_url varchar(255),
    status enum('active', 'inactive') not null default 'inactive',

    organization_id bigint not null,

    deleted TINYINT(1) NOT NULL DEFAULT 0,   -- 删除标识，逻辑删除
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 账号创建时间，默认当前时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 自动更新
);

create table if not exists t_workflow (
    id bigint auto_increment primary key,

    `name` varchar(128) not null unique,
    status enum('active', 'inactive') not null default 'inactive',

    organization_id bigint not null,

    deleted TINYINT(1) NOT NULL DEFAULT 0,   -- 删除标识，逻辑删除
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 账号创建时间，默认当前时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 自动更新
);

create table if not exists t_workflow_version (
    id bigint auto_increment primary key,
    workflow_id bigint not null,

    version varchar(64) not null unique,
    status enum('active', 'inactive') not null default 'inactive',
    version_data JSON, -- full react flow nodes data

    deleted TINYINT(1) NOT NULL DEFAULT 0,   -- 删除标识，逻辑删除
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 账号创建时间，默认当前时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 自动更新
);

create table if not exists t_workflow_exec_log (
    id bigint auto_increment primary key,
    workflow_version_id bigint not null,

    input_data JSON,
    output_data JSON,
    exec_data JSON,
    extra_data JSON,

    deleted TINYINT(1) NOT NULL DEFAULT 0,   -- 删除标识，逻辑删除
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 账号创建时间，默认当前时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 自动更新
);

create table if not exists t_tools (
    id bigint auto_increment primary key,
    `name` varchar(64) not null unique,
    author varchar(64) not null, -- author for this tool.
    `type` enum('llm-tool', 'llm-function', 'llm-model', 'pre-train', 'train', 'post-train') not null default 'llm-tool',

    deleted TINYINT(1) NOT NULL DEFAULT 0,   -- 删除标识，逻辑删除
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 账号创建时间，默认当前时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 自动更新
);

create table if not exists t_tools_version (
    id bigint auto_increment primary key,
    tool_id bigint not null,

    code varchar(128) not null unique, -- load spi via code with auto service.  unique_idx with version
    version varchar(64) not null unique, -- load spi via code: version. unique_idx with code

    status enum('active', 'inactive') not null default 'inactive',
    prototype JSON, -- contains: input, output key, value type, object properties, config properties and types, labels

    deleted TINYINT(1) NOT NULL DEFAULT 0,   -- 删除标识，逻辑删除
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 账号创建时间，默认当前时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 自动更新

    UNIQUE KEY unique_code_version (code, version)
);

create table if not exists t_tools_exec_log (
    id bigint auto_increment primary key,
    tool_version_id bigint not null,

    organization_id bigint not null,

    input_data JSON,
    output_data JSON,
    exec_data JSON,
    extra_data JSON,

    deleted TINYINT(1) NOT NULL DEFAULT 0,   -- 删除标识，逻辑删除
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 账号创建时间，默认当前时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 自动更新
);

create table if not exists t_organization_tools (
    id bigint auto_increment primary key,
    organization_id bigint not null,
    tool_id bigint not null,

    config_data JSON, -- input values exec tool needed.

    deleted TINYINT(1) NOT NULL DEFAULT 0,   -- 删除标识，逻辑删除
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 账号创建时间，默认当前时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 自动更新
);

create table if not exists t_knowledge_base (
    id bigint auto_increment primary key,
    `name` varchar(64) not null unique,
    description text,

    organization_id bigint not null,
    status enum('active', 'inactive') not null default 'inactive',

    deleted TINYINT(1) NOT NULL DEFAULT 0,   -- 删除标识，逻辑删除
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 账号创建时间，默认当前时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 自动更新
);

create table if not exists t_task (
    id bigint auto_increment primary key,
    `name` varchar(64) not null unique,
    description text,

    organization_id bigint not null,

    cron varchar(128) not null, -- exec cron to spring scheduler ??
    workflow_id bigint not null, -- which workflow to execute ??

    status enum('active', 'inactive') not null default 'inactive',

    deleted TINYINT(1) NOT NULL DEFAULT 0,   -- 删除标识，逻辑删除
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 账号创建时间，默认当前时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 自动更新
);

create table if not exists t_task_log (
    id bigint auto_increment primary key,
    task_id bigint not null,

    status enum('wait', 'running', 'complete', 'error') not null default 'wait',
    complete_time TIMESTAMP,

    deleted TINYINT(1) NOT NULL DEFAULT 0,   -- 删除标识，逻辑删除
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 账号创建时间，默认当前时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 自动更新
);

create table if not exists t_application_version (
    id bigint auto_increment primary key,
    application_id bigint not null,

    version varchar(64) not null unique,
    status enum('active', 'inactive') not null default 'inactive',
    version_data JSON, -- full app config data

    deleted TINYINT(1) NOT NULL DEFAULT 0,   -- 删除标识，逻辑删除
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 账号创建时间，默认当前时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 自动更新
);

CREATE TABLE `undo_log` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `branch_id` bigint(20) NOT NULL,
    `xid` varchar(100) NOT NULL,
    `context` varchar(128) NOT NULL,
    `rollback_info` longblob NOT NULL,
    `log_status` int(11) NOT NULL,
    `log_created` datetime NOT NULL,
    `log_modified` datetime NOT NULL,
    `ext` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

alter table t_user
    modify email varchar(100) null;

alter table t_user
    modify password varchar(255) null;

alter table t_user
    modify username varchar(50) null;