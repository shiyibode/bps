-- 生成未绑定到员工的存款账号列表
DROP FUNCTION IF EXISTS cktj_generate_unbound_customer(para_org_code varchar, para_date Date);
CREATE OR REPLACE FUNCTION cktj_generate_unbound_customer(para_org_code varchar, para_date Date)
    RETURNS BOOLEAN
AS $$
DECLARE
    dpfm02_data_count INTEGER;
    next_date Date;
    r RECORD;
    select_sql varchar;
BEGIN

    SELECT count(*) FROM bps_78000.t_ods_hxsj_deposit_account_detail WHERE date = para_date INTO dpfm02_data_count;

    -- 1-判断当日的数据是否已经具备
    IF dpfm02_data_count < 1 THEN
        RAISE EXCEPTION '%日的数据尚未导入：存款分户信息表，bps_78000.t_ods_hxsj_deposit_account_detail表',para_date;
    END IF;

    -- 2-删除未绑定列表中原始的数据
    DELETE FROM bps_78000.t_cktj_unbound_account WHERE date = para_date AND org_code = para_org_code;

    -- 3-筛选出未绑定的账号
    select_sql := 'select dpfm02.account_no,dpfm02.open_date,dpfm02.account_type,ca.card_no,dpfm02.before_day_balance,cua.customer_no,coalesce(c.name,dpfm03.account_name) as customer_name,dpfm02.org_code,c.identity_type,c.identity_no,c.type as customer_type from bps_78000.t_ods_hxsj_deposit_account_detail dpfm02 left join (select account_no,max(card_no) as card_no from bps_78000.t_ods_hxsj_card_account group by account_no) as ca on dpfm02.account_no = substring(ca.account_no from 1 for 22) left join bps_78000.t_ods_hxsj_customer_account cua on cua.account_no = coalesce(ca.card_no,dpfm02.account_no) left join t_ods_hxsj_customer_public_base c on c.no = cua.customer_no left join t_ods_hxsj_internal_account dpfm03 on dpfm02.account_no = dpfm03.account_no where dpfm02.date = $1 and dpfm02.subject_no in (select unnest(subject_no) from t_cktj_deposit_category) and dpfm02.close_flag != ''1'' and dpfm02.org_code = $2 and dpfm02.account_no not in (select account_no from t_cktj_unbound_account) and dpfm02.account_no not in (select account_no from t_cktj_employee_account) and (dpfm02.child_account_no is null or child_account_no = ''000000'' ) ';
    FOR r IN EXECUTE select_sql USING para_date,para_org_code LOOP
        -- 4-处理cifm02表中没有客户信息的记录（后续出现这样的情况，还在这里处理）
        IF r.account_type = '1' AND r.customer_name is NULL THEN
                RAISE EXCEPTION '外部户在cifm02表中找不到客户信息, 账号: %, 客户号: %, 日期: %, 机构: %',r.account_no,r.customer_no,para_date,para_org_code;
        END IF;
        -- 5-插入到未绑定列表中
        IF r.account_type != '1' OR r.customer_name IS NOT NULL THEN
            INSERT INTO bps_78000.t_cktj_unbound_account(customer_no, account_no, account_type, card_no, open_date, customer_name, org_code, identity_type, identity_no, customer_type, date, create_time) VALUES (r.customer_no, r.account_no, r.account_type, r.card_no, r.open_date, r.customer_name, r.org_code, r.identity_type, r.identity_no, r.customer_type, para_date, now());
        END IF;

    END LOOP;

    -- 7-修改配置表
    next_date := para_date + INTERVAL '1 day';
    UPDATE bps_78000.t_cktj_deposit_handle_config SET dp_curr_date=para_date,dp_next_date=next_date,update_time=now() WHERE org_code = para_org_code AND type = '0';
    RETURN TRUE;

EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE '错误信息：%',SQLERRM;
        Return false;
END;
$$ LANGUAGE plpgsql;


