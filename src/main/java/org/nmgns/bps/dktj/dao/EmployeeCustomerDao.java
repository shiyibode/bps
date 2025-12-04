package org.nmgns.bps.dktj.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nmgns.bps.dktj.dto.CustomerOrgAndStatus;
import org.nmgns.bps.dktj.dto.CustomerStatus;
import org.nmgns.bps.dktj.dto.UserInfoForBind;
import org.nmgns.bps.dktj.entity.AccountShareInfo;
import org.nmgns.bps.dktj.entity.AccountTemplate;
import org.nmgns.bps.dktj.entity.EmployeeCustomer;
import org.nmgns.bps.dktj.entity.SpecialAccountType;

import java.util.List;

@Mapper
public interface EmployeeCustomerDao {

    Long findUnregisterCustomerCount(EmployeeCustomer eup);

    List<EmployeeCustomer> findUnregisterCustomer(EmployeeCustomer eup);

//    EmployeeCustomer getUnboundCustomerInfoByXdCustomerNo(String orgCode, String xdCustomerNo);

    EmployeeCustomer getUnboundCustomerInfoByAccountNo(String orgCode, String accountNo);

    void deleteUnboundCustomerInfoByXdCustomerNo(String orgCode, String xdCustomerNo,String accountNo);

    Long findRegisterUncheckedCustomerCount(EmployeeCustomer ec);

    List<EmployeeCustomer> findRegisterUncheckedCustomer(EmployeeCustomer ec);

    int checkRegisterEmployee(EmployeeCustomer ec);

    void deleteRegisterEmployee(EmployeeCustomer ec);

    void deleteCustomerStatus(EmployeeCustomer ec);

    void insertUnboundCustomer(EmployeeCustomer euc);

    EmployeeCustomer getUncheckedCustomerInfoByAccountNo(EmployeeCustomer ec);

    Long findModifiableCustomerCount(EmployeeCustomer ec);

    List<EmployeeCustomer> findModifiableCustomer(EmployeeCustomer ec);

//    List<EmployeeCustomer> getValidEmployeeCustomerInfoListByXdCustomerNo(EmployeeCustomer ec);

//    EmployeeCustomer getValidEmployeeCustomerInfoByXdCustomerNo(EmployeeCustomer ec);

//    List<EmployeeCustomer> getAlterUncheckedEmployeeCustomerInfoListByXdCustomerNo(EmployeeCustomer ec);

    void updateById(EmployeeCustomer ec);

    void insertEmployeeCustomer(EmployeeCustomer ec);

    Long findModifiedUncheckedCustomerCount(EmployeeCustomer ec);

    List<EmployeeCustomer> findModifiedUncheckedCustomer(EmployeeCustomer ec);

    EmployeeCustomer getEmployeeCustomerById(Long id);

    void deleteById(Long id);

    String getUserCurrentOrganizationCodeByUserCode(String tellerCode);

    void insertCustomerOrgAndStatus(CustomerStatus coas);

    void updateValidCustomerOrgAndStatusById(CustomerStatus coas);

//    void deleteCustomerOrgAndStatusByXdCustomerNo(String xdCustomerNo);

    void updateCustomerOrgAndStatusByXdCustomerNoWithNull(CustomerOrgAndStatus coas);

    CustomerStatus getValidCustomerStatusByXdCustomerNoAndOrg(CustomerStatus cs);

    Long findOrgEmployeeCustomerCount(CustomerStatus aec);

    List<CustomerStatus> findOrgEmployeeCustomer(CustomerStatus aec);

    List<UserInfoForBind> findUserList(@Param("userCodeOrName") String userCodeOrName);

    Integer getAccountCount(@Param("xdCustomerNo") String xdCustomerNo,@Param("orgCode") String orgCode);

    EmployeeCustomer getAccountInfoByAccountNo(EmployeeCustomer ec);

    List<EmployeeCustomer> getUncheckedCustomerAccountByXdCustomerNoAndOrg(EmployeeCustomer cs);

    List<EmployeeCustomer> getCustomerAccountByXdCustomerNoAndOrg(EmployeeCustomer cs);

    Boolean changeStatusToFix(EmployeeCustomer ec);

    List<CustomerStatus> getCustomerStatus(CustomerStatus cs);

    CustomerStatus getUncheckedCustomerStatusByXdCustomerNoAndOrg(CustomerStatus cs);

    AccountTemplate getUncheckedAccountTemplateByAccountNoAndOrg(AccountTemplate at);

    void updateAccountTemplateById(AccountTemplate at);

    List<AccountShareInfo> getUncheckedAccountShareInfoByAccountNoAndOrg(AccountShareInfo as);

    void updateAccountShareInfoById(AccountShareInfo as);

    void deleteAccountShareInfoById(AccountShareInfo as);

    EmployeeCustomer getLoanHandleCurrentDate(EmployeeCustomer ec);

    Integer getValidAccountTemplateByAccountNoAndOrgCode(EmployeeCustomer ec);

    List<SpecialAccountType> getSpecialAccountTypeList();

    void insertSpecialAccountType(SpecialAccountType sat);

    void deleteSpecialAccountTypeById(Long id);

    SpecialAccountType getRegisterUncheckSpecialAccountType(SpecialAccountType sat);

    void updateSpecialAccountType(SpecialAccountType sat);

    EmployeeCustomer getValidEmployeeCustomerByAccountNoAndOrg(EmployeeCustomer ec);

    Long findCount(EmployeeCustomer ec);

    List<EmployeeCustomer> findList(EmployeeCustomer ec);
}
