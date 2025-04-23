
-- 创建bps数据库
create role bps with password 'bps';
create database bps with owner = bps;
alter role "bps" with login;

-- 创建支行模式
CREATE SCHEMA bps_78000;
CREATE USER user_bps_78000 WITH PASSWORD 'user_bps_78000';

GRANT ALL PRIVILEGES ON SCHEMA public TO user_bps_78000;
GRANT INSERT,UPDATE,DELETE,SELECT ON ALL TABLES IN SCHEMA public TO user_bps_78000;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO user_bps_78000;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO user_bps_78000;

GRANT ALL PRIVILEGES ON SCHEMA bps_78000 TO user_bps_78000;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA bps_78000 TO user_bps_78000;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA bps_78000 TO user_bps_78000;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA bps_78000 TO user_bps_78000;

ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON TABLES TO user_bps_78000;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON SEQUENCES TO user_bps_78000;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON FUNCTIONS TO user_bps_78000;

ALTER DEFAULT PRIVILEGES IN SCHEMA bps_78000 GRANT ALL PRIVILEGES ON TABLES TO user_bps_78000;
ALTER DEFAULT PRIVILEGES IN SCHEMA bps_78000 GRANT ALL PRIVILEGES ON SEQUENCES TO user_bps_78000;
ALTER DEFAULT PRIVILEGES IN SCHEMA bps_78000 GRANT ALL PRIVILEGES ON FUNCTIONS TO user_bps_78000;

-- 用户表
DROP TABLE IF EXISTS public.t_sys_user;
CREATE TABLE public.t_sys_user
(
    id BIGSERIAL ,
    code VARCHAR(64) NOT NULL unique ,
    login_password VARCHAR(100) NOT NULL,
    name VARCHAR(100) DEFAULT NULL,
    phone VARCHAR(32),
    mobile VARCHAR(32),
    avatar VARCHAR(256),
    identity_no VARCHAR(20),
    birthday DATE,
    sex VARCHAR(1),
    entry_date DATE,
    post VARCHAR(128),
    login_usable BOOLEAN NOT NULL DEFAULT TRUE,
    remarks VARCHAR(1024) DEFAULT NULL,
    admin_flag boolean,
    create_by INTEGER default NULL,
    create_time TIMESTAMP default current_timestamp,
    update_by INTEGER default NULL,
    update_time TIMESTAMP default current_timestamp,
    login_ip VARCHAR(128),
    login_time TIMESTAMP default current_timestamp,
    del_flag BOOLEAN NOT NULL DEFAULT FALSE,
    primary key (id)
);

COMMENT ON COLUMN public.t_sys_user.id IS '主键';
COMMENT ON COLUMN public.t_sys_user.code IS '代号，可以用作登录名';
COMMENT ON COLUMN public.t_sys_user.login_password IS '登录密码';
COMMENT ON COLUMN public.t_sys_user.name IS '名字';
COMMENT ON COLUMN public.t_sys_user.phone IS '电话';
COMMENT ON COLUMN public.t_sys_user.mobile IS '手机号';
COMMENT ON COLUMN public.t_sys_user.avatar IS '头像';
COMMENT ON COLUMN public.t_sys_user.identity_no IS '证件号码';
COMMENT ON COLUMN public.t_sys_user.birthday IS '生日';
COMMENT ON COLUMN public.t_sys_user.sex IS '性别 1-男 2-女';
COMMENT ON COLUMN public.t_sys_user.entry_date IS '入职日期';
COMMENT ON COLUMN public.t_sys_user.post IS '职位';
COMMENT ON COLUMN public.t_sys_user.login_usable IS '是否允许登录';
COMMENT ON COLUMN public.t_sys_user.remarks IS '备注';
COMMENT ON COLUMN public.t_sys_user.admin_flag IS '超级用户标志';
COMMENT ON COLUMN public.t_sys_user.create_by IS '创建者';
COMMENT ON COLUMN public.t_sys_user.create_time IS '创建时间';
COMMENT ON COLUMN public.t_sys_user.update_by IS '更新者';
COMMENT ON COLUMN public.t_sys_user.update_time IS '更新时间';
COMMENT ON COLUMN public.t_sys_user.login_ip IS '登录ip';
COMMENT ON COLUMN public.t_sys_user.login_time IS '登录时间';
COMMENT ON COLUMN public.t_sys_user.del_flag IS '删除标志';


