package org.nmgns.bps.system.config;

import org.nmgns.bps.system.entity.Api;
import org.nmgns.bps.system.entity.ApiPermission;
import org.nmgns.bps.system.entity.Role;
import org.nmgns.bps.system.service.ApiService;
import org.nmgns.bps.system.service.RoleService;
import org.nmgns.bps.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private ApiService apiService;
    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


       org.nmgns.bps.system.entity.User currentUser = userService.getUserByCode(username);

        String password ;
        if (currentUser == null){
            throw new UsernameNotFoundException(username + "不存在");
        }
        else {
            password = currentUser.getLoginPassword();
        }

        //授权，超级用户授予全部接口权限
        List<GrantedAuthority> authorityList = new ArrayList<>();
//        List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN,USER");
//        authorityList.add(new SimpleGrantedAuthority("user:add"));
        if(currentUser.isAdmin()){
            List<Api> apiList = apiService.getAllApiList();
            for (Api api : apiList){
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(api.getPermission());
                authorityList.add(simpleGrantedAuthority);
            }
        }
        else {
            //获取用户角色
            List<Role> userRoleList = roleService.getRoleListByUserId(currentUser.getId());
            // 获取角色对应的api和api对应的接口权限，并加入到security中
            for (Role r : userRoleList){
                List<ApiPermission> roleApiPermissionList = roleService.getRoleApiPermissionsByRoleId(r.getId());

                for (ApiPermission apiPermission : roleApiPermissionList){
                    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(apiPermission.getApi().getPermission());
                    authorityList.add(simpleGrantedAuthority);
                }
            }
        }


        return new User(
                username,
                password,
                true,
                true,
                true,
                true,
                authorityList
        );
    }
}
