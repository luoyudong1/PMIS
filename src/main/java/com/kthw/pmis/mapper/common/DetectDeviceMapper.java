package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.DetectDevice;
import com.kthw.pmis.entiy.DetectDeviceWithDepot;
import com.kthw.pmis.entiy.dto.DetectDeviceDTO;

import java.util.List;
import java.util.Map;

public interface DetectDeviceMapper {
    int deleteByPrimaryKey(Integer detectDeviceId);

    int insert(DetectDevice record);

    int insertSelective(DetectDevice record);

    DetectDevice selectByPrimaryKey(Integer detectDeviceId);

    List<DetectDevice> selectByMap(Map<String, Object> params);

    int updateByPrimaryKeySelective(DetectDevice record);

    int updateByPrimaryKey(DetectDevice record);

    int updateByMap(Map<String, Object> params);

    int getMaxDetectId();

    List<DetectDeviceDTO> listDetectDevice(Map<String, Object> params);

    List<DetectDevice> listDetectDeviceByWorkShop(Map<String, Object> params);

    /**
     * 获取检修劳效系数
     * @param params
     * @return
     */
    List<DetectDeviceWithDepot> listDetectDeviceWithDepot(Map<String, Object> params);
}