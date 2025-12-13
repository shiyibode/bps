
--服务2-遍历信贷系统客户信息，生成未绑定到员工的客户列表（必须是前一日的贷款账户绑定完毕、复核完毕，才能生成今日的未绑定列表）
DROP FUNCTION IF EXISTS dktj_generate_unbound_customer(para_org_code varchar, para_date Date);
CREATE OR REPLACE FUNCTION dktj_generate_unbound_customer(para_org_code varchar, para_date Date)
  RETURNS BOOLEAN
AS $$
DECLARE
  loan_crd_data_count INTEGER;
  lnfm01_data_count INTEGER;
  r RECORD;
  r2 RECORD;
  select_sql varchar;
    tmp_date date;
    tmp_org_code varchar;
BEGIN

  SELECT count(*) FROM t_ods_t03_loancrd_inf_h WHERE date = para_date INTO loan_crd_data_count;

  -- 1-判断当日的数据是否已经具备
  IF loan_crd_data_count < 1 THEN
    RAISE EXCEPTION '%日的数据尚未导入：信贷三期贷款卡片信息，t_ods_t03_loancrd_inf_h表',para_date;
  END IF;
  SELECT count(*) FROM t_ods_s01_lnfm01 WHERE date = para_date INTO lnfm01_data_count;
  IF lnfm01_data_count < 1 THEN
      RAISE EXCEPTION '%日的数据尚未导入：贷款静态信息表，t_ods_s01_lnfm01表',para_date;
  END IF;

  -- 3-删除未绑定列表中原始的数据
  DELETE FROM t_dktj_unbound_customer WHERE date = para_date AND org_code = para_org_code AND flag = '1';

  -- 4-筛选出未绑定的信贷客户的账号loan_account，过滤掉：08-已结清 09-已票据置换  个人证件种类有3种：CD0100102000000020、CD0100102000000099、CD0100102000000011、30、12
--   select_sql := 'select l.cust_id, l.c_name, l.loan_account, lnfm01.open_date, l.loan_date, o.core_ins_no, l.cust_id_type, case when l.cust_id like ''P%'' then ''1'' else ''2'' end as cust_type, l.cust_id_no from t_ods_t03_loancrd_inf_h l left join t_ods_t04_cr_core_inst_ctrt_h o on l.dept_code = o.ori_stm_ins_id left join t_ods_s01_lnfm01 lnfm01 on l.loan_account = lnfm01.account_no where l.date = $1 and l.loan_account not in (select account_no from t_dktj_employee_customer where org_code = $2) and l.loan_account not in (select account_no from t_dktj_unbound_customer where org_code = $3)  and lnfm01.status_flag not in(''08'',''09'') and lnfm01.date = $4';
  select_sql := 'select l.cust_id,l.c_name,l.loan_account,l.start_date,l.loan_date,a.org_code,l.cust_id_type,case when l.cust_id like ''P%'' then ''1'' else ''2'' end as cust_type, l.cust_id_no from t_ods_s01_lnfm02 a left join t_ods_t03_loancrd_inf_h l on a.account_no = l.loan_account where a.child_account_no in (''01'',''02'',''03'',''04'') and a.before_day_balance != 0 and a.account_no not in (select account_no from t_dktj_employee_customer where org_code = $1) and a.account_no not in (select account_no from t_dktj_unbound_customer where org_code = $1) and a.org_code = $1 and a.date = $2 and l.date = $2 ';
  FOR r IN EXECUTE select_sql USING para_org_code,para_date LOOP
--     IF r.core_ins_no ISNULL OR r.core_ins_no = '' THEN
--         RAISE EXCEPTION '客户号：%，贷款账号：%，日期：%，未找到所属的核心机构号',r.cust_id,r.loan_account,para_date;
--     END IF;


    -- 5-插入到未绑定列表中
--     IF r.core_ins_no = para_org_code THEN
      IF r.start_date >= '20210101' THEN
          tmp_date := r.start_date;
      Else
          tmp_date := para_date;
      END IF;

--       RAISE NOTICE 'account_no: %, tmp_date: %',r.loan_account,tmp_date;

      IF tmp_date ISNULL then
          RAISE EXCEPTION '无法获取到登记日期,账号: %, para_date: %',r.loan_account,para_date;
      END IF;

        INSERT INTO t_dktj_unbound_customer(xd_customer_no, account_no, account_open_date, customer_name, start_date, org_code, identity_type, identity_no, date, customer_type, create_time, flag) VALUES (r.cust_id, r.loan_account, r.start_date, r.c_name, null, r.org_code, r.cust_id_type, r.cust_id_no, tmp_date, r.cust_type, now(), '1');
--     END IF;
  END LOOP;

  -- 补充当日开户，当日22点以后才还款导致产生了利息，但是当日日终余额为0的情况
  select_sql := 'select distinct a.account_no from t_ods_t05_loan_dtl_inf a where a.child_account_no = ''05'' and a.deb_cred_flag = ''D'' and a.tx_amt != 0  and a.account_no like ''780__0110%'' and a.account_no not in (select account_no from t_dktj_employee_customer where org_code = $1) and a.account_no not in (select account_no from t_dktj_unbound_customer where org_code = $1)  and a.tx_org_code = $1 and a.tx_date = $2 ';
  FOR r IN EXECUTE select_sql USING para_org_code,para_date LOOP
      SELECT l.cust_id,l.c_name,l.loan_account,l.start_date,l.loan_date,l.cust_id_type,case when l.cust_id like 'P%' then '1' else '2' end as cust_type, l.cust_id_no FROM t_ods_t03_loancrd_inf_h l WHERE date = para_date AND loan_account = r.account_no INTO r2;
      SELECT org_code FROM t_ods_s01_lnfm02 WHERE account_no = r.account_no AND date = para_date INTO tmp_org_code;

      IF tmp_org_code = para_org_code THEN
          INSERT INTO t_dktj_unbound_customer(xd_customer_no, account_no, account_open_date, customer_name, start_date, org_code, identity_type, identity_no, date, customer_type, create_time, flag) VALUES (r2.cust_id, r2.loan_account, r2.start_date, r2.c_name, null, para_org_code, r2.cust_id_type, r2.cust_id_no, tmp_date, r2.cust_type, now(), '1');
      END IF;
  END LOOP;

  -- 6-修改配置表
--   next_date := para_date + INTERVAL '1 day';
--   UPDATE t_dktj_loan_handle_config SET ln_curr_date=para_date,ln_next_date=next_date WHERE org_code = para_org_code AND type = '4';
  RETURN TRUE;

  EXCEPTION
  WHEN OTHERS THEN
    RAISE EXCEPTION '错误信息：%',SQLERRM;

END;
$$ LANGUAGE plpgsql;


-- 辅助服务： 判断在某一日之前，某个网点是否有未复核、未登记的贷款账号
DROP FUNCTION IF EXISTS dktj_have_uncheck_unbound_flag(para_date Date, para_org_code VARCHAR);
CREATE OR REPLACE FUNCTION dktj_have_uncheck_unbound_flag(para_date Date, para_org_code VARCHAR)
    RETURNS BOOLEAN
AS $$
DECLARE
    unbound_count INTEGER;
    register_uncheck_count INTEGER;
    alter_uncheck_count INTEGER;
BEGIN

    SELECT count(*) FROM t_dktj_unbound_customer WHERE date < para_date AND org_code = para_org_code INTO unbound_count;
    SELECT count(*) FROM t_dktj_employee_customer WHERE register_check_status = '0' and start_date < para_date AND org_code = para_org_code INTO register_uncheck_count;
    SELECT count(*) FROM t_dktj_employee_customer WHERE alter_check_status = '0' and start_date < para_date AND org_code = para_org_code INTO alter_uncheck_count;

    IF unbound_count > 0 THEN
        RAISE EXCEPTION '%日 %机构，有未绑定账户 % 户',para_date,para_org_code,unbound_count;
    END IF;
    IF register_uncheck_count > 0 THEN
        RAISE EXCEPTION '%日 %机构，有新绑定未复核账户 % 户',para_date,para_org_code,register_uncheck_count;
    END IF;
    IF alter_uncheck_count > 0 THEN
        RAISE EXCEPTION '%日 %机构，有变更未复核账户 % 户',para_date,para_org_code,alter_uncheck_count;
    END IF;

    RETURN TRUE;

EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE '错误信息：%',SQLERRM;
        RETURN FALSE;
END;
$$ LANGUAGE plpgsql;


--服务3-单个网点贷款跑批（跑批前要先跑 柜员调动释放服务）
DROP FUNCTION IF EXISTS dktj_calculate_loan_detail(para_org_code VARCHAR, para_data_date DATE);
CREATE OR REPLACE FUNCTION dktj_calculate_loan_detail(para_org_code VARCHAR, para_data_date DATE)
  RETURNS BOOLEAN
AS $$
DECLARE
  tmp_count_1 INTEGER;
  tmp_count_2 INTEGER;
  select_sql VARCHAR;
  insert_empln_sql VARCHAR;
  insert_orgln_sql VARCHAR;
  tmp_teller_code VARCHAR;
  tmp_teller_org_code VARCHAR;
  r RECORD;
  tmp_r RECORD;
  next_date Date;
  sum_org NUMERIC;
  sum_emp NUMERIC;
  sum_report_form NUMERIC;
  date_person Date;
  date_group Date;
  date_lc Date;
  date_fm01 Date;
  date_fm02 Date;
  five_class_count INTEGER;
  temp_five_class_flag varchar;
  ledger_count INTEGER;
  tmp_ln_curr_date date;
  sum_tax_int numeric(17,2);
  tmp_count_3 INTEGER;
  min_child_account_no varchar;
  tmp_subject_no varchar;
  before_date date;
  day_of_year INTEGER;
  tmp_form_interest numeric;
  tmp_real_interest numeric;
  begin_date_of_year date;
  tmp_customer_status VARCHAR;
