package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.PartIdConfig;
import java.util.List;
import java.util.Map;

public interface PartIdConfigMapper {
    int insert(PartIdConfig record);

    int insertSelective(PartIdConfig record);

    List<PartIdConfig> selectByMap(Map<String, Object> params);

    int updateByMap(Map<String, Object> params);
}