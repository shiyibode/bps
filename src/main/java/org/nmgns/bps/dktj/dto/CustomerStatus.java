package org.nmgns.bps.dktj.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseDataScopePageEntity;

import java.util.Date;

@Data
public class CustomerStatus extends BaseDataScopePageEntity<CustomerStatus> {

    private String xdCustomerNo;
    private String orgCode;
    private String status;
    private Date startDate;
    private Date endDate;
    private Long parentId;
    private Boolean validFlag;
    private String registerAccountNo;
    private String customerName;
    private String identityNo;
    private String customerType;  //1-个人 2-对公
    private Long organizationId;
    private String checkStatus;

    //status状态
    public static final String STATUS_FIXED_CUSTOMER = "1";
    public static final String STATUS_FLUID_CUSTOMER = "2";

    //复核状态：区分新登记账户未复核、新登记账户已复核状态
    public static final String CHECK_STATUS_UNCHECKED = "0";
    public static final String CHECK_STATUS_CHECKED = "1";

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getStartDate() {
        return startDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getEndDate() {
        return endDate;
    }

    public String getStatusFlag(){
        if (status.equals(STATUS_FIXED_CUSTOMER)) return "固定客户";
        else if (status.equals(STATUS_FLUID_CUSTOMER)) return "流动客户";
        else return null;
    }
}
