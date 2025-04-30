package org.nmgns.bps.system.controller;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.nmgns.bps.system.entity.Dictionary;
import org.nmgns.bps.system.entity.Role;
import org.nmgns.bps.system.entity.User;
import org.nmgns.bps.system.service.UserService;
import org.nmgns.bps.system.utils.PageData;
import org.nmgns.bps.system.utils.base.ResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/test")
    public String testController(HttpServletRequest request) {
        Object currentUser = JSONUtil.parse(request.getSession().getAttribute("currentUser"));

        return currentUser.toString();
    }

    /**
     * 修改用户密码
     * @param originalPassword 原始密码
     * @param newLoginPassword 新密码，须大于等于6位
     */
    @PreAuthorize("hasAuthority('sys:user:updatepassword')")
    @RequestMapping("/updatePassword")
    public ResponseJson updatePassword(@RequestParam("originalPassword") String originalPassword, @RequestParam("newPassword") String newLoginPassword) {
        ResponseJson responseJson = new ResponseJson();

        try {
            userService.updateLoginPassword(null, originalPassword, newLoginPassword);
            responseJson.setSuccess(true);
            responseJson.setMsg("修改密码成功!");
        }catch (Exception e){
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    /**
     * 重置用户密码
     * @param id 用户id
     */
    @PreAuthorize("hasAuthority('sys:user:resetpassword')")
    @RequestMapping("/resetPassword")
    public ResponseJson resetPassword(@RequestParam("id") Long id) {
        ResponseJson responseJson = new ResponseJson();

        try {
            userService.resetPassword(id);
            responseJson.setSuccess(true);
            responseJson.setMsg("修改密码成功!");
        }catch (Exception e){
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        responseJson.setSuccess(true);
        responseJson.setMsg("密码重置成功!");
        return responseJson;
    }

    /**
     * 修改用户所在机构
     * @param organizationId 新机构id
     * @param remarks 备注
     */
    @PreAuthorize("hasAuthority('sys:user:updateorganization')")
    @RequestMapping("/updateOrganization")
    public ResponseJson updateOrganization(
                                           @RequestParam("userId") Long userId,
                                           @RequestParam("organizationId") Long organizationId,
                                           @RequestParam("remarks") String remarks) {
        ResponseJson responseJson = new ResponseJson();

        try {
            userService.alterOrganization(userId, organizationId, remarks);
            responseJson.setSuccess(true);
            responseJson.setMsg("用户机构调动成功!");
        }catch (Exception e){
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    /**
     * 修改用户角色
     * @param userId 用户id
     * @param roleIdList 角色id列表
     */
    @PreAuthorize("hasAuthority('sys:user:updaterole')")
    @RequestMapping("/updateRole")
    public ResponseJson updateRole(@RequestParam("userId") Long userId, @RequestParam("roleIdList") List<Long> roleIdList) {
        ResponseJson responseJson = new ResponseJson();

        try {
            userService.alterRole(userId, roleIdList);
            responseJson.setSuccess(true);
            responseJson.setMsg("设置角色成功");
        }catch (Exception e){
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    @PreAuthorize("hasAuthority('sys:user:get')")
    @RequestMapping("/get")
    public ResponseJson get(@RequestBody User user){
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<User> pageData = userService.getUserListPage(user);
            responseJson.setTotal(pageData.getTotal());
            responseJson.setData(pageData.getList());
            responseJson.setSuccess(Boolean.TRUE);
            responseJson.setMsg("获取用户列表成功");
        }catch (Exception e){
            responseJson.setSuccess(Boolean.FALSE);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @PreAuthorize("hasAuthority('sys:user:create')")
    @RequestMapping("/create")
    public ResponseJson create(@RequestBody User user){
        ResponseJson responseJson = new ResponseJson();

        try {
            userService.create(user);
            responseJson.setSuccess(true);
            responseJson.setMsg("新增用户成功");
        } catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @PreAuthorize("hasAuthority('sys:user:update')")
    @RequestMapping("update")
    public ResponseJson update(@RequestBody User user){
        ResponseJson responseJson = new ResponseJson();

        try {
            userService.update(user);
            responseJson.setSuccess(true);
            responseJson.setMsg("修改用户信息成功");
        } catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @PreAuthorize("hasAuthority('sys:user:delete')")
    @RequestMapping("/delete")
    public ResponseJson delete(@RequestParam("userId") Long userId) {
        ResponseJson responseJson = new ResponseJson();

        try {
            userService.delete(userId);
            responseJson.setSuccess(true);
            responseJson.setMsg("删除用户成功");
        } catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @PreAuthorize("hasAuthority('sys:user:getuserpostlist')")
    @RequestMapping("/getUserPostList")
    public ResponseJson getUserPostList(@RequestParam(required = false,name = "name") String postName){
        ResponseJson responseJson = new ResponseJson();

        try {
            List<Dictionary> userPostList = userService.getUserPostList(postName);
            responseJson.setData(userPostList);
            responseJson.setSuccess(true);
            responseJson.setMsg("获取职位类型成功");
        } catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @PreAuthorize("hasAuthority('sys:user:getuserstatuslist')")
    @RequestMapping("/getUserStatusList")
    public ResponseJson getUserStatusList(@RequestParam(required = false,name = "name") String statusName){
        ResponseJson responseJson = new ResponseJson();

        try {
            List<Dictionary> userStatusList = userService.getUserStatusList(statusName);
            responseJson.setData(userStatusList);
            responseJson.setSuccess(true);
            responseJson.setMsg("获取状态类型成功");
        } catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }


}
