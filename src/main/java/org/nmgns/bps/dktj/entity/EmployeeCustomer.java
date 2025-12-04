package org.nmgns.bps.dktj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseDataScopePageEntity;

import java.util.Date;
import java.util.List;

@Data
public class EmployeeCustomer extends BaseDataScopePageEntity<EmployeeCustomer> {

    private String xdCustomerNo;    //信贷系统客户号
    private String hxCustomerNo;    //核心系统客户号
    private String accountNo;       //贷款账号
    private Date accountOpenDate;   //贷款账号开户时间
    private String customerType;    //1-个人 2-对公
    private String orgCode;         //核心网点号
    private String tellerCode;      //所属柜员的柜员号
    private Date date;              //产生未绑定记录的业务日期
    private Date startDate;         //起始日期
    private Date endDate;           //终止日期
    private Boolean validFlag;      //有效标志
    private String status;          //1-固定客户 2-流动客户
    private String registerType;    //登记类型：1-新客户  2-柜员调动后释放的新客户  3-变更维护人
    private String registerCheckStatus;     //登记审核状态：0-未审核 1-已审核 2-审核不通过
    private Date registerCheckTime;         //登记审核时间
    private String alterCheckStatus;     //变更审核状态：0-未审核 1-已审核 2-审核不通过
    private Date alterCheckTime;         //变更审核时间
    private String opTellerCode;    //操作柜员
    private String registerCheckTellerCode; //登记复核柜员
    private String alterCheckTellerCode;    //变更复核柜员
    private Long parentId;          //父级id，变更维护人时用到
    private String flag;            //1-跑批产生的新客户  2-柜员调动释放的客户
    private Integer accountCount;   //该客户未绑定的账户数
    private String boundType;       //绑定类型：1-自动 2-手工
    private Long templateId;        //模板类型id
    private List<AccountShareInfo> accountShareInfoList;    //员工分成明细
    private Long specialAccountTypeId;      //标记贷款账号：1-公司业务中心贷款 2-小微贷款 3-通汇贷款 4-阿吉奈贷款

    //t_dktj_employee_customer表中没有，但是用来搜索的条件而添加的属性
    private Long organizationId;
    private String organizationName;
    private String customerName;
    private String identityType;
    private String identityNo;
    private String tellerName;
    private String tellerOrgCode;
    private String tellerOrgName;
    private String oldTellerCode;
    private String oldTellerName;

    private Date currentDate;
    private String remarks;


    public static final String CUSTOMER_TYPE_PERSON = "1";
    public static final String CUSTOMER_TYPE_GROUP = "2";

    public static final String STATUS_FIXED_CUSTOMER = "1";
    public static final String STATUS_FLUID_CUSTOMER = "2";

    public static final String REGISTER_TYPE_NEW_CUSTOMER = "1";
    public static final String REGISTER_TYPE_RELEASED_CUSTOMER = "2";
    public static final String REGISTER_TYPE_CHANGED_CUSTOMER = "3";
    public static final String REGISTER_TYPE_STATUS_RELEASED_CUSTOMER = "4";

    public static final String CHECKED_STATUS_UNCHECKED = "0";
    public static final String CHECKED_STATUS_CHECKED = "1";
    public static final String CHECKED_STATUS_DENIED = "2";

    public static final String FLAG_NEW_CUSTOMER = "1";
    public static final String FLAG_RELEASED_CUSTOMER = "2";
    public static final String FLAG_RELEASED_STATUS_RELEASED = "3"; //流动转固定时释放的客户

    public static final String BOUND_FLAG_AUTO = "1";
    public static final String BOUND_FLAG_MANUAL = "2";
    public static final String BOUND_FLAG_STATUS_RELEASED = "3";

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getStartDate() {
        return startDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getEndDate() {
        return endDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getAccountOpenDate() {
        return accountOpenDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getRegisterCheckTime() {
        return registerCheckTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getAlterCheckTime() {
        return alterCheckTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getDate() {
        return date;
    }


    public String getCustomerTypeStr(){
        String customerTypeStr = null;
        if (this.customerType == null) return null;
        switch (this.customerType){
            case CUSTOMER_TYPE_PERSON: customerTypeStr = "个人";break;
            case CUSTOMER_TYPE_GROUP: customerTypeStr = "对公";break;
            default: customerTypeStr = "未识别的客户类型";break;
        }
        return customerTypeStr;
    }

    public String getFlagStr(){
        if (null == flag) return null;
        if (flag.equals(FLAG_NEW_CUSTOMER)) return "新客户";
        else if (flag.equals(FLAG_RELEASED_CUSTOMER)) return "柜员调动释放的客户";
        else if (flag.equals(FLAG_RELEASED_STATUS_RELEASED)) return "客户状态由流动转固定时释放的客户";
        else return null;
    }
}
