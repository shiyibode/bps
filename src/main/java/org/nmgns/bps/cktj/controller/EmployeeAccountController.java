package org.nmgns.bps.cktj.controller;

import org.nmgns.bps.cktj.entity.EmployeeAccount;
import org.nmgns.bps.cktj.service.EmployeeAccountService;
import org.nmgns.bps.system.utils.PageData;
import org.nmgns.bps.system.utils.base.ResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param employeeAccount 包含分页信息、搜索信息
     */
    @RequestMapping(value = "/gettaskmodifiableaccount")
    @PreAuthorize("hasAuthority('cktj:employeeaccount:gettaskmodifiableaccount')")
    public ResponseJson getTaskModifiableAccount(@RequestBody EmployeeAccount employeeAccount) {
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<EmployeeAccount> employeeAccountPageData = employeeAccountService.findTaskModifiableEmployeeAcountPage(employeeAccount);
            responseJson.setSuccess(true);
            responseJson.setData(employeeAccountPageData.getList());
            responseJson.setTotal(employeeAccountPageData.getTotal());
            responseJson.setMsg("获取任务数可变更揽储人列表成功");
        }catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    /**
     * 获取计酬数可变更揽储人的账户信息列表
     * @param employeeAccount 包含分页信息、搜索信息
     */
    @RequestMapping(value = "/getpaymentmodifiableaccount")
    @PreAuthorize("hasAuthority('cktj:employeeaccount:getpaymentmodifiableaccount')")
    public ResponseJson getPaymentModifiableAccount(@RequestBody EmployeeAccount employeeAccount) {
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<EmployeeAccount> employeeAccountPageData = employeeAccountService.findPaymentModifiableTellerPercentagePage(employeeAccount);
            responseJson.setSuccess(true);
            responseJson.setData(employeeAccountPageData.getList());
            responseJson.setTotal(employeeAccountPageData.getTotal());
            responseJson.setMsg("获取计酬数可变更揽储人列表成功");
        }catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    /**
     * 变更揽储人申请-任务数
     * @param employeeAccount 包含accountNo、childAccountNo、tellerTaskPercentageList（这里包含新的用户分成信息:tellerCode、percentage）
     */
    @PreAuthorize("hasAuthority('cktj:employeeaccount:modifyemployeetask')")
    @RequestMapping(value = "/modifyemployeetask")
    public ResponseJson modifyEmployeeTask(@RequestBody EmployeeAccount employeeAccount) {
        ResponseJson responseJson = new ResponseJson();

        try {
            employeeAccountService.modifyTaskEmployee(employeeAccount);
            responseJson.setSuccess(true);
            responseJson.setMsg("变更申请已提交");
        }catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }


    /**
     * 变更揽储人申请-计酬数
     * @param employeeAccount 包含accountNo、childAccountNo、tellerPaymentPercentageList（这里包含新的用户分成信息:tellerCode、percentage）
     */
    @PreAuthorize("hasAuthority('cktj:employeeaccount:modifyemployeepayment')")
    @RequestMapping(value = "/modifyemployeepayment")
    public ResponseJson modifyEmployeePayment(@RequestBody EmployeeAccount employeeAccount) {
        ResponseJson responseJson = new ResponseJson();

        try {
            employeeAccountService.modifyPaymentEmployee(employeeAccount);
            responseJson.setSuccess(true);
            responseJson.setMsg("变更申请已提交");
        }catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }


    /**
     * 获取已变更揽储人但未复核的账户信息列表-任务数
     * @param employeeAccount 包含分页信息
     */
    @PreAuthorize("hasAuthority('cktj:employeeaccount:getmodifieduncheckedaccounttask')")
    @RequestMapping(value = "/getmodifieduncheckedaccounttask")
    public ResponseJson getModifiedUncheckedAccountTask(@RequestBody EmployeeAccount employeeAccount) {
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<EmployeeAccount> employeeAccountPageData = employeeAccountService.findModifiedUncheckedAccountTaskPage(employeeAccount);
            responseJson.setSuccess(true);
            responseJson.setData(employeeAccountPageData.getList());
            responseJson.setTotal(employeeAccountPageData.getTotal());
            responseJson.setMsg("获取变更未复核任务数列表成功");
        }catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }


    /**
     * 获取已变更揽储人但未复核的账户信息列表-计酬数
     * @param employeeAccount 包含分页信息
     */
    @PreAuthorize("hasAuthority('cktj:employeeaccount:getmodifieduncheckedaccountpayment')")
    @RequestMapping(value = "/getmodifieduncheckedaccountpayment")
    public ResponseJson getModifiedUncheckedAccountPayment(@RequestBody EmployeeAccount employeeAccount) {
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<EmployeeAccount> employeeAccountPageData = employeeAccountService.findModifiedUncheckedAccountPaymentPage(employeeAccount);
            responseJson.setSuccess(true);
            responseJson.setData(employeeAccountPageData.getList());
            responseJson.setTotal(employeeAccountPageData.getTotal());
            responseJson.setMsg("获取变更未复核计酬数列表成功");
        }catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }



    /**
     * 复核变更揽储人申请-任务数
     * @param employeeAccount 包含accountNo、childAccountNo
     * @return
     */
    @PreAuthorize("hasAuthority('cktj:employeeaccount:checkmodifyemployeetask')")
    @RequestMapping(value = "/checkmodifyemployeetask")
    public ResponseJson checkModifyEmployeeTask(@RequestBody EmployeeAccount employeeAccount) {
        ResponseJson responseJson = new ResponseJson();

        try {
            employeeAccountService.checkModifyEmployeeTask(employeeAccount);
            responseJson.setSuccess(true);
            responseJson.setMsg("任务数变更申请已通过");
        }catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    /**
     * 复核变更揽储人申请-计酬数
     * @param employeeAccount 包含accountNo、childAccountNo
     * @return
     */
    @PreAuthorize("hasAuthority('cktj:employeeaccount:checkmodifyemployeepayment')")
    @RequestMapping(value = "/checkmodifyemployeepayment")
    public ResponseJson checkModifyEmployeePayment(@RequestBody EmployeeAccount employeeAccount) {
        ResponseJson responseJson = new ResponseJson();

        try {
            employeeAccountService.checkModifyEmployeePayment(employeeAccount);
            responseJson.setSuccess(true);
            responseJson.setMsg("计酬数变更申请已通过");
        }catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }


    /**
     * 撤销变更揽储人申请- 任务数
     * @param employeeAccount 包含accountNo、childAccountNo
     */
    @PreAuthorize("hasAuthority('cktj:employeeaccount:undomodifyemployeetask')")
    @RequestMapping(value = "/undomodifyemployeetask")
    public ResponseJson undoModifyEmployeeTask(@RequestBody EmployeeAccount employeeAccount) {
        ResponseJson responseJson = new ResponseJson();

        try {
            employeeAccountService.undoModifyEmployeeTask(employeeAccount);
            responseJson.setSuccess(true);
            responseJson.setMsg("任务数变更申请已拒绝");
        }catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    /**
     * 撤销变更揽储人申请- 计酬数
     * @param employeeAccount 包含accountNo、childAccountNo
     */
    @PreAuthorize("hasAuthority('cktj:employeeaccount:undomodifyemployeepayment')")
    @RequestMapping(value = "/undomodifyemployeepayment")
    public ResponseJson undoModifyEmployeePayment(@RequestBody EmployeeAccount employeeAccount) {
        ResponseJson responseJson = new ResponseJson();

        try {
            employeeAccountService.undoModifyEmployeePayment(employeeAccount);
            responseJson.setSuccess(true);
            responseJson.setMsg("计酬数变更申请已拒绝");
        }catch (Exception e) {
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }


}
