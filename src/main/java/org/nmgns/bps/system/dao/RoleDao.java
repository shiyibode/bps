package org.nmgns.bps.system.dao;


import org.apache.ibatis.annotations.Mapper;
import org.nmgns.bps.system.entity.*;

import java.util.List;

@Mapper
public interface RoleDao {

    public List<ApiPermission> getRoleApiPermissionsByRoleId(Long roleId);

    public List<Role> getRoleListByUserId(Long userId);

    public void deleteUserRole(UserRole userRole);

    public void deleteUserRoleByUserId(Long userId);

    public void insertUserRole(UserRole userRole);

    public List<RoleApi> getRoleApiPermissionsByUserId(Long userId);

    public List<RoleApi> getRoleApiPermissionsByApiId(Long apiId);

    public void insert(Role role);

    public void update(Role role);

    public void delete(Long roleId);

    public Long getCount(Role role);

    public List<Role> get(Role role);

    public Role getRoleById(Long roleId);

    //拥有该角色的用户数
    public Long getUserListCountByRoleId(Long roleId);

    public List<Dictionary> getDataScopeList();

    public void insertRoleApi(RoleApi roleApi);

    public void updateRoleApi(RoleApi roleApi);

    public void deleteRoleApi(Long roleApiId);

    public List<RoleApi> getRoleApiList(RoleApi roleApi);

    public Long getRoleApiListCount(RoleApi roleApi);

    public void insertRoleApiOrganization(RoleApi roleApi);

    public void deleteRoleApiOrganizationByRoleApiId(Long roleApiId);

    public RoleApi getRoleApiById(Long roleApiId);

    public List<Role> getTenRoles(Role role);

    public void insertRoleMenu(RoleMenu roleMenu);

    public void deleteRoleMenuById(Long id);

    public List<RoleMenu> getRoleMenuByMenuId(Long menuId);

    public Long getRoleMenuListCount(RoleMenu roleMenu);

    public List<RoleMenu> getRoleMenuList(RoleMenu roleMenu);

}
