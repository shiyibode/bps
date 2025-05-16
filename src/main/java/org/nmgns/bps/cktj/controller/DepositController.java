package org.nmgns.bps.cktj.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.nmgns.bps.cktj.entity.Deposit;
import org.nmgns.bps.cktj.service.DepositService;
import org.nmgns.bps.system.utils.PageData;
import org.nmgns.bps.system.utils.base.ResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/cktj/deposit")
public class DepositController {

    @Autowired
    private DepositService depositService;

    /**
     * 分页获取员工的存款任务完成情况
     * @param deposit 包含分页信息
     */
    @PreAuthorize("hasAuthority('cktj:deposit:employeetask')")
    @RequestMapping(value = "/employeetask")
    public ResponseJson getEmployeeDepositTaskList(@RequestBody Deposit deposit) {
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<Deposit> depositPageData = depositService.findEmployeeTaskDepositList(deposit);
            responseJson.setSuccess(true);
            responseJson.setData(depositPageData.getList());
            responseJson.setTotal(depositPageData.getTotal());
            responseJson.setMsg("获取员工的存款任务完成情况成功");
        }catch (Exception e) {
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    @PreAuthorize("hasAuthority('cktj:deposit:exportempdeposittask')")
    @RequestMapping(value = "/exportempdeposittask")
    public void exportEmpDepositTask(HttpServletResponse response,
                                     @RequestParam(value = "date",required = false) Date date,
                                     @RequestParam(value = "organizationId",required = false) Long organizationId,
                                     @RequestParam(value = "depositType",required = false) String depositType) {

        try {
            Deposit deposit = new Deposit();
            deposit.setDate(date);
            deposit.setOrganizationId(organizationId);
            deposit.setDepositType(depositType);
            depositService.exportEmpDepositTask(response, deposit);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 分页获取员工的日均存款任务完成情况
     * @param deposit 包含分页信息
     */
    @PreAuthorize("hasAuthority('cktj:deposit:empaveragetask')")
    @RequestMapping(value = "/empaveragetask")
    public ResponseJson getEmployeeAvgDepositTaskList(@RequestBody Deposit deposit) {
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<Deposit> depositPageData = depositService.findEmployeeTaskDepositAvgList(deposit);
            responseJson.setSuccess(true);
            responseJson.setData(depositPageData.getList());
            responseJson.setTotal(depositPageData.getTotal());
            responseJson.setMsg("获取员工的存款日均任务完成情况成功");
        }catch (Exception e) {
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    /**
     * 分页获取员工的存款计酬完成情况
     * @param deposit 包含分页信息
     */
    @PreAuthorize("hasAuthority('cktj:deposit:employeepayment')")
    @RequestMapping(value = "/employeepayment")
    public ResponseJson getEmployeeDepositPaymentList(@RequestBody Deposit deposit) {
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<Deposit> depositPageData = depositService.findEmployeePaymentDepositList(deposit);
            responseJson.setSuccess(true);
            responseJson.setData(depositPageData.getList());
            responseJson.setTotal(depositPageData.getTotal());
            responseJson.setMsg("获取员工的存款任务完成情况成功");
        }catch (Exception e) {
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    /**
     * 分页获取员工的日均存款计酬完成情况
     * @param deposit 包含分页信息
     */
    @PreAuthorize("hasAuthority('cktj:deposit:empaveragepayment')")
    @RequestMapping(value = "/empaveragepayment")
    public ResponseJson getEmployeeAvgDepositPaymentList(@RequestBody Deposit deposit) {
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<Deposit> depositPageData = depositService.findEmployeePaymentDepositAvgList(deposit);
            responseJson.setSuccess(true);
            responseJson.setData(depositPageData.getList());
            responseJson.setTotal(depositPageData.getTotal());
            responseJson.setMsg("获取员工的存款日均任务完成情况成功");
        }catch (Exception e) {
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }



}
