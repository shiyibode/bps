-- 菜单初始信息
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, locale, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (1, 0, '{0}', '系统菜单', 'MENU_TYPE_MENU_ROOT', '', '', '', 1, '1', '', 'x-fa fa-cog', 1, now(), 1, now(), '系统菜单根节点', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, locale, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (2, 1, '{0,1}', '系统设置', 'MENU_TYPE_MENU_GROUP', '/sys', 'menu.sys', '', 2, '1', '', 'x-fa fa-cog', 1, now(), 1, now(), '系统基本设置', false);

INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, locale, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (3, 2, '{0,1,2}', '菜单管理', 'MENU_TYPE_MENU_MENU', '/sys/menu', 'menu.sys.menu', 'sysmenu', 3, '1', 'sys:menu', 'x-fa fa-list', 1, now(), 1, now(), '系统菜单管理', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (4, 3, '{0,1,2,3}', '添加', 'MENU_TYPE_MENU_PERMISSION', '/sys/menu/create', 'addMenu', 4, '1', 'sys:menu:create', 'x-fa fa-plus-square', 1, now(), 1, now(), '添加菜单项', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (5, 3, '{0,1,2,3}', '删除', 'MENU_TYPE_MENU_PERMISSION', '/sys/menu/delete', 'deleteMenu', 5, '1', 'sys:menu:delete', 'x-fa fa-trash', 1, now(), 1, now(), '删除菜单项及子项', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (6, 3, '{0,1,2,3}', '编辑', 'MENU_TYPE_MENU_PERMISSION', '/sys/menu/update', 'editMenu', 6, '1', 'sys:menu:update', 'x-fa fa-edit', 1, now(), 1, now(), '编辑菜单项', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (7, 3, '{0,1,2,3}', '查看', 'MENU_TYPE_MENU_PERMISSION', '/sys/menu/get', 'viewMenu', 7, '1', 'sys:menu:get', 'x-fa fa-binoculars', 1, now(), 1, now(), '显示菜单项', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (8, 3, '{0,1,2,3}', '导出', 'MENU_TYPE_MENU_PERMISSION', '/sys/menu/export', 'exportMenu', 8, '1', 'sys:menu:export', 'x-fa fa-file-excel', 1, now(), 1, now(), '导出全部菜单数据', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (9, 3, '{0,1,2,3}', '打印', 'MENU_TYPE_MENU_PERMISSION', '/sys/menu/print', 'printMenu', 9, '1', 'sys:menu:print', 'x-fa fa-print', 1, now(), 1, now(), '打印全部菜单数据', false);

INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, locale, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (10, 2, '{0,1,2}', '机构管理', 'MENU_TYPE_MENU_MENU', '/sys/organization', 'menu.sys.organization', 'sysorganization', 10, '1', 'sys:organization', 'x-fa fa-sitemap', 1, now(), 1, now(), '系统机构信息管理', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (11, 10, '{0,1,2,10}', '添加', 'MENU_TYPE_MENU_PERMISSION', '/sys/organization/create', 'addOrganization', 11, '1', 'sys:organization:create', 'x-fa fa-plus-square', 1, now(), 1, now(), '添加机构', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (12, 10, '{0,1,2,10}', '删除', 'MENU_TYPE_MENU_PERMISSION', '/sys/organization/delete', 'deleteOrganization', 12, '1', 'sys:organization:delete', 'x-fa fa-trash', 1, now(), 1, now(), '删除机构及子机构', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (13, 10, '{0,1,2,10}', '编辑', 'MENU_TYPE_MENU_PERMISSION', '/sys/organization/update', 'editOrganization', 13, '1', 'sys:organization:update', 'x-fa fa-edit', 1, now(), 1, now(), '编辑机构', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (14, 10, '{0,1,2,10}', '查看', 'MENU_TYPE_MENU_PERMISSION', '/sys/organization/get', 'viewOrganization', 14, '1', 'sys:organization:get', 'x-fa fa-binoculars', 1, now(), 1, now(), '显示机构', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (15, 10, '{0,1,2,10}', '导出', 'MENU_TYPE_MENU_PERMISSION', '/sys/organization/export', 'exportOrganization', 15, '1', 'sys:organization:export', 'x-fa fa-file-excel', 1, now(), 1, now(), '导出全部机构信息', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (16, 10, '{0,1,2,10}', '打印', 'MENU_TYPE_MENU_PERMISSION', '/sys/organization/print', 'printOrganization', 16, '1', 'sys:organization:print', 'x-fa fa-print', 1, now(), 1, now(), '打印全部机构信息', false);

INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, locale, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (17, 2, '{0,1,2}', '角色管理', 'MENU_TYPE_MENU_MENU', '/sys/role', 'menu.sys.role', 'sysrole', 17, '1', 'sys:role', 'x-fa fa-tags', 1, now(), 1, now(), '系统角色管理', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (18, 17, '{0,1,2,17}', '添加', 'MENU_TYPE_MENU_PERMISSION', '/sys/role/create', 'addRole', 18, '1', 'sys:role:create', 'x-fa fa-plus-square', 1, now(), 1, now(), '添加角色', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (19, 17, '{0,1,2,17}', '删除', 'MENU_TYPE_MENU_PERMISSION', '/sys/role/delete', 'deleteRole', 19, '1', 'sys:role:delete', 'x-fa fa-trash', 1, now(), 1, now(), '删除角色', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (20, 17, '{0,1,2,17}', '编辑', 'MENU_TYPE_MENU_PERMISSION', '/sys/role/update', 'editRole', 20, '1', 'sys:role:update', 'x-fa fa-edit', 1, now(), 1, now(), '编辑角色', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (21, 17, '{0,1,2,17}', '查看', 'MENU_TYPE_MENU_PERMISSION', '/sys/role/get', 'viewRole', 21, '1', 'sys:role:get', 'x-fa fa-info', 1, now(), 1, now(), '显示角色', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (22, 17, '{0,1,2,17}', '导出', 'MENU_TYPE_MENU_PERMISSION', '/sys/role/export', 'exportRole', 21, '1', 'sys:role:export', 'x-fa fa-file-excel', 1, now(), 1, now(), '导出全部角色信息', false);


INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, locale, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (23, 2, '{0,1,2}', '用户管理', 'MENU_TYPE_MENU_MENU', '/sys/user', 'menu.sys.user', 'sysuser', 23, '1', 'sys:user', 'x-fa fa-user', 1, now(), 1, now(), '系统用户管理', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (24, 23, '{0,1,2,23}', '添加', 'MENU_TYPE_MENU_PERMISSION', '/sys/user/create', 'addUser', 24, '1', 'sys:user:create', 'x-fa fa-plus-square', 1, now(), 1, now(), '添加用户', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (25, 23, '{0,1,2,23}', '删除', 'MENU_TYPE_MENU_PERMISSION', '/sys/user/delete', 'deleteUser', 25, '1', 'sys:user:delete', 'x-fa fa-trash', 1, now(), 1, now(), '删除用户', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (26, 23, '{0,1,2,23}', '编辑', 'MENU_TYPE_MENU_PERMISSION', '/sys/user/update', 'editUser', 26, '1', 'sys:user:update', 'x-fa fa-edit', 1, now(), 1, now(), '编辑用户', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (27, 23, '{0,1,2,23}', '重置密码', 'MENU_TYPE_MENU_PERMISSION', '/sys/user/resetPassword', 'resetUserPassword', 27, '1', 'sys:user:resetpassword', 'x-fa fa-key', 1, now(), 1, now(), '重置用户密码', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (28, 23, '{0,1,2,23}', '修改机构', 'MENU_TYPE_MENU_PERMISSION', '/sys/user/updateOrganization', 'alterUserOrganization', 28, '1', 'sys:user:updateorganization', 'x-fa fa-users', 1, now(), 1, now(), '修改用户机构信息', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (29, 23, '{0,1,2,23}', '修改角色', 'MENU_TYPE_MENU_PERMISSION', '/sys/user/updateRole', 'alterUserRole', 29, '1', 'sys:user:updaterole', 'x-fa fa-tags', 1, now(), 1, now(), '修改用户角色信息', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (30, 23, '{0,1,2,23}', '查看', 'MENU_TYPE_MENU_PERMISSION', '/sys/user/get', 'viewUser', 30, '1', 'sys:user:get', 'x-fa fa-info', 1, now(), 1, now(), '显示用户', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (31, 23, '{0,1,2,23}', '导出', 'MENU_TYPE_MENU_PERMISSION', '/sys/user/export', 'exportUser', 31, '1', 'sys:user:export', 'x-fa fa-file-excel', 1, now(), 1, now(), '导出用户信息', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (32, 23, '{0,1,2,23}', '打印', 'MENU_TYPE_MENU_PERMISSION', '/sys/user/print', 'printUser', 32, '1', 'sys:user:print', 'x-fa fa-print', 1, now(), 1, now(), '打印用户信息', false);


INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, locale, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (33, 2, '{0,1,2}', '接口管理', 'MENU_TYPE_MENU_MENU', '/sys/api', 'menu.sys.api', 'sysapi', 33, '1', 'sys:api', 'x-fa fa-list', 1, now(), 1, now(), '接口管理', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (34, 33, '{0,1,2,33}', '添加', 'MENU_TYPE_MENU_PERMISSION', '/sys/api/create', 'addApi', 34, '1', 'sys:api:create', 'x-fa fa-plus-square', 1, now(), 1, now(), '添加接口', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (35, 33, '{0,1,2,33}', '删除', 'MENU_TYPE_MENU_PERMISSION', '/sys/api/delete', 'deleteApi', 35, '1', 'sys:api:delete', 'x-fa fa-trash', 1, now(), 1, now(), '删除接口', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (36, 33, '{0,1,2,33}', '编辑', 'MENU_TYPE_MENU_PERMISSION', '/sys/api/update', 'editApi', 36, '1', 'sys:api:update', 'x-fa fa-edit', 1, now(), 1, now(), '编辑接口', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (37, 33, '{0,1,2,33}', '绑定至角色', 'MENU_TYPE_MENU_PERMISSION', '/sys/permission/create', 'addPermission', 37, '1', 'sys:permission:create', 'x-fa fa-plus-square', 1, now(), 1, now(), '添加接口', false);

INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, locale, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (38, 2, '{0,1,2}', '角色权限', 'MENU_TYPE_MENU_MENU', '/sys/permission', 'menu.sys.permission', 'syspermission', 38, '1', 'sys:permission', 'x-fa fa-key', 1, now(), 1, now(), '角色权限管理', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (39, 38, '{0,1,2,38}', '删除', 'MENU_TYPE_MENU_PERMISSION', '/sys/permission/delete', 'deletePermission', 39, '1', 'sys:permission:delete', 'x-fa fa-trash', 1, now(), 1, now(), '删除接口', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (40, 3, '{0,1,2,3}', '绑定至角色', 'MENU_TYPE_MENU_PERMISSION', '', 'bindToRole', 40, '1', '', 'x-fa fa-plus-square', 1, now(), 1, now(), '绑定菜单至角色', false);

INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, locale, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (41, 2, '{0,1,2}', '角色菜单', 'MENU_TYPE_MENU_MENU', '/sys/rolemenu', 'menu.sys.rolemenu', 'sysrolemenu', 41, '1', '', 'x-fa fa-pencil-ruler', 1, now(), 1, now(), '角色菜单管理', false);
INSERT INTO bps_78000.t_sys_menu (id, parent_id, parent_ids, name, type, uri, target, sort, is_show, permission, icon, create_by, create_time, update_by, update_time, description, del_flag) VALUES (42, 41, '{0,1,2,41}', '删除', 'MENU_TYPE_MENU_PERMISSION', '/sys/rolemenu/delete', 'deleteRoleMenu', 42, '1', '', 'x-fa fa-trash', 1, now(), 1, now(), '删除角色和菜单的关联关系', false);
ALTER SEQUENCE bps_78000.t_sys_menu_id_seq RESTART WITH 43;
update bps_78000.t_sys_menu  set del_flag = true where name in ('查看','打印','导出'); -- 初始不显示这3个菜单，预留以后使用


-- 初始接口列表
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(1,'获取模块列表（模块也是菜单）','/sys/menu/menugroups','sys:menu:menugroups','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(2,'获取全部菜单，分页返回','/sys/menu/get','sys:menu:get','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(3,'新增菜单','/sys/menu/create','sys:menu:create','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(4,'修改菜单','/sys/menu/update','sys:menu:update','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(5,'删除菜单','/sys/menu/delete','sys:menu:delete','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(6,'用户在当前菜单下具有的操作按钮','/sys/menu/currentUser/currentMenuPermission','sys:menu:currentuser:currentmenupermission','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(7,'获取机构列表，分页返回','/sys/organization/get','sys:organization:get','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(8,'获取界面中显示的机构树列表','/sys/organization/getOrganizationTree','sys:organization:getorganizationtree','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(9,'新增机构','/sys/organization/create','sys:organization:create','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(10,'修改机构','/sys/organization/update','sys:organization:update','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(11,'删除机构','/sys/organization/delete','sys:organization:delete','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(12,'获取角色列表','/sys/role/get','sys:role:get','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(13,'创建角色','/sys/role/create','sys:role:create','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(14,'修改角色','/sys/role/update','sys:role:update','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(15,'删除角色','/sys/role/delete','sys:role:delete','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(16,'获取api列表','/sys/api/get','sys:api:get','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(17,'创建api','/sys/api/create','sys:api:create','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(18,'修改api','/sys/api/update','sys:api:update','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(19,'删除api','/sys/api/delete','sys:api:delete','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(20,'按明细设置用户角色的数据范围','/sys/permission/custom','sys:permission:custom','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(21,'获取角色接口关联关系，分页返回','/sys/permission/get','sys:permission:get','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(22,'给角色赋予接口','/sys/permission/create','sys:permission:create','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(23,'修改角色拥有的接口的数据范围','/sys/permission/update','sys:permission:update','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(24,'取消角色拥有的接口','/sys/permission/delete','sys:permission:delete','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(25,'修改用户密码','/sys/user/updatePassword','sys:user:updatepassword','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(26,'重置用户密码','/sys/user/resetPassword','sys:user:resetpassword','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(27,'修改用户在职机构','/sys/user/updateOrganization','sys:user:updateorganization','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(28,'修改用户拥有的角色','/sys/user/updateRole','sys:user:updaterole','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(29,'获取用户列表，分页返回','/sys/user/get','sys:user:get','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(30,'创建用户','/sys/user/create','sys:user:create','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(31,'修改用户','/sys/user/update','sys:user:update','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(32,'删除用户','/sys/user/delete','sys:user:delete','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(33,'获取最多10个角色，用于设置角色对应的接口时，获取角色列表','/sys/role/getTenRoles','sys:role:gettenroles','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(34,'绑定菜单至角色','/sys/role/bindMenuToRole','sys:role:bindmenutorole','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(35,'获取角色绑定的菜单列表','/sys/role/getRoleMenu','sys:role:getrolemenu','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(36,'删除角色绑定的菜单','/sys/role/deleteRoleMenu','sys:role:deleterolemenu','手工写入权限',1,now(),1,now());
insert into bps_78000.t_sys_api (id, name,uri,permission,remarks,create_by,create_time,update_by,update_time) values(37,'获取机构类型列表','/sys/organization/getOrganizationTypeList','sys:organization:getorganizationtypelist','手工写入权限',1,now(),1,now());



-- 角色-获取支行内全部机构列表的角色
INSERT INTO bps_78000.t_sys_role(id, name, en_name, role_type, is_sys, usable, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES (2, '支行内全部机构列表角色', 'zhihang_all_org', null, false, true, null, 1, now(), null, null, false);



-- 角色
INSERT INTO public.t_sys_user(id, code, login_password, name, phone, mobile, avatar, identity_no, birthday, sex, entry_date, post, login_usable, remarks, admin_flag, create_by, create_time, update_by, update_time, login_ip, login_time, del_flag) VALUES(2,'admin_78000', '$2a$10$GT.vY.k4l/iCY0rWk1bVQ.8SfSlBWL4BqUlMWdOi58Uy0QibAq4oW', '伊旗支行管理员', null, null, null, null, null, null, '2025-01-01', 'USER_POST_SUBBRANCH_OPERATOR', true, null, false, 1, now(), null, null, null, null, false);
INSERT INTO public.t_sys_user_status(user_id, start_date, end_date, status, valid_flag, parent_id, remarks, create_by, create_time) VALUES (2, '2025-01-01', null, 'USER_STATUS_NORMAL', true, null, null, 1, now());
INSERT INTO public.t_sys_user_organization(user_id, organization_id, start_date, end_date, valid_flag, parent_id, create_by, update_by, update_time) VALUES (2, 2002, '2025-01-01', null, true, null, 1, null, null);
INSERT INTO bps_78000.t_sys_role(id, name, en_name, role_type, is_sys, usable, remarks, create_by, create_time, update_by, update_time, del_flag) VALUES (1, '支行管理员', 'zhihang_admin', null, false, true, null, 1, now(), null, null, false);
INSERT INTO bps_78000.t_sys_user_role(user_id, role_id) VALUES (2, 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 1, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 2, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 3, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 4, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 5, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 6, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 7, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 8, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 9, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 10, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 11, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 12, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 13, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 14, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 15, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 16, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 17, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 18, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 19, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 20, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 21, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 22, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 23, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 24, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 25, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 26, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 27, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 28, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 29, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 30, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 31, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 32, 'DATA_SCOPE_ORGANIZATION_AND_CHILD', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 33, 'DATA_SCOPE_ALL', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 34, 'DATA_SCOPE_ALL', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 35, 'DATA_SCOPE_ALL', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 36, 'DATA_SCOPE_ALL', now(), 1);
INSERT INTO bps_78000.t_sys_role_permission(role_id, api_id, data_scope, create_time, create_by) VALUES (1, 37, 'DATA_SCOPE_ALL', now(), 1);

INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,1,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,2,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,3,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,4,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,5,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,6,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,7,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,8,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,9,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,10,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,11,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,12,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,13,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,14,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,15,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,16,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,17,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,18,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,19,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,20,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,21,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,22,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,23,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,24,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,25,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,26,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,27,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,28,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,29,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,30,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,31,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,32,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,33,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,34,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,35,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,36,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,37,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,38,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,39,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,40,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,41,true);
INSERT INTO bps_78000.t_sys_role_menu(role_id, menu_id, is_show) VALUES (1,42,true);


INSERT INTO public.t_sys_user(id, code, login_password, name, phone, mobile, avatar, identity_no, birthday, sex, entry_date, post, login_usable, remarks, admin_flag, create_by, create_time, update_by, update_time, login_ip, login_time, del_flag) VALUES(3,'103390', '$2a$10$GT.vY.k4l/iCY0rWk1bVQ.8SfSlBWL4BqUlMWdOi58Uy0QibAq4oW', '石伊波', null, null, null, null, null, null, '2025-01-01', 'USER_POST_SUBBRANCH_DEPARTMENT_EMPLOYEE', true, null, false, 1, now(), null, null, null, null, false);
INSERT INTO public.t_sys_user_status(user_id, start_date, end_date, status, valid_flag, parent_id, remarks, create_by, create_time) VALUES (3, '2025-01-01', null, 'USER_STATUS_NORMAL', true, null, null, 1, now());
INSERT INTO public.t_sys_user_organization(user_id, organization_id, start_date, end_date, valid_flag, parent_id, create_by, update_by, update_time) VALUES (3, 2003, '2025-01-01', null, true, null, 1, null, null);
INSERT INTO public.t_sys_user(id, code, login_password, name, phone, mobile, avatar, identity_no, birthday, sex, entry_date, post, login_usable, remarks, admin_flag, create_by, create_time, update_by, update_time, login_ip, login_time, del_flag) VALUES(4,'103387', '$2a$10$GT.vY.k4l/iCY0rWk1bVQ.8SfSlBWL4BqUlMWdOi58Uy0QibAq4oW', '陈建军', null, null, null, null, null, null, '2025-01-01', 'USER_POST_SUBBRANCH_TERMINAL_TELLER', true, null, false, 1, now(), null, null, null, null, false);
INSERT INTO public.t_sys_user_status(user_id, start_date, end_date, status, valid_flag, parent_id, remarks, create_by, create_time) VALUES (4, '2025-01-01', null, 'USER_STATUS_NORMAL', true, null, null, 1, now());
INSERT INTO public.t_sys_user_organization(user_id, organization_id, start_date, end_date, valid_flag, parent_id, create_by, update_by, update_time) VALUES (4, 2004, '2025-01-01', null, true, null, 1, null, null);