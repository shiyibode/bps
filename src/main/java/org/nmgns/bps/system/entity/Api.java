package org.nmgns.bps.system.entity;

import lombok.Data;
import org.nmgns.bps.system.utils.base.BasePageEntity;

@Data
public class Api extends BasePageEntity<Api> {

    private String name;
    private String uri;
    private String permission;
    private String remarks;



}
