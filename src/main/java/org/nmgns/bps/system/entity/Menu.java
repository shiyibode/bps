package org.nmgns.bps.system.entity;

import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseDataScopePageEntity;

import java.util.List;

@Data
public class Menu extends BaseDataScopePageEntity<Menu> {

    private Long parentId;
    private List<Long> parentIds;
    private String name;
    private String enName;
    private Integer sort;
    private String type;
    private String uri;
    private String locale;
    private String target;
    private String icon;
    private String isShow;
    private String permission;
    private String description;
    private String remarks;
    private Boolean delFlag;
    private String parentName;

    private String typeStr;




}
