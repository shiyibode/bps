--贷款客户(其实是账号)与员工绑定表
DROP TABLE IF EXISTS bps_78000.t_dktj_employee_customer;
CREATE TABLE bps_78000.t_dktj_employee_customer(
     id BIGSERIAL ,
     xd_customer_no VARCHAR(36),
     hx_customer_no VARCHAR(10),
     org_code varchar(10),
     account_no varchar(22),
     account_open_date date,
     customer_name varchar(256),
     customer_type VARCHAR(1),
     identity_type varchar(20),
     identity_no varchar(80),
     teller_code VARCHAR(10),
     start_date Date,
     end_date Date,
     valid_flag BOOLEAN NOT NULL ,
     register_type VARCHAR(1),
     register_check_status VARCHAR(1),
     alter_check_status varchar(1),
     op_teller_code VARCHAR(10),
     create_time TIMESTAMP,
     register_check_teller_code varchar(10),
     register_check_time timestamptz,
     alter_check_teller_code VARCHAR(10),
     alter_check_time TIMESTAMP,
     parent_id BIGINT,
     bound_type varchar(1),
     remarks VARCHAR(128)
);

ALTER TABLE bps_78000.t_dktj_employee_customer ADD PRIMARY KEY (xd_customer_no,account_no,org_code,teller_code,start_date,valid_flag);
-- ALTER TABLE t_dktj_employee_customer ADD CONSTRAINT uk_t_dktj_employee_customer_xd_customer_no_valid_flag UNIQUE (xd_customer_no,org_code,teller_code,valid_flag);
COMMENT ON TABLE bps_78000.t_dktj_employee_customer IS '贷款客户与员工绑定表';
--t_dktj_employee_customer的触发器配置（保证一个客户在一个网点只有一个生效的维护人）
DROP FUNCTION IF EXISTS dktj_fun_employee_customer_before_insert() CASCADE ;
CREATE OR REPLACE FUNCTION dktj_fun_employee_customer_before_insert()
    RETURNS TRIGGER AS $$
DECLARE
    total integer;
BEGIN
    IF NEW.valid_flag = TRUE THEN
        SELECT count(*) FROM bps_78000.t_dktj_employee_customer WHERE account_no = NEW.account_no AND valid_flag = TRUE AND org_code = NEW.org_code INTO total;
        IF total > 0 THEN
            RAISE EXCEPTION '账号: % 已存在生效状态，违反唯一原则，禁止写入',NEW.account_no;
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER dktj_before_employee_customer_insert_trigger BEFORE INSERT ON bps_78000.t_dktj_employee_customer FOR EACH ROW EXECUTE PROCEDURE dktj_fun_employee_customer_before_insert();

COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.xd_customer_no IS '信贷系统客户号';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.hx_customer_no IS '核心系统客户号';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.org_code IS '机构号';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.account_no IS '贷款账号';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.account_open_date IS '贷款账号开户时间';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.customer_name IS '客户名称';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.customer_type IS '客户类型：1-个人 2-对公';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.identity_type IS '证件类型';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.identity_no IS '证件号';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.teller_code IS '所属柜员的柜员号';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.start_date IS '起始日期';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.end_date IS '终止日期';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.valid_flag IS '有效标志，false表示已经转移给其他人';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.register_type IS '登记类型: 1-新客户登记; 2-释放客户登记 3-变更维护人'; --可以与t_dktj_unbound_customer中的flag复用
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.register_check_status IS '登记审核状态：0-未审核; 1-已审核';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.alter_check_status IS '变更审核状态：0-未审核; 1-已审核';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.op_teller_code IS '操作柜员';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.create_time IS '生成时间';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.register_check_teller_code IS '登记复核柜员';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.alter_check_teller_code IS '变更复核柜员';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.register_check_time IS '登记复核时间';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.alter_check_time IS '变更复核时间';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.parent_id IS '上级id，变更维护人时产生';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.bound_type IS '绑定类型：1-自动绑定  2-手工绑定';
COMMENT ON COLUMN bps_78000.t_dktj_employee_customer.remarks IS '备注';

CREATE INDEX ON bps_78000.t_dktj_employee_customer(xd_customer_no,account_no,teller_code);


--未绑定到员工的客户列表
DROP TABLE IF EXISTS bps_78000.t_dktj_unbound_customer;
CREATE TABLE bps_78000.t_dktj_unbound_customer(
    id BIGSERIAL,
    xd_customer_no VARCHAR(36) ,
    account_no varchar(22),
    account_open_date date,
    customer_name VARCHAR(256),
    start_date Date,
    org_code VARCHAR(10),
    identity_type VARCHAR(20),
    identity_no VARCHAR(80),
    customer_type VARCHAR(1),
    date date,
    create_time TIMESTAMP DEFAULT now(),
    flag VARCHAR(1)
);

ALTER TABLE bps_78000.t_dktj_unbound_customer ADD PRIMARY KEY (xd_customer_no,account_no);

