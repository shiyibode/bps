INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(500, 1, '{0,1}', '贷款统计', 500, 'MENU_TYPE_MENU_GROUP', '', '', 'x-fa fa-cog', '1', '', '贷款统计', null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(501, 500, '{0,1,500}', '维护人管理', 501, 'MENU_TYPE_MENU_GROUP', '', '', 'x-fa fa-comments', '1', '', '', '', 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(502, 501, '{0,1,500,501}', '登记申请', 502, 'MENU_TYPE_MENU_MENU', '/dktj/employeecustomer/getUnregisterCustomer', 'dktjemployeecustomer', 'x-fa fa-comments', '1', 'dktj:employeecustomer:getunregistercustomer', '', '', 1, now(), 1, now(), FALSE);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(503, 502, '{0,1,500,501,502}', '登记', 503, 'MENU_TYPE_MENU_PERMISSION', '/dktj/employeecustomer/registerEmployee', 'registerEmployee', 'x-fa fa-comments', '1', 'dktj:employeecustomer:registeremployee', '','', 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(504, 501, '{0,1,500,501}', '登记复核', 504, 'MENU_TYPE_MENU_MENU', '/dktj/employeecustomer/getUncheckedCustomer', 'dktjemployeecustomercheck', 'x-fa fa-comments', '1', 'dktj:employeecustomer:getuncheckedcustomer', '', '', 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(505, 504, '{0,1,500,501,504}', '复核正确', 505, 'MENU_TYPE_MENU_PERMISSION', '/dktj/employeecustomer/checkRegisterEmployee', 'checkRegisterEmployee', 'x-fa fa-comments', '1', 'dktj:employeecustomer:checkregisteremployee', null,null,1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(506, 504, '{0,1,500,501,504}', '撤销登记', 506, 'MENU_TYPE_MENU_PERMISSION', '/dktj/employeecustomer/undoRegisterEmployee', 'undoRegisterEmployee', 'x-fa fa-comments', '1', 'dktj:employeecustomer:undoregisteremployee', null,null,1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(507, 501, '{0,1,500,501}', '变更申请', 507, 'MENU_TYPE_MENU_MENU', '/dktj/employeecustomer/getModifiableCustomer', 'dktjemployeecustomeralter', 'x-fa fa-comments', '1', 'dktj:employeecustomer:getmodifiablecustomer', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(508, 507, '{0,1,500,501,507}', '变更', 508, 'MENU_TYPE_MENU_PERMISSION', '/dktj/employeecustomer/modifyEmployee', 'modifyEmployee', 'x-fa fa-comments', '1', 'dktj:employeecustomer:modifyemployee', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(509, 501, '{0,1,500,501}', '变更复核', 509, 'MENU_TYPE_MENU_MENU', '/dktj/employeecustomer/getModifiedUncheckedCustomer', 'dktjemployeecustomeraltercheck', 'x-fa fa-comments', '1', 'dktj:employeecustomer:getmodifieduncheckedcustomer', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(510, 509, '{0,1,500,501,509}', '同意变更', 510, 'MENU_TYPE_MENU_PERMISSION', '/dktj/employeecustomer/checkModifyEmployee', 'checkModifyEmployee', 'x-fa fa-comments', '1', 'dktj:employeecustomer:checkmodifyemployee', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(511, 509, '{0,1,500,501,509}', '撤销变更', 511, 'MENU_TYPE_MENU_PERMISSION', '/dktj/employeecustomer/undoModifyEmployee','undoModifyEmployee', 'x-fa fa-comments', '1', 'dktj:employeecustomer:undomodifyemployee', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(512, 500, '{0,1,500}', '贷款客户信息', 512, 'MENU_TYPE_MENU_MENU', '/dktj/employeecustomer/get', 'dktjemployeecustomerdetail', 'x-fa fa-comments', '1', 'dktj:employeecustomer:get', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(515, 500, '{0,1,500}', '贷款余额信息', 515, 'MENU_TYPE_MENU_GROUP', '', '', 'x-fa fa-comments', '1', '', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(516, 515, '{0,1,500,515}', '员工时点', 516, 'MENU_TYPE_MENU_MENU', '/dktj/loan/employee', 'dktjloan', 'x-fa fa-comments', '1', 'dktj:loan:employee', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(517, 516, '{0,1,500,515,516}', '导出', 517, 'MENU_TYPE_MENU_PERMISSION', '/dktj/loan/exportEmpLoan', 'dktjExportEmpLoan', 'x-fa fa-comments', '1', 'dktj:loan:exportemploan', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(518, 515, '{0,1,500,515}', '员工日均', 518, 'MENU_TYPE_MENU_MENU', '/dktj/loan/empAverage', 'dktjloan', 'x-fa fa-comments', '1', 'dktj:loan:empaverage', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(519, 518, '{0,1,500,515,518}', '导出', 519, 'MENU_TYPE_MENU_PERMISSION', '/dktj/loan/exportEmpAvgLoan', 'dktjExportEmpAvgLoan', 'x-fa fa-comments', '1', 'dktj:loan:exportempavgloan', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(520, 515, '{0,1,500,515}', '机构时点', 520, 'MENU_TYPE_MENU_MENU', '/dktj/loan/organization', 'dktjloan', 'x-fa fa-comments', '1', 'dktj:loan:organization', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(521, 520, '{0,1,500,515,520}', '导出', 521, 'MENU_TYPE_MENU_PERMISSION', '/dktj/loan/exportOrgLoan', 'dktjExportOrgLoan', 'x-fa fa-comments', '1', 'dktj:loan:exportorgloan', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(522, 515, '{0,1,500,515}', '机构日均', 522, 'MENU_TYPE_MENU_MENU', '/dktj/loan/orgAverage', 'dktjloan', 'x-fa fa-comments', '1', 'dktj:loan:orgaverage', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(523, 522, '{0,1,500,515,522}', '导出', 523, 'MENU_TYPE_MENU_PERMISSION', '/dktj/loan/exportOrgAvgLoan', 'dktjExportOrgAvgLoan', 'x-fa fa-comments', '1', 'dktj:loan:exportorgavgloan', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(524, 500, '{0,1,500}', '客户状态信息', 524, 'MENU_TYPE_MENU_MENU', '/dktj/customerproperty/get', 'dktjcustomerproperty', 'x-fa fa-comments', '1', 'dktj:customerproperty:get', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(525, 524, '{0,1,500,524}', '转流动', 525, 'MENU_TYPE_MENU_PERMISSION', '/dktj/employeecustomer/changeStatusToFluid', 'changeStatusToFluid', 'x-fa fa-comments', '1', 'dktj:employeecustomer:changestatustofluid', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(526, 524, '{0,1,500,524}', '转固定', 526, 'MENU_TYPE_MENU_PERMISSION', '/dktj/employeecustomer/changeStatusToFix', 'changeStatusToFix', 'x-fa fa-comments', '1', 'dktj:employeecustomer:changestatustofix', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(527, 500, '{0,1,500}', '利息分成信息', 527, 'MENU_TYPE_MENU_GROUP', '/dktj/employeeinterest/get', 'dktjemployeeinterestdetail', 'x-fa fa-comments', '1', 'dktj:employeeinterest:get', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(528, 527, '{0,1,500,527}', '员工利息时点', 528, 'MENU_TYPE_MENU_MENU', '/dktj/employeeinterest/empinterest', 'dktjemployeeinterest', 'x-fa fa-comments', '1', 'dktj:employeeinterest:empinterest', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(529, 527, '{0,1,500,527}', '员工利息日均', 529, 'MENU_TYPE_MENU_MENU', '/dktj/employeeinterest/empavginterest', 'dktjemployeeinterest', 'x-fa fa-comments', '1', 'dktj:employeeinterest:empavginterest', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(530, 527, '{0,1,500,527}', '机构利息时点', 530, 'MENU_TYPE_MENU_MENU', '/dktj/employeeinterest/orginterest', 'dktjemployeeinterest', 'x-fa fa-comments', '1', 'dktj:employeeinterest:orginterest', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(531, 527, '{0,1,500,527}', '机构利息日均', 531, 'MENU_TYPE_MENU_MENU', '/dktj/employeeinterest/orgavginterest', 'dktjemployeeinterest', 'x-fa fa-comments', '1', 'dktj:employeeinterest:orgavginterest', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(532, 527, '{0,1,500,527}', '员工利息分成明细', 532, 'MENU_TYPE_MENU_MENU', '/dktj/employeeinterest/get', 'dktjemployeeinterestdetail', 'x-fa fa-comments', '1', 'dktj:employeeinterest:get', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(533, 500, '{0,1,500}', '补录分成规则', 533, 'MENU_TYPE_MENU_MENU', '/dktj/employeeinterest/readd', 'dktjemployeeinterestreadd', 'x-fa fa-comments', '1', 'dktj:employeeinterest:readd', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(534, 533, '{0,1,500,533}', '补录', 534, 'MENU_TYPE_MENU_PERMISSION', '/dktj/employeeinterest/readdaccount', 'readdAccount', 'x-fa fa-comments', '1', 'dktj:employeeinterest:readdaccount', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(535, 500, '{0,1,500}', '岗位责任人', 535, 'MENU_TYPE_MENU_GROUP', null, null, 'x-fa fa-comments', '1', null, null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(536, 535, '{0,1,500,535}', '变更申请', 536, 'MENU_TYPE_MENU_MENU', '/dktj/employeeinterest/positiontelleralterlist', 'dktjpositionteller', 'x-fa fa-comments', '1', 'dktj:employeeinterest:positiontelleralterlist', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(537, 536, '{0,1,500,535,536}', '变更', 537, 'MENU_TYPE_MENU_PERMISSION', '/dktj/employeeinterest/positiontelleralter', 'alter', 'x-fa fa-comments', '1', 'dktj:employeeinterest:positiontelleralter', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(538, 535, '{0,1,500,535}', '变更复核', 538, 'MENU_TYPE_MENU_MENU', '/dktj/employeeinterest/positiontellerchecklist', 'dktjpositionteller', 'x-fa fa-comments', '1', 'dktj:employeeinterest:positiontellerchecklist', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(539, 538, '{0,1,500,535,538}', '同意变更', 539, 'MENU_TYPE_MENU_PERMISSION', '/dktj/employeeinterest/positiontellercheck', 'check', 'x-fa fa-comments', '1', 'dktj:employeeinterest:positiontellercheck', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(540, 538, '{0,1,500,535,538}', '撤销变更', 540, 'MENU_TYPE_MENU_PERMISSION', '/dktj/employeeinterest/positiontelleruncheck', 'uncheck', 'x-fa fa-comments', '1', 'dktj:employeeinterest:positiontelleruncheck', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(542, 500, '{0,1,500}', '变更模板', 542, 'MENU_TYPE_MENU_MENU', '/dktj/template/getaccounttemplatelist', 'dktjaccounttemplate', 'x-fa fa-comments', '1', 'dktj:template:getaccounttemplatelist', null, null, 1, now(), 1, now(), false);
INSERT INTO bps_78000.t_sys_menu(id, parent_id, parent_ids, name, sort, type, uri, target, icon, is_show, permission, description, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES(543, 542, '{0,1,500,542}', '变更模板', 543, 'MENU_TYPE_MENU_PERMISSION', '/dktj/template/alteraccounttemplate', 'alter', 'x-fa fa-comments', '1', 'dktj:template:alteraccounttemplate', null, null, 1, now(), 1, now(), false);


INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 500, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 501, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 502, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 503, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 504, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 505, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 506, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 507, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 508, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 509, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 510, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 511, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 512, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 515, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 516, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 517, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 518, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 519, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 520, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 521, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 522, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 523, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 524, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 525, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 526, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 527, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 528, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 529, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 530, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 531, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 532, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 533, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 534, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 535, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 536, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 537, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 538, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 539, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 540, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 541, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 542, true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1, 543, true);

insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(200,'获取未绑定的贷款','/dktj/employeecustomer/getUnregisterCustomer','dktj:employeecustomer:getunregistercustomer','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(201,'绑定贷款账号到员工','/dktj/employeecustomer/registerEmployee','dktj:employeecustomer:registeremployee','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(202,'获取已绑定未复核的贷款账号列表','/dktj/employeecustomer/getUncheckedCustomer','dktj:employeecustomer:getuncheckedcustomer','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(203,'同意维护人绑定','/dktj/employeecustomer/checkRegisterEmployee','dktj:employeecustomer:checkregisteremployee','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(204,'拒绝复核维护人绑定','/dktj/employeecustomer/undoRegisterEmployee','dktj:employeecustomer:undoregisteremployee','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(205,'获取可变更维护人的客户列表','/dktj/employeecustomer/getModifiableCustomer','dktj:employeecustomer:getmodifiablecustomer','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(206,'提交变更客户维护人申请','/dktj/employeecustomer/modifyEmployee','dktj:employeecustomer:modifyemployee','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(207,'获取提交变更申请，但是未复核的客户列表','/dktj/employeecustomer/getModifiedUncheckedCustomer','dktj:employeecustomer:getmodifieduncheckedcustomer','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(208,'同意变更维护人','/dktj/employeecustomer/checkModifyEmployee','dktj:employeecustomer:checkmodifyemployee','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(209,'拒绝变更维护人','/dktj/employeecustomer/undoModifyEmployee','dktj:employeecustomer:undomodifyemployee','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(210,'获取贷款的维护人列表','/dktj/employeecustomer/get','dktj:employeecustomer:get','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(211,'获取贷款客户的固定流动状态 ','/dktj/employeecustomer/getEmployeeCustomer','dktj:employeecustomer:getstatus','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(212,'将客户由固定态改为流动态 ','/dktj/employeecustomer/changeStatusToFluid','dktj:employeecustomer:changestatustofluid','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(213,'将客户由流动态改为固定态 ','/dktj/employeecustomer/changeStatusToFix','dktj:employeecustomer:changestatustofix','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(214,'获取10个员工列表 ','/dktj/employeecustomer/userList','dktj:employeecustomer:userlist','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(215,'获取适用于表格的用户列表 ','/dktj/employeecustomer/tableUserList','dktj:employeecustomer:tableuserlist','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(216,'获取当前贷款最有可能的维护人 ','/dktj/employeecustomer/possibleTellerCode','dktj:employeecustomer:possibletellercode','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(217,'获取标记类型列表 ','/dktj/employeecustomer/getSpecialAccountTypeList','dktj:employeecustomer:getspecialaccounttypelist','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(218,'获取当前贷款跑批的最小日期 ','/dktj/employeecustomer/getLoanCurrentDate','dktj:employeecustomer:getloancurrentdate','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(219,'获取待补录的贷款账户列表 ','/dktj/employeeinterest/readd','dktj:employeeinterest:readd','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(220,'补录信贷维护人 ','/dktj/employeeinterest/readdaccount','dktj:employeeinterest:readdaccount','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(221,'获取员工时点贷款利息 ','/dktj/employeeinterest/empinterest','dktj:employeeinterest:empinterest','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(222,'获取员工汇总利息 ','/dktj/employeeinterest/empavginterest','dktj:employeeinterest:empavginterest','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(223,'机构时点利息 ','/dktj/employeeinterest/orginterest','dktj:employeeinterest:orginterest','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(224,'获取机构在一个区间段内的汇总利息 ','/dktj/employeeinterest/orgavginterest','dktj:employeeinterest:orgavginterest','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(225,'获取当前生效的账户岗位责任人列表 ','/dktj/employeeinterest/positiontelleralterlist','dktj:employeeinterest:positiontelleralterlist','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(226,'变更贷款岗位责任人 ','/dktj/employeeinterest/positiontelleralter','dktj:employeeinterest:positiontelleralter','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(227,'获取模板列表 ','/dktj/template/getTemplateList','dktj:template:gettemplatelist','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(228,'获取指定模板下的岗位列表及分成比例 ','/dktj/template/getTemplateDetailList','dktj:template:gettemplatedetaillist','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(229,'获取账号对应的模板列表 ','/dktj/template/getccounttemplatelist','dktj:template:getccounttemplatelist','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(230,'变更账号对应的模板 ','/dktj/template/alteraccounttemplate','dktj:template:alteraccounttemplate','手工写入权限',1,now(),1,now());



insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 200, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 201, 'DATA_SCOPE_ALL', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 202, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 203, 'DATA_SCOPE_ALL', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 204, 'DATA_SCOPE_ALL', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 205, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 206, 'DATA_SCOPE_ALL', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 207, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 208, 'DATA_SCOPE_ALL', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 209, 'DATA_SCOPE_ALL', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 210, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 211, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 212, 'DATA_SCOPE_ALL', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 213, 'DATA_SCOPE_ALL', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 214, 'DATA_SCOPE_ALL', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 215, 'DATA_SCOPE_ALL', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 216, 'DATA_SCOPE_ALL', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 217, 'DATA_SCOPE_ALL', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 218, 'DATA_SCOPE_ALL', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 219, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 220, 'DATA_SCOPE_ALL', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 221, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 222, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 223, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 224, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 225, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 226, 'DATA_SCOPE_ALL', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 227, 'DATA_SCOPE_ALL', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 228, 'DATA_SCOPE_ALL', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 229, 'DATA_SCOPE_ALL', now(), 1);
insert into bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by ) VALUES (1, 230, 'DATA_SCOPE_ALL', now(), 1);



--绑定模板表
INSERT INTO bps_78000.t_dktj_template(id, name, sort, valid_flag, invalid_date, create_time, create_by) VALUES (1, '实际岗位模板', 1, true, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template(id, name, sort, valid_flag, invalid_date, create_time, create_by) VALUES (2, '实际岗位模板2', 2, true, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template(id, name, sort, valid_flag, invalid_date, create_time, create_by) VALUES (3, '实际岗位模板3', 3, true, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template(id, name, sort, valid_flag, invalid_date, create_time, create_by) VALUES (4, '含贷审会模板', 4, true, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template(id, name, sort, valid_flag, invalid_date, create_time, create_by) VALUES (5, '含部门审批模板', 5, true, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template(id, name, sort, valid_flag, invalid_date, create_time, create_by) VALUES (6, '含贷审会审批模板', 6, true, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template(id, name, sort, valid_flag, invalid_date, create_time, create_by) VALUES (7, '合规员模板', 7, true, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template(id, name, sort, valid_flag, invalid_date, create_time, create_by) VALUES (8, '合规员总行审批模板', 8, true, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template(id, name, sort, valid_flag, invalid_date, create_time, create_by) VALUES (9, '合规员贷审会模板', 9, true, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template(id, name, sort, valid_flag, invalid_date, create_time, create_by) VALUES (10, '含贷审会审批模板（三个调查人）', 10, true, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template(id, name, sort, valid_flag, invalid_date, create_time, create_by) VALUES (11, '集中审查模板', 11, true, null, now(), 1);

INSERT INTO bps_78000.t_dktj_template(id, name, sort, valid_flag, invalid_date, create_time, create_by) VALUES (12, '支行审批权限（2个调查人）-2024', 12, true, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template(id, name, sort, valid_flag, invalid_date, create_time, create_by) VALUES (13, '支行审批权限（3个调查人）-2024', 13, true, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template(id, name, sort, valid_flag, invalid_date, create_time, create_by) VALUES (14, '授信管理部门审批模板-2024', 14, true, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template(id, name, sort, valid_flag, invalid_date, create_time, create_by) VALUES (15, '含贷审会、风委会审批模板-2024', 15, true, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template(id, name, sort, valid_flag, invalid_date, create_time, create_by) VALUES (16, '合规员模板-2024', 16, true, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template(id, name, sort, valid_flag, invalid_date, create_time, create_by) VALUES (17, '合规员总行审批模板-2024', 17, true, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template(id, name, sort, valid_flag, invalid_date, create_time, create_by) VALUES (18, '合规员贷审会模板-2024', 18, true, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template(id, name, sort, valid_flag, invalid_date, create_time, create_by) VALUES (19, '集中审查模板-2024', 19, true, null, now(), 1);

-- 岗位责任列表
INSERT INTO bps_78000.t_dktj_position(id, name, en_name, type, changeable, create_time, create_by) VALUES (1, '推荐人', 'TUI_JIAN_REN', '1', TRUE, now(), 1);
INSERT INTO bps_78000.t_dktj_position(id, name, en_name, type, changeable, create_time, create_by) VALUES (2, '调查人一', 'DIAO_CHA_REN_YI', '0', FALSE, now(), 1);
INSERT INTO bps_78000.t_dktj_position(id, name, en_name, type, changeable, create_time, create_by) VALUES (3, '调查人二', 'DIAO_CHA_REN_ER', '0', FALSE, now(), 1);
INSERT INTO bps_78000.t_dktj_position(id, name, en_name, type, changeable, create_time, create_by) VALUES (4, '审查人', 'SHEN_CHA_REN', '0', FALSE, now(), 1);
INSERT INTO bps_78000.t_dktj_position(id, name, en_name, type, changeable, create_time, create_by) VALUES (5, '审批人', 'SHEN_PI_REN', '0', FALSE, now(), 1);
INSERT INTO bps_78000.t_dktj_position(id, name, en_name, type, changeable, create_time, create_by) VALUES (6, '贷后维护人一', 'DAI_HOU_WEI_HU_REN_YI', '0', TRUE, now(), 1);
INSERT INTO bps_78000.t_dktj_position(id, name, en_name, type, changeable, create_time, create_by) VALUES (7, '贷后维护人二', 'DAI_HOU_WEI_HU_REN_ER', '0', TRUE, now(), 1);
INSERT INTO bps_78000.t_dktj_position(id, name, en_name, type, changeable, create_time, create_by) VALUES (8, '调查人三(行长)', 'DIAO_CHA_REN_SAN', '0', FALSE, now(), 1);
INSERT INTO bps_78000.t_dktj_position(id, name, en_name, type, changeable, create_time, create_by) VALUES (9, '贷审会', 'DAI_SHEN_HUI', '2', FALSE, now(), 1);
INSERT INTO bps_78000.t_dktj_position(id, name, en_name, type, changeable, create_time, create_by) VALUES (10, '总行审查岗', 'ZONG_HANG_SHEN_CHA', '0', FALSE, now(), 1);
INSERT INTO bps_78000.t_dktj_position(id, name, en_name, type, changeable, create_time, create_by) VALUES (11, '总行审批岗', 'ZONG_HANG_SHEN_PI', '0', FALSE, now(), 1);
INSERT INTO bps_78000.t_dktj_position(id, name, en_name, type, changeable, create_time, create_by) VALUES (12, '合规员', 'HE_GUI_YUAN', '0', FALSE, now(), 1);
INSERT INTO bps_78000.t_dktj_position(id, name, en_name, type, changeable, create_time, create_by) VALUES (13, '行长', 'HANG_ZHANG', '0', FALSE, now(), 1);

-- 模板岗位明细表
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(1, 1, 1, 0.2, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(2, 1, 2, 0.1, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(3, 1, 3, 0.1, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(4, 1, 4, 0.2, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(5, 1, 5, 0.3, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(6, 1, 6, 0.1, null, now(), 1);

INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(7, 2, 1, 0.2, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(8, 2, 2, 0.1, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(9, 2, 3, 0.1, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(10, 2, 4, 0.2, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(11, 2, 5, 0.3, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(12, 2, 6, 0.05, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(13, 2, 7, 0.05, null, now(), 1);

INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(14, 3, 1, 0.2, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(15, 3, 2, 0.1, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(16, 3, 3, 0.05, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(17, 3, 8, 0.05, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(18, 3, 4, 0.2, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(19, 3, 5, 0.3, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(20, 3, 6, 0.05, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(21, 3, 7, 0.05, null, now(), 1);

INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(22, 4, 1, 0.2, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(23, 4, 2, 0.1, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(24, 4, 3, 0.1, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(25, 4, 4, 0.2, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(26, 4, 9, 0.3, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(27, 4, 6, 0.05, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(28, 4, 7, 0.05, null, now(), 1);
--部门审批模板
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(29, 5, 1, 0.175, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(30, 5, 2, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(31, 5, 3, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(32, 5, 4, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(33, 5, 5, 0.175, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(34, 5, 6, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(35, 5, 7, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(36, 5, 10, 0.12, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(37, 5, 11, 0.18, null, now(), 1);
--新贷审会审批模板
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(38, 6, 1, 0.175, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(39, 6, 2, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(40, 6, 3, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(41, 6, 4, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(42, 6, 5, 0.175, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(43, 6, 6, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(44, 6, 7, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(45, 6, 10, 0.3, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(54, 6, 9, 0.0, null, now(), 1);
--合规员模板（支行权限）
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(46, 7, 1, 0.25, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(47, 7, 2, 0.1, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(48, 7, 3, 0.1, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(49, 7, 4, 0.1, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(50, 7, 5, 0.1, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(51, 7, 12, 0.15, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(52, 7, 6, 0.1, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(53, 7, 7, 0.1, null, now(), 1);
--合规员部门审批模板
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(55, 8, 1, 0.175, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(56, 8, 2, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(57, 8, 3, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(58, 8, 4, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(59, 8, 5, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(60, 8, 12, 0.105, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(61, 8, 6, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(62, 8, 7, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(63, 8, 10, 0.12, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(64, 8, 11, 0.18, null, now(), 1);
--合规员贷审会审批模板
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(65, 9, 1, 0.175, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(66, 9, 2, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(67, 9, 3, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(68, 9, 4, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(69, 9, 5, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(70, 9, 12, 0.105, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(71, 9, 6, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(72, 9, 7, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(73, 9, 10, 0.3, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(74, 9, 9, 0.0, null, now(), 1);
--新贷审会审批模板（东胜支行，三个调查人）
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(75, 10, 1, 0.175, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(76, 10, 2, 0.05, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(77, 10, 3, 0.05, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(78, 10, 8, 0.04, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(79, 10, 4, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(80, 10, 5, 0.175, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(81, 10, 6, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(82, 10, 7, 0.07, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(83, 10, 10, 0.3, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(84, 10, 9, 0.0, null, now(), 1);

--集中审查模板
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(85, 11, 1, 0.25, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(86, 11, 2, 0.13, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(87, 11, 3, 0.09, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(88, 11, 10, 0.08, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(89, 11, 11, 0.25, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(91, 11, 6, 0.12, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(92, 11, 7, 0.08, null, now(), 1);



--2024年模板- 支行审批权限（2个调查人）
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(93, 12, 1, 0.25, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(94, 12, 2, 0.13, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(95, 12, 3, 0.09, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(96, 12, 4, 0.08, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(97, 12, 5, 0.25, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(98, 12, 6, 0.12, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(99, 12, 7, 0.08, null, now(), 1);
--2024年模板- 支行审批权限（3个调查人）
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(100, 13, 1, 0.25, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(101, 13, 2, 0.13, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(102, 13, 3, 0.09, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(103, 13, 4, 0.08, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(104, 13, 5, 0.25, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(105, 13, 6, 0.12, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(106, 13, 7, 0.08, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(107, 13, 8, 0.00, null, now(), 1);
--2024年模板-  授信管理部门审批模板
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(108, 14, 1, 0.35, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(109, 14, 2, 0.20, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(110, 14, 3, 0.15, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(111, 14, 4, 0.00, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(112, 14, 5, 0.00, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(113, 14, 6, 0.15, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(114, 14, 7, 0.10, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(115, 14, 10, 0.00, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(116, 14, 11, 0.00, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(117, 14, 13, 0.05, null, now(), 1);
--2024年模板- 含贷审会、风委会审批模板
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(118, 15, 1, 0.35, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(119, 15, 2, 0.20, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(120, 15, 3, 0.15, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(121, 15, 4, 0.00, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(122, 15, 5, 0.00, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(123, 15, 6, 0.15, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(124, 15, 7, 0.10, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(125, 15, 9, 0.00, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(126, 15, 10, 0.00, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(127, 15, 13, 0.05, null, now(), 1);
--2024年模板- 合规员模板
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(128, 16, 1, 0.25, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(129, 16, 2, 0.13, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(130, 16, 3, 0.09, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(131, 16, 4, 0.08, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(132, 16, 5, 0.10, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(133, 16, 6, 0.12, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(134, 16, 7, 0.08, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(135, 16, 12, 0.15, null, now(), 1);
--2024年模板-  合规员总行审批模板
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(136, 17, 1, 0.35, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(137, 17, 2, 0.20, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(138, 17, 3, 0.15, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(139, 17, 4, 0.00, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(140, 17, 5, 0.00, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(141, 17, 6, 0.15, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(142, 17, 7, 0.10, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(143, 17, 10, 0.00, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(143, 17, 11, 0.00, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(143, 17, 12, 0.05, null, now(), 1);
--2024年模板-  合规员贷审会模板
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(144, 18, 1, 0.35, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(145, 18, 2, 0.20, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(146, 18, 3, 0.15, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(147, 18, 4, 0.00, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(148, 18, 5, 0.00, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(149, 18, 6, 0.15, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(150, 18, 7, 0.10, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(151, 18, 9, 0.00, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(152, 18, 10, 0.00, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(153, 18, 12, 0.05, null, now(), 1);
--2024年模板-  集中审查模板
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(154, 19, 1, 0.35, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(155, 19, 2, 0.20, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(156, 19, 3, 0.15, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(157, 19, 6, 0.15, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(158, 19, 7, 0.10, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(159, 19, 10, 0.00, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(160, 19, 11, 0.00, null, now(), 1);
INSERT INTO bps_78000.t_dktj_template_detail(id, template_id, position_id, percentage, remarks, create_time, create_by) VALUES(161, 19, 13, 0.05, null, now(), 1);









--配置贷款利息按分成模板分到每个员工的配置
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78003', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78004', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78005', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78006', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78007', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78011', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78013', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78014', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78016', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78018', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78020', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78022', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78024', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78026', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78028', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78029', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78030', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78031', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78033', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78035', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78037', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78038', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78039', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78051', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78040', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78042', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78043', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78044', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78045', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78046', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78047', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78050', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78053', null, '2021-01-01', 1, now());
INSERT INTO bps_78000.t_dktj_loan_handle_config(type, org_code, ln_curr_date, ln_next_date, update_by, update_time) VALUES ('7', '78054', null, '2021-01-01', 1, now());



INSERT INTO bps_78000.t_dktj_special_account_type(id, name, en_name, valid_flag, sort) VALUES (1, '公司业务中心贷款', 'GONG_SI_DAI_KUAN', TRUE,1);
INSERT INTO bps_78000.t_dktj_special_account_type(id, name, en_name, valid_flag, sort) VALUES (2, '小微业务中心贷款', 'XIAO_WEI_DAI_KUAN', TRUE,2);
INSERT INTO bps_78000.t_dktj_special_account_type(id, name, en_name, valid_flag, sort) VALUES (3, '通汇支行贷款', 'TONG_HUI_DAI_KUAN', TRUE,3);
INSERT INTO bps_78000.t_dktj_special_account_type(id, name, en_name, valid_flag, sort) VALUES (4, '阿吉奈支行贷款', 'A_JI_NAI_DAI_KUAN', TRUE,4);
INSERT INTO bps_78000.t_dktj_special_account_type(id, name, en_name, valid_flag, sort) VALUES (5, '金桌支行不良贷款', 'JIN_ZHUO_BU_LIANG_DAI_KUAN', FALSE,5);
INSERT INTO bps_78000.t_dktj_special_account_type(id, name, en_name, valid_flag, sort) VALUES (6, '扶贫、脱贫贷款', 'FU_PIN_DAI_KUAN', FALSE,6);
INSERT INTO bps_78000.t_dktj_special_account_type(id, name, en_name, valid_flag, sort) VALUES (7, '小微业务中心协助调查贷款', 'XIAO_WEI_XIE_ZHU_DAI_KUAN', FALSE,1);
INSERT INTO bps_78000.t_dktj_special_account_type(id, name, en_name, valid_flag, sort) VALUES (8, '公司业务中心协助调查贷款', 'GONG_SI_XIE_ZHU_DAI_KUAN', FALSE,2);
