package org.nmgns.bps.dktj.service;

import cn.hutool.core.util.StrUtil;
import org.nmgns.bps.dktj.dao.InterestShareInfoDao;
import org.nmgns.bps.dktj.dao.TemplateDao;
import org.nmgns.bps.dktj.entity.*;
import org.nmgns.bps.dktj.utils.Span;
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
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
@Transactional
@RequestMapping("/dktj/employeeinterest")
public class InterestShareInfoSerivce {

    @Autowired
    private InterestShareInfoDao interestShareInfoDao;
    @Autowired
    private TemplateDao templateDao;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private ApiService apiService;

    /**
     * 获取待补录的贷款账户列表
     * @param ei
     */
    public PageData<EmployeeInterest> findUnboundReaddAccount(EmployeeInterest ei) throws Exception{
        if (ei == null || ei.getPage() == null) throw new Exception("未设置分页条件");

        ei.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/employeeinterest/readd").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "o", ""));

        //设置pageNo
        ei.setPage(new Page(ei.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));

        PageData<EmployeeInterest> employeeInterestPageData = new PageData<>();
        Long count = interestShareInfoDao.findUnboundReaddAccountCount(ei);
        List<EmployeeInterest> employeeInterestList = interestShareInfoDao.findUnboundReaddAccount(ei);


        employeeInterestPageData.setTotal(count);
        employeeInterestPageData.setList(employeeInterestList);
        return employeeInterestPageData;
    }

