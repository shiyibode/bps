package org.nmgns.bps.system.entity;

import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseEntity;

@Data
public class ApiPermission extends BaseEntity {

    private Api api;
    private String dataScope;

    public Long getApiId(){
        if (api == null) return null;
        else {
            return api.getId();
        }
    }
}
