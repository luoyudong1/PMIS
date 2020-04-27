package com.kthw.pmis.service.system;

import java.util.List;
import java.util.Map;

import com.kthw.pmis.model.system.StoreHouseInfo;

public interface StoreHouseManageService {

	/**
	 * 通过用户ID查询仓库信息
	 * @param user_id
	 * @return
	 */
	public List<StoreHouseInfo> getUserStorehouseByUserId(Map<String, Object> params);
	
}
