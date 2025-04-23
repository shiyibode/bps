package org.nmgns.bps.cktj.entity;

import lombok.Data;
import org.nmgns.bps.system.utils.base.BaseDataScopePageEntity;

@Data
public class AutoBindRule extends BaseDataScopePageEntity<AutoBindRule> {

    private String orgCode;
    private String accountNo;
    private String childAccountNo;
    private String customerNo;
    private String level;

    private String levelStr;


}
