package org.nmgns.bps.cktj.controller;

import org.nmgns.bps.cktj.entity.EmployeeAccount;
import org.nmgns.bps.cktj.entity.TellerPercentage;
import org.nmgns.bps.cktj.service.EmployeeAccountService;
import org.nmgns.bps.system.utils.PageData;
import org.nmgns.bps.system.utils.base.ResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cktj/employeeaccount")
public class EmployeeAccountController {

    @Autowired
    private EmployeeAccountService employeeAccountService;

    /**
     * 获取未登记揽储人的账户列表
     * @param employeeAccount 分页信息、搜索信息
     * @return
     */
    @PreAuthorize("hasAuthority('cktj:employeeaccount:getunregisteraccount')")
    @RequestMapping(value = "/getunregisteraccount")
    public ResponseJson getUnregisterAccount(@RequestBody EmployeeAccount employeeAccount) {
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<EmployeeAccount> employeeAccountPageData = employeeAccountService.findUnregisterAccountPage(employeeAccount);
            responseJson.setSuccess(true);
            responseJson.setData(employeeAccountPageData.getList());
            responseJson.setTotal(employeeAccountPageData.getTotal());
            responseJson.setMsg("获取数据成功");
        }catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    /**
     * 登记揽储人申请
     * @param employeeAccount 登记的信息，包含任务分成、计酬分成信息
     * @return
     */
    @PreAuthorize("hasAuthority('cktj:employeeaccount:registeremployee')")
    @RequestMapping(value = "/registeremployee")
    public ResponseJson registerEmployee(@RequestBody EmployeeAccount employeeAccount) {
        ResponseJson responseJson = new ResponseJson();

        try {
            employeeAccountService.registerEmployee(employeeAccount);
            responseJson.setSuccess(Boolean.TRUE);
            responseJson.setMsg("登记申请已提交，请复核");
        }catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }



    /**
     * 获取已登记揽储人但未复核的账户列表
     * @param employeeAccount 分页信息、搜索信息
     */
    @PreAuthorize("hasAuthority('cktj:employeeaccount:getregisteruncheckedaccount')")
    @RequestMapping(value = "/getregisteruncheckedaccount")
    public ResponseJson getRegisterUncheckedAccount(@RequestBody EmployeeAccount employeeAccount) {
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<EmployeeAccount> employeeAccountPageData = employeeAccountService.findRegisterUncheckedAccountPage(employeeAccount);
            responseJson.setSuccess(true);
            responseJson.setData(employeeAccountPageData.getList());
            responseJson.setTotal(employeeAccountPageData.getTotal());
            responseJson.setMsg("获取数据成功");
        }catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    /**
     * 复核登记揽储人申请
     * @param employeeAccount 包含账号、子账号信息
     */
    @RequestMapping(value = "/checkregisteremployee")
    @PreAuthorize("hasAuthority('cktj:employeeaccount:checkregisteremployee')")
    public ResponseJson checkRegisterEmployee(@RequestBody EmployeeAccount employeeAccount) {
        ResponseJson responseJson = new ResponseJson();

        try {
            employeeAccountService.checkRegisterEmployee(employeeAccount);
            responseJson.setSuccess(true);
            responseJson.setMsg("账户登记已复核");
        }catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    /**
     * 撤销登记揽储人申请
     * @param employeeAccount 包含accountNo、childAccountNo信息
     * @return
     */
    @RequestMapping(value = "/undoregisteremployee")
    @PreAuthorize("hasAuthority('cktj:employeeaccount:undoregisteremployee')")
    public ResponseJson undoRegisterEmployee(@RequestBody EmployeeAccount employeeAccount) {
        ResponseJson responseJson = new ResponseJson();

        try {
            employeeAccountService.undoRegisterEmployee(employeeAccount);
            responseJson.setSuccess(true);
            responseJson.setMsg("账户登记已撤销");
        }catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    /**
     * 获取任务数可变更揽储人的账户信息列表
     * @param tellerPercentage 包含分页信息、搜索信息
     */
    @RequestMapping(value = "/gettaskmodifiableaccount")
    @PreAuthorize("hasAuthority('cktj:employeeaccount:gettaskmodifiableaccount')")
    public ResponseJson getTaskModifiableAccount(@RequestBody TellerPercentage tellerPercentage) {
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<TellerPercentage> tellerPercentagePageData = employeeAccountService.findTaskModifiableTellerPercentagePage(tellerPercentage);
            responseJson.setSuccess(true);
            responseJson.setData(tellerPercentagePageData.getList());
            responseJson.setTotal(tellerPercentagePageData.getTotal());
            responseJson.setMsg("获取任务数可变更揽储人列表成功");
        }catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    /**
     * 获取计酬数可变更揽储人的账户信息列表
     * @param tellerPercentage 包含分页信息、搜索信息
     */
    @RequestMapping(value = "/getpaymentmodifiableaccount")
    @PreAuthorize("hasAuthority('cktj:employeeaccount:getpaymentmodifiableaccount')")
    public ResponseJson getPaymentModifiableAccount(@RequestBody TellerPercentage tellerPercentage) {
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<TellerPercentage> tellerPercentagePageData = employeeAccountService.findPaymentModifiableTellerPercentagePage(tellerPercentage);
            responseJson.setSuccess(true);
            responseJson.setData(tellerPercentagePageData.getList());
            responseJson.setTotal(tellerPercentagePageData.getTotal());
            responseJson.setMsg("获取计酬数可变更揽储人列表成功");
        }catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }





}
