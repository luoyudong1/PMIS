package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.AssetAttributes;
import java.util.List;
import java.util.Map;

public interface AssetAttributesMapper {
    int deleteByPrimaryKey(Short assetAttributesId);

    int insert(AssetAttributes record);

    int insertSelective(AssetAttributes record);

    AssetAttributes selectByPrimaryKey(Short assetAttributesId);

    List<AssetAttributes> selectByMap(Map<String, Object> params);

    int updateByPrimaryKeySelective(AssetAttributes record);

    int updateByPrimaryKey(AssetAttributes record);

    int updateByMap(Map<String, Object> params);
}