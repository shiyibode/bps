-- 菜单表
DROP TABLE IF EXISTS bps_78000.t_sys_menu;
CREATE TABLE bps_78000.t_sys_menu
(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    parent_id BIGINT NOT NULL,
    parent_ids BIGINT[] NOT NULL,
    name VARCHAR(128) NOT NULL,
    en_name varchar(128),
    sort INTEGER NOT NULL,
    type VARCHAR(64),
    uri VARCHAR(1024),
    locale VARCHAR(128),
    target VARCHAR(128),
    icon VARCHAR(128) ,
    is_show VARCHAR(1) NOT NULL,
    permission VARCHAR(256),
    description VARCHAR(256),
    remarks VARCHAR(256),
    create_by BIGINT NOT NULL,
    create_time TIMESTAMP NOT NULL,
    update_by BIGINT NOT NULL,
    update_time TIMESTAMP NOT NULL,
    del_flag BOOLEAN NOT NULL DEFAULT FALSE
);

COMMENT ON COLUMN bps_78000.t_sys_menu.parent_id IS '父id';
COMMENT ON COLUMN bps_78000.t_sys_menu.parent_ids IS '父id列表';
COMMENT ON COLUMN bps_78000.t_sys_menu.name IS '菜单名称';
COMMENT ON COLUMN bps_78000.t_sys_menu.sort IS '排序号';
COMMENT ON COLUMN bps_78000.t_sys_menu.type IS '类型 menu_root-根菜单 menu-group-菜单组 menu-普通菜单 permission-权限或按钮';
COMMENT ON COLUMN bps_78000.t_sys_menu.uri IS 'uri';
COMMENT ON COLUMN bps_78000.t_sys_menu.locale IS '中文名';
COMMENT ON COLUMN bps_78000.t_sys_menu.target IS '目标操作名称,配合前端使用';
COMMENT ON COLUMN bps_78000.t_sys_menu.icon IS '图标';
COMMENT ON COLUMN bps_78000.t_sys_menu.is_show IS '是否显示';
COMMENT ON COLUMN bps_78000.t_sys_menu.permission IS '权限';
COMMENT ON COLUMN bps_78000.t_sys_menu.description IS '描述';
COMMENT ON COLUMN bps_78000.t_sys_menu.remarks IS '备注';
COMMENT ON COLUMN bps_78000.t_sys_menu.create_by IS '创建人';
COMMENT ON COLUMN bps_78000.t_sys_menu.create_time IS '创建时间';
COMMENT ON COLUMN bps_78000.t_sys_menu.update_by IS '更新人';
COMMENT ON COLUMN bps_78000.t_sys_menu.update_time IS '更新时间';
COMMENT ON COLUMN bps_78000.t_sys_menu.del_flag IS '删除标志';



-- 角色表
DROP TABLE IF EXISTS bps_78000.t_sys_role;
CREATE TABLE bps_78000.t_sys_role
(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL,
    en_name VARCHAR(255),
    role_type VARCHAR(64),
    is_sys BOOLEAN DEFAULT NULL,
    usable BOOLEAN NOT NULL DEFAULT TRUE,
    remarks VARCHAR(255),
    create_by BIGINT NOT NULL,
    create_time timestamptz NOT NULL,
    update_by BIGINT ,
    update_time TIMESTAMPTZ,
    del_flag BOOLEAN NOT NULL DEFAULT FALSE
);

COMMENT ON COLUMN bps_78000.t_sys_role.id IS '角色id';
COMMENT ON COLUMN bps_78000.t_sys_role.name IS '名称';
COMMENT ON COLUMN bps_78000.t_sys_role.en_name IS '英文名称';
COMMENT ON COLUMN bps_78000.t_sys_role.role_type IS '角色类型';
COMMENT ON COLUMN bps_78000.t_sys_role.is_sys IS '是否系统数据：此数据只有超级管理员能进行修改; false: 拥有角色修改权限可以进行修改';
COMMENT ON COLUMN bps_78000.t_sys_role.usable IS '是否可用';
COMMENT ON COLUMN bps_78000.t_sys_role.remarks IS '备注';
COMMENT ON COLUMN bps_78000.t_sys_role.create_by IS '创建人';
COMMENT ON COLUMN bps_78000.t_sys_role.create_time IS '创建时间';
COMMENT ON COLUMN bps_78000.t_sys_role.update_by IS '更新人';
COMMENT ON COLUMN bps_78000.t_sys_role.update_time IS '更新时间';
COMMENT ON COLUMN bps_78000.t_sys_role.del_flag IS '删除标志';


