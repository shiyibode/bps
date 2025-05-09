-- 存款分类表
DROP TABLE IF EXISTS bps_78000.t_cktj_deposit_category cascade ;
CREATE TABLE bps_78000.t_cktj_deposit_category (
     id SERIAL PRIMARY KEY NOT NULL,
     parent_id BIGINT NOT NULL,
     parent_ids bigint[],
     no VARCHAR(100),
     name VARCHAR(100),
     deposit_type VARCHAR(128) NOT NULL DEFAULT 'DEPOSIT_CATEGORY_DEPOSIT_TYPE_WU',
     customer_type VARCHAR(128),
     account_type VARCHAR(1),
     belong_to VARCHAR(128),
     subject_no VARCHAR(10)[],
     valid_flag BOOLEAN DEFAULT TRUE,
     remarks VARCHAR(1024),
     sort INTEGER,
     create_by INTEGER,
     create_time TIMESTAMPTZ NOT NULL,
     update_by INTEGER,
     update_time TIMESTAMPTZ NOT NULL
);
-- bps_78000.t_cktj_deposit_category 表注释
COMMENT ON TABLE bps_78000.t_cktj_deposit_category IS '存款分类表';
COMMENT ON SEQUENCE bps_78000.t_cktj_deposit_category_id_seq IS '存款分类表主键序列';
-- bps_78000.t_cktj_deposit_category 表字段注释
COMMENT ON COLUMN bps_78000.t_cktj_deposit_category.id IS '主键';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_category.parent_id IS '上级ID';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_category.parent_ids IS '所有上级ID';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_category.no IS '分类编号';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_category.name IS '分类名称';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_category.deposit_type IS '存款类型: 0-无类型; 1-活期存款; 2-定期存款; 3-通知存款; 4-定活两便';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_category.customer_type IS '对应客户类型: 1-个人; 2-对公';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_category.account_type IS '对应账户类型';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_category.belong_to IS '归属于: 0-员工+机构; 1-员工; 2-机构';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_category.subject_no IS '科目号';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_category.valid_flag IS '有效标志: true-有效; false-无效';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_category.remarks IS '备注';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_category.sort IS '顺序';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_category.create_by IS '创建者';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_category.create_time IS '创建时间';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_category.update_by IS '更新者';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_category.update_time IS '最后更新时间';
-- bps_78000.t_cktj_deposit_category 表索引
CREATE INDEX ON bps_78000.t_cktj_deposit_category USING GIN (subject_no);





--未绑定到员工的存款账户列表
DROP TABLE IF EXISTS bps_78000.t_cktj_unbound_account;
CREATE TABLE bps_78000.t_cktj_unbound_account(
    id BIGSERIAL,
    customer_no VARCHAR(36) ,
    account_no varchar(22),
    child_account_no varchar(10),
    subject_no varchar(10),
    account_type varchar(1),
    card_no varchar(19),
    open_date date,
    customer_name VARCHAR(256),
    org_code VARCHAR(20),
    identity_type VARCHAR(20),
    identity_no VARCHAR(80),
    customer_type VARCHAR(1),
    date date,
    create_time TIMESTAMP DEFAULT now()
);

ALTER TABLE bps_78000.t_cktj_unbound_account ADD PRIMARY KEY (account_no);

COMMENT ON COLUMN bps_78000.t_cktj_unbound_account.customer_no IS '客户号';
COMMENT ON COLUMN bps_78000.t_cktj_unbound_account.account_no IS '存款账号';
COMMENT ON COLUMN bps_78000.t_cktj_unbound_account.child_account_no IS '存款子账号';
COMMENT ON COLUMN bps_78000.t_cktj_unbound_account.subject_no IS '科目号';
COMMENT ON COLUMN bps_78000.t_cktj_unbound_account.account_type IS '内外部账类别 1-普通账户 2-内部账户';
COMMENT ON COLUMN bps_78000.t_cktj_unbound_account.card_no IS '卡号';
COMMENT ON COLUMN bps_78000.t_cktj_unbound_account.open_date IS '账号开户日期';
COMMENT ON COLUMN bps_78000.t_cktj_unbound_account.customer_name IS '客户名称';
COMMENT ON COLUMN bps_78000.t_cktj_unbound_account.org_code IS '核心系统机构号';
COMMENT ON COLUMN bps_78000.t_cktj_unbound_account.identity_type IS '证件类型';
COMMENT ON COLUMN bps_78000.t_cktj_unbound_account.identity_no IS '证件号码';
COMMENT ON COLUMN bps_78000.t_cktj_unbound_account.customer_type IS '客户类型：1-个人 2-对公';
COMMENT ON COLUMN bps_78000.t_cktj_unbound_account.date IS '哪一个业务日期生成的未绑定客户';
CREATE INDEX ON bps_78000.t_cktj_unbound_account(account_no);


