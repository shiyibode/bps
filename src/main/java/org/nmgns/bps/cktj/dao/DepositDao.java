package org.nmgns.bps.cktj.dao;

import org.apache.ibatis.annotations.Mapper;
import org.nmgns.bps.cktj.entity.Deposit;
import org.nmgns.bps.cktj.entity.DepositCategory;

import java.util.List;

@Mapper
public interface DepositDao {

    List<Deposit> findEmployeeTaskDepositList(Deposit deposit);

    Long findTaskEmployeePageCount(Deposit deposit);

    List<String> findTaskEmployeePage(Deposit deposit);

    List<Deposit> findEmployeeAvgTaskDepositList(Deposit deposit);

    List<Deposit> findEmployeePaymentDepositList(Deposit deposit);

    Long findPaymentEmployeePageCount(Deposit deposit);

    List<String> findPaymentEmployeePage(Deposit deposit);

    List<Deposit> findEmployeeAvgPaymentDepositList(Deposit deposit);




}
