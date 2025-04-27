package org.nmgns.bps.cktj.service;

import cn.hutool.core.util.StrUtil;
import org.nmgns.bps.cktj.dao.DepositDao;
import org.nmgns.bps.cktj.entity.Deposit;
import org.nmgns.bps.cktj.entity.EmployeeAccount;
import org.nmgns.bps.system.dao.UserDao;
import org.nmgns.bps.system.entity.User;
import org.nmgns.bps.system.entity.UserOrganization;
import org.nmgns.bps.system.service.ApiService;
import org.nmgns.bps.system.utils.DataScopeUtils;
import org.nmgns.bps.system.utils.DefaultConfig;
import org.nmgns.bps.system.utils.PageData;
import org.nmgns.bps.system.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepositService {

    @Autowired
    private DepositDao depositDao;
    @Autowired
    private ApiService apiService;
    @Autowired
    private UserUtils userUtils;

    /**
     * 员工时点
     */
    public PageData<Deposit> findEmployeeDepositList(Deposit deposit) {
        if (deposit == null || deposit.getPage() == null ) throw new RuntimeException("未提供参数");
        PageData<Deposit> depositPageData = new PageData<>();

        //设置默认机构为当前用户的在职机构
        if(deposit.getOrganizationId() == null){
            UserOrganization currentUserOrganization = userUtils.getCurrentUserOrganization();
            deposit.setOrganizationId(currentUserOrganization.getId());
        }

        // 设置默认的存款类型为“核心系统存款”
        if (StrUtil.isBlank(deposit.getDepositType())) {
            deposit.setDepositType("DEPOSIT_TYPE_ACCOUNTING_WAY");
        }

        // 如果前端未设置时间，则设置默认日期
        if (deposit.getDate() == null ){
            deposit.setStartDate(userUtils.getMaxDepositCurrDate());
            deposit.setEndDate(deposit.getStartDate());
        }

        //分页获取员工列表以及员工总数
        deposit.getPage().setPageSize(DefaultConfig.DEFAULT_PAGE_SIZE);
        List<String> tellerList = depositDao.findTaskEmployeePage(deposit);
        Long total = depositDao.findTaskEmployeePageCount(deposit);

        if (deposit.getDepositType().equals("DEPOSIT_TYPE_ACCOUNTING_WAY")) {
            deposit.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/deposit/employeetask").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "dpo", "u"));     //核心系统存款
        } else {
            deposit.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/deposit/employeetask").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "dpo,blo", "u")); //汇总或调离或调入存款
        }

        //获取员工对应的分成信息列表
        List<Deposit> tellerDepositList = new ArrayList<>();
        for (String tellerCode : tellerList){
            deposit.setTellerCode(tellerCode);
             tellerDepositList.addAll(depositDao.findEmployeeDepositList(deposit));
        }

        depositPageData.setTotal(total);
        depositPageData.setList(tellerDepositList);

        return depositPageData;
    }




}
