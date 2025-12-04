package org.nmgns.bps.dktj.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.nmgns.bps.dktj.entity.EmployeeCustomer;
import org.nmgns.bps.dktj.entity.EmployeeInterest;
import org.nmgns.bps.dktj.entity.InterestShareInfo;
import org.nmgns.bps.dktj.entity.Position;

import java.util.List;

@Mapper
public interface InterestShareInfoDao {

    List<InterestShareInfo> findOrgLoanInterest(InterestShareInfo isi);

    List<InterestShareInfo> findOrgAvgLoanInterest(InterestShareInfo isi);

    List<InterestShareInfo> findEmployeeLoanInterest(InterestShareInfo isi);

    List<InterestShareInfo> findEmployeeAvgLoanInterest(InterestShareInfo isi);

    Long findUnboundReaddAccountCount(EmployeeInterest ei);

    List<EmployeeInterest> findUnboundReaddAccount(EmployeeInterest ei);

    EmployeeInterest getReaddAccountInfoByAccountNo(EmployeeInterest ei);

    void deleteReaddAccountById(@Param("id")Long id);

    Integer getValidEmployeeAccountCount(EmployeeCustomer ec);

    Integer getEmployeeAccountCount(EmployeeCustomer ec);

    String getCurrentValidEmployeeCustomerTellerCode(EmployeeCustomer ec);

    Long getValidPositionTellerListCount(InterestShareInfo isi);

    List<InterestShareInfo> getValidPositionTellerList(InterestShareInfo isi);

    InterestShareInfo getAccountShareInfoById(@Param("id") Long id);

    Long getAlterUncheckedShareInfoCount(InterestShareInfo isi);

    List<InterestShareInfo> getAlterUncheckedShareInfo(InterestShareInfo isi);

    List<Position> getPositionList();

    Long findCount(InterestShareInfo isi);

    List<InterestShareInfo> findList(InterestShareInfo isi);
}