BEGIN
  -- 1-判断是否有未绑定或未复核的客户
  SELECT count(*) FROM t_dktj_unbound_customer WHERE org_code = para_org_code AND date <= para_data_date INTO tmp_count_1;
  IF tmp_count_1 > 0 THEN
    RAISE EXCEPTION '机构 % 尚有 % 个客户未绑定',para_org_code,tmp_count_1;
  END IF;
  SELECT count(*) FROM t_dktj_employee_customer WHERE org_code = para_org_code AND (register_check_status = '0' OR alter_check_status = '0') AND start_date <= para_data_date INTO tmp_count_2;
  IF tmp_count_2 > 0 THEN
    RAISE EXCEPTION '机构 % 尚有 % 个客户未复核',para_org_code,tmp_count_2;
  END IF;

  -- 2.1-判断生成未绑定贷款列表、柜员调动服务、自动绑定服务是否已经执行完毕
  SELECT ln_curr_date FROM t_dktj_loan_handle_config WHERE org_code = para_org_code AND type = '0' INTO tmp_ln_curr_date;
  IF tmp_ln_curr_date is null OR tmp_ln_curr_date < para_data_date THEN
      RAISE EXCEPTION '尚未执行生成未绑定贷款列表、柜员调动服务、自动绑定服务，当前要执行的日期: %, 上述服务执行到的日期: %',para_data_date, tmp_ln_curr_date;
  END IF;

  -- 2.2-判断利息归属到账号服务是否已经执行完毕
  SELECT ln_curr_date FROM t_dktj_loan_handle_config WHERE org_code = para_org_code AND type = '6' INTO tmp_ln_curr_date;
  IF tmp_ln_curr_date is null OR tmp_ln_curr_date < para_data_date THEN
      RAISE EXCEPTION '尚未执行生成利息到账户服务，当前要执行的日期: %, 上述服务执行到的日期: %',para_data_date, tmp_ln_curr_date;
  END IF;

  -- 3-判断数据日期是否到达
  SELECT import_date FROM t_ods_data_handle_config WHERE file_name = 'DUL_T01_CR_PRVT_IP_INF_H.dat' INTO date_person;
  SELECT import_date FROM t_ods_data_handle_config WHERE file_name = 'DUL_T01_CR_CORP_IP_INF_H.dat' INTO date_group;
  SELECT import_date FROM t_ods_data_handle_config WHERE file_name = 'T03_LOANCRD_INF_H.dat' INTO date_lc;
  SELECT import_date FROM t_ods_data_handle_config WHERE file_name = 'S01_LNFM02.dat' INTO date_fm02;
  SELECT import_date FROM t_ods_data_handle_config WHERE file_name = 'S01_LNFM01.dat' INTO date_fm01;

  IF date_person < para_data_date OR date_group < para_data_date OR date_lc < para_data_date OR date_fm01 < para_data_date OR date_fm02 < para_data_date THEN
    RAISE EXCEPTION '数据日期尚未到达,无法执行服务. 对公客户信息表日期：%, 个人客户信息表日期：%, 贷款卡片表日期：%, LNFM01表: %, LNFM02表日期：%, 当前要执行的日期：%',date_group,date_person,date_lc,date_fm01,date_fm02,para_data_date;
  END IF;

  SELECT count(*) FROM t_hxsj_ledger_detail WHERE date = para_data_date INTO ledger_count;
  IF ledger_count < 1 THEN
      RAISE EXCEPTION '% 日的总账表尚未到达，请检查',para_data_date;
  END IF;

  -- 4-判断贷款每日五级分类表是否生成
  SELECT count(*) FROM t_middle_table_five_class WHERE date = para_data_date INTO five_class_count;
  IF five_class_count < 1 THEN
      RAISE EXCEPTION '% 日的贷款五级分类中间表尚未生成，请检查',para_data_date;
  END IF;

  -- 创建临时表
  CREATE TEMP TABLE tmp_empln_detail (
     date DATE,
     teller_code VARCHAR(10),
     teller_org_code VARCHAR(10),
     four_class_flag VARCHAR(4),
     five_class_flag VARCHAR(4),
     balance NUMERIC(21,2) NOT NULL DEFAULT 0,
     ttl_received_int numeric(17,2) NOT NULL DEFAULT 0,
     day_received_int NUMERIC(17, 2) NOT NULL DEFAULT 0,
     ln_org_code VARCHAR(10),
     belong_org_code VARCHAR(10)
  ) ON COMMIT DROP;
  CREATE UNIQUE INDEX ON tmp_empln_detail(date, teller_code, teller_org_code, ln_org_code, belong_org_code, four_class_flag, five_class_flag);

  CREATE TEMP TABLE tmp_orgln_detail (
     date DATE,
     four_class_flag VARCHAR(4),
     five_class_flag VARCHAR(4),
     balance NUMERIC(21,2) NOT NULL DEFAULT 0,
     ttl_received_int numeric(17,2) NOT NULL DEFAULT 0,
     day_received_int NUMERIC(17, 2) NOT NULL DEFAULT 0,
     ln_org_code VARCHAR(10),
     belong_org_code VARCHAR(10)
  ) ON COMMIT DROP;
  CREATE UNIQUE INDEX ON tmp_orgln_detail(date, ln_org_code, belong_org_code, four_class_flag, five_class_flag);

  insert_empln_sql := 'INSERT INTO tmp_empln_detail (date, teller_code, teller_org_code, four_class_flag, five_class_flag, balance, ln_org_code, belong_org_code, day_received_int)
          VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
          ON CONFLICT (date, teller_code, teller_org_code, ln_org_code, belong_org_code, four_class_flag, five_class_flag) DO UPDATE SET balance = tmp_empln_detail.balance + $6, day_received_int = tmp_empln_detail.day_received_int + $9';

  insert_orgln_sql := 'INSERT INTO tmp_orgln_detail (date, four_class_flag, five_class_flag, balance, ln_org_code, belong_org_code, day_received_int)
          VALUES ($1, $2, $3, $4, $5, $6, $7)
          ON CONFLICT (date, ln_org_code, belong_org_code, four_class_flag, five_class_flag) DO UPDATE SET balance = tmp_orgln_detail.balance + $4, day_received_int = tmp_orgln_detail.day_received_int + $7';
  select_sql := 'SELECT
              org_code, account_no, child_account_no, subject_no, before_day_balance
            FROM
              t_ods_s01_lnfm02
            WHERE
              org_code = $1 AND date = $2 AND
              child_account_no in (''01'',''02'',''03'',''04'') AND before_day_balance >0 AND subject_no NOT LIKE ''1392%'' ';



  FOR r IN EXECUTE select_sql USING para_org_code,para_data_date LOOP
      IF para_org_code = '78011' THEN
          RAISE NOTICE '%',r.account_no;
      END IF;
    --1、通过信贷卡片表获取到客户号
    SELECT loan_account,cust_id FROM t_ods_t03_loancrd_inf_h WHERE loan_account = r.account_no AND date = para_data_date INTO tmp_r;
    SELECT five_class FROM t_middle_table_five_class WHERE account_no = r.account_no AND date = para_data_date INTO temp_five_class_flag;

    IF temp_five_class_flag ISNULL OR temp_five_class_flag = '' THEN
        temp_five_class_flag := '0000';
    END IF;

    --2、通过贷款账号和日期获取到所属柜员
--     SELECT dktj_get_teller_code_by_account_no(r.account_no,para_data_date,para_org_code) INTO tmp_teller_code;
    SELECT dktj_get_teller_code_by_account_no(r.account_no,para_data_date,para_org_code) INTO tmp_teller_code;

    --3、通过teller_code获取到para_data_date日期柜员的在职机构
--     SELECT get_org_code_by_teller_code(tmp_teller_code, para_data_date) INTO tmp_teller_org_code;
    SELECT get_org_code_by_teller_code(tmp_teller_code, para_data_date) INTO tmp_teller_org_code;
    IF tmp_teller_org_code IS NULL OR tmp_teller_org_code = '' THEN
      RAISE EXCEPTION '通过柜员号: %, 日期: %, 找不到该柜员的在职机构！', tmp_teller_code, para_data_date;
    END IF;

    -- 判断客户在当日的状态，固定客户不允许绑定给非本机构员工
--     SELECT status FROM t_dktj_customer_status WHERE para_data_date >= start_date AND para_data_date <= coalesce(end_date,'2099-12-31') AND org_code = para_org_code AND xd_customer_no = tmp_r.cust_id INTO tmp_customer_status;
--     IF tmp_customer_status ISNULL THEN
--         RAISE EXCEPTION '贷款跑批失败, 通过信贷客户号: % 获取客户状态失败, 日期: %, 机构: %',tmp_r.cust_id,para_data_date,para_org_code;
--     END IF;
--     IF tmp_customer_status = '1' and tmp_teller_org_code != para_org_code THEN
--         RAISE EXCEPTION '固定客户的账号归属于了非本机构的柜员, 账号: %, 客户号: %, 日期: %, 机构: %',r.account_no,tmp_r.cust_id,para_data_date,para_org_code;
--     END IF;

    -- 计算该笔贷款账号的当日利息收入
    sum_tax_int := 0;
    SELECT coalesce(sum(tax_int),0) FROM t_dktj_account_interest WHERE account_no = r.account_no AND date = para_data_date AND org_code = para_org_code INTO sum_tax_int;
    SELECT count(*) FROM t_ods_s01_lnfm02 WHERE date = para_data_date AND account_no = r.account_no AND before_day_balance > 0 INTO tmp_count_3;
    IF tmp_count_3 > 1 THEN
        SELECT min(child_account_no) FROM t_ods_s01_lnfm02 WHERE account_no = r.account_no AND date = para_data_date AND before_day_balance > 0 INTO min_child_account_no;
        IF r.child_account_no != min_child_account_no THEN --防止重复统计利息，只把利息放在child_account_no最小的那个记录上
            sum_tax_int := 0;
        END IF;
    END IF;

    --4、写入到临时员工存款统计表
    EXECUTE insert_empln_sql USING para_data_date,tmp_teller_code,tmp_teller_org_code,r.child_account_no,temp_five_class_flag,r.before_day_balance,r.org_code,tmp_teller_org_code,sum_tax_int;

    --5、写入到临时机构存款统计表
    EXECUTE insert_orgln_sql USING para_data_date,r.child_account_no,temp_five_class_flag,r.before_day_balance,r.org_code,tmp_teller_org_code,sum_tax_int;

  END LOOP;

  -- 更新已销户贷款的利息补记
  select_sql := 'select account_no,sum(tax_int) as tax_int from t_dktj_account_interest where org_code = $1 and date = $2 and account_no not in (select account_no from t_ods_s01_lnfm02 where org_code = $1 and date = $2 and child_account_no in (''01'',''02'',''03'',''04'') AND before_day_balance >0 AND subject_no NOT LIKE ''1392%'' ) group by account_no having sum(tax_int) != 0';
  FOR r IN EXECUTE select_sql USING para_org_code,para_data_date LOOP
    SELECT subject_no FROM t_ods_s01_lnfm02 WHERE account_no = r.account_no AND date = para_data_date AND child_account_no = '01' INTO tmp_subject_no;
    SELECT five_class FROM t_middle_table_five_class WHERE account_no = r.account_no AND date = para_data_date INTO temp_five_class_flag;

    IF temp_five_class_flag ISNULL OR temp_five_class_flag = '' THEN
        temp_five_class_flag := '0000';
    END IF;

    SELECT dktj_get_teller_code_by_account_no(r.account_no,para_data_date,para_org_code) INTO tmp_teller_code;
    SELECT get_org_code_by_teller_code(tmp_teller_code, para_data_date) INTO tmp_teller_org_code;
    IF tmp_teller_org_code IS NULL OR tmp_teller_org_code = '' THEN
        RAISE EXCEPTION '通过柜员号: %, 日期: %, 找不到该柜员的在职机构！', tmp_teller_code, para_data_date;
    END IF;

    EXECUTE insert_empln_sql USING para_data_date,tmp_teller_code,tmp_teller_org_code,'01',temp_five_class_flag,0,para_org_code,tmp_teller_org_code,r.tax_int;
    EXECUTE insert_orgln_sql USING para_data_date,'01',temp_five_class_flag,0,para_org_code,tmp_teller_org_code,r.tax_int;
  END LOOP;

  SELECT to_date(to_char(para_data_date,'yyyy-01-01'),'yyyy-mm-dd') INTO begin_date_of_year;
  --6、删除可能存在的该日期数据，写入到员工贷款统计表
  DELETE FROM t_dktj_employee_loan_detail WHERE date = para_data_date AND ln_org_code = para_org_code;
  INSERT INTO t_dktj_employee_loan_detail(date, teller_code, teller_org_code, four_class_flag, five_class_flag, balance, ln_org_code, belong_org_code, day_received_int, ttl_received_int, create_time, update_time) SELECT date,teller_code,teller_org_code,four_class_flag,five_class_flag,balance,ln_org_code,belong_org_code, day_received_int, day_received_int , now(),now() FROM tmp_empln_detail;

  --7、删除可能存在的该日期数据，写入到机构贷款统计表
  DELETE FROM t_dktj_organization_loan_detail WHERE date = para_data_date AND ln_org_code = para_org_code;
  INSERT INTO t_dktj_organization_loan_detail(date, four_class_flag, five_class_flag, balance, ln_org_code, belong_org_code, day_received_int, ttl_received_int, create_time, update_time) SELECT date,four_class_flag,five_class_flag,balance,ln_org_code,belong_org_code,day_received_int, day_received_int + coalesce(ttl_received_int,0),now(),now() FROM tmp_orgln_detail;

  -- 更新累计付息字段
  before_date := para_data_date + INTERVAL  '-1 day';
  select_sql := 'select * from t_dktj_employee_loan_detail where date = $1 and ln_org_code = $2';
  SELECT extract(doy from para_data_date) INTO day_of_year;
  IF day_of_year > 1 THEN
      FOR r IN EXECUTE select_sql USING before_date,para_org_code LOOP
              INSERT INTO t_dktj_employee_loan_detail (date, teller_code, teller_org_code, four_class_flag, five_class_flag, balance, ttl_received_int, day_received_int, ln_org_code, belong_org_code, create_time, update_time) VALUES (para_data_date, r.teller_code, r.teller_org_code, r.four_class_flag, r.five_class_flag, 0, r.ttl_received_int, 0, r.ln_org_code, r.belong_org_code, now(), now() )
              ON CONFLICT (date,teller_code,teller_org_code, four_class_flag, five_class_flag, ln_org_code, belong_org_code) DO UPDATE SET ttl_received_int = t_dktj_employee_loan_detail.ttl_received_int + r.ttl_received_int;
      END LOOP;
  END IF;
  select_sql := 'select * from t_dktj_organization_loan_detail where date = $1 and ln_org_code = $2';
  IF day_of_year > 1 THEN
      FOR r IN EXECUTE select_sql USING before_date,para_org_code LOOP
              INSERT INTO t_dktj_organization_loan_detail (date, four_class_flag, five_class_flag, balance, ttl_received_int, day_received_int, ln_org_code, belong_org_code, create_time, update_time) VALUES (para_data_date, r.four_class_flag, r.five_class_flag, 0, r.ttl_received_int, 0, r.ln_org_code, r.belong_org_code, now(), now() )
              ON CONFLICT (date,four_class_flag, five_class_flag, ln_org_code, belong_org_code) DO UPDATE SET ttl_received_int = t_dktj_organization_loan_detail.ttl_received_int + r.ttl_received_int;
          END LOOP;
  END IF;

  --8、更新t_dktj_loan_handle_config即配置表
  next_date := para_data_date + INTERVAL '1 day';
  UPDATE t_dktj_loan_handle_config SET ln_next_date = next_date,ln_curr_date = para_data_date,update_time = now() WHERE org_code = para_org_code AND type = '1';

  --9、判断计算的结果是否正确
  SELECT sum(balance) FROM t_dktj_employee_loan_detail WHERE date = para_data_date AND ln_org_code = para_org_code INTO sum_emp;
  SELECT sum(balance) FROM t_dktj_organization_loan_detail WHERE date = para_data_date AND ln_org_code = para_org_code INTO sum_org;
  SELECT sum(curr_dbt_bal) FROM t_hxsj_ledger_detail WHERE org_code = para_org_code AND date = para_data_date AND subject_no LIKE '12__' AND subject_no NOT LIKE '121%' AND subject_no NOT LIKE '122%' AND subject_no NOT LIKE '1289%' AND subject_no NOT LIKE '129_' INTO sum_report_form;
  IF sum_report_form IS NULL OR sum_org ISNULL OR sum_emp ISNULL OR sum_report_form != sum_emp OR sum_report_form != sum_org THEN
    RAISE NOTICE '贷款余额跑批计算结果与报表不相符，请检查，日期：%, 机构：%, 报表: %, 实际: %',para_data_date, para_org_code, sum_report_form, sum_emp;
  END IF;

  --10、判断利息计算的结果是否正确
  SELECT curr_day_ttl_crted_amt FROM t_hxsj_ledger_detail WHERE org_code = para_org_code AND date = para_data_date AND subject_no = '5011' INTO tmp_form_interest;
  SELECT sum(day_received_int) FROM t_dktj_organization_loan_detail WHERE date = para_data_date AND ln_org_code = para_org_code INTO tmp_real_interest;
  IF tmp_form_interest != tmp_real_interest THEN
      RAISE NOTICE '利息发生额不符, 日期: %, 机构: %, 报表5011发生额: %, 实际发生额: %',para_data_date,para_org_code,tmp_form_interest,tmp_real_interest;
  END IF;
  SELECT curr_crted_bal FROM t_hxsj_ledger_detail WHERE org_code = para_org_code AND date = para_data_date AND subject_no = '5011' INTO tmp_form_interest;
  SELECT sum(ttl_received_int) FROM t_dktj_organization_loan_detail WHERE date = para_data_date AND ln_org_code = para_org_code INTO tmp_real_interest;
  IF tmp_form_interest != tmp_real_interest THEN
      RAISE NOTICE '5011利息总支出不符, 日期: %, 机构: %, 报表5011余额: %, 实际余额: %',para_data_date,para_org_code,tmp_form_interest,tmp_real_interest;
  END IF;

  DROP TABLE IF EXISTS tmp_empln_detail;
  DROP TABLE IF EXISTS tmp_orgln_detail;
  RETURN TRUE;

  EXCEPTION
  WHEN OTHERS THEN