COMMENT ON COLUMN bps_78000.t_dktj_unbound_customer.xd_customer_no IS '信贷系统客户号';
COMMENT ON COLUMN bps_78000.t_dktj_unbound_customer.account_no IS '贷款账号';
COMMENT ON COLUMN bps_78000.t_dktj_unbound_customer.account_open_date IS '贷款账号开户日期';
COMMENT ON COLUMN bps_78000.t_dktj_unbound_customer.customer_name IS '客户名称';
COMMENT ON COLUMN bps_78000.t_dktj_unbound_customer.start_date IS '起始时间，柜员调动释放的客户需在此处记录起始时间';
COMMENT ON COLUMN bps_78000.t_dktj_unbound_customer.org_code IS '核心系统机构号';
COMMENT ON COLUMN bps_78000.t_dktj_unbound_customer.identity_type IS '证件类型';
COMMENT ON COLUMN bps_78000.t_dktj_unbound_customer.identity_no IS '证件号码';
COMMENT ON COLUMN bps_78000.t_dktj_unbound_customer.customer_type IS '客户类型：1-个人 2-对公';
COMMENT ON COLUMN bps_78000.t_dktj_unbound_customer.date IS '哪一个业务日期生成的未绑定客户';
COMMENT ON COLUMN bps_78000.t_dktj_unbound_customer.flag IS '标志：1-跑批产生的新客户  2-柜员调动释放的固定客户';


--贷款跑批配置表
DROP TABLE IF EXISTS bps_78000.t_dktj_loan_handle_config;
CREATE TABLE bps_78000.t_dktj_loan_handle_config(
      id BIGSERIAL PRIMARY KEY NOT NULL,
      type VARCHAR(1),
      org_code VARCHAR(10),
      ln_curr_date Date,
      ln_next_date Date,
      update_by BIGINT,
      update_time TIMESTAMP
);

COMMENT ON COLUMN bps_78000.t_dktj_loan_handle_config.id IS 'id主键';
COMMENT ON COLUMN bps_78000.t_dktj_loan_handle_config.type IS '类型：0-生成未绑定;柜员调动释放客户;自动绑定已在该网点有贷款账号的服务 1-贷款跑批  2-柜员调动服务跑批  3-客户形态转变服务 4-生成未绑定客户列表服务 5-自动绑定贷款账号到员工服务 6-贷款利息收入跑批服务  7-贷款利息按分成模板分到人';
COMMENT ON COLUMN bps_78000.t_dktj_loan_handle_config.org_code IS '机构号';
COMMENT ON COLUMN bps_78000.t_dktj_loan_handle_config.ln_curr_date IS '当前日期';
COMMENT ON COLUMN bps_78000.t_dktj_loan_handle_config.ln_next_date IS '下一个日期';
COMMENT ON COLUMN bps_78000.t_dktj_loan_handle_config.update_by IS '更新者';
COMMENT ON COLUMN bps_78000.t_dktj_loan_handle_config.update_time IS '更新时间';


--员工贷款余额表
DROP TABLE IF EXISTS bps_78000.t_dktj_employee_loan_detail;
CREATE TABLE bps_78000.t_dktj_employee_loan_detail(
    id BIGSERIAL PRIMARY KEY NOT NULL ,
    date Date,
    teller_code VARCHAR(10),
    teller_org_code VARCHAR(10),
    four_class_flag VARCHAR(4),
    five_class_flag VARCHAR(4),
    customer_type VARCHAR(1),
    balance NUMERIC(16,2),
    ttl_received_int NUMERIC(16,2),
    day_received_int NUMERIC(16,2),
    ln_org_code VARCHAR(10),
    belong_org_code VARCHAR(10),
    create_time TIMESTAMP,
    update_time TIMESTAMP
);

COMMENT ON COLUMN bps_78000.t_dktj_employee_loan_detail.id IS 'id主键';
COMMENT ON COLUMN bps_78000.t_dktj_employee_loan_detail.date IS '日期';
COMMENT ON COLUMN bps_78000.t_dktj_employee_loan_detail.teller_code IS '所属柜员';
COMMENT ON COLUMN bps_78000.t_dktj_employee_loan_detail.four_class_flag IS '四级分类标志';
COMMENT ON COLUMN bps_78000.t_dktj_employee_loan_detail.five_class_flag IS '五级分类标志';
COMMENT ON COLUMN bps_78000.t_dktj_employee_loan_detail.customer_type IS '客户类型：1-个人 2-对公';
COMMENT ON COLUMN bps_78000.t_dktj_employee_loan_detail.balance IS '金额';
COMMENT ON COLUMN bps_78000.t_dktj_employee_loan_detail.ttl_received_int IS '累计收回利息';
COMMENT ON COLUMN bps_78000.t_dktj_employee_loan_detail.day_received_int IS '当日收回利息';
COMMENT ON COLUMN bps_78000.t_dktj_employee_loan_detail.ln_org_code IS '贷款所在网点';
COMMENT ON COLUMN bps_78000.t_dktj_employee_loan_detail.belong_org_code IS '所属网点';
COMMENT ON COLUMN bps_78000.t_dktj_employee_loan_detail.create_time IS '创建时间';
COMMENT ON COLUMN bps_78000.t_dktj_employee_loan_detail.update_time IS '更新时间';

