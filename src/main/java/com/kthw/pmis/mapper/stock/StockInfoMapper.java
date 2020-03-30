package com.kthw.pmis.mapper.stock;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kthw.pmis.model.stock.StockInfo;


@Repository
public interface StockInfoMapper {

	/**
	 * 查询配件信息(具体到个体配件)
	 * @param params 查询条件
	 * @return
	 */
	public List<StockInfo> selectStockInfos(Map<String, Object> params);
	
	/**
	 * 查询配件信息(具体到个体配件)数量
	 * @param params 查询条件
	 * @return
	 */
	public int selectStockInfosCount(Map<String, Object> params);
	
	
	/**
	 * 删除配件信息
	 * @param params
	 * @return
	 */
	public void deleteStockInfo(Map<String, Object> params);
	
	/**
	 * 删除入库单据下的配件信息
	 * @param params
	 * @return
	 */
	public void deleteStockInfoBySheetId(Map<String, Object> params);
	
	/**
	 * 查询最大的配件条码编号(part_code)
	 * @param part_code
	 */
	public String selectMaxPartCodeByPartId(String part_code);
	
	/**
	 * 增加配件详细信息
	 * @param params
	 */
	public void addStockInfo(Map<String, Object> params);
	
	/**
	 * 修改配件详细信息
	 * @param params
	 */
	public void modifyStockInfo(Map<String, Object> params);

	/**
	 * 查询最大的配件编码(part_id_seq)
	 * @param part_id_seq
	 */
	public String getPartIdMaxById(String part_id_seq);

	/**
	 * 配件审核
	 * @param params
	 */
	public void modifyVerify(Map<String, Object> params);
	
	
}