--     RAISE EXCEPTION '错误信息：%',SQLERRM;
    RAISE NOTICE '错误信息：%',SQLERRM;
    RETURN FALSE;

END;
$$ LANGUAGE plpgsql;


--服务5-柜员调动后释放客户服务
DROP FUNCTION IF EXISTS dktj_release_customer_after_employee_transfer(para_org_code VARCHAR, para_teller_code VARCHAR, para_date Date);
CREATE OR REPLACE FUNCTION dktj_release_customer_after_employee_transfer(para_org_code VARCHAR, para_teller_code VARCHAR, para_date Date)
  RETURNS BOOLEAN
AS $$
DECLARE
  tmp_user_id BIGINT;
  tmp_asi_id BIGINT;
  before_day Date;
  min_data_date Date;
  select_sql VARCHAR;
  r RECORD;
  user_org_count INTEGER;
  transfer_count INTEGER;
  tmp_count INTEGER;
BEGIN

  -- 1-判断柜员号是否正确
  SELECT id FROM t_sys_user WHERE code = para_teller_code INTO tmp_user_id;
  IF tmp_user_id ISNULL THEN
    RAISE EXCEPTION '错误的柜员号：%',para_teller_code;
  END IF;

  -- 2-判单数据日期是否到达（配合步骤3使用，否则步骤3可能出错，如：数据尚未导入，步骤3会不报错）
  SELECT MIN(import_date) FROM t_ods_data_handle_config WHERE id IN(50,51,153,154,157,163) INTO min_data_date;
  IF min_data_date < para_date THEN
    RAISE EXCEPTION '数据日期尚未到达%日，请先导入贷款数据',para_date;
  END IF;

  -- 3-判断柜员在职机构中间表是否生成（注意该表的第一天内容中的before_day_org_code 会是null，导致无法判断调动情况）注意：柜员是否调动要在第二日才能发现
  SELECT count(*) FROM t_middle_table_sys_user_organization WHERE date = para_date INTO user_org_count;
  IF user_org_count = 0 THEN
      RAISE EXCEPTION '柜员在职机构中间表t_middle_table_sys_user_organization尚未生成，日期: %',para_date;
  END IF;

  -- 5-判断是否发生了调动
  SELECT count(*) FROM t_middle_table_sys_user_organization WHERE date = para_date AND teller_code = para_teller_code AND before_day_org_code is not null and before_day_org_code != org_code AND status != '6' INTO transfer_count;

  -- 6-para_teller_code在before_date日发生了调动，释放在原机构的固定客户(para_date 是在新机构的起始日期) 注意：柜员before_day日在该org_code机构，发生了调动，则一定是调出了该机构
  IF transfer_count = 1 THEN

      -- 4-判断所有的账户是否已经复核完成
      SELECT count(*) FROM t_dktj_employee_customer WHERE start_date <= para_date AND teller_code = para_teller_code AND (register_check_status = '0' OR alter_check_status = '0') AND org_code = para_org_code INTO tmp_count;
      IF tmp_count > 0 THEN
          RAISE EXCEPTION '员工: % 机构: % 尚有 % 个账户未复核完成,调动释放账户服务无法继续执行',para_teller_code,para_org_code,tmp_count;
      END IF;

      select_sql := 'SELECT a.*,b.status FROM t_dktj_employee_customer a LEFT JOIN t_dktj_customer_status b ON a.xd_customer_no = b.xd_customer_no and a.org_code = b.org_code WHERE a.valid_flag = true AND a.start_date < $1 and b.valid_flag = true AND b.status = ''1'' AND a.teller_code = $2 and a.org_code = $3 and b.org_code = $3 and b.start_date < $1 and coalesce(b.end_date,''2099-12-31'') >= $1 ';
      before_day := para_date + INTERVAL '-1 day';

      --7、释放该员工在原来机构的固定客户：遍历t_dktj_employee_customer，释放该teller_code在该org_code的para_date之前的客户(柜员只在当前在职的机构有固定客户)
      FOR r IN EXECUTE select_sql USING para_date,para_teller_code,para_org_code LOOP
          --7.1 设置t_dktj_employee_customer表中原记录的终止时间和有效标志
          UPDATE t_dktj_employee_customer SET end_date = before_day, valid_flag = FALSE WHERE id = r.id;
          --7.2 在t_dktj_unbound_customer中插入该释放的客户
          INSERT INTO t_dktj_unbound_customer(xd_customer_no, account_no, account_open_date, customer_name, org_code, identity_type, identity_no, customer_type, date, create_time, flag) VALUES (r.xd_customer_no, r.account_no, r.account_open_date, r.customer_name, r.org_code, r.identity_type, r.identity_no, r.customer_type, para_date, now(), '2');
          --7.3 设置账户分成规则中的维护人的有效标志为false
--           SELECT asi.id FROM t_dktj_account_share_info asi left join t_dktj_account_template a on asi.account_template_id = a.id left join t_dktj_template_detail td on td.id = asi.template_detail_id left join t_dktj_position p on td.position_id = p.id where a.org_code = para_org_code and a.account_no = r.account_no and p.type = '1' AND asi.valid_flag = true INTO tmp_asi_id;
--           IF tmp_asi_id is NOT null THEN
--               UPDATE t_dktj_account_share_info SET end_date = before_day,valid_flag = FALSE WHERE id = tmp_asi_id;
--           END IF;

      END LOOP;
  END IF;

  RETURN TRUE;

  EXCEPTION
  WHEN OTHERS THEN
    RAISE EXCEPTION 'release_customer_after_employee_transfer的错误信息：%',SQLERRM;

END;
$$ LANGUAGE plpgsql;

-- 客户由流动转为固定状态时，释放部分账户，写入新状态    最后一个参数para_teller_id 为调用此函数的柜员的id
DROP FUNCTION IF EXISTS dktj_release_customer_after_change_status_to_fix(para_org_code VARCHAR, para_xd_customer_no VARCHAR, para_teller_id BIGINT);
CREATE OR REPLACE FUNCTION dktj_release_customer_after_change_status_to_fix(para_org_code VARCHAR, para_xd_customer_no VARCHAR, para_teller_id BIGINT)
    RETURNS BOOLEAN
AS $$
DECLARE
    before_day Date;
    select_sql VARCHAR;
    r RECORD;
    tmp_count INTEGER;
    tmp_status VARCHAR;
    para_date Date;
    tmp_org_code VARCHAR;
    tmp_id BIGINT;
    tmp_teller_name VARCHAR;
BEGIN

    SELECT current_date INTO para_date;
    before_day := para_date + INTERVAL '-1 day';

    IF para_teller_id IS NULL THEN
        RAISE EXCEPTION '未提供操作柜员的id';
    END IF;
    SELECT name FROM t_sys_user WHERE id = para_teller_id INTO tmp_teller_name;
    IF tmp_teller_name is null THEN
        RAISE EXCEPTION '通过提供的柜员id: % 不能查找柜员信息',para_teller_id;
    END IF;

    -- 1-判断当日是否已经发生过变更
    select status from t_dktj_customer_status where org_code = para_org_code and xd_customer_no = para_xd_customer_no and para_date = start_date into tmp_status;
    IF tmp_status IS NOT NULL THEN
        RAISE EXCEPTION '机构号: %, 客户: % 在 % 日已发生过变更，不允许再此变更',para_org_code,para_xd_customer_no,para_date;
    END IF;

    -- 2-判断当前状态是否已经为固定
    select status,id from t_dktj_customer_status where org_code = para_org_code and xd_customer_no = para_xd_customer_no and para_date >= start_date AND para_date <= coalesce(end_date,'2099-12-31') and valid_flag = TRUE into tmp_status,tmp_id;
    IF tmp_status ISNULL THEN
        RAISE EXCEPTION '无法获取到客户当前生效的状态';
    ELSEIF tmp_status = '1' THEN
        RAISE EXCEPTION '客户当前状态即为固定状态，无需变更';
    ELSE

    END IF;

    -- 3-判断所有的账户是否已经复核完成
    SELECT count(*) FROM t_dktj_employee_customer WHERE xd_customer_no = para_xd_customer_no AND (register_check_status = '0' OR alter_check_status = '0') AND org_code = para_org_code INTO tmp_count;
    IF tmp_count > 0 THEN
        RAISE EXCEPTION '信贷客户号: % 机构: % 尚有 % 个账户未复核完成,调动释放账户服务无法继续执行',para_xd_customer_no,para_org_code,tmp_count;
    END IF;

    -- 3-遍历客户当前拥有的、归属机构不在本机构的账户，释放归属柜员不在本机构的账户
    select_sql := 'select * from t_dktj_employee_customer where org_code = $1 and xd_customer_no = $2 and valid_flag = true';
    FOR r IN EXECUTE select_sql USING para_org_code,para_xd_customer_no LOOP
        SELECT get_org_code_by_teller_code(r.teller_code,para_date) INTO tmp_org_code;
        IF tmp_org_code ISNULL THEN
            RAISE EXCEPTION '无法通过柜员号: %, 日期: % 获取到柜员在职机构',r.teller_code,para_date;
        END IF;
        IF tmp_org_code != para_org_code THEN
            -- 3.1设置原记录的valid_flag为false
            UPDATE t_dktj_employee_customer SET end_date = before_day,valid_flag = FALSE WHERE id = r.id;
            -- 3.2在未绑定列表中写入该条记录
            INSERT INTO t_dktj_unbound_customer(xd_customer_no, account_no, account_open_date, customer_name, start_date, org_code, identity_type, identity_no, customer_type, date, create_time, flag) VALUES (r.xd_customer_no,r.account_no,r.account_open_date,r.customer_name,para_date,para_org_code,r.identity_type,r.identity_no,r.customer_type,para_date,now(),'3');
            -- 3.3设置账户分成规则中的维护人的有效标志为false
--             SELECT asi.id FROM t_dktj_account_share_info asi left join t_dktj_account_template a on asi.account_template_id = a.id left join t_dktj_template_detail td on td.id = asi.template_detail_id left join t_dktj_position p on td.position_id = p.id where a.org_code = para_org_code and a.account_no = r.account_no and p.type = '1' AND asi.valid_flag = true INTO tmp_asi_id;
--             IF tmp_asi_id is NOT null THEN
--                 UPDATE t_dktj_account_share_info SET end_date = before_day,valid_flag = FALSE,check_status='0' WHERE id = tmp_asi_id;
--             END IF;
        END IF;
    END LOOP;

    -- 4-设置原状态的valid_flag为false
    UPDATE t_dktj_customer_status SET valid_flag = FALSE,end_date = before_day WHERE id = tmp_id;

    -- 5-写入新状态
    INSERT INTO t_dktj_customer_status(org_code, xd_customer_no, status, start_date, end_date, parent_id, valid_flag, create_by, create_time, register_account_no, check_status) VALUES (para_org_code, para_xd_customer_no, '1', para_date, null, tmp_id, TRUE, para_teller_id, now(), NULL, '1');

    RETURN TRUE;

EXCEPTION
    WHEN OTHERS THEN
        RAISE EXCEPTION 'dktj_release_customer_after_change_status_to_fix：%',SQLERRM;

END;
$$ LANGUAGE plpgsql;


--柜员调动服务-按网点调用服务5
DROP FUNCTION IF EXISTS dktj_batch_release_customer_after_employee_transfer(para_org_code VARCHAR, para_date Date);
CREATE OR REPLACE FUNCTION dktj_batch_release_customer_after_employee_transfer(para_org_code VARCHAR, para_date Date)
  RETURNS BOOLEAN
AS $$
DECLARE
  r RECORD;
  select_sql VARCHAR;
  before_date Date;
  curr_date Date;
  tmp_result BOOLEAN;
  tmp_count INT;
  user_org_count INTEGER;
