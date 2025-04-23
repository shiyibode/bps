DROP TABLE IF EXISTS bps_78000.t_ods_hxsj_subject_dict;
CREATE TABLE bps_78000.t_ods_hxsj_subject_dict(
    no VARCHAR(8),
    c_name VARCHAR(40),
    type varchar(1),
    acct_type varchar(1),
    ctrl_bal_dc varchar(1),
    ctrl_occ_amt_dc varchar(1),
    bal_gather_way varchar(1),
    level varchar(1),
    parent_no varchar(8),
    child_flag varchar(1),
    int_accr_flag varchar(1),
    red_mark_flag varchar(1),
    spl_acct_flag varchar(1),
    reg_acct_way varchar(1),
    ent_acct_way varchar(1),
    gather_flag varchar(1),
    effective_date Date,
    ccy_ctrl_field varchar(2),
    branch_type_ctrl_field varchar(2),
    date date,
    import_time timestamptz,
    data_source varchar(80)
);
CREATE INDEX ON bps_78000.t_ods_hxsj_subject_dict(no);

COMMENT ON TABLE bps_78000.t_ods_hxsj_subject_dict is '科目字典表';
--t_ods_hxsj_subject_dict 表字段注释
COMMENT ON COLUMN bps_78000.t_ods_hxsj_subject_dict.no IS '科目号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_subject_dict.c_name IS '中文名称';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_subject_dict.type IS '科目类别： 1-资产类科目 2-负债类科目 3-资产负债共同类科目 4-所有者权益类科目 6-表外类科目 7-损益类支出性科目 8-损益类收入性科目 5-或有资产负债类';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_subject_dict.acct_type IS '帐类: 1-存款 2-贷款 3-内部账 4-销账 5-表外';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_subject_dict.ctrl_bal_dc IS '控制余额方向：D-借方 C-贷方 B-借贷双向';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_subject_dict.ctrl_occ_amt_dc IS '控制发生额方向';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_subject_dict.bal_gather_way IS '余额汇总方式 0-轧差汇总 1-借贷并列汇总 在向上级科目或上级机构汇总时是否借贷方余额分别汇总';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_subject_dict.level IS '科目级别';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_subject_dict.parent_no IS '上级科目号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_subject_dict.child_flag IS '子科目标志：N-没有下级科目 Y-有下级科目';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_subject_dict.int_accr_flag IS '计息标志：Y-计息 N-不计息';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_subject_dict.red_mark_flag IS '红字标志：Y-余额可以为红字 N-余额不可以为红字';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_subject_dict.spl_acct_flag IS '分户标志 0-不可开内部帐户 1-开一般内部帐户 2-开标准内部帐户 3-开标准户但手工记账';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_subject_dict.reg_acct_way IS '记帐方式: 0-单式记帐 1-复式记帐 表内科目一定为复式记帐 表外科目有些是复式记帐；有的是单式记帐';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_subject_dict.ent_acct_way IS '入帐方式: 0-自动记帐 1-手工/传票记帐 9-任意自动记帐指该科目通过正常业务交易记帐,或通过下级科目汇总帐务而不能直接通过内部帐传票记帐.如 固定资产等．手工记帐指该科目通过内部帐传票记帐';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_subject_dict.gather_flag IS '汇总标志: N-不汇总明细 Y-汇总明细汇总明细指将当日该科目的总帐流水汇总为一条明细记帐';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_subject_dict.effective_date IS '生效日期';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_subject_dict.ccy_ctrl_field IS '币种控制字段';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_subject_dict.branch_type_ctrl_field IS '网点控制字段';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_subject_dict.date IS '数据日期';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_subject_dict.import_time IS '数据导入时间';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_subject_dict.data_source IS '数据来源文件';

-- 客户公用基本信息表
DROP TABLE IF EXISTS bps_78000.t_ods_hxsj_customer_public_base;
CREATE TABLE bps_78000.t_ods_hxsj_customer_public_base(
     no VARCHAR(10),
     type varchar(1),
     name varchar(200),
     oname varchar(100),
     en_name varchar(100),
     identity_type varchar(1),
     identity_no varchar(50),
     issue_date Date,
     expire_date Date,
     issue_place varchar(50),
     country_code varchar(3),
     create_org varchar(5),
     create_teller varchar(6),
     create_date Date,
     ctrl_code varchar(2),
     status varchar(20),
     level varchar(4),
     mpfs_cert_no varchar(50),
     loan_cret_no varchar(50),
     address_code varchar(50),
     hi_earnest_money numeric(15,2),
     remarks varchar(50),
     credit_level varchar(20),
     date date,
     import_time timestamptz,
     data_source varchar(80)
);
CREATE INDEX ON bps_78000.t_ods_hxsj_customer_public_base(no);