-- 通过账户查找揽储人
-- DROP FUNCTION IF EXISTS cktj_get_teller_code_by_account_no(account_no VARCHAR, data_date DATE);
-- CREATE OR REPLACE FUNCTION cktj_get_teller_code_by_account_no(account_no VARCHAR, data_date DATE)
--     RETURNS VARCHAR
-- AS $$
-- DECLARE
--     sql VARCHAR;
--     teller_code VARCHAR;
-- BEGIN
--     sql := 'SELECT teller_code FROM t_cktj_employee_account WHERE account_no = $1 AND $2 BETWEEN start_date AND COALESCE(end_date, ''2999-12-31''::DATE)';
--     EXECUTE sql INTO teller_code USING account_no, data_date;
--     IF teller_code ISNULL OR teller_code = '' THEN
--         RAISE EXCEPTION '无法通过账号: %, 日期: % 找到该账号的归属柜员',account_no,data_date;
--     END IF;
--     RETURN teller_code;
-- END;
-- $$ LANGUAGE plpgsql;

-- 通过柜员号和日期查找柜员在职机构
DROP FUNCTION IF EXISTS get_org_code_by_teller_code(teller_code VARCHAR, data_date DATE);
CREATE OR REPLACE FUNCTION get_org_code_by_teller_code(teller_code VARCHAR, data_date DATE)
    RETURNS VARCHAR
AS $$
DECLARE
    sql VARCHAR;
    org_code VARCHAR;
BEGIN
    sql := 'SELECT o.code FROM public.t_sys_user_organization a LEFT JOIN public.t_sys_organization o ON o.id = a.organization_id LEFT JOIN public.t_sys_user u on u.id = a.user_id ' ||
           'WHERE u.code = $1 AND $2 BETWEEN a.start_date AND COALESCE(a.end_date, ''2999-12-31''::DATE)';
    EXECUTE sql INTO org_code USING teller_code, data_date;
    IF org_code IS NULL OR org_code = '' THEN
        RAISE EXCEPTION '通过柜员号: %, 日期: % 无法找到员工的在职机构',teller_code,data_date;
    END IF;
    RETURN org_code;
END;
$$ LANGUAGE plpgsql;


-- 通过科目号查找存款分类
DROP FUNCTION IF EXISTS cktj_get_deposit_category_by_subject_no(in_subject_no VARCHAR, in_customer_type VARCHAR);
CREATE OR REPLACE FUNCTION cktj_get_deposit_category_by_subject_no(in_subject_no VARCHAR, in_customer_type VARCHAR)
    RETURNS SETOF t_cktj_deposit_category AS
$$
DECLARE
    tmp_count integer;
BEGIN

    -- 特殊情况判断
    if in_subject_no = '211101' and in_customer_type = '2' then
        in_customer_type = '1';
    end if;

    -- 正常情况
    IF in_customer_type ISNULL THEN
        SELECT count(*) FROM t_cktj_deposit_category WHERE subject_no @> CAST(string_to_array(in_subject_no, ',') AS VARCHAR[]) AND customer_type ISNULL INTO tmp_count;
        IF tmp_count = 0 THEN
            RAISE EXCEPTION '通过科目号: %, 客户类型: % 找不到存款分类',in_subject_no,in_customer_type;
        ELSEIF tmp_count > 1 THEN
            RAISE EXCEPTION '通过科目号: %, 客户类型: % 找到多个存款分类',in_subject_no,in_customer_type;
        ELSE
            RETURN QUERY (SELECT * FROM t_cktj_deposit_category WHERE subject_no @> CAST(string_to_array(in_subject_no, ',') AS VARCHAR[]) AND customer_type ISNULL );
        END IF;
    ELSE
        SELECT count(*) FROM t_cktj_deposit_category WHERE subject_no @> CAST(string_to_array(in_subject_no, ',') AS VARCHAR[]) AND customer_type = in_customer_type INTO tmp_count;
        IF tmp_count = 0 THEN
            RAISE EXCEPTION '通过科目号: %, 客户类型: % 找不到存款分类',in_subject_no,in_customer_type;
        ELSEIF tmp_count > 1 THEN
            RAISE EXCEPTION '通过科目号: %, 客户类型: % 找到多个存款分类',in_subject_no,in_customer_type;
        ELSE
            RETURN QUERY (SELECT * FROM t_cktj_deposit_category WHERE subject_no @> CAST(string_to_array(in_subject_no, ',') AS VARCHAR[]) AND customer_type = in_customer_type );
        END IF;
    END IF;

