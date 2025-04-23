package org.nmgns.bps;

import cn.hutool.crypto.digest.MD5;
import org.junit.jupiter.api.Test;
import org.nmgns.bps.system.entity.TreeMenu;
import org.nmgns.bps.system.entity.User;
import org.nmgns.bps.system.service.MenuService;
import org.nmgns.bps.system.service.UserService;
import org.nmgns.bps.system.utils.PageData;
import org.nmgns.bps.system.utils.base.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootTest
class BpsApplicationTests {

    @Autowired
    UserService userService;

    @Test
    void contextLoads() {

        String newPassword = "123456";
//
//        MD5 md5 = new MD5(UserService.SALT.getBytes());
//        String newMd5Password = md5.digestHex(newPassword);
//
//        System.out.println(newMd5Password);


//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        System.out.println(bCryptPasswordEncoder.encode(newPassword));


//        Long userId = 2L;
//        List<TreeMenu> treeMenuList = menuService.getMenuTreeByUserId(userId);
//        System.out.println(treeMenuList);

//        PageData<User> userPage = userService.getUserListPage(1);
//        System.out.println(userPage.getTotal());
    }

}
