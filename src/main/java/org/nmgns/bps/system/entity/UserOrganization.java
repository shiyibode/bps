package org.nmgns.bps.system.entity;

import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseEntity;

import java.util.Date;

@Data
public class UserOrganization extends BaseEntity {

    private Long userId;
    private Long organizationId;
    private Date startDate;
    private Date endDate;
    private String remarks;
    private Boolean validFlag;
    private Long parentId;

    private Long oldId;

    private String organizationName;
    private String organizationCode;
    private String userCodeOrName;

}
