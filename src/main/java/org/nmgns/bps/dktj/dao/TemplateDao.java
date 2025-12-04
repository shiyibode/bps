package org.nmgns.bps.dktj.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nmgns.bps.dktj.entity.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface TemplateDao {

    public List<Template> findAvailableTemplateList();

    public List<TemplateDetail> findTemplateDetailListByTemplateId(TemplateDetail templateDetail);

    public TemplateDetail findTemplateDetailInfoById(Long templateDetailId);

    Long insertAccountTemplate(AccountTemplate accountTemplate);

    void insertAccountShareInfo(AccountShareInfo accountShareInfo);

    List<AccountTemplate> findAccountTemplateListByAccountNo(AccountTemplate at);

    AccountTemplate findAccountCurrentTemplateByAccountNo(AccountTemplate at);

    void deleteAccountTemplateById(AccountTemplate at);

    void deleteAccountShareInfoByAccountTemplateId(@Param("accountTemplateId") Long accountTemplateId);

    void deleteAccountShareInfoById(@Param("id") Long id);

    AccountShareInfo findAccountShareInfoByAccountTemplateId(AccountShareInfo as); //position是为了提供推荐人类型，前端直接new

    AccountShareInfo findUncheckedAccountShareInfoByAccountTemplateId(AccountShareInfo asi); //position是为了提供推荐人类型，前端直接new

    void updateAccountShareInfoById(AccountShareInfo as);

    Long getTemplateDetailIdByTemplateId(TemplateDetail td);

    Long findAccountTemplateListCount(AccountTemplate at);

    List<AccountTemplate> findAccountTemplateList(AccountTemplate at);

    List<AccountShareInfo> getValidAccountShareInfoByAccountTemplateId(Long accountTemplateId);

    void updateAccountTemplateById(AccountTemplate at);
}
