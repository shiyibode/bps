package org.nmgns.bps.dktj.entity;

import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseEntity;

@Data
public class TellerPercentage extends BaseEntity {

    private Long empCstId;
    private String tellerCode;
    private String tellerName;
    private Double percentage;


}
