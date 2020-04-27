package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.FaultType;
import java.util.List;
import java.util.Map;

public interface FaultTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FaultType record);

    int insertSelective(FaultType record);

    FaultType selectByPrimaryKey(Integer id);

    List<FaultType> selectByMap(Map<String, Object> params);

    int updateByPrimaryKeySelective(FaultType record);

    int updateByPrimaryKey(FaultType record);

    int updateByMap(Map<String, Object> params);
}