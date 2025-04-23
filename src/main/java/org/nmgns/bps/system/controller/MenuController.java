package org.nmgns.bps.system.controller;

import org.nmgns.bps.system.entity.Menu;
import org.nmgns.bps.system.entity.TreeMenu;
import org.nmgns.bps.system.service.MenuService;
import org.nmgns.bps.system.utils.PageData;
import org.nmgns.bps.system.utils.base.ResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sys/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @RequestMapping("/get")
    @PreAuthorize("hasAuthority('sys:menu:get')")
    public ResponseJson get(@RequestBody Menu menu) {
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<Menu> pageData = menuService.getMenuListPage(menu);
            responseJson.setTotal(pageData.getTotal());
            responseJson.setData(pageData.getList());
            responseJson.setSuccess(Boolean.TRUE);
            responseJson.setMsg("获取菜单列表成功");
        }catch (Exception e){
            responseJson.setSuccess(Boolean.FALSE);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @RequestMapping("/create")
    @PreAuthorize("hasAuthority('sys:menu:create')")
    public ResponseJson create(@RequestBody Menu menu) {
        ResponseJson responseJson = new ResponseJson();

        try {
            menuService.create(menu);
            responseJson.setSuccess(Boolean.TRUE);
            responseJson.setMsg("新增菜单成功");
        }catch (Exception e){
            responseJson.setSuccess(Boolean.FALSE);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    public ResponseJson update(@RequestBody Menu menu) {
        ResponseJson responseJson = new ResponseJson();

        try {
            menuService.update(menu);
            responseJson.setSuccess(Boolean.TRUE);
            responseJson.setMsg("修改菜单信息成功");
        }catch (Exception e){
            responseJson.setSuccess(Boolean.FALSE);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    public ResponseJson delete(@RequestParam("menuId") Long menuId) {
        ResponseJson responseJson = new ResponseJson();

        try {
            menuService.delete(menuId);
            responseJson.setSuccess(Boolean.TRUE);
            responseJson.setMsg("菜单删除成功");
        }catch (Exception e){
            responseJson.setSuccess(Boolean.FALSE);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    /**
     * 获取当前用户当前菜单下的权限列表，如当前用户超级管理员的“菜单管理”菜单下有：修改、新建、删除权限
     */
    @RequestMapping("/currentUser/currentMenuPermission")
    @PreAuthorize("hasAuthority('sys:menu:currentuser:currentmenupermission')")
    public ResponseJson getUserCurrentMenuPermission(@RequestParam("uri") String menuUri) {
        ResponseJson responseJson = new ResponseJson();

        try {
            List<TreeMenu> menuList = menuService.getCurrentMenuPermission(menuUri);
            responseJson.setSuccess(Boolean.TRUE);
            responseJson.setData(menuList);
            responseJson.setMsg("获取button列表成功");
        }catch (Exception e){
            responseJson.setSuccess(Boolean.FALSE);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }



}