    /**
     * 补登记账户分成规则
     */
    @Transactional(rollbackFor=Exception.class)
    public void readdEmployeeInterest(EmployeeCustomer euc) throws Exception{
        if (StrUtil.isBlank(euc.getAccountNo())) throw new Exception("补登记维护人时缺少贷款账号");
        if (StrUtil.isBlank(euc.getOrgCode())) throw new Exception("补登记维护人时缺少机构号");
        if (StrUtil.isBlank(euc.getTellerCode())) throw new Exception("补登记维护人时缺少维护人信息");

        // 1-获取到该客户在该网点的未绑定的贷款账号详细信息
        EmployeeInterest tempEI = new EmployeeInterest();
        tempEI.setAccountNo(euc.getAccountNo());
        tempEI.setOrgCode(euc.getOrgCode());
        EmployeeInterest eiInfo = interestShareInfoDao.getReaddAccountInfoByAccountNo(tempEI);
        // 2-获取该客户在该网点的贷款账户信息，只有一条（或不存在）。
        if (eiInfo == null) throw new Exception("通过贷款账号获取补登记维护人信息失败");
        User currentUser = userUtils.getCurrentLoginedUser();

        // 3-判断贷款的维护人信息是否已经生成，即在t_dktj_employee_customer表中已经有了信息，如果没有，则需要先手工录入
        EmployeeCustomer tempEC = new EmployeeCustomer();
        tempEC.setOrgCode(euc.getOrgCode());
        tempEC.setAccountNo(euc.getAccountNo());
        int ecCount = interestShareInfoDao.getEmployeeAccountCount(tempEC);
        if (ecCount == 0) throw new Exception("t_dktj_employee_customer表中缺少该笔贷款的信息,贷款账号: "+euc.getAccountNo());
        int ecValidCount = interestShareInfoDao.getValidEmployeeAccountCount(tempEC);
        if (ecValidCount == 0) throw new Exception("该笔贷款在t_dktj_employee_customer中缺少生效的信息,贷款账号: "+euc.getAccountNo());
        if (ecValidCount > 1) throw new Exception("该笔贷款在t_dktj_employee_customer中生效的信息个数大于1,贷款账号: "+euc.getAccountNo());
        String currentTellerCode = interestShareInfoDao.getCurrentValidEmployeeCustomerTellerCode(tempEC);
        if (!currentTellerCode.equals(euc.getTellerCode())) throw new Exception("补登记的维护人与当前维护人不一致");


        // 4.1-判断所有岗位是否都已填写了行内员工
        if (euc.getAccountShareInfoList() == null || euc.getAccountShareInfoList().size() == 0) throw new Exception("补登记时未填写相关责任人");
        for (AccountShareInfo asi:euc.getAccountShareInfoList()){
            TemplateDetail td = templateDao.findTemplateDetailInfoById(asi.getTemplateDetailId());
            String positionName = td.getPositionName();
            if (StrUtil.isBlank(asi.getTellerCode())) throw new Exception("未登记账户 “"+positionName+"” 岗位的责任人");
            // 4.2-判断柜员号是否存在
            User user = userService.getUserByCode(asi.getTellerCode());
            if (null == user) throw new Exception("柜员号 “"+asi.getTellerCode()+"” 不存在！");
            // 4.3-如果模板中有推荐人，则模板中的推荐人和上方的维护人应该是同一个人
            String positionType = td.getPositionType();
            if (positionType == null) throw new Exception("登记维护人时，无法获取到岗位类型，账号: "+euc.getAccountNo());
            if (positionType.equals(Position.POSITION_TYPE_TUI_JIAN_REN)){
                if (!asi.getTellerCode().equals(euc.getTellerCode())) throw new Exception("客户信息中的推荐人和分成模板中的推荐人不一致！");
            }
        }

        // 5-写入模板账户对照表（只有新账户写入，释放的账户无需更改模板信息）
        AccountTemplate at = new AccountTemplate();
        at.setAccountNo(eiInfo.getAccountNo());
        at.setOrgCode(eiInfo.getOrgCode());
        AccountTemplate tempAT = templateDao.findAccountCurrentTemplateByAccountNo(at);
        if (tempAT != null) throw new Exception("该笔贷款已存在生效的分成模板信息,贷款账号: "+at.getAccountNo());
        at.setTemplateId(euc.getTemplateId());
        at.setStartDate(eiInfo.getAccountOpenDate());
        at.setValidFlag(true);
        at.setCreateTime(new Date());
        at.setCreateBy(currentUser.getId());
        at.setCheckStatus(AccountTemplate.CHECK_STATUS_CHECKED);
        templateDao.insertAccountTemplate(at);
        Long accountTemplateId = at.getId();
        if (accountTemplateId == null) throw new Exception("账号信息写入账号模板对照表失败！");

        // 6-写入分成比例信息表
        for (AccountShareInfo asi:euc.getAccountShareInfoList()){
            asi.setAccountTemplateId(accountTemplateId);
            asi.setStartDate(eiInfo.getAccountOpenDate());
            asi.setValidFlag(true);
            asi.setCheckStatus(AccountShareInfo.CHECK_STATUS_CHECKED);
            asi.setCreateTime(new Date());
            asi.setCreateBy(currentUser.getId());
            templateDao.insertAccountShareInfo(asi);
        }

        // 7-删除t_dktj_unbound_employee_interest表中相应的记录
        interestShareInfoDao.deleteReaddAccountById(eiInfo.getId());

    }

    /**
     * 获取员工贷款的时点利息
     * @param ei
     */
    public List<InterestShareInfo> findEmployeeInterestList(InterestShareInfo ei) throws Exception{
        if (ei == null || StrUtil.isBlank(ei.getQueryType())) throw new Exception("获取员工时点贷款失败：未提供loanType");
        if (ei.getOrganizationId() == null) throw new Exception("获取员工时点贷款利息失败：未提供网点信息");

        if (ei.getQueryType().equals(InterestShareInfo.QUERY_TYPE_HE_XIN)){
            ei.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/employeeinterest/empinterest").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "lno", "u"));
        }else {
            ei.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/employeeinterest/empinterest").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "lno,blo", "u"));
        }

        return interestShareInfoDao.findEmployeeLoanInterest(ei);
    }

