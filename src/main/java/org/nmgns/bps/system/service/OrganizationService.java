package org.nmgns.bps.system.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import org.nmgns.bps.system.dao.*;
import org.nmgns.bps.system.entity.*;
import org.nmgns.bps.system.entity.Dictionary;
import org.nmgns.bps.system.utils.*;
import org.nmgns.bps.system.utils.base.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private ApiDao apiDao;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private LogDao logDao;


    /**
     * 分页获取机构列表
     * @param orgPara 参数信息，必须包含分页pageNo，允许使用搜索参数code(用户编号)、id(机构id)
     * @return
     * @throws RuntimeException
     */
    public PageData<Organization> getOrganizationListPage(Organization orgPara) throws RuntimeException{
        // 获取接口id，通过接口可以获取到数据范围
        Long apiId = apiDao.getApiByUri("/sys/organization/get").getId();
        if (apiId == null) throw new RuntimeException("获取接口信息失败");

        // 获取当前用户及其在职机构
        User dbUser = userUtils.getCurrentLoginedUser();
        if (null == dbUser || dbUser.getId() == null || dbUser.getAdminFlag() == null) throw new RuntimeException("获取用户信息失败");
        if (!dbUser.isAdmin()){ //超级用户无机构，跳过
            UserOrganization uo = userDao.getValidUserOrganizationByUserId(dbUser.getId());
            if (uo ==null || uo.getOrganizationId() == null) throw new RuntimeException("获取用户在职机构失败");

            // 设置权限过滤时需要的当前在职机构的organizationId
            dbUser.setOrganizationId(uo.getOrganizationId());
        }

        //设置权限过滤时需要的roleApiList
        List<RoleApi> roleApiList = roleDao.getRoleApiPermissionsByUserId(dbUser.getId());
        dbUser.setRoleApiList(roleApiList);

        //设置权限信息
        HashMap<String, String> sqlMap = new HashMap<>();
        sqlMap.put("dsf", DataScopeUtils.dataScopeFilter(apiId, dbUser, "a", null));
        orgPara.setSqlMap(sqlMap);

        //设置分页信息
        orgPara.setPage(new Page(orgPara.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));
        Long orgListCount = organizationDao.getCount(orgPara);
        List<Organization> orgList = organizationDao.get(orgPara);

        //设置“类型”的中文名称
        for(int i=0;i<orgListCount;i++){
            orgList.get(i).setTypeStr(dictionaryService.getDictionaryNameByCode(orgList.get(i).getType()));
        }

        PageData<Organization> orgPageData = new PageData<>();
        orgPageData.setList(orgList);
        orgPageData.setTotal(orgListCount);
        orgPageData.setPageNo(orgPara.getPageNo());
        orgPageData.setPageSize(DefaultConfig.DEFAULT_PAGE_SIZE);

        return orgPageData;
    }

    /**
     * 新增机构
     * @param organization 前端提交的机构信息
     */
    @Transactional
    public void create(Organization organization) throws RuntimeException{
        if (organization == null || StrUtil.isBlank(organization.getCode()) || StrUtil.isBlank(organization.getName()) || organization.getSort() == null || StrUtil.isBlank(organization.getType()) || organization.getParentId() == null) throw new RuntimeException("新增机构时未提供参数");

        //获取上级机构
        Organization dbParentOrganization = organizationDao.getOrganizationById(organization.getParentId());

        //上级机构和其类型匹配
        if (dictionaryService.getDictionaryByCode(dbParentOrganization.getType()).getName().equals("总行")){
            if (!dictionaryService.getDictionaryByCode(organization.getType()).getName().equals("中心支行") && dictionaryService.getDictionaryByCode(organization.getType()).getName().equals("总行部门")) throw new RuntimeException("机构类型不符");
        }
        if (dictionaryService.getDictionaryByCode(dbParentOrganization.getType()).getName().equals("中心支行")){
            if (!dictionaryService.getDictionaryByCode(organization.getType()).getName().equals("中心支行部门") && dictionaryService.getDictionaryByCode(organization.getType()).getName().equals("支行")) throw new RuntimeException("机构类型不符");
        }
        if (dictionaryService.getDictionaryByCode(dbParentOrganization.getType()).getName().equals("支行")){
            if (!dictionaryService.getDictionaryByCode(organization.getType()).getName().equals("支行部门") && !dictionaryService.getDictionaryByCode(organization.getType()).getName().equals("网点")) throw new RuntimeException("机构类型不符");
        }

        //默认设置可以使用
        organization.setUsable(Boolean.TRUE);
        organization.setDelFlag(Boolean.FALSE);

        //获取所有上级机构,即parentIds
        List<Long> parentIds = dbParentOrganization.getParentIds();
        parentIds.add(dbParentOrganization.getId());
        organization.setParentIds(parentIds);

        organization.setCreateBy(userUtils.getCurrentLoginedUser().getId());
        organization.setCreateTime(new Date());
        organizationDao.insert(organization);

        //写入日志
        Log log = new Log("XJJG", "新建机构-"+organization.getName(), new Date(), userUtils.getCurrentLoginedUser().getId());
        logDao.insert(log);
    }

    /**
     * 修改机构信息
     * @param organization 修改后的机构信息，必须包含机构id
     */
    @Transactional
    public void update(Organization organization) throws RuntimeException{
        if (organization == null || organization.getId() == null) throw new RuntimeException("修改机构信息时未提供参数");

        organization.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
        organization.setUpdateTime(new Date());
        organizationDao.update(organization);

        //写入日志
        Log log = new Log("XGJG", "修改机构-"+organization.getName(), new Date(), userUtils.getCurrentLoginedUser().getId());
        logDao.insert(log);
    }

    /**
     * 删除机构（逻辑删除）
     * @param organizationId 机构id
     */
    @Transactional
    public void delete(Long organizationId) throws RuntimeException{
        if (organizationId == null ) throw new RuntimeException("删除机构信息时未提供参数");
        Organization dbOrganization = organizationDao.getOrganizationById(organizationId);

        // 删除本机构、删除所有下级机构
        List<Organization> orgAndChildOrgList = organizationDao.getOrgAndLowerLevelOrgListById(organizationId);
        for (Organization org : orgAndChildOrgList){
            organizationDao.delete(org.getId());
        }

        //写入日志
        Log log = new Log("SCJG", "删除机构-"+dbOrganization.getName(), new Date(), userUtils.getCurrentLoginedUser().getId());
        logDao.insert(log);
    }

    /**
     * 获取一个用户对应的机构树，机构树用于显示在界面中间。（特殊用户需要获取非在职机构的机构列表时，可以通过赋予角色来获取支行、中心支行、总行内的全部机构）
     * @return 树形结构的机构树
     */
    public List<TreeOrganization> getOrganizationTree() throws RuntimeException{
        //获取当前登录用户
        User currentUser = userUtils.getCurrentLoginedUser();
        if (null == currentUser || currentUser.getId() == null) throw new RuntimeException("获取当前用户失败");

        //获取当前用户的在职机构
        UserOrganization uo = userDao.getValidUserOrganizationByUserId(currentUser.getId());
        if (null == uo || uo.getOrganizationId() == null) throw new RuntimeException("获取用户在职机构失败");
        Long organizationId = uo.getOrganizationId();

        // 正常情况下，获取员工在职机构及其所有下级机构
        List<Organization> organizationList = organizationDao.getOrgAndLowerLevelOrgListById(organizationId);
        // 获取所有直属上级机构
        List<Organization> upperOrganizationList = organizationDao.getUpperLevelOrgListById(organizationId);
        organizationList.addAll(upperOrganizationList);

        //获取用户角色
        List<Role> roleList = roleDao.getRoleListByUserId(currentUser.getId());
        List<String> roleEnNameList = new ArrayList<String>();
        for (Role role : roleList){
            roleEnNameList.add(role.getName());
        }

        // 如果有“支行全部机构列表角色”，就将用户在职的支行内，全部部门和网点查找出来
        if (roleEnNameList.contains(DefaultConfig.ROLE_ZHI_HANG_ALL_ORG)){
            Organization zhiHang = null;
            for (Organization organization : upperOrganizationList){
                if (organization.getType().equals(DefaultConfig.ORGANIZATION_TYPE_ZHI_HANG)){
                    zhiHang = organization;
                }
            }
            if (zhiHang != null){
                List<Organization> zhiHangOrgList = organizationDao.getOrgAndLowerLevelOrgListById(zhiHang.getId());
                organizationList.addAll(zhiHangOrgList);
            }
        }

        // 如果有“中心支行全部机构列表角色”，就将用户在职的中心支行内，全部支行、部门和网点查找出来
        if (roleEnNameList.contains(DefaultConfig.ROLE_ZHONG_XIN_ZHI_HANG_ALL_ORG)){
            Organization zhongXinZhiHang = null;
            for (Organization organization : upperOrganizationList){
                if (organization.getType().equals(DefaultConfig.ORGANIZATION_TYPE_ZHONG_XIN_ZHI_HANG)){
                    zhongXinZhiHang = organization;
                }
            }
            if (zhongXinZhiHang != null){
                List<Organization> zhongXinZhiHangOrgList = organizationDao.getOrgAndLowerLevelOrgListById(zhongXinZhiHang.getId());
                organizationList.addAll(zhongXinZhiHangOrgList);
            }
        }

        // 如果有“总行全部机构列表角色”，就将总行内，全部部门、中心支行、支行和网点查找出来
        if (roleEnNameList.contains(DefaultConfig.ROLE_ZONG_HANG_ALL_ORG)){
            Organization zongHang = null;
            for (Organization organization : upperOrganizationList){
                if (organization.getType().equals(DefaultConfig.ORGANIZATION_TYPE_ZONG_HANG)){
                    zongHang = organization;
                }
            }
            if (zongHang != null){
                List<Organization> zongHangOrgList = organizationDao.getOrgAndLowerLevelOrgListById(zongHang.getId());
                organizationList.addAll(zongHangOrgList);
            }
        }

        // 去除机构的重复项
        Set<Organization> set = new LinkedHashSet<>(organizationList);
        List<Organization> distinctOrganizationList = new ArrayList<>(set);

        // 返回树结构的机构列表
        List<TreeOrganization> treeOrganizationList = new ArrayList<>();
        for (Organization org:distinctOrganizationList){
            TreeOrganization to = new TreeOrganization();
            BeanUtil.copyProperties(org, to);
            treeOrganizationList.add(to);
        }

        List treeList = TreeEntityUtils.listToTree(treeOrganizationList);
        return treeList;

    }

    /**
     * 获取机构类型列表
     */
    public List<Dictionary> getOrganizationTypeList(){
        return organizationDao.getOrganizationTypeList();
    }





}
