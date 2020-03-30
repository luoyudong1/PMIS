package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.ResponsibleUser;
import java.util.List;
import java.util.Map;

public interface ResponsibleUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ResponsibleUser record);

    int insertSelective(ResponsibleUser record);

    ResponsibleUser selectByPrimaryKey(Integer id);

    List<ResponsibleUser> selectByMap(Map<String, Object> params);

    int updateByPrimaryKeySelective(ResponsibleUser record);

    int updateByPrimaryKey(ResponsibleUser record);

    int updateByMap(Map<String, Object> params);
}