--员工存款账户表（任务数）
DROP TABLE IF EXISTS bps_78000.t_cktj_employee_account_task;
CREATE TABLE bps_78000.t_cktj_employee_account_task (
     id BIGSERIAL PRIMARY KEY NOT NULL,
     teller_code VARCHAR(20),
    percentage numeric(7,4),
    main_teller boolean,
     org_code VARCHAR(20),
     account_no VARCHAR(22),
    child_account_no varchar(10),
    subject_no varchar(20),
     card_no VARCHAR(19),
     account_type VARCHAR(1),
     account_open_date Date,
     customer_no VARCHAR(10),
     customer_name VARCHAR(200),
     customer_type varchar(1),
     identity_type VARCHAR(20),
     identity_no VARCHAR(80),
     start_date DATE,
     end_date DATE,
     valid_flag BOOLEAN,
     register_type VARCHAR(128),
     register_check_status VARCHAR(128),
     alter_check_status VARCHAR(128),
     op_teller_code VARCHAR(20),
     register_check_teller_code VARCHAR(20),
     alter_check_teller_code VARCHAR(20),
     register_check_time TIMESTAMPTZ,
     alter_check_time TIMESTAMPTZ,
     parent_ids BIGINT[],
     remarks VARCHAR(256),
     create_time TIMESTAMPTZ,
     create_by BIGINT,
     update_time timestamptz,
     update_by BIGINT
);
-- bps_78000.t_cktj_employee_account_task 表注释
COMMENT ON TABLE bps_78000.t_cktj_employee_account_task IS '员工揽储账户表';
COMMENT ON SEQUENCE bps_78000.t_cktj_employee_account_task_id_seq IS '员工揽储账户表主键序列';
-- bps_78000.t_cktj_employee_account_task 表字段注释
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.id IS '主键';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.teller_code IS '柜员号';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.percentage IS '柜员号';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.main_teller IS '是否主维护人';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.org_code IS '账户机构编号';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.account_no IS '账号';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.child_account_no IS '账号';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.card_no IS '卡号';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.account_type IS '账别: 1-存款账户; 2-内部账户';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.account_open_date IS '账户开户日';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.customer_no IS '客户号';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.customer_name IS '外部户-客户名称 内部户-账户名称';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.customer_type IS '客户类型：1-个人 2-对公';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.identity_no IS '证件号码';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.identity_type IS '证件类型';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.start_date IS '起始日';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.end_date IS '结束日';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.valid_flag IS '有效标志';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.register_type IS '登记类型: 0-新开账户登记揽储人; 1-变更揽揽人';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.register_check_status IS '登记复核状态：0-未复核 1-已复核';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.alter_check_status IS '变更复核状态：0-未复核 1-已复核';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.op_teller_code IS '操作柜员号';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.create_time IS '记录创建时间(登记时间)';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.register_check_teller_code IS '登记复核柜员号';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.alter_check_teller_code IS '变更复核柜员号';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.register_check_time IS '登记复核时间';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.alter_check_time IS '变更复核时间';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.parent_ids IS '上级ID(变更揽储人时产生)';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.remarks IS '备注';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.create_by IS '创建人';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.update_by IS '修改人';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_task.update_time IS '修改时间';
create index ON bps_78000.t_cktj_employee_account_task(account_no);
create index ON bps_78000.t_cktj_employee_account_task(account_no, start_date, end_date);
create unique index on bps_78000.t_cktj_employee_account_task(account_no,start_date,teller_code);


