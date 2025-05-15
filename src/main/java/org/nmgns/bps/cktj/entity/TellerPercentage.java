package org.nmgns.bps.cktj.entity;

import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseEntity;

@Data
public class TellerPercentage extends BaseEntity {

    private Long empAcctId;
    private String tellerCode;
    private String tellerName;
    private Double percentage;

    private Boolean mainTeller;


}
