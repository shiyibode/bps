package org.nmgns.bps.system.entity;

import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseEntity;

@Data
public class UserRole extends BaseEntity {
    private Long userId;
    private Long roleId;
}
