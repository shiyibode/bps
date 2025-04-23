package org.nmgns.bps.system.utils.base;

import java.util.List;

public abstract class TreeEntity<T> extends BaseEntity{

    protected Long parentId;
    protected Long[] parentIds;
    protected Integer sort;
    protected List<T> children;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long[] getParentIds() {
        return parentIds;
    }

    public void setParentIds(Long[] parentIds) {
        this.parentIds = parentIds;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }

    public Boolean getLeaf(){
        List children = getChildren();
        if (children != null && children.size() > 0) return false;
        else return true;
    }

}