--员工存款账户表（计酬工资数）
DROP TABLE IF EXISTS bps_78000.t_cktj_employee_account_payment;
CREATE TABLE bps_78000.t_cktj_employee_account_payment (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    teller_code VARCHAR(20),
   percentage numeric(7,4),
   main_teller boolean,
    org_code VARCHAR(20),
    account_no VARCHAR(22),
   child_account_no varchar(10),
   subject_no varchar(20),
    card_no VARCHAR(19),
    account_type VARCHAR(1),
    account_open_date Date,
    customer_no VARCHAR(10),
    customer_name VARCHAR(200),
    customer_type varchar(1),
    identity_type VARCHAR(20),
    identity_no VARCHAR(80),
    start_date DATE,
    end_date DATE,
    valid_flag BOOLEAN,
    register_type VARCHAR(128),
    register_check_status VARCHAR(128),
    alter_check_status VARCHAR(128),
    op_teller_code VARCHAR(20),
    register_check_teller_code VARCHAR(20),
    alter_check_teller_code VARCHAR(20),
    register_check_time TIMESTAMPTZ,
    alter_check_time TIMESTAMPTZ,
    parent_ids BIGINT[],
    remarks VARCHAR(256),
    create_time TIMESTAMPTZ,
    create_by BIGINT,
    update_time timestamptz,
    update_by BIGINT
);
-- bps_78000.t_cktj_employee_account_payment 表注释
COMMENT ON TABLE bps_78000.t_cktj_employee_account_payment IS '员工揽储账户表';
COMMENT ON SEQUENCE bps_78000.t_cktj_employee_account_payment_id_seq IS '员工揽储账户表主键序列';
-- bps_78000.t_cktj_employee_account_payment 表字段注释
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.id IS '主键';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.teller_code IS '柜员号';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.percentage IS '柜员号';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.org_code IS '账户机构编号';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.account_no IS '账号';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.child_account_no IS '账号';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.card_no IS '卡号';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.account_type IS '账别: 1-存款账户; 2-内部账户';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.account_open_date IS '账户开户日';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.customer_no IS '客户号';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.customer_name IS '外部户-客户名称 内部户-账户名称';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.customer_type IS '客户类型：1-个人 2-对公';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.identity_no IS '证件号码';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.identity_type IS '证件类型';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.start_date IS '起始日';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.end_date IS '结束日';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.valid_flag IS '有效标志';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.register_type IS '登记类型: 0-新开账户登记揽储人; 1-变更揽揽人';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.register_check_status IS '登记复核状态：0-未复核 1-已复核';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.alter_check_status IS '变更复核状态：0-未复核 1-已复核';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.op_teller_code IS '操作柜员号';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.create_time IS '记录创建时间(登记时间)';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.register_check_teller_code IS '登记复核柜员号';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.alter_check_teller_code IS '变更复核柜员号';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.register_check_time IS '登记复核时间';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.alter_check_time IS '变更复核时间';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.parent_ids IS '上级ID(变更揽储人时产生)';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.remarks IS '备注';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.create_by IS '创建人';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.update_by IS '修改人';
COMMENT ON COLUMN bps_78000.t_cktj_employee_account_payment.update_time IS '修改时间';
create index ON bps_78000.t_cktj_employee_account_payment(account_no);
create index ON bps_78000.t_cktj_employee_account_payment(account_no, start_date, end_date);
create unique index on bps_78000.t_cktj_employee_account_payment(account_no,start_date,teller_code);



