package org.nmgns.bps.system.service;

import cn.hutool.core.util.StrUtil;
import org.nmgns.bps.system.dao.DictionaryDao;
import org.nmgns.bps.system.dao.LogDao;
import org.nmgns.bps.system.dao.MenuDao;
import org.nmgns.bps.system.dao.RoleDao;
import org.nmgns.bps.system.entity.*;
import org.nmgns.bps.system.utils.DefaultConfig;
import org.nmgns.bps.system.utils.PageData;
import org.nmgns.bps.system.utils.UserUtils;
import org.nmgns.bps.system.utils.base.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private LogDao logDao;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private MenuDao menuDao;


    /**
     * 获取角色拥有的接口列表
     * @param roleId 角色id
     */
    @Cacheable("roleApiPermissions")
    public List<ApiPermission> getRoleApiPermissionsByRoleId(Long roleId){
        if (null == roleId) return null;
        return roleDao.getRoleApiPermissionsByRoleId(roleId);
    }

    /**
     * 获取用户拥有的角色列表
     * @param userId 用户id
     */
    @Cacheable("userRoles")
    public List<Role> getRoleListByUserId(Long userId){
        if (null == userId) return null;
        return roleDao.getRoleListByUserId(userId);
    }

    /**
     * 新建角色
     * @param role 角色信息
     */
    @Transactional
    public void create(Role role) throws RuntimeException{
        if (role == null || StrUtil.isBlank(role.getName()) ) throw new RuntimeException("新增角色时未提供参数");

        //默认设置可以使用
        role.setUsable(Boolean.TRUE);
        role.setDelFlag(Boolean.FALSE);

        role.setCreateBy(userUtils.getCurrentLoginedUser().getId());
        role.setCreateTime(new Date());
        roleDao.insert(role);

        //写入日志
        Log log = new Log("XJJS", "新建角色-"+role.getName(), new Date(), userUtils.getCurrentLoginedUser().getId());
        logDao.insert(log);
    }

    /**
     * 修改角色信息
     * @param role 修改后的角色信息，必须包含角色id
     */
    @Transactional
    @CacheEvict(value = {"userRoles"}, allEntries = true)
    public void update(Role role) throws RuntimeException{
        if (role == null || role.getId() == null) throw new RuntimeException("修改角色信息时未提供参数");

        role.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
        role.setUpdateTime(new Date());
        roleDao.update(role);

        //写入日志
        Log log = new Log("XGJS", "修改角色-"+role.getName(), new Date(), userUtils.getCurrentLoginedUser().getId());
        logDao.insert(log);
    }

    /**
     * 删除角色（物理删除）
     * @param roleId 角色id
     */
    @Transactional
    public void delete(Long roleId) throws RuntimeException{
        if (roleId == null ) throw new RuntimeException("删除角色信息时未提供参数");
        Role dbRole = roleDao.getRoleById(roleId);
        if (null == dbRole || dbRole.getId() == null) throw new RuntimeException("角色不存在");

        // 判断角色是否存在接口列表
        List<ApiPermission> roleApiList = roleDao.getRoleApiPermissionsByRoleId(roleId);
        if (null != roleApiList && !roleApiList.isEmpty()) throw new RuntimeException("角色存在接口，禁止删除");

        // 判断角色是否存在关联用户
        Long userCount = roleDao.getUserListCountByRoleId(roleId);
        if (userCount > 0) throw new RuntimeException("角色存在关联用户，禁止删除");

        roleDao.delete(roleId);

        //写入日志
        Log log = new Log("SCJG", "删除角色-"+dbRole.getName(), new Date(), userUtils.getCurrentLoginedUser().getId());
        logDao.insert(log);
    }

    /**
     * 分页获取角色列表
     * @param role 参数信息，必须包含分页pageNo
     */
    public PageData<Role> getRoleListPage(Role role) throws RuntimeException{
        if (role == null || role.getPage() == null) throw new RuntimeException("获取角色信息失败");

        //设置分页信息
        role.setPage(new Page(role.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));
        Long roleListCount = roleDao.getCount(role);
        List<Role> roleList = roleDao.get(role);

        PageData<Role> rolePageData = new PageData<>();
        rolePageData.setList(roleList);
        rolePageData.setTotal(roleListCount);
        rolePageData.setPageNo(role.getPageNo());
        rolePageData.setPageSize(DefaultConfig.DEFAULT_PAGE_SIZE);

        return rolePageData;
    }

    /**
     * 新增角色和接口关联，同时赋予数据范围
     * @param roleApi 角色接口关联信息
     */
    @Transactional
    @CacheEvict(value = {"roleApiPermissions"}, allEntries = true)
    public void insertRoleApi(RoleApi roleApi) throws RuntimeException{
        if (null == roleApi || roleApi.getApiId() == null || roleApi.getRoleId() == null || StrUtil.isBlank(roleApi.getDataScope())) throw new RuntimeException("角色关联接口时未提供参数");

        // 判断数据范围是否是规定的数据范围
        boolean flag = false;
        List<Dictionary> dataScopeList = roleDao.getDataScopeList();
        for (Dictionary dataScope : dataScopeList){
            if (dataScope.getCode().equals(roleApi.getDataScope())) flag = true;
        }
        if (!flag) throw new RuntimeException("数据范围不存在: "+roleApi.getDataScope());

        roleApi.setId(null);
        roleDao.insertRoleApi(roleApi);
        if (roleApi.getId() == null) throw new RuntimeException("写入角色-接口后，未返回id");

        //如果为按明细设置，需要写入新的角色-接口-机构映射关系
        if (roleApi.getDataScope().equals(DefaultConfig.DATA_SCOPE_CUSTOM)){
            roleDao.insertRoleApiOrganization(roleApi);
        }

        //写入日志
        Log log = new Log("XZJSJKGL", "新增角色接口关联-角色id: "+roleApi.getRoleId()+" 接口id: "+roleApi.getApiId()+" 权限范围: "+roleApi.getDataScope(), new Date(), userUtils.getCurrentLoginedUser().getId());
        logDao.insert(log);
    }

    /**
     * 修改角色和接口关联，此处只允许修改数据范围（如果需要取消关联，使用删除功能）
     * @param roleApi 角色接口关联信息
     */
    @Transactional
    @CacheEvict(value = {"roleApiPermissions"}, allEntries = true)
    public void updateRoleApi(RoleApi roleApi) throws RuntimeException{
        if (null == roleApi || roleApi.getId() == null || StrUtil.isBlank(roleApi.getDataScope())) throw new RuntimeException("修改角色关联接口时未提供参数");

        // 判断数据范围是否是规定的数据范围
        boolean flag = false;
        List<Dictionary> dataScopeList = roleDao.getDataScopeList();
        for (Dictionary dataScope : dataScopeList){
            if (dataScope.getCode().equals(roleApi.getDataScope())) flag = true;
        }
        if (!flag) throw new RuntimeException("数据范围不存在: "+roleApi.getDataScope());

        RoleApi dbRoleApi = roleDao.getRoleApiById(roleApi.getId());
        if (dbRoleApi == null) throw new RuntimeException("roleApi不存在");

        // 删除旧关联关系
        roleDao.deleteRoleApi(roleApi.getId());
        // 如果是按明细设置的角色，还要删除关联的机构
        if (dbRoleApi.getDataScope().equals(DefaultConfig.DATA_SCOPE_CUSTOM)){
            roleDao.deleteRoleApiOrganizationByRoleApiId(roleApi.getId());
        }
        // 写入新关联关系
        roleApi.setId(null);
        roleDao.insertRoleApi(roleApi);
        if (roleApi.getId() == null) throw new RuntimeException("写入角色-接口后，未返回id");

        //如果为按明细设置，需要写入新的角色-接口-机构映射关系
        if (roleApi.getDataScope().equals(DefaultConfig.DATA_SCOPE_CUSTOM)){
            roleDao.insertRoleApiOrganization(roleApi);
        }

        Log log = new Log("XGJSJKGL", "修改角色接口关联-id: "+roleApi.getId()+" 权限范围: "+roleApi.getDataScope(), new Date(), userUtils.getCurrentLoginedUser().getId());
        logDao.insert(log);
    }

    /**
     * 删除角色与接口的关联关系
     * @param roleApiId 角色接口关联id
     */
    @Transactional
    @CacheEvict(value = {"roleApiPermissions"}, allEntries = true)
    public void deleteRoleApi(Long roleApiId) throws RuntimeException{
        if (roleApiId == null) throw new RuntimeException("删除角色与接口关联关系时未提供参数");

        RoleApi dbRoleApi = roleDao.getRoleApiById(roleApiId);
        if (dbRoleApi == null) throw new RuntimeException("roleApi不存在");

        //删除t_sys_role_permission表中数据
        roleDao.deleteRoleApi(roleApiId);
        //删除按明细设置表中数据,t_sys_role_permission_organization
        if (dbRoleApi.getDataScope().equals(DefaultConfig.DATA_SCOPE_CUSTOM)){
            roleDao.deleteRoleApiOrganizationByRoleApiId(roleApiId);
        }

        Log log = new Log("SCJSJKGL", "删除角色接口关联-id: "+roleApiId, new Date(), userUtils.getCurrentLoginedUser().getId());
        logDao.insert(log);
    }

    /**
     * 分页获取角色接口关联列表
     * @param roleApi 参数信息，必须包含分页pageNo
     */
    public PageData<RoleApi> getRoleApiListPage(RoleApi roleApi) throws RuntimeException{
        if (roleApi == null || roleApi.getPage() == null) throw new RuntimeException("获取角色接口关联信息失败");

        //设置分页信息
        roleApi.setPage(new Page(roleApi.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));
        Long roleApiListCount = roleDao.getRoleApiListCount(roleApi);
        List<RoleApi> roleApiList = roleDao.getRoleApiList(roleApi);

        //获取和设置dataScope的中文名称
        for (int i=0; i<roleApiList.size();i++){
            String dataScope = roleApiList.get(i).getDataScope();
            String dataScopeStr = dictionaryService.getDictionaryNameByCode(dataScope);
            roleApiList.get(i).setDataScopeStr(dataScopeStr);
        }

        PageData<RoleApi> roleApiPageData = new PageData<>();
        roleApiPageData.setList(roleApiList);
        roleApiPageData.setTotal(roleApiListCount);
        roleApiPageData.setPageNo(roleApi.getPageNo());
        roleApiPageData.setPageSize(DefaultConfig.DEFAULT_PAGE_SIZE);

        return roleApiPageData;
    }


    /**
     * 获取十个角色列表，用于前端绑定角色的接口时，首先获取的角色列表
     * @param name 角色名称，用于模糊搜索
     */
    public List<Role> getTenRoles(String name) throws RuntimeException{
        Role tmpRole = new Role();
        tmpRole.setName(name);
        return roleDao.getTenRoles(tmpRole);
    }

    /**
     * 绑定菜单至角色
     * @param menuId 菜单id
     * @param roleId 角色id
     */
    public void bindMenuToRole(Long menuId, Long roleId) throws RuntimeException{
        if (null == menuId || null == roleId) throw new RuntimeException("参数未提供给");

        Menu menu = menuDao.getMenuById(menuId);
        if(null == menu || menu.getId() == null) throw new RuntimeException("菜单不存在");

        Role role = roleDao.getRoleById(roleId);
        if (null == role || role.getId() == null) throw new RuntimeException("角色不存在");

        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setRole(role);
        roleMenu.setMenu(menu);
        roleMenu.setIsShow(true);

        roleDao.insertRoleMenu(roleMenu);
    }

    /**
     * 分页获取角色菜单关联列表
     * @param roleMenu 参数信息，必须包含分页pageNo
     */
    public PageData<RoleMenu> getRoleMenuListPage(RoleMenu roleMenu) throws RuntimeException{
        if (roleMenu == null || roleMenu.getPage() == null) throw new RuntimeException("获取角色菜单关联信息失败");

        //设置分页信息
        roleMenu.setPage(new Page(roleMenu.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));
        Long roleMenuListCount = roleDao.getRoleMenuListCount(roleMenu);
        List<RoleMenu> roleMenuList = roleDao.getRoleMenuList(roleMenu);

        PageData<RoleMenu> roleMenuPageData = new PageData<>();
        roleMenuPageData.setList(roleMenuList);
        roleMenuPageData.setTotal(roleMenuListCount);
        roleMenuPageData.setPageNo(roleMenu.getPageNo());
        roleMenuPageData.setPageSize(DefaultConfig.DEFAULT_PAGE_SIZE);

        return roleMenuPageData;
    }

    public void deleteRoleMenu(Long id){
        if (null == id) throw new RuntimeException("未提供参数");
        roleDao.deleteRoleMenuById(id);
    }



}
