DROP TABLE IF EXISTS bps_78000.t_ods_data_handle_config;
create table bps_78000.t_ods_data_handle_config(
  id BIGSERIAL PRIMARY KEY NOT NULL NOT NULL ,
  data_desc varchar(128),
  base_path varchar(128) not null,
  file_name varchar(128) unique not null,
  table_name varchar(128) unique not null,
  date_field_name varchar (64),
  handle_seq int unique not null,
  handle_mode varchar(2) not null,
  batch_size int not null,
  import_date timestamp,
  next_date timestamp not null,
  del_flag boolean default false
);

comment on column bps_78000.t_ods_data_handle_config.handle_mode is '数据导入方式：0-删除原表数据,将新数据全量插入  1-将新数据全量插入';
comment on column bps_78000.t_ods_data_handle_config.date_field_name is '日期字段的名称，如果数据导入模式为1，那么肯定是按日期插入的，需要提供日期字段的名称，一般为date、tx_date';



-- 中间表的配置表
DROP TABLE IF EXISTS bps_78000.t_middle_table_handle_config;
CREATE TABLE bps_78000.t_middle_table_handle_config(
     id BIGSERIAL PRIMARY KEY NOT NULL NOT NULL ,
     data_desc varchar(128),
     orig_table varchar(256) ,
     table_name varchar(128) unique not null,
     fun_name varchar(256),
     handle_seq int unique not null,
     handle_mode varchar(2),
     import_date date,
     next_date date not null,
     del_flag boolean default false
);

COMMENT ON COLUMN bps_78000.t_middle_table_handle_config.id IS 'id';
COMMENT ON COLUMN bps_78000.t_middle_table_handle_config.data_desc IS '中间表描述';
COMMENT ON COLUMN bps_78000.t_middle_table_handle_config.orig_table IS '原始表列表，即由哪些原始表加工出来的该表，表名用逗号分隔';
COMMENT ON COLUMN bps_78000.t_middle_table_handle_config.table_name IS '表名称';
COMMENT ON COLUMN bps_78000.t_middle_table_handle_config.fun_name IS '从原始表加工生成中间表的函数fun名称，可以是多个，用逗号分隔';
COMMENT ON COLUMN bps_78000.t_middle_table_handle_config.handle_seq IS '序列';
COMMENT ON COLUMN bps_78000.t_middle_table_handle_config.handle_mode IS '模式';
COMMENT ON COLUMN bps_78000.t_middle_table_handle_config.import_date IS '已插入数据的日期';
COMMENT ON COLUMN bps_78000.t_middle_table_handle_config.next_date IS '下一个日期';
COMMENT ON COLUMN bps_78000.t_middle_table_handle_config.del_flag IS '删除标志';




drop table if exists bps_78000.t_ods_data_table_field;
create table bps_78000.t_ods_data_table_field(
  id bigserial primary key not null,
  handle_config_id bigint not null,
  original_field_flag varchar(1) default '1',
  table_field_name varchar(64) ,
  table_field_type varchar(64) ,
  default_value varchar(64),
  sort int not null,
  file_position int
);

drop table if exists bps_78000.t_ods_data_deal_log;
create table bps_78000.t_ods_data_deal_log(
  id BIGSERIAL PRIMARY KEY NOT NULL ,
  date date,
  file_name varchar(128),
  table_name varchar(128),
  insert_num bigint,
  import_time timestamptz DEFAULT now()
);

comment on column bps_78000.t_ods_data_table_field.handle_config_id is '该字段所属表的id，即t_ods_data_handle_config表中的id';
comment on column bps_78000.t_ods_data_table_field.original_field_flag is '1-文本文件中的字段,且需要导入到数据库中   2-文本文件中的字段，不需要导入到数据库中    3-非文本文件中的字段，数据日期date   4-非文本字段，插入数据的时间import_time  5-非文本文件，数据来源data_source 6-非文本文件,有默认值';
comment on column bps_78000.t_ods_data_table_field.file_position is '第X个文件字段';
