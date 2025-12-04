package org.nmgns.bps.dktj.dto;

import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseEntity;

import java.util.Date;

@Data
public class CustomerOrgAndStatus extends BaseEntity{

    private String xdCustomerNo;
    private String orgCode;
    private String status;
    private Date startDate;
    private Date endDate;
    private Long parentId;
    private Boolean validFlag;
}