CREATE INDEX ON bps_78000.t_dktj_employee_loan_detail(date);
CREATE INDEX ON bps_78000.t_dktj_employee_loan_detail(ln_org_code);
CREATE INDEX ON bps_78000.t_dktj_employee_loan_detail(belong_org_code);
CREATE INDEX ON bps_78000.t_dktj_employee_loan_detail(teller_code);
CREATE INDEX ON bps_78000.t_dktj_employee_loan_detail(five_class_flag);

ALTER TABLE bps_78000.t_dktj_employee_loan_detail ADD CONSTRAINT uk_t_dktj_employee_loan_detail UNIQUE (date,teller_org_code,teller_code,four_class_flag,five_class_flag,ln_org_code,belong_org_code);


--机构贷款余额表
DROP TABLE IF EXISTS bps_78000.t_dktj_organization_loan_detail;
CREATE TABLE bps_78000.t_dktj_organization_loan_detail(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    date Date,
    four_class_flag VARCHAR(4),
    five_class_flag VARCHAR(4),
    customer_type VARCHAR(1),
    balance NUMERIC(16,2),
    ttl_received_int NUMERIC(16,2),
    day_received_int NUMERIC(16,2),
    ln_org_code VARCHAR(10),
    belong_org_code VARCHAR(10),
    create_time TIMESTAMP,
    update_time TIMESTAMP
);

COMMENT ON COLUMN bps_78000.t_dktj_organization_loan_detail.id IS 'id主键';
COMMENT ON COLUMN bps_78000.t_dktj_organization_loan_detail.date IS '日期';
COMMENT ON COLUMN bps_78000.t_dktj_organization_loan_detail.four_class_flag IS '四级分类标志';
COMMENT ON COLUMN bps_78000.t_dktj_organization_loan_detail.five_class_flag IS '五级分类标志';
COMMENT ON COLUMN bps_78000.t_dktj_organization_loan_detail.customer_type IS '客户类型：1-个人 2-对公';
COMMENT ON COLUMN bps_78000.t_dktj_organization_loan_detail.balance IS '金额';
COMMENT ON COLUMN bps_78000.t_dktj_organization_loan_detail.ttl_received_int IS '累计收回利息';
COMMENT ON COLUMN bps_78000.t_dktj_organization_loan_detail.day_received_int IS '当日收回利息';
COMMENT ON COLUMN bps_78000.t_dktj_organization_loan_detail.ln_org_code IS '贷款所在机构';
COMMENT ON COLUMN bps_78000.t_dktj_organization_loan_detail.belong_org_code IS '贷款所属机构';
COMMENT ON COLUMN bps_78000.t_dktj_organization_loan_detail.create_time IS '创建时间';
COMMENT ON COLUMN bps_78000.t_dktj_organization_loan_detail.update_time IS '更新时间';

CREATE INDEX ON bps_78000.t_dktj_organization_loan_detail(date);
CREATE INDEX ON bps_78000.t_dktj_organization_loan_detail(five_class_flag);
CREATE INDEX ON bps_78000.t_dktj_organization_loan_detail(ln_org_code);
CREATE INDEX ON bps_78000.t_dktj_organization_loan_detail(belong_org_code);

ALTER TABLE bps_78000.t_dktj_organization_loan_detail ADD CONSTRAINT uk_t_dktj_organization_loan_detail UNIQUE (date,four_class_flag,five_class_flag,ln_org_code,belong_org_code);