EXCEPTION
    WHEN OTHERS THEN
        RAISE EXCEPTION '存储过程 get_deposit_category_by_subject_no 执行错误: %', SQLERRM;
END;
$$ LANGUAGE plpgsql;


-- 通过账号和账号类型获取客户类型
DROP FUNCTION IF EXISTS cktj_get_customer_type(para_account_no VARCHAR, para_account_type VARCHAR);
CREATE OR REPLACE FUNCTION cktj_get_customer_type(para_account_no VARCHAR, para_account_type VARCHAR)
    RETURNS VARCHAR
AS $$
DECLARE
    tmp_customer_type VARCHAR;
    tmp_card_no varchar;
    tmp_customer_no varchar;
BEGIN
    -- 1-内部户的客户类型为null
    IF para_account_type = '2' THEN
        RETURN NULL;
    END IF;

    -- 2-外部户
    SELECT card_no FROM bps_78000.t_ods_hxsj_card_account WHERE substring(account_no from 1 for 22) = para_account_no LIMIT 1 INTO tmp_card_no ;
    SELECT customer_no FROM bps_78000.t_ods_hxsj_customer_account WHERE account_no = coalesce(tmp_card_no,para_account_no) INTO tmp_customer_no;
    IF tmp_customer_no IS NULL THEN
        RAISE EXCEPTION '无法通过账号: % 获取到客户号',para_account_no;
    END IF;
    SELECT type FROM bps_78000.t_ods_hxsj_customer_public_base WHERE no = tmp_customer_no INTO tmp_customer_type;

    IF tmp_customer_type IS NULL THEN
            RAISE EXCEPTION '无法通过客户号: % 找到客户类型，请检查S01_CIFM02表',tmp_customer_no;
    ELSE
        RETURN tmp_customer_type;
    END IF;
END;
$$ LANGUAGE plpgsql;


-- 单个网点存款跑批-任务数和计酬数同时跑批
DROP FUNCTION IF EXISTS cktj_calculate_deposit_detail(para_org_code VARCHAR, para_data_date DATE);
CREATE OR REPLACE FUNCTION cktj_calculate_deposit_detail(para_org_code VARCHAR, para_data_date DATE)
    RETURNS BOOLEAN AS
$$
DECLARE
    select_sql TEXT;
    insert_empdp_task_sql TEXT;
    insert_empdp_payment_sql TEXT;
    insert_orgdp_task_sql TEXT;
    rs RECORD;
    ea_record RECORD;
    customer_type VARCHAR;
    dp_category RECORD;
    tmp_teller_code VARCHAR;
    teller_org_code VARCHAR;
    tmp_count_1 int;
    tmp_count_2 int;
    tmp_dp_curr_date date;
    date_dpfm02 date;
    next_date date;
    tmp_cnt integer;
