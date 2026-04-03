package org.nmgns.bps.dktj.controller;

import org.nmgns.bps.cktj.entity.Deposit;
import org.nmgns.bps.dktj.entity.Loan;
import org.nmgns.bps.system.utils.PageData;
import org.nmgns.bps.system.utils.base.ResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.nmgns.bps.dktj.service.LoanSerivce;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dktj/loan")
public class LoanController {

    @Autowired
    private LoanSerivce loanSerivce;

    @RequestMapping("/organization")
    @PreAuthorize("hasAuthority('dktj:loan:organization')")
    public ResponseJson organizationLoan(@RequestBody Loan loan){
        ResponseJson responseJson = new ResponseJson();

        List<Loan>  result;
        try {
            result = loanSerivce.findOrganizationLoanList(loan);
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setData(result);
        responseJson.setMsg("获取机构时点贷款成功");
        responseJson.setTotal((long)result.size());
        return responseJson;
    }

    @RequestMapping("/orgAverage")
    @PreAuthorize("hasAuthority('dktj:loan:orgaverage')")
    public @ResponseBody Object organizationAvgLoan(@RequestBody Loan loan){
        ResponseJson responseJson = new ResponseJson();

        List<Loan>  result;
        try {
            result = loanSerivce.findOrganizationAvgLoanList(loan);
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setData(result);
        responseJson.setMsg("获取机构日均贷款成功");
        responseJson.setTotal((long)result.size());
        return responseJson;
    }


    @RequestMapping("/employee")
    @PreAuthorize("hasAuthority('dktj:loan:employee')")
    public @ResponseBody Object employeeLoan(@RequestBody Loan loan){
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<Loan> loanPageData = loanSerivce.findEmployeeLoanList(loan);
            responseJson.setSuccess(true);
            responseJson.setData(loanPageData.getList());
            responseJson.setTotal(loanPageData.getTotal());
            responseJson.setMsg("获取员工的贷款任务完成情况成功");
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @RequestMapping("/empAverage")
    @PreAuthorize("hasAuthority('dktj:loan:empaverage')")
    public @ResponseBody Object employeeAvgLoan(@RequestBody Loan loan){
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<Loan> loanPageData = loanSerivce.findEmployeeAvgLoanList(loan);
            responseJson.setSuccess(true);
            responseJson.setData(loanPageData.getList());
            responseJson.setTotal(loanPageData.getTotal());
            responseJson.setMsg("获取员工的贷款任务完成情况成功");
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }


}
