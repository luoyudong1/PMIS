package com.kthw.pmis.serviceimpl.entryAndOut;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kthw.common.base.ErrCode;
import com.kthw.pmis.mapper.entryAndOut.PurchasePartsMapper;
import com.kthw.pmis.model.entryAndOut.AssetAttribute;
import com.kthw.pmis.model.entryAndOut.SheetDetail;
import com.kthw.pmis.model.entryAndOut.SheetInfo;
import com.kthw.pmis.model.entryAndOut.Supplier;
import com.kthw.pmis.service.entryAndOut.PurchasePartsService;

@Service
public class PurchasePartsServiceImpl implements PurchasePartsService {

    private static Logger logger = LoggerFactory.getLogger(PurchasePartsServiceImpl.class);

    @Autowired
    private PurchasePartsMapper purchasePartsMapper;

	@Override
	public List<SheetInfo> getAllSheets(Map<String, Object> params) {
		List<SheetInfo> sheetInfos = null;
		try {
			sheetInfos = purchasePartsMapper.getAllSheets(params);
		} catch (Exception e) {
			logger.error("getAllSheets error: ", e);
		}
		return sheetInfos;
	}

	@Override
	public int getSheetParamCount(Map<String, Object> params) {
		int count = 0;
        try {
            count = purchasePartsMapper.getSheetParamCount(params);
        } catch (Exception e) {
            logger.error("getSheetParamCount error: ", e);
        }
        return count;
	}

	@Override
	public int getAllSheetCount() {
		int count = 0;
        try {
            count = purchasePartsMapper.getAllSheetCount();
        } catch (Exception e) {
            logger.error("getAllSheetCount error: ", e);
        }
        return count;
	}

	@Override
	public int addSheet(SheetInfo sheetInfo) {
		int errCode = 0;
		try {
			purchasePartsMapper.addSheet(sheetInfo);
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
			purchasePartsMapper.updateSheet(sheetInfo);
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
			purchasePartsMapper.removeSheetById(sheet_id);
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
			sheetInfo = purchasePartsMapper.getSheetById(sheet_id);
		} catch (Exception e) {
			logger.error("getSheetById error: ", e);
		}
		return sheetInfo;
	}

	@Override
	public List<SheetDetail> getAllSheetDetails(Map<String, Object> params) {
		List<SheetDetail> sheetDetails = null;
		try {
			sheetDetails = purchasePartsMapper.getAllSheetDetails(params);
		} catch (Exception e) {
			logger.error("getAllSheetDetails error: ", e);
		}
		return sheetDetails;
	}

	@Override
	public int getSheetDetailParamCount(Map<String, Object> params) {
		int count = 0;
        try {
            count = purchasePartsMapper.getSheetDetailParamCount(params);
        } catch (Exception e) {
            logger.error("getSheetDetailParamCount error: ", e);
        }
        return count;
	}

	@Override
	public int getAllSheetDetailCount() {
		int count = 0;
        try {
            count = purchasePartsMapper.getAllSheetDetailCount();
        } catch (Exception e) {
            logger.error("getAllSheetDetailCount error: ", e);
        }
        return count;
	}

	@Override
	public int addSheetDetail(SheetDetail sheetDetail) {
		int errCode = 0;
		try {
			purchasePartsMapper.addSheetDetail(sheetDetail);
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
			purchasePartsMapper.updateSheetDetail(sheetDetail);
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
			purchasePartsMapper.removeSheetDetailById(sheet_id);
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
			purchasePartsMapper.removeSheetDetailByCode(params);
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
			maxPart_id = purchasePartsMapper.getSheetDetailMaxById(part_id);
		} catch (Exception e) {
			logger.error("getSheetDetailMaxById error: ", e);
		}
		return maxPart_id;
	}

	@Override
	public List<Supplier> getAllSuppliers(Map<String, Object> params) {
		List<Supplier> suppliers = null;
		try {
			suppliers = purchasePartsMapper.getAllSuppliers(params);
		} catch (Exception e) {
			logger.error("getAllSuppliers error: ", e);
		}
		return suppliers;
	}

	@Override
	public List<SheetDetail> getSheetDetails(Map<String, Object> params) {
		List<SheetDetail> sheetDetails = null;
		try {
			sheetDetails = purchasePartsMapper.getSheetDetails(params);
		} catch (Exception e) {
			logger.error("getSheetDetails error: ", e);
		}
		return sheetDetails;
	}

	@Override
	public int updateSheetVerify(SheetInfo sheetInfo) {
		int errCode = 0;
		try {
			purchasePartsMapper.updateSheetVerify(sheetInfo);
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
			sheetInfos = purchasePartsMapper.getAllSheetsVerify(params);
		} catch (Exception e) {
			logger.error("getAllSheetsVerify error: ", e);
		}
		return sheetInfos;
	}

	@Override
	public String getMaxSheetId(String sheet_id) {
		String maxSheet_id = null;
		try {
			maxSheet_id = purchasePartsMapper.getMaxSheetId(sheet_id);
		} catch (Exception e) {
			logger.error("getSheetDetailMaxById error: ", e);
		}
		return maxSheet_id;
	}

	@Override
	public List<AssetAttribute> getAllAssetAttributes(Map<String, Object> params) {
		List<AssetAttribute> assetAttribute = null;
		try {
			assetAttribute = purchasePartsMapper.getAllAssetAttributes(params);
		} catch (Exception e) {
			logger.error("getAllAssetAttributes error: ", e);
		}
		return assetAttribute;
	}

	@Override
	public List<SheetDetail> getAllParts(Map<String, Object> params) {
		List<SheetDetail> parts = null;
		try {
			parts = purchasePartsMapper.getAllParts(params);
		} catch (Exception e) {
			logger.error("getAllParts error: ", e);
		}
		return parts;
	}

	@Override
	public int getAllPartsCount() {
		int count = 0;
        try {
            count = purchasePartsMapper.getAllPartsCount();
        } catch (Exception e) {
            logger.error("getAllPartsCount error: ", e);
        }
        return count;
	}

	@Override
	public int getPartsParamCount(Map<String, Object> params) {
		int count = 0;
        try {
            count = purchasePartsMapper.getPartsParamCount(params);
        } catch (Exception e) {
            logger.error("getPartsParamCount error: ", e);
        }
        return count;
	}

	@Override
	public String getMaxPartId(Map<String, Object> params) {
		String maxPartId="";
		try {
			maxPartId = purchasePartsMapper.getMaxPartId(params);
		} catch (Exception e) {
			logger.error("getMaxPartId error: ", e);
		}
		return maxPartId;
	}

	@Override
	public int getAllSheetsVerifyCount(Map<String, Object> params) {
		int count = 0;
        try {
            count = purchasePartsMapper.getAllSheetsVerifyCount(params);
        } catch (Exception e) {
            logger.error("getPartsParamCount error: ", e);
        }
        return count;
	}
	

}