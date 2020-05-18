package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.FaultHandle;
import com.kthw.pmis.entiy.PlanCheck;
import java.util.List;
import java.util.Map;

public interface PlanCheckMapper {
    int deleteByPrimaryKey(Integer id);
    //按单据删除
    int deleteBySheetId(String sheetId);

    int insert(PlanCheck record);

    int insertSelective(PlanCheck record);

    PlanCheck selectByPrimaryKey(Integer id);

    List<PlanCheck> selectByMap(Map<String, Object> params);

    int updateByPrimaryKeySelective(PlanCheck record);

    int updateByPrimaryKey(PlanCheck record);

    int updateByMap(Map<String, Object> params);

    Integer getMaxId();//

    int batchUpdate(List<PlanCheck> list);//批量修改

    int batchInsert(List<PlanCheck> list);//批量增加

    int getPlanCheckByMap(Map<String, Object> params);    // 辆安站调度获取计划检修处理未完成信息数量

    int getPlanCheckByMapDept(Map<String, Object> params);    // 段值班员（株洲）获取计划检修处理未完成信息数量

    int getWorkPlanCheckByMap(Map<String, Object> params);   // 车间值班员获取计划检修处理未完成信息数量

//
//    int getPlanCheckBySegment(Map<String, Object> params);//段获取排序数据
//
//    int getPlanCheckByCenter(Map<String, Object> params);//集团获取排序数据

}