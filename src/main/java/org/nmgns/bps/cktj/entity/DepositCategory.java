package org.nmgns.bps.cktj.entity;

import lombok.Data;
import org.nmgns.bps.system.utils.base.TreeEntity;

@Data
public class DepositCategory extends TreeEntity<DepositCategory> {

    private String no;
    private String name;
    private String depositType;
    private String customerType;
    private String accountType;
    private String belongTo;
    private String[] subjectNo;
    private Boolean validFlag;
    private String remarks;


    private String depositTypeStr;
    private String customerTypeStr;
    private String belongToStr;


//    public String getDepositTypeStr(){
//        if(null == depositType) return "";
//        String resultString;
//        switch (depositType){
//            case DepositDefaultConfig.DEPOSIT_CATEGORY_DEPOSIT_TYPE_WU: resultString = "";break;
//            case DepositDefaultConfig.DEPOSIT_CATEGORY_DEPOSIT_TYPE_HUOQI: resultString = "活期存款";break;
//            case DepositDefaultConfig.DEPOSIT_CATEGORY_DEPOSIT_TYPE_DINGQI: resultString = "定期存款";break;
//            case DepositDefaultConfig.DEPOSIT_CATEGORY_DEPOSIT_TYPE_TONGZHI: resultString = "通知存款";break;
//            case DepositDefaultConfig.DEPOSIT_CATEGORY_DEPOSIT_TYPE_DINGHUO: resultString = "定活两便存款";break;
//            case DepositDefaultConfig.DEPOSIT_CATEGORY_DEPOSIT_TYPE_BAOZHENGJIN: resultString = "保证金";break;
//            case DepositDefaultConfig.DEPOSIT_CATEGORY_DEPOSIT_TYPE_DAIJIESUANCAIZHENGKUAN: resultString = "待结算财政款";break;
//            case DepositDefaultConfig.DEPOSIT_CATEGORY_DEPOSIT_TYPE_DIFANGCAIZHENGKUKUAN: resultString = "地方财政库款";break;
//            case DepositDefaultConfig.DEPOSIT_CATEGORY_DEPOSIT_TYPE_YINGJIEHUIKUAN: resultString = "应解汇款";break;
//            case DepositDefaultConfig.DEPOSIT_CATEGORY_DEPOSIT_TYPE_DIANZIXIANJIN: resultString = "IC卡电子现金";break;
//
//            default: resultString = "其他";
//        }
//        return resultString;
//    }
//
//    public String getCustomerTypeStr(){
//        String resultString;
//        if (customerType == null) return "";
//        switch (customerType){
//            case DepositDefaultConfig.DEPOSIT_CATEGORY_CUSTOMER_TYPE_PERSON: resultString = "个人";break;
//            case DepositDefaultConfig.DEPOSIT_CATEGORY_CUSTOMER_TYPE_GROUP: resultString = "对公";break;
//
//            default: resultString = "错误的客户分类";
//        }
//
//        return resultString;
//    }
//
//    public String getBelongToStr(){
//        if (null == belongTo) return "";
//        String resultString;
//        switch (belongTo){
//            case DepositDefaultConfig.DEPOSIT_CATEGORY_BELONG_TO_EMPLOYEE_AND_ORG: resultString = "员工与机构";break;
//            case DepositDefaultConfig.DEPOSIT_CATEGORY_BELONG_TO_EMPLOYEE: resultString = "员工";break;
//            case DepositDefaultConfig.DEPOSIT_CATEGORY_BELONG_TO_ORG: resultString = "机构";break;
//
//            default: resultString = "错误的所属类型";
//        }
//
//        return resultString;
//    }

    public String getSubjectNoStr(){
        if (null == subjectNo) return "";
        StringBuilder sb = new StringBuilder();
        for (String s : subjectNo){
            sb.append(s);
            sb.append(",");
        }
        String str = sb.toString();
        if (str.length() > 1) return str.substring(0,str.length() - 1);
        return "";
    }
}
