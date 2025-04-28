package org.nmgns.bps.system.dao;


import org.apache.ibatis.annotations.Mapper;
import org.nmgns.bps.system.entity.Menu;

import java.util.List;

@Mapper
public interface MenuDao {

    public List<Menu> getMenuListByUserId(Long userId);

    public List<Menu> getAllMenuList();

    public Menu getMenuById(Long id);

    public Menu getMenuByUri(String uri);

    public Long getCount(Menu menu);

    public List<Menu> get(Menu menu);

    public void update(Menu menu);

    public void insert(Menu menu);

    public void delete(Long menuId);

    public List<Menu> getLowerLevelMenuById(Long menuId);

    public void deleteChildMenuById(Long menuId);

}
