package org.nmgns.bps.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseDataScopePageEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class User extends BaseDataScopePageEntity<User> {

    public User(){}

    public User(String code){
        this.code = code;
    }

    private String code;
    @JsonIgnore
    private String loginPassword;
    private String name;
    private String phone;
    private String mobile;
    private String type;
    private String avatar;
    private String identityNo;
    private Date birthday;
    private String sex;
    private Date entryDate;
    private String post;
    private String status;
    private Boolean loginUsable;
    @JsonIgnore
    private String loginIp;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;
    private String remarks;
    private String userOrganizationRemarks;
    @JsonIgnore
    private Boolean adminFlag;
    @JsonIgnore
    private Boolean delFlag;

    private List<TreeMenu> userMenuList;
    public Boolean isAdmin(){
        return adminFlag;
    }

    @JsonIgnore
    private List<Role> roleList;
    @JsonIgnore
    private List<RoleApi> roleApiList;
    private Long organizationId;
    private UserOrganization currentUserOrganization;  //用户当前的在职机构
    @JsonIgnore
    private Date startDate;     //用户在机构的入职时间


    private String postStr;
    private String statusStr;


    // 用于前端展示-机构号
    public String getOrganizationCode(){
        if (null == currentUserOrganization) return null;
        else return currentUserOrganization.getOrganizationCode();
    }

    // 用于前端展示-机构名称
    public String getOrganizationName(){
        if (null == currentUserOrganization) return null;
        else return currentUserOrganization.getOrganizationName();
    }

    // 用于前端展示-进入当前机构的时间
    public Date getOrganizationStartDate(){
        if (null == currentUserOrganization) return null;
        else return currentUserOrganization.getStartDate();
    }

    // 用于前端展示-离开当前机构的时间
    public Date getOrganizationEndDate(){
        if (null == currentUserOrganization) return null;
        else return currentUserOrganization.getEndDate();
    }

    // 用于前端展示-在当前机构的是否在职
    public Boolean getValidFlag(){
        if (null == currentUserOrganization) return null;
        else return currentUserOrganization.getValidFlag();
    }

    // 用于前端展示
    public Date getUserOrganizationCreateTime(){
        if (null == currentUserOrganization) return null;
        else return currentUserOrganization.getCreateTime();
    }

    // 用于前端展示
    public Date getUserOrganizationUpdateTime(){
        if (null == currentUserOrganization) return null;
        else return currentUserOrganization.getUpdateTime();
    }

    // 获取角色id列表，用于前端设置角色
    public List<Long> getUserRoleIdList(){
        if (null == roleList) return new ArrayList<>();
        else {
            List<Long> roleIdList = new ArrayList<>();
            for (Role role : roleList) {
                roleIdList.add(role.getId());
            }
            return roleIdList;
        }
    }

}
