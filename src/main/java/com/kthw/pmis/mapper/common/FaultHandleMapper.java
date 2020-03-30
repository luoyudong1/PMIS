package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.FaultHandle;
import java.util.List;
import java.util.Map;

public interface FaultHandleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FaultHandle record);

    int insertSelective(FaultHandle record);

    FaultHandle selectByPrimaryKey(Integer id);

    List<FaultHandle> selectByMap(Map<String, Object> params);

    int updateByPrimaryKeySelective(FaultHandle record);

    int updateByPrimaryKey(FaultHandle record);

    int updateByMap(Map<String, Object> params);

    int getMaxId();
}