BEGIN

    before_date := para_date + INTERVAL '-1 day';

    -- 4-判断柜员在职机构中间表是否生成（注意该表的第一天内容中的before_day_org_code 会是null，导致无法判断调动情况）
    SELECT count(*) FROM t_middle_table_sys_user_organization WHERE date = para_date INTO user_org_count;
    IF user_org_count = 0 THEN
        RAISE EXCEPTION '柜员在职机构中间表t_middle_table_sys_user_organization尚未生成，日期: %',para_date;
    END IF;

  --判断para_date是否超过当前日期，para_date>=当前日期 显然是不合理的，柜员调动服务应该在某一日的日终之后，即第二日才能执行
  SELECT current_date INTO curr_date;
  IF para_date >= curr_date THEN
    RAISE EXCEPTION '参数日期大于或等于当前日期，柜员调动服务无法执行，参数日期：%，当前日期：%',para_date,curr_date;
  END IF;

  --判断para_date日，是否有柜员就职于汇总机构，即‘102’类型的机构，此时应该报错。
  SELECT count(*) FROM t_sys_user_organization uo LEFT JOIN t_sys_organization o ON uo.organization_id = o.id WHERE uo.status = '1' AND (o.type NOT IN ('201','103','104','105') OR  o.code = '78048') AND uo.start_date <= para_date AND coalesce(uo.end_date,'2099-09-09'::date) >= para_date INTO tmp_count;
  IF tmp_count = 0 THEN
    RAISE EXCEPTION '%日有柜员就职于78048或非201、103、104、105类型的机构',para_date;
  END IF;

  --遍历上一日在para_org_code机构的在职员工
  select_sql := 'select teller_code from t_middle_table_sys_user_organization where org_code = $1 and date = $2 and status != ''6'' ';
  FOR r IN EXECUTE select_sql USING para_org_code,before_date LOOP
    SELECT dktj_release_customer_after_employee_transfer(para_org_code,r.teller_code,para_date) INTO tmp_result;
    IF tmp_result = FALSE THEN
      RAISE EXCEPTION '调动服务调用时出错，请检查，柜员号：%',r.teller_code;
    END IF;
  END LOOP;

  --更新柜员调动跑批服务的配置信息
--   next_date := para_date + INTERVAL '1 day';
--   UPDATE t_dktj_loan_handle_config SET ln_curr_date = para_date,ln_next_date = next_date,update_time = now() WHERE org_code = para_org_code AND type = '2';
  RETURN TRUE;

  EXCEPTION
  WHEN OTHERS THEN
    RAISE EXCEPTION 'batch_release_customer的错误信息：%',SQLERRM;
END;
$$ LANGUAGE plpgsql;



--辅助服务，通过贷款账号、日期与机构号查找当时客户所属的员工
DROP FUNCTION IF EXISTS dktj_get_teller_code_by_account_no(para_account_no VARCHAR, para_date Date, para_org_code varchar);
CREATE OR REPLACE FUNCTION dktj_get_teller_code_by_account_no(para_account_no VARCHAR, para_date Date, para_org_code varchar)
RETURNS VARCHAR
AS $$
DECLARE
  r RECORD;
  select_sql VARCHAR;
  tmp_count INTEGER;
  tmp_teller_code VARCHAR;
  tmp_start_date Date;
  tmp_end_date Date;
BEGIN

  select_sql := 'SELECT account_no,start_date,end_date,teller_code FROM t_dktj_employee_customer WHERE account_no = $1 AND (register_check_status = ''1'' or alter_check_status = ''1'') and start_date <= $2 and org_code = $3 order by start_date asc';
  SELECT count(*) FROM t_dktj_employee_customer WHERE account_no = para_account_no AND (register_check_status = '1' OR alter_check_status = '1') INTO tmp_count;

  IF tmp_count > 0 THEN
    FOR r IN EXECUTE select_sql USING para_account_no,para_date,para_org_code LOOP
      IF r.start_date ISNULL THEN
        tmp_start_date := '0001-01-01'::date;
      ELSE
        tmp_start_date := r.start_date;
      END IF;

      IF r.end_date ISNULL THEN
        tmp_end_date := '9999-12-31';
      ELSE
        tmp_end_date := r.end_date;
      END IF;

      IF para_date >= tmp_start_date AND para_date <= tmp_end_date THEN
        tmp_teller_code := r.teller_code;
        EXIT;
      END IF;
    END LOOP;

--     IF tmp_teller_code ISNULL OR tmp_teller_code = '' THEN
--         select_sql := 'SELECT account_no,start_date,end_date,teller_code FROM t_dktj_employee_customer WHERE account_no = $1 AND (register_check_status = ''1'' or alter_check_status = ''1'') and start_date <= $2  order by start_date asc';
--         SELECT count(*) FROM t_dktj_employee_customer WHERE account_no = para_account_no AND (register_check_status = '1' OR alter_check_status = '1') INTO tmp_count;
--
--         IF tmp_count > 0 THEN
--             FOR r IN EXECUTE select_sql USING para_account_no,para_date LOOP
--                     IF r.start_date ISNULL THEN
--                         tmp_start_date := '0001-01-01'::date;
--                     ELSE
--                         tmp_start_date := r.start_date;
--                     END IF;
--
--                     IF r.end_date ISNULL THEN
--                         tmp_end_date := '9999-12-31';
--                     ELSE
--                         tmp_end_date := r.end_date;
--                     END IF;
--
--                     IF para_date >= tmp_start_date AND para_date <= tmp_end_date THEN
--                         tmp_teller_code := r.teller_code;
--                         EXIT;
--                     END IF;
--                 END LOOP;
--             END if;
--     END IF;

    IF tmp_teller_code ISNULL OR tmp_teller_code = '' THEN
      RAISE EXCEPTION '无法找到贷款账号: % 对应的员工，日期：%，机构：%',para_account_no,para_date,para_org_code;
    ELSE
      RETURN tmp_teller_code;
    END IF;
  ELSE
    RAISE EXCEPTION '无法找到贷款账号: % 对应的员工，日期：%，机构：%, 可能原因是绑定了员工但是未复核',para_account_no,para_date,para_org_code;
  END IF;

END;
$$ LANGUAGE plpgsql;


--辅助服务，批量绑定柜员调动(2)、流动转固定(3)释放的贷款客户到一个员工
DROP FUNCTION IF EXISTS dktj_batch_bind_employee_customer(para_date Date, para_org_code VARCHAR, para_teller_code VARCHAR);
CREATE OR REPLACE FUNCTION dktj_batch_bind_employee_customer(para_date Date, para_org_code VARCHAR, para_teller_code VARCHAR)
  RETURNS BOOLEAN
AS $$
DECLARE
  r RECORD;
  select_sql VARCHAR;
  tmp_org_code VARCHAR;
  tmp_status varchar;
  tmp_r Record;
  tmp_before_day date;
  tmp_id bigint;
BEGIN

  select_sql := 'select a.* from t_dktj_unbound_customer a  where a.date = $1 and a.org_code = $2 and flag in (''2'',''3'') ';

  -- 1、获取新柜员的当前在职机构
  SELECT get_org_code_by_teller_code(para_teller_code,para_date) INTO tmp_org_code;

  -- 2、遍历释放的未绑定账号
  FOR r IN EXECUTE select_sql USING para_date,para_org_code LOOP

    -- 3、查找当前的固定流动状态
    SELECT status FROM t_dktj_customer_status WHERE org_code = para_org_code AND valid_flag = true AND xd_customer_no = r.xd_customer_no INTO tmp_status;

    -- 4、判断新柜员在职机构是否为当前机构
    IF tmp_org_code != para_org_code THEN
        RAISE EXCEPTION '无法将固定客户绑定给非本机构的员工，客户所属机构：%, 员工所在机构：%',para_org_code,tmp_org_code;
    END IF;
    IF tmp_status != '1' THEN
        RAISE EXCEPTION '柜员调动释放的客户不为固定状态，信贷客户号：%', r.xd_customer_no;
    END IF;

    -- 5、查找该账号在释放前的维护人记录
    SELECT para_date + INTERVAL '-1 day' INTO tmp_before_day;
    SELECT id FROM t_dktj_employee_customer WHERE account_no = r.account_no AND valid_flag = FALSE AND end_date = tmp_before_day INTO tmp_id;
    IF tmp_id IS NULL THEN
        RAISE EXCEPTION '无法在t_dktj_employee_customer中查找到释放前的记录，账号: %',r.account_no;
    END IF;

    -- 6、在t_dktj_employee_customer表中插入记录
    INSERT INTO t_dktj_employee_customer(xd_customer_no, hx_customer_no, org_code, account_no, account_open_date, customer_name, customer_type, identity_type, identity_no, teller_code, start_date, end_date, valid_flag, register_type, register_check_status, alter_check_status, op_teller_code, create_time, register_check_teller_code, register_check_time, parent_id, bound_type, remarks) VALUES (r.xd_customer_no,null,r.org_code,r.account_no,r.account_open_date,r.customer_name,r.customer_type,r.identity_type,r.identity_no,para_teller_code,r.date,null,true,r.flag,'1',null,'admin',now(),'admin',now(),tmp_id,'2','批量绑定贷款客户到维护人');

    --7、对于柜员调动或流动转固定释放的客户，看是否有分成模板，如果有就修改分成模板中的维护人
    tmp_r := null;
    SELECT s.id,s.account_template_id,s.template_detail_id FROM t_dktj_account_share_info s LEFT JOIN t_dktj_account_template t ON s.account_template_id = t.id LEFT JOIN t_dktj_template_detail td ON td.id = s.template_detail_id WHERE t.account_no = r.account_no AND td.position_id = 1 AND s.valid_flag = TRUE AND t.valid_flag = true INTO tmp_r;
      IF tmp_r is not NULL THEN
          RAISE NOTICE 'tmp_r.id: %',tmp_r.id;
          UPDATE t_dktj_account_share_info SET valid_flag = FALSE,end_date = tmp_before_day,update_time=now(),update_by=1 WHERE id = tmp_r.id;
          INSERT INTO t_dktj_account_share_info(account_template_id, template_detail_id, teller_code, start_date, end_date, parent_id, valid_flag, check_status, remarks, create_time, create_by, alter_check_status, alter_check_teller, update_time, update_by) VALUES (tmp_r.account_template_id, tmp_r.template_detail_id, para_teller_code, r.date, null, tmp_r.id, TRUE, '1', '手工批量绑定柜员调动释放的客户', now(), 1, null, null, now(), 1);
      END IF;

    DELETE FROM t_dktj_unbound_customer WHERE id = r.id;

  END LOOP;

  RETURN TRUE;

  EXCEPTION
  WHEN OTHERS THEN
    RAISE EXCEPTION '错误信息：%',SQLERRM;
END;
$$ LANGUAGE plpgsql;


-- 执行单个网点的全部贷款跑批服务
DROP FUNCTION IF EXISTS dktj_single_org_batch_process(para_org_code varchar);
CREATE OR REPLACE FUNCTION dktj_single_org_batch_process(para_org_code varchar)
    RETURNS BOOLEAN
AS $$
DECLARE
    result1 BOOLEAN;
    result2 BOOLEAN;
    result4 BOOLEAN;
    result5 BOOLEAN;
    run_flag BOOLEAN;
    data_next_date date;
    batch_next_date date;
    compute_next_date date;
    tmp_count_1 integer;
    tmp_count_2 integer;
    tmp_ln_curr_date date;
BEGIN
    -- 1-获得下一个生成待绑定列表的时间
    SELECT ln_next_date FROM t_dktj_loan_handle_config where type='0' and org_code = para_org_code INTO data_next_date;

    -- 2-判断前一日的贷款账号是否都已经绑定完毕
--     SELECT dktj_have_uncheck_unbound_flag(data_next_date,para_org_code) INTO run_flag;
--     IF run_flag = false THEN
--         RAISE EXCEPTION '% 日(不含)之前，% 机构有未复核或未绑定的账户，请检查', data_next_date,para_org_code;
--     END IF;

    -- 3-执行以下服务：生成未绑定贷款账户列表、柜员调动释放客户服务、自动绑定账号到员工服务
    SELECT dktj_generate_unbound_customer(para_org_code, data_next_date) INTO result1;
    SELECT dktj_batch_release_customer_after_employee_transfer(para_org_code, data_next_date) INTO result2;

    RAISE NOTICE '贷款统计----生成未绑定列表服务跑批，网点：%，日期：%，结果：%',para_org_code, data_next_date, result1;
    RAISE NOTICE '贷款统计----柜员调动释放客户服务跑批，网点：%，日期：%，结果：%',para_org_code, data_next_date, result2;

    compute_next_date := data_next_date + INTERVAL '1 day';
    IF result1 = TRUE AND result2 = TRUE THEN
        UPDATE t_dktj_loan_handle_config SET ln_curr_date = data_next_date,ln_next_date = compute_next_date WHERE org_code = para_org_code AND type = '0';
    END IF;

    -- 4-贷款跑批
    -- 4.1 判断生成未绑定账号服务等是否已经执行
    SELECT ln_next_date FROM t_dktj_loan_handle_config WHERE type = '1' AND org_code = para_org_code INTO batch_next_date;
    SELECT ln_curr_date FROM t_dktj_loan_handle_config WHERE org_code = para_org_code AND type = '0' INTO tmp_ln_curr_date;
    IF tmp_ln_curr_date is null OR tmp_ln_curr_date < batch_next_date THEN
        RAISE NOTICE '尚未执行生成未绑定贷款列表、柜员调动服务，当前要执行的日期: %, 上述服务执行到的日期: %',batch_next_date, tmp_ln_curr_date;
        RETURN TRUE;
    END IF;

    -- 4.2 判断是否已经绑定完毕、复核完毕
    SELECT count(*) FROM t_dktj_unbound_customer WHERE org_code = para_org_code AND date <= batch_next_date INTO tmp_count_1;
    IF tmp_count_1 > 0 THEN
        RAISE NOTICE '机构 % 截止 % 日尚有 % 个客户未绑定',para_org_code,batch_next_date,tmp_count_1;
        RETURN TRUE;
    END IF;
    SELECT count(*) FROM t_dktj_employee_customer WHERE org_code = para_org_code AND (register_check_status = '0' OR alter_check_status = '0') AND start_date <= batch_next_date INTO tmp_count_2;
    IF tmp_count_2 > 0 THEN
        RAISE NOTICE '机构 % 尚有 % 个客户未复核',para_org_code,tmp_count_2;
        RETURN TRUE;
    END IF;

    -- 4.3 计算利息（单一服务，会自行更新配置表）
    SELECT ln_next_date FROM t_dktj_loan_handle_config WHERE type = '6' AND org_code = para_org_code INTO data_next_date;
    SELECT dktj_fun_generate_account_interest_record(para_org_code,data_next_date) INTO result4;
    RAISE NOTICE '计算贷款利息跑批，网点：%，日期：%，结果：%',para_org_code, data_next_date,result4;
    IF result4 = FALSE THEN
        RETURN FALSE;
    END IF;

    -- 4.4 计算利息成功时，执行跑批（单一服务，会自行更新配置表）
