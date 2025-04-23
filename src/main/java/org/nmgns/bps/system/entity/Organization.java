package org.nmgns.bps.system.entity;

import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseDataScopePageEntity;
import org.nmgns.bps.system.utils.base.BaseEntity;

import java.util.List;

@Data
public class Organization extends BaseDataScopePageEntity<Organization> {

    private Long parentId;
    private List<Long> parentIds;
    private String code;
    private String name;
    private  Integer sort;
    private String icon;
    private Long areaId;
    private String type;
    private Integer grade;
    private String address;
    private String representative;
    private String phone;
    private Boolean usable;
    private Long primaryUserId;
    private Long deputyUserId;
    private String remarks;
    private Boolean delFlag;

    private String parentName;
    private String typeStr;     //type对应的中文


}
