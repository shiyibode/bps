package org.nmgns.bps.system.entity;

import lombok.Data;
import org.nmgns.bps.system.utils.base.TreeEntity;

import java.util.List;

@Data
public class TreeOrganization extends TreeEntity<Organization> {

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

    public String getText(){
        return name;
    }




}
