package org.nmgns.bps.system.utils.base;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BaseDataScopeEntity implements Serializable {

    protected Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createTime;
    @JsonIgnore
    protected Long createBy;
    protected Date updateTime;
    @JsonIgnore
    protected Long updateBy;

    //自定义SQL（SQL标识，SQL内容）
    @JsonIgnore
    protected Map<String, String> sqlMap;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Map<String, String> getSqlMap() {
        if (sqlMap == null) sqlMap = new HashMap<>();
        return sqlMap;
    }

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }
}
