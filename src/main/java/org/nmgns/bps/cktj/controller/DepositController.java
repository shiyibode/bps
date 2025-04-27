package org.nmgns.bps.cktj.controller;

import org.nmgns.bps.cktj.entity.Deposit;
import org.nmgns.bps.cktj.service.DepositService;
import org.nmgns.bps.system.utils.PageData;
import org.nmgns.bps.system.utils.base.ResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            PageData<Deposit> depositPageData = depositService.findEmployeeDepositList(deposit);
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



}
