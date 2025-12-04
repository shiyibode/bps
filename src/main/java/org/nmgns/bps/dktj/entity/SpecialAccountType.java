package org.nmgns.bps.dktj.entity;

import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseEntity;

import java.util.Date;

@Data
public class SpecialAccountType extends BaseEntity {

    //type需要的字段
    private String name;
    private String enName;
    private Boolean validFlag;

    //accountType需要的字段
    private String orgCode;
    private String accountNo;
    private Date startDate;
    private Date endDate;
    private Long typeId;
    private String checkStatus;
    private String alterCheckStatus;


    public static final String CHECK_STATUS_UNCHECKED = "0";
    public static final String CHECK_STATUS_CHECKED = "1";
}
