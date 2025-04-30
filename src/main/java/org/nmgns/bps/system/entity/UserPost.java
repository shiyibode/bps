package org.nmgns.bps.system.entity;

import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseEntity;

import java.util.Date;

@Data
public class UserPost extends BaseEntity {

    private Long userId;
    private Date startDate;
    private Date endDate;
    private String post;
    private Boolean validFlag;
    private Long parentId;
    private String remarks;


}