-- bps_78000.t_ods_hxsj_customer_public_base 表注释
COMMENT ON TABLE bps_78000.t_ods_hxsj_customer_public_base IS '客户公用基本信息表';
-- bps_78000.t_ods_hxsj_customer_public_base 表字段注释
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.no IS '客户编号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.type IS '客户类别';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.name IS '客户名称1';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.oname IS '客户名称2';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.en_name IS '英文名称';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.identity_type IS '证件类别';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.identity_no IS '证件编号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.issue_date IS '签发日期';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.expire_date IS '失效日期';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.issue_place IS '签发地点';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.country_code IS '国家代码';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.create_org IS '建立机构';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.create_teller IS '建立柜员';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.create_date IS '建立日期';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.ctrl_code IS '控制代码';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.status IS '状态';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.level IS '客户等级';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.mpfs_cert_no IS '股金证号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.loan_cret_no IS '贷款证号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.address_code IS '地址码';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.hi_earnest_money IS '最高保证金额';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.remarks IS '备注';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.credit_level IS '信用等级';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.import_time IS '数据导入时间';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_public_base.data_source IS '数据来源文件';



--客户号与账号/卡号对照表 (cifm01.txt)  增加unknow_field_1~unknow_field_4
DROP TABLE IF EXISTS bps_78000.t_ods_hxsj_customer_account;
CREATE TABLE bps_78000.t_ods_hxsj_customer_account (
      customer_no VARCHAR(10),
      account_no VARCHAR(22),
      child_sys_code VARCHAR(3),
      org_code VARCHAR(10),
      flag VARCHAR(5),
      last_print_date DATE,
      unknow_field_1 DATE,
      unknow_field_2 DATE,
      unknow_field_3 VARCHAR(4),
      unknow_field_4 DATE,
      import_time TIMESTAMPTZ,
      data_source VARCHAR(20)
);
-- bps_78000.t_ods_hxsj_customer_account 表注释
COMMENT ON TABLE bps_78000.t_ods_hxsj_customer_account IS '客户号与账号/卡号对照表';
-- bps_78000.t_ods_hxsj_customer_account 表字段注释
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_account.customer_no IS '客户号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_account.account_no IS '账号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_account.child_sys_code IS '子系统代码';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_account.org_code IS '网点号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_account.flag IS '标志;第一位:0-开户;1-销户;2-并入总户;第二位:1-重复账户';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_account.last_print_date IS '上次打印日期';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_account.import_time IS '数据导入时间';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_customer_account.data_source IS '数据来源文件';
-- bps_78000.t_ods_hxsj_customer_account_no 表索引
CREATE INDEX ON bps_78000.t_ods_hxsj_customer_account(account_no);
CREATE INDEX ON bps_78000.t_ods_hxsj_customer_account(customer_no);




-- 卡号账号对照表 (cdfm02.txt) 相同
DROP TABLE IF EXISTS bps_78000.t_ods_hxsj_card_account;
CREATE TABLE bps_78000.t_ods_hxsj_card_account (
      card_no VARCHAR(19),
      account_type VARCHAR(1),
      account_no VARCHAR(22),
      child_account_no VARCHAR(6),
      valid_flag VARCHAR(1),
      import_time TIMESTAMPTZ,
      data_source VARCHAR(20)
);
-- bps_78000.t_ods_hxsj_card_account 表注释
COMMENT ON TABLE bps_78000.t_ods_hxsj_card_account IS '卡号账号对照表';
-- bps_78000.t_ods_hxsj_card_account 表字段注释
COMMENT ON COLUMN bps_78000.t_ods_hxsj_card_account.card_no IS '卡号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_card_account.account_type IS '账户类型';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_card_account.account_no IS '账号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_card_account.child_account_no IS '分账号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_card_account.valid_flag IS '有效标志: 0-有效; 1-无效';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_card_account.import_time IS '数据导入时间';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_card_account.data_source IS '数据来源文件';
-- bps_78000.t_ods_hxsj_card_account 表字索引
CREATE INDEX ON bps_78000.t_ods_hxsj_card_account(card_no);
CREATE INDEX ON bps_78000.t_ods_hxsj_card_account(account_no);
CREATE INDEX ON bps_78000.t_ods_hxsj_card_account(valid_flag);





