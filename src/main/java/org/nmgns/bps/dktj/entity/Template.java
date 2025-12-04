package org.nmgns.bps.dktj.entity;

import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseEntity;

import java.util.Date;

@Data
public class Template extends BaseEntity {

    private Integer sort;
    private Boolean validFlag;
    private Date invalidDate;
}
