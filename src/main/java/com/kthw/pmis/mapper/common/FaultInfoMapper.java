package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.FaultInfo;
import java.util.List;
import java.util.Map;

public interface FaultInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FaultInfo record);

    int insertSelective(FaultInfo record);

    FaultInfo selectByPrimaryKey(Integer id);

    List<FaultInfo> selectByMap(Map<String, Object> params);

    int updateByPrimaryKeySelective(FaultInfo record);

    int updateByPrimaryKey(FaultInfo record);

    int updateByMap(Map<String, Object> params);
}