package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.PlanCheckSheet;
import java.util.List;
import java.util.Map;

public interface PlanCheckSheetMapper {
    int deleteByPrimaryKey(String sheetId);

    int insert(PlanCheckSheet record);

    int insertSelective(PlanCheckSheet record);

    PlanCheckSheet selectByPrimaryKey(String sheetId);

    List<PlanCheckSheet> selectByMap(Map<String, Object> params);

    int updateByPrimaryKeySelective(PlanCheckSheet record);

    int updateByPrimaryKey(PlanCheckSheet record);

    int updateByMap(Map<String, Object> params);

    String getMaxSheetId(String sheetId);
}