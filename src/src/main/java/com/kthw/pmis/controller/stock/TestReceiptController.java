package com.kthw.pmis.controller.stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kthw.common.DataTable;
import com.kthw.common.base.ErrCode;
import com.kthw.pmis._enum.SheetInfoType;
import com.kthw.pmis._enum.StoreHouseEnum;
import com.kthw.pmis._enum.VerifyFlagType;
import com.kthw.pmis.controller.entryAndOut.PreparePurchaseController;
import com.kthw.pmis.entiy.SheetDetail;
import com.kthw.pmis.entiy.SheetInfo;
import com.kthw.pmis.entiy.StockInfo1;
import com.kthw.pmis.entiy.StoreHouse;
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import com.kthw.pmis.entiy.dto.StockInfoDTO;
import com.kthw.pmis.entiy.ext.SheetInfoExt;
import com.kthw.pmis.mapper.common.SheetDetailMapper;
import com.kthw.pmis.mapper.common.SheetInfoMapper;
import com.kthw.pmis.mapper.common.StockInfo1Mapper;
import com.kthw.pmis.mapper.common.StoreHouseMapper;
@Transactional
@Controller
@RequestMapping(value = "/stock/testReceipt")
public class TestReceiptController {
	private static Logger logger = LoggerFactory.getLogger(TestReceiptController.class);
	@Autowired
	private SheetInfoMapper sheetInfoMapper;
	@Autowired
	private SheetDetailMapper sheetDetailMapper;
	@Autowired
	private StockInfo1Mapper stockInfoMapper;
	@Autowired
	private StoreHouseMapper storeHouseMapper;

	@ResponseBody
	@RequestMapping(value = "/getAllSheets", method = { RequestMethod.GET })
	public DataTable<SheetInfoExt> sheetsList(@RequestParam Map<String, Object> params, HttpServletRequest request) {
		logger.info("显示所调拨到段单据");
		String start = request.getParameter("start");
		String length = request.getParameter("length");
		List<SheetInfoExt> list = new ArrayList<>();
		DataTable<SheetInfoExt> dt = new DataTable<SheetInfoExt>();
		int total = 0;
		params.put("sourceStoreHouseId", request.getParameter("sourceStoreHouseId"));
		params.put("queryTime", request.getParameter("queryTime"));
		params.put("queryTime2", request.getParameter("queryTime2"));
		params.put("sheetId", request.getParameter("sheetId"));
		//params.put("sendVerifyFlag", request.getParameter("sendVerifyFlag"));
		params.put("sheetType", (short) SheetInfoType.REWORKTRANSFER.getId());
		params.put("orderByClause", "receipt_verify_flag asc,send_verify_date desc LIMIT " + length + " OFFSET " + start);
		total = sheetInfoMapper.getSheetParamCount(params);// 根据条件获取单据总数
		dt.setRecordsTotal(total);
		dt.setRecordsFiltered(total);
		list = sheetInfoMapper.getAllSheets(params);
		dt.setData(list);
		dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		return dt;

	}
	@ResponseBody
	@RequestMapping(value = "/modify", method = { RequestMethod.POST })
	public Map<String, Object> modify(@RequestBody SheetInfo sheetInfo) {
		logger.info("修改或审核段调拨到所单据");
		int code = 0;
		Map<String, Object> params = new HashMap<String, Object>();
		List<StockInfo1> list = new ArrayList<StockInfo1>();

		if (sheetInfo != null) {
			SheetInfo info=sheetInfoMapper.selectByPrimaryKey(sheetInfo.getSheetId());
			
		if (sheetInfo.getReceiptVerifyFlag()!=null&&
				sheetInfo.getReceiptVerifyFlag() == (short) VerifyFlagType.VERIFIED.getId()) {
			params.put("eqSheetId", sheetInfo.getSheetId());

			// 根据sheetId查询所调拨配送到段配件详情
			List<SheetDetail> sheetDetails = sheetDetailMapper.selectByMap(params);
            StoreHouse objectStoreHouse=storeHouseMapper.selectByPrimaryKey(info.getObjectStoreHouseId());
			// 添加到stockInfo库存数组中
			for (SheetDetail sheetDetail : sheetDetails) {
				StockInfo1 stockInfo = new StockInfo1();
				stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
				stockInfo.setStorehouseId( objectStoreHouse.getStorehouseId());
				stockInfo.setPartIdSeq(sheetDetail.getPartId());
				stockInfo.setSheetId(sheetDetail.getSheetId());
				stockInfo.setSheetId(sheetDetail.getSheetId());
				stockInfo.setEnabled((short) 1);
				list.add(stockInfo);

			}
			// 批量更新库存
			stockInfoMapper.batchUpdate(list);
		}
		sheetInfoMapper.updateByPrimaryKeySelective(sheetInfo);
		}
		params.clear();
		params.put("code", code);
		params.put("msg", ErrCode.getMessage(code));
		return params;
	}

	@ResponseBody
	@RequestMapping(value = "/find", method = { RequestMethod.GET })
	public DataTable<SheetInfo> getPartsById(HttpServletRequest request, HttpSession httpSession) {
		logger.info("按id查询单据");
		Map<String, Object> params = new HashMap<String, Object>();
		String sheetId = request.getParameter("sheet_id");
		params.put("sheetId", sheetId);
		DataTable<SheetInfo> dt = new DataTable<SheetInfo>();
		List<SheetInfo> list = new ArrayList<SheetInfo>();
		list.add(sheetInfoMapper.selectByPrimaryKey(sheetId));
		dt.setData(list);
		return dt;
	}

