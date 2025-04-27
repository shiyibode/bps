package org.nmgns.bps.cktj.controller;

import org.nmgns.bps.cktj.entity.DepositCategory;
import org.nmgns.bps.cktj.service.DepositCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cktj/depositcategory")
public class DepositCategoryController {

    @Autowired
    private DepositCategoryService depositCategoryService;

    @RequestMapping("/get")
    @PreAuthorize("hasAuthority('cktj:depositcategory:get')")
    public List get() {
        return depositCategoryService.getDepositCategoryTree();
    }

//    @Override
//    @RequiresPermissions(value = "cktj:depositcategory:create")
//    public Object create(@RequestBody RequestJson<DepositCategory, Long> requestJson) {
//        return super.create(requestJson);
//    }
//
//    @Override
//    @RequiresPermissions(value = "cktj:depositcategory:update")
//    public Object update(@RequestBody RequestJson<DepositCategory, Long> requestJson) {
//        return super.update(requestJson);
//    }
//
//    @Override
//    @RequiresPermissions(value = "cktj:depositcategory:delete")
//    public Object delete(@RequestBody RequestJson<DepositCategory, Long> requestJson) {
//        return super.delete(requestJson);
//    }




}
