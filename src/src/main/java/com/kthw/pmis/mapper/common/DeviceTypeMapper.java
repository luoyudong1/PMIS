package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.DeviceType;
import java.util.List;
import java.util.Map;

public interface DeviceTypeMapper {
    int insert(DeviceType record);

    int insertSelective(DeviceType record);

    List<DeviceType> selectByMap(Map<String, Object> params);

    int updateByMap(Map<String, Object> params);
}