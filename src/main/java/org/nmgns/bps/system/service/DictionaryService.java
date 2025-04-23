package org.nmgns.bps.system.service;

import org.nmgns.bps.system.dao.DictionaryDao;
import org.nmgns.bps.system.entity.Dictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DictionaryService {

    @Autowired
    private DictionaryDao dictionaryDao;

    @Cacheable("dictionaryByCode")
    public Dictionary getDictionaryByCode(String code) {
        return dictionaryDao.getByCode(code);
    }

    @Cacheable("dictionaryNameByCode")
    public String getDictionaryNameByCode(String code) {
        Dictionary dictionary = dictionaryDao.getByCode(code);
        if (null == dictionary) return null;
        else return dictionary.getName();
    }




}
