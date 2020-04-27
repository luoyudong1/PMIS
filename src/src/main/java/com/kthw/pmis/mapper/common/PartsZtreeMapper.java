package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.PartsZtree;
import java.util.List;
import java.util.Map;

public interface PartsZtreeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PartsZtree record);

    int insertSelective(PartsZtree record);

    PartsZtree selectByPrimaryKey(Integer id);

    List<PartsZtree> selectByMap(Map<String, Object> params);

    int updateByPrimaryKeySelective(PartsZtree record);

    int updateByPrimaryKey(PartsZtree record);

    int updateByMap(Map<String, Object> params);

    List<PartsZtree> getPartCount(Map<String, Object> params);
}