-- 员工存款明细表（任务数）
DROP TABLE IF EXISTS bps_78000.t_cktj_employee_deposit_task_detail;
CREATE TABLE bps_78000.t_cktj_employee_deposit_task_detail (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    date DATE,
    teller_code VARCHAR(20),
    teller_org_code VARCHAR(20),
    dp_category_id INTEGER,
    balance NUMERIC(21,2) NOT NULL DEFAULT 0,
    prvisn_int NUMERIC(17,2) NOT NULL DEFAULT 0,
    ttl_pay_int numeric(17,2) NOT NULL DEFAULT 0,
    day_pay_int NUMERIC(17, 2) NOT NULL DEFAULT 0,
    dp_org_code VARCHAR(20),
    belong_org_code VARCHAR(20),
    create_time TIMESTAMPTZ,
    update_time TIMESTAMPTZ
);
-- bps_78000.t_cktj_employee_deposit_detail 表注释
COMMENT ON TABLE bps_78000.t_cktj_employee_deposit_task_detail IS '员工存款明细表';
COMMENT ON SEQUENCE bps_78000.t_cktj_employee_deposit_task_detail_id_seq IS '员工存款明细表主键序列';
-- bps_78000.t_cktj_employee_deposit_detail 表字段注释
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_task_detail.id IS '主键';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_task_detail.date IS '日期';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_task_detail.teller_code IS '柜员号';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_task_detail.teller_org_code IS '柜员所在机构';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_task_detail.dp_category_id IS '存款分类ID';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_task_detail.balance IS '存款余额';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_task_detail.prvisn_int IS '应付利息';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_task_detail.ttl_pay_int IS '累计实付利息';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_task_detail.day_pay_int IS '本日实付利息';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_task_detail.dp_org_code IS '存款所在机构(与核心系统对应)';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_task_detail.belong_org_code IS '存款所属机构(该笔存款属于哪个支行、分理处)';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_task_detail.create_time IS '创建时间';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_task_detail.update_time IS '最后更新时间';
-- bps_78000.t_cktj_employee_deposit_detail 表索引
CREATE UNIQUE INDEX ON bps_78000.t_cktj_employee_deposit_task_detail(date, teller_code, dp_org_code, belong_org_code, dp_category_id);


-- 员工存款明细表
DROP TABLE IF EXISTS bps_78000.t_cktj_employee_deposit_payment_detail;
CREATE TABLE bps_78000.t_cktj_employee_deposit_payment_detail (
       id BIGSERIAL NOT NULL PRIMARY KEY,
       date DATE,
       teller_code VARCHAR(20),
       teller_org_code VARCHAR(20),
       dp_category_id INTEGER,
       balance NUMERIC(21,2) NOT NULL DEFAULT 0,
       prvisn_int NUMERIC(17,2) NOT NULL DEFAULT 0,
       ttl_pay_int numeric(17,2) NOT NULL DEFAULT 0,
       day_pay_int NUMERIC(17, 2) NOT NULL DEFAULT 0,
       dp_org_code VARCHAR(20),
       belong_org_code VARCHAR(20),
       create_time TIMESTAMPTZ,
       update_time TIMESTAMPTZ
);
-- bps_78000.t_cktj_employee_deposit_payment_detail 表注释
COMMENT ON TABLE bps_78000.t_cktj_employee_deposit_payment_detail IS '员工存款明细表';
COMMENT ON SEQUENCE bps_78000.t_cktj_employee_deposit_payment_detail_id_seq IS '员工存款明细表主键序列';
-- bps_78000.t_cktj_employee_deposit_payment_detail 表字段注释
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_payment_detail.id IS '主键';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_payment_detail.date IS '日期';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_payment_detail.teller_code IS '柜员号';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_payment_detail.teller_org_code IS '柜员所在机构';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_payment_detail.dp_category_id IS '存款分类ID';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_payment_detail.balance IS '存款余额';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_payment_detail.prvisn_int IS '应付利息';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_payment_detail.ttl_pay_int IS '累计实付利息';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_payment_detail.day_pay_int IS '本日实付利息';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_payment_detail.dp_org_code IS '存款所在机构(与核心系统对应)';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_payment_detail.belong_org_code IS '存款所属机构(该笔存款属于哪个支行、分理处)';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_payment_detail.create_time IS '创建时间';
COMMENT ON COLUMN bps_78000.t_cktj_employee_deposit_payment_detail.update_time IS '最后更新时间';
-- bps_78000.t_cktj_employee_deposit_payment_detail 表索引
CREATE UNIQUE INDEX ON bps_78000.t_cktj_employee_deposit_payment_detail(date, teller_code, dp_org_code, belong_org_code, dp_category_id);