BEGIN

    tmp_count_1 := 0;
    SELECT count(*) FROM bps_78000.t_cktj_unbound_account WHERE org_code = para_org_code AND date <= para_data_date INTO tmp_count_1;
    IF tmp_count_1 > 0 THEN
        RAISE EXCEPTION '机构 % 尚有 % 个账户未绑定',para_org_code,tmp_count_1;
    END IF;

    tmp_count_2 := 0;
    SELECT count(*) FROM bps_78000.t_cktj_employee_account_task a  WHERE a.org_code = para_org_code AND (a.register_check_status = 'CHECKED_STATUS_UNCHECKED' OR a.alter_check_status = 'CHECKED_STATUS_UNCHECKED') AND a.start_date <= para_data_date INTO tmp_count_2;
    IF tmp_count_2 > 0 THEN
        RAISE EXCEPTION '机构 % 尚有 % 个账户未复核-任务',para_org_code,tmp_count_2;
    END IF;

    tmp_count_2 := 0;
    SELECT count(*) FROM bps_78000.t_cktj_employee_account_payment a  WHERE a.org_code = para_org_code AND (a.register_check_status = '0' OR a.alter_check_status = '0') AND a.start_date <= para_data_date INTO tmp_count_2;
    IF tmp_count_2 > 0 THEN
        RAISE EXCEPTION '机构 % 尚有 % 个账户未复核-计酬',para_org_code,tmp_count_2;
    END IF;

    SELECT dp_curr_date FROM bps_78000.t_cktj_deposit_handle_config WHERE org_code = para_org_code AND type = '0' INTO tmp_dp_curr_date;
    IF tmp_dp_curr_date ISNULL THEN tmp_dp_curr_date := '1900-01-01'::date;  END IF;
    IF tmp_dp_curr_date < para_data_date THEN
        RAISE EXCEPTION '机构 % 的生成未绑定存款服务才跑批到 % 日，请先执行该服务',para_org_code,tmp_dp_curr_date;
    END IF;

    SELECT import_date FROM bps_78000.t_ods_data_handle_config WHERE file_name = 'T03_DEP_SUBACC_H.dat' INTO date_dpfm02;

    IF date_dpfm02 < para_data_date  THEN
        RAISE EXCEPTION '数据日期尚未到达,无法执行服务. 存款静态分户表(T03_DEP_SUBACC_H)的日期：% ，当前业务日期: %',date_dpfm02,para_data_date;
    END IF;

    -- 创建临时表-个人任务数
    CREATE TEMP TABLE tmp_empdp_task_detail (
       date DATE,
       teller_code VARCHAR(10),
       teller_org_code VARCHAR(10),
       dp_category_id INTEGER,
       balance NUMERIC(21,2) NOT NULL DEFAULT 0,
       ttl_pay_int numeric(17,2) NOT NULL DEFAULT 0,
       day_pay_int NUMERIC(17, 2) NOT NULL DEFAULT 0,
       dp_org_code VARCHAR(10),
       belong_org_code VARCHAR(10)
    ) ON COMMIT DROP;
    CREATE UNIQUE INDEX ON tmp_empdp_payment_detail(date, teller_code, dp_org_code, belong_org_code, dp_category_id);

    -- 创建临时表-个人计酬数
    CREATE TEMP TABLE tmp_empdp_payment_detail (
        date DATE,
        teller_code VARCHAR(10),
        teller_org_code VARCHAR(10),
        dp_category_id INTEGER,
        balance NUMERIC(21,2) NOT NULL DEFAULT 0,
        ttl_pay_int numeric(17,2) NOT NULL DEFAULT 0,
        day_pay_int NUMERIC(17, 2) NOT NULL DEFAULT 0,
        dp_org_code VARCHAR(10),
        belong_org_code VARCHAR(10)
    ) ON COMMIT DROP;
    CREATE UNIQUE INDEX ON tmp_empdp_payment_detail(date, teller_code, dp_org_code, belong_org_code, dp_category_id);

    -- 创建临时表-机构任务数
    CREATE TEMP TABLE tmp_orgdp_task_detail (
           date DATE,
           dp_category_id INTEGER,
           balance NUMERIC(21,2) NOT NULL DEFAULT 0,
           ttl_pay_int numeric(17,2) NOT NULL DEFAULT 0,
           day_pay_int NUMERIC(17, 2) NOT NULL DEFAULT 0,
           dp_org_code VARCHAR(10),
           belong_org_code VARCHAR(10)
    ) ON COMMIT DROP;
    CREATE UNIQUE INDEX ON tmp_orgdp_task_detail(date, dp_org_code, belong_org_code, dp_category_id);

    insert_empdp_task_sql := 'INSERT INTO tmp_empdp_task_detail (date, teller_code, teller_org_code, dp_category_id, balance, dp_org_code, belong_org_code)
          VALUES ($1, $2, $3, $4, $5, $6, $7)
          ON CONFLICT (date, teller_code, dp_org_code, belong_org_code, dp_category_id) DO UPDATE SET balance = tmp_empdp_task_detail.balance + $5';

    insert_empdp_payment_sql := 'INSERT INTO tmp_empdp_payment_detail (date, teller_code, teller_org_code, dp_category_id, balance, dp_org_code, belong_org_code)
          VALUES ($1, $2, $3, $4, $5, $6, $7)
          ON CONFLICT (date, teller_code, dp_org_code, belong_org_code, dp_category_id) DO UPDATE SET balance = tmp_empdp_payment_detail.balance + $5';

    insert_orgdp_task_sql := 'INSERT INTO tmp_orgdp_task_detail (date, dp_category_id, balance, dp_org_code, belong_org_code)
          VALUES ($1, $2, $3, $4, $5)
          ON CONFLICT (date, dp_org_code, belong_org_code, dp_category_id) DO UPDATE SET balance = tmp_orgdp_task_detail.balance + $3';

    select_sql := 'SELECT
              org_code, account_no, child_account_no, close_flag, subject_no, before_day_balance, account_type
            FROM
              bps_78000.t_ods_hxsj_deposit_account_detail
            WHERE
              org_code = $1 AND date = $2 AND
              subject_no IN (SELECT distinct unnest(subject_no) FROM t_cktj_deposit_category) AND before_day_balance >0  ';



    FOR rs IN EXECUTE select_sql USING para_org_code, para_data_date LOOP

            --通过账号与账户类型查找客户性质
            SELECT cktj_get_customer_type(rs.account_no,rs.account_type) INTO customer_type;

            -- 通过科目号与客户性质找到存款分类ID
            SELECT * FROM cktj_get_deposit_category_by_subject_no(rs.subject_no, customer_type) INTO dp_category;
            IF dp_category IS NULL OR dp_category.id ISNULL OR dp_category.belong_to ISNULL THEN
                RAISE EXCEPTION '通过科目号: %, 账号: %, 客户性质: %, 找不到对应的存款分类！', rs.subject_no, rs.account_no, customer_type;
            END IF;

            -- 判断是否存在任务分成
            SELECT count(*) FROM bps_78000.t_cktj_employee_account_task where account_no = rs.account_no and coalesce(rs.child_account_no,'') = coalesce(child_account_no,'') and (para_data_date BETWEEN start_date AND COALESCE(end_date, '2999-12-31'::DATE) ) into tmp_cnt;
            IF tmp_cnt = 0 THEN
                RAISE EXCEPTION '通过账号: %, 子账号: %, 日期: %, 找不到对应的揽储人-任务！', rs.account_no, rs.child_account_no, para_data_date;
            END IF;

            -- 判断是否存在计酬分成
            SELECT count(*) FROM bps_78000.t_cktj_employee_account_payment where account_no = rs.account_no and coalesce(rs.child_account_no,'') = coalesce(child_account_no,'') and para_data_date BETWEEN start_date AND COALESCE(end_date, '2999-12-31'::DATE) into tmp_cnt;
            IF tmp_cnt = 0 THEN
                RAISE EXCEPTION '通过账号: %, 子账号: %, 日期: %, 找不到对应的揽储人-计酬！', rs.account_no, rs.child_account_no, para_data_date;
            END IF;

            ea_record := null;
            FOR ea_record IN SELECT * FROM bps_78000.t_cktj_employee_account_task WHERE start_date >= para_data_date AND para_data_date <= coalesce(end_date,'2099-12-31') AND account_no = rs.account_no LOOP
                    -- 通过柜员号和日期查找柜员在职机构
                    SELECT get_org_code_by_teller_code(ea_record.teller_code, para_data_date) INTO teller_org_code;
                    IF teller_org_code IS NULL OR teller_org_code = '' THEN
                        RAISE EXCEPTION '通过柜员号: %, 日期: %, 找不到该柜员的在职机构！', tmp_teller_code, para_data_date;
                    END IF;

                    -- 1、计算机构存款
                    IF (dp_category.belong_to = 'DEPOSIT_CATEGORY_BELONG_TO_EMPLOYEE_AND_ORG' OR dp_category.belong_to = 'DEPOSIT_CATEGORY_BELONG_TO_ORG') THEN
                        EXECUTE insert_orgdp_task_sql USING para_data_date, dp_category.id, rs.before_day_balance*ea_record.percentage, rs.org_code, teller_org_code;
                    END IF;

                    -- 2、计算员工存款-任务数
                    IF (dp_category.belong_to = 'DEPOSIT_CATEGORY_BELONG_TO_EMPLOYEE_AND_ORG' OR dp_category.belong_to = 'DEPOSIT_CATEGORY_BELONG_TO_EMPLOYEE') THEN
                        EXECUTE insert_empdp_task_sql USING para_data_date, ea_record.teller_code, teller_org_code, dp_category.id, rs.before_day_balance*ea_record.percentage, rs.org_code, teller_org_code;
                    END IF;
            END LOOP;

            ea_record := null;
            FOR ea_record IN SELECT * FROM bps_78000.t_cktj_employee_account_payment WHERE start_date >= para_data_date AND para_data_date <= coalesce(end_date,'2099-12-31') AND account_no = rs.account_no LOOP
                    -- 通过柜员号和日期查找柜员在职机构
                    SELECT get_org_code_by_teller_code(ea_record.teller_code, para_data_date) INTO teller_org_code;
                    IF teller_org_code IS NULL OR teller_org_code = '' THEN
                        RAISE EXCEPTION '通过柜员号: %, 日期: %, 找不到该柜员的在职机构！', tmp_teller_code, para_data_date;
                    END IF;

                    -- 计算员工存款-计酬数
                    IF (dp_category.belong_to = 'DEPOSIT_CATEGORY_BELONG_TO_EMPLOYEE_AND_ORG' OR dp_category.belong_to = 'DEPOSIT_CATEGORY_BELONG_TO_EMPLOYEE') THEN
                        EXECUTE insert_empdp_payment_sql USING para_data_date, ea_record.teller_code, teller_org_code, dp_category.id, rs.before_day_balance*ea_record.percentage, rs.org_code, teller_org_code;
                    END IF;
                END LOOP;


        END LOOP;

    --计算实付利息
