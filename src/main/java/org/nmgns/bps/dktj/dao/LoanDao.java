package org.nmgns.bps.dktj.dao;

import org.apache.ibatis.annotations.Mapper;
import org.nmgns.bps.dktj.entity.Loan;

import java.util.List;

@Mapper
public interface LoanDao {

    List<Loan> findOrganizationLoanList(Loan loan);

    List<Loan> findOrganizationAvgLoanList(Loan loan);

    List<Loan> findEmployeeLoanList(Loan loan);

    List<Loan> findEmployeeAvgLoanList(Loan loan);

}
