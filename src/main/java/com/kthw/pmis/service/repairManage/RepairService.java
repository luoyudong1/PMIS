package com.kthw.pmis.service.repairManage;

import java.util.List;
import java.util.Map;

import com.kthw.pmis.model.entryAndOut.SheetDetail;
import com.kthw.pmis.model.entryAndOut.SheetInfo;
import com.kthw.pmis.model.entryAndOut.Supplier;
import com.kthw.pmis.model.repairManage.PartsAllocate;
import com.kthw.pmis.model.stock.StockInfo;
import com.kthw.pmis.model.system.DeviceType;
import com.kthw.pmis.model.system.StoreHouseInfo;

public interface RepairService {


	/**
	 * 有条件条数
	 * @param params
	 */
	int getRepairPartsParamCount(Map<String, Object> params);

	/**
	 * 无条件条数
	 */
	int getAllRepairPartsCount();

	/**
	 * 送修配件
	 * @param params
	 */
	List<StockInfo> getAllRepairParts(Map<String, Object> params);

	/**
	 * 添加返修配件
	 * @param parts_id
	 * @return
	 */
	int addRepairParts(Map<String, Object> params);

	/**
	 * 显示返修配件
	 * @param params
	 * @return
	 */
	List<PartsAllocate> getAllVerifyParts(Map<String, Object> params);

	/**
	 * 有条件返修条数
	 * @param params
	 * @return
	 */
	int getVerifyPartsParamCount(Map<String, Object> params);

	/**
	 * 无条件返修条数
	 * @return
	 */
	int getAllVerifyPartsCount();

	/**
	 * 修改或审核
	 * @param params
	 * @return
	 */
	int partsModifyVerify(Map<String, Object> params);

	/**
	 * 显示审核配件
	 * @param params
	 * @return
	 */
	List<PartsAllocate> getAllVerifyingParts(Map<String, Object> params);

	/**
	 * 删除待审核配件
	 * @param params
	 * @return
	 */
	int partsDelete(Map<String, Object> params);

	/**
	 * 查询所内检修入库源仓库
	 * @param params
	 * @return
	 */
	List<StoreHouseInfo> getStorehouse(Map<String, Object> params);
	/**
	 * 查询所内检修出库目的仓库
	 * @param params
	 * @return
	 */
	
	List<StoreHouseInfo> getStorehouseOfRepairOut(Map<String, Object> params);
	/**
	 * 获取仓库信息
	 * @param params
	 * @return
	 */
	List<StoreHouseInfo> getStorehouseByMap(Map<String, Object> params);
	/**
	 * 审核有条件返修条数
	 * @param params
	 * @return
	 */
	int getVerifyingPartsParamCount(Map<String, Object> params);

	/**
	 * 审核无条件返修条数
	 * @return
	 */
	int getAllVerifyingPartsCount();
	
}