--     SELECT dktj_calculate_loan_detail(para_org_code, batch_next_date) INTO result5;
--     RAISE NOTICE '贷款余额和日均跑批，网点：%，日期：%，结果：%',para_org_code, batch_next_date,result5;


    RETURN TRUE;
    EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE '错误信息：%',SQLERRM;
            RETURN FALSE;
END;
$$ LANGUAGE plpgsql;


--执行全部网点的贷款跑批服务（执行生成未绑定、利息跑批）
DROP FUNCTION IF EXISTS dktj_all_org_batch_process();
CREATE OR REPLACE FUNCTION dktj_all_org_batch_process()
    RETURNS BOOLEAN
AS $$
DECLARE
    result BOOLEAN;
    select_sql varchar;
    r RECORD;
    lock_id integer := 123456;
BEGIN


    PERFORM pg_advisory_lock(lock_id);
    PERFORM fun_middle_generate_sys_user_organization();
    PERFORM fun_middle_generate_five_class();
    select_sql := 'select code from t_sys_organization where code like ''780__'' and code != ''78009'' and code != ''78001'' and code != ''78048'' and code != ''78041'' ';

    FOR r IN EXECUTE select_sql LOOP
        SELECT dktj_single_org_batch_process(r.code) INTO result;
    END LOOP;
    PERFORM pg_advisory_unlock(lock_id);

    RAISE NOTICE '%',now();
    RETURN TRUE;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE '错误信息：%',SQLERRM;
        PERFORM pg_advisory_unlock(lock_id);
        RETURN FALSE;
END;
$$ LANGUAGE plpgsql;



--执行全部网点的贷款跑批服务(仅执行余额跑批)
DROP FUNCTION IF EXISTS dktj_all_org_batch_batch_process();
CREATE OR REPLACE FUNCTION dktj_all_org_batch_batch_process()
    RETURNS BOOLEAN
AS $$
DECLARE
    result BOOLEAN;
    select_sql varchar;
    r RECORD;
    unbound_curr_date date;
    batch_next_date date;
    interest_curr_date date;
BEGIN

    select_sql := 'select code from t_sys_organization where code like ''780__'' and code != ''78009'' and code != ''78001'' and code != ''78048'' and code != ''78041'' ';

    FOR r IN EXECUTE select_sql LOOP
            SELECT ln_curr_date FROM t_dktj_loan_handle_config WHERE type = '0' AND org_code = r.code INTO unbound_curr_date;
            SELECT ln_next_date FROM t_dktj_loan_handle_config WHERE type = '1' AND org_code = r.code INTO batch_next_date;
            SELECT ln_curr_date FROM t_dktj_loan_handle_config WHERE type = '6' AND org_code = r.code INTO interest_curr_date;
            IF batch_next_date <= unbound_curr_date AND batch_next_date <= interest_curr_date THEN
                SELECT dktj_calculate_loan_detail(r.code, batch_next_date) INTO result;
                RAISE NOTICE '贷款余额和日均跑批，网点：%，日期：%，结果：%',r.code, batch_next_date,result;
            END IF;
    END LOOP;

    RETURN TRUE;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE '错误信息：%',SQLERRM;
        RETURN FALSE;
END;
$$ LANGUAGE plpgsql;


--执行全部网点的贷款利息跑批服务（贷款利息收入分配到账号）
DROP FUNCTION IF EXISTS dktj_all_org_batch_interest_process();
CREATE OR REPLACE FUNCTION dktj_all_org_batch_interest_process()
    RETURNS BOOLEAN
AS $$
DECLARE
    result BOOLEAN;
    select_sql varchar;
    r RECORD;
    unbound_curr_date date;
    data_next_date date;
BEGIN

    select_sql := 'select code from t_sys_organization where code like ''780__'' and code != ''78009'' and code != ''78001'' and code != ''78048'' and code != ''78041'' ';

    FOR r IN EXECUTE select_sql LOOP
            SELECT ln_curr_date FROM t_dktj_loan_handle_config WHERE type = '0' AND org_code = r.code INTO unbound_curr_date;
            SELECT ln_next_date FROM t_dktj_loan_handle_config WHERE type = '6' AND org_code = r.code INTO data_next_date;
            IF data_next_date <= unbound_curr_date THEN
                SELECT dktj_fun_generate_account_interest_record(r.code,data_next_date) INTO result;
                RAISE NOTICE '计算贷款利息跑批，网点：%，日期：%，结果：%',r.code, data_next_date,result;
            ELSE
                RAISE NOTICE '利息跑批时，未绑定日期>利息日期，网点： %, 未绑定日期： %, 利息日期: %',r.code,unbound_curr_date,data_next_date;
            END IF;
        END LOOP;

    RETURN TRUE;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE '错误信息：%',SQLERRM;
        RETURN FALSE;
END;
$$ LANGUAGE plpgsql;


-- 单个网点贷款跑批服务（余额跑批）
DROP FUNCTION IF EXISTS dktj_single_org_batch_batch_process(para_org_code varchar);
CREATE OR REPLACE FUNCTION dktj_single_org_batch_batch_process(para_org_code varchar)
    RETURNS BOOLEAN
AS $$
DECLARE
    result BOOLEAN;
    unbound_curr_date date;
    batch_next_date date;
    interest_curr_date date;
    tmp_days integer;
    tmp_date date;
BEGIN

    SELECT ln_curr_date FROM t_dktj_loan_handle_config WHERE type = '0' AND org_code = para_org_code INTO unbound_curr_date;
    SELECT ln_next_date FROM t_dktj_loan_handle_config WHERE type = '1' AND org_code = para_org_code INTO batch_next_date;
    SELECT ln_curr_date FROM t_dktj_loan_handle_config WHERE type = '6' AND org_code = para_org_code INTO interest_curr_date;

    IF unbound_curr_date IS NULL THEN
        RAISE EXCEPTION '网点号不存在: %',para_org_code;
    END IF;

    IF unbound_curr_date <= interest_curr_date THEN
        tmp_date = unbound_curr_date;
    ELSE
        tmp_date = interest_curr_date;
    END IF;

    SELECT tmp_date - batch_next_date + 1 INTO tmp_days;
    FOR i IN 1..tmp_days LOOP
            SELECT ln_next_date FROM t_dktj_loan_handle_config WHERE type = '1' AND org_code = para_org_code INTO batch_next_date;
            IF batch_next_date <= unbound_curr_date AND batch_next_date <= interest_curr_date THEN
                SELECT dktj_calculate_loan_detail(para_org_code, batch_next_date) INTO result;
                RAISE NOTICE '贷款余额和日均跑批，网点：%，日期：%，结果：%',para_org_code, batch_next_date,result;
                IF result = false THEN
                    RETURN FALSE;
                END IF;
            END IF;
        END LOOP;

    RETURN TRUE;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE '错误信息：%',SQLERRM;
        RETURN FALSE;
END;
$$ LANGUAGE plpgsql;


-- 计算贷款利息收入到账号
DROP FUNCTION if EXISTS dktj_fun_generate_account_interest_record(para_org_code varchar, para_date date);
CREATE OR REPLACE FUNCTION dktj_fun_generate_account_interest_record(para_org_code varchar, para_date date)
    RETURNS Boolean
AS $$
DECLARE
    select_sql varchar;
    select_sql2 varchar;
    r RECORD;
    count1 integer;
    tmp_account varchar;
    r2 RECORD;
    tmp_subject_no varchar;
    tmp_tax_bal numeric(17,2);
    bal1 numeric(17,2);
    diff_bal numeric(17,2);
    rate numeric(10,6);
    sum_bal numeric(17,2);
    max_id BIGINT;
    count2 integer;
    lnfm02_balance numeric(17,2);
    sum_report_form numeric(17,2);
    ignore_count integer;
    select_sql3 varchar;
    orig_subject_no varchar;
    data_date date;