-- 机构存款明细表-任务数
DROP TABLE IF EXISTS bps_78000.t_cktj_organization_deposit_task_detail;
CREATE TABLE bps_78000.t_cktj_organization_deposit_task_detail (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    date DATE,
    dp_category_id INTEGER,
    balance NUMERIC(21,2) NOT NULL DEFAULT 0,
    prvisn_int NUMERIC(17,2) NOT NULL DEFAULT 0,
    ttl_pay_int numeric(17,2) NOT NULL DEFAULT 0,
    day_pay_int NUMERIC(17, 2) NOT NULL DEFAULT 0,
    dp_org_code VARCHAR(20),
    belong_org_code VARCHAR(20),
    create_time TIMESTAMPTZ,
    update_time TIMESTAMPTZ
);
-- bps_78000.t_cktj_organization_deposit_task_detail 表注释
COMMENT ON TABLE bps_78000.t_cktj_organization_deposit_task_detail IS '机构存款明细表';
COMMENT ON SEQUENCE bps_78000.t_cktj_organization_deposit_task_detail_id_seq IS '机构存款明细表主键序列';
-- bps_78000.t_cktj_organization_deposit_task_detail 表字段注释
COMMENT ON COLUMN bps_78000.t_cktj_organization_deposit_task_detail.id IS '主键';
COMMENT ON COLUMN bps_78000.t_cktj_organization_deposit_task_detail.date IS '日期';
COMMENT ON COLUMN bps_78000.t_cktj_organization_deposit_task_detail.dp_category_id IS '存款分类ID';
COMMENT ON COLUMN bps_78000.t_cktj_organization_deposit_task_detail.balance IS '存款余额';
COMMENT ON COLUMN bps_78000.t_cktj_organization_deposit_task_detail.prvisn_int IS '应付利息';
COMMENT ON COLUMN bps_78000.t_cktj_organization_deposit_task_detail.ttl_pay_int IS '累计实付利息';
COMMENT ON COLUMN bps_78000.t_cktj_organization_deposit_task_detail.day_pay_int IS '本日实付利息';
COMMENT ON COLUMN bps_78000.t_cktj_organization_deposit_task_detail.dp_org_code IS '存款所在机构(与核心系统对应)';
COMMENT ON COLUMN bps_78000.t_cktj_organization_deposit_task_detail.belong_org_code IS '存款所属机构(该笔存款属于哪个支行、分理处)';
COMMENT ON COLUMN bps_78000.t_cktj_organization_deposit_task_detail.create_time IS '创建时间';
COMMENT ON COLUMN bps_78000.t_cktj_organization_deposit_task_detail.update_time IS '最后更新时间';
-- bps_78000.t_cktj_organization_deposit_task_detail 表索引
CREATE UNIQUE INDEX ON bps_78000.t_cktj_organization_deposit_task_detail(date, dp_org_code, belong_org_code, dp_category_id);




-- 存款统计配置
DROP TABLE IF EXISTS bps_78000.t_cktj_deposit_handle_config;
CREATE TABLE bps_78000.t_cktj_deposit_handle_config (
  id SERIAL NOT NULL PRIMARY KEY,
  type VARCHAR(1),
  org_code VARCHAR(20),
  dp_curr_date DATE,
  dp_next_date DATE,
  create_by INTEGER,
  create_time TIMESTAMPTZ NOT NULL,
  update_by INTEGER,
  update_time TIMESTAMPTZ NOT NULL
);
-- bps_78000.t_cktj_deposit_handle_config 表注释
COMMENT ON TABLE bps_78000.t_cktj_deposit_handle_config IS '存款统计配置表';
COMMENT ON SEQUENCE bps_78000.t_cktj_deposit_handle_config_id_seq IS '存款统计配置表主键序列';
-- bps_78000.t_cktj_deposit_handle_config 表字段注释
COMMENT ON COLUMN bps_78000.t_cktj_deposit_handle_config.id IS '主键';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_handle_config.type IS '1-存款跑批  0-生成未绑定账号列表';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_handle_config.org_code IS '机构编号';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_handle_config.dp_curr_date IS '已完成统计的存款日期';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_handle_config.dp_next_date IS '下次统计存款的日期';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_handle_config.create_by IS '创建者';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_handle_config.create_time IS '创建时间';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_handle_config.update_by IS '更新者';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_handle_config.update_time IS '最后更新时间';