    /**
     * 获取利息分成明细
     * @param isi
     */
    public PageData<InterestShareInfo> findPage(InterestShareInfo isi) {
        if (null == isi) {
            isi = new InterestShareInfo();
        }
        isi.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/employeeinterest/empinterest").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "o", "su"));

        //设置pageNo
        isi.setPage(new Page(isi.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));

        PageData<InterestShareInfo> pageData = new PageData<>();
        Long count = interestShareInfoDao.findCount(isi);
        List<InterestShareInfo> list = interestShareInfoDao.findList(isi);

        pageData.setTotal(count);
        pageData.setList(list);
        return pageData;
    }

    /**
     * 获取一个区间段内的员工利息汇总
     * @param ei
     */
    public List<InterestShareInfo> findEmployeeAvgInterestList(InterestShareInfo ei) throws Exception{
        if (ei == null || StrUtil.isBlank(ei.getQueryType())) throw new Exception("获取员工日均贷款失败：未提供loanType");
        if (ei.getOrganizationId() == null) throw new Exception("获取员工日均贷款失败：未提供网点信息");
        if (ei.getStartDate() == null || ei.getEndDate() == null) throw new Exception("获取员工日均贷款失败：未提供起止时间");
        Span span = new Span(ei.getStartDate(),ei.getEndDate());
        ei.setAvgDays(span.getDateSpace());

        if (ei.getQueryType().equals(InterestShareInfo.QUERY_TYPE_HE_XIN)){
            ei.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/employeeinterest/empavginterest").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "lno", "u"));
        }else {
            ei.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/employeeinterest/empavginterest").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "lno,blo", "u"));
        }

        return interestShareInfoDao.findEmployeeAvgLoanInterest(ei);
    }

    /**
     * 获取机构时点利息
     * @param ei
     */
    public List<InterestShareInfo> findOrganizationInterestList(InterestShareInfo ei) throws Exception{
        if (ei == null || StrUtil.isBlank(ei.getQueryType())) throw new Exception("获取机构时点贷款失败：未提供loanType");
        if (ei.getOrganizationId() == null) throw new Exception("获取机构时点贷款失败：未提供网点信息");

        ei.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/employeeinterest/orginterest").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "lno,blo", ""));

        return interestShareInfoDao.findOrgLoanInterest(ei);
    }

    /**
     * 获取机构在一个时间段内的汇总利息
     * @param ei
     */
    public List<InterestShareInfo> findOrganizationAvgInterestList(InterestShareInfo ei) throws Exception{
        if (ei == null || StrUtil.isBlank(ei.getQueryType())) throw new Exception("获取机构日均贷款失败：未提供loanType");
        if (ei.getOrganizationId() == null) throw new Exception("获取机构日均贷款失败：未提供网点信息");
        if (ei.getStartDate() == null || ei.getEndDate() == null) throw new Exception("获取机构日均贷款失败：未提供起止时间");
        Span span = new Span(ei.getStartDate(),ei.getEndDate());
        ei.setAvgDays(span.getDateSpace());

        ei.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/employeeinterest/orgavginterest").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "lno,blo", ""));

        return interestShareInfoDao.findOrgAvgLoanInterest(ei);
    }

    /**
     * 获取当前生效的账户岗位责任人列表
     */
    @Transactional(readOnly = true)
    public PageData<InterestShareInfo> getValidPositionTellerList(InterestShareInfo isi) throws Exception{
        if (isi == null || isi.getPage() == null) throw new Exception("未设置分页条件");

        isi.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/employeeinterest/positiontelleralterlist").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "lno", ""));

        //设置pageNo
        isi.setPage(new Page(isi.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));

        PageData<InterestShareInfo> pageData = new PageData<>();
        Long count = interestShareInfoDao.getValidPositionTellerListCount(isi);
        List<InterestShareInfo> list = interestShareInfoDao.getValidPositionTellerList(isi);

        pageData.setTotal(count);
        pageData.setList(list);
        return pageData;
    }

