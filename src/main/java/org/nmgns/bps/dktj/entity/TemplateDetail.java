package org.nmgns.bps.dktj.entity;


import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseEntity;

@Data
public class TemplateDetail extends BaseEntity{

    private Template template;
    private Position position;
    private Float percentage;

    //用于存放该岗位最可能的柜员
    private String tellerCode;
    //存放当前贷款账号（要通过当前贷款账号获取该贷款客户最后一笔贷款的岗位维护人列表）
    private String accountNo;

    public void setTemplateId(Long templateId){
        if (template == null) template = new Template();
        template.setId(templateId);
    }

    public Long getTemplateId(){
        if (template != null) return template.getId();
        else return null;
    }

    public void setPositionId(Long positionId){
        if (position == null) position = new Position();
        position.setId(positionId);
    }

    public Long getPositionId(){
        if (position != null) return position.getId();
        else return null;
    }

    public String getPositionName() {
        if (position != null) return position.getName();
        else return null;
    }

    public String getPositionType() {
        if (position != null) return position.getType();
        else return null;
    }
}
