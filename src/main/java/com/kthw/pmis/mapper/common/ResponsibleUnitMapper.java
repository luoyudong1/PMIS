package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.ResponsibleUnit;
import java.util.List;
import java.util.Map;

public interface ResponsibleUnitMapper {
    int deleteByPrimaryKey(Integer depotId);

    int insert(ResponsibleUnit record);

    int insertSelective(ResponsibleUnit record);

    ResponsibleUnit selectByPrimaryKey(Integer depotId);

    List<ResponsibleUnit> selectByMap(Map<String, Object> params);

    int updateByPrimaryKeySelective(ResponsibleUnit record);

    int updateByPrimaryKey(ResponsibleUnit record);

    int updateByMap(Map<String, Object> params);
}