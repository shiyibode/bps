package org.nmgns.bps.cktj.service;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
     * @param tellerPercentage 包含分页信息
     */
    public PageData<TellerPercentage> findTaskModifiableTellerPercentagePage(TellerPercentage tellerPercentage) {
        if (null == tellerPercentage || tellerPercentage.getPage() == null) throw new RuntimeException("未提供参数");

        tellerPercentage.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/employeeaccount/gettaskmodifiableaccount").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "o", ""));

        //设置pageNo
        tellerPercentage.setPage(new Page(tellerPercentage.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));

        PageData<TellerPercentage> tellerPercentagePageData = new PageData<>();
        Long count = employeeAccountDao.findTaskModifiableAccountCount(tellerPercentage);
        List<TellerPercentage> tellerPercentageList = employeeAccountDao.findTaskModifiableAccount(tellerPercentage);

        tellerPercentagePageData.setTotal(count);
        tellerPercentagePageData.setList(tellerPercentageList);

        return tellerPercentagePageData;
    }

    /**
     * 获取计酬数可变更揽储人的账户信息列表
     * @param tellerPercentage 包含分页信息
     */
    public PageData<TellerPercentage> findPaymentModifiableTellerPercentagePage(TellerPercentage tellerPercentage) {
        if (null == tellerPercentage || tellerPercentage.getPage() == null) throw new RuntimeException("未提供参数");

        tellerPercentage.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/employeeaccount/gettaskmodifiableaccount").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "o", ""));

        //设置pageNo
        tellerPercentage.setPage(new Page(tellerPercentage.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));

        PageData<TellerPercentage> tellerPercentagePageData = new PageData<>();
        Long count = employeeAccountDao.findPaymentModifiableAccountCount(tellerPercentage);
        List<TellerPercentage> tellerPercentageList = employeeAccountDao.findPaymentModifiableAccount(tellerPercentage);

        tellerPercentagePageData.setTotal(count);
        tellerPercentagePageData.setList(tellerPercentageList);

        return tellerPercentagePageData;
    }

    /**
     * 任务数变更揽储人
     * @param tellerPercentage 新揽储人信息
     */
    @Transactional
    public void modifyTaskEmployee(TellerPercentage tellerPercentage) throws RuntimeException {
//        if (null == tellerPercentage || StrUtil.isBlank(tellerPercentage.getTellerCode()) || ) {
//            throw new RuntimeException("变更揽储人出错, 请求参数不正确！");
//        }
//
//        EmployeeAccount dbEmployeeAccount = dao.get(employeeAccount.getId());
//        if (null == dbEmployeeAccount) {
//            throw new ServiceException("变更揽储人出错, 找不到对应的存款账户信息！");
//        }
//
//        if (!dbEmployeeAccount.getValidFlag()) {
//            StringBuilder sb = new StringBuilder();
//            sb.append(dbEmployeeAccount.getTellerCode());
//            sb.append(" 账号：").append(dbEmployeeAccount.getAccountNo());
//            sb.append(" 的存款账户信息不可变更揽储人，请刷新数据后重试！");
//            throw new ServiceException(sb.toString());
//        }
//
//        if (StringUtils.equals(employeeAccount.getTellerCode(), dbEmployeeAccount.getTellerCode())) {
//            throw new ServiceException("变更揽储人出错, 变更后的揽储人与当前揽储人为同一人，请核对信息后重试！");
//        }
//
//        User user = userOrganizationDao.getUserByUserCode(employeeAccount.getTellerCode());
//        if (null == user) {
//            throw new ServiceException("变更揽储人出错, 编号为 [ " + employeeAccount.getTellerCode() + " ] 的员工信息不存在，请输入正确的柜员号！");
//        }
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String dbDate = sdf.format(dbEmployeeAccount.getStartDate());
//        String currentDate = sdf.format(new Date());
//        if (dbDate.equals(currentDate)) throw new ServiceException("变更为揽储人出错，当日已变更，不允许再次变更");
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DATE,-1);
//        Date yesterday = calendar.getTime();
//
//        //将原揽储人存款账户信息设置为不可变更揽储人
//        EmployeeAccount tmpEA = new EmployeeAccount();
//        tmpEA.setId(dbEmployeeAccount.getId());
//        tmpEA.setEndDate(yesterday);
//        tmpEA.setValidFlag(false);
//        tmpEA.preUpdate();
//        dao.update(tmpEA);
//
//        //在柜员揽储账户表中插入新变更揽储人信息
//        EmployeeAccount ea = new EmployeeAccount();
//        ea.setTellerCode(user.getCode());
//        ea.setOrgCode(dbEmployeeAccount.getOrgCode());
//        ea.setAccountNo(dbEmployeeAccount.getAccountNo());
//        ea.setCardNo(dbEmployeeAccount.getCardNo());
//        ea.setAccountType(dbEmployeeAccount.getAccountType());
//        ea.setAccountOpenDate(dbEmployeeAccount.getAccountOpenDate());
//        ea.setCustomerNo(dbEmployeeAccount.getCustomerNo());
//        ea.setCustomerName(dbEmployeeAccount.getCustomerName());
//        ea.setCustomerType(dbEmployeeAccount.getCustomerType());
//        ea.setIdentityNo(dbEmployeeAccount.getIdentityNo());
//        ea.setIdentityType(dbEmployeeAccount.getIdentityType());
//        ea.setStartDate(new Date());
//        ea.setValidFlag(false);
//        ea.setRegisterType(EmployeeAccount.REGISTER_TYPE_REBOUND);    //登记方式：1-变更揽储人登记
//        ea.setAlterCheckStatus(EmployeeAccount.CHECKED_STATUS_UNCHECKED);
//        ea.setOpTellerCode(UserUtils.getUser().getCode());
//        ea.setParentId(dbEmployeeAccount.getId());
//        ea.setRemarks(employeeAccount.getRemarks());
//        ea.setCreateTime(new Date());
//        ea.preInsert();
//        dao.insert(ea);
    }








}
