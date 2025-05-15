package org.nmgns.bps.cktj.entity;

import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseEntity;

@Data
public class BindLevel extends BaseEntity {

    public BindLevel(String orgCode, String taskPaymentFlag, String level){
        this.orgCode = orgCode;
        this.taskPaymentFlag = taskPaymentFlag;
        this.level = level;
    }

    private String orgCode;
    private String taskPaymentFlag;
    private String level;


}