BEGIN

    SELECT min(import_date) FROM t_ods_data_handle_config WHERE table_name IN ('t_hxsj_deposit_detail','t_ods_t05_loan_dtl_inf','t_hxsj_ledger_detail') INTO data_date;
    IF data_date < para_date THEN
        RAISE NOTICE '以下表的数据尚未到达:t_hxsj_deposit_detail、t_ods_t05_loan_dtl_inf';
    END IF;

    -- 1-删除旧数据
    DELETE FROM t_dktj_account_interest WHERE org_code = para_org_code AND date = para_date;

    -- 2-将在dpfm30表中直接有贷款账号的5011记录入表
    INSERT INTO t_dktj_account_interest(date, orig_tx_no, child_tx_no, orig_tx_code, summary_code, summary, org_code, account_no, tax_int, tx_org_code, tx_teller, create_time, acct_flag, reg_teller, reg_time, subject_no) SELECT tx_date, orig_tx_no, child_tx_no, orig_tx_code, summary_code, summary, para_org_code, other_side_acct_no, tx_amt, tx_org_code, tx_teller, now(), '1', null, null,substring(account_no FROM 8 FOR 8) FROM t_hxsj_deposit_detail WHERE tx_date = para_date AND account_no LIKE concat(para_org_code,'015011%') AND other_side_acct_no LIKE'780__01100%';

    select_sql := 'select account_no,child_account_no,tx_date,orig_tx_no,child_tx_no,tx_amt,deb_cred_flag,balance,tx_org_code,orig_tx_code,tx_teller,other_side_acct_no,other_side_child_acct_no,summary_code,summary,open_acct_org_code from t_hxsj_deposit_detail where tx_date = $1 and account_no like $2 and (other_side_acct_no not like $3 or other_side_acct_no is null) and orig_tx_code != ''462B07'' ';
    select_sql2 := 'select tax_int,account_no from t_dktj_account_interest where date = $1 and org_code = $2 and subject_no like $3 and orig_tx_code != ''462B07'' ';
    select_sql3 := 'select account_no,child_account_no,tx_date,orig_tx_no,child_tx_no,tx_amt,deb_cred_flag,balance,tx_org_code,orig_tx_code,tx_teller,other_side_acct_no,other_side_child_acct_no,summary_code,summary,open_acct_org_code from t_hxsj_deposit_detail where tx_date = $1 and account_no like $2 and (other_side_acct_no not like $3 or other_side_acct_no is null) and orig_tx_code = ''462B07'' ';

    -- 3-遍历直接没有贷款账号的dpfm30表的5011记录(非价税分离的记录)
    FOR r IN EXECUTE select_sql USING para_date,concat(para_org_code,'015011%'),'780__01100%' LOOP
            --20240524日机构合并，处理特殊情况(一正一负抵消，无需处理)
            IF r.orig_tx_no = '161596' AND para_date = '2024-05-24' THEN
                CONTINUE ;
            END IF;
            IF r.orig_tx_no = '160361' AND para_date = '2024-10-19' THEN
                CONTINUE ;
            END IF;
            -- 3.1 忽略一些正负数抵消的记录
            SELECT count(*) FROM t_dktj_ignore_account_interest WHERE orig_tx_no = r.orig_tx_no AND child_tx_no = r.child_tx_no AND date = r.tx_date INTO ignore_count;
            IF ignore_count > 0 THEN
                CONTINUE ;
            END IF;

            -- 3.6 忽略年终结转损益
            IF r.summary = '年终结转损益' THEN
                CONTINUE ;
            END IF;

            tmp_account := NULL;
            -- 获取科目号
            SELECT substring(r.account_no FROM 8 FOR 8) INTO tmp_subject_no;
            -- 3.2 核销收回
            IF r.orig_tx_code = '562100' THEN
                SELECT count(a.loan_account) FROM t_ods_t05_loan_repyint_dtl_inf a LEFT JOIN t_ods_t04_cr_core_inst_ctrt_h b ON a.dept_code = b.ori_stm_ins_id WHERE b.core_ins_no = r.tx_org_code AND a.repay_date = r.tx_date AND a.interest = r.tx_amt INTO count1;

                IF count1 = 1 THEN
                    SELECT a.loan_account FROM t_ods_t05_loan_repyint_dtl_inf a LEFT JOIN t_ods_t04_cr_core_inst_ctrt_h b ON a.dept_code = b.ori_stm_ins_id WHERE b.core_ins_no = r.tx_org_code AND a.repay_date = r.tx_date AND a.interest = r.tx_amt INTO tmp_account;
                END IF;

                IF tmp_account ISNULL THEN
                    SELECT count(account_no) FROM t_ods_s01_lnfm10_history_interest WHERE tx_date = r.tx_date AND orig_tx_no = ''||r.orig_tx_no AND account_no LIKE '780__01100%' INTO count1;
                    SELECT DISTINCT account_no FROM t_ods_s01_lnfm10_history_interest WHERE tx_date = r.tx_date AND orig_tx_no = ''||r.orig_tx_no AND account_no LIKE '780__01100%' LIMIT 1 INTO tmp_account;
                END IF;
            END IF;

            -- 3.3 手工贷款收回
            IF r.orig_tx_code = '562150' THEN
                SELECT count(a.loan_account) FROM t_ods_t05_loan_repyint_dtl_inf a LEFT JOIN t_ods_t04_cr_core_inst_ctrt_h b ON a.dept_code = b.ori_stm_ins_id WHERE b.core_ins_no = r.tx_org_code AND a.repay_date = r.tx_date AND a.interest = r.tx_amt INTO count1;

                IF count1 = 1 THEN
                    SELECT a.loan_account FROM t_ods_t05_loan_repyint_dtl_inf a LEFT JOIN t_ods_t04_cr_core_inst_ctrt_h b ON a.dept_code = b.ori_stm_ins_id WHERE b.core_ins_no = r.tx_org_code AND a.repay_date = r.tx_date AND a.interest = r.tx_amt INTO tmp_account;
                END IF;

                IF tmp_account IS NULL THEN
                    SELECT count(DISTINCT account_no) FROM t_ods_t05_loan_dtl_inf WHERE orig_tx_no = r.orig_tx_no AND tx_date = para_date AND account_no LIKE '780__0110%' INTO count1;
                    IF count1 = 1 THEN
                        SELECT DISTINCT account_no FROM t_ods_t05_loan_dtl_inf WHERE orig_tx_no = r.orig_tx_no AND tx_date = para_date AND account_no LIKE '780__0110%' INTO tmp_account;
                    END IF;
                END IF;

                IF tmp_account ISNULL THEN
                    SELECT count(account_no) FROM t_ods_s01_lnfm10_history_interest WHERE tx_date = r.tx_date AND orig_tx_no = ''||r.orig_tx_no AND account_no LIKE '780__01100%' INTO count1;
                    SELECT DISTINCT account_no FROM t_ods_s01_lnfm10_history_interest WHERE tx_date = r.tx_date AND orig_tx_no = ''||r.orig_tx_no AND account_no LIKE '780__01100%' LIMIT 1 INTO tmp_account;
                END IF;
            END IF;

            -- 3.4 非价税分离、非核销收回、非手工贷款收回的记录，直接在dpfm30表的该笔交易中查找贷款账号
            IF r.orig_tx_code != '462B07' AND r.orig_tx_code != '562100' AND r.orig_tx_code != '562150'  THEN
                SELECT count(DISTINCT other_side_acct_no) FROM t_hxsj_deposit_detail WHERE orig_tx_no = r.orig_tx_no AND tx_date = r.tx_date AND other_side_acct_no LIKE concat('780__','0110%') INTO count1;
                IF count1 = 1 THEN
                    SELECT DISTINCT other_side_acct_no FROM t_hxsj_deposit_detail WHERE orig_tx_no = r.orig_tx_no AND tx_date = r.tx_date AND other_side_acct_no LIKE concat('780__','0110%') INTO tmp_account;
                else
                    tmp_account := null;
                    IF para_date <= '2020-03-31' THEN
                        SELECT DISTINCT account_no FROM t_ods_s01_lnfm10_history_interest WHERE orig_tx_no = ''||r.orig_tx_no AND tx_date = r.tx_date AND account_no LIKE concat(para_org_code,'0110%') INTO tmp_account;
                    END IF;
                END IF;

            END IF;

            -- 3.5 跨法人机构还贷款
            IF tmp_account ISNULL AND r.orig_tx_code = '562025' THEN
                SELECT count(DISTINCT account_no) FROM t_ods_t05_loan_dtl_inf WHERE orig_tx_no = r.orig_tx_no AND tx_date = r.tx_date INTO count1;

                IF count1 = 1 THEN
                    SELECT DISTINCT account_no FROM t_ods_t05_loan_dtl_inf WHERE orig_tx_no = r.orig_tx_no AND tx_date = r.tx_date INTO tmp_account;
                END IF;
            END IF;



            -- 判断是否是手工收息的情况，如果手工收息，需要手动添加记录到t_dktj_manual_account_interest表中
            IF tmp_account ISNULL THEN
                SELECT account_no FROM t_dktj_manual_account_interest WHERE date = r.tx_date AND orig_tx_no = r.orig_tx_no AND child_tx_no = r.child_tx_no AND orig_tx_code = r.orig_tx_code INTO tmp_account;
            END IF;
            IF tmp_account ISNULL THEN
                RAISE EXCEPTION '无法找到该笔5011利息收入对应的贷款账号，交易日期: %, 流水号: %, 子流水号: %',r.tx_date,r.orig_tx_no,r.child_tx_no;
            ELSE
                INSERT INTO t_dktj_account_interest(date, orig_tx_no, child_tx_no, orig_tx_code, summary_code, summary, org_code, account_no, tax_int, tx_org_code, tx_teller, create_time, acct_flag, subject_no ) VALUES (r.tx_date, r.orig_tx_no, r.child_tx_no, r.orig_tx_code, r.summary_code, r.summary, r.open_acct_org_code, tmp_account, r.tx_amt, r.tx_org_code, r.tx_teller, now(), '1', tmp_subject_no);
            END IF;

        END LOOP;

    -- 4-价税分离的记录
    FOR r IN EXECUTE select_sql3 USING para_date,concat(para_org_code,'015011%'),'780__01100%' LOOP

            SELECT count(*) FROM t_dktj_ignore_account_interest WHERE orig_tx_no = r.orig_tx_no AND child_tx_no = r.child_tx_no AND date = r.tx_date INTO ignore_count;
            IF ignore_count > 0 THEN
                CONTINUE ;
            END IF;

            -- 4.1 判断价税分离的金额是正数（正数-贷款利息减免导致的税回冲为利息 负数-正常价税分离）
            SELECT count(*) FROM t_dktj_positive_interest_split WHERE orig_tx_no = r.orig_tx_no AND child_tx_no = r.child_tx_no AND date = r.tx_date INTO count2;
            IF r.tx_amt > 1000 AND (count2 < 1) THEN
                SELECT count(*) FROM t_dktj_manual_account_interest WHERE orig_tx_no = r.orig_tx_no AND child_tx_no = r.child_tx_no AND date = r.tx_date INTO count2;
                IF count2 != 1 THEN
                    RAISE EXCEPTION '价税分离为正数的记录对应的贷款账号没有找到(或找到多条),请查找t_dktj_manual_account_interest中是否有以下记录, orig_tx_no: %, child_tx_no: %, date: %',r.orig_tx_no,r.child_tx_no,r.tx_date;
                END IF;
                SELECT account_no FROM t_dktj_manual_account_interest WHERE orig_tx_no = r.orig_tx_no AND child_tx_no = r.child_tx_no AND date = r.tx_date INTO tmp_account;
                INSERT INTO t_dktj_account_interest(date, orig_tx_no, child_tx_no, orig_tx_code, summary_code, summary, org_code, account_no, tax_int, tx_org_code, tx_teller, create_time, acct_flag) VALUES (r.tx_date, r.orig_tx_no, r.child_tx_no, r.orig_tx_code, r.summary_code, r.summary, r.open_acct_org_code, tmp_account, r.tx_amt, r.tx_org_code, r.tx_teller, now(), '1');

            -- 4.2 判断价税分离是负数
            ELSE
                -- 4.2.1 获取科目号
                SELECT substring(r.account_no FROM 8 FOR 8) INTO tmp_subject_no;
                orig_subject_no := tmp_subject_no;

                -- 4.2.2 计算比例
                SELECT sum(tx_amt) FROM t_hxsj_deposit_detail WHERE tx_date = para_date AND account_no LIKE concat(para_org_code,'01',tmp_subject_no,'%')  AND orig_tx_code != '462B07' INTO sum_bal;
                IF sum_bal IS NULL OR sum_bal = 0 THEN
                    RAISE NOTICE '计算价税分离时，未找到 % 科目有对应的贷款账号,将在全部5011收息贷款账号上均摊 日期： %, 机构： %, 金额: %, 流水号: %',tmp_subject_no,para_date,para_org_code,r.tx_amt,r.orig_tx_no;
                    --不存在该5011科目的贷款利息收入，向全部有5011贷款利息收入的账号均摊（价税分离一般情况下是从5011的发生额上提取）
                    tmp_subject_no := '5011';
                    SELECT sum(tax_int) FROM t_dktj_account_interest WHERE date = para_date AND org_code = para_org_code AND orig_tx_code != '462B07' INTO sum_bal;
                END IF;
                SELECT round(r.tx_amt/sum_bal,6) INTO rate;

                -- 4.2.3 将该科目的贷款利息收入分摊
                FOR r2 IN EXECUTE select_sql2 USING para_date,para_org_code,concat(tmp_subject_no,'%') LOOP
                        SELECT round(r2.tax_int * rate,2) INTO tmp_tax_bal;
                        INSERT INTO t_dktj_account_interest(date, orig_tx_no, child_tx_no, orig_tx_code, summary_code, summary, org_code, account_no, tax_int, tx_org_code, tx_teller, create_time, acct_flag, subject_no) VALUES (r.tx_date, r.orig_tx_no, r.child_tx_no, r.orig_tx_code, r.summary_code, r.summary, para_org_code, r2.account_no, tmp_tax_bal, r.tx_org_code, r.tx_teller, now(), '1', orig_subject_no);
                    END LOOP;

                -- 4.2.4 保留小数位会产生误差，修正该误差，放在单笔金额最大的贷款利息收入上
                SELECT coalesce(sum(tax_int),0) FROM t_dktj_account_interest WHERE orig_tx_no = r.orig_tx_no AND child_tx_no = r.child_tx_no AND date = para_date AND org_code = para_org_code INTO bal1;
                diff_bal := r.tx_amt - bal1;
                SELECT id FROM t_dktj_account_interest WHERE orig_tx_no = r.orig_tx_no AND child_tx_no = r.child_tx_no AND date = para_date AND org_code = para_org_code ORDER BY tax_int DESC LIMIT 1 INTO max_id;
                -- 4.2.5 修正该科目上有贷款利息收入的记录
                IF max_id IS NOT NULL THEN
                    UPDATE t_dktj_account_interest SET tax_int = tax_int + diff_bal WHERE id = max_id;
                -- 4.2.6 修正该科目上无贷款利息收入的记录
                else
                    SELECT max(id) FROM t_dktj_account_interest WHERE orig_tx_code = '462B07' AND date = para_date and org_code = para_org_code INTO max_id;
                    UPDATE t_dktj_account_interest SET tax_int = tax_int + diff_bal WHERE id = max_id;
                END IF;
            END IF;
            -- 4.3 校验明细是否和总账相符
            SELECT coalesce(sum(tax_int),0) FROM t_dktj_account_interest WHERE date = r.tx_date AND orig_tx_no = r.orig_tx_no AND child_tx_no = r.child_tx_no AND org_code = para_org_code INTO sum_bal;
            IF sum_bal != r.tx_amt THEN
                RAISE EXCEPTION '价税分离时，拆分出来的明细值与5011发生明细不符，5011发生明细的发生额: %, 拆分明细总和: %, orig_tx_no: %, child_tx_no: %, tx_date: %',r.tx_amt,sum_bal,r.orig_tx_no,r.child_tx_no,r.tx_date;
            END IF;

        END LOOP;

    -- 5. 校验明细与报表是否相符
    SELECT sum(tax_int) FROM t_dktj_account_interest WHERE date = para_date AND  org_code = para_org_code INTO lnfm02_balance;
    SELECT sum(curr_day_ttl_crted_amt) FROM t_hxsj_ledger_detail WHERE date = para_date AND  org_code = para_org_code AND subject_no ='5011' INTO sum_report_form;
    IF lnfm02_balance != sum_report_form THEN
        IF para_date = '2024-10-19' AND para_org_code = '78043' THEN
            RAISE NOTICE '% 日, % 机构的5011报表发生额与计算总和不符, t_dktj_account_interest计算值: %, 报表值: %, 差额: %',para_date,para_org_code,lnfm02_balance,sum_report_form,lnfm02_balance-sum_report_form;
        ELSE
            RAISE EXCEPTION '% 日, % 机构的5011报表发生额与计算总和不符, t_dktj_account_interest计算值: %, 报表值: %, 差额: %',para_date,para_org_code,lnfm02_balance,sum_report_form,lnfm02_balance-sum_report_form;
        END IF;

    END IF;

    -- 6. 更新配置表
    UPDATE t_dktj_loan_handle_config SET ln_next_date = (para_date + INTERVAL '1 day'),ln_curr_date = para_date WHERE org_code = para_org_code AND type = '6';

    RETURN TRUE;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE '错误信息：%',SQLERRM;
        RETURN FALSE;
end;
$$ LANGUAGE plpgsql;


