package org.nmgns.bps.system.controller;

import org.nmgns.bps.system.entity.RoleApi;
import org.nmgns.bps.system.service.RoleService;
import org.nmgns.bps.system.utils.PageData;
import org.nmgns.bps.system.utils.base.ResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/permission")
public class RoleApiController {

    @Autowired
    private RoleService roleService;

    @PreAuthorize("hasAuthority('sys:permission:get')")
    @RequestMapping("/get")
    public ResponseJson get(@RequestBody RoleApi roleApi){
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<RoleApi> pageData = roleService.getRoleApiListPage(roleApi);
            responseJson.setTotal(pageData.getTotal());
            responseJson.setData(pageData.getList());
            responseJson.setSuccess(Boolean.TRUE);
            responseJson.setMsg("获取角色接口关联列表成功");
        }catch (Exception e){
            responseJson.setSuccess(Boolean.FALSE);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    /**
     * 关联角色与接口
     * @param roleApi 角色接口关联信息
     */
    @RequestMapping("/create")
    @PreAuthorize("hasAuthority('sys:permission:create')")
    public ResponseJson createRoleApi(@RequestBody RoleApi roleApi) {
        ResponseJson responseJson = new ResponseJson();

        try {
            roleService.insertRoleApi(roleApi);
            responseJson.setSuccess(Boolean.TRUE);
            responseJson.setMsg("角色关联接口成功");
        }catch (Exception e){
            responseJson.setSuccess(Boolean.FALSE);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    /**
     * 修改角色与接口
     * @param roleApi 角色接口关联信息
     */
    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('sys:permission:update')")
    public ResponseJson updateRoleApi(@RequestBody RoleApi roleApi) {
        ResponseJson responseJson = new ResponseJson();

        try {
            roleService.updateRoleApi(roleApi);
            responseJson.setSuccess(Boolean.TRUE);
            responseJson.setMsg("修改角色关联接口成功");
        }catch (Exception e){
            responseJson.setSuccess(Boolean.FALSE);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    /**
     * 删除角色与接口
     * @param roleApiId 角色接口关联id
     */
    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('sys:permission:delete')")
    public ResponseJson deleteRoleApi(@RequestParam("id") Long roleApiId) {
        ResponseJson responseJson = new ResponseJson();

        try {
            roleService.deleteRoleApi(roleApiId);
            responseJson.setSuccess(Boolean.TRUE);
            responseJson.setMsg("删除角色关联接口成功");
        }catch (Exception e){
            responseJson.setSuccess(Boolean.FALSE);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }


}