--存款账户信息表 (dpfm01.txt)
DROP TABLE IF EXISTS bps_78000.t_ods_hxsj_deposit_account;  --减少open_date、close_date、owner_flag共3个非原始数据有的字段
CREATE TABLE bps_78000.t_ods_hxsj_deposit_account (
      account_no VARCHAR(22),
      account_type varchar(10),
      close_acct_flag VARCHAR(10),
      overall_acct_flag VARCHAR(10),
      sec_lvl_acct_flag VARCHAR(10),
      voucher_flag VARCHAR(10),
      voucher_type VARCHAR(10),
      rmb_crown_no varchar(15),
      voucher_no VARCHAR(20),
      draw_way VARCHAR(10),
      draw_cert VARCHAR(100),
      valid_spl_acct_num NUMERIC(11),
      spl_acct_max_seq_num NUMERIC(11),
      customer_type VARCHAR(10),
      passbook_no NUMERIC(11),
      open_acct_licence_no VARCHAR(20),
      approval_cert_no VARCHAR(20),
      valid_date DATE,
      acct_obligate_flag VARCHAR(10),
      org_code VARCHAR(10),
      ledger_org_code varchar(10),
      date date,
      import_time TIMESTAMPTZ,
      data_source VARCHAR(20)
);
-- bps_78000.t_ods_hxsj_deposit_account 表注释
COMMENT ON TABLE bps_78000.t_ods_hxsj_deposit_account IS '存款账户信息表';
-- bps_78000.t_ods_hxsj_deposit_account 表字段注释
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.account_no IS '账号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.close_acct_flag IS '销户标志';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.overall_acct_flag IS '总户标志';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.sec_lvl_acct_flag IS '二级户标志';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.voucher_flag IS '凭证标志';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.voucher_type IS '凭证类型';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.voucher_no IS '凭证号码';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.draw_way IS '支取方式';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.draw_cert IS '支取证件';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.valid_spl_acct_num IS '有效分户数';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.spl_acct_max_seq_num IS '分户最大顺序号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.customer_type IS '客户性质';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.passbook_no IS '存折册号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.open_acct_licence_no IS '开户许可证号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.approval_cert_no IS '核准件号码';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.valid_date IS '有效期';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.acct_obligate_flag IS '帐户预留标志';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.org_code IS '网点号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.import_time IS '数据导入时间';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.data_source IS '数据来源文件';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.account_type IS '账户类型';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.rmb_crown_no IS '冠字号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account.ledger_org_code IS '核算机构';
-- bps_78000.t_ods_hxsj_deposit_account 表索引
CREATE INDEX ON bps_78000.t_ods_hxsj_deposit_account(account_no);
CREATE INDEX ON bps_78000.t_ods_hxsj_deposit_account(close_acct_flag);


