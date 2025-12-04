package org.nmgns.bps.dktj.service;

import cn.hutool.core.util.StrUtil;
import org.nmgns.bps.cktj.entity.EmployeeAccount;
import org.nmgns.bps.dktj.dao.EmployeeCustomerDao;
import org.nmgns.bps.dktj.dao.TemplateDao;
import org.nmgns.bps.dktj.dto.CustomerStatus;
import org.nmgns.bps.dktj.dto.EmployeeUnboundCustomer;
import org.nmgns.bps.dktj.dto.UserInfoForBind;
import org.nmgns.bps.dktj.entity.*;
import org.nmgns.bps.system.entity.User;
import org.nmgns.bps.system.entity.UserOrganization;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeCustomerSerivce {

    @Autowired
    private EmployeeCustomerDao employeeCustomerDao;
    @Autowired
    private UserService userService;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private TemplateDao templateDao;
    @Autowired
    private ApiService apiService;

    /**
     *获取未绑定客户列表
     */
    public PageData<EmployeeCustomer> findUnregisterCustomerPage(EmployeeCustomer eup) throws Exception{
        if (eup == null || eup.getPage() == null) throw new RuntimeException("未设置分页条件");

        eup.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/employeecustomer/getUnregisterCustomer").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "o", ""));

        //设置pageNo
        eup.setPage(new Page(eup.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));

        PageData<EmployeeCustomer> employeeCustomerPageData = new PageData<>();
        Long count = employeeCustomerDao.findUnregisterCustomerCount(eup);
        List<EmployeeCustomer> employeeCustomerList = employeeCustomerDao.findUnregisterCustomer(eup);


        employeeCustomerPageData.setTotal(count);
        employeeCustomerPageData.setList(employeeCustomerList);
        return employeeCustomerPageData;
    }

    /**
     *绑定客户到员工
     */
    @Transactional(rollbackFor=Exception.class)
    public void registerEmployee(EmployeeCustomer euc) throws Exception{
        if (StrUtil.isBlank(euc.getAccountNo())) throw new Exception("登记维护人时缺少贷款账号");
        if (StrUtil.isBlank(euc.getOrgCode())) throw new Exception("登记维护人时缺少机构号");
        if (StrUtil.isBlank(euc.getTellerCode())) throw new Exception("登记维护人时缺少维护人信息");

        // 1-获取到该客户在该网点的未绑定的贷款账号详细信息
        EmployeeCustomer eucInfo = employeeCustomerDao.getUnboundCustomerInfoByAccountNo(euc.getOrgCode(), euc.getAccountNo());
        // 2-获取该客户在该网点的贷款账户信息，只有一条（或不存在）。
        if (eucInfo == null) throw new Exception("通过贷款账号获取未登记维护人信息失败");

        //3-判断客户是否已登记了状态，判断该客户是否已经有了状态信息
        CustomerStatus temp = new CustomerStatus();
        temp.setXdCustomerNo(eucInfo.getXdCustomerNo());
        temp.setOrgCode(eucInfo.getOrgCode());
        List<CustomerStatus> csList = employeeCustomerDao.getCustomerStatus(temp);
        if (StrUtil.isBlank(euc.getStatus()) && (csList == null || csList.isEmpty())) throw new Exception("登记维护人时缺少客户流动/固定状态");
        // 4-只可以在固定/流动客户转变功能中做变更，不可以在重新绑定释放的客户时做改变(且只取最新的状态)
        if (eucInfo.getStatus() != null && !eucInfo.getStatus().equals(euc.getStatus())){
            if (eucInfo.getStatus().equals(EmployeeCustomer.STATUS_FIXED_CUSTOMER)) throw new Exception("客户已为固定状态，不允许更改");
            if (eucInfo.getStatus().equals(EmployeeCustomer.STATUS_FLUID_CUSTOMER)) throw new Exception("客户已为流动状态，不允许更改");
        }

        // 5-固定客户不允许跨机构绑定
        String currentOrganizationCode = employeeCustomerDao.getUserCurrentOrganizationCodeByUserCode(euc.getTellerCode());
        if (!currentOrganizationCode.equals(eucInfo.getOrgCode()) && euc.getStatus().equals(EmployeeCustomer.STATUS_FIXED_CUSTOMER)) throw new Exception("登记维护人出错，固定客户无法绑定到非本机构的员工");

        User currentUser = userUtils.getCurrentLoginedUser();

        //7-对于新客户的账号
        if (eucInfo.getFlag().equals(EmployeeCustomer.FLAG_NEW_CUSTOMER) ){
            // 8.1-判断所有岗位是否都已填写了行内员工
            if (euc.getAccountShareInfoList() == null || euc.getAccountShareInfoList().size() == 0) throw new Exception("新账户登记时未填写相关责任人");
            for (AccountShareInfo asi:euc.getAccountShareInfoList()){
                TemplateDetail td = templateDao.findTemplateDetailInfoById(asi.getTemplateDetailId());
                String positionName = td.getPositionName();
                if (StrUtil.isBlank(asi.getTellerCode())) throw new Exception("未登记账户 “"+positionName+"” 岗位的责任人");
                // 8.2-判断柜员号是否存在
                User user = userService.getUserByCode(asi.getTellerCode());
                if (null == user) throw new Exception("柜员号 “"+asi.getTellerCode()+"” 不存在！");
                // 8.3-如果模板中有推荐人，则模板中的推荐人和上方的维护人应该是同一个人
                String positionType = td.getPositionType();
                if (positionType == null) throw new Exception("登记维护人时，无法获取到岗位类型，账号: "+euc.getAccountNo());
                if (positionType.equals(Position.POSITION_TYPE_TUI_JIAN_REN)){
                    if (!asi.getTellerCode().equals(euc.getTellerCode())) throw new Exception("客户信息中的推荐人和分成模板中的推荐人不一致！");
                }
            }

            // 9-绑定新客户：更新t_dktj_customer_status中客户的status，即固定/流动状态; 柜员调动后释放的客户不应该修改其status(固定/流动状态)，而且释放的客户只可能是固定状态
                           //已登记了状态的客户（无论是否已经复核）都不再登记状态
            if (csList == null || csList.size() == 0){
                CustomerStatus cs = new CustomerStatus();
                cs.setStartDate(eucInfo.getDate());
                cs.setStatus(euc.getStatus());
                cs.setXdCustomerNo(eucInfo.getXdCustomerNo());
                cs.setOrgCode(euc.getOrgCode());
                cs.setCreateBy(currentUser.getId());
                cs.setCreateTime(new Date());
                cs.setValidFlag(false);
                cs.setRegisterAccountNo(euc.getAccountNo());
                cs.setCheckStatus(CustomerStatus.CHECK_STATUS_UNCHECKED);
                employeeCustomerDao.insertCustomerOrgAndStatus(cs);
            }

            // 10-写入模板账户对照表（只有新账户写入，释放的账户无需更改模板信息）
            AccountTemplate at = new AccountTemplate();
            at.setAccountNo(eucInfo.getAccountNo());
            at.setTemplateId(euc.getTemplateId());
            at.setStartDate(eucInfo.getDate());
            at.setValidFlag(false);
            at.setCreateTime(new Date());
            at.setCreateBy(currentUser.getId());
            at.setOrgCode(eucInfo.getOrgCode());
            at.setCheckStatus(AccountTemplate.CHECK_STATUS_UNCHECKED);
            templateDao.insertAccountTemplate(at);
            Long accountTemplateId = at.getId();
            if (accountTemplateId == null) throw new Exception("账号信息写入账号模板对照表失败！");

            // 11-写入分成比例信息表（释放的账户无需写入）
            for (AccountShareInfo asi:euc.getAccountShareInfoList()){
                asi.setAccountTemplateId(accountTemplateId);
                asi.setStartDate(eucInfo.getDate());
                asi.setValidFlag(false);
                asi.setCheckStatus(AccountShareInfo.CHECK_STATUS_UNCHECKED);
                asi.setCreateTime(new Date());
                asi.setCreateBy(currentUser.getId());
                templateDao.insertAccountShareInfo(asi);
            }

            // 12-将账号的特殊标记写入数据库
            if (euc.getSpecialAccountTypeId() != null){
                SpecialAccountType spa = new SpecialAccountType();
                spa.setOrgCode(eucInfo.getOrgCode());
                spa.setAccountNo(eucInfo.getAccountNo());
                spa.setTypeId(euc.getSpecialAccountTypeId());
                spa.setStartDate(eucInfo.getDate());
                spa.setValidFlag(false);
                spa.setCheckStatus(SpecialAccountType.CHECK_STATUS_UNCHECKED);
                spa.setCreateBy(currentUser.getId());
                spa.setCreateTime(new Date());
                employeeCustomerDao.insertSpecialAccountType(spa);
            }
        }


        //12-对于流动转固定释放的客户、柜员调动释放的客户，需新写入分成信息
        if (eucInfo.getFlag().equals(EmployeeCustomer.FLAG_RELEASED_CUSTOMER) || eucInfo.getFlag().equals(EmployeeCustomer.FLAG_RELEASED_STATUS_RELEASED)){
            // 获取分成模板，判断分成模板中是否有维护人
            AccountTemplate temp2 = new AccountTemplate();
            temp2.setAccountNo(eucInfo.getAccountNo());
            temp2.setOrgCode(eucInfo.getOrgCode());
            AccountTemplate at = templateDao.findAccountCurrentTemplateByAccountNo(temp2);
            if (at == null || at.getTemplateId() == null) throw new Exception("无法通过贷款账号获取到最新的账号模板对应信息");
            TemplateDetail td = new TemplateDetail();
            td.setTemplateId(at.getTemplateId());
            td.setPosition(new Position());
            Long tdId = templateDao.getTemplateDetailIdByTemplateId(td);
            // 如果模板中有维护人这一项，就写入一条新的维护人记录，并修改旧的维护人记录为false
            if (tdId != null ){
                AccountShareInfo asTemp = new AccountShareInfo();
                asTemp.setPosition(new Position());
                asTemp.setAccountTemplateId(at.getId());
                AccountShareInfo origAS = templateDao.findAccountShareInfoByAccountTemplateId(asTemp);
                if (origAS == null || origAS.getId() == null ) throw new Exception("无法通过贷款账号获取到最新的分成信息中的推荐人");

                //设置旧的分成信息中的推荐人信息为false
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(eucInfo.getDate());
                calendar.set(Calendar.HOUR_OF_DAY,-24);
                Date date_yesterday = calendar.getTime();
                asTemp = new AccountShareInfo();
                asTemp.setId(origAS.getId());
                asTemp.setEndDate(date_yesterday);
                asTemp.setValidFlag(false);
                asTemp.setUpdateTime(new Date());
                asTemp.setUpdateBy(currentUser.getId());
                templateDao.updateAccountShareInfoById(asTemp);

                //在分成信息中插入新的推荐人信息
                AccountShareInfo temp4 = new AccountShareInfo();
                temp4.setAccountTemplateId(at.getId());
                temp4.setTemplateDetailId(tdId);
                temp4.setTellerCode(euc.getTellerCode());
                temp4.setStartDate(eucInfo.getDate());
                temp4.setParentId(origAS.getId());
                temp4.setValidFlag(false);
                if (eucInfo.getFlag().equals(EmployeeCustomer.FLAG_RELEASED_CUSTOMER)) temp4.setRemarks("柜员调动释放后重新绑定到: "+euc.getTellerCode());
                if (eucInfo.getFlag().equals(EmployeeCustomer.FLAG_RELEASED_STATUS_RELEASED)) temp4.setRemarks("客户流动转固定后贷款账户重新绑定到: "+euc.getTellerCode());
                temp4.setCreateTime(new Date());
                temp4.setCreateBy(userUtils.getCurrentLoginedUser().getId());
                temp4.setCheckStatus(AccountShareInfo.CHECK_STATUS_UNCHECKED);
                templateDao.insertAccountShareInfo(temp4);
            }


        }

        // 6-将未绑定的贷款账号绑定到员工
        // 6.1-构造
        EmployeeCustomer ec = new EmployeeCustomer();
        ec.setOrgCode(euc.getOrgCode());
        ec.setAccountNo(eucInfo.getAccountNo());
        ec.setAccountOpenDate(eucInfo.getAccountOpenDate());
        ec.setXdCustomerNo(eucInfo.getXdCustomerNo());
        ec.setHxCustomerNo(eucInfo.getHxCustomerNo());
        ec.setCustomerName(eucInfo.getCustomerName());
        ec.setIdentityNo(eucInfo.getIdentityNo());
        ec.setIdentityType(eucInfo.getIdentityType());
        ec.setCustomerType(eucInfo.getCustomerType());
        ec.setTellerCode(euc.getTellerCode());
        ec.setStartDate(eucInfo.getDate());
        ec.setValidFlag(false);
        ec.setRegisterCheckStatus(EmployeeCustomer.CHECKED_STATUS_UNCHECKED);
        ec.setOpTellerCode(currentUser.getCode());
        ec.setCreateTime(new Date());
        ec.setRemarks(euc.getRemarks());
        ec.setBoundType(EmployeeCustomer.BOUND_FLAG_MANUAL);
        // 6.2-设置登记类型
        if (eucInfo.getFlag().equals(EmployeeUnboundCustomer.FLAG_NEW_CUSTOMER)) ec.setRegisterType(EmployeeCustomer.REGISTER_TYPE_NEW_CUSTOMER);
        else if (eucInfo.getFlag().equals(EmployeeUnboundCustomer.FLAG_RELEASED_CUSTOMER)) ec.setRegisterType(EmployeeCustomer.REGISTER_TYPE_RELEASED_CUSTOMER);
        else if (eucInfo.getFlag().equals(EmployeeUnboundCustomer.FLAG_RELEASED_STATUS_RELEASED)) ec.setRegisterType(EmployeeCustomer.REGISTER_TYPE_STATUS_RELEASED_CUSTOMER);
        else throw new Exception("登记信贷客户维护人时无法获取到有效的登记类型(flag)");
        // 6.3-将新记录插入到t_dktj_employee_customer表中
        employeeCustomerDao.insertEmployeeCustomer(ec);
        // 6.4-删除t_dktj_unbound_customer表中相应的记录
        employeeCustomerDao.deleteUnboundCustomerInfoByXdCustomerNo(ec.getOrgCode(),eucInfo.getXdCustomerNo(),eucInfo.getAccountNo());
        
    }

    /**
     *获取已绑定但未复核的客户列表
     */
    public PageData<EmployeeCustomer> findRegisterUncheckedCustomer(EmployeeCustomer ec) throws Exception{
        if (ec == null || ec.getPage() == null) throw new Exception("未设置分页条件");

        ec.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/employeecustomer/getUncheckedCustomer").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "o", ""));

        //设置pageNo
        ec.setPage(new Page(ec.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));

        PageData<EmployeeCustomer> employeeCustomerPageData = new PageData<>();
        Long count = employeeCustomerDao.findRegisterUncheckedCustomerCount(ec);
        List<EmployeeCustomer> employeeCustomerList = employeeCustomerDao.findRegisterUncheckedCustomer(ec);


        employeeCustomerPageData.setTotal(count);
        employeeCustomerPageData.setList(employeeCustomerList);
        return employeeCustomerPageData;
    }

    /**
     *同意维护人绑定
     */
    @Transactional(rollbackFor=Exception.class)
    public void checkRegisterEmployee(List<EmployeeCustomer> ecList) throws Exception{
        for(EmployeeCustomer ec: ecList) {
            if (null == ec.getId()) throw new Exception("复核失败：复核记录的id不存在");
            if (StrUtil.isBlank(ec.getAccountNo())) throw new Exception("复核失败：未提供正确的贷款账号");
            if (StrUtil.isBlank(ec.getOrgCode()))
                throw new Exception(("复核失败：未提供正确的机构号"));  //后期应该判断这个柜员是否有这个机构，否则可能会发生：柜员登录以后，模拟请求，发送不同的org_code，更新了客户在其他机构的信息。

            EmployeeCustomer dbec = employeeCustomerDao.getEmployeeCustomerById(ec.getId());
            if (dbec == null) throw new Exception("无法通过id: " + ec.getId() + " 获取到未复核的信息");
            User currentUser = userUtils.getCurrentLoginedUser();

            ec.setRegisterCheckTellerCode(currentUser.getCode());
            ec.setRegisterCheckTime(new Date());
            ec.setRegisterCheckStatus(EmployeeCustomer.CHECKED_STATUS_CHECKED);
            ec.setValidFlag(true);

            int re = employeeCustomerDao.checkRegisterEmployee(ec);
            if (re == 0) throw new Exception("复核失败：未更新数据库，客户号为" + ec.getXdCustomerNo() + "，请管理员检查");

            //新客户时更改t_dktj_customer_status中的check_status='1'
            CustomerStatus temp = new CustomerStatus();
            temp.setXdCustomerNo(dbec.getXdCustomerNo());
            temp.setOrgCode(dbec.getOrgCode());
            CustomerStatus cs = employeeCustomerDao.getUncheckedCustomerStatusByXdCustomerNoAndOrg(temp);
//            if (cs == null && dbec.getRegisterType().equals(EmployeeCustomer.REGISTER_TYPE_NEW_CUSTOMER)) throw new Exception("通过客户号: "+dbec.getXdCustomerNo()+" 未找到未复核的状态信息");
            if (cs != null && !dbec.getRegisterType().equals(EmployeeCustomer.REGISTER_TYPE_NEW_CUSTOMER))
                throw new Exception("客户 " + dbec.getXdCustomerNo() + " 已绑定状态却存在未复核的状态");
            if (cs != null) {
                CustomerStatus temp2 = new CustomerStatus();
                temp2.setId(cs.getId());
                temp2.setCheckStatus(CustomerStatus.CHECK_STATUS_CHECKED);
                temp2.setValidFlag(true);
                employeeCustomerDao.updateValidCustomerOrgAndStatusById(temp2);
            }

            //设置特殊标记为有效状态
            SpecialAccountType tempSpa = new SpecialAccountType();
            tempSpa.setAccountNo(dbec.getAccountNo());
            tempSpa.setOrgCode(dbec.getOrgCode());
            SpecialAccountType unCheckSpa = employeeCustomerDao.getRegisterUncheckSpecialAccountType(tempSpa);
            if (unCheckSpa != null && unCheckSpa.getId() != null) {
                tempSpa = new SpecialAccountType();
                tempSpa.setValidFlag(true);
                tempSpa.setCheckStatus(SpecialAccountType.CHECK_STATUS_CHECKED);
                tempSpa.setId(unCheckSpa.getId());
                employeeCustomerDao.updateSpecialAccountType(tempSpa);
            }

            //对于新客户，更改t_dktj_account_template的复核状态
            AccountTemplate temp3 = new AccountTemplate();
            temp3.setAccountNo(dbec.getAccountNo());
            temp3.setOrgCode(dbec.getOrgCode());
            AccountTemplate at = employeeCustomerDao.getUncheckedAccountTemplateByAccountNoAndOrg(temp3);
            if (at == null && dbec.getRegisterType().equals(EmployeeCustomer.REGISTER_TYPE_NEW_CUSTOMER))
                throw new Exception("通过账号: " + dbec.getAccountNo() + " 未找到未复核的账号模板信息");
            if (at != null && !dbec.getRegisterType().equals(EmployeeCustomer.REGISTER_TYPE_NEW_CUSTOMER))
                throw new Exception("账号 " + dbec.getAccountNo() + " 已绑定模板却存在未复核的状态");
            if (at != null) {
                AccountTemplate temp4 = new AccountTemplate();
                temp4.setId(at.getId());
                temp4.setValidFlag(true);
                temp4.setCheckStatus(AccountTemplate.CHECK_STATUS_CHECKED);
                temp4.setUpdateBy(currentUser.getId());
                temp4.setUpdateTime(new Date());
                employeeCustomerDao.updateAccountTemplateById(temp4);
            }

            //更改t_dktj_account_share_info的复核状态
            AccountShareInfo temp5 = new AccountShareInfo();
            temp5.setAccountNo(dbec.getAccountNo());
            temp5.setOrgCode(dbec.getOrgCode());
            List<AccountShareInfo> asList = employeeCustomerDao.getUncheckedAccountShareInfoByAccountNoAndOrg(temp5);
            for (AccountShareInfo as : asList) {
                AccountShareInfo temp6 = new AccountShareInfo();
                temp6.setId(as.getId());
                temp6.setValidFlag(true);
                temp6.setCheckStatus(AccountShareInfo.CHECK_STATUS_CHECKED);
                temp6.setUpdateBy(currentUser.getId());
                temp6.setUpdateTime(new Date());

                employeeCustomerDao.updateAccountShareInfoById(temp6);
            }
        }
    }

    /**
     * 拒绝复核维护人绑定
     */
    @Transactional(rollbackFor=Exception.class)
    public void undoRegisterEmployee(List<EmployeeCustomer> ecList) throws Exception{
        for (EmployeeCustomer ec:ecList){
            if (null == ec.getId()) throw new Exception("复核拒绝失败：复核记录的id不存在");
            if (StrUtil.isBlank(ec.getAccountNo()) ) throw new Exception("复核拒绝失败：未提供正确的贷款账号");
            if (StrUtil.isBlank(ec.getOrgCode()) ) throw new Exception("复核拒绝失败：未提供正确的机构号");

            EmployeeCustomer ecInfo = employeeCustomerDao.getUncheckedCustomerInfoByAccountNo(ec);

            if (null == ecInfo) throw new Exception("复核拒绝失败：通过账号"+ec.getAccountNo()+"无法查找到未复核信息");

            EmployeeCustomer euc = new EmployeeUnboundCustomer();
            euc.setXdCustomerNo(ecInfo.getXdCustomerNo());
            euc.setAccountNo(ecInfo.getAccountNo());
            euc.setAccountOpenDate(ecInfo.getAccountOpenDate());
            euc.setCustomerName(ecInfo.getCustomerName());
            euc.setDate(ecInfo.getStartDate());
            euc.setOrgCode(ecInfo.getOrgCode());
            euc.setIdentityType(ecInfo.getIdentityType());
            euc.setIdentityNo(ecInfo.getIdentityNo());
            euc.setCustomerType(ecInfo.getCustomerType());
            euc.setCreateTime(new Date());
//            euc.setFlag(ecInfo.getRegisterType());
            if (ecInfo.getRegisterType().equals(EmployeeCustomer.REGISTER_TYPE_NEW_CUSTOMER)) euc.setFlag(EmployeeUnboundCustomer.FLAG_NEW_CUSTOMER);
            else if (ecInfo.getRegisterType().equals(EmployeeCustomer.REGISTER_TYPE_RELEASED_CUSTOMER)) euc.setFlag(EmployeeUnboundCustomer.FLAG_RELEASED_CUSTOMER);
            else if (ecInfo.getRegisterType().equals(EmployeeCustomer.REGISTER_TYPE_STATUS_RELEASED_CUSTOMER)) euc.setFlag(EmployeeUnboundCustomer.FLAG_RELEASED_STATUS_RELEASED);
            else throw new Exception("拒绝维护人绑定时，无法获取到有效的登记类型(registerType)");

            employeeCustomerDao.deleteRegisterEmployee(ecInfo);
            employeeCustomerDao.insertUnboundCustomer(euc);

            if (ecInfo.getRegisterType().equals(EmployeeCustomer.REGISTER_TYPE_NEW_CUSTOMER)){
                //获取t_dktj_employee_account表中，该信贷客户在该机构还有几条记录
                int accountCnt = employeeCustomerDao.getAccountCount(ecInfo.getXdCustomerNo(),ecInfo.getOrgCode());

                //1-对于新客户，删除t_dktj_customer_org_and_status的status(当t_dktj_employee_customer表中没有该客户的账号时，可以删除该条状态记录)
                if (ecInfo.getRegisterType().equals(EmployeeCustomer.FLAG_NEW_CUSTOMER) && accountCnt == 0){
                    employeeCustomerDao.deleteCustomerStatus(ecInfo);
                }
                //2-删除模板账户对照表中信息
                AccountTemplate at = new AccountTemplate();
                at.setAccountNo(ecInfo.getAccountNo());
                at.setOrgCode(ecInfo.getOrgCode());
                List<AccountTemplate> atList = templateDao.findAccountTemplateListByAccountNo(at);
                if (atList.size() > 1) throw new Exception("逻辑错误：新登记未复核的账户出现多条账号与模板对照信息！");
                if (atList.size() == 0) throw new Exception("逻辑错误：新登记未复核的账户无账号与模板对照信息！");
                at = atList.get(0);
                templateDao.deleteAccountTemplateById(at);

                //删除账户特殊标记
                SpecialAccountType tempSpa = new SpecialAccountType();
                tempSpa.setAccountNo(ecInfo.getAccountNo());
                tempSpa.setOrgCode(ecInfo.getOrgCode());
                SpecialAccountType unCheckSpa = employeeCustomerDao.getRegisterUncheckSpecialAccountType(tempSpa);
                if (unCheckSpa != null && unCheckSpa.getId() != null){
                    employeeCustomerDao.deleteSpecialAccountTypeById(unCheckSpa.getId());
                }

                //3-删除分成比例信息表中信息
                templateDao.deleteAccountShareInfoByAccountTemplateId(at.getId());
            }

            //4-对于释放的客户,删除分成规则中新增的维护人记录
            if (ecInfo.getRegisterType().equals(EmployeeCustomer.REGISTER_TYPE_RELEASED_CUSTOMER) || ecInfo.getRegisterType().equals(EmployeeCustomer.REGISTER_TYPE_STATUS_RELEASED_CUSTOMER)){
                AccountShareInfo temp = new AccountShareInfo();
                temp.setAccountNo(ecInfo.getAccountNo());
                temp.setOrgCode(ecInfo.getOrgCode());
                List<AccountShareInfo> asList = employeeCustomerDao.getUncheckedAccountShareInfoByAccountNoAndOrg(temp);
                int tjr = 0; //推荐人的个数
                for (AccountShareInfo as : asList){
                    if (as.getPositionType().equals(Position.POSITION_TYPE_TUI_JIAN_REN) && as.getParentId() != null) {
                        tjr++;
                        temp = as;
                    }
                }
                if (tjr != 1) throw new Exception("未复核的推荐人个数不为1，账号: "+ecInfo.getAccountNo());
                employeeCustomerDao.deleteAccountShareInfoById(temp);

                //恢复原来推荐人的记录
                AccountShareInfo asi = new AccountShareInfo();
                asi.setId(temp.getParentId());
                if (temp.getParentId() == null) throw new Exception("未复核的分成信息中的推荐人记录无parentId,记录id: "+temp.getId());
                asi.setValidFlag(true);
                asi.setEndDate(null);
                asi.setUpdateTime(new Date());
                asi.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
                templateDao.updateAccountShareInfoById(asi);
            }
        }
    }

    /**
     * 获取可变更维护人的客户列表
     */
    @Transactional(rollbackFor = Exception.class)
    public PageData<EmployeeCustomer> findModifiableCustomer(EmployeeCustomer ec) throws Exception{
        if (ec == null || ec.getPage() == null) throw new Exception("未设置分页条件");

        ec.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/employeecustomer/getModifiableCustomer").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "o", ""));

        //设置pageNo
        ec.setPage(new Page(ec.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));

        PageData<EmployeeCustomer> employeeCustomerPageData = new PageData<>();
        Long count = employeeCustomerDao.findModifiableCustomerCount(ec);
        List<EmployeeCustomer> employeeCustomerList = employeeCustomerDao.findModifiableCustomer(ec);


        employeeCustomerPageData.setTotal(count);
        employeeCustomerPageData.setList(employeeCustomerList);
        return employeeCustomerPageData;
    }

    /**
     * 变更单个账户的维护人
     */
    public void modifyEmployee(EmployeeCustomer ec) throws Exception{
        if (null == ec || StrUtil.isBlank(ec.getXdCustomerNo())) throw new Exception("变更维护人出错，参数不正确");
        if (StrUtil.isBlank(ec.getAccountNo())) throw new Exception("变更维护人出错，未提供贷款账号");
        if (StrUtil.isBlank(ec.getTellerCode())) throw new Exception("变更维护人出错，未提供柜员号");
        if (StrUtil.isBlank(ec.getOrgCode())) throw new Exception("变更维护人出错，未提供机构号");
        User user = userService.getUserByCode(ec.getTellerCode());
        if (null == user) {
            throw new Exception("变更维护人出错, 编号为 [ " + ec.getTellerCode() + " ] 的员工信息不存在，请输入正确的柜员号！");
        }
        String currentOrganizationCode = employeeCustomerDao.getUserCurrentOrganizationCodeByUserCode(ec.getTellerCode());
        if (StrUtil.isBlank(currentOrganizationCode)) throw new Exception("变更维护人出错，无法通过员工编号"+ec.getTellerCode()+"获取到当前在职机构");

        EmployeeCustomer dbEmployeeCustomer = employeeCustomerDao.getAccountInfoByAccountNo(ec);
        if(null == dbEmployeeCustomer) throw new Exception("变更维护人出错，后台数据中无此账号信息，账号: "+ec.getAccountNo());

        if (dbEmployeeCustomer.getTellerCode().equals(ec.getTellerCode())) throw new Exception("变更维护人出错，相同的维护人无需变更");
        if (!dbEmployeeCustomer.getOrgCode().equals(currentOrganizationCode) && dbEmployeeCustomer.getStatus().equals(EmployeeCustomer.STATUS_FIXED_CUSTOMER)) throw new Exception("变更维护人出错，固定客户不允许跨机构绑定");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date_cur_date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,-24);
        Date date_yesterday = calendar.getTime();
        String dbDate = sdf.format(dbEmployeeCustomer.getStartDate());
        String currentDate = sdf.format(date_cur_date);
        if (dbDate.equals(currentDate)) throw new Exception("变更维护人出错，当日已变更，无需再次变更");

        //如果账号不存在利息分成规则，则不允许做变更
        int accountTemplateCnt = employeeCustomerDao.getValidAccountTemplateByAccountNoAndOrgCode(ec);
        if (accountTemplateCnt == 0) throw new Exception("该笔贷款不存在分成信息，账号: "+ec.getAccountNo());

        //1-设置原始记录的valid_flag=false
        EmployeeCustomer temp1 = new EmployeeCustomer();
        temp1.setId(dbEmployeeCustomer.getId());
        temp1.setValidFlag(false);
        temp1.setEndDate(date_yesterday);
        employeeCustomerDao.updateById(temp1);

        //2-在t_dktj_employee_customer表中插入新记录
        EmployeeCustomer temp2 = new EmployeeCustomer();
        temp2.setXdCustomerNo(ec.getXdCustomerNo());
        temp2.setHxCustomerNo(dbEmployeeCustomer.getHxCustomerNo());
        temp2.setAccountNo(dbEmployeeCustomer.getAccountNo());
        temp2.setAccountOpenDate(dbEmployeeCustomer.getAccountOpenDate());
        temp2.setCustomerName(dbEmployeeCustomer.getCustomerName());
        temp2.setCustomerType(dbEmployeeCustomer.getCustomerType());
        temp2.setIdentityType(dbEmployeeCustomer.getIdentityType());
        temp2.setIdentityNo(dbEmployeeCustomer.getIdentityNo());
        temp2.setOrgCode(dbEmployeeCustomer.getOrgCode());
        temp2.setTellerCode(ec.getTellerCode());
        temp2.setStartDate(date_cur_date);
        temp2.setValidFlag(false);
        temp2.setRegisterType(EmployeeCustomer.REGISTER_TYPE_CHANGED_CUSTOMER);
        temp2.setAlterCheckStatus(EmployeeCustomer.CHECKED_STATUS_UNCHECKED);
        temp2.setOpTellerCode(userUtils.getCurrentLoginedUser().getCode());
        temp2.setCreateTime(new Date());
        temp2.setParentId(dbEmployeeCustomer.getId());
        temp2.setBoundType(dbEmployeeCustomer.getBoundType());
        employeeCustomerDao.insertEmployeeCustomer(temp2);

        //3-设置原始账户分成表中的valid_flag=false
          //3.1-获取账户模板对应信息（最新的账户模板对应信息）
        AccountTemplate temp5 = new AccountTemplate();
        temp5.setAccountNo(dbEmployeeCustomer.getAccountNo());
        temp5.setOrgCode(dbEmployeeCustomer.getOrgCode());
        AccountTemplate at = templateDao.findAccountCurrentTemplateByAccountNo(temp5);
        if (at == null) throw new Exception("无法通过贷款账号获取到最新的账号模板对应信息");
          //3.2-判断该账号的分成规则中有无推荐人，如果有推荐人则将valid_flag设置为false
        AccountShareInfo tmpAsi = new AccountShareInfo();
        tmpAsi.setAccountTemplateId(at.getId());
        AccountShareInfo asi = templateDao.findAccountShareInfoByAccountTemplateId(tmpAsi);
        if (asi != null && asi.getId() != null){
            AccountShareInfo temp3 = new AccountShareInfo();
            temp3.setValidFlag(false);
            temp3.setEndDate(date_yesterday);
            temp3.setId(asi.getId());
            temp3.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
            temp3.setUpdateTime(new Date());
            templateDao.updateAccountShareInfoById(temp3);
            //3.3-在账户分成表中的新增一条推荐人的信息
            AccountShareInfo temp4 = new AccountShareInfo();
            temp4.setAccountTemplateId(asi.getAccountTemplateId());
            temp4.setTemplateDetailId(asi.getTemplateDetailId());
            temp4.setTellerCode(ec.getTellerCode());
            temp4.setStartDate(date_cur_date);
            temp4.setParentId(asi.getId());
            temp4.setValidFlag(false);
            temp4.setCheckStatus(AccountShareInfo.CHECK_STATUS_UNCHECKED);
            temp4.setRemarks("推荐人变更: "+asi.getTellerCode()+" -> "+ec.getTellerCode());
            temp4.setCreateTime(new Date());
            temp4.setCreateBy(userUtils.getCurrentLoginedUser().getId());
            templateDao.insertAccountShareInfo(temp4);
        }

    }

    /**
     * 提交变更客户维护人申请
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyEmployee(List<EmployeeCustomer> ecList) throws Exception{
        for (EmployeeCustomer ec:ecList){
            this.modifyEmployee(ec);
        }
    }

    /**
     * 获取提交变更申请，但是未复核的客户列表
     */
    @Transactional(rollbackFor = Exception.class)
    public PageData<EmployeeCustomer> findModifiedUncheckedCustomer(EmployeeCustomer ec) throws Exception{
        if (ec == null || ec.getPage() == null) throw new Exception("未设置分页条件");

        ec.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/employeecustomer/getModifiedUncheckedCustomer").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "o", ""));

        //设置pageNo
        ec.setPage(new Page(ec.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));

        PageData<EmployeeCustomer> employeeCustomerPageData = new PageData<>();
        Long count = employeeCustomerDao.findModifiedUncheckedCustomerCount(ec);
        List<EmployeeCustomer> employeeCustomerList = employeeCustomerDao.findModifiedUncheckedCustomer(ec);


        employeeCustomerPageData.setTotal(count);
        employeeCustomerPageData.setList(employeeCustomerList);
        return employeeCustomerPageData;
    }

    /**
     * 同意变更维护人
     */
    public void checkModifyEmployee(EmployeeCustomer ec) throws Exception{
        if (null == ec || null == ec.getId() || StrUtil.isBlank(ec.getAccountNo())) throw new Exception("同意变更维护人出错，错误的请求参数");
        if (StrUtil.isBlank(ec.getOrgCode())) throw new Exception("同意变更维护人出错，未提供机构号");

        EmployeeCustomer dbEc = employeeCustomerDao.getEmployeeCustomerById(ec.getId());
        if (null == dbEc.getParentId() || !dbEc.getAlterCheckStatus().equals(EmployeeCustomer.CHECKED_STATUS_UNCHECKED)) throw new Exception("同意变更维护人错误，"+ec.getAccountNo()+"账号未提交过变更申请");

        //更新复核状态
        EmployeeCustomer temp2 = new EmployeeCustomer();
        temp2.setId(dbEc.getId());
        temp2.setAlterCheckStatus(EmployeeCustomer.CHECKED_STATUS_CHECKED);
        temp2.setValidFlag(true);
        temp2.setAlterCheckTime(new Date());
        temp2.setAlterCheckTellerCode(userUtils.getCurrentLoginedUser().getCode());
        employeeCustomerDao.updateById(temp2);

        //更新分成规则valid_flag状态为true
        AccountTemplate temp3 = new AccountTemplate();
        temp3.setAccountNo(dbEc.getAccountNo());
        temp3.setOrgCode(dbEc.getOrgCode());
        AccountTemplate at = templateDao.findAccountCurrentTemplateByAccountNo(temp3);
        AccountShareInfo tmpAsi = new AccountShareInfo();
        tmpAsi.setAccountTemplateId(at.getId());
        AccountShareInfo asi = templateDao.findUncheckedAccountShareInfoByAccountTemplateId(tmpAsi);
        if (null != asi){ //如果模板中有推荐人，则复核通过
            AccountShareInfo temp1 = new AccountShareInfo();
            temp1.setId(asi.getId());
            temp1.setValidFlag(true);
            temp1.setCheckStatus(AccountShareInfo.CHECK_STATUS_CHECKED);
            temp1.setUpdateTime(new Date());
            temp1.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
            templateDao.updateAccountShareInfoById(temp1);
        }

    }

    /**
     * 同意变更信贷客户维护人
     */
    @Transactional(rollbackFor = Exception.class)
    public void checkModifyEmployee(List<EmployeeCustomer> ecList) throws Exception{
        for (EmployeeCustomer ec : ecList){
            this.checkModifyEmployee(ec);
        }
    }

    /**
     * 拒绝变更维护人
     */
    public void undoModifyEmployee(EmployeeCustomer ec) throws Exception{
        if (null == ec || ec.getId() == null) throw new Exception("复核变更维护人出错，参数错误");

        EmployeeCustomer dbEc = employeeCustomerDao.getEmployeeCustomerById(ec.getId());
        if (null == dbEc.getParentId()) throw new Exception("拒绝变更维护人错误，"+ec.getXdCustomerNo()+"客户未提交过变更申请");

        EmployeeCustomer dbParentEc = employeeCustomerDao.getEmployeeCustomerById(dbEc.getParentId());

        if (null == dbParentEc || dbParentEc.getId() == null) throw new Exception("拒绝变更维护人错误，无法通过id获取到客户维护人信息");

        //1-将原记录的valid_flag重新设置为true
        EmployeeCustomer temp1 = new EmployeeCustomer();
        temp1.setId(dbParentEc.getId());
        temp1.setValidFlag(true);
        temp1.setEndDate(null);
        employeeCustomerDao.updateById(temp1);

        //2-删除新记录
        employeeCustomerDao.deleteById(dbEc.getId());

        //3-恢复分成规则中原记录
        AccountTemplate temp3 = new AccountTemplate();
        temp3.setAccountNo(dbEc.getAccountNo());
        temp3.setOrgCode(dbEc.getOrgCode());
        AccountTemplate at = templateDao.findAccountCurrentTemplateByAccountNo(temp3);
        AccountShareInfo tmpAsi = new AccountShareInfo();
        tmpAsi.setAccountTemplateId(at.getId());
        AccountShareInfo asi = templateDao.findUncheckedAccountShareInfoByAccountTemplateId(tmpAsi);
        AccountShareInfo temp2 = new AccountShareInfo();
        temp2.setId(asi.getParentId());
        temp2.setEndDate(null);
        temp2.setValidFlag(true);
        temp2.setUpdateTime(new Date());
        temp2.setUpdateBy(userUtils.getCurrentLoginedUser().getId());
        templateDao.updateAccountShareInfoById(temp2);

        //4-删除新增的分成规则记录
        templateDao.deleteAccountShareInfoById(asi.getId());

    }

    /**
     * 拒绝变更维护人
     */
    @Transactional(rollbackFor = Exception.class)
    public void undoModifyEmployee(List<EmployeeCustomer> ecList)throws Exception{
        for (EmployeeCustomer ec : ecList){
            this.undoModifyEmployee(ec);
        }
    }

    /**
     * 固定客户转流动客户
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeStatusToFluid(CustomerStatus cs) throws Exception{
        if (StrUtil.isBlank(cs.getXdCustomerNo())) throw new Exception("变更客户状态出错：未提供正确的信贷客户号");
        if (StrUtil.isBlank(cs.getStatus())) throw new Exception("变更客户状态出错：未提供新的客户状态");
        if (StrUtil.isBlank(cs.getOrgCode())) throw new Exception("变更客户状态出错：未提供客户所在机构号");

        CustomerStatus dbCoas = employeeCustomerDao.getValidCustomerStatusByXdCustomerNoAndOrg(cs);
        if (dbCoas == null||StrUtil.isBlank(dbCoas.getStatus())) throw new Exception("变更客户状态出错：无法通过信贷客户号和机构号获取到当前生效的客户状态");
        if (dbCoas.getStatus().equals(cs.getStatus())) throw new Exception("客户状态未改变，无需变更");

        if (dbCoas.getStatus().equals(CustomerStatus.STATUS_FLUID_CUSTOMER)) throw new Exception("客户当前就为流动状态，无需变更");

        Date startDate = new Date();
        Date endDate = getBeforeDay(startDate);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDateSdf = sdf.parse(sdf.format(startDate));
        if (dbCoas.getStartDate().equals(startDateSdf)) throw new Exception("当日已发生过客户状态变更，无法再次变更");

        //1-更新原记录的end_date和valid_flag=false
        CustomerStatus coas = new CustomerStatus();
        coas.setId(dbCoas.getId());
        coas.setEndDate(endDate);
        coas.setValidFlag(false);
        employeeCustomerDao.updateValidCustomerOrgAndStatusById(coas);

        //2-新增新记录
        coas.setEndDate(null);
        coas.setStartDate(startDate);
        coas.setValidFlag(true);
        coas.setStatus(CustomerStatus.STATUS_FLUID_CUSTOMER);
        coas.setOrgCode(dbCoas.getOrgCode());
        coas.setCheckStatus(CustomerStatus.CHECK_STATUS_CHECKED);
        coas.setParentId(dbCoas.getId());
        coas.setCreateBy(userUtils.getCurrentLoginedUser().getId());
        coas.setXdCustomerNo(dbCoas.getXdCustomerNo());
        coas.setCreateTime(new Date());
        employeeCustomerDao.insertCustomerOrgAndStatus(coas);
    }

    /**
     * 变更客户为固定状态
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeStatusToFix(CustomerStatus cs) throws Exception{
        if (StrUtil.isBlank(cs.getXdCustomerNo())) throw new Exception("变更客户状态出错：未提供正确的信贷客户号");
        if (StrUtil.isBlank(cs.getStatus())) throw new Exception("变更客户状态出错：未提供新的客户状态");
        if (StrUtil.isBlank(cs.getOrgCode())) throw new Exception("变更客户状态出错：未提供客户所在机构号");

        CustomerStatus dbCoas = employeeCustomerDao.getValidCustomerStatusByXdCustomerNoAndOrg(cs);
        if (dbCoas == null||StrUtil.isBlank(dbCoas.getStatus())) throw new Exception("变更客户状态出错：无法通过信贷客户号和机构号获取到当前生效的客户状态");
        if (dbCoas.getStatus().equals(cs.getStatus())) throw new Exception("客户状态未改变，无需变更");

        if (dbCoas.getStatus().equals(CustomerStatus.STATUS_FIXED_CUSTOMER)) throw new Exception("客户当前就为固定状态，无需变更");

        Date startDate = new Date();
        Date endDate = getBeforeDay(startDate);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDateSdf = sdf.parse(sdf.format(startDate));
        if (dbCoas.getStartDate().equals(startDateSdf)) throw new Exception("当日已发生过客户状态变更，无法再次变更");

        EmployeeCustomer ec = new EmployeeCustomer();
        ec.setOrgCode(dbCoas.getOrgCode());
        ec.setXdCustomerNo(dbCoas.getXdCustomerNo());
        employeeCustomerDao.changeStatusToFix(ec);

    }

    /**
     * 获取员工所属的贷款客户列表
     */
    public PageData<EmployeeCustomer> findPage(EmployeeCustomer ec) {
        if (null == ec || ec.getPage() == null) throw new RuntimeException("缺少分页信息");

        ec.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/employeecustomer/getModifiableCustomer").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "o", "su"));

        //设置pageNo
        ec.setPage(new Page(ec.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));

        PageData<EmployeeCustomer> employeeCustomerPageData = new PageData<>();
        Long count = employeeCustomerDao.findCount(ec);
        List<EmployeeCustomer> employeeCustomerList = employeeCustomerDao.findList(ec);


        employeeCustomerPageData.setTotal(count);
        employeeCustomerPageData.setList(employeeCustomerList);
        return employeeCustomerPageData;
    }

    public EmployeeCustomer get(Long id){
        return employeeCustomerDao.getEmployeeCustomerById(id);
    }

    /**
     * 获取指定日期的上一日
     */
    private Date getBeforeDay(Date today){
        Date startDate = today;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date endDate = calendar.getTime();
        return endDate;
    }

    /**
     * 分页获取客户的固定流动状态
     * @param cs
     */
    public PageData<CustomerStatus> findOrgEmployeeCustomer(CustomerStatus cs) throws Exception{
        if (cs == null ||cs.getPage() == null) throw new Exception("未设置分页条件");

        cs.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/employeecustomer/getEmployeeCustomer").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "o", ""));

        //设置pageNo
        cs.setPage(new Page(cs.getPageNo(), DefaultConfig.DEFAULT_PAGE_SIZE));

        PageData<CustomerStatus> customerStatusPageData = new PageData<>();
        Long count = employeeCustomerDao.findOrgEmployeeCustomerCount(cs);
        List<CustomerStatus> customerStatusList = employeeCustomerDao.findOrgEmployeeCustomer(cs);


        customerStatusPageData.setTotal(count);
        customerStatusPageData.setList(customerStatusList);
        return customerStatusPageData;
    }

    /**
     * 通过给定的姓名或柜员号查找柜员，最多匹配10个柜员
     */
    public List<UserInfoForBind> findUserList(UserOrganization uo) throws Exception{
        String userCodeOrName = null;
        if (uo != null) userCodeOrName = uo.getUserCodeOrName();

        return employeeCustomerDao.findUserList(userCodeOrName);
    }

    public EmployeeCustomer getLoanCurrentDate(EmployeeCustomer employeeCustomer) {
        employeeCustomer.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/employeecustomer/getLoanCurrentDate").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "o", ""));
        return employeeCustomerDao.getLoanHandleCurrentDate(employeeCustomer);
    }

    public List<SpecialAccountType> getSpecialAccountTypeList(){
        return employeeCustomerDao.getSpecialAccountTypeList();
    }


}
