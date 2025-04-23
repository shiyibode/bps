package org.nmgns.bps.cktj.utils;

import java.util.HashMap;
import java.util.Map;

public class TypeString {
    private static final Map<String, String> CUSTOMER_TYPE_STR = new HashMap<>();
    static {
        CUSTOMER_TYPE_STR.put("1", "个人");
        CUSTOMER_TYPE_STR.put("2", "对公");
    }

    private static final Map<String, String> CUSTOMER_SUB_TYPE_STR = new HashMap<>();
    static {
        CUSTOMER_SUB_TYPE_STR.put("1", "个人");
        CUSTOMER_SUB_TYPE_STR.put("2", "行政单位");
        CUSTOMER_SUB_TYPE_STR.put("3", "事业单位");
        CUSTOMER_SUB_TYPE_STR.put("4", "企业单位");
    }

    private static final Map<String, String> IDENTITY_TYPE_STR = new HashMap<>();
    static {
        IDENTITY_TYPE_STR.put("1", "公民身份证");
        IDENTITY_TYPE_STR.put("2", "户口簿");
        IDENTITY_TYPE_STR.put("3", "军人身份证");
        IDENTITY_TYPE_STR.put("4", "武警身份证");
        IDENTITY_TYPE_STR.put("5", "护照");
        IDENTITY_TYPE_STR.put("6", "港澳台同胞回乡证");
        IDENTITY_TYPE_STR.put("9", "其他个人有效证件");
        IDENTITY_TYPE_STR.put("A", "营业执照");
        IDENTITY_TYPE_STR.put("B", "开户批文");
        IDENTITY_TYPE_STR.put("C", "开户证明");
        IDENTITY_TYPE_STR.put("D", "开户登记证书");
        IDENTITY_TYPE_STR.put("Z", "其他对公有效证件");
    }

    private static final Map<String, String> BELONG_TO_STR = new HashMap<>();
    static {
        BELONG_TO_STR.put("0", "员工和机构");
        BELONG_TO_STR.put("1", "仅揽储人");
        BELONG_TO_STR.put("2", "仅机构");
    }

    private static final Map<String, String> DEPOSIT_TYPE_STR = new HashMap<>();
    static {
        DEPOSIT_TYPE_STR.put("0", "");
        DEPOSIT_TYPE_STR.put("1", "活期存款");
        DEPOSIT_TYPE_STR.put("2", "定期存款");
        DEPOSIT_TYPE_STR.put("3", "通知存款");
        DEPOSIT_TYPE_STR.put("4", "定活两便");
        DEPOSIT_TYPE_STR.put("5", "保证金");
        DEPOSIT_TYPE_STR.put("6", "财政款项");
        DEPOSIT_TYPE_STR.put("7", "财政库款");
        DEPOSIT_TYPE_STR.put("8", "应解汇款");
    }

    private static final Map<String, String> ACCOUNT_TYPE_STR = new HashMap<>();
    static {
        ACCOUNT_TYPE_STR.put("1", "存款账户");
        ACCOUNT_TYPE_STR.put("2", "内部账户");
    }

    private static final Map<String, String> CLOSE_ACCT_FLAG_STR = new HashMap<>();
    static {
        CLOSE_ACCT_FLAG_STR.put("0", "正常");
        CLOSE_ACCT_FLAG_STR.put("1", "销户");
        CLOSE_ACCT_FLAG_STR.put("3", "不动户");
    }

    public static final String getCustomerTypeStr(String customerType) {
        String s = CUSTOMER_TYPE_STR.get(customerType);
        if (s == null || s.length() == 0) {
            return "";
        }
        return s;
    }


    public static final String getCustomerSubTypeStr(String customerSubType) {
        String s = CUSTOMER_SUB_TYPE_STR.get(customerSubType);
        if (s == null || s.length() == 0) {
            return "";
        }
        return s;
    }

    public static final String getIdentityTypeStr(String identityType) {
        String s = IDENTITY_TYPE_STR.get(identityType);
        if (s == null || s.length() == 0) {
            return "未知";
        }
        return s;
    }

    public static final String getBelongToStr(String belongTo) {
        String s = BELONG_TO_STR.get(belongTo);
        if (s == null || s.length() == 0) {
            return "";
        }
        return s;
    }

    public static final String getDepositTypeStr(String depositType) {
        String s = DEPOSIT_TYPE_STR.get(depositType);
        if (s == null || s.length() == 0) {
            return "";
        }
        return s;
    }

    public static final String getAccountTypeStr(String accountType) {
        String s = ACCOUNT_TYPE_STR.get(accountType);
        if (s == null || s.length() == 0) {
            return "";
        }
        return s;
    }

    public static final String getCloseAcctFlagStr(String closeAcctFlag) {
        String s = CLOSE_ACCT_FLAG_STR.get(closeAcctFlag);
        if (s == null || s.length() == 0) {
            return "";
        }
        return s;
    }
}