	/**
	 * 根据sheetId获取单据详情
	 * 
	 * @param request
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getAllSheetDetails", method = { RequestMethod.POST })
	public DataTable<SheetDetailDTO> SheetDetailsList(HttpServletRequest request,
			@RequestParam Map<String, Object> params) {
		logger.info("显示配件详情");
		DataTable<SheetDetailDTO> dt = new DataTable<SheetDetailDTO>();
		List<SheetDetailDTO> data = new ArrayList<SheetDetailDTO>();
		String sheetId = request.getParameter("sheetId");
		// 为空直接返回
		if (sheetId == null || sheetId == "") {
			dt.setData(data);
			dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
			;
			return dt;
		}
		params.put("eqSheetId", sheetId);
		params.put("sheetType", (short) SheetInfoType.REWORKTRANSFER.getId());
		dt.setData(sheetDetailMapper.selectWithParts(params));
		dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		return dt;
	}

	/**
	 * 根据sheetId获取单据详情
	 * 
	 * @param request
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/search", method = { RequestMethod.POST })
	public DataTable<SheetDetailDTO> searchPreparePurchase(HttpServletRequest request,
			@RequestParam Map<String, Object> params) {
		logger.info("段调拨配送到所入库查询");
		DataTable<SheetDetailDTO> dt = new DataTable<SheetDetailDTO>();
		String sheetId = request.getParameter("sheetId");
		params.put("eqSheetId", sheetId);
		params.put("sheetType", (short) SheetInfoType.REWORKTRANSFER.getId());
		params.put("sendVerifyFlag", (short) VerifyFlagType.VERIFIED.getId());
		dt.setData(sheetDetailMapper.selectWithParts(params));
		dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		return dt;
	}

	@ResponseBody
	@RequestMapping(value = "/SheetDetailCreate", method = { RequestMethod.POST })
	public Map<String, Object> SheetDetailNew(@RequestBody SheetDetail sheetDetail) {
		logger.info("增加所调拨配送到段入库配件");
		int code = 0;
		System.out.println(sheetDetail.getSheetId());
		System.out.println(sheetDetail.getPartCode());
		if (sheetDetail != null && StringUtils.isNotBlank(sheetDetail.getSheetId())
				&& StringUtils.isNotBlank(sheetDetail.getPartCode())) {
			StockInfo1 stockInfo = new StockInfo1();
			stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
			stockInfo.setStorehouseId((short) StoreHouseEnum.TEST_DELIVERY.getId());
			stockInfo.setPartIdSeq(sheetDetail.getPartId());
			//stockInfo.setAssetAttributesId((short)99);//空资产属性
			stockInfo.setEnabled((short)0);//不可用
			stockInfoMapper.updateByPrimaryKeySelective(stockInfo);
			sheetDetailMapper.insert(sheetDetail);

		}
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}

	@ResponseBody
	@RequestMapping(value = "/SheetDetailModify", method = { RequestMethod.POST })
	public Map<String, Object> SheetDetailEdit(@RequestBody SheetDetail sheetDetail) {
		logger.info("修改所调拨配送到段入库配件");
		int code = 0;
		if (sheetDetail != null && StringUtils.isNotBlank(sheetDetail.getSheetId())
				&& StringUtils.isNotBlank(sheetDetail.getPartCode())) {
			int row=sheetDetailMapper.updateByPrimaryKeySelective(sheetDetail);
			 if(row==0) {
	            	code=ErrCode.MODIFY_ERROR;
	            }

		} else {
			code = ErrCode.INCOMPLETE_INFO;
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		code = ErrCode.SUCCESS;
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}
	/**
	 * 获取对应库存的配件信息
	 * @param
	 * @param request
	 * @return StockInfoDTO
	 */
	@ResponseBody
	@RequestMapping(value = "/getAllPartsByStock", method = { RequestMethod.GET })
	public DataTable<StockInfoDTO> getAllPartsByStock(@RequestParam Map<String, Object> params,
			HttpServletRequest request) {
		logger.info("获取配送库库存配件信息");
		List<StockInfoDTO> list = new ArrayList<StockInfoDTO>();
		DataTable<StockInfoDTO> dt = new DataTable<StockInfoDTO>();
		int total=0;
		params.put("start", request.getParameter("start"));
	    params.put("length", request.getParameter("length"));
	    params.put("storeHouseId", (short)StoreHouseEnum.TEST_DELIVERY.getId());
		//获取配件数量
		total=stockInfoMapper.getPartsParamCount(params);
		System.out.println("total"+ params.get("partsCode"));
		//获取配件详细信息
		if(total!=0) {
		list = stockInfoMapper.selectWithParts(params);
		}
		//返回dataTable参数
		dt.setRecordsTotal(total);
        dt.setRecordsFiltered(total);
		dt.setData(list);
		dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		return dt;
	}
	
	/**
	 * 获取对应库存的配件信息
	 * @param
	 * @param request
	 * @return StockInfoDTO
	 */
	@ResponseBody
	@RequestMapping(value = "/getAllReceiptStoreHouse", method = { RequestMethod.GET })
	public DataTable<StoreHouse> getAllReceiptStoreHouse(
			HttpServletRequest request) {
		logger.info("获取所有收货库信息");
		List<StoreHouse> list = new ArrayList<StoreHouse>();
		DataTable<StoreHouse> dt = new DataTable<StoreHouse>();
		Map<String, Object> params=new HashMap<String, Object>();
	    params.put("eqType", (short)5);//收货库类型
		//获取收货库
		list = storeHouseMapper.selectByMap(params);
		//返回dataTable参数
		dt.setData(list);
		dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		return dt;
	}
}