--贷款利息明细表
DROP TABLE IF EXISTS bps_78000.t_dktj_account_interest;
CREATE TABLE bps_78000.t_dktj_account_interest (
     id BIGSERIAL PRIMARY KEY NOT NULL,
     date DATE,
     orig_tx_no INTEGER,
     child_tx_no INTEGER,
     subject_no varchar(10),
     orig_tx_code VARCHAR(10),
     summary_code VARCHAR(3),
     summary VARCHAR(40),
     org_code VARCHAR(10),
     account_no VARCHAR(22),
     tax_int NUMERIC(14, 2),
     tx_org_code VARCHAR(10),
     tx_teller VARCHAR(10),
     create_time TIMESTAMPTZ,
     acct_flag  VARCHAR(1),
     reg_teller VARCHAR(10),
     reg_time TIMESTAMPTZ
);
-- t_dktj_account_interest 表注释
COMMENT ON TABLE bps_78000.t_dktj_account_interest IS '存款账户付息明细表';
-- t_dktj_account_interest 表字段注释
COMMENT ON COLUMN bps_78000.t_dktj_account_interest.id IS 'ID主键';
COMMENT ON COLUMN bps_78000.t_dktj_account_interest.date IS '交易日期';
COMMENT ON COLUMN bps_78000.t_dktj_account_interest.orig_tx_no IS '原交易流水号';
COMMENT ON COLUMN bps_78000.t_dktj_account_interest.child_tx_no IS '子交易流水号';
COMMENT ON COLUMN bps_78000.t_dktj_account_interest.orig_tx_code IS '原交易码';
COMMENT ON COLUMN bps_78000.t_dktj_account_interest.summary_code IS '摘要码';
COMMENT ON COLUMN bps_78000.t_dktj_account_interest.summary IS '摘要';
COMMENT ON COLUMN bps_78000.t_dktj_account_interest.org_code IS '开户机构';
COMMENT ON COLUMN bps_78000.t_dktj_account_interest.account_no IS '账号';
COMMENT ON COLUMN bps_78000.t_dktj_account_interest.tax_int IS '应税利息';
COMMENT ON COLUMN bps_78000.t_dktj_account_interest.tx_org_code IS '交易机构';
COMMENT ON COLUMN bps_78000.t_dktj_account_interest.tx_teller IS '交易柜员';
COMMENT ON COLUMN bps_78000.t_dktj_account_interest.create_time IS '记录生成时间';
COMMENT ON COLUMN bps_78000.t_dktj_account_interest.acct_flag IS '账号来源:1-系统匹配;2-柜员补登记';
COMMENT ON COLUMN bps_78000.t_dktj_account_interest.reg_teller IS '补登记账号柜员';
COMMENT ON COLUMN bps_78000.t_dktj_account_interest.reg_time IS '补登记账号时间';
-- t_dktj_account_interest 表索引
CREATE INDEX ON bps_78000.t_dktj_account_interest(date);
CREATE INDEX ON bps_78000.t_dktj_account_interest(account_no);
CREATE INDEX ON bps_78000.t_dktj_account_interest(subject_no);



--客户状态变化表
DROP TABLE IF EXISTS bps_78000.t_dktj_customer_status;
CREATE TABLE bps_78000.t_dktj_customer_status(
   id BIGSERIAL PRIMARY KEY,
   org_code varchar(10),
   xd_customer_no VARCHAR(36),
   status VARCHAR(2),
   start_date Date,
   end_date Date,
   parent_id BIGINT,
   valid_flag BOOLEAN,
   check_status VARCHAR(1),
   register_account_no varchar(22),
   create_by BIGINT,
   create_time TIMESTAMP
);

COMMENT ON COLUMN bps_78000.t_dktj_customer_status.id IS '主键';
COMMENT ON COLUMN bps_78000.t_dktj_customer_status.org_code IS '机构号';
COMMENT ON COLUMN bps_78000.t_dktj_customer_status.xd_customer_no IS '信贷客户号';
COMMENT ON COLUMN bps_78000.t_dktj_customer_status.status IS '固定/流动状态：1-固定 2-流动';
COMMENT ON COLUMN bps_78000.t_dktj_customer_status.start_date IS '状态转变的起始日期';
COMMENT ON COLUMN bps_78000.t_dktj_customer_status.end_date IS '状态转变的终止日期';
COMMENT ON COLUMN bps_78000.t_dktj_customer_status.parent_id IS '上级id，状态转变时产生';
COMMENT ON COLUMN bps_78000.t_dktj_customer_status.valid_flag IS '有效标志';
COMMENT ON COLUMN bps_78000.t_dktj_customer_status.check_status IS '复核标志: 0-未复核 1-已复核';
COMMENT ON COLUMN bps_78000.t_dktj_customer_status.register_account_no IS '登记此贷款账号时确定了该贷款客户的固定流动状态';
COMMENT ON COLUMN bps_78000.t_dktj_customer_status.create_by IS '创建人';
COMMENT ON COLUMN bps_78000.t_dktj_customer_status.create_time IS '创建时间';

CREATE INDEX ON bps_78000.t_dktj_customer_status(xd_customer_no);

-- ALTER TABLE t_dktj_customer_status ADD CONSTRAINT uk_t_dktj_customer_status_xd_customer_no_start_date UNIQUE (org_code,xd_customer_no,start_date);
-- ALTER TABLE t_dktj_customer_status DROP CONSTRAINT uk_t_dktj_customer_status_xd_customer_no_start_date;
-- 这里这么设置是不准确的，这样设置将不允许变更固定/流动状态。准确的表述应该是：valid_flag=true时，org_code、xd_customer_no应该是唯一的，即valid_flag=true的只能有一个
-- 解决上述问题的方法是新建触发器:insert update
-- 20210427日解决上述问题，新增一个start_date字段不就可以了嘛
ALTER TABLE bps_78000.t_dktj_customer_status ADD CONSTRAINT uk_t_dktj_customer_status_xd_customer_no_valid_flag UNIQUE (org_code,xd_customer_no,start_date,valid_flag);






-- 贷款客户属性表：1-潜在客户 2-存量客户 3-过渡客户 4-逾期客户 5-流失客户
DROP TABLE IF EXISTS bps_78000.t_dktj_customer_property;
CREATE TABLE bps_78000.t_dktj_customer_property(
     id BIGSERIAL PRIMARY KEY,
     date Date,
     xd_customer_no VARCHAR(36),
     property VARCHAR(1),
     remarks VARCHAR(128),
     create_time TIMESTAMP
);

