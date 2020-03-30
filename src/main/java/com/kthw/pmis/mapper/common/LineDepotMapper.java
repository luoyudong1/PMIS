package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.LineDepot;
import java.util.List;
import java.util.Map;

public interface LineDepotMapper {
    int insert(LineDepot record);

    int insertSelective(LineDepot record);

    List<LineDepot> selectByMap(Map<String, Object> params);

    int updateByMap(Map<String, Object> params);
}