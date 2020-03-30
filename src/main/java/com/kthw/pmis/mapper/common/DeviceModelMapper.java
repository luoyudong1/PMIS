package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.DeviceModel;
import java.util.List;
import java.util.Map;

public interface DeviceModelMapper {
    int insert(DeviceModel record);

    int insertSelective(DeviceModel record);

    List<DeviceModel> selectByMap(Map<String, Object> params);

    int updateByMap(Map<String, Object> params);
}