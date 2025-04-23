package org.nmgns.bps.system.utils.base;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;


public class BasePageEntity<T> extends BaseEntity {

    @JsonIgnore
    @Setter
    protected Page page;

    public Page getPage() {
        if (page == null) {page = new Page();}
        return page;
    }

    // 保存前端上传的分页信息
    @JsonProperty("page")
    public void setPageNo(int pageNo) {
        this.getPage().setPageNo(pageNo);
    }

    public int getPageNo() {
        return this.getPage().getPageNo();
    }

}