COMMENT ON COLUMN bps_78000.t_dktj_customer_property.id IS '主键';
COMMENT ON COLUMN bps_78000.t_dktj_customer_property.date IS '日期';
COMMENT ON COLUMN bps_78000.t_dktj_customer_property.xd_customer_no IS '信贷系统客户号';
COMMENT ON COLUMN bps_78000.t_dktj_customer_property.property IS '属性：1-潜在客户 2-存量客户 3-过渡客户 4-逾期客户 5-流失客户';
COMMENT ON COLUMN bps_78000.t_dktj_customer_property.remarks IS '备注';
COMMENT ON COLUMN bps_78000.t_dktj_customer_property.create_time IS '生成记录时间';

CREATE INDEX ON bps_78000.t_dktj_customer_property(date);
CREATE INDEX ON bps_78000.t_dktj_customer_property(xd_customer_no);
CREATE INDEX ON bps_78000.t_dktj_customer_property(property);



-- 员工所属机构中间表，按日生成员工所在的机构
DROP TABLE IF EXISTS bps_78000.t_middle_table_sys_user_organization;
CREATE TABLE bps_78000.t_middle_table_sys_user_organization(
     id BIGSERIAL,
     teller_code varchar(10),
     teller_name varchar(64),
     user_id bigint,
     organization_id bigint,
     before_day_org_code varchar(10),
     org_code varchar(10),
     is_new BOOLEAN DEFAULT FALSE,
     status varchar(1),
     start_date date,
     date date,
     import_time timestamptz
);
ALTER TABLE bps_78000.t_middle_table_sys_user_organization ADD PRIMARY KEY (user_id,organization_id,status,date);
COMMENT ON COLUMN bps_78000.t_middle_table_sys_user_organization.teller_code IS '柜员号';
COMMENT ON COLUMN bps_78000.t_middle_table_sys_user_organization.teller_name IS '柜员名称';
COMMENT ON COLUMN bps_78000.t_middle_table_sys_user_organization.user_id IS '员工id';
COMMENT ON COLUMN bps_78000.t_middle_table_sys_user_organization.organization_id IS '机构id';
COMMENT ON COLUMN bps_78000.t_middle_table_sys_user_organization.before_day_org_code IS '上一日的机构号';
COMMENT ON COLUMN bps_78000.t_middle_table_sys_user_organization.org_code IS '机构号';
COMMENT ON COLUMN bps_78000.t_middle_table_sys_user_organization.is_new IS '是否当日新增的用户';
COMMENT ON COLUMN bps_78000.t_middle_table_sys_user_organization.status IS '状态：0-调离 1-在职 2-停职 3-辞职 4-辞退 5-退休 6-揽储 7-休假';
COMMENT ON COLUMN bps_78000.t_middle_table_sys_user_organization.start_date IS '转入当前机构的起始日期';
COMMENT ON COLUMN bps_78000.t_middle_table_sys_user_organization.date IS '日期';
COMMENT ON COLUMN bps_78000.t_middle_table_sys_user_organization.import_time IS '数据导入时间';

-- INSERT INTO bps_78000.t_middle_table_handle_config(id, data_desc, orig_table, table_name, fun_name, handle_seq, handle_mode, import_date, next_date) VALUES (2, '根据员工与机构对照表，加工出员工每日的在职机构', 't_sys_user_organization', 't_middle_table_sys_user_organization', 'fun_middle_generate_sys_user_organization()', 2, null, null, '2019-12-31');



-- 贷款手工登记利息表
DROP TABLE IF EXISTS bps_78000.t_dktj_ignore_account_interest;
CREATE TABLE bps_78000.t_dktj_ignore_account_interest(
   id bigserial PRIMARY KEY ,
   date Date,
   orig_tx_no integer,
   child_tx_no integer,
   orig_tx_code varchar(10),
   summary_code varchar(3),
   summary varchar(40),
   org_code varchar(10),
   tax_int numeric(14,2),
   tx_org_code varchar(10),
   tx_teller varchar(10),
   create_time timestamptz,
   create_teller_code varchar(64)
);


--绑定模板表
DROP TABLE IF EXISTS bps_78000.t_dktj_template;
CREATE TABLE bps_78000.t_dktj_template(
    id BIGSERIAL PRIMARY KEY ,
    name VARCHAR(256),
    sort INT UNIQUE NOT NULL ,
    valid_flag BOOLEAN,
    invalid_date DATE,
    create_time TIMESTAMPTZ,
    create_by BIGINT
);
COMMENT ON COLUMN bps_78000.t_dktj_template.id IS '主键id';
COMMENT ON COLUMN bps_78000.t_dktj_template.name IS '模板名称';
COMMENT ON COLUMN bps_78000.t_dktj_template.sort IS '排序，序号最小的即为默认给出的模板';
COMMENT ON COLUMN bps_78000.t_dktj_template.valid_flag IS '有效标志，false-已停用 true-正常使用，停用以后不得再次启用';
COMMENT ON COLUMN bps_78000.t_dktj_template.invalid_date IS '停用时间';



