package org.nmgns.bps.cktj.dao;

import org.apache.ibatis.annotations.Mapper;
import org.nmgns.bps.cktj.entity.AutoBindRule;
import org.nmgns.bps.cktj.entity.EmployeeAccount;
import org.nmgns.bps.cktj.entity.TellerPercentage;

import java.util.List;

@Mapper
public interface EmployeeAccountDao {

    public Long findUnregisterAccountCount(EmployeeAccount ea);

    public List<EmployeeAccount> findUnregisterAccount(EmployeeAccount ea);

    public void insertAutoBindRule(AutoBindRule autoBindRule);

    public void updateAutoBindRule(AutoBindRule autoBindRule);

    public EmployeeAccount getUnboundAccountInfoById(Long id);

    public void insertTask(EmployeeAccount ea);

    public void insertPayment(EmployeeAccount ea);

    public void deleteUnboundAccountById(Long id);

    public EmployeeAccount getById(Long id);

    public Long findRegisterUncheckedAccountCount(EmployeeAccount ea);

    public List<EmployeeAccount> findRegisterUncheckedAccount(EmployeeAccount ea);

    public List<TellerPercentage> getTellerTaskPercentageListByAccountNoAndChildAccountNo(TellerPercentage tp);

    public List<TellerPercentage> getTellerPaymentPercentageListByAccountNoAndChildAccountNo(TellerPercentage tp);

    public void updateTaskById(TellerPercentage tp);

    public void updatePaymentById(TellerPercentage tp);

    public void deleteTask(Long id);

    public void deletePayment(Long id);

    public void insertUnboundEmployeeAccount(EmployeeAccount ea);

    public EmployeeAccount getEmployeeAccountByAccountNoAndChildAccountNoFromTask(TellerPercentage tp);

    public EmployeeAccount getEmployeeAccountByAccountNoAndChildAccountNoFromPayment(TellerPercentage tp);

    public Long findTaskModifiableAccountCount(EmployeeAccount ea);

    public List<EmployeeAccount> findTaskModifiableAccount(EmployeeAccount ea);

    public Long findPaymentModifiableAccountCount(EmployeeAccount ea);

    public List<EmployeeAccount> findPaymentModifiableAccount(EmployeeAccount ea);

    public Long findModifiedUncheckedAccountTaskCount(EmployeeAccount ea);

    public List<EmployeeAccount> findModifiedUncheckedAccountTask(EmployeeAccount ea);

    public Long findModifiedUncheckedAccountPaymentCount(EmployeeAccount ea);

    public List<EmployeeAccount> findModifiedUncheckedAccountPayment(EmployeeAccount ea);

    public Long getTellerTaskPercentageListCount(TellerPercentage tp);

    public List<TellerPercentage> getTellerTaskPercentageList(TellerPercentage tp);

    public Long getTellerPaymentPercentageListCount(TellerPercentage tp);

    public List<TellerPercentage> getTellerPaymentPercentageList(TellerPercentage tp);

}
