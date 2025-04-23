package org.nmgns.bps.system.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import org.nmgns.bps.system.dao.*;
import org.nmgns.bps.system.entity.*;
import org.nmgns.bps.system.utils.*;
import org.nmgns.bps.system.utils.base.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuDao menuDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LogDao logDao;
    @Autowired
    private ApiDao  apiDao;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private DictionaryService dictionaryService;

    /**
     * 获取用户拥有的所有菜单
     * @param userId 用户id
     * @return
     */
    public List<Menu> getMenuListByUserId(Long userId){
        return menuDao.getMenuListByUserId(userId);
    }


    /**
     * 获取用户拥有的菜单（树形结构）
     * @param userId 用户id
     */
    @Cacheable("userMenuTreeList")
    public List<TreeMenu> getMenuTreeByUserId(Long userId) throws RuntimeException{
        User user = userDao.getUserById(userId);
        if (null == user) throw new RuntimeException("员工不存在");

        List<Menu> menuList;
        if (user.isAdmin()) {
            menuList = menuDao.getAllMenuList();
        }else{
            menuList = getMenuListByUserId(userId);
        }
        List<TreeMenu> treeMenuList = new ArrayList<>();
        for (Menu menu:menuList){
            //前端显示的菜单树，不需要按钮，只返回根菜单、菜单组、菜单
            if (menu.getType().equals(DefaultConfig.MENU_TYPE_MENU_ROOT) || menu.getType().equals(DefaultConfig.MENU_TYPE_MENU_GROUP) || menu.getType().equals(DefaultConfig.MENU_TYPE_MENU_MENU)){
                TreeMenu tm = new TreeMenu();
                BeanUtil.copyProperties(menu, tm);
                treeMenuList.add(tm);
            }
        }

        List treeList = TreeEntityUtils.listToTree(treeMenuList);
        return treeList;
    }

    /**
     * 分页获取菜单列表
     * @param menuPara 参数信息，必须包含分页pageNo
     * @return 
     */
    public PageData<Menu> getMenuListPage(Menu menuPara) throws RuntimeException{
        // 获取接口id，通过接口可以获取到数据范围
        Long apiId = apiDao.getApiByUri("/sys/menu/get").getId();
        if (apiId == null) throw new RuntimeException("获取接口信息失败");

        //设置分页信息
        menuPara.setPage(new Page(menuPara.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));
        Long menuListCount = menuDao.getCount(menuPara);
        List<Menu> menuList = menuDao.get(menuPara);

        //设置“类型”的中文
        for (int i=0;i<menuList.size();i++){
            menuList.get(i).setTypeStr(dictionaryService.getDictionaryNameByCode(menuList.get(i).getType()));
        }

        PageData<Menu> menuPageData = new PageData<>();
        menuPageData.setList(menuList);
        menuPageData.setTotal(menuListCount);
        menuPageData.setPageNo(menuPara.getPageNo());
        menuPageData.setPageSize(DefaultConfig.DEFAULT_PAGE_SIZE);

        return menuPageData;
    }

    /**
     * 新增菜单
     * @param menu 前端提交的菜单信息
     */
    @Transactional
    @CacheEvict(value = {"menuPermissionButtons","userMenuTreeList"}, allEntries = true)
    public void create(Menu menu) throws RuntimeException{
        if (menu == null || StrUtil.isBlank(menu.getName()) || menu.getSort() == null || StrUtil.isBlank(menu.getType()) || menu.getParentId() == null || StrUtil.isBlank(menu.getIcon())) throw new RuntimeException("新增菜单时未提供参数");

        if (StrUtil.isBlank(menu.getIsShow())) menu.setIsShow(DefaultConfig.MENU_IS_SHOW_SHOW);

        //获取上级机构
        Menu dbParentMenu = menuDao.getMenuById(menu.getParentId());
        if (null == dbParentMenu) throw new RuntimeException("上级菜单不存在");

        //默认设置可以使用
        menu.setDelFlag(Boolean.FALSE);

        //获取所有上级机构,即parentIds
        List<Long> parentIds = dbParentMenu.getParentIds();
        parentIds.add(dbParentMenu.getId());
        menu.setParentIds(parentIds);

        menu.setCreateBy(userUtils.getCurrentLoginedUser().getId());
        menu.setCreateTime(new Date());
        menuDao.insert(menu);

        //写入日志
        Log log = new Log("XJCD", "新建菜单-"+menu.getName(), new Date(), userUtils.getCurrentLoginedUser().getId());
        logDao.insert(log);
    }

    /**
     * 修改菜单信息
     * @param menu 修改后的菜单信息，必须包含菜单id
     */
    @Transactional
    @CacheEvict(value = {"menuPermissionButtons","userMenuTreeList"}, allEntries = true)
    public void update(Menu menu) throws RuntimeException{
        if (menu == null || menu.getId() == null) throw new RuntimeException("修改菜单信息时未提供参数");

        menu.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
        menu.setUpdateTime(new Date());
        menuDao.update(menu);

        //写入日志
        Log log = new Log("XGCD", "修改菜单-"+menu.getName(), new Date(), userUtils.getCurrentLoginedUser().getId());
        logDao.insert(log);
    }

    /**
     * 删除机构（物理删除）
     * @param menuId 机构id
     */
    @Transactional
    @CacheEvict(value = {"menuPermissionButtons","userMenuTreeList"}, allEntries = true)
    public void delete(Long menuId) throws RuntimeException{
        if (menuId == null ) throw new RuntimeException("删除菜单信息时未提供参数");
        Menu dbMenu = menuDao.getMenuById(menuId);

        menuDao.delete(menuId);

        //写入日志
        Log log = new Log("SCCD", "删除菜单-"+dbMenu.getName(), new Date(), userUtils.getCurrentLoginedUser().getId());
        logDao.insert(log);
    }

    /**
     * 获取一个菜单的下级按钮
     * @param menuUri 菜单id
     */
    @Cacheable("menuPermissionButtons")
    public List<TreeMenu> getCurrentMenuPermission(String menuUri) throws RuntimeException{
        Menu dbMenu = menuDao.getMenuByUri(menuUri);
        if (null == dbMenu || dbMenu.getId() == null) throw new RuntimeException("通过uri获取菜单失败");

        //将类型是“按钮”的菜单筛选后返回
        if (dbMenu.getType().equals(DefaultConfig.MENU_TYPE_MENU_MENU)){
            List<Menu> childPermissionMenu = menuDao.getLowerLevelMenuById(dbMenu.getId());
            List<TreeMenu> childPermissionList = new ArrayList<>();
            for (Menu childMenu : childPermissionMenu){
                if (childMenu.getType().equals(DefaultConfig.MENU_TYPE_MENU_PERMISSION)){
                    TreeMenu tm = new TreeMenu();
                    BeanUtil.copyProperties(childMenu, tm);
                    childPermissionList.add(tm);
                }
            }
            return childPermissionList;
        } else{
            throw new RuntimeException("无下级按钮的菜单");
        }
    }





}
