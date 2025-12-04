package org.nmgns.bps.dktj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseDataScopePageEntity;

import java.util.Date;
import java.util.List;

@Data
public class AccountTemplate extends BaseDataScopePageEntity<AccountTemplate> {

    private String accountNo;
    private Template template;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    private Date endDate;
    private Long parentId;
    private Boolean validFlag;
    private String orgCode;
    private String checkStatus;

    //用于查询的字段
    private String customerName;
    private Date accountOpenDate;
    private String identityNo;
    private String xdCustomerNo;
    private String templateName;
    private Long employeeCustomerId;
    private Long organizationId;
    private List<AccountShareInfo> accountShareInfoList;
    private String remarks;

    private String tellerCode;//变更模板时，新的维护人号


    public static final String CHECK_STATUS_UNCHECKED = "0";
    public static final String CHECK_STATUS_CHECKED = "1";

    public void setTemplateId(Long templateId){
        if (template == null) template = new Template();
        template.setId(templateId);
    }

    public Long getTemplateId(){
        if (template != null) return template.getId();
        else return null;
    }
}
