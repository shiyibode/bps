package org.nmgns.bps.cktj.service;

import org.nmgns.bps.cktj.dao.BranchDictionaryDao;
import org.nmgns.bps.system.entity.Dictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class BranchDictionaryService {

    @Autowired
    private BranchDictionaryDao branchDictionaryDao;

    @Cacheable("branchDictionaryByCode")
    public Dictionary getDictionaryByCode(String code) {
        return branchDictionaryDao.getByCode(code);
    }

    @Cacheable("branchDictionaryNameByCode")
    public String getDictionaryNameByCode(String code) {
        Dictionary dictionary = branchDictionaryDao.getByCode(code);
        if (null == dictionary) return null;
        else return dictionary.getName();
    }




}
