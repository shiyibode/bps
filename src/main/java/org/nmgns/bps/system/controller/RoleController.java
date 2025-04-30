package org.nmgns.bps.system.controller;

import org.nmgns.bps.system.entity.Dictionary;
import org.nmgns.bps.system.entity.Role;
import org.nmgns.bps.system.entity.RoleMenu;
import org.nmgns.bps.system.service.RoleService;
import org.nmgns.bps.system.utils.PageData;
import org.nmgns.bps.system.utils.base.Page;
import org.nmgns.bps.system.utils.base.ResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sys/role")
public class RoleController {

    @Autowired
    private RoleService roleService;


    @PreAuthorize("hasAuthority('sys:role:get')")
    @RequestMapping("/get")
    public ResponseJson get(@RequestBody Role role){
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<Role> pageData = roleService.getRoleListPage(role);
            responseJson.setTotal(pageData.getTotal());
            responseJson.setData(pageData.getList());
            responseJson.setSuccess(Boolean.TRUE);
            responseJson.setMsg("获取角色列表成功");
        }catch (Exception e){
            responseJson.setSuccess(Boolean.FALSE);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @PreAuthorize("hasAuthority('sys:role:create')")
    @RequestMapping("/create")
    public ResponseJson create(@RequestBody Role role){
        ResponseJson responseJson = new ResponseJson();

        try {
            roleService.create(role);
            responseJson.setSuccess(true);
            responseJson.setMsg("新增角色成功");
        } catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @PreAuthorize("hasAuthority('sys:role:update')")
    @RequestMapping("/update")
    public ResponseJson update(@RequestBody Role role){
        ResponseJson responseJson = new ResponseJson();

        try {
            roleService.update(role);
            responseJson.setSuccess(true);
            responseJson.setMsg("修改角色成功");
        } catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @PreAuthorize("hasAuthority('sys:role:delete')")
    @RequestMapping("/delete")
    public ResponseJson delete(@RequestParam("roleId") Long roleId){
        ResponseJson responseJson = new ResponseJson();

        try {
            roleService.delete(roleId);
            responseJson.setSuccess(true);
            responseJson.setMsg("删除角色成功");
        } catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @PreAuthorize("hasAuthority('sys:role:gettenroles')")
    @RequestMapping("/getTenRoles")
    public ResponseJson getTenRoles(@RequestParam(required = false,name = "name") String roleName){
        ResponseJson responseJson = new ResponseJson();

        try {
            List<Role> roleList = roleService.getTenRoles(roleName);
            responseJson.setData(roleList);
            responseJson.setSuccess(true);
            responseJson.setMsg("获取角色列表成功");
        } catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @PreAuthorize("hasAuthority('sys:role:bindmenutorole')")
    @RequestMapping("/bindMenuToRole")
    public ResponseJson bindMenuToRole(@RequestParam(name = "menuId") Long menuId, @RequestParam(name = "roleId") Long roleId){
        ResponseJson responseJson = new ResponseJson();

        try {
            roleService.bindMenuToRole(menuId, roleId);
            responseJson.setSuccess(true);
            responseJson.setMsg("菜单绑定角色成功");
        } catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @PreAuthorize("hasAuthority('sys:role:getrolemenu')")
    @RequestMapping("/getRoleMenu")
    public ResponseJson getRoleMenu(@RequestBody RoleMenu roleMenu) {
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<RoleMenu> pageData = roleService.getRoleMenuListPage(roleMenu);
            responseJson.setTotal(pageData.getTotal());
            responseJson.setData(pageData.getList());
            responseJson.setSuccess(Boolean.TRUE);
            responseJson.setMsg("获取菜单绑定角色成功");
        } catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @PreAuthorize("hasAuthority('sys:role:deleterolemenu')")
    @RequestMapping("/deleteRoleMenu")
    public ResponseJson deleteRoleMenu(@RequestBody RoleMenu roleMenu) {
        ResponseJson responseJson = new ResponseJson();

        try {
            roleService.deleteRoleMenu(roleMenu.getId());
            responseJson.setSuccess(true);
            responseJson.setMsg("菜单角色关联关系已取消");
        } catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @PreAuthorize("hasAuthority('sys:role:getdatascopelist')")
    @RequestMapping("/getDataScopeList")
    public ResponseJson getDataScopeList(){
        ResponseJson responseJson = new ResponseJson();

        try {
            List<Dictionary> dataScopeList = roleService.getDataScopeList();
            responseJson.setSuccess(true);
            responseJson.setTotal((long)dataScopeList.size());
            responseJson.setData(dataScopeList);
            responseJson.setMsg("获取数据范围类型成功");
        } catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }


}
