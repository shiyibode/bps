package org.nmgns.bps.cktj.entity;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.nmgns.bps.cktj.utils.TypeString;
import org.nmgns.bps.system.utils.base.BaseDataScopePageEntity;

import java.util.Date;
import java.util.List;

@Data
public class EmployeeAccount extends BaseDataScopePageEntity<EmployeeAccount> {


    private Long unboundAccountId;
    private String orgCode;
    private String accountNo;
    private String childAccountNo;
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
    private Date startDate;     //用户t_cktj_unbound_account表中的date字段
    private String remarks;

    private String mainTellerCode;
    private Date endDate;
    private String opTellerCode;
    private Date createTime;
    private Long parentId;

    private String registerType;         //登记类型: 0-新开账户登记揽储人; 1-变更揽揽人
    private String registerCheckStatus; //登记复核状态：0-未复核 1-已复核
    private String alterCheckStatus;    //变更复核状态：0-未复核 1-已复核
    private String registerCheckTellerCode; //登记复核柜员
    private String alterCheckTellerCode;    //变更复核柜员
    private Date registerCheckTime;         //登记复核时间
    private Date alterCheckTime;            //变更复核时间
    private Boolean validFlag;

    private String tellerCode;
    private String tellerName;
    private String tellerOrgCode;
    private String tellerOrgName;

    private Long organizationId;  //用于根据机构条件过滤
    private String autoBindRule;    //账户自动绑定规则：不自动绑定、子账户级自动绑定、客户级自动绑定
    private List<TellerPercentage> tellerTaskPercentageList;
    private List<TellerPercentage> tellerPaymentPercentageList;
    private Boolean taskPaymentSameFlag;    //任务分成比例和计酬分成比例相同标志

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getAccountOpenDate() {
        return accountOpenDate;
    }

    private List<TellerPercentage> oldTellerTaskPercentageList;
    private List<TellerPercentage> oldTellerPaymentPercentageList;

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

    public String getRegisterTypeStr() {
        return registerType;
    }

    public String getAccountTypeStr() {
        return TypeString.getAccountTypeStr(accountType);
    }

    public String getCloseAcctFlagStr() {
        return TypeString.getCloseAcctFlagStr(this.closeAcctFlag);
    }

    public String getCustomerTypeStr() {
        return TypeString.getCustomerTypeStr(this.customerType);
    }

    public String getIdentityTypeStr() {
        return TypeString.getIdentityTypeStr(this.identityType);
    }

    // 以字符串形式返回任务数分成信息
    public String getTellerTaskPercentageStr() {
        if (tellerTaskPercentageList != null && !tellerTaskPercentageList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (TellerPercentage tp : tellerTaskPercentageList) {
                sb.append(tp.getTellerName());
                sb.append("-");
                sb.append(tp.getTellerCode());
                sb.append(":");
                sb.append(tp.getPercentage()*100);
                sb.append("%, ");
            }
            return StrUtil.removeSuffix(sb.toString(),", ");
        }

        return null;
    }

    // 以字符串形式返回计酬数分成信息
    public String getTellerPaymentPercentageStr() {
        if (tellerPaymentPercentageList != null && !tellerPaymentPercentageList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (TellerPercentage tp : tellerPaymentPercentageList) {
                sb.append(tp.getTellerName());
                sb.append("-");
                sb.append(tp.getTellerCode());
                sb.append(":");
                sb.append(tp.getPercentage()*100);
                sb.append("%, ");
            }
            return StrUtil.removeSuffix(sb.toString(),", ");
        }

        return null;
    }

    // 以字符串形式返回任务数分成信息
    public String getOldTellerTaskPercentageStr() {
        if (oldTellerTaskPercentageList != null && !oldTellerTaskPercentageList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (TellerPercentage tp : oldTellerTaskPercentageList) {
                sb.append(tp.getTellerName());
                sb.append("-");
                sb.append(tp.getTellerCode());
                sb.append(":");
                sb.append(tp.getPercentage()*100);
                sb.append("%, ");
            }
            return StrUtil.removeSuffix(sb.toString(),", ");
        }

        return null;
    }

    // 以字符串形式返回计酬数分成信息
    public String getOldTellerPaymentPercentageStr() {
        if (oldTellerPaymentPercentageList != null && !oldTellerPaymentPercentageList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (TellerPercentage tp : oldTellerPaymentPercentageList) {
                sb.append(tp.getTellerName());
                sb.append("-");
                sb.append(tp.getTellerCode());
                sb.append(":");
                sb.append(tp.getPercentage()*100);
                sb.append("%, ");
            }
            return StrUtil.removeSuffix(sb.toString(),", ");
        }

        return null;
    }
}
