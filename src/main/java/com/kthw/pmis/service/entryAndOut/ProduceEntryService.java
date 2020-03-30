package com.kthw.pmis.service.entryAndOut;

import java.util.List;
import java.util.Map;

import com.kthw.pmis.model.entryAndOut.AssetAttribute;
import com.kthw.pmis.model.entryAndOut.SheetDetail;
import com.kthw.pmis.model.entryAndOut.SheetInfo;
import com.kthw.pmis.model.entryAndOut.Supplier;

public interface ProduceEntryService {

	/**
	 * 获取所有单据
	 * @param params
	 * @return
	 */
	List<SheetInfo> getAllSheets(Map<String, Object> params);

	/**
	 * 有条件总记录数
	 * @param params
	 * @return
	 */
	int getSheetParamCount(Map<String, Object> params);

	/**
	 * 无条件总记录数
	 * @return
	 */
	int getAllSheetCount();

	/**
	 * 添加单据
	 * @param sheetInfo
	 * @return
	 */
	int addSheet(SheetInfo sheetInfo);

	/**
	 * 修改单据
	 * @param sheetInfo
	 * @return
	 */
	int updateSheet(SheetInfo sheetInfo);

	/**
	 * 删除单据
	 * @param sheet_id
	 * @return
	 */
	int removeSheetById(String sheet_id);

	/**
	 * 根据id查询单据
	 * @param sheet_id
	 * @return
	 */
	List<SheetInfo> getSheetById(String sheet_id);

	/**
	 *  获取所有配件详情
	 * @param params
	 * @return
	 */
	List<SheetDetail> getAllSheetDetails(Map<String, Object> params);

	/**
	 * 有条件总记录数
	 * @param params
	 * @return
	 */
	int getSheetDetailParamCount(Map<String, Object> params);

	/**
	 * 无条件总记录数
	 * @return
	 */
	int getAllSheetDetailCount();

	/**
	 * 添加配件详情
	 * @param sheetDetail
	 * @return
	 */
	int addSheetDetail(SheetDetail sheetDetail);

	/**
	 *  修改配件详情
	 * @param sheetDetail
	 * @return
	 */
	int updateSheetDetail(SheetDetail sheetDetail);

	/**
	 * 删除入库单据下的配件
	 * @param sheet_id
	 * @return
	 */
	int removeSheetDetailById(String sheet_id);
	
	/**
	 * 删除配件
	 */	
	int removeSheetDetailByCode(Map<String, Object> params);
	
	/**
	 *  根据配件id查询配件详情
	 * @param part_id
	 * @return
	 */
	String getSheetDetailMaxById(String part_id);
	
	/**
	 * 获取供货商
	 * @param params
	 * @return
	 */
	List<Supplier> getAllSuppliers(Map<String, Object> params);
	
	/**
	 * 采购入库查询
	 * @param params
	 * @return
	 */
	List<SheetDetail> getSheetDetails(Map<String, Object> params);

	/**
	 * 采购单审核
	 * @param sheetInfo
	 * @return
	 */
	int updateSheetVerify(SheetInfo sheetInfo);

	List<SheetInfo> getAllSheetsVerify(Map<String, Object> params);

	int getAllSheetsVerifyCount(Map<String, Object> params);
	/**
	 * 获取当日入库单据最大值
	 */
	String getMaxSheetId(String sheet_id);

	/**
	 * 资产属性
	 * @param params
	 * @return
	 */
	List<AssetAttribute> getAllAssetAttributes(Map<String, Object> params);
	/**
	 * 获取最大的partId	
	 * @param params
	 * @return
	 */
	
	String getMaxPartId(Map<String, Object> params);
	
}
