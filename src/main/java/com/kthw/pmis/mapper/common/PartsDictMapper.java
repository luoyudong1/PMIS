package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.PartsDict;
import com.kthw.pmis.entiy.ext.ViewParts;

import java.util.List;
import java.util.Map;

public interface PartsDictMapper {
    int deleteByPrimaryKey(Integer partsId);

    int insert(PartsDict record);

    int insertSelective(PartsDict record);

    PartsDict selectByPrimaryKey(Integer partsId);

    List<PartsDict> selectByMap(Map<String, Object> params);

    List<ViewParts> getPartsByModel(Map<String, Object> params);

    int updateByPrimaryKeySelective(PartsDict record);

    int updateByPrimaryKey(PartsDict record);

    int updateByMap(Map<String, Object> params);
}