package org.nmgns.bps.system.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Log implements Serializable {

    private Long id;
    private Long userId;
    private String operation;
    private String remarks;
    private Date createTime;
    private Long createBy;

    public Log(){}

    public Log(String operation, String remarks, Date createTime, Long createBy) {
        this.operation = operation;
        this.remarks = remarks;
        this.createTime = createTime;
        this.createBy = createBy;
    }


}
