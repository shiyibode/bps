package org.nmgns.bps.dktj.service;

import cn.hutool.core.util.StrUtil;
import org.nmgns.bps.dktj.dao.EmployeeCustomerDao;
import org.nmgns.bps.dktj.dao.TemplateDao;
import org.nmgns.bps.dktj.entity.*;
import org.nmgns.bps.system.entity.User;
import org.nmgns.bps.system.service.ApiService;
import org.nmgns.bps.system.service.UserService;
import org.nmgns.bps.system.utils.DataScopeUtils;
import org.nmgns.bps.system.utils.DefaultConfig;
import org.nmgns.bps.system.utils.PageData;
import org.nmgns.bps.system.utils.UserUtils;
import org.nmgns.bps.system.utils.base.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TemplateSerivce {

    @Autowired
    private TemplateDao templateDao;
    @Autowired
    private EmployeeCustomerDao employeeCustomerDao;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private ApiService apiService;

    /**
     * 获取可用的模板列表
     */
    public List<Template> findAvailableTemplateList(){
        return templateDao.findAvailableTemplateList();
    }

    /**
     * 获取指定模板下的岗位列表及分成比例
     */
    public List<TemplateDetail> findTemplateDetailListByTemplateId(TemplateDetail templateDetail) throws Exception{
        if (templateDetail == null || templateDetail.getTemplateId() == null) throw new Exception("通过模板id获取岗位列表时，模板id为空");
        return templateDao.findTemplateDetailListByTemplateId(templateDetail);
    }

    /**
     * 将账号与模板的对应信息入库
     */
    public void insertAccountTemplate(AccountTemplate accountTemplate){
        templateDao.insertAccountTemplate(accountTemplate);
    }

    /**
     * 将账号-模板-柜员-分成比例对应信息入库
     */
    public void insertAccountShareInfo(List<AccountShareInfo> accountShareInfoList){
        for (AccountShareInfo accountShareInfo : accountShareInfoList){
            templateDao.insertAccountShareInfo(accountShareInfo);
        }
    }

    /**
     * 获取可变更模板的账户信息
     */
    @Transactional(rollbackFor = Exception.class)
    public PageData<AccountTemplate> findAccountTemplateList(AccountTemplate at) throws Exception{
        if (at == null || at.getParentId() == null) throw new Exception("未设置分页条件");

        at.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/template/getaccounttemplatelist").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "o", ""));

        //设置pageNo
        at.setPage(new Page(at.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));

        PageData<AccountTemplate> pageData = new PageData<>();
        Long count = templateDao.findAccountTemplateListCount(at);
        List<AccountTemplate> list = templateDao.findAccountTemplateList(at);

        pageData.setTotal(count);
        pageData.setList(list);
        return pageData;
    }


    @Transactional(rollbackFor=Exception.class)
    public void alterAccountTemplate(AccountTemplate at) throws Exception{

        if (StrUtil.isBlank(at.getAccountNo())) throw new Exception("变更模板时缺少贷款账号");
        if (StrUtil.isBlank(at.getOrgCode())) throw new Exception("变更模板时缺少机构号");
        if (at.getTemplateId() == null) throw new Exception("变更模板时缺少新模板id");
        if (at.getId() == null) throw new Exception("变更模板时缺少原模板id");
        if (at.getAccountShareInfoList().size() == 0) throw new Exception("变更模板时缺少模板下岗位信息");
        if (StrUtil.isBlank(at.getTellerCode())) throw new Exception("变更模板时缺少新维护人");

        //获取原始模板信息
        AccountTemplate origAt = templateDao.findAccountCurrentTemplateByAccountNo(at);
        if (origAt == null) throw new Exception("获取原始的账号模板信息失败");
        if (!origAt.getId().equals(at.getId())) throw new Exception("要变更的信息已失效，请刷新后重新变更");

        //获取原始维护人信息
        EmployeeCustomer tmpEc = new EmployeeCustomer();
        tmpEc.setAccountNo(origAt.getAccountNo());
        tmpEc.setOrgCode(origAt.getOrgCode());
        EmployeeCustomer origEc = employeeCustomerDao.getAccountInfoByAccountNo(tmpEc);
        if (origEc == null) throw new Exception("获取原始维护人信息失败，账号:"+origAt.getAccountNo());

        //获取原始分成信息
        List<AccountShareInfo> origAccountShareInfoList = templateDao.getValidAccountShareInfoByAccountTemplateId(origAt.getId());
        if (origAccountShareInfoList.size() == 0) throw new Exception("获取账号生效的分成信息失败，账号: "+origAt.getAccountNo());
        for (AccountShareInfo asi : origAccountShareInfoList) {
            if (!asi.getValidFlag() || (asi.getAlterCheckStatus() != null && asi.getAlterCheckStatus().equals(AccountShareInfo.CHECK_STATUS_UNCHECKED)) || (asi.getCheckStatus() != null && asi.getCheckStatus().equals(AccountShareInfo.CHECK_STATUS_UNCHECKED))) {
                throw new Exception("变更模板时，该账号下存在未复核的记录");
            }
        }


        // 固定客户不允许跨机构绑定
        String currentOrganizationCode = employeeCustomerDao.getUserCurrentOrganizationCodeByUserCode(at.getTellerCode());
        if (!currentOrganizationCode.equals(at.getOrgCode()) && origEc.getStatus().equals(EmployeeCustomer.STATUS_FIXED_CUSTOMER)) throw new Exception("登记维护人出错，固定客户无法绑定到非本机构的员工");


        // 判断提交上来的信息是否准确和完整
        for (AccountShareInfo asi : at.getAccountShareInfoList()){
            TemplateDetail td = templateDao.findTemplateDetailInfoById(asi.getTemplateDetailId());
            String positionName = td.getPositionName();
            if (StrUtil.isBlank(asi.getTellerCode())) throw new Exception("未登记账户 “"+positionName+"” 岗位的责任人");
            // 8.2-判断柜员号是否存在
            User user = userService.getUserByCode(asi.getTellerCode());
            if (null == user) throw new Exception("柜员号 “"+asi.getTellerCode()+"” 不存在！");
            // 8.3-如果模板中有推荐人，则模板中的推荐人和上方的维护人应该是同一个人
            String positionType = td.getPositionType();
            if (positionType == null) throw new Exception("登记维护人时，无法获取到岗位类型，账号: "+ at.getAccountNo());
            if (positionType.equals(Position.POSITION_TYPE_TUI_JIAN_REN)){
                if (!asi.getTellerCode().equals(at.getTellerCode())) throw new Exception("客户信息中的推荐人和分成模板中的推荐人不一致！");
            }
        }

        //常规信息设置
        User currentUser = userUtils.getCurrentLoginedUser();
        Date startDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DATE,-1);
        Date beforeDate = calendar.getTime();

        //设置原账号模板对应信息失效
        AccountTemplate tmpAt = new AccountTemplate();
        tmpAt.setId(origAt.getId());
        tmpAt.setEndDate(beforeDate);
        tmpAt.setValidFlag(false);
        tmpAt.setUpdateTime(new Date());
        tmpAt.setUpdateBy(currentUser.getId());
        templateDao.updateAccountTemplateById(tmpAt);

        // 写入模板账户对照表（只有新账户写入，释放的账户无需更改模板信息）
        tmpAt = new AccountTemplate();
        tmpAt.setAccountNo(origAt.getAccountNo());
        tmpAt.setTemplateId(at.getTemplateId());
        tmpAt.setParentId(origAt.getId());
        tmpAt.setStartDate(startDate);
        tmpAt.setValidFlag(true);
        tmpAt.setCreateTime(new Date());
        tmpAt.setCreateBy(currentUser.getId());
        tmpAt.setOrgCode(origAt.getOrgCode());
        tmpAt.setCheckStatus(AccountTemplate.CHECK_STATUS_CHECKED);
        templateDao.insertAccountTemplate(tmpAt);
        Long accountTemplateId = tmpAt.getId();
        if (accountTemplateId == null) throw new Exception("账号信息写入账号模板对照表失败！");

        //设置原分成信息为失效状态
        for (AccountShareInfo asi : origAccountShareInfoList){
            if (!asi.getValidFlag() || (asi.getAlterCheckStatus() != null && asi.getAlterCheckStatus().equals(AccountShareInfo.CHECK_STATUS_UNCHECKED)) || (asi.getCheckStatus() != null && asi.getCheckStatus().equals(AccountShareInfo.CHECK_STATUS_UNCHECKED))) {
                throw new Exception("变更模板时，该账号下存在未复核的记录");
            }
            AccountShareInfo tmpAsi = new AccountShareInfo();
            tmpAsi.setId(asi.getId());
            tmpAsi.setEndDate(beforeDate);
            tmpAsi.setValidFlag(false);
            tmpAsi.setUpdateTime(new Date());
            tmpAsi.setUpdateBy(currentUser.getId());
            templateDao.updateAccountShareInfoById(tmpAsi);
        }

        // 写入分成比例信息表（
        for (AccountShareInfo asi : at.getAccountShareInfoList()){
            asi.setAccountTemplateId(accountTemplateId);
            asi.setStartDate(startDate);
            asi.setValidFlag(true);
            asi.setCheckStatus(AccountShareInfo.CHECK_STATUS_CHECKED);
            asi.setCreateTime(new Date());
            asi.setCreateBy(currentUser.getId());
            templateDao.insertAccountShareInfo(asi);
        }
    }

}
