package org.nmgns.bps.dktj.entity;

import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseDataScopePageEntity;

import java.util.Date;

@Data
public class AccountShareInfo extends BaseDataScopePageEntity<AccountShareInfo> {

    private AccountTemplate accountTemplate;
    private TemplateDetail templateDetail;
    private String tellerCode;
    private Date startDate;
    private Date endDate;
    private Long parentId;
    private Boolean validFlag;
    private String checkStatus;
    private String alterCheckStatus;
    private String alterCheckTeller;
    private String remarks;

    public static final String CHECK_STATUS_UNCHECKED = "0";
    public static final String CHECK_STATUS_CHECKED = "1";

    private String positionType;
    private Position position;

    public void setAccountTemplateId(Long accountTemplateId){
        if (accountTemplate == null) accountTemplate = new AccountTemplate();
        accountTemplate.setId(accountTemplateId);
    }

    public Long getAccountTemplateId(){
        if (accountTemplate != null) return accountTemplate.getId();
        else return null;
    }

    public void setTemplateDetailId(Long templateDetailId){
        if (templateDetail == null) templateDetail = new TemplateDetail();
        templateDetail.setId(templateDetailId);
    }

    public Long getTemplateDetailId(){
        if (templateDetail != null) return templateDetail.getId();
        else return null;
    }

    public void setTemplateId(Long id){
        if (accountTemplate == null) accountTemplate = new AccountTemplate();
        accountTemplate.setTemplateId(id);
    }

    public void setAccountNo(String accountNo){
        if (accountTemplate == null) accountTemplate = new AccountTemplate();
        accountTemplate.setAccountNo(accountNo);
    }

    public String getAccountNo(){
        if (accountTemplate != null) return accountTemplate.getAccountNo();
        else return null;
    }

    public void setOrgCode(String orgCode){
        if (accountTemplate == null) accountTemplate = new AccountTemplate();
        accountTemplate.setOrgCode(orgCode);
    }

    public String getOrgCode(){
        if (accountTemplate != null) return accountTemplate.getOrgCode();
        else return null;
    }

    public Position getPosition() {
        if (position == null) position = new Position();
        return position;
    }
}
