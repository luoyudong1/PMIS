package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.DevicePart;
import com.kthw.pmis.entiy.dto.DetectPartsDTO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface DevicePartMapper {
    int deleteByPrimaryKey(Short devicePartsId);

    int insert(DevicePart record);

    int insertSelective(DevicePart record);

    DevicePart selectByPrimaryKey(Short devicePartsId);

    List<DevicePart> selectByMap(Map<String, Object> params);

    int updateByPrimaryKeySelective(DevicePart record);

    int updateByPrimaryKey(DevicePart record);

    int updateByMap(Map<String, Object> params);
}