    /**
     * 变更岗位责任人
     */
    public void positionTellerAlter(List<InterestShareInfo> isiList) throws Exception{
        for(InterestShareInfo isi : isiList) {
            if (StrUtil.isBlank(isi.getAccountNo())) throw new Exception("变更岗位责任人时缺少贷款账号");
            if (StrUtil.isBlank(isi.getLnOrgCode())) throw new Exception("变更岗位责任人时缺少机构号");
            if (StrUtil.isBlank(isi.getTellerCode())) throw new Exception("变更岗位责任人时缺少新责任人信息");
            if (isi.getId() == null) throw new Exception("变更贷款岗位责任人时缺少id");

            InterestShareInfo dbIsi = interestShareInfoDao.getAccountShareInfoById(isi.getId());
            if (dbIsi == null) throw new Exception("无法通过id：" + "获取到完整的账户分成信息");
            if (dbIsi.getTellerCode().equals(isi.getTellerCode())) throw new Exception("责任人相同，无需变更");
            //判断该岗位是否允许变更
            if (!dbIsi.getPositionChangeable()) throw new Exception(dbIsi.getPositionName() + "不允许变更责任人");
            if (!dbIsi.getValidFlag()) throw new Exception("当前岗位责任人已过期，请刷新");
            if (dbIsi.getPositionType().equals(Position.POSITION_TYPE_TUI_JIAN_REN))
                throw new Exception("推荐人不允许在此功能处变更");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = sdf.parse(sdf.format(new Date()));
            if (!currentDate.after(dbIsi.getStartDate())) throw new Exception("起始日期大于或等于当前日期，无需变更");
            Date beforeDay = getBeforeDay(currentDate);

            //设置旧记录为false
            AccountShareInfo tmp = new AccountShareInfo();
            tmp.setId(dbIsi.getId());
            tmp.setValidFlag(false);
            tmp.setEndDate(beforeDay);
            tmp.setUpdateTime(new Date());
            tmp.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
            templateDao.updateAccountShareInfoById(tmp);

            //新增加新记录
            tmp = new AccountShareInfo();
            tmp.setAccountTemplateId(dbIsi.getAccountTemplate().getId());
            tmp.setTemplateDetailId(dbIsi.getTemplateDetailId());
            tmp.setTellerCode(isi.getTellerCode());
            tmp.setStartDate(currentDate);
            tmp.setParentId(dbIsi.getId());
            tmp.setValidFlag(false);
            tmp.setCheckStatus(AccountShareInfo.CHECK_STATUS_CHECKED);
            tmp.setAlterCheckStatus(AccountShareInfo.CHECK_STATUS_UNCHECKED);
            tmp.setCreateTime(new Date());
            tmp.setCreateBy(userUtils.getCurrentLoginedUser().getId());
            templateDao.insertAccountShareInfo(tmp);

        }
    }

    public Long getAlterUncheckedShareInfoCount(InterestShareInfo isi){
        return interestShareInfoDao.getAlterUncheckedShareInfoCount(isi);
    }

    /**
     * 获取变更未复核的岗位责任人列表
     */
    public PageData<InterestShareInfo> getAlterUncheckedShareInfo(InterestShareInfo isi) throws Exception{
        if (isi == null || isi.getPage() == null) throw new Exception("未设置分页条件");

        isi.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/employeeinterest/positiontellerchecklist").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "lno", ""));

        //设置pageNo
        isi.setPage(new Page(isi.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));

        PageData<InterestShareInfo> pageData = new PageData<>();
        Long count = interestShareInfoDao.getAlterUncheckedShareInfoCount(isi);
        List<InterestShareInfo> list = interestShareInfoDao.getAlterUncheckedShareInfo(isi);

        pageData.setTotal(count);
        pageData.setList(list);
        return pageData;
    }

