package com.kthw.pmis.serviceimpl.repairManage;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kthw.common.base.ErrCode;
import com.kthw.pmis.mapper.repairManage.RepairMapper;
import com.kthw.pmis.model.entryAndOut.SheetDetail;
import com.kthw.pmis.model.entryAndOut.SheetInfo;
import com.kthw.pmis.model.entryAndOut.Supplier;
import com.kthw.pmis.model.repairManage.PartsAllocate;
import com.kthw.pmis.model.stock.StockInfo;
import com.kthw.pmis.model.system.DeviceType;
import com.kthw.pmis.model.system.StoreHouseInfo;
import com.kthw.pmis.service.repairManage.RepairService;

@Service
public class RepairServiceImpl implements RepairService {

    private static Logger logger = LoggerFactory.getLogger(RepairServiceImpl.class);

    @Autowired
    private RepairMapper repairMapper;


	@Override
	public int getRepairPartsParamCount(Map<String, Object> params) {
		int count = 0;
        try {
            count = repairMapper.getRepairPartsParamCount(params);
        } catch (Exception e) {
            logger.error("getRepairPartsParamCount error: ", e);
        }
        return count;
	}

	@Override
	public int getAllRepairPartsCount() {
		int count = 0;
        try {
            count = repairMapper.getAllRepairPartsCount();
        } catch (Exception e) {
            logger.error("getAllRepairPartsCount error: ", e);
        }
        return count;
	}

	@Override
	public List<StockInfo> getAllRepairParts(Map<String, Object> params) {
		List<StockInfo> partsAllocate = null;
		try {
			partsAllocate = repairMapper.getAllRepairParts(params);
		} catch (Exception e) {
			logger.error("getAllRepair error: ", e);
		}
		return partsAllocate;
	}

	@Override
	public int addRepairParts(Map<String, Object> params) {
		int errCode = 0;
		try {
			repairMapper.addRepairParts(params);
		} catch (Exception e) {
			logger.error("addRepairParts error: ", e);
			errCode = ErrCode.DB_ERROR;
		}
		return errCode;
	}

	@Override
	public List<PartsAllocate> getAllVerifyParts(Map<String, Object> params) {
		List<PartsAllocate> partsAllocates = null;
		try {
			partsAllocates = repairMapper.getAllVerifyParts(params);
		} catch (Exception e) {
			logger.error("getAllVerifyParts error: ", e);
		}
		return partsAllocates;
	}

	@Override
	public int getVerifyPartsParamCount(Map<String, Object> params) {
		int count = 0;
        try {
            count = repairMapper.getVerifyPartsParamCount(params);
        } catch (Exception e) {
            logger.error("getVerifyPartsParamCount error: ", e);
        }
        return count;
	}

	@Override
	public int getAllVerifyPartsCount() {
		int count = 0;
        try {
            count = repairMapper.getAllVerifyPartsCount();
        } catch (Exception e) {
            logger.error("getAllRepairPartsCount error: ", e);
        }
        return count;
	}

	@Override
	public int partsModifyVerify(Map<String, Object> params) {
		int errCode = 0;
        try {
            repairMapper.partsModifyVerify(params);
        } catch (Exception e) {
            logger.error("partsModifyVerify error: ", e);
            errCode = ErrCode.DB_ERROR;
        }
        return errCode;
        
	}

	@Override
	public List<PartsAllocate> getAllVerifyingParts(Map<String, Object> params) {
		List<PartsAllocate> partsAllocates = null;
		try {
			partsAllocates = repairMapper.getAllVerifyingParts(params);
		} catch (Exception e) {
			logger.error("getAllVerifyingParts error: ", e);
		}
		return partsAllocates;
	}

	@Override
	public int partsDelete(Map<String, Object> params) {
		int count = 0;
        try {
            repairMapper.partsDelete(params);
        } catch (Exception e) {
            logger.error("partsDelete error: ", e);
        }
        return count;
	}

	@Override
	public List<StoreHouseInfo> getStorehouse(Map<String, Object> params) {
		List<StoreHouseInfo> storeHouseInfo = null;
		try {
			storeHouseInfo = repairMapper.getStorehouse(params);
		} catch (Exception e) {
			logger.error("getStorehouse error: ", e);
		}
		return storeHouseInfo;
	}

	@Override
	public int getVerifyingPartsParamCount(Map<String, Object> params) {
		int count = 0;
        try {
            count = repairMapper.getVerifyingPartsParamCount(params);
        } catch (Exception e) {
            logger.error("getVerifyingPartsParamCount error: ", e);
        }
        return count;
	}

	@Override
	public int getAllVerifyingPartsCount() {
		int count = 0;
        try {
            count = repairMapper.getAllVerifyingPartsCount();
        } catch (Exception e) {
            logger.error("getAllVerifyingPartsCount error: ", e);
        }
        return count;
	}

	@Override
	public List<StoreHouseInfo> getStorehouseOfRepairOut(Map<String, Object> params) {
		List<StoreHouseInfo> storeHouseInfo = null;
		try {
			storeHouseInfo = repairMapper.getStorehouseOfRepairOut(params);
		} catch (Exception e) {
			logger.error("getStorehouseOfRepairOut: ", e);
		}
		return storeHouseInfo;
	
	}

	@Override
	public List<StoreHouseInfo> getStorehouseByMap(Map<String, Object> params) {
		List<StoreHouseInfo> storeHouseInfo = null;
		try {
			storeHouseInfo = repairMapper.getStorehouseByMap(params);
		} catch (Exception e) {
			logger.error("getStorehouseByMap: ", e);
		}
		return storeHouseInfo;
	
	}

}