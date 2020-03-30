package com.kthw.pmis.service.entryAndOut;

import java.util.List;
import java.util.Map;

import com.kthw.pmis.model.entryAndOut.AssetAttribute;
import com.kthw.pmis.model.entryAndOut.SheetDetail;
import com.kthw.pmis.model.entryAndOut.SheetInfo;
import com.kthw.pmis.model.entryAndOut.Supplier;
import com.kthw.pmis.model.system.Parts;

public interface RepairEntryService {

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
	 * 添加配件详情
	 * @param sheetDetail
	 * @return
	 */
	int addSheetDetail(SheetDetail sheetDetail);

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
	 * 采购单审核
	 * @param sheetInfo
	 * @return
	 */
	int updateSheetVerify(SheetInfo sheetInfo);

	List<SheetInfo> getAllSheetsVerify(Map<String, Object> params);

	/**
	 * 返厂库配件
	 * @param params
	 * @return
	 */
	List<SheetDetail> getAllParts(Map<String, Object> params);

	/**
	 * 有条件返厂库配件数
	 * @param params
	 * @return
	 */
	int getPartsParamCount(Map<String, Object> params);

	/**
	 * 无条件返厂库配件数
	 * @return
	 */
	int getAllPartsCount();

	/**
	 * 根据配件出厂编号查询配件
	 * @param params
	 * @return
	 */
	SheetDetail getPartCode(Map<String, Object> params);

	/**
	 * 返修入库查询
	 * @param params
	 * @return
	 */
	List<SheetDetail> getSheetDetails(Map<String, Object> params);
	
}
