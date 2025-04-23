package org.nmgns.bps.system.entity;

import lombok.Data;
import org.nmgns.bps.system.utils.DefaultConfig;
import org.nmgns.bps.system.utils.base.TreeEntity;

@Data
public class TreeMenu extends TreeEntity<Menu> {
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


    // Extjs前端使用text来显示菜单名称
    public String getText(){
        return name;
    }

    // 前端显示的菜单树，叶子节点是MENU_TYPE_MENU_MENU
    public Boolean getLeaf(){
        if (type != null && type.equals(DefaultConfig.MENU_TYPE_MENU_MENU)) return true;
        if (type != null && type.equals(DefaultConfig.MENU_TYPE_MENU_GROUP)) return false;
        if (type != null && type.equals(DefaultConfig.MENU_TYPE_MENU_ROOT)) return false;

        return null;
    }

    // Extjs使用iconCls来显示icon
    public String getIconCls(){
        return icon;
    }

    // Extjs使用viewType来做导航
    public String getViewType(){
        return target;
    }
}
