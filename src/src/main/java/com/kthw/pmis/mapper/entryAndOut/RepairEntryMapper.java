package com.kthw.pmis.mapper.entryAndOut;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kthw.pmis.model.entryAndOut.AssetAttribute;
import com.kthw.pmis.model.entryAndOut.SheetDetail;
import com.kthw.pmis.model.entryAndOut.SheetInfo;
import com.kthw.pmis.model.entryAndOut.Supplier;

@Repository
public interface RepairEntryMapper {

	// 获取所有单据
	List<SheetInfo> getAllSheets(Map<String, Object> params);

	// 有条件总记录数
	int getSheetParamCount(Map<String, Object> params);

	// 无条件总记录数
	int getAllSheetCount();

	// 添加单据
	int addSheet(SheetInfo sheetInfo);

	// 修改单据
	int updateSheet(SheetInfo sheetInfo);

	// 删除单据
	int removeSheetById(String sheet_id);

	// 根据id查询单据
	List<SheetInfo> getSheetById(String sheet_id);

	// 获取所有配件详情
	List<SheetDetail> getAllSheetDetails(Map<String, Object> params);

	// 有条件总记录数
	int getSheetDetailParamCount(Map<String, Object> params);

	// 无条件总记录数
	int getAllSheetDetailCount();

	// 添加配件详情
	int addSheetDetail(SheetDetail sheetDetail);

	// 修改配件详情
	int updateSheetDetail(SheetDetail sheetDetail);

	// 删除配件详情
	int removeSheetDetailById(String sheet_id);

	// 删除配件详情
	int removeSheetDetailByCode(Map<String, Object> params);

	// 根据配件id查询配件详情
	String getSheetDetailMaxById(String part_id);

	// 获取供货商
	List<Supplier> getAllSuppliers(Map<String, Object> params);

	// 采购入库查询
	List<SheetDetail> getSheetDetails(Map<String, Object> params);

	// 采购单审核
	int updateSheetVerify(SheetInfo sheetInfo);

	List<SheetInfo> getAllSheetsVerify(Map<String, Object> params);

	// 获取最大sheet_id
	String getMaxSheetId(String sheet_id);

	// 资产属性
	List<AssetAttribute> getAllAssetAttributes(Map<String, Object> params);

	// 获取送修配件
	List<SheetDetail> getAllParts(Map<String, Object> params);

	// 有条件返修配件条数
	int getPartsParamCount(Map<String, Object> params);

	// 无条件返修配件条数
	int getAllPartsCount();

	// 根据配件出厂编号查询配件
	SheetDetail getPartCode(Map<String, Object> params);
}