-- 岗位责任列表
DROP TABLE IF EXISTS bps_78000.t_dktj_position;
CREATE TABLE bps_78000.t_dktj_position(
    id BIGSERIAL PRIMARY KEY ,
    name VARCHAR(256) UNIQUE NOT NULL ,
    en_name VARCHAR(128) UNIQUE,
    type varchar(1) DEFAULT 0 NOT NULL ,
    changeable BOOLEAN DEFAULT FALSE NOT NULL ,
    create_time TIMESTAMPTZ,
    create_by BIGINT
);
COMMENT ON COLUMN bps_78000.t_dktj_position.id IS 'id主键';
COMMENT ON COLUMN bps_78000.t_dktj_position.name IS '中文名称（唯一）';
COMMENT ON COLUMN bps_78000.t_dktj_position.en_name IS '英文名称（唯一）';
COMMENT ON COLUMN bps_78000.t_dktj_position.type IS '岗位类型: 0-普通 1-推荐人 2-贷审会';
COMMENT ON COLUMN bps_78000.t_dktj_position.changeable IS '岗位责任人是否可以改变，例如引荐人和贷后维护人可以改变';


-- 模板岗位明细表
DROP TABLE IF EXISTS bps_78000.t_dktj_template_detail;
CREATE TABLE bps_78000.t_dktj_template_detail(
    id BIGSERIAL,
    template_id BIGINT,
    position_id BIGINT,
    percentage NUMERIC(5,4) NOT NULL ,
    remarks VARCHAR(256),
    create_time TIMESTAMPTZ,
    create_by BIGINT
);
ALTER TABLE bps_78000.t_dktj_template_detail ADD PRIMARY KEY (template_id, position_id);
COMMENT ON COLUMN bps_78000.t_dktj_template_detail.id IS 'id';
COMMENT ON COLUMN bps_78000.t_dktj_template_detail.template_id IS '模板id';
COMMENT ON COLUMN bps_78000.t_dktj_template_detail.position_id IS '岗位id';
COMMENT ON COLUMN bps_78000.t_dktj_template_detail.percentage IS '岗位分成比例，同一个模板下的所有岗位的分成比例之和为100%';
COMMENT ON COLUMN bps_78000.t_dktj_template_detail.remarks IS '备注';
COMMENT ON COLUMN bps_78000.t_dktj_template_detail.create_time IS '创建时间';
COMMENT ON COLUMN bps_78000.t_dktj_template_detail.create_by IS '创建人';




-- 账号-模板对照表
DROP TABLE IF EXISTS bps_78000.t_dktj_account_template;
CREATE TABLE bps_78000.t_dktj_account_template(
    id BIGSERIAL,
    account_no VARCHAR(22),
    template_id BIGINT,
    org_code VARCHAR(10),
    start_date DATE NOT NULL ,
    end_date DATE,
    parent_id BIGINT,
    valid_flag BOOLEAN,
    check_status VARCHAR(1),
    remarks VARCHAR(128),
    create_time TIMESTAMPTZ,
    create_by BIGINT,
    update_time TIMESTAMPTZ,
    update_by BIGINT
);
ALTER TABLE bps_78000.t_dktj_account_template ADD PRIMARY KEY (org_code,account_no,template_id,start_date);
CREATE INDEX ON bps_78000.t_dktj_account_template(account_no);
COMMENT ON COLUMN bps_78000.t_dktj_account_template.id IS 'id';
COMMENT ON COLUMN bps_78000.t_dktj_account_template.account_no IS '贷款账号';
COMMENT ON COLUMN bps_78000.t_dktj_account_template.template_id IS '模板id';
COMMENT ON COLUMN bps_78000.t_dktj_account_template.org_code IS '机构号';
COMMENT ON COLUMN bps_78000.t_dktj_account_template.start_date IS '起始日期';
COMMENT ON COLUMN bps_78000.t_dktj_account_template.end_date IS '终止日期';
COMMENT ON COLUMN bps_78000.t_dktj_account_template.parent_id IS '父id';
COMMENT ON COLUMN bps_78000.t_dktj_account_template.valid_flag IS '当前有效标志';
COMMENT ON COLUMN bps_78000.t_dktj_account_template.check_status IS '复核状态: 0-未复核 1-已复核'; --注意：这个check_status仅仅是用来表示账户登记维护人的复核状态。因为账号模板、客户的流动和固定状态都是在账号登记维护人的时候选定的，登记维护人后需要复核才能正式成立。check_status=0表示登记了维护人 check_status=1表示复核通过了登记维护人。所以这个check_status不能用来表示其他的状态。
COMMENT ON COLUMN bps_78000.t_dktj_account_template.remarks IS '备注';
COMMENT ON COLUMN bps_78000.t_dktj_account_template.create_time IS '创建时间';
COMMENT ON COLUMN bps_78000.t_dktj_account_template.create_by IS '创建人';
COMMENT ON COLUMN bps_78000.t_dktj_account_template.update_time IS '修改时间';
COMMENT ON COLUMN bps_78000.t_dktj_account_template.update_by IS '修改人';



