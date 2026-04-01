package org.nmgns.bps.dktj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.nmgns.bps.system.utils.base.BaseDataScopeEntity;

import java.math.BigDecimal;
import java.util.Date;

public class Loan extends BaseDataScopeEntity {

    private String tellerCode;
    private String tellerName;
    private String tellerOrgCode;
    private String tellerOrgName;
    private String lnOrgCode;
    private String lnOrgName;
    private String belongOrgCode;
    private String belongOrgName;
    private String fourClassFlag;
    private String fiveClassFlag;
    private BigDecimal balance;
    private BigDecimal prvisnInt;
    private BigDecimal dayReceivedInt;
    private BigDecimal ttlReceivedInt;
    private Date date;

    //以下属性用于搜索
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private Integer avgDays;
    private Long organizationId;
    private String loanType;    //对于机构，贷款类型: 0-核心贷款;1-汇总;2-调入贷款;3-调离贷款
                                //对于员工，贷款类型：0-核心系统贷款:本机构员工在本机构贷款+其他机构员工在本机构贷款; 1-员工实际贷款：本机构员工在所有机构的贷款; 2-员工在本机构贷款; 3-调入贷款：本机构员工在其他机构的贷款; 4-调离贷款：其他机构员工在本机构贷款;

    public static final String ORG_LOAN_TYPE_HE_XIN = "0";
    public static final String ORG_LOAN_TYPE_HUI_ZONG = "1";
    public static final String ORG_LOAN_TYPE_DIAO_RU = "2";
    public static final String ORG_LOAN_TYPE_DIAO_LI = "3";

    public static final String EMP_LOAN_TYPE_HE_XIN = "0";
    public static final String EMP_LOAN_TYPE_SHI_JI = "1";
    public static final String EMP_LOAN_TYPE_SUO_ZAI = "2";
    public static final String EMP_LOAN_TYPE_DIAO_RU = "3";
    public static final String EMP_LOAN_TYPE_DIAO_LI = "4";


    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTellerCode() {
        return tellerCode;
    }

    public void setTellerCode(String tellerCode) {
        this.tellerCode = tellerCode;
    }

    public String getTellerName() {
        return tellerName;
    }

    public void setTellerName(String tellerName) {
        this.tellerName = tellerName;
    }

    public String getTellerOrgCode() {
        return tellerOrgCode;
    }

    public void setTellerOrgCode(String tellerOrgCode) {
        this.tellerOrgCode = tellerOrgCode;
    }

    public String getTellerOrgName() {
        return tellerOrgName;
    }

    public void setTellerOrgName(String tellerOrgName) {
        this.tellerOrgName = tellerOrgName;
    }

    public String getLnOrgCode() {
        return lnOrgCode;
    }

    public void setLnOrgCode(String lnOrgCode) {
        this.lnOrgCode = lnOrgCode;
    }

    public String getLnOrgName() {
        return lnOrgName;
    }

    public void setLnOrgName(String lnOrgName) {
        this.lnOrgName = lnOrgName;
    }

    public String getBelongOrgCode() {
        return belongOrgCode;
    }

    public void setBelongOrgCode(String belongOrgCode) {
        this.belongOrgCode = belongOrgCode;
    }

    public String getBelongOrgName() {
        return belongOrgName;
    }

    public void setBelongOrgName(String belongOrgName) {
        this.belongOrgName = belongOrgName;
    }
    public String getFourClassFlag() {
        return fourClassFlag;
    }

    public void setFourClassFlag(String fourClassFlag) {
        this.fourClassFlag = fourClassFlag;
    }

    public String getFiveClassFlag() {
        return fiveClassFlag;
    }

    public void setFiveClassFlag(String fiveClassFlag) {
        this.fiveClassFlag = fiveClassFlag;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getPrvisnInt() {
        return prvisnInt;
    }

    public void setPrvisnInt(BigDecimal prvisnInt) {
        this.prvisnInt = prvisnInt;
    }

    public BigDecimal getDayReceivedInt() {
        return dayReceivedInt;
    }

    public void setDayReceivedInt(BigDecimal dayReceivedInt) {
        this.dayReceivedInt = dayReceivedInt;
    }

    public BigDecimal getTtlReceivedInt() {
        return ttlReceivedInt;
    }

    public void setTtlReceivedInt(BigDecimal ttlReceivedInt) {
        this.ttlReceivedInt = ttlReceivedInt;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getAvgDays() {
        return avgDays;
    }

    public void setAvgDays(Integer avgDays) {
        this.avgDays = avgDays;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