-- 通过t_sys_user_organization生成柜员每日的在职机构
DROP FUNCTION IF EXISTS fun_middle_generate_sys_user_organization();
CREATE OR REPLACE FUNCTION fun_middle_generate_sys_user_organization()
    RETURNS Boolean
AS $$
DECLARE
    select_sql varchar;
    select_sql1 varchar;
    select_sql2 varchar;
    r RECORD;
    r1 RECORD;
    r2 RECORD;
    r3 RECORD;
    para_date date;
    tmp_end_date date;
    data_next_date date;
    data_before_date date;
    before_date_count integer;
    curr_date date;
    new_date date;
BEGIN

    --1- 判断中间配置表中是否可以计算该中间表
    SELECT * FROM t_middle_table_handle_config WHERE table_name = 't_middle_table_sys_user_organization' INTO r;
    IF r.del_flag THEN
        RAISE NOTICE '配置表中有以下配置： del_flag:true，即：停止生成t_middle_table_sys_user_organization';
    END IF;

    para_date := r.next_date;

    -- 2- 判断参数日期是否小于当前日期
    SELECT current_date INTO curr_date;
    IF para_date >= curr_date THEN
        RAISE NOTICE '参数日期大于或等于当前日期，无法生成柜员在职机构。参数日期：%，当前日期：%',para_date,curr_date;
        RETURN FALSE;
    END IF;

    -- 3- 判断前一日的数据是否生成
    data_before_date := para_date + INTERVAL '-1 day';
    SELECT count(*) FROM t_middle_table_sys_user_organization WHERE date = data_before_date INTO before_date_count;
    IF before_date_count < 1 THEN
        RAISE NOTICE '%日的数据未生成，%日数据的before_day_org_code将为空，请注意',data_before_date,para_date;
    END IF;

    -- 4- 删除原日期的全部记录
    DELETE FROM t_middle_table_sys_user_organization WHERE date = para_date;

    -- 5- 遍历t_sys_user表中所有不同的柜员号-非揽储身份 status != '6'
    select_sql := 'select id,code,name from t_sys_user where del_flag = false';
    FOR r2 IN EXECUTE select_sql LOOP
        select_sql2 := 'SELECT uo.organization_id,o.code,uo.start_date,uo.end_date,uo.status FROM t_sys_user_organization uo LEFT JOIN t_sys_organization o on uo.organization_id = o.id WHERE uo.user_id = $1 AND uo.del_flag = false and uo.status != ''6'' order by uo.start_date desc';
        tmp_end_date := '2099-12-31';
        FOR r3 IN EXECUTE select_sql2 USING r2.id LOOP
            --判断para_date所属的区间段，如果找到区间段就证明柜员在该机构
            IF para_date >= r3.start_date AND para_date < tmp_end_date THEN
                INSERT INTO t_middle_table_sys_user_organization(teller_code, teller_name, user_id, organization_id, org_code, status, start_date, date, import_time) VALUES (r2.code,r2.name,r2.id,r3.organization_id,r3.code,r3.status,r3.start_date,para_date,now());
            END IF;
            tmp_end_date := r3.start_date;
        END LOOP;
    END LOOP;

    -- 6- 遍历t_sys_user表中所有不同的柜员号-揽储身份status=6
    FOR r2 IN EXECUTE select_sql LOOP
        -- 一个柜员可以同时在多个机构揽储，所以需要遍历各个机构
        select_sql1 := 'select distinct id from t_sys_organization';
        FOR r1 IN EXECUTE select_sql1 LOOP
            --查找para_date所属的时间区间段
            select_sql2 := 'SELECT uo.organization_id,o.code,uo.start_date,uo.end_date,uo.status FROM t_sys_user_organization uo LEFT JOIN t_sys_organization o on uo.organization_id = o.id WHERE uo.user_id = $1 AND uo.del_flag = false and uo.status = ''6'' and uo.organization_id = $2 order by uo.start_date desc';

            tmp_end_date := '2099-12-31';
            FOR r3 IN EXECUTE select_sql2 USING r2.id,r1.id LOOP
                IF para_date >= r3.start_date AND para_date < tmp_end_date AND para_date <= coalesce(r3.end_date,'2099-12-31'::date) THEN
                    INSERT INTO t_middle_table_sys_user_organization(teller_code, teller_name, user_id, organization_id, org_code, status, start_date, date, import_time) VALUES (r2.code,r2.name,r2.id,r3.organization_id,r3.code,r3.status,r3.start_date,para_date,now());
                END IF;
                tmp_end_date := r3.start_date;
            END LOOP;
        END LOOP;
    END LOOP;

    -- 7- 更新before_day_org_code字段
    select_sql := 'select id,user_id,organization_id,status from t_middle_table_sys_user_organization where date = $1 and status != ''6'' ';
    FOR r IN EXECUTE select_sql USING para_date LOOP

        -- 7.1获取到昨日的所在机构（一个员工，除了‘6’以外的状态，其他状态每日只可能在一个机构。即一个员工可能以揽储身份调入了几个机构，但是只可能在一个机构在职，而且只可能有一个非‘6’的机构存在）
        SELECT org_code FROM t_middle_table_sys_user_organization WHERE date = data_before_date AND user_id = r.user_id AND status != '6' INTO r2;
        IF r2 is NOT NULL THEN
            UPDATE t_middle_table_sys_user_organization SET before_day_org_code = r2.org_code WHERE id = r.id;
        END IF;

        -- 7.2判断是否当日新增的员工，更新is_new字段
        SELECT min(start_date) FROM t_sys_user_organization WHERE user_id = r.user_id INTO new_date;
        IF new_date = para_date THEN
            UPDATE t_middle_table_sys_user_organization SET is_new = TRUE WHERE id = r.id;
        END IF;
    END LOOP;

    data_next_date := para_date + INTERVAL '1 day';
    UPDATE t_middle_table_handle_config SET import_date=para_date,next_date=data_next_date WHERE table_name = 't_middle_table_sys_user_organization';

    RAISE NOTICE '生成 % 日的柜员所属机构成功',para_date;
    RETURN TRUE;
END;
$$ LANGUAGE plpgsql;



-- 通过信贷客户号、机构号和日期获取用户在该机构的固定流动状态
DROP FUNCTION IF EXISTS dktj_get_customer_status_by_xd_customer_no(para_xd_customer_no VARCHAR, para_date Date, para_org_code varchar);
CREATE OR REPLACE FUNCTION dktj_get_customer_status_by_xd_customer_no(para_xd_customer_no VARCHAR, para_date Date, para_org_code varchar)
    RETURNS VARCHAR
AS $$
DECLARE
    r RECORD;
    select_sql VARCHAR;
    tmp_status VARCHAR;
    tmp_start_date Date;
    tmp_end_date Date;
BEGIN

    select_sql := 'SELECT xd_customer_no,start_date,end_date,status FROM t_dktj_customer_status WHERE xd_customer_no = $1 AND start_date <= $2 and org_code = $3 order by start_date asc';

        FOR r IN EXECUTE select_sql USING para_xd_customer_no,para_date,para_org_code LOOP
                IF r.start_date ISNULL THEN
                    tmp_start_date := '0001-01-01'::date;
                ELSE
                    tmp_start_date := r.start_date;
                END IF;

                IF r.end_date ISNULL THEN
                    tmp_end_date := '9999-12-31';
                ELSE
                    tmp_end_date := r.end_date;
                END IF;

                IF para_date >= tmp_start_date AND para_date <= tmp_end_date THEN
                    tmp_status := r.status;
                    EXIT;
                END IF;
            END LOOP;

        IF tmp_status ISNULL OR tmp_status = '' THEN
            RAISE EXCEPTION '无法找到固定/流动状态，日期：%，机构：%, 信贷客户号: %',para_date,para_org_code,para_xd_customer_no;
        END IF;

    RETURN tmp_status;
END;
$$ LANGUAGE plpgsql;



--循环执行网点的贷款余额跑批服务
DROP FUNCTION IF EXISTS dktj_batch_process(para_count int);
CREATE OR REPLACE FUNCTION dktj_batch_process(para_count int)
    RETURNS BOOLEAN
AS $$
DECLARE
    i integer;
BEGIN

    FOR i IN 1..para_count LOOP
            PERFORM dktj_all_org_batch_batch_process();
        END LOOP;

    RETURN TRUE;
END;
$$ LANGUAGE plpgsql;



--校验5011利息收入与报表是否相等
DROP FUNCTION IF EXISTS check_5011_detail(in_begin_date date,in_end_date date);
CREATE OR REPLACE FUNCTION check_5011_detail(in_begin_date date,in_end_date date)
    RETURNS BOOLEAN
AS $$
DECLARE
    tmp_date DATE;
    days INTEGER;
    i INTEGER;
    counter1 NUMERIC(17,2);
    counter2 NUMERIC(17,2);
    select_sql VARCHAR;
BEGIN

    IF in_end_date::date < in_begin_date::date THEN
        RETURN FALSE ;
    END IF;

    tmp_date := in_begin_date;
    i := 1;
    select_sql := 'select org_code from tmp_org_code';
    SELECT in_end_date::date - in_begin_date::date + 1 INTO days;

    FOR i IN 1..days LOOP

            SELECT curr_day_ttl_crted_amt FROM t_hxsj_ledger_detail WHERE date = tmp_date AND org_code = '78000' AND subject_no = '5011' INTO counter1;
            SELECT sum(tax_int) FROM t_dktj_account_interest WHERE date = tmp_date INTO counter2 ;

            IF counter1 != counter2 THEN
                RAISE NOTICE '%日的5011不正确，请检查，总账：%, 实际明细：%, 差额: %',tmp_date,counter1,counter2,counter1-counter2;
            ELSE
                RAISE NOTICE '%日正确',tmp_date;
            END IF;
            tmp_date := tmp_date + INTERVAL '1 day';
    END LOOP;

    RETURN TRUE;
END;
$$ LANGUAGE plpgsql;

--校验5011利息收入与报表是否相等
DROP FUNCTION IF EXISTS check_5011_detail_by_org(in_begin_date date,in_end_date date);
CREATE OR REPLACE FUNCTION check_5011_detail_by_org(in_begin_date date,in_end_date date)
    RETURNS BOOLEAN
AS $$
DECLARE
    tmp_date DATE;
    days INTEGER;
    i INTEGER;
    counter1 NUMERIC(17,2);
    counter2 NUMERIC(17,2);
    select_sql VARCHAR;
    r RECORD;
BEGIN

    IF in_end_date::date < in_begin_date::date THEN
        RETURN FALSE ;
    END IF;

    tmp_date := in_begin_date;
    i := 1;
    select_sql := 'select org_code from tmp_org_code';
    SELECT in_end_date::date - in_begin_date::date + 1 INTO days;

    FOR i IN 1..days LOOP

        FOR r IN EXECUTE select_sql LOOP
            SELECT curr_day_ttl_crted_amt FROM t_hxsj_ledger_detail WHERE date = tmp_date AND org_code = r.org_code AND subject_no = '5011' INTO counter1;
            SELECT sum(day_received_int) FROM t_dktj_organization_loan_detail WHERE date = tmp_date AND ln_org_code = r.org_code INTO counter2 ;

            IF counter1 != counter2 THEN
                RAISE NOTICE '%日 % 机构的5011不正确，请检查，总账：%, 实际明细：%, 差额: %',tmp_date,r.org_code,counter1,counter2,counter1-counter2;
            ELSE
                RAISE NOTICE '%日正确',tmp_date;
            END IF;
        END LOOP;
        tmp_date := tmp_date + INTERVAL '1 day';
    END LOOP;

    RETURN TRUE;
END;
$$ LANGUAGE plpgsql;


--通过信贷客户号与机构号获取一笔新贷款最有可能的维护人
DROP FUNCTION IF EXISTS fun_dktj_get_most_possible_teller_code(para_xd_customer_no VARCHAR, para_org_code VARCHAR);
CREATE OR REPLACE FUNCTION fun_dktj_get_most_possible_teller_code(para_xd_customer_no VARCHAR, para_org_code VARCHAR)
    RETURNS VARCHAR
AS $$
DECLARE
    tmp_teller_code VARCHAR;
BEGIN

    SELECT teller_code FROM bps_78000.t_dktj_employee_customer WHERE account_open_date = (SELECT max(account_open_date) FROM bps_78000.t_dktj_employee_customer WHERE xd_customer_no = para_xd_customer_no) AND xd_customer_no = para_xd_customer_no AND org_code = para_org_code LIMIT 1 INTO tmp_teller_code;

    RETURN tmp_teller_code;
END;
$$ LANGUAGE plpgsql;


-- 获取某一笔贷款的上一笔贷款（机构相同）的岗位责任人
DROP FUNCTION IF EXISTS bps_78000.fun_dktj_get_most_possible_position_teller_code(para_account_no VARCHAR, para_template_id BIGINT, para_position_id BIGINT);
CREATE OR REPLACE FUNCTION bps_78000.fun_dktj_get_most_possible_position_teller_code(para_account_no VARCHAR, para_template_id BIGINT, para_position_id BIGINT)
    RETURNS VARCHAR
AS $$
DECLARE
    tmp_teller_code VARCHAR;
    tmp_customer_no VARCHAR;
    tmp_customer_no2 VARCHAR;
    tmp_account_no VARCHAR;
    tmp_org_code VARCHAR;
    tmp_org_code2 VARCHAR;
    tmp_account_template_id BIGINT;
    tmp_template_detail_id BIGINT;