-- 账号-员工分成比例表
DROP TABLE IF EXISTS bps_78000.t_dktj_account_share_info;
CREATE TABLE bps_78000.t_dktj_account_share_info(
    id BIGSERIAL,
    account_template_id BIGINT,
    template_detail_id BIGINT,
    teller_code VARCHAR(10),
    start_date DATE NOT NULL ,
    end_date DATE,
    parent_id BIGINT,
    valid_flag BOOLEAN,
    check_status VARCHAR(1),
    remarks VARCHAR(128),
    create_time TIMESTAMPTZ,
    create_by BIGINT,
    alter_check_status VARCHAR(1),
    alter_check_teller VARCHAR(10),
    update_time TIMESTAMPTZ,
    update_by BIGINT
);
ALTER TABLE bps_78000.t_dktj_account_share_info ADD PRIMARY KEY (account_template_id, template_detail_id, teller_code, start_date);
CREATE INDEX ON bps_78000.t_dktj_account_share_info(teller_code);
COMMENT ON COLUMN bps_78000.t_dktj_account_share_info.id IS 'id';
COMMENT ON COLUMN bps_78000.t_dktj_account_share_info.account_template_id IS '账号模板id';
COMMENT ON COLUMN bps_78000.t_dktj_account_share_info.template_detail_id IS '模板明细id，包含岗位与分成比例信息';
COMMENT ON COLUMN bps_78000.t_dktj_account_share_info.teller_code IS '员工编号';
COMMENT ON COLUMN bps_78000.t_dktj_account_share_info.start_date IS '起始日期';
COMMENT ON COLUMN bps_78000.t_dktj_account_share_info.end_date IS '终止日期';
COMMENT ON COLUMN bps_78000.t_dktj_account_share_info.parent_id IS '父id';
COMMENT ON COLUMN bps_78000.t_dktj_account_share_info.valid_flag IS '当前有效标志';
COMMENT ON COLUMN bps_78000.t_dktj_account_share_info.check_status IS '登记复核状态: 0-未复核 1-已复核';--注意：这个check_status仅仅是用来表示账户登记维护人的复核状态。因为账号模板、客户的流动和固定状态都是在账号登记维护人的时候选定的，登记维护人后需要复核才能正式成立。check_status=0表示登记了维护人 check_status=1表示复核通过了登记维护人。所以这个check_status不能用来表示其他的状态。
COMMENT ON COLUMN bps_78000.t_dktj_account_share_info.remarks IS '备注';
COMMENT ON COLUMN bps_78000.t_dktj_account_share_info.create_time IS '创建时间';
COMMENT ON COLUMN bps_78000.t_dktj_account_share_info.create_by IS '创建人';
COMMENT ON COLUMN bps_78000.t_dktj_account_share_info.alter_check_status IS '变更复核状态: 0-未复核  1-已复核';
COMMENT ON COLUMN bps_78000.t_dktj_account_share_info.alter_check_teller IS '变更复核柜员';
COMMENT ON COLUMN bps_78000.t_dktj_account_share_info.update_time IS '更新时间';
COMMENT ON COLUMN bps_78000.t_dktj_account_share_info.update_by IS '更新人';


-- 柜员利息分成结果表
DROP TABLE IF EXISTS bps_78000.t_dktj_employee_interest_share;
CREATE TABLE bps_78000.t_dktj_employee_interest_share(
    id BIGSERIAL PRIMARY KEY ,
    account_no VARCHAR(22) NOT NULL,
    xd_customer_no VARCHAR(36),
    teller_code VARCHAR(10) NOT NULL,
    date DATE NOT NULL,
    balance NUMERIC(17,2) NOT NULL ,
    ln_org_code VARCHAR(10) NOT NULL,
    belong_org_code VARCHAR(10) NOT NULL,
    account_template_id BIGINT NOT NULL,
    template_id BIGINT NOT NULL,
    template_detail_id BIGINT NOT NULL,
    position_id BIGINT NOT NULL,
    percentage NUMERIC(5,4) NOT NULL,
    create_time TIMESTAMPTZ NOT NULL
);
CREATE UNIQUE INDEX ON bps_78000.t_dktj_employee_interest_share(account_no,ln_org_code,teller_code,date,position_id);

