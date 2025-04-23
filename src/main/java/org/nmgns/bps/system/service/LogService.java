package org.nmgns.bps.system.service;

import org.nmgns.bps.system.dao.LogDao;
import org.nmgns.bps.system.dao.RoleDao;
import org.nmgns.bps.system.entity.ApiPermission;
import org.nmgns.bps.system.entity.Log;
import org.nmgns.bps.system.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {

    @Autowired
    private LogDao logDao;

    public void insert(Log log){
        logDao.insert(log);
    }




}
