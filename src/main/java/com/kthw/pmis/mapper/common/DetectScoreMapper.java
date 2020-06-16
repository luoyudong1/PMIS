package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.DetectScore;
import java.util.List;
import java.util.Map;

public interface DetectScoreMapper {
    int deleteByPrimaryKey(Integer detectDeviceId);

    int insert(DetectScore record);

    int insertSelective(DetectScore record);

    DetectScore selectByPrimaryKey(Integer detectDeviceId);

    List<DetectScore> selectByMap(Map<String, Object> params);

    int updateByPrimaryKeySelective(DetectScore record);

    int updateByPrimaryKey(DetectScore record);

    int updateByMap(Map<String, Object> params);
//
//    /**
//     *连接探测站表，获取探测站信息
//     * @param params
//     * @return
//     */
//    List<DetectScore> selectWithDetect(Map<String, Object> params);

    /**
     * 批量增加
     * @param list
     * @return
     */
    int batchInsert(List<DetectScore> list);
}