-- 机构表
DROP TABLE IF EXISTS public.t_sys_organization;
CREATE TABLE public.t_sys_organization
(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    parent_id Integer NOT NULL,
    parent_ids Integer[] NOT NULL,
    code VARCHAR(10),
    name VARCHAR(100) NOT NULL,
    sort VARCHAR(10) NOT NULL,
    icon VARCHAR(100),
    area_id BIGINT DEFAULT NULL,
    type VARCHAR(128) NOT NULL,
    grade VARCHAR(2) NOT NULL DEFAULT '0',
    address VARCHAR(512),
    representative VARCHAR(100),
    phone VARCHAR(128),
    usable BOOLEAN,
    primary_user_id INTEGER,
    deputy_user_id INTEGER,
    remarks VARCHAR(256),
    create_by BIGINT NOT NULL,
    create_time TIMESTAMP NOT NULL DEFAULT now(),
    update_by BIGINT ,
    update_time TIMESTAMP DEFAULT now(),
    del_flag BOOLEAN NOT NULL DEFAULT FALSE
);
create unique index t_sys_organization_code_index on public.t_sys_organization(code);

COMMENT ON COLUMN public.t_sys_organization.id IS '机构id';
COMMENT ON COLUMN public.t_sys_organization.parent_id IS '上级id';
COMMENT ON COLUMN public.t_sys_organization.parent_ids IS '上级机构列表';
COMMENT ON COLUMN public.t_sys_organization.code IS '机构号';
COMMENT ON COLUMN public.t_sys_organization.name IS '名称';
COMMENT ON COLUMN public.t_sys_organization.sort IS '排序';
COMMENT ON COLUMN public.t_sys_organization.icon IS '图标';
COMMENT ON COLUMN public.t_sys_organization.area_id IS '所属区域id';
COMMENT ON COLUMN public.t_sys_organization.type IS '类型：100:总行; 101-总行部门; 200:中心支行; 201:中心支行部门 300:支行; 301:支行部门; 400:网点';
COMMENT ON COLUMN public.t_sys_organization.grade IS '机构等级';
COMMENT ON COLUMN public.t_sys_organization.address IS '地址';
COMMENT ON COLUMN public.t_sys_organization.representative IS '法定代表人';
COMMENT ON COLUMN public.t_sys_organization.phone IS '电话';
COMMENT ON COLUMN public.t_sys_organization.usable IS '是否可用';
COMMENT ON COLUMN public.t_sys_organization.primary_user_id IS '主要负责人id';
COMMENT ON COLUMN public.t_sys_organization.deputy_user_id IS '第二负责人id';
COMMENT ON COLUMN public.t_sys_organization.remarks IS '备注';
COMMENT ON COLUMN public.t_sys_organization.create_by IS '创建人';
COMMENT ON COLUMN public.t_sys_organization.create_time IS '创建时间';
COMMENT ON COLUMN public.t_sys_organization.update_by IS '更新人';
COMMENT ON COLUMN public.t_sys_organization.update_time IS '更新时间';
COMMENT ON COLUMN public.t_sys_organization.del_flag IS '删除标志';


-- 用户-机构 关联表
DROP TABLE IF EXISTS public.t_sys_user_organization;
CREATE TABLE public.t_sys_user_organization
(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    user_id BIGINT NOT NULL,
    organization_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE DEFAULT NULL,
    remarks VARCHAR(1024) DEFAULT NULL,
    valid_flag BOOLEAN NOT NULL,
    parent_id BIGINT,
    create_by INTEGER NOT NULL,
    create_time TIMESTAMP with time zone NOT NULL default now(),
    update_by INTEGER,
    update_time TIMESTAMP with time zone
);
create index t_sys_user_organization_user_id on t_sys_user_organization(user_id);
create index t_sys_user_organization_organization_id on t_sys_user_organization(organization_id);
create unique index on public.t_sys_user_organization(user_id, organization_id, start_date)