--     RAISE NOTICE '开始计算实付利息';
--     SELECT cktj_calculate_pay_interest(org_code, org_type, data_date) INTO call_ret;
--     RAISE NOTICE 'cktj_calculate_pay_interest返回的结果是：%',call_ret;

    --7、删除可能存在的该日期数据，写入到员工存款统计表-任务数
    DELETE FROM bps_78000.t_cktj_employee_deposit_task_detail WHERE date = para_data_date AND dp_org_code = para_org_code;
    INSERT INTO bps_78000.t_cktj_employee_deposit_task_detail (date, teller_code, teller_org_code, dp_category_id, balance, ttl_pay_int, day_pay_int, dp_org_code, belong_org_code, create_time, update_time) SELECT t.date, t.teller_code, t.teller_org_code, t.dp_category_id, t.balance, t.ttl_pay_int, t.day_pay_int, t.dp_org_code, t.belong_org_code, now(), now() FROM tmp_empdp_task_detail t;

    --7、删除可能存在的该日期数据，写入到员工存款统计表-计酬数
    DELETE FROM bps_78000.t_cktj_employee_deposit_payment_detail WHERE date = para_data_date AND dp_org_code = para_org_code;
    INSERT INTO bps_78000.t_cktj_employee_deposit_payment_detail (date, teller_code, teller_org_code, dp_category_id, balance, ttl_pay_int, day_pay_int, dp_org_code, belong_org_code, create_time, update_time) SELECT t.date, t.teller_code, t.teller_org_code, t.dp_category_id, t.balance, t.ttl_pay_int, t.day_pay_int, t.dp_org_code, t.belong_org_code, now(), now() FROM tmp_empdp_payment_detail t;

    --8、删除可能存在的该日期数据，写入到机构存款统计表
    DELETE FROM bps_78000.t_cktj_organization_deposit_task_detail WHERE date = para_data_date AND dp_org_code = para_org_code;
    INSERT INTO bps_78000.t_cktj_organization_deposit_task_detail (date, dp_category_id, balance, ttl_pay_int, day_pay_int, dp_org_code, belong_org_code, create_time, update_time) SELECT t.date, t.dp_category_id, t.balance, t.ttl_pay_int, t.day_pay_int, t.dp_org_code, t.belong_org_code, now(), now() FROM tmp_orgdp_task_detail t;

    --9、更新t_dktj_loan_handle_config即配置表
    next_date := para_data_date + INTERVAL '1 day';
    UPDATE bps_78000.t_cktj_deposit_handle_config SET dp_next_date = next_date,dp_curr_date = para_data_date,update_time = now() WHERE org_code = para_org_code AND type = '1';

	drop table if exists tmp_empdp_task_detail;
	drop table if exists tmp_empdp_payment_detail;
	drop table if exists tmp_orgdp_task_detail;

    RETURN TRUE;

