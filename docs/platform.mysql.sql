-- pf_user
DROP TABLE IF EXISTS pf_user;
CREATE TABLE pf_user
(
    id           BIGINT(20) UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '主键ID',
    username     VARCHAR(50)         DEFAULT NULL COMMENT '用户名',
    password     VARCHAR(255)        DEFAULT NULL COMMENT '密码',
    nickname     VARCHAR(50)         DEFAULT NULL COMMENT '昵称',
    name         VARCHAR(50)         DEFAULT NULL COMMENT '姓名',
    introduction VARCHAR(500)        DEFAULT NULL COMMENT '用户简介',
    avatar       VARCHAR(1024)       DEFAULT NULL COMMENT '头像链接',
    gender       TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '性别',
    birthday     DATETIME            DEFAULT NULL COMMENT '生日',
    phone        VARCHAR(20)         DEFAULT NULL COMMENT '手机号',
    email        VARCHAR(50)         DEFAULT NULL COMMENT '邮箱',
    status       TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '账号状态',
    created_at   DATETIME COMMENT '创建时间',
    updated_at   DATETIME COMMENT '更新时间',
    deleted      TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '删除标识',
    PRIMARY KEY (id),
    UNIQUE KEY idx_pf_user_username (username)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';

-- pf_role
DROP TABLE IF EXISTS pf_role;
CREATE TABLE pf_role
(
    id          BIGINT(20) UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '主键ID',
    name        VARCHAR(50)         DEFAULT NULL COMMENT '角色名称',
    `key`       VARCHAR(50)         DEFAULT NULL COMMENT '角色key',
    permissions TEXT COMMENT '权限列表',
    remark      VARCHAR(500)        DEFAULT NULL COMMENT '备注',
    created_at  DATETIME COMMENT '创建时间',
    updated_at  DATETIME COMMENT '更新时间',
    deleted     TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '删除标识',
    PRIMARY KEY (id),
    KEY idx_pf_role_name (name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色表';

-- pf_permission_group
DROP TABLE IF EXISTS pf_permission_group;
CREATE TABLE pf_permission_group
(
    id         BIGINT(20) UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '主键ID',
    name       VARCHAR(50)         DEFAULT NULL COMMENT '权限组名',
    `key`      VARCHAR(50)         DEFAULT NULL COMMENT 'key',
    remark     VARCHAR(500)        DEFAULT NULL COMMENT '备注',
    created_at DATETIME COMMENT '创建时间',
    updated_at DATETIME COMMENT '更新时间',
    deleted    TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '删除标识',
    PRIMARY KEY (id),
    KEY idx_pf_permission_group_name (name),
    UNIQUE KEY idx_pf_permission_group_key (`key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='权限表';

-- pf_permission
DROP TABLE IF EXISTS pf_permission;
CREATE TABLE pf_permission
(
    id         BIGINT(20) UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '主键ID',
    group_id   BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '权限组ID',
    name       VARCHAR(50)         DEFAULT NULL COMMENT '权限名称',
    `key`      VARCHAR(50)         DEFAULT NULL COMMENT '权限key',
    remark     VARCHAR(500)        DEFAULT NULL COMMENT '备注',
    created_at DATETIME COMMENT '创建时间',
    updated_at DATETIME COMMENT '更新时间',
    deleted    TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '删除标识',
    PRIMARY KEY (id),
    KEY idx_pf_permission_name (name),
    UNIQUE KEY idx_pf_permission_key (`key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='权限表';

-- pf_user_role
DROP TABLE IF EXISTS pf_user_role;
CREATE TABLE pf_user_role
(
    id         BIGINT(20) UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '主键ID',
    user_id    BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '用户ID',
    role_id    BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '角色ID',
    created_at DATETIME COMMENT '创建时间',
    updated_at DATETIME COMMENT '更新时间',
    deleted    TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '删除标识',
    PRIMARY KEY (id),
    KEY idx_pf_user_role_user_id (user_id),
    KEY idx_pf_user_role_role_id (role_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户角色关联表';

-- pf_role_permission
DROP TABLE IF EXISTS pf_role_permission;
CREATE TABLE pf_role_permission
(
    id            BIGINT(20) UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '主键ID',
    role_id       BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '角色ID',
    permission_id BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '权限ID',
    created_at    DATETIME COMMENT '创建时间',
    updated_at    DATETIME COMMENT '更新时间',
    deleted       TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '删除标识',
    PRIMARY KEY (id),
    KEY idx_pf_role_permission_role_id (role_id),
    KEY idx_pf_role_permission_permission_id (permission_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色权限关联表';

-- pf_dict_entry
DROP TABLE IF EXISTS pf_dict_entry;
CREATE TABLE pf_dict_entry
(
    id         BIGINT(20) UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '主键项ID',
    entry_name VARCHAR(50)         DEFAULT NULL COMMENT '字典项名称',
    entry_key  VARCHAR(50)         DEFAULT NULL COMMENT '字典项',
    remark     VARCHAR(500)        DEFAULT NULL COMMENT '备注',
    created_at DATETIME COMMENT '创建时间',
    updated_at DATETIME COMMENT '更新时间',
    deleted    TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '删除标识',
    PRIMARY KEY (id),
    UNIQUE KEY idx_pf_dict_entry_entry_key (`entry_key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='字典项表';

-- pf_dict_value
DROP TABLE IF EXISTS pf_dict_value;
CREATE TABLE pf_dict_value
(
    id         BIGINT(20) UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '主键ID',
    entry_id   BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '字典项ID',
    dict_key   VARCHAR(50)         DEFAULT NULL COMMENT '字典值key',
    code       INT                 DEFAULT NULL COMMENT '字典code',
    value      VARCHAR(50)         DEFAULT NULL COMMENT '字典value',
    remark     VARCHAR(500)        DEFAULT NULL COMMENT '备注',
    created_at DATETIME COMMENT '创建时间',
    updated_at DATETIME COMMENT '更新时间',
    deleted    TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '删除标识',
    PRIMARY KEY (id),
    UNIQUE KEY idx_pf_dict_value_dict_key (`dict_key`)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='字典value表';

DROP TABLE IF EXISTS pf_user_login_log;
CREATE TABLE pf_user_login_log
(
    id         BIGINT(20) UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '主键ID',
    username   VARCHAR(50)         DEFAULT NULL COMMENT '用户账号',
    status     VARCHAR(50)         DEFAULT NULL COMMENT '登录状态',
    ip         VARCHAR(50)         DEFAULT NULL COMMENT '登录IP地址',
    location   VARCHAR(50)         DEFAULT NULL COMMENT '登录地点',
    browser    VARCHAR(50)         DEFAULT NULL COMMENT '浏览器类型',
    os         VARCHAR(50)         DEFAULT NULL COMMENT '操作系统',
    message    VARCHAR(500)        DEFAULT NULL COMMENT '提示消息',
    time       DATETIME COMMENT '访问时间',
    created_at DATETIME COMMENT '创建时间',
    updated_at DATETIME COMMENT '更新时间',
    deleted    TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '删除标识',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户登录日志表';

-- pf_file_detail
DROP TABLE IF EXISTS pf_file_detail;
CREATE TABLE pf_file_detail
(
    id                BIGINT(20) UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '主键ID',
    file_name         VARCHAR(50)         DEFAULT NULL COMMENT '文件名称',
    original_filename VARCHAR(50)         DEFAULT NULL COMMENT '原始文件名称',
    size              BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '文件大小',
    type              VARCHAR(50)         DEFAULT NULL COMMENT '文件类型',
    ext               VARCHAR(50)         DEFAULT NULL COMMENT '文件后缀',
    content_type      VARCHAR(50)         DEFAULT NULL COMMENT '文件contentType',
    user_id           BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '用户ID',
    path              VARCHAR(500)        DEFAULT NULL COMMENT '文件路径',
    url               VARCHAR(500)        DEFAULT NULL COMMENT '文件url',
    temp_key          VARCHAR(100)        DEFAULT NULL COMMENT '临时文件key',
    expired_at        DATETIME COMMENT '过期时间',
    attr              VARCHAR(500)        DEFAULT NULL COMMENT '文件属性',
    created_at        DATETIME COMMENT '创建时间',
    updated_at        DATETIME COMMENT '更新时间',
    deleted           TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '删除标识',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='文件详情表';
