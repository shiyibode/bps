package org.nmgns.bps.cktj.dao;

import org.apache.ibatis.annotations.Mapper;
import org.nmgns.bps.cktj.entity.Deposit;
import org.nmgns.bps.cktj.entity.DepositCategory;

import java.util.List;

@Mapper
public interface DepositDao {

    List<Deposit> findEmployeeDepositList(Deposit deposit);

    Long findTaskEmployeePageCount(Deposit deposit);

    List<String> findTaskEmployeePage(Deposit deposit);




}
