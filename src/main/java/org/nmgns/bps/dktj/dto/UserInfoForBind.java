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

    private String statusStr;

}
