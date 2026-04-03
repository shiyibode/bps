package org.nmgns.bps.dktj.service;

import cn.hutool.core.util.StrUtil;
import org.nmgns.bps.cktj.entity.Deposit;
import org.nmgns.bps.dktj.dao.LoanDao;
import org.nmgns.bps.dktj.entity.Loan;
import org.nmgns.bps.dktj.utils.Span;
import org.nmgns.bps.system.dao.OrganizationDao;
import org.nmgns.bps.system.entity.Organization;
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
public class LoanSerivce {

    @Autowired
    private LoanDao loanDao;
    @Autowired
    private ApiService apiService;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private OrganizationDao organizationDao;

    public List<Loan> findOrganizationLoanList(Loan loan) throws Exception{
        if (loan == null || StrUtil.isBlank(loan.getLoanType())) throw new Exception("获取机构时点贷款失败：未提供loanType");
        if (loan.getOrganizationId() == null) throw new Exception("获取机构时点贷款失败：未提供网点信息");

        //网点的层级不得超过一级支行
        Organization org = organizationDao.getOrganizationById(loan.getOrganizationId());
        if (org == null) throw new RuntimeException("网点不存在");
        if (org.getGrade() < 3) throw new RuntimeException("请选择一级支行及以下的机构");

        loan.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/loan/organization").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "lno,blo", ""));

        return loanDao.findOrganizationLoanList(loan);
    }

    public List<Loan> findOrganizationAvgLoanList(Loan loan) throws Exception{
        if (loan == null || StrUtil.isBlank(loan.getLoanType())) throw new Exception("获取机构日均贷款失败：未提供loanType");
        if (loan.getOrganizationId() == null) throw new Exception("获取机构日均贷款失败：未提供网点信息");
        if (loan.getStartDate() == null || loan.getEndDate() == null) throw new Exception("获取机构日均贷款失败：未提供起止时间");
        Span span = new Span(loan.getStartDate(),loan.getEndDate());
        loan.setAvgDays(span.getDateSpace());

        loan.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/loan/orgAverage").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "lno,blo", ""));

        return loanDao.findOrganizationAvgLoanList(loan);
    }


    /**
     *  获取员工贷款任务时点
     * @param loan 参数
     */
    public PageData<Loan> findEmployeeLoanList(Loan loan) throws Exception{
        PageData<Loan> loanPageData = new PageData<>();
        if (loan == null || StrUtil.isBlank(loan.getLoanType())) throw new Exception("获取员工时点贷款失败：未提供loanType");
        if (loan.getOrganizationId() == null) throw new Exception("获取员工时点贷款失败：未提供网点信息");

        //网点的层级不得超过一级支行
        Organization org = organizationDao.getOrganizationById(loan.getOrganizationId());
        if (org == null) throw new RuntimeException("网点不存在");
        if (org.getGrade() < 3) throw new RuntimeException("请选择一级支行及以下的机构");

        if (loan.getLoanType().equals(Loan.EMP_LOAN_TYPE_HE_XIN)){
            loan.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/loan/employee").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "lno", "u"));
        }else {
            loan.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/loan/employee").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "lno,blo", "u"));
        }

        //分页获取员工列表以及员工总数
        loan.getPage().setPageSize(DefaultConfig.DEFAULT_PAGE_SIZE);
        List<String> tellerList = loanDao.findTaskEmployeePage(loan);
        Long total = loanDao.findTaskEmployeePageCount(loan);

        //获取员工对应的分成信息列表
        List<Loan> tellerLoanList = new ArrayList<>();
        for (String tellerCode : tellerList){
            loan.setTellerCode(tellerCode);
            tellerLoanList.addAll(loanDao.findEmployeeLoanList(loan));
        }


        loanPageData.setTotal(total);
        loanPageData.setList(tellerLoanList);

        return loanPageData;
    }

    /**
     *  获取员工贷款任务日均
     * @param loan 参数
     */
    public PageData<Loan> findEmployeeAvgLoanList(Loan loan) throws Exception{
        PageData<Loan> loanPageData = new PageData<>();

        if (loan == null || StrUtil.isBlank(loan.getLoanType())) throw new Exception("获取员工日均贷款失败：未提供loanType");
        if (loan.getOrganizationId() == null) throw new Exception("获取员工日均贷款失败：未提供网点信息");
        if (loan.getStartDate() == null || loan.getEndDate() == null) throw new Exception("获取员工日均贷款失败：未提供起止时间");
        Span span = new Span(loan.getStartDate(),loan.getEndDate());
        loan.setAvgDays(span.getDateSpace());

        //网点的层级不得超过一级支行
        Organization org = organizationDao.getOrganizationById(loan.getOrganizationId());
        if (org == null) throw new RuntimeException("网点不存在");
        if (org.getGrade() < 3) throw new RuntimeException("请选择一级支行及以下的机构");

        if (loan.getLoanType().equals(Loan.EMP_LOAN_TYPE_HE_XIN)){
            loan.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/loan/empAverage").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "lno", "u"));
        }else {
            loan.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(apiService.getApiByUri("/dktj/loan/empAverage").getId(), userUtils.getCurrentLoginedUserIncludeRole(), "lno,blo", "u"));
        }

        //分页获取员工列表以及员工总数
        loan.getPage().setPageSize(DefaultConfig.DEFAULT_PAGE_SIZE);
        List<String> tellerList = loanDao.findTaskEmployeeAvgPage(loan);
        Long total = loanDao.findTaskEmployeeAvgPageCount(loan);

        //获取员工对应的分成信息列表
        List<Loan> tellerLoanList = new ArrayList<>();
        for (String tellerCode : tellerList){
            loan.setTellerCode(tellerCode);
            tellerLoanList.addAll(loanDao.findEmployeeAvgLoanList(loan));
        }

        loanPageData.setTotal(total);
        loanPageData.setList(tellerLoanList);

        return loanPageData;
    }

}
