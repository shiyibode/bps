package org.nmgns.bps.system.dao;


import org.apache.ibatis.annotations.Mapper;
import org.nmgns.bps.system.entity.Dictionary;
import org.nmgns.bps.system.entity.Organization;

import java.util.List;

@Mapper
public interface OrganizationDao {

    public Organization getOrganizationByCode(String code);

    public Organization getOrganizationById(Long id);

    public void update(Organization organization);

    public void insert(Organization organization);

    public Long getCount(Organization organization);

    public List<Organization> get(Organization organization);

    public void delete(Long id);

    public List<Organization> getOrgAndLowerLevelOrgListById(Long organizationId);

    public List<Organization> getUpperLevelOrgListById(Long organizationId);

    public List<Dictionary> getOrganizationTypeList();

    public Organization getSubBranchByOrgCode(String orgCode);

    public Organization getBranchByOrgCode(String orgCode);
}
