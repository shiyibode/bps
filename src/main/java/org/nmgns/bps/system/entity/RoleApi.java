package org.nmgns.bps.system.entity;

import lombok.Data;
import org.nmgns.bps.system.utils.base.BasePageEntity;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoleApi extends BasePageEntity<RoleApi> {

    private Long id;
    private Role role;
    private Api api;
    private String dataScope;
    private String dataScopeStr;

    //以下属性用来搜素
    private String roleName;
    private String apiName;
    private List<Organization> organizationList;

    public List<Long> getOrganizationIdList(){
        if (organizationList == null) organizationList = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (Organization organization : organizationList) {
            ids.add(organization.getId());
        }
        return ids;
    }

    public void setOrganizationIdList(List<Long> organizationIdList){
        List<Organization> organizations = new ArrayList<>();
        for(Long orgId : organizationIdList){
            Organization organization = new Organization();
            organization.setId(orgId);
            organizations.add(organization);
        }
        organizationList = organizations;
    }

    public String getApiUri(){
        if (null != api) return api.getUri();
        return null;
    }

    public String getApiPermission(){
        if (null != api) return api.getPermission();
        return null;
    }

    public String getRoleName(){
        if (null != role) return role.getName();
        return null;
    }

    public String getApiName(){
        if (null != api) return api.getName();
        return null;
    }

    public void setApiId(Long apiId){
        if (null == api) api = new Api();
        api.setId(apiId);
    }

    public void setRoleId(Long roleId){
        if (null == role) role = new Role();
        role.setId(roleId);
    }

    public Long getRoleId(){
        if (null != role) return role.getId();
        return null;
    }

    public Long getApiId(){
        if (null != api) return api.getId();
        return null;
    }
}