COMMENT ON COLUMN public.t_sys_user_organization.id IS 'id';
COMMENT ON COLUMN public.t_sys_user_organization.user_id IS '用户id';
COMMENT ON COLUMN public.t_sys_user_organization.organization_id IS '机构id';
COMMENT ON COLUMN public.t_sys_user_organization.start_date IS '起始日期';
COMMENT ON COLUMN public.t_sys_user_organization.end_date IS '终止日期';
COMMENT ON COLUMN public.t_sys_user_organization.remarks IS '备注';
COMMENT ON COLUMN public.t_sys_user_organization.valid_flag IS '有效标志';
COMMENT ON COLUMN public.t_sys_user_organization.parent_id IS '调动前所在机构';
COMMENT ON COLUMN public.t_sys_user_organization.create_by IS '创建人';
COMMENT ON COLUMN public.t_sys_user_organization.create_time IS '创建时间';
COMMENT ON COLUMN public.t_sys_user_organization.update_by IS '更新人';
COMMENT ON COLUMN public.t_sys_user_organization.update_time IS '更新时间';


--用户状态表
drop table if exists public.t_sys_user_status;
create table public.t_sys_user_status(
  id serial primary key ,
  user_id bigint,
  start_date date,
  end_date date,
  status varchar(128),
  valid_flag BOOLEAN,
  parent_id bigint,
  remarks varchar(256),
  create_by bigint,
  create_time timestamptz,
    update_by bigint,
    update_time timestamptz
);

create unique index on public.t_sys_user_status(user_id,start_date);

COMMENT ON COLUMN public.t_sys_user_status.id IS 'id';
COMMENT ON COLUMN public.t_sys_user_status.user_id IS '用户id';
COMMENT ON COLUMN public.t_sys_user_status.start_date IS '起始日期';
COMMENT ON COLUMN public.t_sys_user_status.end_date IS '终止日期';
COMMENT ON COLUMN public.t_sys_user_status.status IS '状态类型: 内容参照字典表';
COMMENT ON COLUMN public.t_sys_user_status.valid_flag IS '当前有效状态';
COMMENT ON COLUMN public.t_sys_user_status.parent_id IS '父id，变更状态时产生';
COMMENT ON COLUMN public.t_sys_user_status.remarks IS '备注';
COMMENT ON COLUMN public.t_sys_user_status.create_by IS '创建人';
COMMENT ON COLUMN public.t_sys_user_status.create_time IS '创建时间';


-- 系统字典表
DROP TABLE IF EXISTS public.t_sys_dictionary;
CREATE TABLE  public.t_sys_dictionary(
    code varchar(128) primary key ,
    name varchar(128) unique not null ,
    sort integer unique ,
    create_time timestamptz default now()
);

COMMENT ON COLUMN public.t_sys_dictionary.code IS '编号';
COMMENT ON COLUMN public.t_sys_dictionary.name IS '名称';
COMMENT ON COLUMN public.t_sys_dictionary.sort IS '排序';
COMMENT ON COLUMN public.t_sys_dictionary.create_time IS '创建时间';


-- 用户登录和操作日志
DROP TABLE IF EXISTS public.t_sys_log;
CREATE TABLE public.t_sys_log(
    id bigserial primary key ,
    user_id bigint ,
    operation varchar(32) not null ,
    remarks varchar(128),
    create_time timestamptz default now(),
    create_by bigint not null
);

COMMENT ON COLUMN public.t_sys_log.id IS 'id';
COMMENT ON COLUMN public.t_sys_log.user_id IS '用户id';
COMMENT ON COLUMN public.t_sys_log.operation IS '用户操作类型: DL-登录  DC-登出  ';
COMMENT ON COLUMN public.t_sys_log.remarks IS '备注';
COMMENT ON COLUMN public.t_sys_log.create_time IS '创建时间';
COMMENT ON COLUMN public.t_sys_log.create_by IS '创建者';


