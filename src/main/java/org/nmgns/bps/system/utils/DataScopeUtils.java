package org.nmgns.bps.system.utils;

import cn.hutool.core.util.StrUtil;
import org.nmgns.bps.system.entity.RoleApi;
import org.nmgns.bps.system.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据范围过滤工具类
 */
public class DataScopeUtils {

    /**
     * 数据范围过滤
     * @param apiId 当前要进行数据范围过滤的接口
     * @param user 当前用户对象
     * @param organizationAlias 机构表别名，多个用“,”逗号隔开。
     * @param userAlias 用户表别名，多个用“,”逗号隔开，传递空，忽略此参数
     */
    //user中必须设置roleApiList、organizationId(用户当前在职机构id)、id(用户id)
    public static String dataScopeFilter(Long apiId, User user, String organizationAlias, String userAlias) {

        StringBuilder sqlString = new StringBuilder();

        // 进行权限过滤，多个角色权限范围之间为或者关系。
        List<String> dataScope = new ArrayList<>();

        // 超级管理员跳过权限过滤，普通用户添加过滤条件
        if (!user.isAdmin()){
            boolean isDataScopeAll = false;
            for (RoleApi ra : user.getRoleApiList()){
                // 用户没有这个接口权限，直接跳过
                if (!ra.getApiId().equals(apiId)) continue;

                // 客户拥有这个接口，并指定了接口的权限范围
                // 指定了机构别名
                for (String oa : StrUtil.split(organizationAlias, ",")){
                    //
                    if (!dataScope.contains(ra.getDataScope()) && StrUtil.isNotBlank(oa)){
                        if (DefaultConfig.DATA_SCOPE_ALL.equals(ra.getDataScope())){
                            isDataScopeAll = true;
                        }
                        else if (DefaultConfig.DATA_SCOPE_CUSTOM.equals(ra.getDataScope())){
                            sqlString.append(" OR EXISTS (SELECT 1 FROM ").append(DefaultConfig.SCHEMA).append("t_sys_role_permission_organization WHERE role_permission_id = ").append(ra.getId());
//                            sqlString.append(" OR EXISTS (SELECT 1 FROM ").append(DefaultConfig.SCHEMA).append("t_sys_role_permission_organization WHERE role_id = ").append(ra.getRoleId());
//                            sqlString.append(" AND api_id = ").append(apiId);
                            sqlString.append(" AND organization_id = ").append(oa).append(".id)");
                        }
                        else if (DefaultConfig.DATA_SCOPE_ORGANIZATION_AND_CHILD.equals(ra.getDataScope())){
                            String orgId = user.getOrganizationId().toString();
                            sqlString.append(" OR ").append(oa).append(".id = ").append(user.getOrganizationId());
                            sqlString.append(" OR ").append(oa).append(".parent_ids @> ARRAY[").append(orgId).append("]");
                        }
                        else if (DefaultConfig.DATA_SCOPE_ORGANIZATION.equals(ra.getDataScope())){
                            sqlString.append(" OR ").append(oa).append(".id = ").append(user.getOrganizationId());
                        }
                        else if (DefaultConfig.DATA_SCOPE_SELF.equals(ra.getDataScope())){}
                        else {}
                    }
                }
                dataScope.add(ra.getDataScope());
            }
            // 如果没有全部数据权限，并设置了用户别名，则当前权限为本人；如果未设置别名，当前权限为已植入权限
            if (!isDataScopeAll){
                if (StrUtil.isNotBlank(userAlias)){
                    for (String ua : StrUtil.split(userAlias, ",")){
                        sqlString.append(" OR ").append(ua).append(".id = ").append(user.getId());
                    }
                }else {
                    for (String oa : StrUtil.split(organizationAlias, ",")){
                        sqlString.append(" OR ").append(oa).append(".id IS NULL");
                    }
                }
            }else{
                // 如果包含全部权限，则去掉之前添加的所有条件，并跳出循环。
                sqlString = new StringBuilder();
            }
        }
        if (StrUtil.isNotBlank(sqlString.toString())){
            return " AND (" + sqlString.substring(4) + ")";
        }
        return "";
    }




}