-- 存款统计日志
DROP TABLE IF EXISTS bps_78000.t_cktj_deposit_handle_log;
CREATE TABLE bps_78000.t_cktj_deposit_handle_log (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  org_code VARCHAR(20),
  date DATE,
  dp_begin_time TIMESTAMPTZ,
  dp_end_time TIMESTAMPTZ,
  dp_cost_time INTEGER,
  prvisn_int_begin_time TIMESTAMPTZ,
  prvisn_int_end_time TIMESTAMPTZ,
  prvisn_int_cost_time INTEGER,
  pay_int_begin_time TIMESTAMPTZ,
  pay_int_end_time TIMESTAMPTZ,
  pay_int_cost_time INTEGER,
  remarks TEXT
);
-- bps_78000.t_cktj_deposit_handle_log 表注释
COMMENT ON TABLE bps_78000.t_cktj_deposit_handle_log IS '存款统计日志表';
COMMENT ON SEQUENCE bps_78000.t_cktj_deposit_handle_log_id_seq IS '存款统计日志表主键序列';
-- bps_78000.t_cktj_deposit_handle_log 表字段注释
COMMENT ON COLUMN bps_78000.t_cktj_deposit_handle_log.id IS '主键';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_handle_log.org_code IS '机构编号';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_handle_log.date IS '存款日期';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_handle_log.dp_begin_time IS '统计存款开始时间';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_handle_log.dp_end_time IS '统计存款结束时间';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_handle_log.dp_cost_time IS '统计存款用时(毫秒)';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_handle_log.prvisn_int_begin_time IS '统计计提利息开始时间';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_handle_log.prvisn_int_end_time IS '统计计提利息结束时间';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_handle_log.prvisn_int_cost_time IS '统计计提利息用时(毫秒)';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_handle_log.pay_int_begin_time IS '统计兑付利息开始时间';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_handle_log.pay_int_end_time IS '统计兑付利息结束时间';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_handle_log.pay_int_cost_time IS '统计兑付利息用时(毫秒)';
COMMENT ON COLUMN bps_78000.t_cktj_deposit_handle_log.remarks IS '备注';



-- 存款绑定的层级
DROP TABLE IF EXISTS bps_78000.t_cktj_bind_level;
CREATE TABLE bps_78000.t_cktj_bind_level(
    id serial,
    org_code varchar(20),
    task_payment_flag varchar(10),
    level varchar(128),
    create_time timestamptz,
    create_by bigint,
    update_time timestamptz,
    update_by bigint
);
CREATE UNIQUE INDEX ON bps_78000.t_cktj_bind_level(org_code, task_payment_flag);

COMMENT ON COLUMN bps_78000.t_cktj_bind_level.id IS 'id';
COMMENT ON COLUMN bps_78000.t_cktj_bind_level.org_code IS '机构号';
COMMENT ON COLUMN bps_78000.t_cktj_bind_level.task_payment_flag IS '任务数还是计酬数的标志: task-任务数  payment-计酬数';
COMMENT ON COLUMN bps_78000.t_cktj_bind_level.level IS '层级: terminal-网点  subbranch-支行  branch-中心支行  headquarters-总行';
COMMENT ON COLUMN bps_78000.t_cktj_bind_level.create_time IS '创建时间';
COMMENT ON COLUMN bps_78000.t_cktj_bind_level.create_by IS '创建人';
COMMENT ON COLUMN bps_78000.t_cktj_bind_level.update_time IS '更新时间';
COMMENT ON COLUMN bps_78000.t_cktj_bind_level.update_by IS '更新人';





