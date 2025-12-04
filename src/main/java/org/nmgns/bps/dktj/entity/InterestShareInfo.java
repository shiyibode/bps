package org.nmgns.bps.dktj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseDataScopePageEntity;

import java.util.Date;

@Data
public class InterestShareInfo extends BaseDataScopePageEntity<InterestShareInfo> {

    private String accountNo;
    private String tellerCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private Double balance;
    private String lnOrgCode;
    private String belongOrgCode;
    private AccountTemplate accountTemplate;
    private Template template;
    private TemplateDetail templateDetail;
    private Position position;
    private Double percentage;
    private String xdCustomerNo;
    private String templateName;
    private String positionName;
    private String customerName;
    private Boolean positionChangeable;
    private String positionType;    //0-普通  1-推荐人
    private Boolean validFlag;
    private String oldTellerCode;
    private String oldTellerName;
    private Long parentId;
    private String alterCheckStatus;

    //用于查询条件
    private Long organizationId;
    private String lnOrgName;
    private String belongOrgName;
    private String queryType;   //0-利息明细 1-核心系统利息 2-本机构汇总利息 3-调入利息 4-调出利息
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private String orgOrEmpType; //1-机构 2-员工
    private String singleDayOrAvgType; //1-时点  2-日均
    private int avgDays;
    private Date currentDate;
    private String tellerName;


//    public static final String QUERY_TYPE_DETAIL = "0";
    public static final String QUERY_TYPE_HE_XIN = "1";
    public static final String QUERY_TYPE_HUI_ZONG = "2";
    public static final String QUERY_TYPE_DIAO_RU = "3";
    public static final String QUERY_TYPE_DIAO_LI = "4";

    public static final String CHECK_STATUS_UNCHECKED = AccountShareInfo.CHECK_STATUS_UNCHECKED;
    public static final String CHECK_STATUS_CHECKED = AccountShareInfo.CHECK_STATUS_CHECKED;

    public void setAccountTemplateId(Long accountTemplateId){
        if (null == accountTemplate) accountTemplate = new AccountTemplate();
        accountTemplate.setId(accountTemplateId);
    }

    public Long getAccountTemplateId(){
        if (null == accountTemplate) return null;
        return accountTemplate.getId();
    }

    public void setTemplateId(Long templateId){
        if (null == template) template = new Template();
        template.setId(templateId);
    }

    public Long getTemplateId(){
        if (null == template) return null;
        return template.getId();
    }

    public void setTemplateDetailId(Long templateDetailId){
        if (null == templateDetail) templateDetail = new TemplateDetail();
        templateDetail.setId(templateDetailId);
    }

    public Long getTemplateDetailId(){
        if (null == templateDetail) return null;
        return templateDetail.getId();
    }

    public void setPositionId(Long positionId){
        if (null == position) position = new Position();
        position.setId(positionId);
    }

    public Long getPositionId(){
        if (null == position) return null;
        return position.getId();
    }

    public String getQueryTypeName(){
        if (null == queryType) return null;
        if (queryType.equals(QUERY_TYPE_HE_XIN)) return "核心系统利息";
        else if (queryType.equals(QUERY_TYPE_HUI_ZONG)) return "汇总利息";
        else if (queryType.equals(QUERY_TYPE_DIAO_RU)) return "调入利息";
        else if (queryType.equals(QUERY_TYPE_DIAO_LI)) return "调出利息";
        else return "未识别的查询类型";
    }
}
