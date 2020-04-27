package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.SheetDetail;
import com.kthw.pmis.entiy.SheetDetailKey;
import com.kthw.pmis.entiy.dto.PartsStatictsDTO;
import com.kthw.pmis.entiy.dto.SheetDetailDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SheetDetailMapper {
    int deleteByPrimaryKey(SheetDetailKey key);
    /**
     * 根据sheetId删除
     * @param sheetId
     * @return
     */
    int deleteBySheetId(String sheetId);

    int deleteByPartCode(String partCode);

    int insert(SheetDetail record);

    int insertSelective(SheetDetail record);

    SheetDetail selectByPrimaryKey(SheetDetailKey key);

    List<SheetDetail> selectByMap(Map<String, Object> params);
    /**
     * 获取配件相关信息的sheetDetail
     * @param params
     * @return
     */
    List<SheetDetailDTO> selectWithParts(Map<String, Object> params);
    /**
     * 获取配件相关信息的sheetDetail
     * @param params
     * @return
     */
    List<SheetDetailDTO> selectPartsUnstall(Map<String, Object> params);
    /**
     * 获取配件相关信息的selectWithParts的数量
     * @param params
     * @return
     */
    int getParamsCount(Map<String, Object> params);

    /**
     * 统计数量
     * @param params
     * @return
     */
    List<PartsStatictsDTO> getPartsCount(Map<String, Object> params);
    /**
     * 统计金额
     * @param params
     * @return
     */
    Integer getPartsPrice(Map<String, Object> params);

    /**
     * 获取一段时间内的同一单据类型的配件
     * @param params
     * @return
     */
    List<SheetDetailDTO> getPartsBySheetType(Map<String, Object> params);
    /**
     * 获取配件库存相关信息的sheetDetail
     * @param params
     * @return
     */
    List<SheetDetailDTO> selectWithStock(Map<String, Object> params);
    /**
     * 获取配件相关信息的sheetDetail
     * @param params
     * @return
     */

    List<SheetDetailDTO> selectDeliverySheetDetails(Map<String, Object> params);
    /**
     * 获取对应仓库配件信息sheetDetail
     * @param params
     * @return
     */
    List<SheetDetailDTO> selectStockWithSheetId(Map<String, Object> params);
    /**
     * 批量更新库存，修改库存配件仓库id
     * @param list
     * @return
     */
    
   int batchUpdate(List<SheetDetail> list);//
   
   int batchInsert(List<SheetDetail> list);
   
    Date selectMaxDateByPartCode(Map<String, Object> params);

    int updateByPrimaryKeySelective(SheetDetail record);

    int updateByPrimaryKey(SheetDetail record);

    int updateByMap(Map<String, Object> params);
    
   /**
    * 修改出厂编码
    */
    int updatePartCode(Map<String, Object> params);
}