package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.FaultHandle;
import java.util.List;
import java.util.Map;

public interface FaultHandleMapper {
    int deleteByPrimaryKey(String id);

    int insert(FaultHandle record);

    int insertSelective(FaultHandle record);

    FaultHandle selectByPrimaryKey(String id);

    List<FaultHandle> selectByMap(Map<String, Object> params);

    int updateByPrimaryKeySelective(FaultHandle record);

    int updateByPrimaryKey(FaultHandle record);

    int updateByMap(Map<String, Object> params);

    String getMaxId(String id);
    /**
     * 批量更新
     * @param list
     * @return
     */

    int batchUpdate(List<FaultHandle> list);//

    int getFaultCheckByMap(Map<String, Object> params);    // 辆安站调度获取故障预报处理未完成信息数量

    int getFaultCheckByMapDepot(Map<String, Object> params);  //段值班员获取故障预报处理未完成信息数量


}