-- 存款账户付息明细表 (手动生成数据，根据 dpfm30;dpfm21;dpfm99 三张表生成)
DROP TABLE IF EXISTS bps_78000.t_cktj_account_interest;
CREATE TABLE bps_78000.t_cktj_account_interest (
  id BIGSERIAL PRIMARY KEY NOT NULL,
  date DATE,
  orig_tx_no INTEGER,
  child_tx_no INTEGER,
  orig_tx_code VARCHAR(10),
  summary_code VARCHAR(3),
  summary VARCHAR(40),
  org_code VARCHAR(20),
  account_no VARCHAR(22),
  child_account_no VARCHAR(6),
  start_date DATE,
  end_date DATE,
  term1 INTEGER,
  term2 VARCHAR(1),
  subject_no VARCHAR(8),
  product_no VARCHAR(3),
  rate_type VARCHAR(1),
  rate NUMERIC(8, 6),
  draw_amt NUMERIC(17, 2),
  tax_int NUMERIC(14, 2),
  tx_org_code VARCHAR(20),
  tx_teller VARCHAR(10),
  orig_5211_account_no varchar(22) NOT NULL ,
  create_time TIMESTAMPTZ,
  acct_flag  VARCHAR(1),
  reg_teller VARCHAR(10),
  reg_time TIMESTAMPTZ
);
-- bps_78000.t_cktj_account_interest 表注释
COMMENT ON TABLE bps_78000.t_cktj_account_interest IS '存款账户付息明细表';
-- COMMENT ON SEQUENCE bps_78000.t_cktj_account_interest IS '存款账户付息明细表主键序列';
-- bps_78000.t_cktj_account_interest 表字段注释
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.id IS 'ID主键';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.date IS '交易日期';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.orig_tx_no IS '原交易流水号';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.child_tx_no IS '子交易流水号';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.orig_tx_code IS '原交易码';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.summary_code IS '摘要码';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.summary IS '摘要';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.org_code IS '开户机构';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.account_no IS '账号';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.child_account_no IS '分账号';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.start_date IS '起息日';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.end_date IS '止息日';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.term1 IS '存期';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.term2 IS '存期单位';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.subject_no IS '科目号';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.product_no IS '产品号';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.rate_type IS '利率类型:1-活期;2:定期;3:国债;4:保值';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.rate IS '利率';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.draw_amt IS '支取金额';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.tax_int IS '应税利息';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.tx_org_code IS '交易机构';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.tx_teller IS '交易柜员';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.orig_5211_account_no IS '来源于5211的哪个账号';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.create_time IS '记录生成时间';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.acct_flag IS '账号来源:1-系统匹配;2-柜员补登记';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.reg_teller IS '补登记账号柜员';
COMMENT ON COLUMN bps_78000.t_cktj_account_interest.reg_time IS '补登记账号时间';
-- bps_78000.t_cktj_account_interest 表索引
CREATE INDEX ON bps_78000.t_cktj_account_interest(date);
CREATE INDEX ON bps_78000.t_cktj_account_interest(account_no);


-- 支行专属的字典表
DROP TABLE IF EXISTS bps_78000.t_branch_dictionary;
CREATE TABLE  bps_78000.t_branch_dictionary(
     code varchar(128) primary key ,
     name varchar(128) unique not null ,
     sort integer unique ,
     create_time timestamptz default now()
);

COMMENT ON COLUMN bps_78000.t_branch_dictionary.code IS '编号';
COMMENT ON COLUMN bps_78000.t_branch_dictionary.name IS '名称';
COMMENT ON COLUMN bps_78000.t_branch_dictionary.sort IS '排序';
COMMENT ON COLUMN bps_78000.t_branch_dictionary.create_time IS '创建时间';


-- 存款账户的自动绑定规则
DROP TABLE IF EXISTS bps_78000.t_cktj_auto_bind_rule;
CREATE TABLE bps_78000.t_cktj_auto_bind_rule(
    id bigserial,
    org_code varchar(20),
    account_no varchar(22) not null ,
    child_account_no varchar(10),
    customer_no varchar(20) not null ,
    level varchar(128) not null default 'DEPOSIT_ACCOUNT_AUTO_BIND_LEVEL_ACCOUNT',
    create_time timestamptz default now(),
    create_by bigint,
    update_time timestamptz,
    update_by bigint,
    -- 约束
    CONSTRAINT child_account_no_check CHECK (
        child_account_no IS NULL OR child_account_no = '000000'
    )
);

