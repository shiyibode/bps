package org.nmgns.bps.system.dao;


import org.apache.ibatis.annotations.Mapper;
import org.nmgns.bps.system.entity.Log;

@Mapper
public interface LogDao {

    public void insert(Log log);

}
