package org.nmgns.bps.cktj.entity;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.nmgns.bps.cktj.utils.TypeString;
import org.nmgns.bps.system.utils.base.BaseDataScopePageEntity;

import java.text.SimpleDateFormat;
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
//    private Date closeDate;
    private Date startDate;     //用户t_cktj_unbound_account表中的date字段
    private String remarks;


    private Long organizationId;  //用于根据机构条件过滤
    private String autoBindRule;    //账户自动绑定规则：不自动绑定、子账户级自动绑定、客户级自动绑定
    private List<TellerPercentage> tellerTaskPercentageList;
    private List<TellerPercentage> tellerPaymentPercentageList;
    private Boolean taskPaymentSameFlag;    //任务分成比例和计酬分成比例相同标志


    public String getAccountTypeStr() {
        return TypeString.getAccountTypeStr(accountType);
    }

    public String getCloseAcctFlagStr() {
        return TypeString.getCloseAcctFlagStr(this.getCloseAcctFlag());
    }

    public String getCustomerTypeStr() {
        return TypeString.getCustomerTypeStr(this.getCustomerType());
    }

    public String getIdentityTypeStr() {
        return TypeString.getIdentityTypeStr(this.getIdentityType());
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getAccountOpenDate() {
        return accountOpenDate;
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

    public String getCreateTimeStr(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (tellerTaskPercentageList!= null && !tellerTaskPercentageList.isEmpty()) return sdf.format(tellerTaskPercentageList.getFirst().getCreateTime());
        else return "";
    }

    public String getOpTellerCode(){
        if (tellerTaskPercentageList!= null && !tellerTaskPercentageList.isEmpty()) return tellerTaskPercentageList.getFirst().getOpTellerCode();
        else return "";
    }

    public String getRegisterTypeStr(){
        if (tellerTaskPercentageList!= null && !tellerTaskPercentageList.isEmpty()) return tellerTaskPercentageList.getFirst().getRegisterType();
        else return "";
    }

    public String getRegisterCheckStatus(){
        if (tellerTaskPercentageList!= null && !tellerTaskPercentageList.isEmpty()) return tellerTaskPercentageList.getFirst().getRegisterCheckStatus();
        else return "";
    }

    public String getStartDateStr(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (tellerTaskPercentageList!= null && !tellerTaskPercentageList.isEmpty()) return sdf.format(tellerTaskPercentageList.getFirst().getStartDate());
        return null;
    }



}