END;
$$ LANGUAGE plpgsql;




--辅助服务，批量绑定账户到一个员工，默认按照100%比例
DROP FUNCTION IF EXISTS cktj_batch_bind_employee_account(para_org_code VARCHAR, para_date Date, para_teller_code VARCHAR);
CREATE OR REPLACE FUNCTION cktj_batch_bind_employee_account(para_org_code VARCHAR, para_date Date, para_teller_code VARCHAR)
    RETURNS BOOLEAN
AS $$
DECLARE
    r RECORD;
    select_sql VARCHAR;
    tmp_count INTEGER;
BEGIN

    -- 判断柜员号是否存在
    SELECT count(*) FROM public.t_sys_user WHERE code = para_teller_code INTO tmp_count;
    IF tmp_count < 1 THEN
        RAISE EXCEPTION '不存在该柜员号: %',para_teller_code;
    END IF;

    select_sql := 'select a.* from bps_78000.t_cktj_unbound_account a where a.date = $1 and a.org_code = $2 ';
    FOR r IN EXECUTE select_sql USING para_date,para_org_code LOOP
            -- 1、在t_dktj_employee_customer_task表中插入记录
            INSERT INTO bps_78000.t_cktj_employee_account_task(teller_code, percentage, main_teller, org_code, account_no, child_account_no, card_no, account_type, account_open_date, customer_no, customer_name, customer_type, identity_type, identity_no, start_date, end_date, valid_flag, register_type, register_check_status, alter_check_status, op_teller_code, register_check_teller_code, alter_check_teller_code, register_check_time, alter_check_time, parent_id, remarks, create_time, create_by, update_time, update_by) VALUES (para_teller_code, 1, true,r.org_code, r.account_no, r.child_account_no, r.card_no, r.account_type, r.open_date, r.customer_no, r.customer_name, r.customer_type, r.identity_type, r.identity_no, r.date, null, true, '0', '1', null, 'admin', 'admin', null, now(), null, null, '批量绑定账户到揽储人', now(), 1, now(), 1);

            -- 2、在t_dktj_employee_customer_payment表中插入记录
            INSERT INTO bps_78000.t_cktj_employee_account_payment(teller_code, percentage, main_teller, org_code, account_no, child_account_no, card_no, account_type, account_open_date, customer_no, customer_name, customer_type, identity_type, identity_no, start_date, end_date, valid_flag, register_type, register_check_status, alter_check_status, op_teller_code, register_check_teller_code, alter_check_teller_code, register_check_time, alter_check_time, parent_id, remarks, create_time, create_by, update_time, update_by) VALUES (para_teller_code, 1, true,r.org_code, r.account_no, r.child_account_no, r.card_no, r.account_type, r.open_date, r.customer_no, r.customer_name, r.customer_type, r.identity_type, r.identity_no, r.date, null, true, '0', '1', null, 'admin', 'admin', null, now(), null, null, '批量绑定账户到揽储人', now(), 1, now(), 1);

            -- 3、删除t_dktj_unbound_customer表中的记录
            DELETE FROM bps_78000.t_cktj_unbound_account WHERE id = r.id;
        END LOOP;

    RETURN TRUE;

EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE '错误信息：%',SQLERRM;
        RETURN FALSE;
END;
$$ LANGUAGE plpgsql;





--全部网点跑批生成未绑定列表
DROP FUNCTION IF EXISTS cktj_batch_generate_unbound_customer();
CREATE OR REPLACE FUNCTION cktj_batch_generate_unbound_customer()
    RETURNS BOOLEAN
AS $$
DECLARE
    select_sql1 VARCHAR;
    r RECORD;
    result BOOLEAN;
BEGIN

    select_sql1 := 'SELECT org_code,dp_next_date FROM t_cktj_deposit_handle_config where type=''2'' order by dp_next_date,org_code';

    FOR r IN EXECUTE select_sql1 LOOP
            SELECT cktj_generate_unbound_customer(r.org_code, r.dp_next_date) INTO result;
            RAISE NOTICE '存款统计生成未绑定列表跑批，网点：%，日期：%，结果：%',r.org_code, r.dp_next_date, result;
        END LOOP;

    Return true;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE '错误信息：%',SQLERRM;
        RETURN FALSE;
END;
$$ LANGUAGE plpgsql;


-- 全部网点跑批生成存款余额
DROP FUNCTION IF EXISTS cktj_batch_calculate_deposit_detail();
CREATE OR REPLACE FUNCTION cktj_batch_calculate_deposit_detail()
    RETURNS BOOLEAN
