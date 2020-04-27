package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.StoreHouse;
import java.util.List;
import java.util.Map;

public interface StoreHouseMapper {
    int deleteByPrimaryKey(Short storehouseId);

    int insert(StoreHouse record);

    int insertSelective(StoreHouse record);

    StoreHouse selectByPrimaryKey(Short storehouseId);

    List<StoreHouse> selectByMap(Map<String, Object> params);

    int updateByPrimaryKeySelective(StoreHouse record);

    int updateByPrimaryKey(StoreHouse record);

    int updateByMap(Map<String, Object> params);
    /**
     * 获取车间下面班组库
     * @param params
     * @return
     */
    List<StoreHouse> getDepotReceiptStoreHouse(Map<String, Object> params);
    /**
     * 班组获取对应车间库
     * @param params
     * @return
     */
    List<StoreHouse> getWorkShopReceiptStoreHouse(Map<String, Object> params);
}