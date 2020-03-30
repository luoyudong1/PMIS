package com.kthw.pmis.serviceimpl.entryAndOut;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kthw.common.base.ErrCode;
import com.kthw.pmis.mapper.entryAndOut.ProduceEntryMapper;
import com.kthw.pmis.model.entryAndOut.AssetAttribute;
import com.kthw.pmis.model.entryAndOut.SheetDetail;
import com.kthw.pmis.model.entryAndOut.SheetInfo;
import com.kthw.pmis.model.entryAndOut.Supplier;
import com.kthw.pmis.service.entryAndOut.ProduceEntryService;

@Service
public class ProduceEntryServiceImpl implements ProduceEntryService {

    private static Logger logger = LoggerFactory.getLogger(ProduceEntryServiceImpl.class);

    @Autowired
    private ProduceEntryMapper produceEntryMapper;

	@Override
	public List<SheetInfo> getAllSheets(Map<String, Object> params) {
		List<SheetInfo> sheetInfos = null;
		try {
			sheetInfos = produceEntryMapper.getAllSheets(params);
		} catch (Exception e) {
			logger.error("getAllSheets error: ", e);
		}
		return sheetInfos;
	}

	@Override
	public int getSheetParamCount(Map<String, Object> params) {
		int count = 0;
        try {
            count = produceEntryMapper.getSheetParamCount(params);
        } catch (Exception e) {
            logger.error("getSheetParamCount error: ", e);
        }
        return count;
	}

	@Override
	public int getAllSheetCount() {
		int count = 0;
        try {
            count = produceEntryMapper.getAllSheetCount();
        } catch (Exception e) {
            logger.error("getAllSheetCount error: ", e);
        }
        return count;
	}

	@Override
	public int addSheet(SheetInfo sheetInfo) {
		int errCode = 0;
		try {
			produceEntryMapper.addSheet(sheetInfo);
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
			produceEntryMapper.updateSheet(sheetInfo);
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
			produceEntryMapper.removeSheetById(sheet_id);
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
			sheetInfo = produceEntryMapper.getSheetById(sheet_id);
		} catch (Exception e) {
			logger.error("getSheetById error: ", e);
		}
		return sheetInfo;
	}

	@Override
	public List<SheetDetail> getAllSheetDetails(Map<String, Object> params) {
		List<SheetDetail> sheetDetails = null;
		try {
			sheetDetails = produceEntryMapper.getAllSheetDetails(params);
		} catch (Exception e) {
			logger.error("getAllSheetDetails error: ", e);
		}
		return sheetDetails;
	}

	@Override
	public int getSheetDetailParamCount(Map<String, Object> params) {
		int count = 0;
        try {
            count = produceEntryMapper.getSheetDetailParamCount(params);
        } catch (Exception e) {
            logger.error("getSheetDetailParamCount error: ", e);
        }
        return count;
	}

	@Override
	public int getAllSheetDetailCount() {
		int count = 0;
        try {
            count = produceEntryMapper.getAllSheetDetailCount();
        } catch (Exception e) {
            logger.error("getAllSheetDetailCount error: ", e);
        }
        return count;
	}

	@Override
	public int addSheetDetail(SheetDetail sheetDetail) {
		int errCode = 0;
		try {
			produceEntryMapper.addSheetDetail(sheetDetail);
		} catch (Exception e) {
			logger.error("addSheetDetail error: ", e);
			errCode = ErrCode.DB_ERROR;
		}
		return errCode;
	}

	@Override
	public int updateSheetDetail(SheetDetail sheetDetail) {
		int errCode = 0;
		try {
			produceEntryMapper.updateSheetDetail(sheetDetail);
		} catch (Exception e) {
			logger.error("updateSheetDetail error: ", e);
			errCode = ErrCode.DB_ERROR;
		}
		return errCode;
	}

	@Override
	public int removeSheetDetailById(String sheet_id) {
		int errCode = 0;
		try {
			produceEntryMapper.removeSheetDetailById(sheet_id);
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
			produceEntryMapper.removeSheetDetailByCode(params);
		} catch (Exception e) {
			logger.error("removeSheetDetailByCode error: ", e);
			errCode = ErrCode.DB_ERROR;
		}
		return errCode;
	}

	@Override
	public String getSheetDetailMaxById(String part_id) {
		String maxPart_id = null;
		try {
			maxPart_id = produceEntryMapper.getSheetDetailMaxById(part_id);
		} catch (Exception e) {
			logger.error("getSheetDetailMaxById error: ", e);
		}
		return maxPart_id;
	}

	@Override
	public List<Supplier> getAllSuppliers(Map<String, Object> params) {
		List<Supplier> suppliers = null;
		try {
			suppliers = produceEntryMapper.getAllSuppliers(params);
		} catch (Exception e) {
			logger.error("getAllSuppliers error: ", e);
		}
		return suppliers;
	}

	@Override
	public List<SheetDetail> getSheetDetails(Map<String, Object> params) {
		List<SheetDetail> sheetDetails = null;
		try {
			sheetDetails = produceEntryMapper.getSheetDetails(params);
		} catch (Exception e) {
			logger.error("getSheetDetails error: ", e);
		}
		return sheetDetails;
	}

	@Override
	public int updateSheetVerify(SheetInfo sheetInfo) {
		int errCode = 0;
		try {
			produceEntryMapper.updateSheetVerify(sheetInfo);
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
			sheetInfos = produceEntryMapper.getAllSheetsVerify(params);
		} catch (Exception e) {
			logger.error("getAllSheetsVerify error: ", e);
		}
		return sheetInfos;
	}

	@Override
	public String getMaxSheetId(String sheet_id) {
		String maxSheet_id = null;
		try {
			maxSheet_id = produceEntryMapper.getMaxSheetId(sheet_id);
		} catch (Exception e) {
			logger.error("getSheetDetailMaxById error: ", e);
		}
		return maxSheet_id;
	}

	@Override
	public List<AssetAttribute> getAllAssetAttributes(Map<String, Object> params) {
		List<AssetAttribute> assetAttribute = null;
		try {
			assetAttribute = produceEntryMapper.getAllAssetAttributes(params);
		} catch (Exception e) {
			logger.error("getAllSuppliers error: ", e);
		}
		return assetAttribute;
	}

	@Override
	public int getAllSheetsVerifyCount(Map<String, Object> params) {
		int count = 0;
		try {
			count=produceEntryMapper.getAllSheetsVerifyCount( params);
		} catch (Exception e) {
			logger.error("getAllSheetsVerifyCount error: ", e);
		}
		return count;
	}
	@Override
	public String getMaxPartId(Map<String, Object> params) {
		String maxPartId="";
		try {
			maxPartId =produceEntryMapper.getMaxPartId(params);
		} catch (Exception e) {
			logger.error("getMaxPartId error: ", e);
		}
		return maxPartId;
	}

}