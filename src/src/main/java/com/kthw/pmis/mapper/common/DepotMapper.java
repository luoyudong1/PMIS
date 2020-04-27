package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.Depot;
import java.util.List;
import java.util.Map;

public interface DepotMapper {
    int deleteByPrimaryKey(Long depotId);

    int insert(Depot record);

    int insertSelective(Depot record);

    Depot selectByPrimaryKey(Long depotId);

    List<Depot> selectByMap(Map<String, Object> params);

    int updateByPrimaryKeySelective(Depot record);

    int updateByPrimaryKey(Depot record);

    int updateByMap(Map<String, Object> params);
}