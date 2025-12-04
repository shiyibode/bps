package org.nmgns.bps.dktj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseDataScopePageEntity;
import org.nmgns.bps.system.utils.base.BaseEntity;

import java.util.Date;

@Data
public class EmployeeInterest extends BaseDataScopePageEntity<EmployeeInterest> {

    private String accountNo;
    private String xdCustomerNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date accountOpenDate;
    private String customerName;
    private String orgCode;
    private String identityType;
    private String identityNo;
    private String customerType;
    private String orgName;

    private String tellerCode;
    private Long organizationId;

    public static final String CUSTOMER_TYPE_PERSON = "1";
    public static final String CUSTOMER_TYPE_COMPANY = "2";

    public String getCustomerTypeStr(){
        if (customerType.equals(CUSTOMER_TYPE_PERSON)) return "个人";
        else if (customerType.equals(CUSTOMER_TYPE_COMPANY)) return "对公";
        else return null;
    }
}