AS $$
DECLARE
    select_sql2 VARCHAR;
    r RECORD;
    result BOOLEAN;
BEGIN

    select_sql2 := 'SELECT org_code,dp_next_date FROM t_cktj_deposit_handle_config WHERE type = ''1'' ORDER BY dp_next_date,org_code';

    FOR r IN EXECUTE select_sql2 LOOP
            SELECT cktj_calculate_deposit_detail(r.org_code, r.dp_next_date) INTO result;
            RAISE NOTICE '存款余额和日均跑批，网点：%，日期：%，结果：%',r.org_code, r.dp_next_date,result;
        END LOOP;
    RETURN TRUE;

EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE '错误信息：%',SQLERRM;
        RETURN FALSE;
END;
$$ LANGUAGE plpgsql;


-- 单个网点跑批生成存款余额
DROP FUNCTION IF EXISTS cktj_single_org_calculate_deposit_detail(para_org_code VARCHAR);
CREATE OR REPLACE FUNCTION cktj_single_org_calculate_deposit_detail(para_org_code VARCHAR)
    RETURNS BOOLEAN
AS $$
DECLARE
    result BOOLEAN;
    tmp_next_date Date;
BEGIN

    SELECT dp_next_date FROM bps_78000.t_cktj_deposit_handle_config WHERE type = '1' AND org_code = para_org_code INTO tmp_next_date;

    SELECT cktj_calculate_deposit_detail(para_org_code, tmp_next_date) INTO result;
    RAISE NOTICE '存款余额和日均跑批，网点：%，日期：%，结果：%',para_org_code, tmp_next_date,result;

    RETURN TRUE;

EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE '错误信息：%',SQLERRM;
        RETURN FALSE;
END;
$$ LANGUAGE plpgsql;


--用于定时任务执行的function，
DROP FUNCTION IF EXISTS cktj_all_fun();
CREATE OR REPLACE FUNCTION cktj_all_fun()
    RETURNS BOOLEAN
AS $$
DECLARE
    unbound_result BOOLEAN;
    calculate_result BOOLEAN;
BEGIN

    select cktj_batch_generate_unbound_customer() into unbound_result;
    RAISE NOTICE '生成未绑定结果： %',unbound_result;
    select cktj_batch_calculate_deposit_detail() into calculate_result;
    RAISE NOTICE '计算跑批结果： %',calculate_result;

    RETURN TRUE;

EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE '错误信息：%',SQLERRM;
        RETURN FALSE;
END;
$$ LANGUAGE plpgsql;