-- bps_78000.t_ods_hxsj_deposit_account_detail 存款静态分户信息表
DROP TABLE IF EXISTS bps_78000.t_ods_hxsj_deposit_account_detail CASCADE;
CREATE TABLE bps_78000.t_ods_hxsj_deposit_account_detail (
      agrm_id varchar(40),
      agrm_mod varchar(20),
      account_no VARCHAR(22),
      child_account_no VARCHAR(6),
      org_code VARCHAR(10),
      check_org_code VARCHAR(10),
      subject_no VARCHAR(8),
      account_type VARCHAR(1),
      dem_regl_flag VARCHAR(1),
      close_flag VARCHAR(1),
      ccy_no VARCHAR(18),
      cash_remit_flag VARCHAR(1),
      product_no VARCHAR(3),
      open_date DATE,
      start_date DATE,
      end_date DATE,
      term1 NUMERIC(2),
      term2 VARCHAR(1),
      interest_plan VARCHAR(3),
      float_rate NUMERIC(2),
      prev_biz_date DATE,
      account_nature VARCHAR(1),
      frgn_acct_nature VARCHAR(4),
      insp_mon_acct_flag VARCHAR(1),
      unives_exchag_flag VARCHAR(1),
      encash_flag VARCHAR(1),
      ahead_draw_times NUMERIC(2),
      draw_interest_way VARCHAR(1),
      agrmt_rate NUMERIC(8,6),
      freeze_flag VARCHAR(3),
      pledge_flag VARCHAR(1),
      period_prove_flag varchar(1),
      agrmt_dp_flag VARCHAR(1),
      sec_lvl_acct_flag VARCHAR(1),
      ovrd_acct_flag VARCHAR(1),
      collt_interest_acct_flag VARCHAR(1),
      interest_enter_acct_flag VARCHAR(1),
      float_flag VARCHAR(1),
      fex_dp_instl_normal_amt NUMERIC(11,2),
      eve_tm_draw_dp_amt NUMERIC(11,2),
      intcol_fex_dp_inteval_mth NUMERIC(2),
      have_deal_times NUMERIC(2),
      nxt_tm_deal_date DATE,
      fex_dp_instl_omit_flag VARCHAR(1),
      deflt_times NUMERIC(2),
      rlover_flag VARCHAR(1),
      rlover_times NUMERIC(3),
      agrmt_no VARCHAR(12),
      sttl_comm_chrg_collt_way VARCHAR(1),
      reserved_amt NUMERIC(17,2),
      ctrl_flag VARCHAR(10),
      bgn_int_rate_year NUMERIC(8,6),
      curr_balance NUMERIC(17,2),
      curr_bal_dc VARCHAR(1),
      day_end_balance NUMERIC(17,2),
      day_end_bal_dc VARCHAR(1),
      before_day_balance NUMERIC(17,2),
      before_day_bal_dc VARCHAR(1),
      ctrl_amt NUMERIC(17,2),
      freeze_amt NUMERIC(17,2),
      ovrd_amt NUMERIC(17,2),
      accum1 NUMERIC(21,2),
      accum2 NUMERIC(21,2),
      accum3 NUMERIC(21,2),
      date DATE NOT NULL,
      import_time TIMESTAMPTZ,
      data_source VARCHAR(120)
);
-- bps_78000.t_ods_hxsj_deposit_account_detail 表注释
COMMENT ON TABLE bps_78000.t_ods_hxsj_deposit_account_detail IS '存款账户分户信息表';
-- bps_78000.t_ods_hxsj_deposit_account_detail 表字段注释
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.agrm_id IS '协议id';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.agrm_mod IS '协议修饰符';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.account_no IS '账号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.child_account_no IS '分账号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.org_code IS '机构号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.check_org_code IS '核算机构号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.subject_no IS '科目代号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.account_type IS '帐别';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.dem_regl_flag IS '活定期标志:1-活期,2-定期';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.close_flag IS '销户标志';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.ccy_no IS '币种号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.cash_remit_flag IS '钞汇标志';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.product_no IS '产品号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.open_date IS '开户日期';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.start_date IS '起息日';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.end_date IS '到期日';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.term1 IS '存期';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.term2 IS '存期类型';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.interest_plan IS '利息计划';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.float_rate IS '浮动利率';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.prev_biz_date IS '上笔业务日';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.account_nature IS '账户性质';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.frgn_acct_nature IS '外币账户性质';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.insp_mon_acct_flag IS '验资户标志';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.unives_exchag_flag IS '通兑标志';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.encash_flag IS '取现标志';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.ahead_draw_times IS '提前支取次数';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.draw_interest_way IS '取息方式';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.agrmt_rate IS '协议利率';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.freeze_flag IS '冻结标志';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.pledge_flag IS '质押标志';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.period_prove_flag IS '时段证明标志';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.agrmt_dp_flag IS '协定存款标志';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.sec_lvl_acct_flag IS '二级户标志';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.ovrd_acct_flag IS '透支账户标志';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.collt_interest_acct_flag IS '收息账户标志';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.interest_enter_acct_flag IS '利息入账标志';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.float_flag IS '浮动标志';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.fex_dp_instl_normal_amt IS '零整正常金额';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.eve_tm_draw_dp_amt IS '每次支存金额';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.intcol_fex_dp_inteval_mth IS '取息/零整间隔月';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.have_deal_times IS '已处理次数';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.nxt_tm_deal_date IS '下次处理日期';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.fex_dp_instl_omit_flag IS '零整漏存标志';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.deflt_times IS '违约次数';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.rlover_flag IS '转存标志:1-不转存,2-本息转存,4-本金转存';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.rlover_times IS '转存次数';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.agrmt_no IS '协议号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.sttl_comm_chrg_collt_way IS '结算手续费收取方式';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.reserved_amt IS '保留金额';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.ctrl_flag IS '控制标志';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.bgn_int_rate_year IS '起息日年利率';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.curr_balance IS '当前余额';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.curr_bal_dc IS '当前余额方向';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.day_end_balance IS '日终余额';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.day_end_bal_dc IS '日终余额方向';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.before_day_balance IS '上日余额';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.before_day_bal_dc IS '上日余额方向';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.ctrl_amt IS '控制金额';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.freeze_amt IS '冻结金额';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.ovrd_amt IS '透支金额';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.accum1 IS '积数1';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.accum2 IS '积数2';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.accum3 IS '积数3';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.date IS '日期';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.import_time IS '导入时间';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_account_detail.data_source IS '数据来源文件';
-- bps_78000.t_ods_hxsj_deposit_account_detail 表索引
CREATE INDEX ON bps_78000.t_ods_hxsj_deposit_account_detail(date);
CREATE INDEX ON bps_78000.t_ods_hxsj_deposit_account_detail(org_code);
CREATE INDEX ON bps_78000.t_ods_hxsj_deposit_account_detail(account_no);
CREATE INDEX ON bps_78000.t_ods_hxsj_deposit_account_detail(subject_no);




