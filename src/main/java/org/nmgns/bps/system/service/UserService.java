package org.nmgns.bps.system.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.nmgns.bps.system.dao.*;
import org.nmgns.bps.system.entity.*;
import org.nmgns.bps.system.utils.DataScopeUtils;
import org.nmgns.bps.system.utils.DefaultConfig;
import org.nmgns.bps.system.utils.PageData;
import org.nmgns.bps.system.utils.UserUtils;
import org.nmgns.bps.system.utils.base.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private LogDao logDao;
    @Autowired
    private ApiDao apiDao;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private OrganizationDao organizationDao;

    @Cacheable("userByCode")
    public User getUserByCode(String code) {
        return userDao.getUserByCode(code);
    }

    @CacheEvict(value = "userByCode", allEntries = true)
    public void updateUser(User user){
        userDao.update(user);
    }

    @Transactional
    @CacheEvict(value = "userByCode", allEntries = true)
    public void updateLoginPassword(Long userId, String originalPassword, String newLoginPassword) throws RuntimeException{
        if (StrUtil.isBlank(newLoginPassword) || StrUtil.isBlank(originalPassword)) throw new RuntimeException("参数为空");
        if (StrUtil.length(newLoginPassword) < 6) throw new RuntimeException("密码必须超过6位数");

        // 如果前端上传了用户id，则修改指定用户的密码；如果前端未上传用户，则修改当前登录用户的密码
        User currentUser = userUtils.getCurrentLoginedUser();
        if(null == userId) userId = currentUser.getId();

        User dbUser = userDao.getUserById(userId);
        if (null == dbUser) throw new RuntimeException("用户不存在");

        // 判断旧密码是否正确
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String dbPassword = dbUser.getLoginPassword();
        if (StrUtil.isBlank(dbPassword)) throw new RuntimeException("用户不存在原始密码");
        if(!bCryptPasswordEncoder.matches(originalPassword, dbPassword)) throw new RuntimeException("旧密码不正确");

        // 更新密码
        User user = new User();
        user.setId(userId);
        String encryptPassword = bCryptPasswordEncoder.encode(newLoginPassword);
        user.setLoginPassword(encryptPassword);
        userDao.update(user);
    }

    @Transactional
    @CacheEvict(value = "userByCode", allEntries = true)
    public void resetPassword(Long userId) throws RuntimeException{
        if (userId == null ) throw new RuntimeException("参数为空");

        User dbUser = userDao.getUserById(userId);
        if (null == dbUser) throw new RuntimeException("用户不存在");

        User user = new User();
        user.setId(userId);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encryptPassword = bCryptPasswordEncoder.encode(DefaultConfig.RESET_ORIG_PASSWORD);
        user.setLoginPassword(encryptPassword);
        userDao.update(user);
    }

    /**
     * 修改用户的在职机构
     * @param userId 用户id
     * @param newOrganizationId 新机构id
     * @param remarks 备注
     */
    @Transactional
    public void alterOrganization(Long userId, Long newOrganizationId, String remarks) throws RuntimeException{
        if (null == userId) throw new RuntimeException("未提供参数");

        UserOrganization dbUserOrganization = userDao.getValidUserOrganizationByUserId(userId);
        if (null == dbUserOrganization) {
            throw new RuntimeException("机构调动失败, 获取用户机构信息时出错!");
        }

        //调入机构与当前机构为同一机构
        if (dbUserOrganization.getOrganizationId().equals(newOrganizationId)) {
            throw new RuntimeException("机构调动失败, 该用户当前机构与调入机构为同一机构!");
        }

        // 计算原机构的终止日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date today,yesterday;
        UserOrganization uo = new UserOrganization();
        try {
            today = sdf.parse(sdf.format(new Date()));
            calendar.setTime(today);
            calendar.add(Calendar.DATE,-1);
            yesterday = calendar.getTime();
        } catch (Exception e){
            throw new RuntimeException("机构调动失败，时间解析错误");
        }

        //同一天发生多次调动
        if (dbUserOrganization.getStartDate().equals(today)){
            uo.setParentId(dbUserOrganization.getParentId());  //设置新记录的parentId为原纪录的parentId
            userDao.deleteUserOrganizationById(dbUserOrganization.getId());  //删除当日的原记录
        }
        else{ //当日的第一次调动
            //更新原记录，使得原记录无效
            UserOrganization temp = new UserOrganization();
            temp.setId(dbUserOrganization.getId());
            temp.setEndDate(yesterday);
            temp.setValidFlag(false);
            temp.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
            temp.setUpdateTime(new Date());
            userDao.updateUserOrganizationById(temp);
            uo.setParentId(dbUserOrganization.getId());
        }

        //写入新记录信息
        uo.setUserId(userId);
        uo.setOrganizationId(newOrganizationId);
        uo.setStartDate(today);
        uo.setValidFlag(true);
        uo.setRemarks(remarks);
        uo.setCreateBy(userUtils.getCurrentLoginedUser().getId());
        uo.setCreateTime(new Date());
        userDao.insertUserOrganization(uo);

        //写入日志
        Log log = new Log();
        log.setUserId(userId);
        log.setRemarks("修改用户机构");
        log.setCreateTime(new Date());
        log.setCreateBy(userUtils.getCurrentLoginedUser().getId());
        log.setOperation("XGYHJG");
        logDao.insert(log);
    }


    /**
     * 修改用户角色
     * @param userId 用户id
     * @param roleIdList 角色id列表
     */
    @CacheEvict(value = {"userRoles"}, allEntries = true)
    @Transactional
    public void alterRole(Long userId, List<Long> roleIdList) {
        if (null == userId || null == roleIdList || roleIdList.isEmpty()) throw new RuntimeException("未提供参数");

        //删除原始全部角色
        roleDao.deleteUserRoleByUserId(userId);
        //新写入角色
        for (Long roleId:roleIdList){
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            roleDao.insertUserRole(userRole);
        }

        //写入日志
        Log log = new Log();
        log.setUserId(userId);
        log.setRemarks("修改用户角色");
        log.setCreateTime(new Date());
        log.setCreateBy(userUtils.getCurrentLoginedUser().getId());
        log.setOperation("XGYHJS");
        logDao.insert(log);
    }

    /**
     * 获取分页的用户信息，用于前端-系统设置-用户管理中的用户信息列表展示
     * @param userPara 参数信息，必须包含分页pageNo，允许使用搜索参数code(用户编号)、organizationId(机构id)
     */
    public PageData<User> getUserListPage(User userPara) throws RuntimeException{
        // 获取接口id，通过接口可以获取到数据范围
        Long apiId = apiDao.getApiByUri("/sys/user/get").getId();
        if (apiId == null) throw new RuntimeException("获取接口信息失败");
        Long paraOrganizationId = userPara.getOrganizationId();

        // 获取用户的在职机构
        User dbUser = userUtils.getCurrentLoginedUser();
        if (null == dbUser || dbUser.getId() == null || dbUser.getAdminFlag() == null) throw new RuntimeException("获取用户信息失败");
        if (!dbUser.isAdmin()){ //超级用户无机构，跳过
            UserOrganization uo = userDao.getValidUserOrganizationByUserId(dbUser.getId());
            if (uo ==null || uo.getOrganizationId() == null) throw new RuntimeException("获取用户在职机构失败");

            // 设置权限过滤时需要的当前在职机构的organizationId
            userPara.setOrganizationId(uo.getOrganizationId());
        }
        userPara.setAdminFlag(dbUser.getAdminFlag());   //设置adminFlag，用于在dsf中判断权限

        //设置权限过滤时需要的roleApiList
        List<RoleApi> roleApiList = roleDao.getRoleApiPermissionsByUserId(dbUser.getId());
        userPara.setRoleApiList(roleApiList);

        //设置权限信息
        HashMap<String, String> sqlMap = new HashMap<>();
        sqlMap.put("dsf", DataScopeUtils.dataScopeFilter(apiId, userPara, "o", null));
        userPara.setSqlMap(sqlMap);
        userPara.setOrganizationId(paraOrganizationId);   //机构权限已添加在dsf中，这里恢复为前端提交的organizationId，用于过滤搜索结果

        //设置分页信息
        userPara.setPage(new Page(userPara.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));
        Long userListCount = userDao.getCount(userPara);
        List<User> userList = userDao.get(userPara);

        // 设置用户的在职机构
        for (User u:userList){
            UserOrganization uo = userDao.getValidUserOrganizationByUserId(u.getId());
            if (null == uo) throw new RuntimeException("查找用户在职机构失败");

            Organization o = organizationDao.getOrganizationById(uo.getOrganizationId());
            uo.setOrganizationName(o.getName());
            uo.setOrganizationCode(o.getCode());
            u.setCurrentUserOrganization(uo);
        }

        //设置返回的职位信息、查找当前最新的状态信息、角色信息
        for (int i=0;i<userList.size();i++){
            userList.get(i).setPostStr(dictionaryService.getDictionaryNameByCode(userList.get(i).getPost()));
            userList.get(i).setStatusStr(dictionaryService.getDictionaryNameByCode(userList.get(i).getStatus()));

            List<Role> roleList = roleDao.getRoleListByUserId(userList.get(i).getId());
            userList.get(i).setRoleList(roleList);
        }

        PageData<User> userPageData = new PageData<>();
        userPageData.setList(userList);
        userPageData.setTotal(userListCount);
        userPageData.setPageNo(userPara.getPageNo());
        userPageData.setPageSize(DefaultConfig.DEFAULT_PAGE_SIZE);

        return userPageData;
    }

    /**
     * 新增用户
     * @param userPara 前端提交的用户基础信息
     */
    @Transactional
    public void create(User userPara) throws RuntimeException{
        if (null == userPara || StrUtil.isBlank(userPara.getCode()) || StrUtil.isBlank(userPara.getName()) || null == userPara.getEntryDate() || StrUtil.isBlank(userPara.getStatus()) || StrUtil.isBlank(userPara.getPost())) throw new RuntimeException("新增用户时未提供参数");

        //新增用户时,设置默认密码
        userPara.setLoginPassword(new BCryptPasswordEncoder().encode(DefaultConfig.RESET_ORIG_PASSWORD));
        userPara.setDelFlag(false);
        // 默认允许登录
        if(userPara.getLoginUsable() == null) userPara.setLoginUsable(Boolean.TRUE);
        // 默认非超级用户
        userPara.setAdminFlag(Boolean.FALSE);
        userPara.setCreateBy(userUtils.getCurrentLoginedUser().getId());
        userPara.setCreateTime(new Date());
        Dictionary dictionary = dictionaryService.getDictionaryByCode(userPara.getPost());
        if (null == dictionary) throw new RuntimeException("无效的用户职位代码");
        userDao.insert(userPara);
        if (userPara.getId() == null) throw new RuntimeException("新增用户失败");

        //写入用户机构
        UserOrganization uo = new UserOrganization();
        uo.setUserId(userPara.getId());
        uo.setOrganizationId(userPara.getOrganizationId());
        uo.setValidFlag(Boolean.TRUE);
        uo.setStartDate(userPara.getEntryDate());
        uo.setRemarks(userPara.getUserOrganizationRemarks());
        uo.setCreateBy(userUtils.getCurrentLoginedUser().getId());
        uo.setCreateTime(new Date());
        userDao.insertUserOrganization(uo);

        //写入用户状态
        UserStatus userStatus = new UserStatus();
        userStatus.setUserId(userPara.getId());
        userStatus.setStartDate(userPara.getEntryDate());
        dictionary = dictionaryService.getDictionaryByCode(userPara.getStatus());
        if (null == dictionary) throw new RuntimeException("无效的用户在职状态代码");
        userStatus.setStatus(userPara.getStatus());
        userStatus.setValidFlag(Boolean.TRUE);  //新增用户，默认设置用户状态有效
        userStatus.setCreateBy(userUtils.getCurrentLoginedUser().getId());
        userStatus.setCreateTime(new Date());
        userDao.insertUserStatus(userStatus);

        //写入用户职位
        UserPost userPost = new UserPost();
        userPost.setUserId(userPara.getId());
        userPost.setStartDate(userPara.getEntryDate());
        dictionary = dictionaryService.getDictionaryByCode(userPara.getPost());
        if (null == dictionary) throw new RuntimeException("无效的用户职位代码");
        userPost.setPost(userPara.getPost());
        userPost.setValidFlag(Boolean.TRUE);  //新增用户，默认设置用户状态有效
        userPost.setCreateBy(userUtils.getCurrentLoginedUser().getId());
        userPost.setCreateTime(new Date());
        userDao.insertUserPost(userPost);

        //写入日志
        Log log = new Log();
        log.setUserId(userPara.getId());
        log.setRemarks("新建用户");
        log.setCreateTime(new Date());
        log.setCreateBy(userUtils.getCurrentLoginedUser().getId());
        log.setOperation("XJYH");
        logDao.insert(log);
    }

    /**
     * 修改用户信息
     * @param userPara 前端提交的用户基础信息
     */
    @Transactional
    public void update(User userPara) throws RuntimeException{
        if (null == userPara || userPara.getId() == null ) throw new RuntimeException("修改用户信息时未提供参数");

        //只允许修改：name、mobile、identityNo、post、birthday、sex、remarks
        User dbUser = userDao.getUserById(userPara.getId());
        User tmpUser = new User();
        tmpUser.setId(userPara.getId());
        if (!StrUtil.equals(userPara.getName(), dbUser.getName())){
            tmpUser.setName(userPara.getName());
        }
        if (!StrUtil.equals(userPara.getMobile(), dbUser.getMobile())){
            tmpUser.setMobile(userPara.getMobile());
        }
        if (!StrUtil.equals(userPara.getIdentityNo(), dbUser.getIdentityNo())){
            tmpUser.setIdentityNo(userPara.getIdentityNo());
        }
        if (!StrUtil.equals(userPara.getSex(), dbUser.getSex())){
            tmpUser.setSex(userPara.getSex());
        }
        if (userPara.getBirthday() != null && !userPara.getBirthday().equals(dbUser.getBirthday())){
            tmpUser.setBirthday(userPara.getBirthday());
        }
        if (StrUtil.isNotBlank(userPara.getRemarks())){
            tmpUser.setRemarks(userPara.getRemarks());
        }
        userDao.update(tmpUser);

        // 允许修改用户在职状态
        if (StrUtil.isNotBlank(userPara.getStatus())){
            UserStatus dbUserStatus = userDao.getValidUserStatusByUserId(userPara.getId());
            if (!StrUtil.equals(userPara.getStatus(), dbUserStatus.getStatus())){
                //修改旧状态为无效
                UserStatus tmpUserStatus = new UserStatus();
                tmpUserStatus.setId(dbUserStatus.getId());
                tmpUserStatus.setEndDate(DateUtil.yesterday());
                tmpUserStatus.setValidFlag(Boolean.FALSE);
                tmpUserStatus.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
                tmpUserStatus.setUpdateTime(new Date());
                userDao.updateUserStatusById(tmpUserStatus);
                //写入新状态
                tmpUserStatus = new UserStatus();
                tmpUserStatus.setStartDate(DateUtil.parseDate(DateUtil.today()));
                tmpUserStatus.setStatus(userPara.getStatus());
                tmpUserStatus.setValidFlag(Boolean.TRUE);
                tmpUserStatus.setParentId(dbUserStatus.getId());
                tmpUserStatus.setCreateBy(userUtils.getCurrentLoginedUser().getId());
                tmpUserStatus.setCreateTime(new Date());
                userDao.insertUserStatus(tmpUserStatus);
            }
        }

        // 允许修改用户职位
        if (StrUtil.isNotBlank(userPara.getPost())){
            UserPost dbUserPost = userDao.getValidUserPostByUserId(userPara.getId());
            if (!StrUtil.equals(userPara.getPost(), dbUserPost.getPost())){
                //修改旧状态为无效
                UserPost tmpUserPost = new UserPost();
                tmpUserPost.setId(dbUserPost.getId());
                tmpUserPost.setEndDate(DateUtil.yesterday());
                tmpUserPost.setValidFlag(Boolean.FALSE);
                tmpUserPost.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
                tmpUserPost.setUpdateTime(new Date());
                userDao.updateUserPostById(tmpUserPost);
                //写入新状态
                tmpUserPost = new UserPost();
                tmpUserPost.setStartDate(DateUtil.parseDate(DateUtil.today()));
                tmpUserPost.setPost(userPara.getPost());
                tmpUserPost.setValidFlag(Boolean.TRUE);
                tmpUserPost.setParentId(dbUserPost.getId());
                tmpUserPost.setCreateBy(userUtils.getCurrentLoginedUser().getId());
                tmpUserPost.setCreateTime(new Date());
                userDao.insertUserPost(tmpUserPost);
            }
        }

        //写入日志
        Log log = new Log();
        log.setUserId(userPara.getId());
        log.setRemarks("修改用户基础信息");
        log.setCreateTime(new Date());
        log.setCreateBy(userUtils.getCurrentLoginedUser().getId());
        log.setOperation("XJYHJCXX");
        logDao.insert(log);
    }

    /**
     * 删除用户（逻辑删除）
     * @param userId 用户id
     */
    public void delete(Long userId) throws RuntimeException{
        if(userId == null ) throw new RuntimeException("删除用户时未提供参数");

        userDao.delete(userId);

        //写入日志
        Log log = new Log();
        log.setUserId(userId);
        log.setRemarks("删除用户");
        log.setCreateTime(new Date());
        log.setCreateBy(userUtils.getCurrentLoginedUser().getId());
        log.setOperation("SCYH");
        logDao.insert(log);
    }

    /**
     * 获取用户的职位类型列表（用于新增用户、修改用户职位）
     * @param name 用于搜索、职位类型名称
     */
    public List<Dictionary> getUserPostList(String name){
        return userDao.getUserPostList(name);
    }

    /**
     * 获取用户的在职状态类型列表（用于新增用户、修改用户在职状态）
     * @param name 用于搜索、状态类型名称
     */
    public List<Dictionary> getUserStatusList(String name){
        return userDao.getUserStatusList(name);
    }

    /**
     * 获取10个用户，codeOrName复用code字段，用作搜索参数
     * @param codeOrName 用户编号或名称
     */
    public List<User> getTenUsers(String codeOrName){
        List<User> userList = userDao.getTenUsers(new User(codeOrName));

        for (int i=0;i<userList.size();i++){
            UserOrganization uo = userDao.getValidUserOrganizationByUserId(userList.get(i).getId());
            Organization o = organizationDao.getOrganizationById(uo.getOrganizationId());
            uo.setOrganizationName(o.getName());
            uo.setOrganizationCode(o.getCode());
            userList.get(i).setCurrentUserOrganization(uo);
        }

        return userList;
    }

    /**
     * 通过用户id获取用户当前的在职机构
     * @param userId 用户id
     */
    public UserOrganization getValidUserOrganizationByUserId(Long userId){
        return userDao.getValidUserOrganizationByUserId(userId);
    }





}
