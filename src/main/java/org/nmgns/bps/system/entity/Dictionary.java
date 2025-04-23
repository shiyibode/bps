package org.nmgns.bps.system.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Dictionary implements Serializable {

    private Integer sort;
    private String code;
    private String name;
    private Date createTime;


}