-- 内部账户信息表(dpfm03.txt)
DROP TABLE IF EXISTS bps_78000.t_ods_hxsj_internal_account;
CREATE TABLE bps_78000.t_ods_hxsj_internal_account (
      account_no VARCHAR(22),
      org_code VARCHAR(10),
      close_acct_flag VARCHAR(1),
      standard_flag VARCHAR(1),
      ctrl_bal_way VARCHAR(1),
      account_name VARCHAR(80),
      jf_kind VARCHAR(1),
      cle_reco_way VARCHAR(1),
      other_bank_name VARCHAR(50),
      other_side_acct_no VARCHAR(32),
      open_teller_code VARCHAR(10),
      close_date DATE,
      close_teller_code VARCHAR(10),
      import_time TIMESTAMPTZ,
      data_source VARCHAR(20)
);
-- bps_78000.t_ods_hxsj_internal_account 表注释
COMMENT ON TABLE bps_78000.t_ods_hxsj_internal_account IS '内部账户信息表';
-- bps_78000.t_ods_hxsj_internal_account 表字段注释
COMMENT ON COLUMN bps_78000.t_ods_hxsj_internal_account.account_no IS '账号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_internal_account.org_code IS '机构编号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_internal_account.close_acct_flag IS '销户标志: 0-正常; 1-销户';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_internal_account.standard_flag IS '是否标准户: 0-标准户; 其他-非标准户';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_internal_account.ctrl_bal_way IS '余额控制方向:C-贷方; D-借方';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_internal_account.account_name IS '户名';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_internal_account.jf_kind IS '账页种类: 3-内部账; 4-销账; 5-表外';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_internal_account.cle_reco_way IS '销账方式: 1-可以部分销账; 2-全额销账';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_internal_account.other_bank_name IS '他行行名';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_internal_account.other_side_acct_no IS '对方 / 他行账号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_internal_account.open_teller_code IS '开户柜员';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_internal_account.close_date IS '销户日期';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_internal_account.close_teller_code IS '销户柜员';

COMMENT ON COLUMN bps_78000.t_ods_hxsj_internal_account.import_time IS '数据导入时间';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_internal_account.data_source IS '数据来源文件';
-- bps_78000.t_ods_hxsj_internal_account 表字索引
CREATE INDEX ON bps_78000.t_ods_hxsj_internal_account(account_no);






