package org.nmgns.bps.cktj.dao;


import org.apache.ibatis.annotations.Mapper;
import org.nmgns.bps.system.entity.Dictionary;

import java.util.List;

@Mapper
public interface BranchDictionaryDao {

    public Dictionary getByCode(String code);

    public List<Dictionary> getListByPrefixCode(String code);

    public List<Dictionary> getListBySuffixCode(String code);

    public List<Dictionary> getListByPrefixAndSuffixCode(String code);

}
