package org.nmgns.bps.cktj.dao;

import org.apache.ibatis.annotations.Mapper;
import org.nmgns.bps.cktj.entity.AutoBindRule;
import org.nmgns.bps.cktj.entity.BindLevel;
import org.nmgns.bps.cktj.entity.EmployeeAccount;
import org.nmgns.bps.cktj.entity.TellerPercentage;
import org.nmgns.bps.system.entity.Dictionary;

import java.util.List;

@Mapper
public interface EmployeeAccountDao {

    public Long findUnregisterAccountCount(EmployeeAccount ea);

    public List<EmployeeAccount> findUnregisterAccount(EmployeeAccount ea);

    public void insertAutoBindRule(AutoBindRule autoBindRule);

    public void updateAutoBindRule(AutoBindRule autoBindRule);

    public EmployeeAccount getUnboundAccountInfoById(Long id);

    public void insertTask(EmployeeAccount ea);

    public void insertTaskTellerPercentage(TellerPercentage tp);

    public void insertPayment(EmployeeAccount ea);

    public void insertPaymentTellerPercentage(TellerPercentage tp);

    public BindLevel getBindLevelByOrgAndFlag(BindLevel bl);

    public void deleteUnboundAccountById(Long id);

    public EmployeeAccount getEmployeeTaskById(Long id);

    public EmployeeAccount getEmployeePaymentById(Long id);

    public Long findRegisterUncheckedAccountCount(EmployeeAccount ea);

    public List<EmployeeAccount> findRegisterUncheckedAccount(EmployeeAccount ea);

    public List<TellerPercentage> getTellerTaskPercentageListByEmployeeAccountId(EmployeeAccount ea);

    public List<TellerPercentage> getTellerPaymentPercentageListByEmployeeAccountId(EmployeeAccount ea);

    public List<TellerPercentage> getTellerPaymentPercentageListByEmployeeAccountAndChildAccountNo(EmployeeAccount ea);

    public void updateTaskById(EmployeeAccount ea);

    public void updatePaymentById(EmployeeAccount ea);

    public void deleteTask(Long id);

    public void deleteTaskTellerPercentage(Long taskEmpAcctId);

    public void deletePaymentTellerPercentage(Long paymentEmpAcctId);

    public void deletePayment(Long id);

    public void insertUnboundEmployeeAccount(EmployeeAccount ea);

    public EmployeeAccount getEmployeeAccountByAccountNoAndChildAccountNoFromTask(EmployeeAccount ea);

    public EmployeeAccount getEmployeeAccountByAccountNoAndChildAccountNoFromPayment(EmployeeAccount ea);

    public Long findTaskModifiableAccountCount(EmployeeAccount ea);

    public List<EmployeeAccount> findTaskModifiableAccount(EmployeeAccount ea);

    public Long findPaymentModifiableAccountCount(EmployeeAccount ea);

    public List<EmployeeAccount> findPaymentModifiableAccount(EmployeeAccount ea);

    public Long findModifiedUncheckedAccountTaskCount(EmployeeAccount ea);

    public List<EmployeeAccount> findModifiedUncheckedAccountTask(EmployeeAccount ea);

    public Long findModifiedUncheckedAccountPaymentCount(EmployeeAccount ea);

    public List<EmployeeAccount> findModifiedUncheckedAccountPayment(EmployeeAccount ea);

    public List<Dictionary> getDepositAccountAutoBindRule();

}
