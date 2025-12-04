package org.nmgns.bps.dktj.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.nmgns.bps.dktj.entity.EmployeeCustomer;

@JsonIgnoreProperties(value = {"isNewRecord", "isAutoGenId", "validFlag", "endDate", "opTellerCode", "createTime", "checkTellerCode", "checkTime", "parentId", "remarks"})
public class EmployeeUnboundCustomer extends EmployeeCustomer {

}
