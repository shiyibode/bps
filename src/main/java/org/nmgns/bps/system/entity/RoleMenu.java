package org.nmgns.bps.system.entity;

import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseEntity;
import org.nmgns.bps.system.utils.base.BasePageEntity;

@Data
public class RoleMenu extends BasePageEntity<RoleMenu> {

    private Menu menu;
    private Role role;
    private Boolean isShow;

    public Long getMenuId(){
        if (menu == null) return null;
        else return menu.getId();
    }

    public void setMenuId(Long menuId){
        if (menu == null) menu = new Menu();
        menu.setId(menuId);
    }

    public Long getRoleId(){
        if (role == null) return null;
        else return role.getId();
    }

    public void setRoleId(Long roleId){
        if (role == null) role = new Role();
        role.setId(roleId);
    }

    public void setRoleName(String roleName){
        if (null == role) role = new Role();
        role.setName(roleName);
    }

    public void setMenuName(String menuName){
        if (null == menu) menu = new Menu();
        menu.setName(menuName);
    }

    public String getRoleName(){
        if (null == role) return null;
        else return role.getName();
    }

    public String getMenuName(){
        if (null == menu) return null;
        else return menu.getName();
    }

    public String getMenuTarget(){
        if (null == menu) return null;
        else return menu.getTarget();
    }

    public String getMenuIcon(){
        if (null == menu) return null;
        else return menu.getIcon();
    }

    public String getMenuDescription(){
        if (null == menu) return null;
        else return menu.getDescription();
    }

    public void setMenuDescription(String description){
        if (null == menu) menu = new Menu();
        menu.setDescription(description);
    }

}
