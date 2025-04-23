package org.nmgns.bps.system.controller;

import org.nmgns.bps.system.entity.Api;
import org.nmgns.bps.system.service.ApiService;
import org.nmgns.bps.system.utils.PageData;
import org.nmgns.bps.system.utils.base.ResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/api")
public class ApiController {

    @Autowired
    private ApiService apiService;


    @PreAuthorize("hasAuthority('sys:api:get')")
    @RequestMapping("/get")
    public ResponseJson get(@RequestBody Api api){
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<Api> pageData = apiService.getApiListPage(api);
            responseJson.setTotal(pageData.getTotal());
            responseJson.setData(pageData.getList());
            responseJson.setSuccess(Boolean.TRUE);
            responseJson.setMsg("获取接口列表成功");
        }catch (Exception e){
            responseJson.setSuccess(Boolean.FALSE);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @PreAuthorize("hasAuthority('sys:api:create')")
    @RequestMapping("/create")
    public ResponseJson create(@RequestBody Api api){
        ResponseJson responseJson = new ResponseJson();

        try {
            apiService.create(api);
            responseJson.setSuccess(true);
            responseJson.setMsg("新增接口成功");
        } catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @PreAuthorize("hasAuthority('sys:api:update')")
    @RequestMapping("/update")
    public ResponseJson update(@RequestBody Api api){
        ResponseJson responseJson = new ResponseJson();

        try {
            apiService.update(api);
            responseJson.setSuccess(true);
            responseJson.setMsg("修改接口成功");
        } catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }

    @PreAuthorize("hasAuthority('sys:api:delete')")
    @RequestMapping("/delete")
    public ResponseJson delete(@RequestParam("apiApiId") Long apiApiId){
        ResponseJson responseJson = new ResponseJson();

        try {
            apiService.delete(apiApiId);
            responseJson.setSuccess(true);
            responseJson.setMsg("删除接口成功");
        } catch (Exception e) {
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }
        return responseJson;
    }


}
