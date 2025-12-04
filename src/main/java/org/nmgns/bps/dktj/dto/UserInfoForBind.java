package org.nmgns.bps.dktj.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class UserInfoForBind {

    private String userCode;
    private String userName;
    private String organizationName;
    private String organizationCode;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    public String getStatusStr() {
        switch (status){
            case "0": return "调离";
            case "1": return "在职";
            case "2": return "停职";
            case "3": return "辞职";
            case "4": return "辞退";
            case "5": return "退休";
            case "6": return "揽储";
            case "7": return "休假";

            default: return "无法识别的柜员在职状态";
        }
    }

}
