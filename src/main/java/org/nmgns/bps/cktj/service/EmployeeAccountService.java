package org.nmgns.bps.cktj.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.nmgns.bps.cktj.dao.EmployeeAccountDao;
import org.nmgns.bps.cktj.entity.AutoBindRule;
import org.nmgns.bps.cktj.entity.BindLevel;
import org.nmgns.bps.cktj.entity.EmployeeAccount;
import org.nmgns.bps.cktj.entity.TellerPercentage;
import org.nmgns.bps.cktj.utils.DepositDefaultConfig;
import org.nmgns.bps.system.entity.Dictionary;
import org.nmgns.bps.system.entity.Organization;
import org.nmgns.bps.system.entity.User;
import org.nmgns.bps.system.entity.UserOrganization;
import org.nmgns.bps.system.service.ApiService;
import org.nmgns.bps.system.service.OrganizationService;
import org.nmgns.bps.system.service.UserService;
import org.nmgns.bps.system.utils.DataScopeUtils;
import org.nmgns.bps.system.utils.DefaultConfig;
import org.nmgns.bps.system.utils.PageData;
import org.nmgns.bps.system.utils.UserUtils;
import org.nmgns.bps.system.utils.base.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EmployeeAccountService {

    @Autowired
    private ApiService apiService;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private EmployeeAccountDao employeeAccountDao;
    @Autowired
    private OrganizationService organizationService;

    /**
     * 获取未登记揽储人的账户列表
     * @param employeeAccount 参数信息，必须包含分页pageNo
     */
    public PageData<EmployeeAccount> findUnregisterAccountPage(EmployeeAccount employeeAccount) {
        if (null == employeeAccount) {
            employeeAccount = new EmployeeAccount();
        }

        //设置权限信息
        employeeAccount.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/employeeaccount/getunregisteraccount").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "o", null));
        //设置pageNo
        employeeAccount.setPage(new Page(employeeAccount.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));


        Long employeeAccountListCount = employeeAccountDao.findUnregisterAccountCount(employeeAccount);
        List<EmployeeAccount> employeeAccountList = employeeAccountDao.findUnregisterAccount(employeeAccount);

        PageData<EmployeeAccount> employeeAccountPageData = new PageData<>();
        employeeAccountPageData.setTotal(employeeAccountListCount);
        employeeAccountPageData.setList(employeeAccountList);
        employeeAccountPageData.setPageNo(employeeAccount.getPageNo());
        employeeAccountPageData.setPageSize(DefaultConfig.DEFAULT_PAGE_SIZE);

        return employeeAccountPageData;
    }


    /**
     * 校验一个网点的账户是否可以绑定到一个指定的员工上
     * @param userId 目标员工id，账户要绑定至的员工id
     * @param dpOrgCode 账户所在的会计机构
     * @param taskPaymentFlag 计酬还是任务绑定
     */
    private Boolean checkAccountBindToUser(Long userId, String dpOrgCode, String taskPaymentFlag) throws RuntimeException{
        if (!taskPaymentFlag.equals("EVALUATION_TYPE_TASK") && !taskPaymentFlag.equals("EVALUATION_TYPE_PAYMENT")) throw new RuntimeException("校验账户是否允许绑定给员工时，只能为任务数或计酬数");
        if (null == userId) throw new RuntimeException("校验账户是否允许绑定给员工时，未提供用户id");

        // 获取账户所在的支行
        Organization subBranchOrg = organizationService.getSubBranchByOrgCode(dpOrgCode);
        if (null == subBranchOrg) throw new RuntimeException("获取支行失败");
        String dpSubBranchOrgCode = subBranchOrg.getCode();
        // 获取账户所在中心支行
        Organization branchOrg = organizationService.getBranchByOrgCode(dpOrgCode);
        if (null == branchOrg) throw new RuntimeException("获取中心支行失败");
        String dpBranchOrgCode = branchOrg.getCode();

        BindLevel bindLevel = employeeAccountDao.getBindLevelByOrgAndFlag(new BindLevel(dpOrgCode,taskPaymentFlag,null));
        if(null == bindLevel) throw new RuntimeException("获取机构的允许绑定层级失败");

        //确定绑定的员工是否在允许绑定的层级内
        UserOrganization uo = userService.getValidUserOrganizationByUserId(userId);
        if(uo == null || uo.getOrganizationId() == null || StrUtil.isBlank(uo.getOrganizationCode())) throw new RuntimeException("查找员工的在职机构失败");
        // 只允许绑定到本网点的员工
        if (bindLevel.getLevel().equals("DEPOSIT_ORG_BIND_RULE_INSIDE_TERMINAL")){
            if (!uo.getOrganizationCode().equals(dpOrgCode)) throw new RuntimeException("该网点账号只允许绑定给本网点内的员工");
        }
        // 只允许绑定到本支行内的员工
        if (bindLevel.getLevel().equals("DEPOSIT_ORG_BIND_RULE_INSIDE_SUBBRANCH")){
            String userSubBranchOrgCode = organizationService.getSubBranchByOrgCode(uo.getOrganizationCode()).getCode();
            if (!userSubBranchOrgCode.equals(dpSubBranchOrgCode)) throw new RuntimeException("该网点账号只允许绑定给本支行内的员工");
        }
        // 只允许绑定到本中心支行内的员工
        if (bindLevel.getLevel().equals("DEPOSIT_ORG_BIND_RULE_INSIDE_BRANCH")){
            String userBranchOrgCode = organizationService.getBranchByOrgCode(uo.getOrganizationCode()).getCode();
            if (!userBranchOrgCode.equals(dpBranchOrgCode)) throw new RuntimeException("该网点账号只允许绑定给本中心支行内的员工");
        }

        return true;
    }

    /**
     * 登记揽储人
     * @param employeeAccount 登记的揽储人信息
     */
    @Transactional
    public void registerEmployee(EmployeeAccount employeeAccount) throws RuntimeException{
        if (null == employeeAccount || employeeAccount.getUnboundAccountId() == null || employeeAccount.getTaskPaymentSameFlag() == null) {
            throw new RuntimeException("登记揽储人出错, 请求参数不正确！");
        }

        if (employeeAccount.getTellerTaskPercentageList() == null || employeeAccount.getTellerTaskPercentageList().isEmpty()) throw new RuntimeException("未提供分成比例");

        EmployeeAccount dbUnboundDepositAccount = employeeAccountDao.getUnboundAccountInfoById(employeeAccount.getUnboundAccountId());
        if (null == dbUnboundDepositAccount || StrUtil.isBlank(dbUnboundDepositAccount.getAccountNo())) {
            throw new RuntimeException("登记揽储人出错, 找不到账号：" + employeeAccount.getAccountNo() + " 对应的存款账户信息！");
        }
        if (null == dbUnboundDepositAccount.getAccountOpenDate()) {
            throw new RuntimeException("登记揽储人出错, 账户的开户日期不正确， 请联系管理员处理！");
        }

        // 获取账户所在的网点
        String acctDpOrgCode = dbUnboundDepositAccount.getOrgCode();

        // 判断任务分成的员工是否合法
        String mainTellerCode = null;
        for (TellerPercentage tp:employeeAccount.getTellerTaskPercentageList()){
            User user = userService.getUserByCode(tp.getTellerCode());
            if (null == user) {
                throw new RuntimeException("登记揽储人出错, 编号为 [ " + tp.getTellerCode() + " ] 的员工信息不存在，请输入正确的柜员号！");
            }
            //确定该网点的账户是否允许绑定给目标员工
            checkAccountBindToUser(user.getId(), acctDpOrgCode, "EVALUATION_TYPE_TASK");

            if (null == tp.getMainTeller()) throw new RuntimeException("登记揽储人出错，未确定是否主揽储人身份");
            if (tp.getMainTeller()) mainTellerCode = tp.getTellerCode();
        }
        if (null == mainTellerCode) throw new RuntimeException("登记揽储人时，未确定主揽储人");

        // 前端未提供计酬分成比例时，设置与任务分成比例一致
        if (!employeeAccount.getTaskPaymentSameFlag() && (employeeAccount.getTellerPaymentPercentageList() == null || employeeAccount.getTellerTaskPercentageList().isEmpty())) {
            throw new RuntimeException("计酬比例与任务分成比例不一致，且未提供计酬分成比例");
        }
        if (employeeAccount.getTaskPaymentSameFlag() ){
            employeeAccount.setTellerPaymentPercentageList(employeeAccount.getTellerTaskPercentageList());
        }

        // 判断计酬分成员工是否合法
//        mainTellerCode = null;  //此处注释代码，表示计酬的主维护人默认与任务的主维护人，就是用任务的主维护人
        if(employeeAccount.getTellerPaymentPercentageList() != null && !employeeAccount.getTellerPaymentPercentageList().isEmpty()){
            for (TellerPercentage tp:employeeAccount.getTellerPaymentPercentageList()){
                User user = userService.getUserByCode(tp.getTellerCode());
                if (null == user) {
                    throw new RuntimeException("登记揽储人出错, 编号为 [ " + tp.getTellerCode() + " ] 的员工信息不存在，请输入正确的柜员号！");
                }

                //确定该网点的账户是否允许绑定给目标员工
                checkAccountBindToUser(user.getId(), acctDpOrgCode, "EVALUATION_TYPE_PAYMENT");
//                if (null == tp.getMainTeller()) throw new RuntimeException("登记揽储人出错，未确定是否主揽储人身份");
//                if (tp.getMainTeller()) mainTellerCode = tp.getTellerCode();
            }
//            if (null == mainTellerCode) throw new RuntimeException("登记揽储人时，未确定主揽储人");
        }

        //任务分成-在柜员揽储账户表中插入新登记的账户信息
        EmployeeAccount ea = new EmployeeAccount();
        ea.setMainTellerCode(mainTellerCode);
        ea.setOrgCode(dbUnboundDepositAccount.getOrgCode());
        ea.setAccountNo(dbUnboundDepositAccount.getAccountNo());
        ea.setChildAccountNo(dbUnboundDepositAccount.getChildAccountNo());
        ea.setSubjectNo(dbUnboundDepositAccount.getSubjectNo());
        ea.setCardNo(dbUnboundDepositAccount.getCardNo());
        ea.setAccountType(dbUnboundDepositAccount.getAccountType());
        ea.setAccountOpenDate(dbUnboundDepositAccount.getAccountOpenDate());
        ea.setCustomerNo(dbUnboundDepositAccount.getCustomerNo());
        ea.setCustomerName(dbUnboundDepositAccount.getCustomerName());
        ea.setCustomerType(dbUnboundDepositAccount.getCustomerType());
        ea.setIdentityNo(dbUnboundDepositAccount.getIdentityNo());
        ea.setIdentityType(dbUnboundDepositAccount.getIdentityType());
        ea.setRemarks(employeeAccount.getRemarks());
        ea.setStartDate(dbUnboundDepositAccount.getStartDate());
        ea.setValidFlag(false);
        ea.setRegisterType(DepositDefaultConfig.REGISTER_TYPE_NEW);
        ea.setRegisterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_UNCHECKED);
        ea.setOpTellerCode(userUtils.getCurrentLoginedUser().getCode());
        ea.setCreateTime(new Date());
        ea.setCreateBy(userUtils.getCurrentLoginedUser().getId());
        employeeAccountDao.insertTask(ea);
        if (ea.getId() == null) throw new RuntimeException("写入柜员揽储账户表后未返回id");


        // 写入任务分成比例
        List<TellerPercentage> tellerTaskPercentageList = new ArrayList<>();
        Double sumPercentage = (double)0;
        int mainTellerCount = 0;
        for (TellerPercentage tp:employeeAccount.getTellerTaskPercentageList()){
            TellerPercentage tmpTellerPercentage = new TellerPercentage();
            tmpTellerPercentage.setEmpAcctId(ea.getId());
            tmpTellerPercentage.setTellerCode(tp.getTellerCode());
            tmpTellerPercentage.setPercentage(tp.getPercentage());
            tmpTellerPercentage.setCreateTime(new Date());
            tmpTellerPercentage.setCreateBy(userUtils.getCurrentLoginedUser().getId());
            tellerTaskPercentageList.add(tmpTellerPercentage);

            sumPercentage += tp.getPercentage();
            if (tp.getMainTeller()) mainTellerCount +=1;
        }
        if (sumPercentage != 1) throw new RuntimeException("分成比例总和不为100%");
        if (mainTellerCount > 1) throw new RuntimeException("主维护人只能有1位");
        //将新记录插入到t_cktj_employee_account_task_detail表中
        for (TellerPercentage tp:tellerTaskPercentageList){
            employeeAccountDao.insertTaskTellerPercentage(tp);
        }


        //写入柜员揽储账户表（计酬）
        ea.setId(null);
        employeeAccountDao.insertPayment(ea);
        if(ea.getId() == null) throw new RuntimeException("写入柜员揽储账户表后未返回id");
        // 写入计酬分成比例
        List<TellerPercentage> tellerPaymentPercentageList = new ArrayList<>();
        sumPercentage = (double)0;

        for (TellerPercentage tp:employeeAccount.getTellerPaymentPercentageList()){
            TellerPercentage tmpTellerPercentage = new TellerPercentage();
            tmpTellerPercentage.setEmpAcctId(ea.getId());
            tmpTellerPercentage.setTellerCode(tp.getTellerCode());
            tmpTellerPercentage.setPercentage(tp.getPercentage());
            tmpTellerPercentage.setCreateBy(userUtils.getCurrentLoginedUser().getId());
            tmpTellerPercentage.setCreateTime(new Date());
            tellerPaymentPercentageList.add(tmpTellerPercentage);

            sumPercentage += tp.getPercentage();
        }
        if (sumPercentage != 1) throw new RuntimeException("分成比例总和不为100%");
        //将新记录插入到t_cktj_employee_account_payment表中
        for (TellerPercentage tp:tellerPaymentPercentageList){
            employeeAccountDao.insertPaymentTellerPercentage(tp);
        }


        // 写入账户自动绑定层级，而且只允许主账户设定自动绑定规则
        if (StrUtil.isNotBlank(dbUnboundDepositAccount.getAccountNo()) && (StrUtil.isBlank(dbUnboundDepositAccount.getChildAccountNo()) || dbUnboundDepositAccount.getChildAccountNo().equals("000000"))){
            AutoBindRule abr = new AutoBindRule();
            abr.setOrgCode(dbUnboundDepositAccount.getOrgCode());
            abr.setAccountNo(dbUnboundDepositAccount.getAccountNo());
            abr.setChildAccountNo(dbUnboundDepositAccount.getChildAccountNo());
            abr.setCustomerNo(dbUnboundDepositAccount.getCustomerNo());
            if (StrUtil.isNotBlank(employeeAccount.getAutoBindRule())) abr.setLevel(employeeAccount.getAutoBindRule());
            else abr.setLevel(DepositDefaultConfig.DEPOSIT_ACCOUNT_AUTO_BIND_LEVEL_ACCOUNT);  // 默认使用子账户级的自动绑定
            abr.setCreateBy(userUtils.getCurrentLoginedUser().getId());
            abr.setCreateTime(new Date());
            employeeAccountDao.insertAutoBindRule(abr);
        }


        //删除t_cktj_unbound_account表中的未绑定记录
        employeeAccountDao.deleteUnboundAccountById(employeeAccount.getUnboundAccountId());

    }

    /**
     * 获取已登记揽储人但未复核的账户列表
     */
    public PageData<EmployeeAccount> findRegisterUncheckedAccountPage(EmployeeAccount employeeAccount) {
        if (null == employeeAccount) {
            employeeAccount = new EmployeeAccount();
        }

        //设置权限信息
        employeeAccount.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/employeeaccount/getunregisteraccount").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "o", null));
        //设置pageNo
        employeeAccount.setPage(new Page(employeeAccount.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));

        PageData<EmployeeAccount> employeeAccountPageData = new PageData<>();
        Long count = employeeAccountDao.findRegisterUncheckedAccountCount(employeeAccount);
        List<EmployeeAccount> employeeAccountList = employeeAccountDao.findRegisterUncheckedAccount(employeeAccount);


        // 设置任务分成人列表、计酬分成人
        for (int i=0;i<employeeAccountList.size();i++){
            EmployeeAccount ea = employeeAccountList.get(i);
            ea.setRegisterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_UNCHECKED);   //查找未复核的

            List<TellerPercentage> tellerTaskPercentageList = employeeAccountDao.getTellerTaskPercentageListByEmployeeAccountId(ea);
            employeeAccountList.get(i).setTellerTaskPercentageList(tellerTaskPercentageList);

            List<TellerPercentage> tellerPaymentPercentageList = employeeAccountDao.getTellerPaymentPercentageListByEmployeeAccountAndChildAccountNo(ea);
            employeeAccountList.get(i).setTellerPaymentPercentageList(tellerPaymentPercentageList);
        }

        employeeAccountPageData.setTotal(count);
        employeeAccountPageData.setList(employeeAccountList);
        return employeeAccountPageData;
    }

    /**
     * 复核登记揽储人申请
     * @param employeeAccount 包含账号、子账号
     */
    @Transactional
    public void checkRegisterEmployee(EmployeeAccount employeeAccount) {
        if (null == employeeAccount || StrUtil.isBlank(employeeAccount.getAccountNo()) ) throw new RuntimeException("未提供参数");

        employeeAccount.setRegisterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_UNCHECKED);

        // 复核任务分成信息
        EmployeeAccount dbTaskEmployeeAccount = employeeAccountDao.getEmployeeAccountByAccountNoAndChildAccountNoFromTask(employeeAccount);
        if (null == dbTaskEmployeeAccount) throw new RuntimeException("不存在未复核的记录");

        //修改状态为已复核
        EmployeeAccount tmpEmpAcct = new EmployeeAccount();
        tmpEmpAcct.setId(dbTaskEmployeeAccount.getId());
        tmpEmpAcct.setValidFlag(true);
        tmpEmpAcct.setRegisterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_CHECKED);
        tmpEmpAcct.setRegisterCheckTellerCode(userUtils.getCurrentLoginedUser().getCode());
        tmpEmpAcct.setRegisterCheckTime(new Date());
        tmpEmpAcct.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
        tmpEmpAcct.setUpdateTime(new Date());
        employeeAccountDao.updateTaskById(tmpEmpAcct);


        // 复核计酬分成信息
        EmployeeAccount dbPaymentEmployeeAccount = employeeAccountDao.getEmployeeAccountByAccountNoAndChildAccountNoFromPayment(employeeAccount);
        if (null == dbPaymentEmployeeAccount) throw new RuntimeException("不存在未复核的记录");

        //修改状态为已复核
        tmpEmpAcct = new EmployeeAccount();
        tmpEmpAcct.setId(dbPaymentEmployeeAccount.getId());
        tmpEmpAcct.setValidFlag(true);
        tmpEmpAcct.setRegisterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_CHECKED);
        tmpEmpAcct.setRegisterCheckTellerCode(userUtils.getCurrentLoginedUser().getCode());
        tmpEmpAcct.setRegisterCheckTime(new Date());
        tmpEmpAcct.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
        tmpEmpAcct.setUpdateTime(new Date());
        employeeAccountDao.updatePaymentById(tmpEmpAcct);
    }

    /**
     * 撤销登记揽储人申请
     * @param employeeAccount 包含账号、子账号
     */
    @Transactional
    public void undoRegisterEmployee(EmployeeAccount employeeAccount) throws RuntimeException{
        if (null == employeeAccount || StrUtil.isBlank(employeeAccount.getAccountNo()) ) throw new RuntimeException("未提供参数");

        employeeAccount.setRegisterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_UNCHECKED);

        // 复核任务分成信息
        EmployeeAccount dbTaskEmployeeAccount = employeeAccountDao.getEmployeeAccountByAccountNoAndChildAccountNoFromTask(employeeAccount);
        if (null == dbTaskEmployeeAccount) throw new RuntimeException("不存在未复核的记录");

        //删除t_cktj_employee_account表中记录
        employeeAccountDao.deleteTask(dbTaskEmployeeAccount.getId());

        // 删除任务分成比例
        employeeAccountDao.deleteTaskTellerPercentage(dbTaskEmployeeAccount.getId());

        EmployeeAccount dbPaymentEmployeeAccount = employeeAccountDao.getEmployeeAccountByAccountNoAndChildAccountNoFromPayment(employeeAccount);
        if (null == dbPaymentEmployeeAccount) throw new RuntimeException("不存在未复核的记录");

        //删除t_cktj_employee_account表中记录
        employeeAccountDao.deletePayment(dbPaymentEmployeeAccount.getId());

        // 删除计酬分成比例
        employeeAccountDao.deletePaymentTellerPercentage(dbTaskEmployeeAccount.getId());

        //恢复t_cktj_unbound_account表中的记录
        employeeAccountDao.insertUnboundEmployeeAccount(dbTaskEmployeeAccount);

    }

    /**
     * 获取任务数可变更揽储人的账户信息列表
     * @param employeeAccount 包含分页信息、搜索信息
     */
    public PageData<EmployeeAccount> findTaskModifiableEmployeeAcountPage(EmployeeAccount employeeAccount) {
        if (null == employeeAccount || employeeAccount.getPage() == null) throw new RuntimeException("未提供参数");

        employeeAccount.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/employeeaccount/gettaskmodifiableaccount").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "o", ""));

        //设置pageNo
        employeeAccount.setPage(new Page(employeeAccount.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));

        PageData<EmployeeAccount> employeeAccountPageData = new PageData<>();
        Long count = employeeAccountDao.findTaskModifiableAccountCount(employeeAccount);
        List<EmployeeAccount> employeeAccountList = employeeAccountDao.findTaskModifiableAccount(employeeAccount);

        for (int i=0;i<employeeAccountList.size();i++){
            EmployeeAccount ea = employeeAccountList.get(i);
            ea.setRegisterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_CHECKED);   //查找登记已复核的
            ea.setValidFlag(true);

            List<TellerPercentage> tellerTaskPercentageList = employeeAccountDao.getTellerTaskPercentageListByEmployeeAccountId(ea);
            employeeAccountList.get(i).setTellerTaskPercentageList(tellerTaskPercentageList);
        }

        employeeAccountPageData.setTotal(count);
        employeeAccountPageData.setList(employeeAccountList);

        return employeeAccountPageData;
    }

    /**
     * 获取计酬数可变更揽储人的账户信息列表
     * @param employeeAccount 包含分页信息、搜索参数信息
     */
    public PageData<EmployeeAccount> findPaymentModifiableTellerPercentagePage(EmployeeAccount employeeAccount) {
        if (null == employeeAccount || employeeAccount.getPage() == null) throw new RuntimeException("未提供参数");

        employeeAccount.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/employeeaccount/gettaskmodifiableaccount").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "o", ""));

        //设置pageNo
        employeeAccount.setPage(new Page(employeeAccount.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));

        PageData<EmployeeAccount> employeeAccountPageData = new PageData<>();
        Long count = employeeAccountDao.findPaymentModifiableAccountCount(employeeAccount);
        List<EmployeeAccount> employeeAccountList = employeeAccountDao.findPaymentModifiableAccount(employeeAccount);

        for (int i=0;i<employeeAccountList.size();i++){
            EmployeeAccount ea = employeeAccountList.get(i);
            ea.setRegisterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_CHECKED);   //查找登记已复核的
            ea.setValidFlag(true);

            List<TellerPercentage> tellerTaskPercentageList = employeeAccountDao.getTellerPaymentPercentageListByEmployeeAccountId(ea);
            employeeAccountList.get(i).setTellerPaymentPercentageList(tellerTaskPercentageList);
        }

        employeeAccountPageData.setTotal(count);
        employeeAccountPageData.setList(employeeAccountList);

        return employeeAccountPageData;
    }

    /**
     * 任务数变更揽储人
     * @param employeeAccount 新揽储人信息，参数为旧记录id，新的任务分成比例
     */
    @Transactional
    public void modifyTaskEmployee(EmployeeAccount employeeAccount) throws RuntimeException {
        if (null == employeeAccount || employeeAccount.getTellerTaskPercentageList() == null || employeeAccount.getTellerTaskPercentageList().isEmpty() || null == employeeAccount.getId()) {
            throw new RuntimeException("变更揽储人出错, 请求参数不正确！");
        }

        EmployeeAccount dbTaskEmployeeAccount = employeeAccountDao.getEmployeeTaskById(employeeAccount.getId());
        if (null == dbTaskEmployeeAccount) throw new RuntimeException("变更揽储人出错, 找不到对应的存款账户信息！");

        // 判断员工编号是否正确、比例之和是否为100%
        Double sumPercentage = (double)0;
        String mainTellerCode = null;
        for (TellerPercentage tp:employeeAccount.getTellerTaskPercentageList()) {
            User user = userService.getUserByCode(tp.getTellerCode());
            if (null == user) {
                throw new RuntimeException("登记揽储人出错, 编号为 [ " + tp.getTellerCode() + " ] 的员工信息不存在，请输入正确的柜员号！");
            }

            //确定该网点的账户是否允许绑定给目标员工
            checkAccountBindToUser(user.getId(), dbTaskEmployeeAccount.getOrgCode(), "EVALUATION_TYPE_TASK");

            if (null == tp.getMainTeller()) throw new RuntimeException("登记揽储人出错，未确定是否主揽储人身份");
            if (tp.getMainTeller()) mainTellerCode = tp.getTellerCode();
            sumPercentage += tp.getPercentage();
        }
        if(sumPercentage != 1) throw new RuntimeException("比例求和不为100%");
        if (StrUtil.isBlank(mainTellerCode)) throw new RuntimeException("未确定主维护人");

        // 获取原始的任务分成比例
        employeeAccount.setValidFlag(true);
        List<TellerPercentage> dbTaskTellerPercentageList = employeeAccountDao.getTellerTaskPercentageListByEmployeeAccountId(dbTaskEmployeeAccount);

        if (null == dbTaskTellerPercentageList || dbTaskTellerPercentageList.isEmpty() ) {
            throw new RuntimeException("变更揽储人出错, 找不到对应的存款账户信息！");
        }

        // 判断当日是否已经发生过变更（当前不允许当日再次变更，后期优化）
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dbDate = sdf.format(dbTaskEmployeeAccount.getStartDate());
        String currentDate = sdf.format(new Date());
        if (dbDate.equals(currentDate)) throw new RuntimeException("变更为揽储人出错，当日已变更，不允许再次变更");

        //将原揽储人存款账户信息设置为不可变更揽储人
        Date yesterday = DateUtil.yesterday();
        EmployeeAccount tmpEmpAcct = new EmployeeAccount();
        tmpEmpAcct.setId(dbTaskEmployeeAccount.getId());
        tmpEmpAcct.setEndDate(yesterday);
        tmpEmpAcct.setValidFlag(false);
        tmpEmpAcct.setUpdateTime(new Date());
        tmpEmpAcct.setUpdateBy(userUtils.getCurrentLoginedUser().getId());

        employeeAccountDao.updateTaskById(tmpEmpAcct);

        //在柜员揽储账户表中插入新变更揽储人信息
        tmpEmpAcct = new EmployeeAccount();
        tmpEmpAcct.setMainTellerCode(mainTellerCode);
        tmpEmpAcct.setAccountNo(dbTaskEmployeeAccount.getAccountNo());
        tmpEmpAcct.setOrgCode(dbTaskEmployeeAccount.getOrgCode());
        tmpEmpAcct.setChildAccountNo(dbTaskEmployeeAccount.getChildAccountNo());
        tmpEmpAcct.setSubjectNo(dbTaskEmployeeAccount.getSubjectNo());
        tmpEmpAcct.setCardNo(dbTaskEmployeeAccount.getCardNo());
        tmpEmpAcct.setAccountType(dbTaskEmployeeAccount.getAccountType());
        tmpEmpAcct.setAccountOpenDate(dbTaskEmployeeAccount.getAccountOpenDate());
        tmpEmpAcct.setCustomerNo(dbTaskEmployeeAccount.getCustomerNo());
        tmpEmpAcct.setCustomerName(dbTaskEmployeeAccount.getCustomerName());
        tmpEmpAcct.setCustomerType(dbTaskEmployeeAccount.getCustomerType());
        tmpEmpAcct.setIdentityType(dbTaskEmployeeAccount.getIdentityType());
        tmpEmpAcct.setIdentityNo(dbTaskEmployeeAccount.getIdentityNo());
        tmpEmpAcct.setStartDate(new Date());
        tmpEmpAcct.setValidFlag(false);
        tmpEmpAcct.setRegisterType(DepositDefaultConfig.REGISTER_TYPE_REBOUND);      //登记方式：1-变更揽储人登记
        tmpEmpAcct.setAlterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_UNCHECKED);
        tmpEmpAcct.setOpTellerCode(userUtils.getCurrentLoginedUser().getCode());
        tmpEmpAcct.setParentId(dbTaskEmployeeAccount.getId());
        tmpEmpAcct.setRemarks(employeeAccount.getRemarks());
        tmpEmpAcct.setCreateTime(new Date());
        tmpEmpAcct.setCreateBy(userUtils.getCurrentLoginedUser().getId());
        employeeAccountDao.insertTask(tmpEmpAcct);

        for (TellerPercentage tp:employeeAccount.getTellerTaskPercentageList()) {
            TellerPercentage tmpTp = new TellerPercentage();
            tmpTp.setEmpAcctId(tmpEmpAcct.getId());
            tmpTp.setTellerCode(tp.getTellerCode());
            tmpTp.setPercentage(tp.getPercentage());
            tmpTp.setCreateTime(new Date());
            tmpTp.setCreateBy(userUtils.getCurrentLoginedUser().getId());

            employeeAccountDao.insertTaskTellerPercentage(tmpTp);   //写入新的分成比例
        }
    }

    /**
     * 计酬数变更揽储人
     * @param employeeAccount 新揽储人信息，参数为旧记录id，新的计酬分成比例
     */
    @Transactional
    public void modifyPaymentEmployee(EmployeeAccount employeeAccount) throws RuntimeException {
        if (null == employeeAccount || employeeAccount.getTellerPaymentPercentageList() == null || employeeAccount.getTellerPaymentPercentageList().isEmpty() || null == employeeAccount.getId()) {
            throw new RuntimeException("变更揽储人出错, 请求参数不正确！");
        }

        EmployeeAccount dbPaymentEmployeeAccount = employeeAccountDao.getEmployeePaymentById(employeeAccount.getId());
        if (null == dbPaymentEmployeeAccount) throw new RuntimeException("变更揽储人出错, 找不到对应的存款账户信息！");

        // 判断员工编号是否正确、比例之和是否为100%
        Double sumPercentage = (double)0;
        String mainTellerCode = null;
        for (TellerPercentage tp:employeeAccount.getTellerPaymentPercentageList()) {
            User user = userService.getUserByCode(tp.getTellerCode());
            if (null == user) {
                throw new RuntimeException("登记揽储人出错, 编号为 [ " + tp.getTellerCode() + " ] 的员工信息不存在，请输入正确的柜员号！");
            }

            //确定该网点的账户是否允许绑定给目标员工
            checkAccountBindToUser(user.getId(), dbPaymentEmployeeAccount.getOrgCode(), "EVALUATION_TYPE_PAYMENT");

//            if (null == tp.getMainTeller()) throw new RuntimeException("登记揽储人出错，未确定是否主揽储人身份");
//            if (tp.getMainTeller()) mainTellerCode = tp.getTellerCode();
            sumPercentage += tp.getPercentage();
        }
        if(sumPercentage != 1) throw new RuntimeException("比例求和不为100%");
//        if (StrUtil.isBlank(mainTellerCode)) throw new RuntimeException("未确定主维护人");

        // 获取原始的任务分成比例
        employeeAccount.setValidFlag(true);
        List<TellerPercentage> dbPaymentTellerPercentageList = employeeAccountDao.getTellerPaymentPercentageListByEmployeeAccountId(dbPaymentEmployeeAccount);

        if (null == dbPaymentTellerPercentageList || dbPaymentTellerPercentageList.isEmpty() ) {
            throw new RuntimeException("变更揽储人出错, 找不到对应的存款账户信息！");
        }

        // 判断当日是否已经发生过变更（当前不允许当日再次变更，后期优化）
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dbDate = sdf.format(dbPaymentEmployeeAccount.getStartDate());
        String currentDate = sdf.format(new Date());
        if (dbDate.equals(currentDate)) throw new RuntimeException("变更为揽储人出错，当日已变更，不允许再次变更");

        //将原揽储人存款账户信息设置为不可变更揽储人
        Date yesterday = DateUtil.yesterday();
        EmployeeAccount tmpEmpAcct = new EmployeeAccount();
        tmpEmpAcct.setId(dbPaymentEmployeeAccount.getId());
        tmpEmpAcct.setEndDate(yesterday);
        tmpEmpAcct.setValidFlag(false);
        tmpEmpAcct.setUpdateTime(new Date());
        tmpEmpAcct.setUpdateBy(userUtils.getCurrentLoginedUser().getId());

        employeeAccountDao.updatePaymentById(tmpEmpAcct);

        //在柜员揽储账户表中插入新变更揽储人信息
        tmpEmpAcct = new EmployeeAccount();
        tmpEmpAcct.setMainTellerCode(mainTellerCode);
        tmpEmpAcct.setAccountNo(dbPaymentEmployeeAccount.getAccountNo());
        tmpEmpAcct.setOrgCode(dbPaymentEmployeeAccount.getOrgCode());
        tmpEmpAcct.setChildAccountNo(dbPaymentEmployeeAccount.getChildAccountNo());
        tmpEmpAcct.setSubjectNo(dbPaymentEmployeeAccount.getSubjectNo());
        tmpEmpAcct.setCardNo(dbPaymentEmployeeAccount.getCardNo());
        tmpEmpAcct.setAccountType(dbPaymentEmployeeAccount.getAccountType());
        tmpEmpAcct.setAccountOpenDate(dbPaymentEmployeeAccount.getAccountOpenDate());
        tmpEmpAcct.setCustomerNo(dbPaymentEmployeeAccount.getCustomerNo());
        tmpEmpAcct.setCustomerName(dbPaymentEmployeeAccount.getCustomerName());
        tmpEmpAcct.setCustomerType(dbPaymentEmployeeAccount.getCustomerType());
        tmpEmpAcct.setIdentityType(dbPaymentEmployeeAccount.getIdentityType());
        tmpEmpAcct.setIdentityNo(dbPaymentEmployeeAccount.getIdentityNo());
        tmpEmpAcct.setStartDate(new Date());
        tmpEmpAcct.setValidFlag(false);
        tmpEmpAcct.setRegisterType(DepositDefaultConfig.REGISTER_TYPE_REBOUND);      //登记方式：1-变更揽储人登记
        tmpEmpAcct.setAlterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_UNCHECKED);
        tmpEmpAcct.setOpTellerCode(userUtils.getCurrentLoginedUser().getCode());
        tmpEmpAcct.setParentId(dbPaymentEmployeeAccount.getId());
        tmpEmpAcct.setRemarks(employeeAccount.getRemarks());
        tmpEmpAcct.setCreateTime(new Date());
        tmpEmpAcct.setCreateBy(userUtils.getCurrentLoginedUser().getId());
        employeeAccountDao.insertPayment(tmpEmpAcct);

        for (TellerPercentage tp:employeeAccount.getTellerPaymentPercentageList()) {
            TellerPercentage tmpTp = new TellerPercentage();
            tmpTp.setEmpAcctId(tmpEmpAcct.getId());
            tmpTp.setTellerCode(tp.getTellerCode());
            tmpTp.setPercentage(tp.getPercentage());
            tmpTp.setCreateTime(new Date());
            tmpTp.setCreateBy(userUtils.getCurrentLoginedUser().getId());

            employeeAccountDao.insertPaymentTellerPercentage(tmpTp);   //写入新的分成比例
        }
    }


    /**
     * 获取已变更揽储人但未复核的账户信息列表-任务数
     */
    public PageData<EmployeeAccount> findModifiedUncheckedAccountTaskPage(EmployeeAccount employeeAccount) {
        if (null == employeeAccount || employeeAccount.getPage() == null) throw new RuntimeException("未提供参数");

        employeeAccount.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/employeeaccount/getmodifieduncheckedaccounttask").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "o", ""));

        //设置pageNo
        employeeAccount.setPage(new Page(employeeAccount.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));

        PageData<EmployeeAccount> employeeAccountPageData = new PageData<>();
        Long count = employeeAccountDao.findModifiedUncheckedAccountTaskCount(employeeAccount);
        List<EmployeeAccount> employeeAccountList = employeeAccountDao.findModifiedUncheckedAccountTask(employeeAccount);

        for (int i=0;i<employeeAccountList.size();i++){
            EmployeeAccount ea = employeeAccountList.get(i);

            List<TellerPercentage> tellerTaskPercentageList = employeeAccountDao.getTellerTaskPercentageListByEmployeeAccountId(ea);
            employeeAccountList.get(i).setTellerTaskPercentageList(tellerTaskPercentageList);

            EmployeeAccount parentEmpAcct = new EmployeeAccount();
            parentEmpAcct.setId(ea.getParentId());
            List<TellerPercentage> oldTellerTaskPercentageList = employeeAccountDao.getTellerTaskPercentageListByEmployeeAccountId(parentEmpAcct);
            employeeAccountList.get(i).setOldTellerTaskPercentageList(oldTellerTaskPercentageList);
        }

        employeeAccountPageData.setTotal(count);
        employeeAccountPageData.setList(employeeAccountList);

        return employeeAccountPageData;
    }

    /**
     * 获取已变更揽储人但未复核的账户信息列表-计酬数
     */
    public PageData<EmployeeAccount> findModifiedUncheckedAccountPaymentPage(EmployeeAccount employeeAccount) {
        if (null == employeeAccount || employeeAccount.getPage() == null) throw new RuntimeException("未提供参数");

        employeeAccount.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/employeeaccount/getmodifieduncheckedaccountpayment").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "o", ""));

        //设置pageNo
        employeeAccount.setPage(new Page(employeeAccount.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));

        PageData<EmployeeAccount> employeeAccountPageData = new PageData<>();
        Long count = employeeAccountDao.findModifiedUncheckedAccountPaymentCount(employeeAccount);
        List<EmployeeAccount> employeeAccountList = employeeAccountDao.findModifiedUncheckedAccountPayment(employeeAccount);

        for (int i=0;i<employeeAccountList.size();i++){
            EmployeeAccount ea = employeeAccountList.get(i);

            List<TellerPercentage> tellerPaymentPercentageList = employeeAccountDao.getTellerPaymentPercentageListByEmployeeAccountId(ea);
            employeeAccountList.get(i).setTellerPaymentPercentageList(tellerPaymentPercentageList);

            EmployeeAccount parentEmpAcct = new EmployeeAccount();
            parentEmpAcct.setId(ea.getParentId());
            List<TellerPercentage> oldTellerPaymentPercentageList = employeeAccountDao.getTellerPaymentPercentageListByEmployeeAccountId(parentEmpAcct);
            employeeAccountList.get(i).setOldTellerPaymentPercentageList(oldTellerPaymentPercentageList);
        }

        employeeAccountPageData.setTotal(count);
        employeeAccountPageData.setList(employeeAccountList);

        return employeeAccountPageData;
    }



    /**
     * 复核变更揽储人申请-任务数
     * @param employeeAccount 包含id
     */
    @Transactional
    public void checkModifyEmployeeTask(EmployeeAccount employeeAccount) {
        if (null == employeeAccount || null == employeeAccount.getId()) throw new RuntimeException("未提供参数");

        EmployeeAccount dbEmployeeAccount = employeeAccountDao.getEmployeeTaskById(employeeAccount.getId());
        if (null == dbEmployeeAccount) throw new RuntimeException("不存在未复核的记录");

        //修改状态为已复核
        employeeAccount.setValidFlag(true);
        employeeAccount.setAlterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_CHECKED);
        employeeAccount.setAlterCheckTellerCode(userUtils.getCurrentLoginedUser().getCode());
        employeeAccount.setAlterCheckTime(new Date());
        employeeAccount.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
        employeeAccount.setUpdateTime(new Date());

        employeeAccountDao.updateTaskById(employeeAccount);
    }

    /**
     * 复核变更揽储人申请-计酬数
     * @param employeeAccount 包含id
     */
    @Transactional
    public void checkModifyEmployeePayment(EmployeeAccount employeeAccount) {
        if (null == employeeAccount || null == employeeAccount.getId()) throw new RuntimeException("未提供参数");

        EmployeeAccount dbEmployeeAccount = employeeAccountDao.getEmployeePaymentById(employeeAccount.getId());
        if (null == dbEmployeeAccount) throw new RuntimeException("不存在未复核的记录");

        employeeAccount.setValidFlag(true);
        employeeAccount.setAlterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_CHECKED);
        employeeAccount.setAlterCheckTellerCode(userUtils.getCurrentLoginedUser().getCode());
        employeeAccount.setAlterCheckTime(new Date());
        employeeAccount.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
        employeeAccount.setUpdateTime(new Date());

        employeeAccountDao.updatePaymentById(employeeAccount);
    }

    /**
     * 撤销变更揽储人申请-任务数
     * @param employeeAccount 包含id
     */
    @Transactional
    public void undoModifyEmployeeTask(EmployeeAccount employeeAccount) {
        if (null == employeeAccount || null == employeeAccount.getId()) throw new RuntimeException("未提供参数");

        EmployeeAccount dbEmployeeAccount = employeeAccountDao.getEmployeeTaskById(employeeAccount.getId());
        if (null == dbEmployeeAccount) throw new RuntimeException("不存在未复核的记录");

        //将原揽储人存款账户信息设置为可变更揽储人
        Long parentId = dbEmployeeAccount.getParentId();
        EmployeeAccount ea = new EmployeeAccount();
        ea.setId(parentId);
        ea.setValidFlag(true);
        ea.setEndDate(null);
        ea.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
        ea.setUpdateTime(new Date());
        employeeAccountDao.updateTaskById(ea);

        //删除新记录
        employeeAccountDao.deleteTask(dbEmployeeAccount.getId());

        // 删除分成比例
        employeeAccountDao.deleteTaskTellerPercentage(dbEmployeeAccount.getId());

    }

    /**
     * 撤销变更揽储人申请-计酬数
     * @param employeeAccount 包含id
     */
    @Transactional
    public void undoModifyEmployeePayment(EmployeeAccount employeeAccount) {
        if (null == employeeAccount || null == employeeAccount.getId()) throw new RuntimeException("未提供参数");

        EmployeeAccount dbEmployeeAccount = employeeAccountDao.getEmployeePaymentById(employeeAccount.getId());
        if (null == dbEmployeeAccount) throw new RuntimeException("不存在未复核的记录");

        //将原揽储人存款账户信息设置为可变更揽储人
        Long parentId = dbEmployeeAccount.getParentId();
        EmployeeAccount ea = new EmployeeAccount();
        ea.setId(parentId);
        ea.setValidFlag(true);
        ea.setEndDate(null);
        ea.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
        ea.setUpdateTime(new Date());
        employeeAccountDao.updatePaymentById(ea);

        //删除新记录
        employeeAccountDao.deletePayment(dbEmployeeAccount.getId());

        // 删除分成比例
        employeeAccountDao.deletePaymentTellerPercentage(dbEmployeeAccount.getId());

    }

    /**
     * 获取员工和存款账户的关联信息列表- 任务分成
     * @param employeeAccount 包含分页信息、搜索信息
     */
    public PageData<EmployeeAccount> findTellerPercentageTaskPage(EmployeeAccount employeeAccount) {
        if (null == employeeAccount || employeeAccount.getPage() == null) throw new RuntimeException("未提供参数");

        employeeAccount.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/employeeaccount/gettask").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "o", ""));

        //设置pageNo
        employeeAccount.setPage(new Page(employeeAccount.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));

        PageData<EmployeeAccount> employeeAccountPageData = new PageData<>();
        Long count = employeeAccountDao.findTaskModifiableAccountCount(employeeAccount);
        List<EmployeeAccount> employeeAccountList = employeeAccountDao.findTaskModifiableAccount(employeeAccount);

        for (int i=0;i<employeeAccountList.size();i++){
            EmployeeAccount ea = employeeAccountList.get(i);
            ea.setRegisterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_CHECKED);   //查找登记已复核的
            ea.setValidFlag(true);

            List<TellerPercentage> tellerTaskPercentageList = employeeAccountDao.getTellerTaskPercentageListByEmployeeAccountId(ea);
            employeeAccountList.get(i).setTellerTaskPercentageList(tellerTaskPercentageList);
        }

        employeeAccountPageData.setTotal(count);
        employeeAccountPageData.setList(employeeAccountList);

        return employeeAccountPageData;
    }

    /**
     * 获取员工和存款账户的关联信息列表- 计酬分成
     * @param employeeAccount 包含分页信息、搜索信息
     */
    public PageData<EmployeeAccount> findTellerPercentagePaymentPage(EmployeeAccount employeeAccount) {
        if (null == employeeAccount || employeeAccount.getPage() == null) throw new RuntimeException("未提供参数");

        employeeAccount.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/employeeaccount/getpayment").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "o", ""));

        //设置pageNo
        employeeAccount.setPage(new Page(employeeAccount.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));

        PageData<EmployeeAccount> employeeAccountPageData = new PageData<>();
        Long count = employeeAccountDao.findPaymentModifiableAccountCount(employeeAccount);
        List<EmployeeAccount> employeeAccountList = employeeAccountDao.findPaymentModifiableAccount(employeeAccount);

        for (int i=0;i<employeeAccountList.size();i++){
            EmployeeAccount ea = employeeAccountList.get(i);
            ea.setRegisterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_CHECKED);   //查找登记已复核的
            ea.setValidFlag(true);

            List<TellerPercentage> tellerTaskPercentageList = employeeAccountDao.getTellerPaymentPercentageListByEmployeeAccountId(ea);
            employeeAccountList.get(i).setTellerPaymentPercentageList(tellerTaskPercentageList);
        }

        employeeAccountPageData.setTotal(count);
        employeeAccountPageData.setList(employeeAccountList);

        return employeeAccountPageData;
    }

    public List<Dictionary> getDepositAccountAutoBindRule(){
        return employeeAccountDao.getDepositAccountAutoBindRule();
    }






}
