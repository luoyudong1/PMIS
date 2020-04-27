package com.kthw.pmis.serviceimpl.entryAndOut;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kthw.common.base.ErrCode;
import com.kthw.pmis.mapper.entryAndOut.RepairOutMapper;
import com.kthw.pmis.model.entryAndOut.AssetAttribute;
import com.kthw.pmis.model.entryAndOut.SheetDetail;
import com.kthw.pmis.model.entryAndOut.SheetInfo;
import com.kthw.pmis.model.entryAndOut.Supplier;
import com.kthw.pmis.service.entryAndOut.RepairOutService;

@Service
public class RepairOutServiceImpl implements RepairOutService {
	
	private static Logger logger = LoggerFactory.getLogger(RepairOutServiceImpl.class);

	@Autowired
	private RepairOutMapper repairOutMapper;
	
	@Override
	public List<SheetInfo> getAllSheets(Map<String, Object> params) {
		List<SheetInfo> sheetInfos = null;
		try {
			sheetInfos = repairOutMapper.getAllSheets(params);
		} catch (Exception e) {
			logger.error("getAllSheets error: ", e);
		}
		return sheetInfos;
	}

	@Override
	public int getSheetParamCount(Map<String, Object> params) {
		int count = 0;
        try {
            count = repairOutMapper.getSheetParamCount(params);
        } catch (Exception e) {
            logger.error("getSheetParamCount error: ", e);
        }
        return count;
	}

	@Override
	public int getAllSheetCount() {
		int count = 0;
        try {
            count = repairOutMapper.getAllSheetCount();
        } catch (Exception e) {
            logger.error("getAllSheetCount error: ", e);
        }
        return count;
	}

	@Override
	public int addSheet(SheetInfo sheetInfo) {
		int errCode = 0;
		try {
			repairOutMapper.addSheet(sheetInfo);
		} catch (Exception e) {
			logger.error("addSheet error: ", e);
			errCode = ErrCode.DB_ERROR;
		}
		return errCode;
	}

	@Override
	public int updateSheet(SheetInfo sheetInfo) {
		int errCode = 0;
		try {
			repairOutMapper.updateSheet(sheetInfo);
		} catch (Exception e) {
			logger.error("updateSheet error: ", e);
			errCode = ErrCode.DB_ERROR;
		}
		return errCode;
	}

	@Override
	public int removeSheetById(String sheet_id) {
		int errCode = 0;
		try {
			repairOutMapper.removeSheetById(sheet_id);
		} catch (Exception e) {
			logger.error("removeSheetById error: ", e);
			errCode = ErrCode.DB_ERROR;
		}
		return errCode;
	}

	@Override
	public List<SheetInfo> getSheetById(String sheet_id) {
		List<SheetInfo> sheetInfo = null;
		try {
			sheetInfo = repairOutMapper.getSheetById(sheet_id);
		} catch (Exception e) {
			logger.error("getSheetById error: ", e);
		}
		return sheetInfo;
	}

	@Override
	public List<SheetDetail> getAllSheetDetails(Map<String, Object> params) {
		List<SheetDetail> sheetDetails = null;
		try {
			sheetDetails = repairOutMapper.getAllSheetDetails(params);
		} catch (Exception e) {
			logger.error("getAllSheetDetails error: ", e);
		}
		return sheetDetails;
	}

	@Override
	public int addSheetDetail(SheetDetail sheetDetail) {
		int errCode = 0;
		try {
			repairOutMapper.addSheetDetail(sheetDetail);
		} catch (Exception e) {
			logger.error("addSheetDetail error: ", e);
			errCode = ErrCode.DB_ERROR;
		}
		return errCode;
	}

	@Override
	public int removeSheetDetailById(String sheet_id) {
		int errCode = 0;
		try {
			repairOutMapper.removeSheetDetailById(sheet_id);
		} catch (Exception e) {
			logger.error("removeSheetDetailById error: ", e);
			errCode = ErrCode.DB_ERROR;
		}
		return errCode;
	}

	@Override
	public int removeSheetDetailByCode(Map<String, Object> params) {
		int errCode = 0;
		try {
			repairOutMapper.removeSheetDetailByCode(params);
		} catch (Exception e) {
			logger.error("removeSheetDetailByCode error: ", e);
			errCode = ErrCode.DB_ERROR;
		}
		return errCode;
	}

	@Override
	public int updateSheetVerify(SheetInfo sheetInfo) {
		int errCode = 0;
		try {
			repairOutMapper.updateSheetVerify(sheetInfo);
		} catch (Exception e) {
			logger.error("updateSheetVerify error: ", e);
			errCode = ErrCode.DB_ERROR;
		}
		return errCode;
	}

	@Override
	public List<SheetInfo> getAllSheetsVerify(Map<String, Object> params) {
		List<SheetInfo> sheetInfos = null;
		try {
			sheetInfos = repairOutMapper.getAllSheetsVerify(params);
		} catch (Exception e) {
			logger.error("getAllSheetsVerify error: ", e);
		}
		return sheetInfos;
	}

	@Override
	public List<SheetDetail> getAllParts(Map<String, Object> params) {
		List<SheetDetail> parts = null;
		try {
			parts = repairOutMapper.getAllParts(params);
		} catch (Exception e) {
			logger.error("getAllParts error: ", e);
		}
		return parts;
	}

	@Override
	public int getPartsParamCount(Map<String, Object> params) {
		int count = 0;
        try {
            count = repairOutMapper.getPartsParamCount(params);
        } catch (Exception e) {
            logger.error("getPartsParamCount error: ", e);
        }
        return count;
	}

	@Override
	public int getAllPartsCount() {
		int count = 0;
        try {
            count = repairOutMapper.getAllPartsCount();
        } catch (Exception e) {
            logger.error("getAllPartsCount error: ", e);
        }
        return count;
	}

	@Override
	public SheetDetail getPartCode(Map<String, Object> params) {
		SheetDetail sheetDetail = null;
		try {
			sheetDetail = repairOutMapper.getPartCode(params);
		} catch (Exception e) {
			logger.error("getSheetIdPartCode error: ", e);
		}
		return sheetDetail;
	}

	@Override
	public List<SheetDetail> getSheetDetails(Map<String, Object> params) {
		List<SheetDetail> sheetDetails = null;
		try {
			sheetDetails = repairOutMapper.getSheetDetails(params);
		} catch (Exception e) {
			logger.error("getSheetDetails error: ", e);
		}
		return sheetDetails;
	}

	
}
