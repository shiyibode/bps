package org.nmgns.bps.system.service;

import cn.hutool.core.util.StrUtil;
import org.nmgns.bps.system.dao.ApiDao;
import org.nmgns.bps.system.dao.LogDao;
import org.nmgns.bps.system.dao.RoleDao;
import org.nmgns.bps.system.entity.Api;
import org.nmgns.bps.system.entity.Log;
import org.nmgns.bps.system.entity.RoleApi;
import org.nmgns.bps.system.utils.DefaultConfig;
import org.nmgns.bps.system.utils.PageData;
import org.nmgns.bps.system.utils.UserUtils;
import org.nmgns.bps.system.utils.base.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ApiService {

    @Autowired
    private ApiDao apiDao;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private LogDao logDao;
    @Autowired
    private RoleDao roleDao;

    /**
     *  通过id获取api
     * @param id 接口id
     */
    @Cacheable("apiById")
    public Api getApiById(Long id) {
        return apiDao.getApiById(id);
    }

    /**
     *  通过uri获取api
     * @param uri uri
     */
    @Cacheable("apiByUri")
    public Api getApiByUri(String uri) {
        return apiDao.getApiByUri(uri);
    }

    /**
     * 获取全部api
     */
    public List<Api> getAllApiList(){
        return apiDao.getAllApi();
    }

    /**
     * 新增接口
     * @param api 接口详细信息
     */
    @Transactional
    public void create(Api api) throws RuntimeException{
        if (null == api || StrUtil.isBlank(api.getPermission()) || StrUtil.isBlank(api.getName())) throw new RuntimeException("新增接口时未提供参数");

        api.setCreateBy(userUtils.getCurrentLoginedUser().getId());
        api.setCreateTime(new Date());
        apiDao.insert(api);

        //写入日志
        Log log = new Log("XJJK", "新建接口-"+api.getName(), new Date(), userUtils.getCurrentLoginedUser().getId());
        logDao.insert(log);
    }

    /**
     * 更新接口信息
     * @param api 更新后的接口信息
     */
    @Transactional
    @CacheEvict(value = {"apiById","apiByUri"}, allEntries = true)
    public void update(Api api) throws RuntimeException{
        if (null == api || api.getId() == null) throw new RuntimeException("更新接口时未提供参数");

        api.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
        api.setUpdateTime(new Date());
        apiDao.update(api);

        //写入日志
        Log log = new Log("XGJK", "修改接口-"+api.getName(), new Date(), userUtils.getCurrentLoginedUser().getId());
        logDao.insert(log);
    }

    /**
     * 删除接口
     * @param apiId 接口id
     */
    @Transactional
    @CacheEvict(value = {"apiById","apiByUri"}, allEntries = true)
    public void delete(Long apiId) throws RuntimeException{
        if (null == apiId) throw new RuntimeException("删除接口时未提供参数信息");
        Api dbApi = apiDao.getApiById(apiId);
        if (null == dbApi) throw new RuntimeException("接口不存在");

        //判断接口是否已经关联了角色，如果已经关联了角色则不允许删除
        List<RoleApi> roleApiList = roleDao.getRoleApiPermissionsByApiId(apiId);
        if (roleApiList != null && !roleApiList.isEmpty()) throw new RuntimeException("存在角色关联了该接口，无法删除");

        // 删除
        apiDao.delete(apiId);

        //写入日志
        Log log = new Log("SCJK", "删除接口-"+dbApi.getName(), new Date(), userUtils.getCurrentLoginedUser().getId());
        logDao.insert(log);
    }

    /**
     * 分页获取接口列表
     * @param api 参数信息，必须包含分页pageNo
     */
    public PageData<Api> getApiListPage(Api api) throws RuntimeException{
        if (api == null || api.getPage() == null) throw new RuntimeException("获取接口信息失败");

        //设置分页信息
        api.setPage(new Page(api.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));
        Long apiListCount = apiDao.getCount(api);
        List<Api> apiList = apiDao.get(api);

        PageData<Api> apiPageData = new PageData<>();
        apiPageData.setList(apiList);
        apiPageData.setTotal(apiListCount);
        apiPageData.setPageNo(api.getPageNo());
        apiPageData.setPageSize(DefaultConfig.DEFAULT_PAGE_SIZE);

        return apiPageData;
    }


}
