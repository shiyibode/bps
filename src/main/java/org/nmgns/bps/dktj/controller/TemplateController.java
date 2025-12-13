package org.nmgns.bps.dktj.controller;

import org.nmgns.bps.dktj.entity.AccountTemplate;
import org.nmgns.bps.dktj.entity.Template;
import org.nmgns.bps.dktj.entity.TemplateDetail;
import org.nmgns.bps.dktj.service.TemplateSerivce;
import org.nmgns.bps.system.utils.PageData;
import org.nmgns.bps.system.utils.base.ResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dktj/template")
public class TemplateController {

    @Autowired
    private TemplateSerivce templateSerivce;

    /**
     * 获取模板列表
     * @param template
     */
    @RequestMapping("/getTemplateList")
    @PreAuthorize("hasAuthority('dktj:template:gettemplatelist')")
    public ResponseJson getTemplateList(@RequestBody Template template){
        ResponseJson responseJson = new ResponseJson();

        try {
            List<Template> templateList = templateSerivce.findAvailableTemplateList();
            responseJson.setData(templateList);
            responseJson.setTotal((long)templateList.size());
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
            return responseJson;
        }

        responseJson.setSuccess(true);
        responseJson.setMsg("获取模板列表成功");
        return responseJson;
    }

    /**
     * 获取指定模板下的岗位列表及分成比例
     */
    @RequestMapping("/getTemplateDetailList")
    @PreAuthorize("hasAuthority('dktj:template:gettemplatedetaillist')")
    public ResponseJson getTemplateDetailList(@RequestBody TemplateDetail td){
        ResponseJson responseJson = new ResponseJson();

        try {
            List<TemplateDetail> templateDetailList = templateSerivce.findTemplateDetailListByTemplateId(td);
            responseJson.setData(templateDetailList);
            responseJson.setTotal((long)templateDetailList.size());
            responseJson.setSuccess(true);
            responseJson.setMsg("获取模板明细列表成功");
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    /**
     * 获取账号对应的模板列表
     */
    @RequestMapping("/getccounttemplatelist")
    @PreAuthorize("hasAuthority('dktj:template:getaccounttemplatelist')")
    public ResponseJson getAccountTemplateList(@RequestBody AccountTemplate at){
        ResponseJson responseJson = new ResponseJson();

        try {
            PageData<AccountTemplate> pageData = templateSerivce.findAccountTemplateList(at);
            responseJson.setTotal(pageData.getTotal());
            responseJson.setData(pageData.getList());
            responseJson.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }

    /**
     * 变更账号对应的模板
     */
    @RequestMapping("/alteraccounttemplate")
    @PreAuthorize("hasAuthority('dktj:template:alteraccounttemplate')")
    public ResponseJson alterAccountTemplate(@RequestBody AccountTemplate at){
        ResponseJson responseJson = new ResponseJson();

        try {
            templateSerivce.alterAccountTemplate(at);
            responseJson.setSuccess(true);
            responseJson.setMsg("获取账号标记列表成功");
        }catch (Exception e){
            e.printStackTrace();
            responseJson.setSuccess(false);
            responseJson.setMsg(e.getMessage());
        }

        return responseJson;
    }


}