-- 角色-菜单表
DROP TABLE if exists bps_78000.t_sys_role_menu;
CREATE TABLE bps_78000.t_sys_role_menu
(
    role_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    is_show BOOLEAN DEFAULT TRUE NOT NULL,
    PRIMARY KEY (role_id, menu_id)
);

COMMENT ON COLUMN bps_78000.t_sys_role_menu.role_id IS '角色id';
COMMENT ON COLUMN bps_78000.t_sys_role_menu.menu_id IS '菜单id';
COMMENT ON COLUMN bps_78000.t_sys_role_menu.is_show IS '显示标志';


-- 用户-角色表
DROP TABLE IF EXISTS bps_78000.t_sys_user_role;
CREATE TABLE bps_78000.t_sys_user_role
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY(user_id, role_id)
);

COMMENT ON COLUMN bps_78000.t_sys_user_role.user_id IS '用户id';
COMMENT ON COLUMN bps_78000.t_sys_user_role.role_id IS '角色id';


-- 接口信息表
DROP TABLE IF EXISTS bps_78000.t_sys_api;
CREATE TABLE bps_78000.t_sys_api(
      id BIGSERIAL PRIMARY KEY NOT NULL ,
      name VARCHAR(128),
      uri VARCHAR(256) unique ,
      permission VARCHAR(256) unique ,
      remarks VARCHAR(256),
      create_by BIGINT,
      create_time timestamptz,
      update_by BIGINT,
      update_time timestamptz
);
COMMENT ON COLUMN bps_78000.t_sys_api.id IS 'id';
COMMENT ON COLUMN bps_78000.t_sys_api.name IS '接口名称';
COMMENT ON COLUMN bps_78000.t_sys_api.uri IS '接口uri路径,例如:/sys/user/save';
COMMENT ON COLUMN bps_78000.t_sys_api.permission IS '接口代表的权限，例如：sys:user:save';
COMMENT ON COLUMN bps_78000.t_sys_api.remarks IS '接口描述';
COMMENT ON COLUMN bps_78000.t_sys_api.create_by IS '创建人';
COMMENT ON COLUMN bps_78000.t_sys_api.create_time IS '创建时间';
COMMENT ON COLUMN bps_78000.t_sys_api.update_by IS '修改人';
COMMENT ON COLUMN bps_78000.t_sys_api.update_time IS '修改时间';


-- 角色-接口映射表
DROP TABLE IF EXISTS bps_78000.t_sys_role_permission;
CREATE TABLE bps_78000.t_sys_role_permission
(
    id BIGSERIAL primary key ,
    role_id BIGINT,
    api_id BIGINT,
    data_scope VARCHAR(128),
    create_time timestamptz,
    create_by BIGINT,
    update_time timestamptz,
    update_by BIGINT
);
create unique index on bps_78000.t_sys_role_permission(role_id,api_id,data_scope);

COMMENT ON COLUMN bps_78000.t_sys_role_permission.role_id IS '角色id';
COMMENT ON COLUMN bps_78000.t_sys_role_permission.api_id IS '接口id';
COMMENT ON COLUMN bps_78000.t_sys_role_permission.data_scope IS '数据范围: 1-所有数据 2-所在机构及以下数据 3-所在机构数据 4-仅本人数据 5-按明细设置 ';


-- 角色-机构表（数据范围为按明细设置时用到该表）
DROP TABLE IF EXISTS bps_78000.t_sys_role_permission_organization;
CREATE TABLE bps_78000.t_sys_role_permission_organization
(
    role_permission_id BIGINT NOT NULL,
    organization_id BIGINT NOT NULL,
    PRIMARY KEY (role_permission_id, organization_id)
);

COMMENT ON COLUMN bps_78000.t_sys_role_permission_organization.role_permission_id IS '角色id';
COMMENT ON COLUMN bps_78000.t_sys_role_permission_organization.organization_id IS '机构id';





