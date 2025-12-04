package org.nmgns.bps.dktj.entity;


import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseEntity;

@Data
public class Position extends BaseEntity {

    private String name;
    private String enName;
    private String type;    //岗位类型: 0-普通 1-推荐人

    public static final String POSITION_TYPE_PU_TONG = "0";
    public static final String POSITION_TYPE_TUI_JIAN_REN = "1";
}
