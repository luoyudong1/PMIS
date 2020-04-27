package com.kthw.pmis.serviceimpl.stock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kthw.common.base.ErrCode;
import com.kthw.pmis.mapper.stock.StockInfoMapper;
import com.kthw.pmis.model.stock.StockInfo;
import com.kthw.pmis.service.stock.StockInfoService;

@Service
public class StockInfoServiceImpl implements StockInfoService {
	private static Logger logger = LoggerFactory.getLogger(StockInfoServiceImpl.class);
	
	@Autowired
	private StockInfoMapper stockInfoMapper;

	/**
	 * 查询配件信息(具体到个体配件)
	 * @param params 查询条件
	 * @return
	 */
	@Override
	public List<StockInfo> selectStockInfos(Map<String, Object> params) {
		List<StockInfo> stockInfos=null;
		try {
			stockInfos=stockInfoMapper.selectStockInfos(params);
			if(stockInfos==null) {
				stockInfos=new ArrayList<StockInfo>();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("StockInfoServiceImpl selectStockInfos ERROR : "+ e);
		}
		
		return stockInfos;
	}
	/**
	 * 查询配件信息(具体到个体配件)数量
	 * @param params 查询条件
	 * @return
	 */
	@Override
	public int selectStockInfosCount(Map<String, Object> params) {
		int num=0;
		try {
			num=stockInfoMapper.selectStockInfosCount(params);
		} catch (Exception e) {
			logger.error("StockInfoServiceImpl selectStockInfosCount ERROR : "+ e);
		}
		return num;
	}
	
	/**
	 * 删除配件信息
	 * @param params
	 * @return
	 */
	@Override
	public int deleteStockInfo(Map<String, Object> params) {
		int code=0;
		try {
			stockInfoMapper.deleteStockInfo(params);
		} catch (Exception e) {
			logger.error("StockInfoServiceImpl deleteStockInfo ERROR : "+ e);
			code=ErrCode.DB_ERROR;
		}
		return code;
	}
	
	/**
	 * 删除入库单据下的配件信息
	 * @param params
	 * @return
	 */
	@Override
	public int deleteStockInfoBySheetId(Map<String, Object> params) {
		int code=0;
		try {
			stockInfoMapper.deleteStockInfoBySheetId(params);
		} catch (Exception e) {
			logger.error("StockInfoServiceImpl deleteStockInfoBySheetId ERROR : "+ e);
			code=ErrCode.DB_ERROR;
		}
		return code;
	}
	
	/**
	 * 查询最大的配件条码编号(part_code)
	 * @param part_code
	 */
	@Override
	public String selectMaxPartCodeByPartId(String part_code) {
		String maxPartCode=null;
		try {
			maxPartCode=stockInfoMapper.selectMaxPartCodeByPartId(part_code);
		} catch (Exception e) {
			logger.error("StockInfoServiceImpl selectMaxPartCodeByPartId ERROR : "+ e);
		}
		return maxPartCode;
	}
	
	/**
	 * 增加配件详细信息
	 * @param params
	 */
	@Override
	public int addStockInfo(Map<String, Object> params) {
		int code=0;
		try {
			stockInfoMapper.addStockInfo(params);
		} catch (Exception e) {
			logger.error("StockInfoServiceImpl addStockInfo ERROR : "+ e);
			code=ErrCode.DB_ERROR;
		}
		return code;
	}
	
	/**
	 * 修改配件详细信息
	 * @param params
	 */
	@Override
	public int modifyStockInfo(Map<String, Object> params) {
		int code=0;
		try {
			stockInfoMapper.modifyStockInfo(params);
		} catch (Exception e) {
			logger.error("StockInfoServiceImpl modifyStockInfo ERROR : "+ e);
			code=ErrCode.DB_ERROR;
		}
		return code;
	}
	
	/**
	 * 查询最大的配件编码(part_id_seq)
	 * @param part_id_seq
	 */
	@Override
	public String getPartIdMaxById(String part_id_seq) {
		String maxPart_id=null;
		try {
			maxPart_id=stockInfoMapper.getPartIdMaxById(part_id_seq);
		} catch (Exception e) {
			logger.error("StockInfoServiceImpl getPartIdMaxById ERROR : "+ e);
		}
		return maxPart_id;
	}
	
	/**
	 * 配件审核
	 */
	@Override
	public int modifyVerify(Map<String, Object> params) {
		int code=0;
		try {
			stockInfoMapper.modifyVerify(params);
		} catch (Exception e) {
			logger.error("StockInfoServiceImpl modifyVerify ERROR : "+ e);
			code=ErrCode.DB_ERROR;
		}
		return code;
	}

}
