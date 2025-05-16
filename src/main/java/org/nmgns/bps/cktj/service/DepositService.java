package org.nmgns.bps.cktj.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.nmgns.bps.cktj.dao.DepositDao;
import org.nmgns.bps.cktj.entity.Deposit;
import org.nmgns.bps.cktj.utils.ExcelExportUtils;
import org.nmgns.bps.system.entity.UserOrganization;
import org.nmgns.bps.system.service.ApiService;
import org.nmgns.bps.system.utils.DataScopeUtils;
import org.nmgns.bps.system.utils.DefaultConfig;
import org.nmgns.bps.system.utils.PageData;
import org.nmgns.bps.system.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DepositService {

    @Autowired
    private DepositDao depositDao;
    @Autowired
    private ApiService apiService;
    @Autowired
    private UserUtils userUtils;

    /**
     * 员工任务时点
     */
    public PageData<Deposit> findEmployeeTaskDepositList(Deposit deposit) {
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
        if (deposit.getStartDate() == null && deposit.getEndDate() == null){
            deposit.setStartDate(userUtils.getMaxDepositCurrDate());
            deposit.setEndDate(deposit.getStartDate());
        }
        if (deposit.getStartDate() == null && deposit.getEndDate() != null) deposit.setStartDate(deposit.getEndDate());
        if (deposit.getEndDate() == null && deposit.getStartDate() != null) deposit.setEndDate(deposit.getStartDate());
        if (deposit.getStartDate().after(deposit.getEndDate())) throw new RuntimeException("起始日期大于终止日期");
        long days = DateUtil.between(deposit.getStartDate(), deposit.getEndDate(), DateUnit.DAY) + 1;
        if (days > 31) throw new RuntimeException("最长的跨度区间不得超过31天");


        if (deposit.getDepositType().equals("DEPOSIT_TYPE_ACCOUNTING_WAY")) {
            deposit.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/deposit/employeetask").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "dpo", "u"));     //核心系统存款
        } else {
            deposit.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/deposit/employeetask").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "dpo,blo", "u")); //汇总或调离或调入存款
        }

        //分页获取员工列表以及员工总数
        deposit.getPage().setPageSize(DefaultConfig.DEFAULT_PAGE_SIZE);
        List<String> tellerList = depositDao.findTaskEmployeePage(deposit);
        Long total = depositDao.findTaskEmployeePageCount(deposit);

        //获取员工对应的分成信息列表
        List<Deposit> tellerDepositList = new ArrayList<>();
        for (String tellerCode : tellerList){
            deposit.setTellerCode(tellerCode);
             tellerDepositList.addAll(depositDao.findEmployeeTaskDepositList(deposit));
        }

        depositPageData.setTotal(total);
        depositPageData.setList(tellerDepositList);

        return depositPageData;
    }

    /**
     * 导出员工任务时点数
     */
    public void exportEmpDepositTask(HttpServletResponse response, Deposit deposit) throws Exception {

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
        if (deposit.getStartDate() == null && deposit.getEndDate() != null) deposit.setStartDate(deposit.getEndDate());
        if (deposit.getEndDate() == null && deposit.getStartDate() != null) deposit.setEndDate(deposit.getStartDate());
        if (deposit.getStartDate().after(deposit.getEndDate())) throw new RuntimeException("起始日期大于终止日期");
        long days = DateUtil.between(deposit.getStartDate(), deposit.getEndDate(), DateUnit.DAY) + 1;
        if (days > 31) throw new RuntimeException("最长的跨度区间不得超过31天");

        // 导出excel时，导出全部员工的任务完成数
        deposit.setPage(null);
        List<String> tellerList = depositDao.findTaskEmployeePage(deposit);

        if (deposit.getDepositType().equals("DEPOSIT_TYPE_ACCOUNTING_WAY")) {
            deposit.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/deposit/employeetask").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "dpo", "u"));     //核心系统存款
        } else {
            deposit.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/deposit/employeetask").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "dpo,blo", "u")); //汇总或调离或调入存款
        }

        //获取员工对应的分成信息列表
        List<Deposit> tellerDepositList = new ArrayList<>();
        for (String tellerCode : tellerList){
            deposit.setTellerCode(tellerCode);
            tellerDepositList.addAll(depositDao.findEmployeeTaskDepositList(deposit));
        }


        List<Map<String,Object>> data = new ArrayList<>();
        for (int i=0;i<tellerDepositList.size();i++){
            Deposit d = tellerDepositList.get(i);
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("在职机构",d.getTellerOrgName());
            map.put("在职机构号",d.getTellerOrgCode());
            map.put("员工姓名",d.getTellerName());
            map.put("存款所在会计机构",d.getDpOrgCode());
            map.put("存款上级分类",d.getParentCategoryName());
            map.put("存款分类",d.getDpCategoryName());
            map.put("金额",d.getBalance());
            map.put("累计付息",d.getTtlPayInt());
            map.put("当日付息",d.getDayPayInt());

            data.add(map);
        }


        ExcelExportUtils.exportExcel(response, data, "员工存款任务时点数-"+ DateUtil.formatDate(deposit.getDate()), 9);

    }

    /**
     * 员工任务日均
     */
    public PageData<Deposit> findEmployeeTaskDepositAvgList(Deposit deposit) {
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
        if (deposit.getStartDate() == null && deposit.getEndDate() == null){
            deposit.setStartDate(userUtils.getMaxDepositCurrDate());
            deposit.setEndDate(deposit.getStartDate());
        }

        if (deposit.getStartDate() == null && deposit.getEndDate() != null) deposit.setStartDate(deposit.getEndDate());
        if (deposit.getEndDate() == null && deposit.getStartDate() != null) deposit.setEndDate(deposit.getStartDate());
        if (deposit.getStartDate().after(deposit.getEndDate())) throw new RuntimeException("起始日期大于终止日期");
        long days = DateUtil.between(deposit.getStartDate(), deposit.getEndDate(), DateUnit.DAY) + 1;
        if (days > 31) throw new RuntimeException("最长的跨度区间不得超过31天");

        //分页获取员工列表以及员工总数
        deposit.getPage().setPageSize(DefaultConfig.DEFAULT_PAGE_SIZE);
        List<String> tellerList = depositDao.findTaskEmployeePage(deposit);
        Long total = depositDao.findTaskEmployeePageCount(deposit);

        if (deposit.getDepositType().equals("DEPOSIT_TYPE_ACCOUNTING_WAY")) {
            deposit.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/deposit/empaveragetask").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "dpo", "u"));     //核心系统存款
        } else {
            deposit.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/deposit/empaveragetask").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "dpo,blo", "u")); //汇总或调离或调入存款
        }

        //获取员工对应的分成信息列表
        List<Deposit> tellerDepositList = new ArrayList<>();
        for (String tellerCode : tellerList){
            deposit.setTellerCode(tellerCode);
            tellerDepositList.addAll(depositDao.findEmployeeAvgTaskDepositList(deposit));
        }

        depositPageData.setTotal(total);
        depositPageData.setList(tellerDepositList);

        return depositPageData;
    }

    /**
     * 员工计酬时点
     */
    public PageData<Deposit> findEmployeePaymentDepositList(Deposit deposit) {
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
        if (deposit.getStartDate() == null && deposit.getEndDate() == null){
            deposit.setStartDate(userUtils.getMaxDepositCurrDate());
            deposit.setEndDate(deposit.getStartDate());
        }
        if (deposit.getStartDate() == null && deposit.getEndDate() != null) deposit.setStartDate(deposit.getEndDate());
        if (deposit.getEndDate() == null && deposit.getStartDate() != null) deposit.setEndDate(deposit.getStartDate());
        if (deposit.getStartDate().after(deposit.getEndDate())) throw new RuntimeException("起始日期大于终止日期");
        long days = DateUtil.between(deposit.getStartDate(), deposit.getEndDate(), DateUnit.DAY) + 1;
        if (days > 31) throw new RuntimeException("最长的跨度区间不得超过31天");


        if (deposit.getDepositType().equals("DEPOSIT_TYPE_ACCOUNTING_WAY")) {
            deposit.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/deposit/employeepayment").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "dpo", "u"));     //核心系统存款
        } else {
            deposit.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/deposit/employeepayment").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "dpo,blo", "u")); //汇总或调离或调入存款
        }

        //分页获取员工列表以及员工总数
        deposit.getPage().setPageSize(DefaultConfig.DEFAULT_PAGE_SIZE);
        List<String> tellerList = depositDao.findPaymentEmployeePage(deposit);
        Long total = depositDao.findPaymentEmployeePageCount(deposit);

        //获取员工对应的分成信息列表
        List<Deposit> tellerDepositList = new ArrayList<>();
        for (String tellerCode : tellerList){
            deposit.setTellerCode(tellerCode);
            tellerDepositList.addAll(depositDao.findEmployeePaymentDepositList(deposit));
        }

        depositPageData.setTotal(total);
        depositPageData.setList(tellerDepositList);

        return depositPageData;
    }

    /**
     * 员工计酬日均
     */
    public PageData<Deposit> findEmployeePaymentDepositAvgList(Deposit deposit) {
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
        if (deposit.getStartDate() == null && deposit.getEndDate() == null){
            deposit.setStartDate(userUtils.getMaxDepositCurrDate());
            deposit.setEndDate(deposit.getStartDate());
        }

        if (deposit.getStartDate() == null && deposit.getEndDate() != null) deposit.setStartDate(deposit.getEndDate());
        if (deposit.getEndDate() == null && deposit.getStartDate() != null) deposit.setEndDate(deposit.getStartDate());
        if (deposit.getStartDate().after(deposit.getEndDate())) throw new RuntimeException("起始日期大于终止日期");
        long days = DateUtil.between(deposit.getStartDate(), deposit.getEndDate(), DateUnit.DAY) + 1;
        if (days > 31) throw new RuntimeException("最长的跨度区间不得超过31天");

        //分页获取员工列表以及员工总数
        deposit.getPage().setPageSize(DefaultConfig.DEFAULT_PAGE_SIZE);
        List<String> tellerList = depositDao.findPaymentEmployeePage(deposit);
        Long total = depositDao.findPaymentEmployeePageCount(deposit);

        if (deposit.getDepositType().equals("DEPOSIT_TYPE_ACCOUNTING_WAY")) {
            deposit.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/deposit/empaveragepayment").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "dpo", "u"));     //核心系统存款
        } else {
            deposit.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/deposit/empaveragepayment").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "dpo,blo", "u")); //汇总或调离或调入存款
        }

        //获取员工对应的分成信息列表
        List<Deposit> tellerDepositList = new ArrayList<>();
        for (String tellerCode : tellerList){
            deposit.setTellerCode(tellerCode);
            tellerDepositList.addAll(depositDao.findEmployeeAvgPaymentDepositList(deposit));
        }

        depositPageData.setTotal(total);
        depositPageData.setList(tellerDepositList);

        return depositPageData;
    }

    /**
     * 机构任务时点
     */
    public List<Deposit> findOrganizationTaskDepositList(Deposit deposit) {
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
        if (deposit.getStartDate() == null && deposit.getEndDate() == null){
            deposit.setStartDate(userUtils.getMaxDepositCurrDate());
            deposit.setEndDate(deposit.getStartDate());
        }
        if (deposit.getStartDate() == null && deposit.getEndDate() != null) deposit.setStartDate(deposit.getEndDate());
        if (deposit.getEndDate() == null && deposit.getStartDate() != null) deposit.setEndDate(deposit.getStartDate());
        if (deposit.getStartDate().after(deposit.getEndDate())) throw new RuntimeException("起始日期大于终止日期");
        long days = DateUtil.between(deposit.getStartDate(), deposit.getEndDate(), DateUnit.DAY) + 1;
        if (days > 31) throw new RuntimeException("最长的跨度区间不得超过31天");


        if (deposit.getDepositType().equals("DEPOSIT_TYPE_ACCOUNTING_WAY")) {
            deposit.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/deposit/organizationtask").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "dpo", null));     //核心系统存款
        } else {
            deposit.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/deposit/organizationtask").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "dpo,blo", null)); //汇总或调离或调入存款
        }

        //获取员工对应的分成信息列表
        List<Deposit> orgDepositList = depositDao.findOrganizationTaskDepositList(deposit);

        return orgDepositList;
    }

    /**
     * 机构任务日均
     */
    public List<Deposit> findOrganizationTaskDepositAvgList(Deposit deposit) {

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
        if (deposit.getStartDate() == null && deposit.getEndDate() == null){
            deposit.setStartDate(userUtils.getMaxDepositCurrDate());
            deposit.setEndDate(deposit.getStartDate());
        }

        if (deposit.getStartDate() == null && deposit.getEndDate() != null) deposit.setStartDate(deposit.getEndDate());
        if (deposit.getEndDate() == null && deposit.getStartDate() != null) deposit.setEndDate(deposit.getStartDate());
        if (deposit.getStartDate().after(deposit.getEndDate())) throw new RuntimeException("起始日期大于终止日期");
        long days = DateUtil.between(deposit.getStartDate(), deposit.getEndDate(), DateUnit.DAY) + 1;
        if (days > 31) throw new RuntimeException("最长的跨度区间不得超过31天");


        if (deposit.getDepositType().equals("DEPOSIT_TYPE_ACCOUNTING_WAY")) {
            deposit.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/deposit/organizationtask").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "dpo", null));     //核心系统存款
        } else {
            deposit.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/cktj/deposit/organizationtask").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "dpo,blo", null)); //汇总或调离或调入存款
        }

        //获取员工对应的分成信息列表
        List<Deposit> orgAvgDepositList = depositDao.findOrganizationAvgTaskDepositList(deposit);


        return orgAvgDepositList;
    }




}
