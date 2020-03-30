package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.RepairUser;
import java.util.List;
import java.util.Map;

public interface RepairUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RepairUser record);

    int insertSelective(RepairUser record);

    RepairUser selectByPrimaryKey(Integer id);

    List<RepairUser> selectByMap(Map<String, Object> params);

    int updateByPrimaryKeySelective(RepairUser record);

    int updateByPrimaryKey(RepairUser record);

    int updateByMap(Map<String, Object> params);
}