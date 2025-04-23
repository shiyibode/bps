package org.nmgns.bps.system.dao;


import org.apache.ibatis.annotations.Mapper;
import org.nmgns.bps.system.entity.Api;

import java.util.List;

@Mapper
public interface ApiDao {

    public Api getApiById(Long id);

    public List<Api> getAllApi();

    public Api getApiByUri(String uri);

    public void insert(Api api);

    public void update(Api api);

    public void delete(Long id);

    public Long getCount(Api api);

    public List<Api> get(Api api);

}
