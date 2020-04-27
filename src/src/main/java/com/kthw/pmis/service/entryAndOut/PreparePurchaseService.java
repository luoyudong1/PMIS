package com.kthw.pmis.service.entryAndOut;

import java.util.List;

import java.util.Map;

import com.kthw.pmis.entiy.SheetDetail;
import com.kthw.pmis.entiy.SheetInfo;

public interface PreparePurchaseService {

	List<SheetInfo> getAllSheets(Map<String, Object> params);// 获取预购单据

	int createSheetInfo(SheetInfo sheetInfo);// 新增预购单据

	int sheetInfoModify(SheetInfo sheetInfo);// 修改预购单据

	int modifyVerify(SheetInfo sheetInfo);// 修改预购单据

	int getAllSheetDetails(SheetInfo sheetInfo);// 修改预购单据

	int searchPreparePurchase(SheetInfo sheetInfo);// 修改预购单据

	int getPartsZtree(SheetInfo sheetInfo);// 修改预购单据

	int sheetDetailCreate(SheetDetail sheetDetail);//增加sheetDetail

	int sheetDetailModify(SheetDetail sheetDetail);//修改sheetDetail

	int sheetDetailDeleteByCode(SheetDetail sheetDetail);//删除sheetDetail

	int delete(SheetInfo sheetInfo);//删除sheetInfo

	int exportSheetInfo(SheetInfo sheetInfo);//删除sheetInfo

}
