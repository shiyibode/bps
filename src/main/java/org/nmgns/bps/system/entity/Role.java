package org.nmgns.bps.system.entity;

import lombok.Data;
import org.nmgns.bps.system.utils.base.BasePageEntity;

import java.util.Date;

@Data
public class Role extends BasePageEntity<Role> {

    private String name;
    private String enName;
    private String roleType;
    private Boolean isSys;
    private Boolean usable;
    private String remarks;
    private Boolean delFlag;

}