BEGIN

    -- 1-获取信贷客户号
    SELECT xd_customer_no,org_code FROM bps_78000.t_dktj_unbound_customer WHERE account_no = para_account_no LIMIT 1 INTO tmp_customer_no,tmp_org_code;
    IF tmp_customer_no is null THEN
        SELECT xd_customer_no,org_code FROM bps_78000.t_dktj_unbound_employee_interest WHERE account_no = para_account_no LIMIT 1 INTO tmp_customer_no2,tmp_org_code2;
        IF tmp_customer_no2 ISNULL THEN
            RETURN NULL;
        Else
            tmp_customer_no := tmp_customer_no2;
            tmp_org_code := tmp_org_code2;
        END IF;
    END IF;

    RAISE NOTICE '信贷客户号: %',tmp_customer_no;

    -- 2-获取最后一笔贷款的账号
    SELECT max(a.account_no) FROM bps_78000.t_dktj_employee_customer a LEFT JOIN bps_78000.t_dktj_account_template b on a.account_no = b.account_no AND a.org_code = b.org_code WHERE a.xd_customer_no = tmp_customer_no AND a.org_code = tmp_org_code AND b.id IS NOT NULL INTO tmp_account_no;
    RAISE NOTICE '贷款账号: %',tmp_account_no;

    -- 3-获取最后一笔贷款对应的模板、岗位信息
    SELECT id FROM bps_78000.t_dktj_account_template WHERE account_no = tmp_account_no AND template_id = para_template_id AND valid_flag = TRUE INTO tmp_account_template_id;
    RAISE NOTICE 'account_template_id: %',tmp_account_template_id;
    SELECT id FROM bps_78000.t_dktj_template_detail WHERE template_id = para_template_id AND position_id = para_position_id INTO tmp_template_detail_id;
    RAISE NOTICE 'tmp_template_detail_id: %',tmp_template_detail_id;

    -- 4-返回最后一笔贷款对应某一模板、岗位的责任人
    SELECT teller_code FROM bps_78000.t_dktj_account_share_info WHERE account_template_id = tmp_account_template_id AND template_detail_id = tmp_template_detail_id AND valid_flag = TRUE INTO tmp_teller_code;

    RETURN tmp_teller_code;
END;
$$ LANGUAGE plpgsql;


-- 按分成规则将利息分到各个相关人（前提条件是利息已经分到账户，即t_dktj_account_interest内容已经生成）
DROP FUNCTION IF EXISTS fun_dktj_calculate_loan_interest_detail(para_org_code VARCHAR, para_data_date DATE);
CREATE OR REPLACE FUNCTION fun_dktj_calculate_loan_interest_detail(para_org_code VARCHAR, para_data_date DATE)
    RETURNS BOOLEAN
AS $$
DECLARE
    select_sql VARCHAR;
    tmp_teller_org_code VARCHAR;
    r RECORD;
    r2 RECORD;
    sum_tax_int numeric(17,2);
    tmp_form_interest numeric;
    tmp_real_interest numeric;
    tmp_template_id BIGINT;
    tmp_account_template_id BIGINT;
    tmp_id BIGINT;
    tmp_position_id BIGINT;
    tmp_percentage numeric;
    tmp_xd_customer_no VARCHAR;
    tmp_curr_date date;
    unbound_curr_date date;
    unbound_cnt int;
BEGIN

    -- 1-判断利息计算的结果是否正确
    SELECT curr_day_ttl_crted_amt FROM t_hxsj_ledger_detail WHERE org_code = para_org_code AND date = para_data_date AND subject_no = '5011' INTO tmp_form_interest;
    SELECT sum(tax_int) FROM t_dktj_account_interest WHERE date = para_data_date AND org_code = para_org_code INTO tmp_real_interest;
    IF tmp_form_interest != tmp_real_interest THEN
        RAISE NOTICE '利息发生额不符, 日期: %, 报表5011发生额: %, 实际发生额: %',para_data_date,tmp_form_interest,tmp_real_interest;
    END IF;

    -- 2-判断模板下的岗位分成之和是否为1
    select_sql := 'SELECT template_id,sum(percentage) percent FROM t_dktj_template_detail GROUP BY template_id ';
    FOR r IN EXECUTE select_sql LOOP
        IF r.percent != 1 THEN
            RAISE EXCEPTION '模板下的岗位分成之和不为1, 模板id: %',r.template_id;
        END IF;
    END LOOP;

    -- 3-判断利息跑批服务是否已经执行完毕
    SELECT ln_curr_date FROM t_dktj_loan_handle_config WHERE type = '6' AND org_code = para_org_code INTO tmp_curr_date;
    IF para_data_date > tmp_curr_date THEN
        RAISE EXCEPTION '利息跑批服务尚未执行，利息跑批的当前日期为: %, 要执行的日期是: %',tmp_curr_date,para_data_date;
    END IF;

    -- 4-判断是否绑定完毕，否则无贷款模板，无法按比例分成
    SELECT ln_curr_date FROM t_dktj_loan_handle_config WHERE org_code = para_org_code AND type = '0' INTO unbound_curr_date;
    IF para_data_date > unbound_curr_date THEN
        RAISE NOTICE '贷款未绑定服务才跑批到 % 日，当前要执行的利息日期: %',unbound_curr_date,para_data_date;
        RETURN FALSE;
    END IF;
    SELECT count(*) FROM t_dktj_unbound_customer WHERE date = para_data_date AND org_code = para_org_code INTO unbound_cnt;
    IF unbound_cnt > 0 THEN
        RAISE NOTICE '存在尚未绑定的贷款账号, 日期: %',para_data_date;
        RETURN FALSE;
    END IF;

    -- 5-判断是否有变更未复核的岗位责任人
    SELECT count(*) FROM t_dktj_account_share_info s LEFT JOIN t_dktj_account_template dat ON s.account_template_id = dat.id WHERE s.alter_check_status = '0' AND dat.org_code = para_org_code AND s.start_date <= para_data_date INTO unbound_cnt;
    IF unbound_cnt > 0 THEN
        RAISE NOTICE '存在 % 个尚未复核的岗位责任人变更, 日期: %，机构: %',unbound_cnt,para_data_date,para_org_code;
        RETURN FALSE;
    END IF;


    -- 删除原来的记录
    DELETE FROM t_dktj_employee_interest_share WHERE ln_org_code = para_org_code AND date = para_data_date;

    -- 3-按分成比例分成利息到柜员
    select_sql := 'SELECT org_code,account_no,sum(tax_int) balance FROM t_dktj_account_interest WHERE date = $1 AND org_code = $2 GROUP BY org_code,account_no ';
    FOR r IN EXECUTE select_sql USING para_data_date,para_org_code LOOP
        -- 通过账号获取模板信息和分成信息
        SELECT id,template_id FROM t_dktj_account_template WHERE account_no = r.account_no AND para_data_date >= start_date AND para_data_date <= coalesce(end_date,'2099-12-31'::date) AND org_code = para_org_code INTO tmp_account_template_id,tmp_template_id;
        IF tmp_account_template_id IS NULL OR tmp_template_id is NULL THEN
            RAISE EXCEPTION '通过贷款账号: % 查找模板信息失败, 日期: %, 机构: %',r.account_no,para_data_date,para_org_code;
        END IF;
        FOR r2 IN SELECT * FROM t_dktj_account_share_info WHERE account_template_id = tmp_account_template_id AND para_data_date >= start_date AND para_data_date <= coalesce(end_date,'2099-12-31'::date) LOOP
            -- 获取分成比例
            SELECT position_id,percentage FROM t_dktj_template_detail WHERE id = r2.template_detail_id INTO tmp_position_id,tmp_percentage;

            -- 获取柜员的在职机构
            SELECT get_org_code_by_teller_code(r2.teller_code, para_data_date) INTO tmp_teller_org_code;
            IF tmp_teller_org_code IS NULL THEN
                RAISE EXCEPTION '计算利息分成时,无法通过账号: %, 柜员号: % 获取到在职机构, 日期: %, 机构: %',r.account_no,r2.teller_code,para_data_date,para_org_code;
            END IF;
            -- 获取信贷客户号
            SELECT cust_id FROM t_ods_t03_loancrd_inf_h WHERE loan_account = r.account_no LIMIT 1 INTO tmp_xd_customer_no;
            IF tmp_xd_customer_no ISNULL THEN
                RAISE EXCEPTION '计算利息分成时,无法通过账号: % 获取到信贷客户号, 日期: %, 机构: %',r.account_no,para_data_date,para_org_code;
            END IF;
            -- 写入数据库
            INSERT INTO t_dktj_employee_interest_share(account_no, teller_code, date, balance, ln_org_code, belong_org_code, account_template_id, template_id, template_detail_id, position_id, percentage, xd_customer_no, create_time) VALUES (r.account_no, r2.teller_code, para_data_date, round(r.balance*tmp_percentage,2), r.org_code, tmp_teller_org_code, tmp_account_template_id, tmp_template_id, r2.template_detail_id, tmp_position_id, tmp_percentage, tmp_xd_customer_no, now());
        END LOOP;

        --修正四舍五入导致的误差
        SELECT sum(balance) FROM t_dktj_employee_interest_share WHERE date = para_data_date AND ln_org_code = para_org_code AND account_no = r.account_no INTO sum_tax_int;
        IF sum_tax_int != r.balance THEN
            SELECT id FROM t_dktj_employee_interest_share WHERE date = para_data_date AND ln_org_code = para_org_code AND account_no = r.account_no LIMIT 1 INTO tmp_id;
            UPDATE t_dktj_employee_interest_share SET balance = balance + (r.balance - sum_tax_int) WHERE id = tmp_id;
        END IF;

    END LOOP;

    --校验
    SELECT sum(balance) from t_dktj_employee_interest_share where date = para_data_date AND ln_org_code = para_org_code INTO tmp_real_interest;
    IF tmp_real_interest != tmp_form_interest THEN
--         RAISE EXCEPTION '结果不符，请检查程序';
        RAISE NOTICE '结果不符，请检查程序';
    END IF;

    RETURN TRUE;

EXCEPTION
    WHEN OTHERS THEN
        RAISE EXCEPTION '错误信息：%',SQLERRM;
END;
$$ LANGUAGE plpgsql;


--批量绑定待补录账户到某一个员工（模板为“全给推荐人模板”）
DROP FUNCTION IF EXISTS fun_test(para_org_code VARCHAR, para_teller_code VARCHAR);
CREATE OR REPLACE FUNCTION fun_test(para_org_code VARCHAR, para_teller_code VARCHAR)
    RETURNS BOOLEAN
AS $$
DECLARE
    select_sql VARCHAR;
    r record;
    tmp_template_id INT;
BEGIN

    select_sql := 'select account_no,org_code,account_open_date from t_dktj_unbound_employee_interest where org_code = $1 group by account_no,org_code,account_open_date';
    FOR r IN EXECUTE select_sql USING para_org_code LOOP
        INSERT INTO t_dktj_account_template(account_no, template_id, start_date, end_date, parent_id, valid_flag, remarks, create_time, create_by, update_time, update_by, org_code, check_status) VALUES (r.account_no, 3, r.account_open_date, null, null, true, '批量登记', now(), 1, null, null, r.org_code, '1');
        SELECT id FROM t_dktj_account_template WHERE account_no = r.account_no AND org_code = r.org_code AND valid_flag = true INTO tmp_template_id;

        INSERT INTO t_dktj_account_share_info(account_template_id, template_detail_id, teller_code, start_date, end_date, parent_id, valid_flag, remarks, create_time, create_by, update_time, update_by) VALUES (tmp_template_id, 10, para_teller_code, r.account_open_date, null, null, true, null, now(), 1, null, null);
    END LOOP;

    RETURN TRUE;

EXCEPTION
    WHEN OTHERS THEN
        RAISE EXCEPTION '错误信息：%',SQLERRM;
END;
$$ LANGUAGE plpgsql;



-- 将贷款利息计算到人的时候，可能会遇到没有模板的贷款账号，在这里新增补录
insert into t_dktj_unbound_employee_interest(account_no,xd_customer_no,account_open_date,customer_name,org_code,identity_type,identity_no,customer_type) select loan_account,cust_id,start_date,c_name,lnfm02.org_code,cust_id_type,cust_id_no,case when cust_id like 'P%' then '1' when cust_id like 'C%' then '2' end customer_type from t_ods_t03_loancrd_inf_h a left join (select account_no,org_code from t_ods_s01_lnfm02 where child_account_no in ('01','02','03','04') and date = '2024-01-01' group by account_no,org_code ) lnfm02 on lnfm02.account_no = a.loan_account where a.date = '2024-01-01' and loan_account = '7804501100000000154006';

-- 将贷款利息计算到人时候，可能会遇到没有绑定过的贷款账号，在这里新增绑定
INSERT INTO t_dktj_unbound_customer(xd_customer_no, account_no, account_open_date, customer_name, start_date, org_code, identity_type, identity_no, date, customer_type, create_time, flag) VALUES ('P0100000782807 ', '7803101100000000948126', '2024-04-29', '刘海龙', '20240523', '78007', 'CD0100032000000010', '152728199611250616', '20240523', '1', now(), '1');

insert into t_dktj_customer_status(org_code, xd_customer_no, status, start_date, end_date, parent_id, valid_flag, create_by, create_time, register_account_no, check_status) VALUES ('78024','P01Y2Y55752398', '1', '20210816',null, null, true, 1, now(), '7802401100000001757178','1');
