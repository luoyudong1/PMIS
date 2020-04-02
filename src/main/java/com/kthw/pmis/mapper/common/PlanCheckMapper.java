package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.PlanCheck;
import java.util.List;
import java.util.Map;

public interface PlanCheckMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PlanCheck record);

    int insertSelective(PlanCheck record);

    PlanCheck selectByPrimaryKey(Integer id);

    List<PlanCheck> selectByMap(Map<String, Object> params);

    int updateByPrimaryKeySelective(PlanCheck record);

    int updateByPrimaryKey(PlanCheck record);

    int updateByMap(Map<String, Object> params);

    int getMaxId();//
}