INSERT INTO public.t_sys_user(id, code, login_password, name, phone, mobile, avatar, identity_no, birthday, sex, entry_date, post, login_usable, remarks, admin_flag, create_by, create_time, update_by, update_time, login_ip, login_time, del_flag) VALUES(1,'admin', '$2a$10$GT.vY.k4l/iCY0rWk1bVQ.8SfSlBWL4BqUlMWdOi58Uy0QibAq4oW', '超级用户', null, null, null, null, null, null, '2025-01-01', null, true, null, true, 1, now(), null, null, null, null, false);


INSERT INTO public.t_sys_organization(id, parent_id, parent_ids, code, name, sort, icon, area_id, type, grade, address, representative, phone, usable, primary_user_id, deputy_user_id, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES (1, 0, '{0}', '00000', '内蒙古农商银行', 1, null, null, 'ORGANIZATION_TYPE_100', '1', null, null, null, true, null, null, null, 1, now(), null, null, false);
INSERT INTO public.t_sys_organization(id, parent_id, parent_ids, code, name, sort, icon, area_id, type, grade, address, representative, phone, usable, primary_user_id, deputy_user_id, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES (2001, 1, '{0,1}', '01000', '鄂尔多斯中心支行', 2001, null, null, 'ORGANIZATION_TYPE_200', '2', null, null, null, true, null, null, null, 1, now(), null, null, false);
INSERT INTO public.t_sys_organization(id, parent_id, parent_ids, code, name, sort, icon, area_id, type, grade, address, representative, phone, usable, primary_user_id, deputy_user_id, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES (2002, 2001, '{0,1,2001}', '78000', '伊金霍洛旗支行', 2002, null, null, 'ORGANIZATION_TYPE_300', '3', null, null, null, true, null, null, null, 1, now(),null, null, false);
INSERT INTO public.t_sys_organization(id, parent_id, parent_ids, code, name, sort, icon, area_id, type, grade, address, representative, phone, usable, primary_user_id, deputy_user_id, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES (2003,2002,'{0,1,2001,2002}','1078903', '业务发展部', 2003,null, null, 'ORGANIZATION_TYPE_301', '4', null, null, null, true, null, null, null, 1, now(), null, null, false);
INSERT INTO public.t_sys_organization(id, parent_id, parent_ids, code, name, sort, icon, area_id, type, grade, address, representative, phone, usable, primary_user_id, deputy_user_id, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES (2004, 2002, '{0,1,2001,2002}', '78003', '营业部', 2004, null, null, 'ORGANIZATION_TYPE_400', '4', null, null, null, true, null, null, null,1,now(), null, null, false);


INSERT INTO public.t_sys_user_organization(user_id, organization_id, start_date, end_date, remarks, valid_flag, parent_id, create_by, create_time, update_by, update_time) VALUES (1, 1,'2025-01-01', null, null, true, null, 1, now(), null, null);


INSERT INTO public.t_sys_user_status(user_id, start_date, end_date, status, valid_flag, parent_id, remarks, create_by, create_time) VALUES (1, '2025-01-01',null, 'USER_STATUS_NORMAL', true, null, null, 1, now());

-- 字典表-用户状态
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (1,'USER_STATUS_NORMAL','普通在职', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (2,'USER_STATUS_SPECIAL_SENIOR','高管改专', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (3,'USER_STATUS_INNER_RETIRED_SENIOR','退二线领导', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (4,'USER_STATUS_INNER_ASSIST','内部协存', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (5,'USER_STATUS_OUTSIDE_ASSIST','外部协存', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (6,'USER_STATUS_INNER_RETIRED','内退', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (7,'USER_STATUS_RETIRED','退休', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (8,'USER_STATUS_RESIGN','辞职', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (9,'USER_STATUS_FIRED','辞退', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (10,'USER_STATUS_OTHERS','其他', now());
-- 字典表-用户职位
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (21,'USER_POST_SUBBRANCH_TERMINAL_TELLER','网点普通员工', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (22,'USER_POST_SUBBRANCH_TERMINAL_MANAGER','网点客户经理', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (23,'USER_POST_SUBBRANCH_TERMINAL_ACCOUNTANT','网点委派会计', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (24,'USER_POST_SUBBRANCH_TERMINAL_THREECLASSTELLER','网点三级柜员', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (25,'USER_POST_SUBBRANCH_TERMINAL_DEPUTY','网点副行长', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (26,'USER_POST_SUBBRANCH_TERMINAL_DIRECTOR','网点行长', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (27,'USER_POST_SUBBRANCH_TERMINAL_PUBLIC','网点公共用户', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (28,'USER_POST_SUBBRANCH_TERMINAL_INNERASSIST','网点内部协存', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (29,'USER_POST_SUBBRANCH_TERMINAL_OUTSIDEASSIST','网点外部协存', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (30,'USER_POST_SUBBRANCH_DEPARTMENT_EMPLOYEE','支行部门普通员工', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (31,'USER_POST_SUBBRANCH_DEPARTMENT_DEPUTY','支行部门副职', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (32,'USER_POST_SUBBRANCH_DEPARTMENT_DIRECTOR','支行部门正职', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (36,'USER_POST_SUBBRANCH_SENIOR_SENIOREMPLOYEE','支行高级专员', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (37,'USER_POST_SUBBRANCH_SENIOR_RETIREDSENIOR','支行退二线领导', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (38,'USER_POST_SUBBRANCH_SENIOR_DEPUTY','支行副职', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (39,'USER_POST_SUBBRANCH_SENIOR_DIRECTOR','支行正职', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (33,'USER_POST_CENTERBRANCH_DEPARTMENT_EMPLOYEE','中心支行部门普通员工', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (34,'USER_POST_CENTERBRANCH_DEPARTMENT_DEPUTY','中心支行部门副职', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (35,'USER_POST_CENTERBRANCH_DEPARTMENT_DIRECTOR','中心支行部门正职', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (40,'USER_POST_CENTERBRANCH_SENIOR_DEPUTY','中心支行副职', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (41,'USER_POST_CENTERBRANCH_SENIOR_DIRECTOR','中心支行正职', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (42,'USER_POST_SUBBRANCH_OPERATOR','支行操作员', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (43,'USER_POST_SUBBRANCH_TERMINAL_OPERATOR','网点操作员', now());
-- 字典表-机构类型
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (100, 'ORGANIZATION_TYPE_100', '总行' , now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (101, 'ORGANIZATION_TYPE_101', '总行部门' , now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (102, 'ORGANIZATION_TYPE_200', '中心支行', now() );
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (103, 'ORGANIZATION_TYPE_201', '中心支行部门' , now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (104, 'ORGANIZATION_TYPE_300', '支行' , now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (105, 'ORGANIZATION_TYPE_301', '支行部门' , now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (106, 'ORGANIZATION_TYPE_400', '网点' , now());
-- 字典表-菜单类型
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (150, 'MENU_TYPE_MENU_ROOT', '根菜单', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (151, 'MENU_TYPE_MENU_GROUP', '菜单组', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (152, 'MENU_TYPE_MENU_MENU', '普通菜单', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (153, 'MENU_TYPE_MENU_PERMISSION', '权限或按钮', now());
-- 字典表-数据范围
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (200, 'DATA_SCOPE_ALL', '全部数据', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (201, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', '所在机构及以下数据', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (202, 'DATA_SCOPE_ORGANIZATION', '所在机构数据', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (203, 'DATA_SCOPE_SELF', '仅本人数据', now());
INSERT INTO public.t_sys_dictionary(sort, code, name, create_time) VALUES (204, 'DATA_SCOPE_CUSTOM', '按明细设置', now());



