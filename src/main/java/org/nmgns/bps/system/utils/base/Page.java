package org.nmgns.bps.system.utils.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.nmgns.bps.system.utils.DefaultConfig;

import java.io.Serializable;
import java.util.regex.Pattern;

public class Page implements Serializable {

    @Getter
    @Setter
    private int pageNo ; // 当前页码

    @Getter
    private int pageSize; // 页面大小，设置为“-1”表示不进行分页（分页无效）

    @Getter
    private long total;// 总记录数，设置为“-1”表示不查询总数


    @Setter
    private String orderBy = ""; // 标准查询有效， 实例： updatedate desc, text asc

    public Page() {
        this.pageNo = 1;
        this.pageSize = -1;
    }


    /**
     * 构造方法
     * @param pageNo 当前页码
     * @param pageSize 分页大小
     */
    public Page(int pageNo, int pageSize) {
        this(pageNo, pageSize, 0L);
    }

    /**
     * 构造方法
     * @param pageNo 当前页码
     * @param pageSize 分页大小
     * @param total 数据条数
     */
    public Page(int pageNo, int pageSize, long total) {
        this.setTotal(total);
        this.setPageNo(pageNo);
        this.pageSize = pageSize;
    }

    /**
     * 设置数据总数
     * @param total
     */
    public void setTotal(long total) {
        this.total = total;
        if (pageSize >= total){
            pageNo = 1;
        }
    }

    /**
     * 设置页面大小（最大500）
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize <= 0 ? DefaultConfig.DEFAULT_PAGE_SIZE : pageSize;// > 500 ? 500 : pageSize;
    }


    @JsonIgnore
    public int getStart() {
        int start = (getPageNo() - 1) * getPageSize();
        return start;
    }

    /**
     * 获取查询排序字符串
     * @return
     */
    @JsonIgnore
    public String getOrderBy() {
        // SQL过滤，防止注入
        String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
                + "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
        Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        if (sqlPattern.matcher(orderBy).find()) {
            return "";
        }
        return orderBy;
    }


}