package org.nmgns.bps.cktj.dao;

import org.apache.ibatis.annotations.Mapper;
import org.nmgns.bps.cktj.entity.DepositCategory;

import java.util.List;

@Mapper
public interface DepositCategoryDao {



    public DepositCategory getById(Long id);

    public List<DepositCategory> getAll();

    public List<DepositCategory> getByParentId(Long parentId);

    public void insert(DepositCategory depositCategory);

    public void update(DepositCategory depositCategory);

    public void delete(DepositCategory depositCategory);









}
