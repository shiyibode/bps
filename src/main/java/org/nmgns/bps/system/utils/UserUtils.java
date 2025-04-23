package org.nmgns.bps.system.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.nmgns.bps.system.dao.RoleDao;
import org.nmgns.bps.system.dao.UserDao;
import org.nmgns.bps.system.entity.RoleApi;
import org.nmgns.bps.system.entity.User;
import org.nmgns.bps.system.entity.UserOrganization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户工具类,主要针对登录用的缓存设置
 */
@Slf4j
@Component
public class UserUtils {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;

    /**
     * 获取当前登录的用户
     */
    public User getCurrentLoginedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) return null;

        Object principal = authentication.getPrincipal();
        if (principal instanceof String) return null;

        // 获取springsecurity中保存的用户名称
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();

        // 查找用户详细信息
        User currentUser = userDao.getUserByCode(username);
        // 查找当前在职机构
        UserOrganization uo = userDao.getValidUserOrganizationByUserId(currentUser.getId());
        if (uo != null) {
            currentUser.setOrganizationId(uo.getOrganizationId());
        }

        return currentUser;
    }

    /**
     * 获取当前登录的用户，同时获取用户的角色
     */
    public User getCurrentLoginedUserIncludeRole(){
        User currentUser = getCurrentLoginedUser();

        List<RoleApi> roleApiList = roleDao.getRoleApiPermissionsByUserId(currentUser.getId());
        currentUser.setRoleApiList(roleApiList);

        return currentUser;
    }

    /**
     * 获取用户的真实ip
     * @param request
     */
    public String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");

        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0];
        }

        if (ipAddress != null && ipAddress.equals("0:0:0:0:0:0:0:1")){
            ipAddress = "127.0.0.1";
        }

        return ipAddress;
    }



}
