package org.nmgns.bps.cktj.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.nmgns.bps.cktj.dao.EmployeeAccountDao;
import org.nmgns.bps.cktj.entity.AutoBindRule;
import org.nmgns.bps.cktj.entity.EmployeeAccount;
import org.nmgns.bps.cktj.entity.TellerPercentage;
import org.nmgns.bps.cktj.utils.DepositDefaultConfig;
import org.nmgns.bps.system.dao.RoleDao;
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

        for (TellerPercentage tp:employeeAccount.getTellerTaskPercentageList()){
            User user = userService.getUserByCode(tp.getTellerCode());
            if (null == user) {
                throw new RuntimeException("登记揽储人出错, 编号为 [ " + tp.getTellerCode() + " ] 的员工信息不存在，请输入正确的柜员号！");
            }
            if (null == tp.getMainTeller()) throw new RuntimeException("登记揽储人出错，未确定是否主揽储人身份");
        }
        if(employeeAccount.getTellerPaymentPercentageList() != null && !employeeAccount.getTellerPaymentPercentageList().isEmpty()){
            for (TellerPercentage tp:employeeAccount.getTellerPaymentPercentageList()){
                User user = userService.getUserByCode(tp.getTellerCode());
                if (null == user) {
                    throw new RuntimeException("登记揽储人出错, 编号为 [ " + tp.getTellerCode() + " ] 的员工信息不存在，请输入正确的柜员号！");
                }
            }
        }
        if (!employeeAccount.getTaskPaymentSameFlag() && (employeeAccount.getTellerPaymentPercentageList() == null || employeeAccount.getTellerTaskPercentageList().isEmpty())) {
            throw new RuntimeException("计酬比例与任务分成比例不一致，且未提供计酬分成比例");
        }

        //如果前端未提交tellerPaymentPercentage，默认设置为与tellerTaskPercentage一致的比例
        if (employeeAccount.getTaskPaymentSameFlag() ) {
            employeeAccount.setTellerPaymentPercentageList(employeeAccount.getTellerTaskPercentageList());
        }

        //任务分成-在柜员揽储账户表中插入新登记的账户信息
        EmployeeAccount ea = new EmployeeAccount();
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


        // 写入任务分成比例
        List<TellerPercentage> tellerTaskPercentageList = new ArrayList<>();
        Double sumPercentage = (double)0;
        for (TellerPercentage tp:employeeAccount.getTellerTaskPercentageList()){
            TellerPercentage tmpTellerPercentage = new TellerPercentage();
            tmpTellerPercentage.setTellerCode(tp.getTellerCode());
            tmpTellerPercentage.setMainTeller(tp.getMainTeller());
            tmpTellerPercentage.setPercentage(tp.getPercentage());
            tmpTellerPercentage.setStartDate(dbUnboundDepositAccount.getStartDate());
            tmpTellerPercentage.setValidFlag(false);
            tmpTellerPercentage.setRegisterType(DepositDefaultConfig.REGISTER_TYPE_NEW);
            tmpTellerPercentage.setRegisterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_UNCHECKED);
            tmpTellerPercentage.setOpTellerCode(userUtils.getCurrentLoginedUser().getCode());
            tmpTellerPercentage.setCreateTime(new Date());
            tellerTaskPercentageList.add(tmpTellerPercentage);

            sumPercentage += tp.getPercentage();
        }
        ea.setTellerTaskPercentageList(tellerTaskPercentageList);
        if (sumPercentage != 1) throw new RuntimeException("分成比例总和不为100%");
        //将新记录插入到t_cktj_employee_account_task表中
        ea.setCreateBy(userUtils.getCurrentLoginedUser().getId());
        ea.setCreateTime(new Date());
        employeeAccountDao.insertTask(ea);


        // 写入计酬分成比例
        List<TellerPercentage> tellerPaymentPercentageList = new ArrayList<>();
        sumPercentage = (double)0;
        for (TellerPercentage tp:employeeAccount.getTellerPaymentPercentageList()){
            TellerPercentage tmpTellerPercentage = new TellerPercentage();
            tmpTellerPercentage.setTellerCode(tp.getTellerCode());
            tmpTellerPercentage.setPercentage(tp.getPercentage());
            tmpTellerPercentage.setStartDate(dbUnboundDepositAccount.getStartDate());
            tmpTellerPercentage.setValidFlag(false);
            tmpTellerPercentage.setRegisterType(DepositDefaultConfig.REGISTER_TYPE_NEW);
            tmpTellerPercentage.setRegisterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_UNCHECKED);
            tmpTellerPercentage.setOpTellerCode(userUtils.getCurrentLoginedUser().getCode());
            tmpTellerPercentage.setCreateTime(new Date());
            tellerPaymentPercentageList.add(tmpTellerPercentage);

            sumPercentage += tp.getPercentage();
        }
        ea.setTellerPaymentPercentageList(tellerPaymentPercentageList);
        if (sumPercentage != 1) throw new RuntimeException("分成比例总和不为100%");
        //将新记录插入到t_cktj_employee_account_payment表中
        ea.setCreateBy(userUtils.getCurrentLoginedUser().getId());
        ea.setCreateTime(new Date());
        employeeAccountDao.insertPayment(ea);


        // 写入账户自动绑定层级
        AutoBindRule abr = new AutoBindRule();
        abr.setOrgCode(dbUnboundDepositAccount.getOrgCode());
        abr.setAccountNo(dbUnboundDepositAccount.getAccountNo());
        abr.setChildAccountNo(dbUnboundDepositAccount.getChildAccountNo());
        abr.setCustomerNo(dbUnboundDepositAccount.getCustomerNo());
        if (StrUtil.isNotBlank(employeeAccount.getAutoBindRule())) abr.setLevel(employeeAccount.getAutoBindRule());
        else abr.setLevel(DepositDefaultConfig.DEPOSIT_ACCOUNT_AUTO_BIND_LEVEL_CHILD_ACCOUNT);  // 默认使用子账户级的自动绑定
        abr.setCreateBy(userUtils.getCurrentLoginedUser().getId());
        abr.setCreateTime(new Date());
        employeeAccountDao.insertAutoBindRule(abr);


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


        // 设置任务分成人列表
        for (int i=0;i<employeeAccountList.size();i++){
            EmployeeAccount ea = employeeAccountList.get(i);
            TellerPercentage tp = new TellerPercentage();
            tp.setAccountNo(ea.getAccountNo());
            tp.setChildAccountNo(ea.getChildAccountNo());
            tp.setRegisterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_UNCHECKED);   //查找未复核的

            List<TellerPercentage> tellerTaskPercentageList = employeeAccountDao.getTellerTaskPercentageListByAccountNoAndChildAccountNo(tp);
            employeeAccountList.get(i).setTellerTaskPercentageList(tellerTaskPercentageList);
        }

        // 设置计酬分成人
        for (int i=0;i<employeeAccountList.size();i++){
            EmployeeAccount ea = employeeAccountList.get(i);
            TellerPercentage tp = new TellerPercentage();
            tp.setAccountNo(ea.getAccountNo());
            tp.setChildAccountNo(ea.getChildAccountNo());
            tp.setRegisterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_UNCHECKED);   //查找未复核的

            List<TellerPercentage> tellerPaymentPercentageList = employeeAccountDao.getTellerPaymentPercentageListByAccountNoAndChildAccountNo(tp);
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

        TellerPercentage tellerPercentage = new TellerPercentage();
        tellerPercentage.setAccountNo(employeeAccount.getAccountNo());
        tellerPercentage.setChildAccountNo(employeeAccount.getChildAccountNo());
        tellerPercentage.setRegisterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_UNCHECKED);

        // 复核任务分成信息
        List<TellerPercentage> tellerTaskPercentageList = employeeAccountDao.getTellerTaskPercentageListByAccountNoAndChildAccountNo(tellerPercentage);
        if (null == tellerTaskPercentageList || tellerTaskPercentageList.isEmpty()) throw new RuntimeException("不存在未复核的记录");

        //修改状态为已复核
        for (TellerPercentage tp:tellerTaskPercentageList) {
            tp.setValidFlag(true);
            tp.setRegisterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_CHECKED);
            tp.setRegisterCheckTellerCode(userUtils.getCurrentLoginedUser().getCode());
            tp.setRegisterCheckTime(new Date());
            tp.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
            tp.setUpdateTime(new Date());

            employeeAccountDao.updateTaskById(tp);
        }

        // 复核计酬分成信息
        List<TellerPercentage> tellerPaymentPercentageList = employeeAccountDao.getTellerPaymentPercentageListByAccountNoAndChildAccountNo(tellerPercentage);
        if (null == tellerPaymentPercentageList || tellerPaymentPercentageList.isEmpty()) throw new RuntimeException("不存在未复核的记录");

        //修改状态为已复核
        for (TellerPercentage tp:tellerPaymentPercentageList) {
            tp.setValidFlag(true);
            tp.setRegisterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_CHECKED);
            tp.setRegisterCheckTellerCode(userUtils.getCurrentLoginedUser().getCode());
            tp.setRegisterCheckTime(new Date());
            tp.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
            tp.setUpdateTime(new Date());

            employeeAccountDao.updatePaymentById(tp);
        }
    }

    /**
     * 撤销登记揽储人申请
     * @param employeeAccount 包含账号、子账号
     */
    @Transactional
    public void undoRegisterEmployee(EmployeeAccount employeeAccount) throws RuntimeException{
        if (null == employeeAccount || StrUtil.isBlank(employeeAccount.getAccountNo()) ) throw new RuntimeException("未提供参数");

        TellerPercentage tellerPercentage = new TellerPercentage();
        tellerPercentage.setAccountNo(employeeAccount.getAccountNo());
        tellerPercentage.setChildAccountNo(employeeAccount.getChildAccountNo());
        tellerPercentage.setRegisterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_UNCHECKED);

        // 复核任务分成信息
        EmployeeAccount dbEmployeeAccount = employeeAccountDao.getEmployeeAccountByAccountNoAndChildAccountNoFromTask(tellerPercentage);
        List<TellerPercentage> tellerTaskPercentageList = employeeAccountDao.getTellerTaskPercentageListByAccountNoAndChildAccountNo(tellerPercentage);
        if (null == tellerTaskPercentageList || tellerTaskPercentageList.isEmpty()) throw new RuntimeException("不存在未复核的记录");

        //删除t_cktj_employee_account表中记录
        for (TellerPercentage tp:tellerTaskPercentageList) {
            employeeAccountDao.deleteTask(tp.getId());
        }

        List<TellerPercentage> tellerPaymentPercentageList = employeeAccountDao.getTellerPaymentPercentageListByAccountNoAndChildAccountNo(tellerPercentage);
        if (null == tellerPaymentPercentageList || tellerPaymentPercentageList.isEmpty()) throw new RuntimeException("不存在未复核的记录");

        //删除t_cktj_employee_account表中记录
        for (TellerPercentage tp:tellerPaymentPercentageList) {
            employeeAccountDao.deletePayment(tp.getId());
        }

        //恢复t_cktj_unbound_account表中的记录
        employeeAccountDao.insertUnboundEmployeeAccount(dbEmployeeAccount);

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
            TellerPercentage tp = new TellerPercentage();
            tp.setAccountNo(ea.getAccountNo());
            tp.setChildAccountNo(ea.getChildAccountNo());
            tp.setValidFlag(true);

            List<TellerPercentage> tellerTaskPercentageList = employeeAccountDao.getTellerTaskPercentageListByAccountNoAndChildAccountNo(tp);
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
            TellerPercentage tp = new TellerPercentage();
            tp.setAccountNo(ea.getAccountNo());
            tp.setChildAccountNo(ea.getChildAccountNo());
            tp.setRegisterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_CHECKED);   //查找登记已复核的
            tp.setValidFlag(true);

            List<TellerPercentage> tellerTaskPercentageList = employeeAccountDao.getTellerPaymentPercentageListByAccountNoAndChildAccountNo(tp);
            employeeAccountList.get(i).setTellerPaymentPercentageList(tellerTaskPercentageList);
        }

        employeeAccountPageData.setTotal(count);
        employeeAccountPageData.setList(employeeAccountList);

        return employeeAccountPageData;
    }

    /**
     * 任务数变更揽储人
     * @param employeeAccount 新揽储人信息
     */
    @Transactional
    public void modifyTaskEmployee(EmployeeAccount employeeAccount) throws RuntimeException {
        if (null == employeeAccount || employeeAccount.getTellerTaskPercentageList() == null || employeeAccount.getTellerTaskPercentageList().isEmpty() || StrUtil.isBlank(employeeAccount.getAccountNo())) {
            throw new RuntimeException("变更揽储人出错, 请求参数不正确！");
        }

        // 判断员工编号是否正确、比例之和是否为100%
        Double sumPercentage = (double)0;
        for (TellerPercentage tp:employeeAccount.getTellerTaskPercentageList()) {
            User user = userService.getUserByCode(tp.getTellerCode());
            if (null == user) {
                throw new RuntimeException("登记揽储人出错, 编号为 [ " + tp.getTellerCode() + " ] 的员工信息不存在，请输入正确的柜员号！");
            }
            if (null == tp.getMainTeller()) throw new RuntimeException("登记揽储人出错，未确定是否主揽储人身份");
            sumPercentage += tp.getPercentage();
        }
        if(sumPercentage != 1) throw new RuntimeException("比例求和不为100%");

        // 获取原始的任务分成比例
        TellerPercentage tmpTp = new TellerPercentage();
        tmpTp.setAccountNo(employeeAccount.getAccountNo());
        tmpTp.setChildAccountNo(employeeAccount.getChildAccountNo());
        tmpTp.setValidFlag(true);
        List<TellerPercentage> dbTaskTellerPercentageList = employeeAccountDao.getTellerTaskPercentageListByAccountNoAndChildAccountNo(tmpTp);
        EmployeeAccount dbEmployeeAccount = employeeAccountDao.getEmployeeAccountByAccountNoAndChildAccountNoFromTask(tmpTp);

        if (null == dbTaskTellerPercentageList || dbTaskTellerPercentageList.isEmpty() || dbEmployeeAccount == null) {
            throw new RuntimeException("变更揽储人出错, 找不到对应的存款账户信息！");
        }

        // 判断当日是否已经发生过变更（当前不允许当日再次变更，后期优化）
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (TellerPercentage tp:dbTaskTellerPercentageList) {
            String dbDate = sdf.format(tp.getStartDate());
            String currentDate = sdf.format(new Date());
            if (dbDate.equals(currentDate)) throw new RuntimeException("变更为揽储人出错，当日已变更，不允许再次变更");
        }

        //将原揽储人存款账户信息设置为不可变更揽储人
        Date yesterday = DateUtil.yesterday();
        List<Long> newParentIdList = new ArrayList<>();
        for (TellerPercentage tp : dbTaskTellerPercentageList) {
            tmpTp = new TellerPercentage();
            tmpTp.setId(tp.getId());
            tmpTp.setEndDate(yesterday);
            tmpTp.setValidFlag(false);
            tmpTp.setUpdateTime(new Date());
            tmpTp.setUpdateBy(userUtils.getCurrentLoginedUser().getId());

            employeeAccountDao.updateTaskById(tmpTp);
            newParentIdList.add(tp.getId());
        }

        //在柜员揽储账户表中插入新变更揽储人信息
        List<TellerPercentage> tellerPercentageList = new ArrayList<>();
        for (TellerPercentage tp:employeeAccount.getTellerTaskPercentageList()) {
            tmpTp = new TellerPercentage();
            tmpTp.setTellerCode(tp.getTellerCode());
            tmpTp.setMainTeller(tp.getMainTeller());
            tmpTp.setPercentage(tp.getPercentage());
            tmpTp.setStartDate(new Date());
            tmpTp.setValidFlag(false);
            tmpTp.setRegisterType(DepositDefaultConfig.REGISTER_TYPE_REBOUND);      //登记方式：1-变更揽储人登记
            tmpTp.setAlterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_UNCHECKED);
            tmpTp.setOpTellerCode(userUtils.getCurrentLoginedUser().getCode());
            tmpTp.setParentIds(newParentIdList);
            tmpTp.setRemarks(employeeAccount.getRemarks());
            tmpTp.setCreateTime(new Date());
            tmpTp.setCreateBy(userUtils.getCurrentLoginedUser().getId());

            tellerPercentageList.add(tmpTp);
        }
        dbEmployeeAccount.setTellerTaskPercentageList(tellerPercentageList);
        employeeAccountDao.insertTask(dbEmployeeAccount);
    }

    /**
     * 计酬数变更揽储人
     * @param employeeAccount 新揽储人信息
     */
    @Transactional
    public void modifyPaymentEmployee(EmployeeAccount employeeAccount) throws RuntimeException {
        if (null == employeeAccount || employeeAccount.getTellerPaymentPercentageList() == null || employeeAccount.getTellerPaymentPercentageList().isEmpty() || StrUtil.isBlank(employeeAccount.getAccountNo())) {
            throw new RuntimeException("变更揽储人出错, 请求参数不正确！");
        }

        // 判断员工编号是否正确、比例之和是否为100%
        Double sumPercentage = (double)0;
        for (TellerPercentage tp:employeeAccount.getTellerPaymentPercentageList()) {
            User user = userService.getUserByCode(tp.getTellerCode());
            if (null == user) {
                throw new RuntimeException("登记揽储人出错, 编号为 [ " + tp.getTellerCode() + " ] 的员工信息不存在，请输入正确的柜员号！");
            }
            if (null == tp.getMainTeller()) throw new RuntimeException("登记揽储人出错，未确定是否主揽储人身份");
            sumPercentage += tp.getPercentage();
        }
        if(sumPercentage != 1) throw new RuntimeException("比例求和不为100%");

        // 获取原始的任务分成比例
        TellerPercentage tmpTp = new TellerPercentage();
        tmpTp.setAccountNo(employeeAccount.getAccountNo());
        tmpTp.setChildAccountNo(employeeAccount.getChildAccountNo());
        tmpTp.setValidFlag(true);
        List<TellerPercentage> dbPaymentTellerPercentageList = employeeAccountDao.getTellerPaymentPercentageListByAccountNoAndChildAccountNo(tmpTp);
        EmployeeAccount dbEmployeeAccount = employeeAccountDao.getEmployeeAccountByAccountNoAndChildAccountNoFromPayment(tmpTp);

        if (null == dbPaymentTellerPercentageList || dbPaymentTellerPercentageList.isEmpty() || dbEmployeeAccount == null) {
            throw new RuntimeException("变更揽储人出错, 找不到对应的存款账户信息！");
        }

        // 判断当日是否已经发生过变更（当前不允许当日再次变更，后期优化）
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (TellerPercentage tp:dbPaymentTellerPercentageList) {
            String dbDate = sdf.format(tp.getStartDate());
            String currentDate = sdf.format(new Date());
            if (dbDate.equals(currentDate)) throw new RuntimeException("变更为揽储人出错，当日已变更，不允许再次变更");
        }

        //将原揽储人存款账户信息设置为不可变更揽储人
        Date yesterday = DateUtil.yesterday();
        List<Long> newParentIdList = new ArrayList<>();
        for (TellerPercentage tp : dbPaymentTellerPercentageList) {
            tmpTp = new TellerPercentage();
            tmpTp.setId(tp.getId());
            tmpTp.setEndDate(yesterday);
            tmpTp.setValidFlag(false);
            tmpTp.setUpdateTime(new Date());
            tmpTp.setUpdateBy(userUtils.getCurrentLoginedUser().getId());

            employeeAccountDao.updatePaymentById(tmpTp);
            newParentIdList.add(tp.getId());
        }

        //在柜员揽储账户表中插入新变更揽储人信息
        List<TellerPercentage> tellerPercentageList = new ArrayList<>();
        for (TellerPercentage tp:employeeAccount.getTellerPaymentPercentageList()) {
            tmpTp = new TellerPercentage();
            tmpTp.setTellerCode(tp.getTellerCode());
            tmpTp.setMainTeller(tp.getMainTeller());
            tmpTp.setPercentage(tp.getPercentage());
            tmpTp.setStartDate(new Date());
            tmpTp.setValidFlag(false);
            tmpTp.setRegisterType(DepositDefaultConfig.REGISTER_TYPE_REBOUND);      //登记方式：1-变更揽储人登记
            tmpTp.setAlterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_UNCHECKED);
            tmpTp.setOpTellerCode(userUtils.getCurrentLoginedUser().getCode());
            tmpTp.setParentIds(newParentIdList);
            tmpTp.setRemarks(employeeAccount.getRemarks());
            tmpTp.setCreateTime(new Date());
            tmpTp.setCreateBy(userUtils.getCurrentLoginedUser().getId());

            tellerPercentageList.add(tmpTp);
        }
        dbEmployeeAccount.setTellerPaymentPercentageList(tellerPercentageList);
        employeeAccountDao.insertPayment(dbEmployeeAccount);
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
            TellerPercentage tp = new TellerPercentage();
            tp.setAccountNo(ea.getAccountNo());
            tp.setChildAccountNo(ea.getChildAccountNo());
            tp.setValidFlag(false);
            tp.setAlterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_UNCHECKED);   //查找未复核的

            List<TellerPercentage> tellerTaskPercentageList = employeeAccountDao.getTellerTaskPercentageListByAccountNoAndChildAccountNo(tp);
            employeeAccountList.get(i).setTellerTaskPercentageList(tellerTaskPercentageList);
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
            TellerPercentage tp = new TellerPercentage();
            tp.setAccountNo(ea.getAccountNo());
            tp.setChildAccountNo(ea.getChildAccountNo());
            tp.setValidFlag(false);
            tp.setAlterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_UNCHECKED);   //查找未复核的

            List<TellerPercentage> tellerPaymentPercentageList = employeeAccountDao.getTellerPaymentPercentageListByAccountNoAndChildAccountNo(tp);
            employeeAccountList.get(i).setTellerPaymentPercentageList(tellerPaymentPercentageList);
        }

        employeeAccountPageData.setTotal(count);
        employeeAccountPageData.setList(employeeAccountList);

        return employeeAccountPageData;
    }



    /**
     * 复核变更揽储人申请-任务数
     * @param employeeAccount 包含账号、子账号
     */
    @Transactional
    public void checkModifyEmployeeTask(EmployeeAccount employeeAccount) {
        if (null == employeeAccount || StrUtil.isBlank(employeeAccount.getAccountNo())) throw new RuntimeException("未提供参数");

        TellerPercentage tellerPercentage = new TellerPercentage();
        tellerPercentage.setAccountNo(employeeAccount.getAccountNo());
        tellerPercentage.setChildAccountNo(employeeAccount.getChildAccountNo());
        tellerPercentage.setValidFlag(false);
        tellerPercentage.setAlterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_UNCHECKED);

        // 复核任务分成信息
        List<TellerPercentage> tellerTaskPercentageList = employeeAccountDao.getTellerTaskPercentageListByAccountNoAndChildAccountNo(tellerPercentage);
        if (null == tellerTaskPercentageList || tellerTaskPercentageList.isEmpty()) throw new RuntimeException("不存在未复核的记录");

        //修改状态为已复核
        for (TellerPercentage tp:tellerTaskPercentageList) {
            tp.setValidFlag(true);
            tp.setAlterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_CHECKED);
            tp.setAlterCheckTellerCode(userUtils.getCurrentLoginedUser().getCode());
            tp.setAlterCheckTime(new Date());
            tp.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
            tp.setUpdateTime(new Date());

            employeeAccountDao.updateTaskById(tp);
        }

        // 复核计酬分成信息
        List<TellerPercentage> tellerPaymentPercentageList = employeeAccountDao.getTellerPaymentPercentageListByAccountNoAndChildAccountNo(tellerPercentage);
        if (null == tellerPaymentPercentageList || tellerPaymentPercentageList.isEmpty()) throw new RuntimeException("不存在未复核的记录");

        //修改状态为已复核
        for (TellerPercentage tp:tellerPaymentPercentageList) {
            tp.setValidFlag(true);
            tp.setRegisterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_CHECKED);
            tp.setRegisterCheckTellerCode(userUtils.getCurrentLoginedUser().getCode());
            tp.setRegisterCheckTime(new Date());
            tp.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
            tp.setUpdateTime(new Date());

            employeeAccountDao.updatePaymentById(tp);
        }
    }

    /**
     * 复核变更揽储人申请-计酬数
     * @param employeeAccount 包含账号、子账号
     */
    @Transactional
    public void checkModifyEmployeePayment(EmployeeAccount employeeAccount) {
        if (null == employeeAccount || StrUtil.isBlank(employeeAccount.getAccountNo())) throw new RuntimeException("未提供参数");

        TellerPercentage tellerPercentage = new TellerPercentage();
        tellerPercentage.setAccountNo(employeeAccount.getAccountNo());
        tellerPercentage.setChildAccountNo(employeeAccount.getChildAccountNo());
        tellerPercentage.setValidFlag(false);
        tellerPercentage.setAlterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_UNCHECKED);

        // 复核计酬分成信息
        List<TellerPercentage> tellerPaymentPercentageList = employeeAccountDao.getTellerPaymentPercentageListByAccountNoAndChildAccountNo(tellerPercentage);
        if (null == tellerPaymentPercentageList || tellerPaymentPercentageList.isEmpty()) throw new RuntimeException("不存在未复核的记录");

        //修改状态为已复核
        for (TellerPercentage tp:tellerPaymentPercentageList) {
            tp.setValidFlag(true);
            tp.setAlterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_CHECKED);
            tp.setAlterCheckTellerCode(userUtils.getCurrentLoginedUser().getCode());
            tp.setAlterCheckTime(new Date());
            tp.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
            tp.setUpdateTime(new Date());

            employeeAccountDao.updatePaymentById(tp);
        }

    }

    /**
     * 撤销变更揽储人申请-任务数
     * @param employeeAccount 包含accountNo、childAccountNo
     */
    @Transactional
    public void undoModifyEmployeeTask(EmployeeAccount employeeAccount) {
        if (null == employeeAccount || StrUtil.isBlank(employeeAccount.getAccountNo())) throw new RuntimeException("未提供参数");

        TellerPercentage tellerPercentage = new TellerPercentage();
        tellerPercentage.setAccountNo(employeeAccount.getAccountNo());
        tellerPercentage.setChildAccountNo(employeeAccount.getChildAccountNo());
        tellerPercentage.setValidFlag(false);
        tellerPercentage.setAlterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_UNCHECKED);

        // 查找未复核的任务分成信息
        List<TellerPercentage> tellerTaskPercentageList = employeeAccountDao.getTellerTaskPercentageListByAccountNoAndChildAccountNo(tellerPercentage);
        if (null == tellerTaskPercentageList || tellerTaskPercentageList.isEmpty()) throw new RuntimeException("不存在未复核的记录");

        //将原揽储人存款账户信息设置为可变更揽储人
        Set<Long> parentIds = new HashSet<>();
        for (TellerPercentage tp:tellerTaskPercentageList) {
            parentIds.addAll(tp.getParentIds());
        }
        for (Long id:parentIds){
            TellerPercentage tp = new TellerPercentage();
            tp.setId(id);
            tp.setValidFlag(true);
            tp.setEndDate(null);
            tp.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
            tp.setUpdateTime(new Date());

            employeeAccountDao.updateTaskById(tp);
        }

        //删除新记录
        for (TellerPercentage tp:tellerTaskPercentageList) {
            employeeAccountDao.deleteTask(tp.getId());
        }

    }

    /**
     * 撤销变更揽储人申请-计酬数
     * @param employeeAccount 包含accountNo、childAccountNo
     */
    @Transactional
    public void undoModifyEmployeePayment(EmployeeAccount employeeAccount) {
        if (null == employeeAccount || StrUtil.isBlank(employeeAccount.getAccountNo())) throw new RuntimeException("未提供参数");

        TellerPercentage tellerPercentage = new TellerPercentage();
        tellerPercentage.setAccountNo(employeeAccount.getAccountNo());
        tellerPercentage.setChildAccountNo(employeeAccount.getChildAccountNo());
        tellerPercentage.setValidFlag(false);
        tellerPercentage.setAlterCheckStatus(DepositDefaultConfig.CHECKED_STATUS_UNCHECKED);

        // 查找未复核的任务分成信息
        List<TellerPercentage> tellerPaymentPercentageList = employeeAccountDao.getTellerPaymentPercentageListByAccountNoAndChildAccountNo(tellerPercentage);
        if (null == tellerPaymentPercentageList || tellerPaymentPercentageList.isEmpty()) throw new RuntimeException("不存在未复核的记录");

        //将原揽储人存款账户信息设置为可变更揽储人
        Set<Long> parentIds = new HashSet<>();
        for (TellerPercentage tp:tellerPaymentPercentageList) {
            parentIds.addAll(tp.getParentIds());
        }
        for (Long id:parentIds){
            TellerPercentage tp = new TellerPercentage();
            tp.setId(id);
            tp.setValidFlag(true);
            tp.setEndDate(null);
            tp.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
            tp.setUpdateTime(new Date());

            employeeAccountDao.updatePaymentById(tp);
        }

        //删除新记录
        for (TellerPercentage tp:tellerPaymentPercentageList) {
            employeeAccountDao.deletePayment(tp.getId());
        }

    }








}
