package com.kthw.pmis.controller.stock;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kthw.common.ExcelUtil;
import com.kthw.pmis.entiy.*;
import com.kthw.pmis.helper.DepotHelper;
import com.kthw.pmis.helper.SheetDetailHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import com.kthw.pmis.entiy.dto.StockInfoDTO;
import com.kthw.pmis.entiy.ext.SheetInfoExt;
import com.kthw.pmis.mapper.common.SheetDetailMapper;
import com.kthw.pmis.mapper.common.SheetInfoMapper;
import com.kthw.pmis.mapper.common.StockInfo1Mapper;
import com.kthw.pmis.mapper.common.StoreHouseMapper;
@Transactional
@Controller
@RequestMapping(value = "/stock/depotToTest")
public class DepotToTestController {
	private static Logger logger = LoggerFactory.getLogger(DepotToTestController.class);
	@Autowired
	private SheetInfoMapper sheetInfoMapper;
	@Autowired
	private SheetDetailMapper sheetDetailMapper;
	@Autowired
	private StockInfo1Mapper stockInfoMapper;
	@Autowired
	private StoreHouseMapper storeHouseMapper;
	@Autowired
	private SheetDetailHelper sheetDetailHelper;
	@Autowired
	private DepotHelper depotHelper;
	@ResponseBody
	@RequestMapping(value = "/getAllSheets", method = { RequestMethod.GET })
	public DataTable<SheetInfoExt> sheetsList(@RequestParam Map<String, Object> params, HttpServletRequest request) {
		logger.info("显示车间调拨到所单据");
		String start = request.getParameter("start");
		String length = request.getParameter("length");
		List<SheetInfoExt> list = new ArrayList<>();
		DataTable<SheetInfoExt> dt = new DataTable<SheetInfoExt>();
		int total = 0;
		params.put("sourceStoreHouseId", request.getParameter("sourceStoreHouseId"));
		params.put("sourceDepotId", Short.valueOf(request.getParameter("sourceDepotId")));
		params.put("queryTime", request.getParameter("queryTime"));
		params.put("queryTime2", request.getParameter("queryTime2"));
		params.put("sheetId", request.getParameter("sheetId"));
		params.put("sendVerifyFlag", request.getParameter("sendVerifyFlag"));
		params.put("sheetType", (short) SheetInfoType.REWORKTRANSFER.getId());
		params.put("orderByClause", "send_verify_flag asc,add_date desc LIMIT " + length + " OFFSET " + start);
		total = sheetInfoMapper.getSheetParamCount(params);// 根据条件获取单据总数
		dt.setRecordsTotal(total);
		dt.setRecordsFiltered(total);
		list = sheetInfoMapper.getAllSheets(params);
		dt.setData(list);
		dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		return dt;

	}

