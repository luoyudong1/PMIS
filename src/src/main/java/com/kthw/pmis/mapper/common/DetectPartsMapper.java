package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.DetectParts;
import com.kthw.pmis.entiy.DevicePart;
import com.kthw.pmis.entiy.dto.DetectPartsDTO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface DetectPartsMapper {
    int deleteByPrimaryKey(Integer detectPartsId);

    int deleteByDetectDeviceId(Integer detectDeviceId);

    int insert(DetectParts record);

    int insertSelective(DetectParts record);

    DetectParts selectByPrimaryKey(Integer detectPartsId);

    List<DetectParts> selectByMap(Map<String, Object> params);

    int updateByPrimaryKeySelective(DetectParts record);

    int updateByPrimaryKey(DetectParts record);

    int updateByMap(Map<String, Object> params);
    
    List<DetectPartsDTO> selectWithParts(Map<String, Object> params);

    List<DetectPartsDTO> listDetectParts(Map<String, Object> params);
    //获取未安装的探测站
    List<String> getPartsUnstall(Map<String, Object> params);

    int getMaxDetectId();
    /**
     * 批量修改探测站配件信息
     * @param list
     * @return
     */
    int batchUpdate(@Param("list")List<DetectParts> list);
}