COMMENT ON COLUMN bps_78000.t_cktj_auto_bind_rule.id IS 'id';
COMMENT ON COLUMN bps_78000.t_cktj_auto_bind_rule.org_code IS '设置绑定规则时账号所在的机构';
COMMENT ON COLUMN bps_78000.t_cktj_auto_bind_rule.account_no IS '设置绑定规则时使用的账号';
COMMENT ON COLUMN bps_78000.t_cktj_auto_bind_rule.child_account_no IS '设置绑定规则时使用的子账号';
COMMENT ON COLUMN bps_78000.t_cktj_auto_bind_rule.customer_no IS '客户号';
COMMENT ON COLUMN bps_78000.t_cktj_auto_bind_rule.level IS '层级: DEPOSIT_ACCOUNT_AUTO_BIND_LEVEL_NONE-不自动绑定  DEPOSIT_ACCOUNT_AUTO_BIND_LEVEL_ACCOUNT-账户级自动绑定';
COMMENT ON COLUMN bps_78000.t_cktj_auto_bind_rule.create_time IS '创建时间';
COMMENT ON COLUMN bps_78000.t_cktj_auto_bind_rule.create_by IS '创建者';
COMMENT ON COLUMN bps_78000.t_cktj_auto_bind_rule.update_time IS '更新时间';
COMMENT ON COLUMN bps_78000.t_cktj_auto_bind_rule.update_by IS '更新人';


-- 机构存款账户绑定规则表（存款账户绑定到员工时，允许员工在职机构的层级：只允许本网点内的员工绑定、允许本支行内的员工绑定、允许中心支行内的员工绑定、允许全部员工绑定）
DROP TABLE IF EXISTS bps_78000.t_cktj_org_bind_rule;
CREATE TABLE bps_78000.t_cktj_org_bind_rule(
    id bigserial,
    org_code varchar(20),
    level varchar(128),
    create_time timestamptz default now(),
    create_by bigint,
    update_time timestamptz,
    update_by bigint,
    primary key (org_code, level)
);

COMMENT ON COLUMN bps_78000.t_cktj_org_bind_rule.id IS 'id';
COMMENT ON COLUMN bps_78000.t_cktj_org_bind_rule.org_code IS '机构号';
COMMENT ON COLUMN bps_78000.t_cktj_org_bind_rule.level IS '机构内存款账号允许绑定的层级：只允许本网点内的员工绑定、允许本支行内的员工绑定、允许中心支行内的员工绑定、允许全部员工绑定';
COMMENT ON COLUMN bps_78000.t_cktj_org_bind_rule.create_time IS '创建时间';
COMMENT ON COLUMN bps_78000.t_cktj_org_bind_rule.create_by IS '创建人';
COMMENT ON COLUMN bps_78000.t_cktj_org_bind_rule.update_time IS '修改时间';
COMMENT ON COLUMN bps_78000.t_cktj_org_bind_rule.update_by IS '修改人';

-- 特殊账户绑定规则表（机构被t_cktj_org_bind_rule表中的规则限制，又想突破规则时，可在这里添加）
DROP TABLE IF EXISTS bps_78000.t_cktj_account_bind_custom_detail;
CREATE TABLE bps_78000.t_cktj_account_bind_custom_detail(
    id bigserial,
    account_no varchar(22),
    child_account_no varchar(10),
    target_teller_code varchar(20) not null ,
    create_time timestamptz default now(),
    create_by bigint,
    update_time timestamptz,
    update_by bigint
);

COMMENT ON COLUMN bps_78000.t_cktj_account_bind_custom_detail.id IS 'id';
COMMENT ON COLUMN bps_78000.t_cktj_account_bind_custom_detail.account_no IS '账号';
COMMENT ON COLUMN bps_78000.t_cktj_account_bind_custom_detail.child_account_no IS '子账号';
COMMENT ON COLUMN bps_78000.t_cktj_account_bind_custom_detail.target_teller_code IS '目标员工编号';
COMMENT ON COLUMN bps_78000.t_cktj_account_bind_custom_detail.create_time IS '创建时间';
COMMENT ON COLUMN bps_78000.t_cktj_account_bind_custom_detail.create_by IS '创建人';
COMMENT ON COLUMN bps_78000.t_cktj_account_bind_custom_detail.update_time IS '修改时间';
COMMENT ON COLUMN bps_78000.t_cktj_account_bind_custom_detail.update_by IS '修改人';