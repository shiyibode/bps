package org.nmgns.bps.cktj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseDataScopePageEntity;

import java.util.Date;

@Data
public class TellerPercentage extends BaseDataScopePageEntity<TellerPercentage> {

    private String tellerCode;
    private Double percentage;
    private Boolean mainTeller;

    private Date startDate;
    private Date endDate;
    private String opTellerCode;
    private Date createTime;
    private Long[] parentIds;
    private String oldTellerCode;
    private String oldTellerName;
    private String registerType;         //登记类型: 0-新开账户登记揽储人; 1-变更揽揽人
    private String registerCheckStatus; //登记复核状态：0-未复核 1-已复核
    private String alterCheckStatus;    //变更复核状态：0-未复核 1-已复核
    private String registerCheckTellerCode; //登记复核柜员
    private String alterCheckTellerCode;    //变更复核柜员
    private Date registerCheckTime;         //登记复核时间
    private Date alterCheckTime;            //变更复核时间
    private Boolean validFlag;
    private String remarks;


    private String tellerName;
    private String tellerOrgCode;
    private String tellerOrgName;

    private String accountNo;       //用于搜索
    private String childAccountNo;  //用于搜索
    private Long organizationId;
    private String subjectNo;
    private String subjectName;
    private String accountType;     //1-普通 2-内部户
    private String cardNo;
    private String closeAcctFlag;
    private String customerNo;
    private String customerName;
    private String customerType;
    private String identityType;
    private String identityNo;
    private Date accountOpenDate;


    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getStartDate() {
        return startDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getEndDate() {
        return endDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getRegisterCheckTime() {
        return registerCheckTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getAlterCheckTime(){
        return alterCheckTime;
    }

}
