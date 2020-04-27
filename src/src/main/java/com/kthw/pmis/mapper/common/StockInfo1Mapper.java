package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.StockInfo1;
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import com.kthw.pmis.entiy.dto.StockInfoCountDTO;
import com.kthw.pmis.entiy.dto.StockInfoDTO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


public interface StockInfo1Mapper {
    int deleteByPrimaryKey(String factoryPartsCode);

    int insert(StockInfo1 record);

    int insertSelective(StockInfo1 record);

    StockInfo1 selectByPrimaryKey(String factoryPartsCode);

    List<StockInfo1> selectByMap(Map<String, Object> params);

    int updateByPrimaryKeySelective(StockInfo1 record);

    int updateByPrimaryKey(StockInfo1 record);

    int updateByMap(Map<String, Object> params);
    /**
     * 批量插入库存，预采购的时候用
     * @param list
     * @return
     */
    int batchInsert(List<StockInfo1> list);
    /**
     * 批量更新库存，修改库存配件仓库id
     * @param list
     * @return
     */
    int batchUpdate(List<StockInfo1> list);
    /**
     * 批量更新库存，修改库存配件仓库id
     * @param list
     * @return
     */
    
    int batchUpdateByPartsId(List<StockInfo1> list);
    /**
     * 批量删除库存，修改库存配件仓库id
     * @param list
     * @return
     */
    int batchDelete(List<StockInfo1> list);
    /**
     * 获取对应仓库配件信息
     * @param params
     * @return
     */
    List<StockInfoDTO> selectWithParts(Map<String, Object> params);
    /**
     * 获取配件基本信息
     * @param params
     * @return
     */
    StockInfoDTO getPartInfo(Map<String, Object> params);
    /**
     * 获取非新购配件最大partId
     * @param params
     * @return
     */
    String getMaxPartId(Map<String, Object> params);
    /**
     * 获取数量
     * @param params
     * @return
     */
    int getPartsParamCount(Map<String, Object> params);
    /**
     * 修改出厂编码
     */
     int updatePartCode(Map<String, Object> params);

     List<StockInfoCountDTO> countPartsByStoreHouse(Map<String, Object> params);

    }