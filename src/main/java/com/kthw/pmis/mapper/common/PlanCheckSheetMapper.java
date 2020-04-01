package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.PlanCheckSheet;
import java.util.List;
import java.util.Map;

public interface PlanCheckSheetMapper {
    int insert(PlanCheckSheet record);

    int insertSelective(PlanCheckSheet record);

    List<PlanCheckSheet> selectByMap(Map<String, Object> params);

    int updateByMap(Map<String, Object> params);

    //获取最大sheet_id
    String getMaxSheetId(String sheetId);
}