    /**
     * 复核通过变更岗位责任人
     */
    public void positionTellerAlterCheck(List<InterestShareInfo> isiList) throws Exception{
        for(InterestShareInfo isi: isiList) {
            if (StrUtil.isBlank(isi.getAccountNo())) throw new Exception("复核变更岗位责任人时缺少贷款账号");
            if (StrUtil.isBlank(isi.getLnOrgCode())) throw new Exception("复核变更岗位责任人时缺少机构号");
            if (isi.getId() == null) throw new Exception("复核变更贷款岗位责任人时缺少id");

            InterestShareInfo dbIsi = interestShareInfoDao.getAccountShareInfoById(isi.getId());
            if (dbIsi == null) throw new Exception("无法通过id：" + "获取到完整的账户分成信息");
            //判断该岗位是否允许变更
            if (!dbIsi.getPositionChangeable()) throw new Exception(dbIsi.getPositionName() + "不允许变更责任人");
            if (dbIsi.getValidFlag()) throw new Exception("当前变更已复核，请刷新");
            if (dbIsi.getPositionType().equals(Position.POSITION_TYPE_TUI_JIAN_REN))
                throw new Exception("推荐人不允许在此功能处变更");

            //设置新记录为true
            AccountShareInfo tmp = new AccountShareInfo();
            tmp.setId(dbIsi.getId());
            tmp.setValidFlag(true);
            tmp.setAlterCheckStatus(AccountShareInfo.CHECK_STATUS_CHECKED);
            tmp.setAlterCheckTeller(userUtils.getCurrentLoginedUser().getCode());
            tmp.setUpdateTime(new Date());
            tmp.setUpdateBy(userUtils.getCurrentUserOrganization().getId());
            templateDao.updateAccountShareInfoById(tmp);
        }
    }

    /**
     * 复核拒绝变更岗位责任人
     */
    public void positionTellerAlterUncheck(List<InterestShareInfo> isiList) throws Exception{
        for (InterestShareInfo isi: isiList){
            if (StrUtil.isBlank(isi.getAccountNo())) throw new Exception("复核变更岗位责任人时缺少贷款账号");
            if (StrUtil.isBlank(isi.getLnOrgCode())) throw new Exception("复核变更岗位责任人时缺少机构号");
            if (isi.getId() == null) throw new Exception("复核变更贷款岗位责任人时缺少id");

            InterestShareInfo dbIsi = interestShareInfoDao.getAccountShareInfoById(isi.getId());
            if (dbIsi == null) throw new Exception("无法通过id："+"获取到完整的账户分成信息");
            //判断该岗位是否允许变更
            if (!dbIsi.getPositionChangeable()) throw new Exception(dbIsi.getPositionName()+"不允许变更责任人");
            if (dbIsi.getValidFlag()) throw new Exception("当前变更已复核，请刷新");
            if (dbIsi.getPositionType().equals(Position.POSITION_TYPE_TUI_JIAN_REN)) throw new Exception("推荐人不允许在此功能处变更");

            if (dbIsi.getParentId() == null || dbIsi.getAlterCheckStatus() == null || !dbIsi.getAlterCheckStatus().equals(AccountShareInfo.CHECK_STATUS_UNCHECKED)) throw new Exception("非变更未复核的记录");

            //设置旧记录为true
            AccountShareInfo tmp = new AccountShareInfo();
            tmp.setId(dbIsi.getParentId());
            tmp.setValidFlag(true);
            tmp.setEndDate(null);
            tmp.setUpdateTime(new Date());
            tmp.setUpdateBy(userUtils.getCurrentUserOrganization().getId());
            templateDao.updateAccountShareInfoById(tmp);

            //删除新记录
            templateDao.deleteAccountShareInfoById(dbIsi.getId());
        }
    }

    /**
     * 获取全部岗位类型
     */
    @Transactional(readOnly = true)
    public List<Position> getPositionList() throws Exception{
        return interestShareInfoDao.getPositionList();
    }

    private Date getBeforeDay(Date today){
        Date startDate = today;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date endDate = calendar.getTime();
        return endDate;
    }

}
