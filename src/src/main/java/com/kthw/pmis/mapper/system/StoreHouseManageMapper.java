package com.kthw.pmis.mapper.system;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kthw.pmis.model.system.StoreHouseInfo;

@Repository
public interface StoreHouseManageMapper {

	/**
	 * 通过用户ID查询仓库信息
	 * @param user_id
	 * @return
	 */
	public List<StoreHouseInfo> getUserStorehouseByUserId(Map<String, Object> params);
}
