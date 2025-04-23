--科目字典表
insert into bps_78000.t_ods_data_handle_config(id,data_desc,base_path,file_name,table_name,date_field_name,handle_seq,handle_mode,batch_size,import_date,next_date) VALUES(1,'科目字典表','/opt/ftproot/gtp01/95130_ODS_data','T09_SBJ.dat','bps_78000.t_ods_hxsj_subject_dict',null,1,'0',1,null,'2020-12-01');

insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(1,'no','VARCHAR',1,1);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(1,'c_name','VARCHAR',2,2);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(1,'type','VARCHAR',3,3);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(1,'acct_type','VARCHAR',4,4);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(1,'ctrl_bal_dc','VARCHAR',5,5);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(1,'ctrl_occ_amt_dc','VARCHAR',6,6);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(1,'bal_gather_way','VARCHAR',7,7);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(1,'level','VARCHAR',8,8);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(1,'parent_no','VARCHAR',9,9);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(1,'child_flag','VARCHAR',10,10);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(1,'int_accr_flag','VARCHAR',11,11);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(1,'red_mark_flag','VARCHAR',12,12);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(1,'spl_acct_flag','VARCHAR',13,13);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(1,'reg_acct_way','VARCHAR',14,14);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(1,'ent_acct_way','VARCHAR',15,15);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(1,'gather_flag','VARCHAR',16,16);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(1,'effective_date','Date',17,17);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(1,'ccy_ctrl_field','VARCHAR',18,18);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(1,'branch_type_ctrl_field','VARCHAR',19,19);
insert into bps_78000.t_ods_data_table_field(handle_config_id, original_field_flag, table_field_name, table_field_type, sort) values(1,'3','date','DATE',20);
insert into bps_78000.t_ods_data_table_field(handle_config_id, original_field_flag, table_field_name, table_field_type, sort) values(1,'4','import_time','TIMESTAMP',21);
insert into bps_78000.t_ods_data_table_field(handle_config_id, original_field_flag, table_field_name, table_field_type, sort) values(1,'5','data_source','VARCHAR',22);




--t_ods_s01_cifm02 客户公用信息表
insert into bps_78000.t_ods_data_handle_config(id,data_desc,base_path,file_name,table_name,date_field_name,handle_seq,handle_mode,batch_size,import_date,next_date) VALUES(2,'客户公用信息表','/home/gtp/data','T01_CST_PBLC_BSC_INF_FILE_H.dat','bps_78000.t_ods_hxsj_customer_public_base',null,2,'0',1,null,'2020-07-01');

insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'no','VARCHAR',1,1);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'type','VARCHAR',2,2);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'name','VARCHAR',3,3);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'oname','VARCHAR',4,4);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'en_name','VARCHAR',5,5);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'identity_type','VARCHAR',6,6);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'identity_no','VARCHAR',7,7);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'issue_date','DATE',8,8);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'expire_date','Date',9,9);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'issue_place','VARCHAR',10,10);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'country_code','VARCHAR',11,11);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'create_org','VARCHAR',12,12);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'create_teller','VARCHAR',13,13);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'create_date','Date',14,14);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'ctrl_code','VARCHAR',15,15);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'status','VARCHAR',16,16);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'level','VARCHAR',17,17);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'mpfs_cert_no','VARCHAR',18,18);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'loan_cret_no','VARCHAR',19,19);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'address_code','VARCHAR',20,20);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'hi_earnest_money','NUMERIC',21,21);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'remarks','VARCHAR',22,22);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(2,'credit_level','VARCHAR',23,23);
insert into bps_78000.t_ods_data_table_field(handle_config_id, original_field_flag, table_field_name, table_field_type, sort) values(2,'4','import_time','Date',24);
insert into bps_78000.t_ods_data_table_field(handle_config_id, original_field_flag, table_field_name, table_field_type, sort) values(2,'5','data_source','VARCHAR',25);


-- bps_78000.t_ods_s01_cifm01 客户号与账号/卡号对照表
insert into bps_78000.t_ods_data_handle_config(id,data_desc,base_path,file_name,table_name,date_field_name,handle_seq,handle_mode,batch_size,import_date,next_date) VALUES(3,'客户号账号对照表','/opt/ftproot/gtp01/95130_ODS_data','S01_CIFM01.dat','bps_78000.t_ods_hxsj_customer_account',null,3,'0',1,null,'2025-04-05');

insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(3,'customer_no','VARCHAR',1,1);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(3,'account_no','VARCHAR',2,2);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(3,'child_sys_code','VARCHAR',3,3);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(3,'org_code','VARCHAR',4,4);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(3,'flag','VARCHAR',5,5);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(3,'last_print_date','DATE',6,6);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(3,'unknow_field_1','DATE',7,7);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(3,'unknow_field_2','DATE',8,8);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(3,'unknow_field_3','VARCHAR',9,9);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(3,'unknow_field_4','DATE',10,10);
insert into bps_78000.t_ods_data_table_field(handle_config_id, original_field_flag, table_field_name, table_field_type, sort) values(3,'4','import_time','Date',11);
insert into bps_78000.t_ods_data_table_field(handle_config_id, original_field_flag, table_field_name, table_field_type, sort) values(3,'5','data_source','VARCHAR',12);


---- 卡号账号对照表 (cdfm02.txt) 相同
insert into bps_78000.t_ods_data_handle_config(id,data_desc,base_path,file_name,table_name,date_field_name,handle_seq,handle_mode,batch_size,import_date,next_date) VALUES(4,'卡号账号对照表','/opt/ftproot/gtp01/95130_ODS_data','S01_CDFM02.dat','bps_78000.t_ods_hxsj_card_account',null,4,'0',1,null,'2025-04-05');

insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(4,'card_no','VARCHAR',1,1);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(4,'account_type','VARCHAR',2,2);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(4,'account_no','VARCHAR',3,3);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(4,'child_account_no','VARCHAR',4,4);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(4,'valid_flag','VARCHAR',5,5);
insert into bps_78000.t_ods_data_table_field(handle_config_id, original_field_flag, table_field_name, table_field_type, sort) values(4,'4','import_time','Date',6);
insert into bps_78000.t_ods_data_table_field(handle_config_id, original_field_flag, table_field_name, table_field_type, sort) values(4,'5','data_source','VARCHAR',7);


--存款账户信息表 (dpfm01.txt)
insert into bps_78000.t_ods_data_handle_config(id,data_desc,base_path,file_name,table_name,date_field_name,handle_seq,handle_mode,batch_size,import_date,next_date) VALUES(6,'存款账户静态表','/home/gtp/data','T03_DEP_ACC_H.dat','bps_78000.t_ods_hxsj_deposit_account',null,6,'0',1,null,'2020-10-01');

insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(6,'account_no','VARCHAR',1,3);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(6,'account_type','VARCHAR',2,4);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(6,'close_acct_flag','VARCHAR',3,5);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(6,'overall_acct_flag','VARCHAR',4,6);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(6,'sec_lvl_acct_flag','VARCHAR',5,7);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(6,'voucher_flag','VARCHAR',6,8);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(6,'voucher_type','VARCHAR',7,9);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(6,'rmb_crown_no','VARCHAR',8,10);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(6,'voucher_no','VARCHAR',9,11);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(6,'draw_way','VARCHAR',10,12);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(6,'draw_cert','VARCHAR',11,13);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(6,'valid_spl_acct_num','NUMERIC',12,14);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(6,'spl_acct_max_seq_num','NUMERIC',13,15);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(6,'customer_type','VARCHAR',14,16);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(6,'passbook_no','NUMERIC',15,17);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(6,'open_acct_licence_no','VARCHAR',16,18);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(6,'approval_cert_no','VARCHAR',17,19);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(6,'valid_date','DATE',18,20);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(6,'acct_obligate_flag','VARCHAR',19,21);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(6,'org_code','VARCHAR',20,22);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(6,'ledger_org_code','VARCHAR',21,23);
insert into bps_78000.t_ods_data_table_field(handle_config_id, original_field_flag, table_field_name, table_field_type, sort) values(6,'3','date','DATE',22);
insert into bps_78000.t_ods_data_table_field(handle_config_id, original_field_flag, table_field_name, table_field_type, sort) values(6,'4','import_time','Date',23);
insert into bps_78000.t_ods_data_table_field(handle_config_id, original_field_flag, table_field_name, table_field_type, sort) values(6,'5','data_source','VARCHAR',24);


-- 存款静态分户信息表（t_ods_s01_dpfm02)
insert into bps_78000.t_ods_data_handle_config(id,data_desc,base_path,file_name,table_name,date_field_name,handle_seq,handle_mode,batch_size,import_date,next_date) VALUES(8,'存款账户分户信息表','/home/gtp/data','T03_DEP_SUBACC_H.dat','bps_78000.t_ods_hxsj_deposit_account_detail','date',8,'1',1,null,'2020-07-01');

insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'agrm_id','varchar',1,1);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'agrm_mod','varchar',2,2);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'account_no','varchar',3,3);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'child_account_no','varchar',4,4);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'org_code','varchar',5,5);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'check_org_code','varchar',6,6);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'subject_no','varchar',7,7);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'account_type','varchar',8,8);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'dem_regl_flag','varchar',9,9);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'close_flag','varchar',10,10);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'ccy_no','varchar',11,11);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'cash_remit_flag','varchar',12,12);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'product_no','varchar',13,13);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'open_date','date',14,14);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'start_date','date',15,15);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'end_date','date',16,16);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'term1','numeric',17,17);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'term2','varchar',18,18);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'interest_plan','varchar',19,19);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'float_rate','numeric',20,20);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'prev_biz_date','date',21,21);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'account_nature','varchar',22,22);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'frgn_acct_nature','varchar',23,23);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'insp_mon_acct_flag','varchar',24,24);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'unives_exchag_flag','varchar',25,25);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'encash_flag','varchar',26,26);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'ahead_draw_times','varchar',27,27);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'draw_interest_way','varchar',28,28);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'agrmt_rate','numeric',29,29);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'freeze_flag','varchar',30,30);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'pledge_flag','varchar',31,31);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'period_prove_flag','varchar',32,32);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'agrmt_dp_flag','varchar',33,33);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'sec_lvl_acct_flag','varchar',34,34);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'ovrd_acct_flag','varchar',35,35);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'collt_interest_acct_flag','varchar',36,36);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'interest_enter_acct_flag','varchar',37,37);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'float_flag','varchar',38,38);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'fex_dp_instl_normal_amt','numeric',39,39);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'eve_tm_draw_dp_amt','numeric',40,40);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'intcol_fex_dp_inteval_mth','numeric',41,41);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'have_deal_times','numeric',42,42);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'nxt_tm_deal_date','date',43,43);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'fex_dp_instl_omit_flag','varchar',44,44);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'deflt_times','numeric',45,45);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'rlover_flag','varchar',46,46);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'rlover_times','numeric',47,47);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'agrmt_no','varchar',48,48);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'sttl_comm_chrg_collt_way','varchar',49,49);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'reserved_amt','numeric',50,50);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'ctrl_flag','varchar',51,51);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'bgn_int_rate_year','numeric',52,52);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'curr_balance','numeric',53,53);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'curr_bal_dc','varchar',54,54);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'day_end_balance','numeric',55,55);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'day_end_bal_dc','varchar',56,56);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'before_day_balance','numeric',57,57);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'before_day_bal_dc','varchar',58,58);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'ctrl_amt','numeric',59,59);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'freeze_amt','numeric',60,60);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'ovrd_amt','numeric',61,61);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'accum1','numeric',62,62);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'accum2','numeric',63,63);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(8,'accum3','numeric',64,64);
insert into bps_78000.t_ods_data_table_field(handle_config_id, original_field_flag, table_field_name, table_field_type, sort) values(8,'3','date','DATE',65);
insert into bps_78000.t_ods_data_table_field(handle_config_id, original_field_flag, table_field_name, table_field_type, sort) values(8,'4','import_time','TIMESTAMP',66);
insert into bps_78000.t_ods_data_table_field(handle_config_id, original_field_flag, table_field_name, table_field_type, sort) values(8,'5','data_source','VARCHAR',67);






-- 内部账户信息表(dpfm03.txt)
insert into bps_78000.t_ods_data_handle_config(id,data_desc,base_path,file_name,table_name,date_field_name,handle_seq,handle_mode,batch_size,import_date,next_date) VALUES(9,'内部户信息表','/home/gtp/data','T03_INRACC_H.dat','bps_78000.t_ods_hxsj_internal_account',null,9,'0',1,null,'2020-07-01');

insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(9,'account_no','VARCHAR',3,3);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(9,'org_code','VARCHAR',4,4);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(9,'close_acct_flag','VARCHAR',5,5);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(9,'standard_flag','VARCHAR',6,6);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(9,'ctrl_bal_way','VARCHAR',7,7);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(9,'account_name','VARCHAR',8,8);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(9,'jf_kind','VARCHAR',9,9);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(9,'cle_reco_way','VARCHAR',10,10);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(9,'other_bank_name','VARCHAR',11,11);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(9,'other_side_acct_no','VARCHAR',12,12);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(9,'open_teller_code','VARCHAR',13,13);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(9,'close_date','date',14,14);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(9,'close_teller_code','VARCHAR',15,15);
insert into bps_78000.t_ods_data_table_field(handle_config_id, original_field_flag, table_field_name, table_field_type, sort) values(9,'4','import_time','Date',16);
insert into bps_78000.t_ods_data_table_field(handle_config_id, original_field_flag, table_field_name, table_field_type, sort) values(9,'5','data_source','VARCHAR',17);






-- 存款交易明细表(t_ods_s01_dpfm30)
insert into bps_78000.t_ods_data_handle_config(id,data_desc,base_path,file_name,table_name,date_field_name,handle_seq,handle_mode,batch_size,import_date,next_date) VALUES(12,'账户交易明细表','/opt/ftproot/gtp01/95130_ODS_data','S01_DPFM30.dat','bps_78000.t_ods_hxsj_deposit_detail',null,12,'1',1,null,'2025-04-05');

insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'account_no','VARCHAR',1,1);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'child_account_no','VARCHAR',2,2);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'tx_date','date',3,3);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'orig_tx_no','VARCHAR',4,4);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'child_tx_no','VARCHAR',5,5);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'ccy','VARCHAR',6,6);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'customer_type','VARCHAR',7,7);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'tx_amt','numeric',8,8);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'deb_cred_flag','VARCHAR',9,9);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'balance','numeric',10,10);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'curr_bal_dc','VARCHAR',11,11);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'tx_org_code','VARCHAR',12,12);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'open_acct_org_code','VARCHAR',13,13);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'cash_tran_flag','VARCHAR',14,14);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'voucher_type','VARCHAR',15,15);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'voucher_no','VARCHAR',16,17);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'page_no','numeric',17,18);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'count','numeric',18,19);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'print_flag','VARCHAR',19,20);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'orig_tx_code','VARCHAR',20,21);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'tx_teller','VARCHAR',21,22);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'review_teller','VARCHAR',22,23);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'other_side_acct_no','VARCHAR',23,24);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'other_side_child_acct_no','VARCHAR',24,25);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'summary_code','VARCHAR',25,26);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'summary','VARCHAR',26,27);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'tx_src','VARCHAR',27,28);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(12,'card_no','VARCHAR',28,30);
insert into bps_78000.t_ods_data_table_field(handle_config_id, original_field_flag, table_field_name, table_field_type, sort) values(12,'4','import_time','TIMESTAMP',29);
insert into bps_78000.t_ods_data_table_field(handle_config_id, original_field_flag, table_field_name, table_field_type, sort) values(12,'5','data_source','VARCHAR',30);




-- 总账（t_ods_s01_glfm01)
insert into bps_78000.t_ods_data_handle_config(id,data_desc,base_path,file_name,table_name,date_field_name,handle_seq,handle_mode,batch_size,import_date,next_date) VALUES(13,'总账明细表','/home/gtp/data','T09_GENERAL_LEDGER_DTL_S.dat','bps_78000.t_ods_hxsj_ledger_detail',null,13,'1',1,null,'2025-04-05');

insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(13,'busi_date','date',1,1);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(13,'org_code','VARCHAR',2,2);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(13,'ccy_no','VARCHAR',3,3);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(13,'subject_no','VARCHAR',4,4);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(13,'curr_day_ttl_dbt_amt','numeric',5,5);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(13,'curr_day_ttl_crted_amt','numeric',6,6);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(13,'curr_day_ttl_dbt_cnt','numeric',7,7);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(13,'curr_day_ttl_crted_cnt','numeric',8,8);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(13,'curr_dbt_bal','numeric',9,9);
insert into bps_78000.t_ods_data_table_field(handle_config_id, table_field_name, table_field_type, sort, file_position) values(13,'curr_crted_bal','numeric',10,10);
insert into bps_78000.t_ods_data_table_field(handle_config_id, original_field_flag, table_field_name, table_field_type, sort) values(13,'3','date','DATE',11);
insert into bps_78000.t_ods_data_table_field(handle_config_id, original_field_flag, table_field_name, table_field_type, sort) values(13,'4','import_time','TIMESTAMP',12);
insert into bps_78000.t_ods_data_table_field(handle_config_id, original_field_flag, table_field_name, table_field_type, sort) values(13,'5','data_source','VARCHAR',13);

