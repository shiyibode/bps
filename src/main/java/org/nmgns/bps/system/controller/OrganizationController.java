package org.nmgns.bps.system.controller;

import org.nmgns.bps.system.entity.Organization;
import org.nmgns.bps.system.entity.TreeOrganization;
import org.nmgns.bps.system.service.OrganizationService;
import org.nmgns.bps.system.utils.PageData;
import org.nmgns.bps.system.utils.base.ResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sys/organization")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @RequestMapping("/get")
    @PreAuthorize("hasAuthority('sys:organization:get')")
    public ResponseJson get(@RequestBody Organization organization) {
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<Organization> pageData = organizationService.getOrganizationListPage(organization);
            responseJson.setTotal(pageData.getTotal());
            responseJson.setData(pageData.getList());
            responseJson.setSuccess(Boolean.TRUE);
            responseJson.setMsg("获取机构列表成功");
        }catch (Exception e){
            responseJson.setSuccess(Boolean.FALSE);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @RequestMapping("/create")
    @PreAuthorize("hasAuthority('sys:organization:create')")
    public ResponseJson create(@RequestBody Organization organization) {
        ResponseJson responseJson = new ResponseJson();

        try {
            organizationService.create(organization);
            responseJson.setSuccess(Boolean.TRUE);
            responseJson.setMsg("新增机构成功");
        }catch (Exception e){
            responseJson.setSuccess(Boolean.FALSE);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('sys:organization:update')")
    public ResponseJson update(@RequestBody Organization organization) {
        ResponseJson responseJson = new ResponseJson();

        try {
            organizationService.update(organization);
            responseJson.setSuccess(Boolean.TRUE);
            responseJson.setMsg("修改机构信息成功");
        }catch (Exception e){
            responseJson.setSuccess(Boolean.FALSE);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('sys:organization:delete')")
    public ResponseJson delete(@RequestParam("organizationId") Long organizationId) {
        ResponseJson responseJson = new ResponseJson();

        try {
            organizationService.delete(organizationId);
            responseJson.setSuccess(Boolean.TRUE);
            responseJson.setMsg("机构删除成功");
        }catch (Exception e){
            responseJson.setSuccess(Boolean.FALSE);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    /**
     * 获取用户界面中显示的机构树（特殊用户，可以通过增加默认角色来获取非在职机构）
     */
    @RequestMapping("/getOrganizationTree")
    @PreAuthorize("hasAuthority('sys:organization:getorganizationtree')")
    public List<TreeOrganization> getOrganizationTree() {
        try {
            return organizationService.getOrganizationTree();
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<TreeOrganization>();
        }
    }



}
