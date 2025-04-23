package org.nmgns.bps.system.utils;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageData<T> implements java.io.Serializable {

    private long pageNo;
    private long pageSize;
    private long total;
    private List<T> list;

    public List<T> getList() {
        if (list == null) {list = new ArrayList<T>();}
        return list;
    }
}
