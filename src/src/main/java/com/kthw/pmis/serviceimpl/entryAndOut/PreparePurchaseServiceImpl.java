package com.kthw.pmis.serviceimpl.entryAndOut;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kthw.pmis.entiy.SheetDetail;
import com.kthw.pmis.entiy.SheetInfo;
import com.kthw.pmis.mapper.common.SheetInfoMapper;
import com.kthw.pmis.service.entryAndOut.PreparePurchaseService;

@Service
public class PreparePurchaseServiceImpl implements PreparePurchaseService {
	private static Logger logger = LoggerFactory.getLogger(PreparePurchaseServiceImpl.class);

	@Autowired
	private SheetInfoMapper sheetInfoMapper;
	@Autowired
	private SheetInfoMapper sheetDetailMapper;

	@Override
	public List<SheetInfo> getAllSheets(Map<String, Object> params) {
		try {


		} catch (Exception e) {
			logger.error("getAllSheets error: ", e);
		}
		return null;
	}

	@Override
	public int createSheetInfo(SheetInfo sheetInfo) {
		try {


		} catch (Exception e) {
			logger.error("getAllSheets error: ", e);
		}
		return 0;
	}

	@Override
	public int sheetInfoModify(SheetInfo sheetInfo) {
		try {


		} catch (Exception e) {
			logger.error("getAllSheets error: ", e);
		}
		return 0;
	}

	@Override
	public int modifyVerify(SheetInfo sheetInfo) {
		try {


		} catch (Exception e) {
			logger.error("getAllSheets error: ", e);
		}
		return 0;
	}

	@Override
	public int getAllSheetDetails(SheetInfo sheetInfo) {
		try {


		} catch (Exception e) {
			logger.error("getAllSheets error: ", e);
		}
		return 0;
	}

	@Override
	public int searchPreparePurchase(SheetInfo sheetInfo) {
		try {


		} catch (Exception e) {
			logger.error("getAllSheets error: ", e);
		}
		return 0;
	}

	@Override
	public int getPartsZtree(SheetInfo sheetInfo) {
		try {


		} catch (Exception e) {
			logger.error("getAllSheets error: ", e);
		}
		return 0;
	}

	@Override
	public int sheetDetailCreate(SheetDetail sheetDetail) {
		try {


		} catch (Exception e) {
			logger.error("getAllSheets error: ", e);
		}
		return 0;
	}

	@Override
	public int sheetDetailModify(SheetDetail sheetDetail) {
		try {


		} catch (Exception e) {
			logger.error("getAllSheets error: ", e);
		}
		return 0;
	}

	@Override
	public int sheetDetailDeleteByCode(SheetDetail sheetDetail) {
		try {


		} catch (Exception e) {
			logger.error("getAllSheets error: ", e);
		}
		return 0;
	}

	@Override
	public int delete(SheetInfo sheetInfo) {
		try {


		} catch (Exception e) {
			logger.error("getAllSheets error: ", e);
		}
		return 0;
	}

	@Override
	public int exportSheetInfo(SheetInfo sheetInfo) {
		return 0;
	}
}