--账户交易明细表(dpfm30.txt)
DROP TABLE IF EXISTS bps_78000.t_ods_hxsj_deposit_detail;
CREATE TABLE bps_78000.t_ods_hxsj_deposit_detail (
      account_no VARCHAR(22),
      child_account_no VARCHAR(6),
      tx_date DATE,
      orig_tx_no INTEGER,
      child_tx_no INTEGER,
      ccy VARCHAR(2),
      customer_type VARCHAR(1),
      tx_amt NUMERIC(17,2),
      deb_cred_flag VARCHAR(1),
      balance NUMERIC(17,2),
      curr_bal_dc VARCHAR(1),
      tx_org_code VARCHAR(10),
      open_acct_org_code VARCHAR(10),
      cash_tran_flag VARCHAR(1),
      voucher_type VARCHAR(3),
      voucher_no VARCHAR(20),
      page_no NUMERIC(6),
      count NUMERIC(2),
      print_flag VARCHAR(1),
      orig_tx_code VARCHAR(10),
      tx_teller VARCHAR(10),
      review_teller VARCHAR(10),
      other_side_acct_no VARCHAR(22),
      other_side_child_acct_no VARCHAR(6),
      summary_code VARCHAR(3),
      summary VARCHAR(80),
      tx_src VARCHAR(2),
      card_no VARCHAR(19),
      import_time TIMESTAMPTZ,
      data_source VARCHAR(20)
);
-- bps_78000.t_ods_hxsj_deposit_detail 表注释
COMMENT ON TABLE bps_78000.t_ods_hxsj_deposit_detail IS '账户交易明细表';
-- bps_78000.t_ods_hxsj_deposit_detail 表字段注释
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.account_no IS '账号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.child_account_no IS '分账号/内部账分笔序号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.tx_date IS '交易日期';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.orig_tx_no IS '原交易流水号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.child_tx_no IS '子交易流水号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.ccy IS '币种';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.customer_type IS '客户类别: 1-储蓄;2-对公;３-金融;5-内部账';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.tx_amt IS '交易金额';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.deb_cred_flag IS '借贷标识: C-贷;D-借';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.balance IS '余额';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.curr_bal_dc IS '当前余额方向: C-贷;D-借';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.tx_org_code IS '交易机构';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.open_acct_org_code IS '开户机构';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.cash_tran_flag IS '现转标识: 1-现金;2-转账';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.voucher_type IS '凭证类型';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.voucher_no IS '凭证号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.page_no IS '页次';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.count IS '笔次';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.print_flag IS '打印标志 0-未打印 1-已打印';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.orig_tx_code IS '原交易码';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.tx_teller IS '交易柜员';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.review_teller IS '复核柜员';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.other_side_acct_no IS '对方帐号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.other_side_child_acct_no IS '对方分账号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.summary_code IS '摘要码';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.summary IS '摘要';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.tx_src IS '交易来源';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.card_no IS '卡号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.import_time IS '导入时间';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_deposit_detail.data_source IS '数据来源文件';
-- bps_78000.t_ods_hxsj_deposit_detail 表索引
CREATE INDEX ON bps_78000.t_ods_hxsj_deposit_detail(tx_date);




-- 总账明细(glfm01.txt)
DROP TABLE IF EXISTS bps_78000.t_ods_hxsj_ledger_detail;
CREATE TABLE bps_78000.t_ods_hxsj_ledger_detail (
      busi_date date,
      org_code VARCHAR(10),
      ccy_no VARCHAR(18),
      subject_no VARCHAR(8),
      curr_day_ttl_dbt_amt NUMERIC(17,2),
      curr_day_ttl_crted_amt NUMERIC(17,2),
      curr_day_ttl_dbt_cnt numeric(6,0),
      curr_day_ttl_crted_cnt numeric(6,0),
      curr_dbt_bal NUMERIC(17,2),
      curr_crted_bal NUMERIC(17,2),
      date DATE NOT NULL,
      import_time TIMESTAMPTZ,
      data_source VARCHAR(120)
);
-- bps_78000.t_ods_hxsj_ledger_detail 表注释
COMMENT ON TABLE bps_78000.t_ods_hxsj_ledger_detail IS '总账明细表';
-- bps_78000.t_ods_hxsj_ledger_detail 表字段注释
COMMENT ON COLUMN bps_78000.t_ods_hxsj_ledger_detail.busi_date IS '日期';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_ledger_detail.org_code IS '机构号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_ledger_detail.ccy_no IS '币种号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_ledger_detail.subject_no IS '科目号';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_ledger_detail.curr_day_ttl_dbt_amt IS '本日累计借方发生额';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_ledger_detail.curr_day_ttl_crted_amt IS '本日累计贷方发生额';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_ledger_detail.curr_day_ttl_dbt_cnt IS '本日累计借方发生笔数';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_ledger_detail.curr_day_ttl_crted_cnt IS '本日累计贷方发生笔数';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_ledger_detail.curr_dbt_bal IS '当前借方余额';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_ledger_detail.curr_crted_bal IS '当前贷方余额';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_ledger_detail.date IS '数据日期';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_ledger_detail.import_time IS '导入时间';
COMMENT ON COLUMN bps_78000.t_ods_hxsj_ledger_detail.data_source IS '数据来源文件';
-- bps_78000.t_ods_hxsj_ledger_detail 表索引
CREATE INDEX ON bps_78000.t_ods_hxsj_ledger_detail(date);
CREATE INDEX ON bps_78000.t_ods_hxsj_ledger_detail(org_code);











