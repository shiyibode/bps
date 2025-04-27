package org.nmgns.bps.cktj.entity;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseDataScopePageEntity;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Deposit extends BaseDataScopePageEntity<Deposit> {

    private Date date;
    private String tellerCode;
    private String tellerName;
    private String tellerOrgCode;
    private String tellerOrgName;
    private String dpOrgCode;
    private String dpOrgName;
    private String belongOrgCode;
    private String belongOrgName;
    private String parentCategoryName;
    private Integer dpCategoryId;
    private String dpCategoryNo;
    private String dpCategoryName;
    private BigDecimal balance;
    private BigDecimal prvisnInt;
    private BigDecimal ttlPayInt;
    private BigDecimal dayPayInt;
//    private Short dpType;   //存款类型: 0-机构存款;1-调离存款;2-调入存款


    //以下属性用于搜索
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private Integer avgDays;
    private Long organizationId;
    private String depositType;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getDate() {
        return date;
    }

    @JsonIgnore
    public String getStartDateStr() {
        return DateUtil.formatDate(this.getStartDate());
    }

    @JsonIgnore
    public String getEndDateStr() {
        return DateUtil.formatDate(this.getEndDate());
    }

    public Integer getAvgDays() {
        if (avgDays == null || avgDays <= 0) {
            if (getStartDate() != null && getEndDate() != null) {
                avgDays = (int)DateUtil.between(this.getStartDate(), this.getEndDate(), DateUnit.DAY) + 1;
            }
        }
        return avgDays;
    }



}
