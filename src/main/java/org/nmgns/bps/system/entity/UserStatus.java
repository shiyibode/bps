package org.nmgns.bps.system.entity;

import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseEntity;

import java.util.Date;

@Data
public class UserStatus extends BaseEntity {

    private Long userId;
    private Date startDate;
    private Date endDate;
    private String status;
    private Boolean validFlag;
    private Long parentId;
    private String remarks;


}