COMMENT ON COLUMN bps_78000.t_dktj_employee_interest_share.account_no IS '贷款账号';
COMMENT ON COLUMN bps_78000.t_dktj_employee_interest_share.xd_customer_no IS '信贷客户号';
COMMENT ON COLUMN bps_78000.t_dktj_employee_interest_share.teller_code IS '归属柜员';
COMMENT ON COLUMN bps_78000.t_dktj_employee_interest_share.date IS '日期';
COMMENT ON COLUMN bps_78000.t_dktj_employee_interest_share.balance IS '总利息上归属该柜员的利息';
COMMENT ON COLUMN bps_78000.t_dktj_employee_interest_share.ln_org_code IS '贷款账号所在机构';
COMMENT ON COLUMN bps_78000.t_dktj_employee_interest_share.belong_org_code IS '柜员所在机构';
COMMENT ON COLUMN bps_78000.t_dktj_employee_interest_share.account_template_id IS '账号模板id';
COMMENT ON COLUMN bps_78000.t_dktj_employee_interest_share.template_id IS '模板id';
COMMENT ON COLUMN bps_78000.t_dktj_employee_interest_share.template_detail_id IS '模板与岗位对应id';
COMMENT ON COLUMN bps_78000.t_dktj_employee_interest_share.position_id IS '岗位id';
COMMENT ON COLUMN bps_78000.t_dktj_employee_interest_share.percentage IS '本次分成的比例';
COMMENT ON COLUMN bps_78000.t_dktj_employee_interest_share.create_time IS '生成时间';




-- 待补录分成规则的账户信息 (1:用于历史数据补录-上线时  2:用于核销贷款的收回处理-平时)
DROP TABLE IF EXISTS bps_78000.t_dktj_unbound_employee_interest;
CREATE TABLE bps_78000.t_dktj_unbound_employee_interest(
   id BIGSERIAL PRIMARY KEY ,
   account_no VARCHAR(22) NOT NULL,
   xd_customer_no VARCHAR(36),
   account_open_date DATE,
   customer_name VARCHAR(256),
   org_code VARCHAR(10),
   identity_type VARCHAR(20),
   identity_no VARCHAR(80),
   customer_type VARCHAR(1),
   del_flag BOOLEAN DEFAULT FALSE NOT NULL ,
   create_time TIMESTAMPTZ DEFAULT now() NOT NULL
);
CREATE UNIQUE INDEX ON bps_78000.t_dktj_unbound_employee_interest(account_no,org_code);

COMMENT ON COLUMN bps_78000.t_dktj_unbound_employee_interest.account_no IS '贷款账号';
COMMENT ON COLUMN bps_78000.t_dktj_unbound_employee_interest.xd_customer_no IS '贷款账号';
COMMENT ON COLUMN bps_78000.t_dktj_unbound_employee_interest.account_open_date IS '账户开户日期';
COMMENT ON COLUMN bps_78000.t_dktj_unbound_employee_interest.customer_name IS '客户名称';
COMMENT ON COLUMN bps_78000.t_dktj_unbound_employee_interest.org_code IS '机构号';
COMMENT ON COLUMN bps_78000.t_dktj_unbound_employee_interest.identity_type IS '证件类型';
COMMENT ON COLUMN bps_78000.t_dktj_unbound_employee_interest.identity_no IS '证件号码';
COMMENT ON COLUMN bps_78000.t_dktj_unbound_employee_interest.customer_type IS '客户类型: 1-个人  2-对公';
COMMENT ON COLUMN bps_78000.t_dktj_unbound_employee_interest.del_flag IS '删除标志';
COMMENT ON COLUMN bps_78000.t_dktj_unbound_employee_interest.create_time IS '生成时间';


--特殊账户类型
DROP TABLE IF EXISTS bps_78000.t_dktj_special_account_type;
CREATE TABLE bps_78000.t_dktj_special_account_type(
    id bigserial,
    name varchar(128),
    en_name varchar(64),
    valid_flag boolean,
    sort int
);
--特殊账户登记
DROP TABLE IF EXISTS bps_78000.t_dktj_special_account;
CREATE TABLE bps_78000.t_dktj_special_account(
    id bigserial,
    org_code varchar(10),
    account_no varchar(22),
    type_id int,
    start_date date,
    end_date date,
    valid_flag BOOLEAN,
    check_status varchar(1),
    alter_check_status varchar(1),
    create_time timestamptz,
    create_by int
);
CREATE UNIQUE INDEX ON bps_78000.t_dktj_special_account(org_code,account_no,start_date,type_id);
COMMENT ON COLUMN bps_78000.t_dktj_special_account.org_code IS '贷款所在的核心机构';
COMMENT ON COLUMN bps_78000.t_dktj_special_account.account_no IS '账号';
COMMENT ON COLUMN bps_78000.t_dktj_special_account.type_id IS '特殊标记id';
COMMENT ON COLUMN bps_78000.t_dktj_special_account.start_date IS '起始日';
COMMENT ON COLUMN bps_78000.t_dktj_special_account.end_date IS '到期日';
COMMENT ON COLUMN bps_78000.t_dktj_special_account.valid_flag IS '有效标志';
COMMENT ON COLUMN bps_78000.t_dktj_special_account.check_status IS '登记校验标志';
COMMENT ON COLUMN bps_78000.t_dktj_special_account.alter_check_status IS '变更校验标志';
COMMENT ON COLUMN bps_78000.t_dktj_special_account.create_time IS '创建时间';
COMMENT ON COLUMN bps_78000.t_dktj_special_account.create_by IS '创建人';




