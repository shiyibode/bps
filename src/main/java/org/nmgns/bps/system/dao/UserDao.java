package org.nmgns.bps.system.dao;


import org.apache.ibatis.annotations.Mapper;
import org.nmgns.bps.system.entity.User;
import org.nmgns.bps.system.entity.UserOrganization;
import org.nmgns.bps.system.entity.UserStatus;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserDao {

    public List<User> findAll();

    public User findById(Long id);

    public User getUserByCode(String code);

    public User getUserById(Long id);

    public void insert(User user);

    public void update(User user);

    public void delete(Long id);

    public Long insertUserOrganization(UserOrganization uo);

    public UserOrganization getUserOrganizationById(Long id);

    public void updateUserOrganizationById(UserOrganization uo);

    public void deleteUserOrganizationById(Long id);

    public UserOrganization getValidUserOrganizationByUserId(Long userId);

    public Long getCount(User user);

    public List<User> get(User user);

    public void insertUserStatus(UserStatus us);

    public UserStatus getValidUserStatusByUserId(Long userId);

    public void updateUserStatusById(UserStatus us);

    public List<User> getTenUsers(User user);

    public Date getMaxDepositCurrDate();

}
