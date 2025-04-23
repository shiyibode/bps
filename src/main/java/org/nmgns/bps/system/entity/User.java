package org.nmgns.bps.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseDataScopePageEntity;

import java.util.Date;
import java.util.List;

@Data
public class User extends BaseDataScopePageEntity<User> {

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
    @JsonIgnore
    private Boolean loginUsable;
    @JsonIgnore
    private String loginIp;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;
    private String remarks;
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
    @JsonIgnore
    private Date startDate;     //用户在机构的入职时间


    private String postStr;
    private String statusStr;

}
