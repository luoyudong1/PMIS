package com.kthw.pmis.serviceimpl.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kthw.pmis.mapper.system.StoreHouseManageMapper;
import com.kthw.pmis.model.system.StoreHouseInfo;
import com.kthw.pmis.service.system.StoreHouseManageService;

@Service
public class StoreHouseManageServiceImpl implements StoreHouseManageService {

	private static Logger logger = LoggerFactory.getLogger(StoreHouseManageServiceImpl.class);
	
	
	@Autowired
	private StoreHouseManageMapper storeHouseManageMapper;
	/**
	 * 通过用户ID查询仓库信息
	 * @param user_id
	 * @return
	 */
	@Override
	public List<StoreHouseInfo> getUserStorehouseByUserId(Map<String, Object> params) {
		List<StoreHouseInfo> storeHouseInfos=null;
		try {
			storeHouseInfos=storeHouseManageMapper.getUserStorehouseByUserId(params);
			if(storeHouseInfos==null) {
				storeHouseInfos=new ArrayList<StoreHouseInfo>();
			}
		} catch (Exception e) {
			logger.error("StoreHouseManageServiceImpl getUserStorehouseByUserId ERROR :" + e);
		}
		return storeHouseInfos;
	}

}
