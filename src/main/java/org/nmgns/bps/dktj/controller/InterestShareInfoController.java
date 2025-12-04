package org.nmgns.bps.dktj.controller;

import org.nmgns.bps.dktj.entity.EmployeeCustomer;
import org.nmgns.bps.dktj.entity.EmployeeInterest;
import org.nmgns.bps.dktj.entity.InterestShareInfo;
import org.nmgns.bps.dktj.entity.Position;
import org.nmgns.bps.dktj.service.InterestShareInfoSerivce;
import org.nmgns.bps.system.utils.PageData;
import org.nmgns.bps.system.utils.base.ResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dktj/employeeinterest")
public class InterestShareInfoController {

    @Autowired
    private InterestShareInfoSerivce interestShareInfoSerivce;

    /**
     * 获取待补录的贷款账户列表
     * @param ei
     */
    @RequestMapping("/readd")
    @PreAuthorize("hasAuthority('dktj:employeeinterest:readd')")
    public ResponseJson getReaddList(@RequestBody EmployeeInterest ei){
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<EmployeeInterest> employeeInterestPageData = interestShareInfoSerivce.findUnboundReaddAccount(ei);
            responseJson.setSuccess(true);
            responseJson.setData(employeeInterestPageData.getList());
            responseJson.setTotal(employeeInterestPageData.getTotal());
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        responseJson.setMsg("获取待补录列表成功");
        return responseJson;
    }

    /**
     * 补录信贷维护人
     * @param ec
     */
    @RequestMapping("/readdaccount")
    @PreAuthorize("hasAuthority('dktj:employeeinterest:readdaccount')")
    public ResponseJson getReaddAccount(@RequestBody EmployeeCustomer ec){
        ResponseJson responseJson = new ResponseJson();

        try {
            interestShareInfoSerivce.readdEmployeeInterest(ec);
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setMsg("补录信贷维护人成功");
        return responseJson;
    }

    /**
     * 获取员工时点贷款利息
     * @param isi
     */
    @RequestMapping("/empinterest")
    @PreAuthorize("hasAuthority('dktj:employeeinterest:empinterest')")
    public ResponseJson getEmployeeInterestList(@RequestBody InterestShareInfo isi){
        ResponseJson responseJson = new ResponseJson();

        List<InterestShareInfo> result = new ArrayList<>();
        try {
            result = interestShareInfoSerivce.findEmployeeInterestList(isi);
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setData(result);
        return responseJson;
    }

    /**
     * 获取员工汇总利息
     * @param isi
     */
    @RequestMapping("/empavginterest")
    @PreAuthorize("hasAuthority('dktj:employeeinterest:empavginterest')")
    public ResponseJson getEmployeeAvgInterestList(@RequestBody InterestShareInfo isi){
        ResponseJson responseJson = new ResponseJson();

        try {
            List<InterestShareInfo> result = interestShareInfoSerivce.findEmployeeAvgInterestList(isi);
            responseJson.setSuccess(true);
            responseJson.setData(result);
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    /**
     * 机构时点利息
     * @param isi
     */
    @RequestMapping("/orginterest")
    @PreAuthorize("hasAuthority('dktj:employeeinterest:orginterest')")
    public ResponseJson getOrganizationInterestList(@RequestBody InterestShareInfo isi){
        ResponseJson responseJson = new ResponseJson();

        try {
            List<InterestShareInfo> result = interestShareInfoSerivce.findOrganizationInterestList(isi);
            responseJson.setSuccess(true);
            responseJson.setData(result);
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        return responseJson;
    }

    /**
     * 获取机构在一个区间段内的汇总利息
     * @param isi
     */
    @RequestMapping("/orgavginterest")
    @PreAuthorize("hasAuthority('dktj:employeeinterest:orgavginterest')")
    public ResponseJson getOrganizationAvgInterestList(@RequestBody InterestShareInfo isi){
        ResponseJson responseJson = new ResponseJson();

        try {
            List<InterestShareInfo> result = interestShareInfoSerivce.findOrganizationAvgInterestList(isi);
            responseJson.setSuccess(true);
            responseJson.setData(result);
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        return responseJson;
    }

    /**
     * 获取当前生效的账户岗位责任人列表
     */
    @RequestMapping("/positiontelleralterlist")
    @PreAuthorize("hasAuthority('dktj:employeeinterest:positiontelleralterlist')")
    public ResponseJson getPositionTellerAlterList(@RequestBody InterestShareInfo isi){
        ResponseJson responseJson = new ResponseJson();


        try {
            PageData<InterestShareInfo> pageData = interestShareInfoSerivce.getValidPositionTellerList(isi);
            responseJson.setSuccess(true);
            responseJson.setData(pageData);
            responseJson.setTotal(pageData.getTotal());
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        return responseJson;
    }

    /**
     * 变更贷款岗位责任人
     * @param isiList
     */
    @RequestMapping("/positiontelleralter")
    @PreAuthorize("hasAuthority('dktj:employeeinterest:positiontelleralter')")
    public ResponseJson positionTellerAlter(@RequestBody List<InterestShareInfo> isiList){
        ResponseJson responseJson = new ResponseJson();

        try {
            interestShareInfoSerivce.positionTellerAlter(isiList);
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setMsg("变更岗位责任人成功");
        return responseJson;
    }


    @RequestMapping("/positiontellerchecklist")
    @PreAuthorize("hasAuthority('dktj:employeeinterest:positiontellerchecklist')")
    public ResponseJson getAlterUncheckedPositionTellerList(@RequestBody InterestShareInfo isi){
        ResponseJson responseJson = new ResponseJson();
        try {
            PageData<InterestShareInfo> pageData = interestShareInfoSerivce.getAlterUncheckedShareInfo(isi);
            responseJson.setSuccess(true);
            responseJson.setData(pageData.getList());
            responseJson.setTotal(pageData.getTotal());
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        return responseJson;
    }

    @RequestMapping("/positiontellercheck")
    @PreAuthorize("hasAuthority('dktj:employeeinterest:positiontellercheck')")
    public ResponseJson positionTellerCheck(@RequestBody List<InterestShareInfo> isiList){
        ResponseJson responseJson = new ResponseJson();

        try {
            interestShareInfoSerivce.positionTellerAlterCheck(isiList);
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setMsg("变更岗位责任人成功");
        return responseJson;
    }

    @RequestMapping("/positiontelleruncheck")
    @PreAuthorize("hasAuthority('dktj:employeeinterest:positiontelleruncheck')")
    public ResponseJson positionTellerUncheck(@RequestBody List<InterestShareInfo> isiList){
        ResponseJson responseJson = new ResponseJson();

        try {
            interestShareInfoSerivce.positionTellerAlterUncheck(isiList);
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setMsg("变更岗位责任人成功");
        return responseJson;
    }

    @RequestMapping("/positionlist")
    @PreAuthorize("hasAuthority('dktj:employeeinterest:positiontelleralterlist')")
    public ResponseJson getPositionList(){
        ResponseJson responseJson = new ResponseJson();


        List<Position> result = new ArrayList<>();
        try {
            result = interestShareInfoSerivce.getPositionList();
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setData(result);
        responseJson.setTotal((long)result.size());
        return responseJson;
    }



}
