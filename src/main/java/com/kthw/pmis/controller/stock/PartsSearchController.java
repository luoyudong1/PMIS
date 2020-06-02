package com.kthw.pmis.controller.stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kthw.pmis.entiy.StockInfo1;
import com.kthw.pmis.entiy.dto.StockInfoDTO;
import com.kthw.pmis.mapper.common.StockInfo1Mapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kthw.common.DataTable;
import com.kthw.common.base.ErrCode;
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import com.kthw.pmis.mapper.common.SheetDetailMapper;
@Transactional
@Controller
@RequestMapping(value = "/stock/partsSearch")
public class PartsSearchController {
	private static Logger logger = LoggerFactory.getLogger(PartsSearchController.class);
	@Autowired
	private SheetDetailMapper sheetDetailMapper;
	@Autowired
	private StockInfo1Mapper stockInfoMapper;
	
	

	/**
	 * 根据sheetId获取单据详情
	 * 
	 * @param request
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/search", method = { RequestMethod.GET})
	public DataTable<SheetDetailDTO> searchPreparePurchase(HttpServletRequest request) {
		logger.info("配件流水查询");
		int total=0;
		DataTable<SheetDetailDTO> dt = new DataTable<SheetDetailDTO>();
		List<SheetDetailDTO> list=new ArrayList<SheetDetailDTO>();
		Map<String,Object> params=new HashMap<>();
		String partId = request.getParameter("partId");
		String partCode = request.getParameter("partCode");
		String start = request.getParameter("start");
		String length = request.getParameter("length");
		if(StringUtils.isNotBlank(partId)||StringUtils.isNotBlank(partCode)) {
		

		params.put("eqPartId", partId);
		params.put("eqPartCode", partCode);
		params.put("length", length);
		params.put("start", start);
		params.put("orderByClause", "si.add_date desc");
		total=sheetDetailMapper.getParamsCount(params);
		list=sheetDetailMapper.selectWithParts(params);
	}
		dt.setRecordsFiltered(total);
		dt.setRecordsTotal(total);
		dt.setData(list);
	
	    dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		return dt;
	}
	@ResponseBody
	@RequestMapping(value = "/getPartInfo", method = { RequestMethod.GET})
	public DataTable<StockInfoDTO> getPartInfo(HttpServletRequest request) {
		logger.info("配件信息查询");
		DataTable<StockInfoDTO> dt = new DataTable<StockInfoDTO>();
		List<StockInfoDTO> list=new ArrayList<StockInfoDTO>();
		Map<String,Object> params=new HashMap<>();
		//String sheetId = request.getParameter("sheetId");
		String partId = request.getParameter("partId");
		String partCode = request.getParameter("partCode");
		if(StringUtils.isNotBlank(partId)||StringUtils.isNotBlank(partCode)) {


			//params.put("eqSheetId", sheetId);
			params.put("eqPartId", partId);
			params.put("eqPartCode", partCode);
			StockInfoDTO stockInfo=stockInfoMapper.getPartInfo(params);
			if(stockInfo!=null) {
				list.add(stockInfo);
			}
		}
		dt.setData(list);

		dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		return dt;
	}
}
