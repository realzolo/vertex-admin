-- sys_user
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user
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
    pwd_exp_date DATETIME            DEFAULT NULL COMMENT '密码过期时间',
    status       TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '账号状态',
    created_at   DATETIME COMMENT '创建时间',
    updated_at   DATETIME COMMENT '更新时间',
    deleted      TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '删除标识',
    PRIMARY KEY (id),
    UNIQUE KEY idx_sys_user_username (username)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';

-- sys_role
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role
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
    KEY idx_sys_role_name (name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色表';

-- sys_user_role
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role
(
    id         BIGINT(20) UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '主键ID',
    user_id    BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '用户ID',
    role_id    BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '角色ID',
    created_at DATETIME COMMENT '创建时间',
    updated_at DATETIME COMMENT '更新时间',
    deleted    TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '删除标识',
    PRIMARY KEY (id),
    KEY idx_sys_user_role_user_id (user_id),
    KEY idx_sys_user_role_role_id (role_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户角色关联表';

-- sys_menu
DROP TABLE IF EXISTS sys_menu;
CREATE TABLE sys_menu
(
    id          BIGINT(20) UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '主键ID',
    menu_name   VARCHAR(50)         DEFAULT NULL COMMENT '菜单名称',
    path        VARCHAR(500)        DEFAULT NULL COMMENT '路由地址',
    component   VARCHAR(500)        DEFAULT NULL COMMENT '组件路径',
    icon        VARCHAR(50)         DEFAULT NULL COMMENT '菜单图标',
    menu_type   VARCHAR(50)         DEFAULT NULL COMMENT '类型（M目录 C菜单 F按钮）',
    visible     TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '显示状态（true显示 false隐藏）',
    status      TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '菜单状态（true正常 false停用）',
    perms       VARCHAR(500)        DEFAULT NULL COMMENT '权限字符串',
    parent_id   BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '父菜单ID',
    parent_name VARCHAR(50)         DEFAULT NULL COMMENT '父菜单名称',
    order_num   INT                 DEFAULT NULL COMMENT '显示顺序',
    is_frame    TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '是否为外链（true是 false否）',
    is_cache    TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '是否缓存（true缓存 false不缓存）',
    query       VARCHAR(500)        DEFAULT NULL COMMENT '路由参数',
    remark      VARCHAR(500)        DEFAULT NULL COMMENT '备注',
    created_at  DATETIME COMMENT '创建时间',
    updated_at  DATETIME COMMENT '更新时间',
    deleted     TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '删除标识',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='菜单表';

-- sys_role_menu
DROP TABLE IF EXISTS sys_role_menu;
CREATE TABLE sys_role_menu
(
    id         BIGINT(20) UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '主键ID',
    role_id BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '角色ID',
    menu_id BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '菜单ID',
    created_at DATETIME COMMENT '创建时间',
    updated_at DATETIME COMMENT '更新时间',
    deleted    TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '删除标识',
    PRIMARY KEY (id),
    UNIQUE KEY idx_sys_role_menu_role_id_menu_id (`role_id`, `menu_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色菜单关联表';

DROP TABLE IF EXISTS sys_user_login_log;
CREATE TABLE sys_user_login_log
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

-- sys_file_detail
DROP TABLE IF EXISTS sys_file_detail;
CREATE TABLE sys_file_detail
(
    id                BIGINT(20) UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '主键ID',
    file_name         VARCHAR(50)  DEFAULT NULL COMMENT '文件名称',
    original_filename VARCHAR(50)  DEFAULT NULL COMMENT '原始文件名称',
    size              BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '文件大小',
    type              VARCHAR(50)  DEFAULT NULL COMMENT '文件类型',
    ext               VARCHAR(50)  DEFAULT NULL COMMENT '文件后缀',
    content_type      VARCHAR(50)  DEFAULT NULL COMMENT '文件contentType',
    user_id           BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '用户ID',
    path              VARCHAR(500) DEFAULT NULL COMMENT '文件路径',
    url               VARCHAR(500) DEFAULT NULL COMMENT '文件url',
    temp_key          VARCHAR(100) DEFAULT NULL COMMENT '临时文件key',
    expired_at        DATETIME COMMENT '过期时间',
    attr              VARCHAR(500) DEFAULT NULL COMMENT '文件属性',
    created_at        DATETIME COMMENT '创建时间',
    updated_at        DATETIME COMMENT '更新时间',
    deleted           TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '删除标识',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='文件详情表';

-- sys_access_log
DROP TABLE IF EXISTS sys_access_log;
CREATE TABLE sys_access_log
(
    id             BIGINT(20) UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '主键ID',
    user_id        BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '用户ID',
    user_type      VARCHAR(50)         DEFAULT NULL COMMENT '用户类型',
    user_name      VARCHAR(50)         DEFAULT NULL COMMENT '用户名称',
    module         VARCHAR(50)         DEFAULT NULL COMMENT '模块名称',
    action         VARCHAR(50)         DEFAULT NULL COMMENT '操作名称',
    description    VARCHAR(500)        DEFAULT NULL COMMENT '操作描述',
    url            VARCHAR(500)        DEFAULT NULL COMMENT '请求地址',
    method         VARCHAR(255)        DEFAULT NULL COMMENT '请求方式',
    params         VARCHAR(1024)       DEFAULT NULL COMMENT '请求参数',
    ip             VARCHAR(50)         DEFAULT NULL COMMENT 'IP地址',
    location       VARCHAR(100)        DEFAULT NULL COMMENT '地理位置',
    browser        VARCHAR(50)         DEFAULT NULL COMMENT '浏览器类型',
    os             VARCHAR(50)         DEFAULT NULL COMMENT '操作系统',
    success        TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '是否成功（true成功 false失败）',
    failure_reason VARCHAR(500)        DEFAULT NULL COMMENT '失败原因',
    time           BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '耗时（毫秒）',
    created_at     DATETIME COMMENT '创建时间',
    updated_at     DATETIME COMMENT '更新时间',
    deleted        TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '删除标识',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='访问日志表';

-- sys_message
DROP TABLE IF EXISTS sys_message;
CREATE TABLE sys_message
(
    id          BIGINT(20) UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '主键ID',
    user_id     BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '用户ID',
    type        TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '消息类型',
    title       VARCHAR(50)         DEFAULT NULL COMMENT '标题',
    content     VARCHAR(500)        DEFAULT NULL COMMENT '内容',
    all_visible TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '是否全部可见',
    receiver_id BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '接收者ID',
    timing      TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '定时发布',
    send_time   DATETIME COMMENT '发送时间',
    visible     TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '是否可见',
    created_at  DATETIME COMMENT '创建时间',
    updated_at  DATETIME COMMENT '更新时间',
    deleted     TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '删除标识',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='消息表';

-- sys_dictionary
DROP TABLE IF EXISTS sys_dictionary;
CREATE TABLE sys_dictionary
(
    id         BIGINT(20) UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '主键ID',
    dict_key   VARCHAR(50)  DEFAULT NULL COMMENT '字典键',
    dict_value VARCHAR(50)  DEFAULT NULL COMMENT '字典值',
    dict_code  INT          DEFAULT NULL COMMENT '字典码',
    parent_id  BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '父ID',
    remark     VARCHAR(500) DEFAULT NULL COMMENT '备注',
    created_at DATETIME COMMENT '创建时间',
    updated_at DATETIME COMMENT '更新时间',
    deleted    TINYINT(1) UNSIGNED DEFAULT 0 COMMENT '删除标识',
    PRIMARY KEY (ID),
    UNIQUE KEY IDX_SYS_DICTIONARY_DICT_KEY_DICT_VALUE_DICT_CODE (`DICT_KEY`, `DICT_VALUE`, `DICT_CODE`)
)