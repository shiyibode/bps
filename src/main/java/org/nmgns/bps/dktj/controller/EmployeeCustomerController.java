package org.nmgns.bps.dktj.controller;

import org.nmgns.bps.dktj.dto.CustomerStatus;
import org.nmgns.bps.dktj.dto.UserInfoForBind;
import org.nmgns.bps.dktj.entity.EmployeeCustomer;
import org.nmgns.bps.dktj.entity.SpecialAccountType;
import org.nmgns.bps.system.entity.UserOrganization;
import org.nmgns.bps.system.utils.PageData;
import org.nmgns.bps.system.utils.base.ResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.nmgns.bps.dktj.service.EmployeeCustomerSerivce;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dktj/employeecustomer")
public class EmployeeCustomerController {

    @Autowired
    private EmployeeCustomerSerivce employeeCustomerSerivce;

    @RequestMapping("/getUnregisterCustomer")
    @PreAuthorize("hasAuthority('dktj:employeecustomer:getunregistercustomer')")
    public ResponseJson UnboundCustomer(@RequestBody EmployeeCustomer ec){
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<EmployeeCustomer> employeeCustomerPageData = employeeCustomerSerivce.findUnregisterCustomerPage(ec);
            responseJson.setSuccess(true);
            responseJson.setData(employeeCustomerPageData.getList());
            responseJson.setTotal(employeeCustomerPageData.getTotal());
            responseJson.setMsg("获取数据成功");
        }catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    /**
     * 绑定贷款
     * @param ec 参数信息
     */
    @RequestMapping("/registerEmployee")
    @PreAuthorize("hasAuthority('dktj:employeecustomer:registeremployee')")
    public ResponseJson registerEmployee(@RequestBody EmployeeCustomer ec){
        ResponseJson responseJson = new ResponseJson();

        try {
            employeeCustomerSerivce.registerEmployee(ec);
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setMsg("绑定信贷维护人成功");
        return responseJson;
    }

    /**
     * 获取已绑定但未复核的客户列表
     * @param ec 搜索参数信息
     */
    @RequestMapping("/getUncheckedCustomer")
    @PreAuthorize("hasAuthority('dktj:employeecustomer:getuncheckedcustomer')")
    public ResponseJson getRegisteredUncheckedCustomer(@RequestBody EmployeeCustomer ec){
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<EmployeeCustomer> employeeCustomerPageData = employeeCustomerSerivce.findRegisterUncheckedCustomer(ec);
            responseJson.setSuccess(true);
            responseJson.setMsg("获取已登记未复核信贷客户列表成功");
            responseJson.setData(employeeCustomerPageData.getList());
            responseJson.setTotal(employeeCustomerPageData.getTotal());
        }catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    /**
     * 同意维护人绑定
     * @param ec
     */
    @RequestMapping("/checkRegisterEmployee")
    @PreAuthorize("hasAuthority('dktj:employeecustomer:checkregisteremployee')")
    public ResponseJson checkRegisterEmployee(@RequestBody EmployeeCustomer ec){
        ResponseJson responseJson = new ResponseJson();

        List<EmployeeCustomer> ecList = new ArrayList<>();
        ecList.add(ec); //留下后期扩展
        try {
            employeeCustomerSerivce.checkRegisterEmployee(ecList);
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setMsg("复核信贷客户成功");
        return responseJson;
    }

    /**
     * 拒绝复核维护人绑定
     * @param ecList
     * @return
     */
    @RequestMapping("/undoRegisterEmployee")
    @PreAuthorize("hasAuthority('dktj:employeecustomer:undoregisteremployee')")
    public ResponseJson undoRegisterEmployee(@RequestBody List<EmployeeCustomer> ecList){
        ResponseJson responseJson = new ResponseJson();

//        List<EmployeeCustomer> ecList = new ArrayList<>();
//        ecList.add(ec);
        try {
            employeeCustomerSerivce.undoRegisterEmployee(ecList);
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setMsg("拒绝复核信贷客户成功");

        return responseJson;
    }

    /**
     * 获取可变更维护人的客户列表
     * @param ec
     * @return
     */
    @RequestMapping("/getModifiableCustomer")
    @PreAuthorize("hasAuthority('dktj:employeecustomer:getmodifiablecustomer')")
    public ResponseJson getModifiableCustomer(@RequestBody EmployeeCustomer ec){
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<EmployeeCustomer> employeeCustomerPageData = employeeCustomerSerivce.findModifiableCustomer(ec);
            responseJson.setSuccess(true);
            responseJson.setMsg("获取可变更维护人的信贷客户列表成功");
            responseJson.setData(employeeCustomerPageData.getList());
            responseJson.setTotal(employeeCustomerPageData.getTotal());
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    /**
     * 提交变更客户维护人申请
     * @param ecList
     */
    @RequestMapping("/modifyEmployee")
    @PreAuthorize("hasAuthority('dktj:employeecustomer:modifyemployee')")
    public ResponseJson modifyEmployee(@RequestBody EmployeeCustomer ecList){
        ResponseJson responseJson = new ResponseJson();

//        List<EmployeeCustomer> ecList = new ArrayList<>();
//        ecList.add(ec);
        try {
            employeeCustomerSerivce.modifyEmployee(ecList);
            responseJson.setSuccess(true);
            responseJson.setMsg("变更维护人提交成功，等待复核");
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    /**
     * 获取提交变更申请，但是未复核的客户列表
     * @param ec
     */
    @RequestMapping("/getModifiedUncheckedCustomer")
    @PreAuthorize("hasAuthority('dktj:employeecustomer:getmodifieduncheckedcustomer')")
    public ResponseJson getModifiedUncheckedCustomer(@RequestBody EmployeeCustomer ec){
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<EmployeeCustomer> employeeCustomerPageData = employeeCustomerSerivce.findModifiedUncheckedCustomer(ec);
            responseJson.setSuccess(true);
            responseJson.setMsg("获取已提交变更维护人申请的信贷客户列表成功");
            responseJson.setData(employeeCustomerPageData.getList());
            responseJson.setTotal(employeeCustomerPageData.getTotal());
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    /**
     * 同意变更维护人
     * @param ecList
     */
    @RequestMapping("/checkModifyEmployee")
    @PreAuthorize("hasAuthority('dktj:employeecustomer:checkmodifyemployee')")
    public ResponseJson checkModifyEmployee(@RequestBody List<EmployeeCustomer> ecList){
        ResponseJson responseJson = new ResponseJson();

        try {
            employeeCustomerSerivce.checkModifyEmployee(ecList);
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setMsg("同意变更维护人成功");
        return responseJson;
    }

    /**
     * 拒绝变更维护人
     * @param ecList
     */
    @RequestMapping("/undoModifyEmployee")
    @PreAuthorize("hasAuthority('dktj:employeecustomer:undomodifyemployee')")
    public ResponseJson undoModifyEmployee(@RequestBody List<EmployeeCustomer> ecList){
        ResponseJson responseJson = new ResponseJson();

        try {
            employeeCustomerSerivce.undoModifyEmployee(ecList);
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setMsg("拒绝变更维护人成功");
        return responseJson;
    }

    /**
     * 获取贷款的维护人列表
     * @param ec
     */
    @RequestMapping(value = "/get")
    @PreAuthorize("hasAuthority('dktj:employeecustomer:get')")
    public ResponseJson get(@RequestBody EmployeeCustomer ec) {
        ResponseJson responseJson = new ResponseJson();

        Long id = ec.getId();
        if (null != id && Long.valueOf(id.toString()) > 0) {
            EmployeeCustomer model = employeeCustomerSerivce.get(id);
            responseJson.setData(model);
            responseJson.setTotal(1L);
            responseJson.setSuccess(true);
            responseJson.setMsg("获取数据成功");
        } else {

            try {
                PageData<EmployeeCustomer> employeeCustomerPageData = employeeCustomerSerivce.findPage(ec);
                responseJson.setSuccess(true);
                responseJson.setMsg("获取数据成功");
                responseJson.setData(employeeCustomerPageData.getList());
                responseJson.setTotal(employeeCustomerPageData.getTotal());
            }catch (Exception e){
                e.printStackTrace();
                responseJson.setSuccess(false);
                responseJson.setMsg(e.getMessage());
            }

        }

        return responseJson;
    }

    /**
     * 获取贷款客户的固定流动状态
     * @param cs
     */
    @RequestMapping(value = "/getEmployeeCustomer")
    @PreAuthorize("hasAuthority('dktj:employeecustomer:getstatus')")
    public ResponseJson getEmployeeCustomerStatus( @RequestBody CustomerStatus cs) {
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<CustomerStatus> customerStatusPageData = employeeCustomerSerivce.findOrgEmployeeCustomer(cs);
            responseJson.setSuccess(true);
            responseJson.setMsg("获取客户状态列表成功");
            responseJson.setData(customerStatusPageData.getList());
            responseJson.setTotal(customerStatusPageData.getTotal());
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    /**
     * 将客户由固定态改为流动态
     * @param cs
     */
    @RequestMapping(value = "/changeStatusToFluid")
    @PreAuthorize("hasAuthority('dktj:employeecustomer:changestatustofluid')")
    public ResponseJson changeStatusToFluid(@RequestBody CustomerStatus cs){
        ResponseJson responseJson = new ResponseJson();

        try {
            employeeCustomerSerivce.changeStatusToFluid(cs);
            responseJson.setSuccess(true);
            responseJson.setMsg("变更客户状态成功");
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    /**
     * 将客户由流动态改为固定态
     * @param cs
     */
    @RequestMapping(value = "/changeStatusToFix")
    @PreAuthorize("hasAuthority('dktj:employeecustomer:changestatustofix')")
    public ResponseJson changeStatusToFix(@RequestBody CustomerStatus cs){
        ResponseJson responseJson = new ResponseJson();

        try {
            employeeCustomerSerivce.changeStatusToFix(cs);
            responseJson.setSuccess(true);
            responseJson.setMsg("变更客户状态成功");
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    /**
     * 获取10个员工列表
     * @param uo
     */
    @RequestMapping("/userList")
    @PreAuthorize("hasAuthority('dktj:employeecustomer:userlist')")
    public ResponseJson userList(@RequestBody UserOrganization uo){
        ResponseJson responseJson = new ResponseJson();

        List<UserInfoForBind> userList;
        try {
            userList = employeeCustomerSerivce.findUserList(uo);
        }catch (Exception e){
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setData(userList);
        responseJson.setTotal((long)userList.size());
        return responseJson;
    }

    /**
     * 获取适用于表格的用户列表
     */
    @RequestMapping("/tableUserList")
    @PreAuthorize("hasAuthority('dktj:employeecustomer:tableuserlist')")
    public ResponseJson tableUserList(@RequestBody UserOrganization userOrganization){
        ResponseJson responseJson = new ResponseJson();

        List<UserInfoForBind> userList = null;
        try {
            userList = employeeCustomerSerivce.findUserList(userOrganization);
        }catch (Exception e){
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setData(userList);
        responseJson.setTotal((long)userList.size());
        return responseJson;
    }

    /**
     * 获取当前贷款最有可能的维护人
     */
    @RequestMapping("/possibleTellerCode")
    @PreAuthorize("hasAuthority('dktj:employeecustomer:possibletellercode')")
    public ResponseJson getMostPossibleBelongEmployeeTellerCode(@RequestBody UserOrganization userOrganization){
        ResponseJson responseJson = new ResponseJson();

        List<UserInfoForBind> userList = null;
        try {
            userList = employeeCustomerSerivce.findUserList(userOrganization);
        }catch (Exception e){
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setData(userList);
        responseJson.setTotal((long)userList.size());
        return responseJson;
    }

    /**
     * 获取标记类型列表
     */
    @RequestMapping("/getSpecialAccountTypeList")
    @PreAuthorize("hasAuthority('dktj:employeecustomer:getspecialaccounttypelist')")
    public ResponseJson getSpecialAccountTypeList(){
        ResponseJson responseJson = new ResponseJson();

        try {
            List<SpecialAccountType> typeList = employeeCustomerSerivce.getSpecialAccountTypeList();
            responseJson.setData(typeList);
            responseJson.setTotal((long)typeList.size());
            responseJson.setSuccess(true);
            responseJson.setMsg("获取账号标记列表成功");
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }


    /**
     * 获取当前贷款跑批的最小日期
     */
    @RequestMapping(value = "/getLoanCurrentDate")
    @PreAuthorize("hasAuthority('dktj:employeecustomer:getloancurrentdate')")
    public ResponseJson getLoanCurrentDate(@RequestBody EmployeeCustomer ec) {
        ResponseJson responseJson = new ResponseJson();

        EmployeeCustomer loanHandleConfig = employeeCustomerSerivce.getLoanCurrentDate(ec);
        responseJson.setTotal(null != loanHandleConfig ? 1L : 0L);
        responseJson.setData(loanHandleConfig);
        responseJson.setSuccess(true);
        responseJson.setMsg("获取数据成功!");

        return responseJson;
    }

}
