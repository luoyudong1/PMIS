package com.kthw.pmis.mapper.repairManage;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kthw.pmis.model.entryAndOut.SheetDetail;
import com.kthw.pmis.model.entryAndOut.SheetInfo;
import com.kthw.pmis.model.entryAndOut.Supplier;
import com.kthw.pmis.model.repairManage.PartsAllocate;
import com.kthw.pmis.model.stock.StockInfo;
import com.kthw.pmis.model.system.StoreHouseInfo;




@Repository
public interface RepairMapper {

	List<PartsAllocate> getAllVerifyParts(Map<String, Object> params);

	int getRepairPartsParamCount(Map<String, Object> params);

	int getAllRepairPartsCount();

	List<StockInfo> getAllRepairParts(Map<String, Object> params);

	int addRepairParts(Map<String, Object> params);

	int getVerifyPartsParamCount(Map<String, Object> params);

	int getAllVerifyPartsCount();

	int partsModifyVerify(Map<String, Object> params);

	List<PartsAllocate> getAllVerifyingParts(Map<String, Object> params);

	int partsDelete(Map<String, Object> params);

	List<StoreHouseInfo> getStorehouse(Map<String, Object> params);
	
	List<StoreHouseInfo> getStorehouseOfRepairOut(Map<String, Object> params);
	
	List<StoreHouseInfo> getStorehouseByMap(Map<String, Object> params);
	
	int getVerifyingPartsParamCount(Map<String, Object> params);

	int getAllVerifyingPartsCount();
}
