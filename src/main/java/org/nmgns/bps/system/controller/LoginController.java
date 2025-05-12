package org.nmgns.bps.system.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.nmgns.bps.system.entity.Log;
import org.nmgns.bps.system.entity.TreeMenu;
import org.nmgns.bps.system.entity.User;
import org.nmgns.bps.system.service.LogService;
import org.nmgns.bps.system.service.MenuService;
import org.nmgns.bps.system.utils.DefaultConfig;
import org.nmgns.bps.system.utils.UserUtils;
import org.nmgns.bps.system.utils.base.ResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private UserUtils userUtils;
    @Autowired
    private MenuService menuService;
    @Autowired
    private LogService logService;

    @RequestMapping("/loginfailure")
    public ResponseJson loginFail() {
        ResponseJson responseJson = new ResponseJson();
        responseJson.setSuccess(Boolean.FALSE);
        responseJson.setMsg("登录失败，请重试");
        return responseJson;
    }

    @RequestMapping("/loginsuccess")
    public ResponseJson loginSuccess(HttpServletRequest request) {
        ResponseJson responseJson = new ResponseJson();
        Map<String, Object> retData = new HashMap<>(2);

        // 登录成功后的操作
        User currentUser = userUtils.getCurrentLoginedUser();
        if (currentUser != null) {

            // 记录用户登录的ip和时间
//            User user = new User();
//            user.setLoginTime(new Date());
//            user.setLoginIp(userUtils.getClientIp(request));
//            user.setId(currentUser.getId());
//            user.setUpdateBy(currentUser.getId());
//            user.setUpdateTime(new Date());
//            userService.updateUser(user);

            //写入操作日志
            Log log = new Log();
            log.setCreateBy(currentUser.getId());
            log.setOperation(DefaultConfig.LOG_IN_OPERATION);
            log.setCreateTime(new Date());
            log.setUserId(currentUser.getId());
            log.setRemarks("ip: "+userUtils.getClientIp(request));
            logService.insert(log);


            // 获取用户菜单
            List<TreeMenu> treeMenuList = menuService.getMenuTreeByUserId(currentUser.getId());
            currentUser.setUserMenuList(treeMenuList);
            retData.put("loggedIn", Boolean.TRUE);
            retData.put("userInfo", currentUser) ;

            responseJson.setData(retData);
            responseJson.setSuccess(Boolean.TRUE);
            responseJson.setMsg("登录成功!");

            // 写入session
            request.getSession().setAttribute("currentUser", currentUser);
        }

        return responseJson;
    }

    @RequestMapping("/loginstatus")
    public ResponseJson loginStatus() {
        ResponseJson responseJson = new ResponseJson();
        Map<String, Object> retData = new HashMap<>(2);
        User currentUser = userUtils.getCurrentLoginedUser();
        if (currentUser != null) {
            // 获取用户菜单
            List<TreeMenu> treeMenuList = menuService.getMenuTreeByUserId(currentUser.getId());
            currentUser.setUserMenuList(treeMenuList);
            retData.put("loggedIn", Boolean.TRUE);
            retData.put("userInfo", currentUser) ;
            responseJson.setSuccess(Boolean.TRUE);
        }
        else {
            retData.put("loggedIn", Boolean.FALSE);
            responseJson.setSuccess(Boolean.FALSE);
            responseJson.setMsg("用户未登录或登录已失效");
        }

        responseJson.setData(retData);
        return responseJson;
    }


}