	@ResponseBody
	@RequestMapping(value = "/createSheetInfo", method = { RequestMethod.POST })
	public Map<String, Object> createSheet(@RequestBody SheetInfo sheetInfo) {
		logger.info("新增车间调拨到所单据");
		int code = 0;
		if (sheetInfo != null && StringUtils.isNotBlank(sheetInfo.getSheetId())) {
			if (sheetInfoMapper.selectByPrimaryKey(sheetInfo.getSheetId()) != null) {
				code = ErrCode.SHEET_ALREADY_EXISTS;
			} else {
				sheetInfo.setSheetType((short) SheetInfoType.REWORKTRANSFER.getId());
				sheetInfo.setSendVerifyFlag((short) VerifyFlagType.NOVERIFY.getId());
				sheetInfoMapper.insert(sheetInfo);
				code = ErrCode.SUCCESS;
			}

		} else {
			code = ErrCode.INCOMPLETE_INFO;
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}

	@ResponseBody
	@RequestMapping(value = "/modify", method = { RequestMethod.POST })
	public Map<String, Object> modify(@RequestBody SheetInfo sheetInfo) {
		logger.info("修改或审核段返修单据");
		int code = 0;
		Map<String, Object> params = new HashMap<String, Object>();
		List<StockInfo1> list = new ArrayList<StockInfo1>();
        
		if (sheetInfo != null) {
			SheetInfo info=sheetInfoMapper.selectByPrimaryKey(sheetInfo.getSheetId());
			
		if (sheetInfo.getSendVerifyFlag()!=null&&sheetInfo.getSendVerifyFlag() == (short) VerifyFlagType.VERIFIED.getId()) {
			params.put("eqSheetId", sheetInfo.getSheetId());

			// 根据sheetId查询车间调拨到所配件详情
			List<SheetDetail> sheetDetails = sheetDetailMapper.selectByMap(params);
            StoreHouse sourceStoreHouse=storeHouseMapper.selectByPrimaryKey(info.getSourceStoreHouseId());
			// 添加到stockInfo库存数组中
			for (SheetDetail sheetDetail : sheetDetails) {
				StockInfo1 stockInfo = new StockInfo1();
				
				stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
				stockInfo.setStorehouseId(sourceStoreHouse.getExtrStorehouseId());
				stockInfo.setPartIdSeq(sheetDetail.getPartId());
				stockInfo.setPartsState((short)2);
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
	 * 查询页面用
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/searchSheets", method = {RequestMethod.GET})
	public DataTable<SheetInfoExt> searchSheets(HttpServletRequest request) {
		logger.info("显示段返修单据");
		String start = request.getParameter("start");
		String length = request.getParameter("length");
		List<SheetInfoExt> list = new ArrayList<>();
		DataTable<SheetInfoExt> dt = new DataTable<SheetInfoExt>();
		int total = 0;
		Map<String, Object> params = new HashMap<>();
		params.put("queryTime", request.getParameter("queryTime"));
		params.put("queryTime2", request.getParameter("queryTime2"));
		params.put("sheetId", request.getParameter("sheetId"));
		params.put("sheetType", (short) SheetInfoType.REWORKTRANSFER.getId());
		params.put("orderByClause", "receipt_verify_flag asc,receipt_verify_date asc LIMIT " + length + " OFFSET " + start);
		String sourceDepotId = request.getParameter("sourceDepotId");
		//获取子部门
		if (sourceDepotId != null) {
			List<Depot> childrens = depotHelper.getChildrens(Long.valueOf(sourceDepotId));
			params.put("sourceDepotIdIn", childrens);
			total = sheetInfoMapper.getSheetParamCount(params);// 根据条件获取单据总数

			dt.setRecordsTotal(total);
			dt.setRecordsFiltered(total);
			list = sheetInfoMapper.getAllSheets(params);
		}
		dt.setData(list);
		dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		return dt;

	}

	@ResponseBody
	@RequestMapping(value = "/SheetDetailCreate", method = { RequestMethod.POST })
	public Map<String, Object> SheetDetailNew(@RequestBody SheetDetail sheetDetail) {
		logger.info("增加车间调拨到所入库配件");
		int code = 0;
		Map<String, Object> ret = new HashMap<String, Object>();
		if(sheetDetail.getSheetId()!=null) {
		SheetInfo sheetInfo=sheetInfoMapper.selectByPrimaryKey(sheetDetail.getSheetId());
		if(sheetDetailMapper.selectByPrimaryKey(sheetDetail)!=null) {
			code=ErrCode.SHEETDETAIL_ALREADY_EXISTS;
		}else {
			StockInfo1 stockInfo = new StockInfo1();
			stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
			stockInfo.setStorehouseId((short) sheetInfo.getSourceStoreHouseId());
			stockInfo.setPartIdSeq(sheetDetail.getPartId());
			stockInfo.setEnabled((short)0);//不可用
			stockInfoMapper.updateByPrimaryKeySelective(stockInfo);
			
			sheetDetailMapper.insert(sheetDetail);

		}
		}
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}

	/**
	 * 批量增加sheetDetail
	 * @param request
	 * @param list
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sheetDetailBatchCreate", method = { RequestMethod.POST })
	public Map<String, Object> sheetDetailBatchCreate(HttpServletRequest request,@RequestBody List<SheetDetail> list) {
		logger.info("增加车间调拨到所入库配件");
		int code = 0;
		Map<String, Object> ret = new HashMap<String, Object>();
		if(list.size()>0) {
			SheetInfo sheetInfo=sheetInfoMapper.selectByPrimaryKey(list.get(0).getSheetId());
			if(sheetInfo!=null) {
		for(SheetDetail sheetDetail:list) {
			try {
				sheetDetail=sheetDetailHelper.repalce(sheetDetail);
			}catch (Exception e){
				e.printStackTrace();
			}
			StockInfo1 stockInfo = new StockInfo1();
			stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
			stockInfo.setStorehouseId(sheetInfo.getSourceStoreHouseId());
			stockInfo.setPartIdSeq(sheetDetail.getPartId());
			stockInfo.setEnabled((short)0);//不可用
			stockInfoMapper.updateByPrimaryKeySelective(stockInfo);
			
			sheetDetailMapper.insert(sheetDetail);

		
		}}
		}else {
			code=ErrCode.EMPTY_RESULT;
		}
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}

	/**
	 * 修改sheetDetail
	 * @param sheetDetail
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/SheetDetailModify", method = { RequestMethod.POST })
	public Map<String, Object> SheetDetailEdit(@RequestBody SheetDetail sheetDetail) {
		logger.info("修改车间调拨到所入库配件");
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

	@ResponseBody
	@RequestMapping(value = "/SheetDetailDeleteByCode", method = { RequestMethod.POST })
	public Map<String, Object> SheetDetailDeleteByCode(@RequestBody SheetDetail sheetDetail) {
		logger.info("删除车间调拨到所入库配件");
		int code = 0;
		if (sheetDetail != null) {
			//如果审核未通过，回退库存到预采购库
			SheetInfo sheetInfo=sheetInfoMapper.selectByPrimaryKey(sheetDetail.getSheetId());
		if(sheetInfo.getSendVerifyFlag()!=(short) VerifyFlagType.VERIFIED.getId()) {
		if (sheetDetail != null && StringUtils.isNotBlank(sheetDetail.getSheetId())
				&& StringUtils.isNotBlank(sheetDetail.getPartId())
				&& StringUtils.isNotBlank(sheetDetail.getPartCode())) {
			StockInfo1 stockInfo = new StockInfo1();
			stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
			stockInfo.setStorehouseId( sheetInfo.getSourceStoreHouseId());
			stockInfo.setPartIdSeq(sheetDetail.getPartId());
			stockInfo.setEnabled((short)1);//可用
			stockInfoMapper.updateByPrimaryKeySelective(stockInfo);
			
			
			//删除sheetDetail
			
		} 
		}
			sheetDetailMapper.deleteByPrimaryKey(sheetDetail);

		} else {
			code = ErrCode.INCOMPLETE_INFO;
		}
		code = ErrCode.SUCCESS;
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = { RequestMethod.POST })
	public Map<String, Object> delete(@RequestBody SheetInfo sheetInfo) {
		logger.info("删除入库单据下的配件");
		int code = 0;
		List<StockInfo1> list = new ArrayList<StockInfo1>();
		Map<String, Object> params=new HashMap<String, Object>();
		if (sheetInfo != null&&sheetInfo.getSheetId()!=null) {
			SheetInfo info=sheetInfoMapper.selectByPrimaryKey(sheetInfo.getSheetId());
		if(info.getSendVerifyFlag()!=(short) VerifyFlagType.VERIFIED.getId()) {
			
			params.put("eqSheetId", sheetInfo.getSheetId());
			// 根据sheetId查询预采购配件详情
			List<SheetDetail> sheetDetails = sheetDetailMapper.selectByMap(params);

			// 添加到stockInfo库存数组中
			for (SheetDetail sheetDetailDTO : sheetDetails) {
				StockInfo1 stockInfo = new StockInfo1();
				stockInfo.setFactoryPartsCode(sheetDetailDTO.getPartCode());
				stockInfo.setStorehouseId( info.getSourceStoreHouseId());
				stockInfo.setPartIdSeq(sheetDetailDTO.getPartId());
				stockInfo.setEnabled((short) 1);
				list.add(stockInfo);

			}
			// 批量更新库存
			stockInfoMapper.batchUpdate(list);
			

		
		}
        sheetInfoMapper.deleteByPrimaryKey(sheetInfo.getSheetId());
		
		List<SheetDetail> sheetDetaillist = sheetDetailMapper.selectByMap(params);
		if (sheetDetaillist.size() > 0) {
			sheetDetailMapper.deleteBySheetId(sheetInfo.getSheetId());

		} 
		}else {
				code = ErrCode.INCOMPLETE_INFO;
			}
		
		code = ErrCode.SUCCESS;
		Map<String, Object> ret = new HashMap<String, Object>();
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
	public DataTable<SheetDetailDTO> getAllPartsByStock(
			HttpServletRequest request) {
		logger.info("获取送修库库存配件信息");
		List<SheetDetailDTO> list = new ArrayList<SheetDetailDTO>();
		DataTable<SheetDetailDTO> dt = new DataTable<SheetDetailDTO>();
		int total=0;
		Map<String, Object> params=new HashMap<String, Object>();
		String partCode=request.getParameter("partCode");
		String partId=request.getParameter("partId");
		String storeHouseId=request.getParameter("storeHouseId");
		if(storeHouseId!=null&&storeHouseId!="") {
	    params.put("eqStorehouseId",Short.valueOf(storeHouseId));
	    params.put("partIdSeq", partId);
	    params.put("eqFactoryPartsCode", partCode);

		//获取配件详细信息
		
		list = sheetDetailMapper.selectStockWithSheetId(params);
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
	@RequestMapping(value = "/getAllSourceStoreHouse", method = { RequestMethod.POST })
	public DataTable<StoreHouse> getAllReceiptStoreHouse(
			HttpServletRequest request,@RequestBody StoreHouse storeHouse) {
		logger.info("获取所有收货库信息");
		List<StoreHouse> list = new ArrayList<StoreHouse>();
		DataTable<StoreHouse> dt = new DataTable<StoreHouse>();
		Map<String, Object> params=new HashMap<String, Object>();
	    params.put("eqType", storeHouse.getType());//收货库类型
	    params.put("eqDepotId",storeHouse.getDepotId());//收货库类型
		//获取收货库
		list = storeHouseMapper.selectByMap(params);
		//返回dataTable参数
		dt.setData(list);
		dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		return dt;
	}
	// ExcelUtil导出报表
	@RequestMapping(value = "/exportSheetInfo", method = { RequestMethod.GET })
	@ResponseBody
	public void exportSheetInfo(@RequestParam Map<String, Object> params, HttpServletRequest request,
								HttpServletResponse response) {
		logger.info("配送到车间或班组关键配件交接单---ExcelUtil导出报表");
		List<SheetInfoExt> sheetInfos = sheetInfoMapper.getAllSheets(params);// 获取单据信息
		if (sheetInfos.size() > 0) {
			SheetInfoExt sheetInfoExt = sheetInfos.get(0);
			params.clear();
			params.put("eqSheetId", sheetInfoExt.getSheetId());// 获取单据信息
			List<SheetDetailDTO> sheetDetailList = sheetDetailMapper.selectWithParts(params);// 获取单据对应的配件详情
			String file_name = "车辆运行安全监控系统设备故障关键配件送修交接单";
			String[] title = {"编号", "关键配件名称", "设备型号", "设备类型", "生产厂家", "配件出厂编码", "配件二维码", "资产配属", "使用探测站","故障发生日期","配件故障现象", "是否质保期", "备注"};
			HSSFWorkbook wb = ExcelUtil.exportDeoptToTest(file_name, title, sheetDetailList, sheetInfoExt, null);
			try {
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setHeader("Content-Disposition",
						"attachment;filename=" + new String((file_name + ".xls").getBytes(), "iso-8859-1"));
				response.addHeader("Pargam", "no-cache");
				response.addHeader("Cache-Control", "no-cache");
				OutputStream os = response.getOutputStream();
				wb.write(os);
				os.flush();
				os.close();
			} catch (Exception e) {
				logger.error("0 ERROR :" + e);
				e.printStackTrace();
			}
		}
	}
}
