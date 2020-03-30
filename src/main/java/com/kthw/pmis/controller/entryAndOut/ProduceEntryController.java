package com.kthw.pmis.controller.entryAndOut;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Date;
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
import com.kthw.common.Utils;
import com.kthw.common.base.ErrCode;
import com.kthw.pmis._enum.StoreHouseEnum;
import com.kthw.pmis._enum.VerifyFlagType;
import com.kthw.pmis.entiy.StockInfo1;
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import com.kthw.pmis.mapper.common.SheetDetailMapper;
import com.kthw.pmis.mapper.common.StockInfo1Mapper;
import com.kthw.pmis.model.entryAndOut.AssetAttribute;
import com.kthw.pmis.model.entryAndOut.SheetDetail;
import com.kthw.pmis.model.entryAndOut.SheetInfo;
import com.kthw.pmis.model.entryAndOut.Supplier;
import com.kthw.pmis.service.entryAndOut.ProduceEntryService;

/**
 * 描述:生产入库Controller.
 */
@Transactional
@Controller
@RequestMapping(value = "/entryAndOut/produceEntry")
public class ProduceEntryController {

	private static Logger logger = LoggerFactory.getLogger(ProduceEntryController.class);

	@Autowired
	private ProduceEntryService produceEntryService;
	@Autowired
	private StockInfo1Mapper stockInfoMapper;
	@Autowired
	private SheetDetailMapper sheetDetailMapper;
	private final String[] ZEROS = { "000", "00", "0", "" };

	@ResponseBody
	@RequestMapping(value = "/getAllSheets", method = { RequestMethod.GET })
	public DataTable<SheetInfo> sheetsList(@RequestParam Map<String, Object> params, HttpServletRequest request) {
		logger.info("显示生产入库单据");
		params.put("start", request.getParameter("start"));
		params.put("length", request.getParameter("length"));
		List<SheetInfo> list = new ArrayList<>();
		DataTable<SheetInfo> dt = new DataTable<SheetInfo>();
		int total = 0;
		try{if (StringUtils.isNotBlank(request.getParameter("queryTime"))
				|| StringUtils.isNotBlank(request.getParameter("queryTime2"))
				|| StringUtils.isNotBlank(request.getParameter("sheet_id"))
				|| StringUtils.isNotBlank(request.getParameter("verify_flag"))) {
			total = produceEntryService.getSheetParamCount(params);// 有条件总数
		} else {
			total = produceEntryService.getAllSheetCount();// 无条件总数
		}
		dt.setRecordsTotal(total);
		dt.setRecordsFiltered(total);
		list = produceEntryService.getAllSheets(params);
		} catch (Exception e) {
			logger.error("getAllSheets error" + request);
		}
		dt.setData(list);
		dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		return dt;
	}

	@ResponseBody
	@RequestMapping(value = "/create", method = { RequestMethod.POST })
	public Map<String, Object> sheetNew(@RequestBody SheetInfo sheetInfo) {
		logger.info("增加生产入库单据");
		int code = 0;
		try{if (sheetInfo != null && StringUtils.isNotBlank(sheetInfo.getSheet_id())) {
			if (produceEntryService.getSheetById(sheetInfo.getSheet_id()).size() > 0) {
				code = ErrCode.SHEET_ALREADY_EXISTS;
			} else {
				code = produceEntryService.addSheet(sheetInfo);
			}

		} else {
			code = ErrCode.INCOMPLETE_INFO;
		}
		} catch (Exception e) {
			logger.error("create error" + sheetInfo);
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}

	@ResponseBody
	@RequestMapping(value = "/modify", method = { RequestMethod.POST })
	public Map<String, Object> partsManageEdit(@RequestBody SheetInfo sheetInfo) {
		logger.info("修改生产入库单据");
		int code = 0;
		try{
			if (sheetInfo != null) {
			code = produceEntryService.updateSheet(sheetInfo);
		} else {
			code = ErrCode.DB_ERROR;
		}
	} catch (Exception e) {
		logger.error("modify error" + sheetInfo);
	}
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = { RequestMethod.POST })
	public Map<String, Object> partsManageDelete(@RequestBody SheetInfo sheetInfo) {
		logger.info("删除生产入库单据");
		int code = 0;
		List<StockInfo1> list = new ArrayList<StockInfo1>();
		List<SheetInfo> sheetInfos=new ArrayList<SheetInfo>();
		try{if (sheetInfo != null&&sheetInfo.getSheet_id()!=null) {
			sheetInfos=produceEntryService.getSheetById(sheetInfo.getSheet_id());
		if(sheetInfos.get(0).getReceipt_verify_flag()!=(short) VerifyFlagType.VERIFIED.getId()) {
			Map<String, Object> params=new HashMap<String, Object>();
			params.put("eqSheetId", sheetInfo.getSheet_id());
			// 根据sheetId查询预采购配件详情
			List<com.kthw.pmis.entiy.SheetDetail> sheetDetails = sheetDetailMapper.selectByMap(params);

			// 添加到stockInfo库存数组中
			for (com.kthw.pmis.entiy.SheetDetail sheetDetailDTO : sheetDetails) {
				StockInfo1 stockInfo = new StockInfo1();
				stockInfo.setFactoryPartsCode(sheetDetailDTO.getPartCode());
				list.add(stockInfo);

			}
			// 批量删除库存
			stockInfoMapper.batchDelete(list);
			
		}
			code = produceEntryService.removeSheetById(sheetInfo.getSheet_id());
			if (StringUtils.isNotBlank(sheetInfo.getSheet_id())) {
				code = produceEntryService.removeSheetDetailById(sheetInfo.getSheet_id());
			} 
		} else {
			code = ErrCode.INCOMPLETE_INFO;
		}
		} catch (Exception e) {
			logger.error("delete error" + sheetInfo);
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}

	@ResponseBody
	@RequestMapping(value = "/find", method = { RequestMethod.GET })
	public DataTable<SheetInfo> getPartsById(HttpServletRequest request, HttpSession httpSession) {
		logger.info("按id查询生产入库单据");
		Map<String, Object> params = new HashMap<String, Object>();
		DataTable<SheetInfo> dt = new DataTable<SheetInfo>();
		try{String sheet_id = request.getParameter("sheet_id");
		params.put("sheet_id", sheet_id);
		dt.setData(produceEntryService.getSheetById(sheet_id));
	} catch (Exception e) {
		logger.error("find error" + request);
	}
		return dt;
	}

	@ResponseBody
	@RequestMapping(value = "/getAllSheetDetails", method = { RequestMethod.GET })
	public DataTable<SheetDetail> SheetDetailsList(HttpServletRequest request,
			@RequestParam Map<String, Object> params) {
		logger.info("显示生产入库配件详情");
		DataTable<SheetDetail> dt = new DataTable<SheetDetail>();
		try{dt.setData(produceEntryService.getAllSheetDetails(params));
		dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		} catch (Exception e) {
			logger.error("getAllSheetDetails error" + request);
		}
		return dt;
	}

	@ResponseBody
	@RequestMapping(value = "/SheetDetailCreate", method = { RequestMethod.POST })
	public Map<String, Object> SheetDetailNew(@RequestBody SheetDetail sheetDetail) {
		logger.info("增加生产入库配件");
		int code = 0;
		try{if (sheetDetail != null && StringUtils.isNotBlank(sheetDetail.getSheet_id())
				&& StringUtils.isNotBlank(sheetDetail.getPart_code())) {
//			String maxPart_id = produceEntryService.getSheetDetailMaxById(sheetDetail.getPart_id());
			StringBuffer part_id = new StringBuffer(String.valueOf(sheetDetail.getPart_id()));
			if (stockInfoMapper.selectByPrimaryKey(sheetDetail.getPart_code()) != null) {
				code = ErrCode.PARTCODE_ALREADY_EXISTS;
			} else {
				StockInfo1 stockInfo = new StockInfo1();
				stockInfo.setFactoryPartsCode(sheetDetail.getPart_code());
				stockInfo.setStorehouseId((short) StoreHouseEnum.TEST_PRODUCE.getId());
				stockInfo.setPartIdSeq(sheetDetail.getPart_id());
				stockInfo.setAssetAttributesId((short) 99);// 空资产属性
				stockInfo.setEnabled((short) 0);// 不可用
				stockInfoMapper.insertSelective(stockInfo);
				sheetDetail.setPart_id(part_id.toString());
				code = produceEntryService.addSheetDetail(sheetDetail);
			}
		} else {
			code = ErrCode.INCOMPLETE_INFO;
		}
		} catch (Exception e) {
			logger.error("SheetDetailCreate error" + sheetDetail);
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}

	@ResponseBody
	@RequestMapping(value = "/SheetDetailModify", method = { RequestMethod.POST })
	public Map<String, Object> SheetDetailEdit(@RequestBody SheetDetail sheetDetail) {
		logger.info("修改生产入库配件");
		int code = 0;
		try{if (sheetDetail != null && StringUtils.isNotBlank(sheetDetail.getSheet_id())
				&& StringUtils.isNotBlank(sheetDetail.getPart_id())) {
			code = produceEntryService.updateSheetDetail(sheetDetail);
		} else {
			code = ErrCode.INCOMPLETE_INFO;
		}
	} catch (Exception e) {
		logger.error("SheetDetailModify error" + sheetDetail);
	}
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}

	@ResponseBody
	@RequestMapping(value = "/SheetDetailDeleteByCode", method = { RequestMethod.POST })
	public Map<String, Object> SheetDetailDeleteByCode(@RequestBody SheetDetail sheetDetail) {
		logger.info("删除生产入库配件");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sheet_id", sheetDetail.getSheet_id());
		params.put("part_code", sheetDetail.getPart_code());
		int code = 0;
		// 获取sheetInfo
		List<SheetInfo> sheetInfo = new ArrayList<SheetInfo>();
		try{if (sheetDetail != null) {
			sheetInfo = produceEntryService.getSheetById(sheetDetail.getSheet_id());

			// 如果审核未通过，删除库存
			if (sheetInfo.get(0).getReceipt_verify_flag() != (short) VerifyFlagType.VERIFIED.getId()) {

				if (sheetDetail != null && StringUtils.isNotBlank(sheetDetail.getSheet_id())
						&& StringUtils.isNotBlank(sheetDetail.getPart_code())) {
					// 删除库存
					stockInfoMapper.deleteByPrimaryKey(sheetDetail.getPart_code());

				}
			}
		}
		// 删除sheetDetail
		code = produceEntryService.removeSheetDetailByCode(params);
		} catch (Exception e) {
			logger.error("SheetDetailDeleteByCode error" + sheetDetail);
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}

	@ResponseBody
	@RequestMapping(value = "/SheetDetailDeleteById", method = { RequestMethod.POST })
	public Map<String, Object> SheetDetailDeleteById(@RequestBody SheetDetail sheetDetail) {
		logger.info("删除生产入库单据下的配件");
		int code = 0;
		List<SheetInfo> sheetInfo = new ArrayList<SheetInfo>();
try{
		// 获取sheetInfo
		if (sheetDetail != null) {
			// 删除sheetDetail
			code = produceEntryService.removeSheetDetailById(sheetDetail.getSheet_id());
		} else {
			code = ErrCode.INCOMPLETE_INFO;
		}
} catch (Exception e) {
	logger.error("SheetDetailDeleteById error" + sheetDetail);
}
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}

	@ResponseBody
	@RequestMapping(value = "/getAllsuppliers", method = { RequestMethod.GET })
	public DataTable<Supplier> suppliersList(@RequestParam Map<String, Object> params, HttpServletRequest request) {
		logger.info("查询供货商");
		List<Supplier> list = new ArrayList<>();
		DataTable<Supplier> dt = new DataTable<Supplier>();
		try{list = produceEntryService.getAllSuppliers(params);
		dt.setData(list);
		} catch (Exception e) {
			logger.error("getAllsuppliers error" + request);
		}
		dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		return dt;
	}

	// 生产入库查询
	@ResponseBody
	@RequestMapping(value = "/getSheetDetails", method = { RequestMethod.GET })
	public DataTable<SheetDetail> FindSheetDetailsList(HttpServletRequest request,
			@RequestParam Map<String, Object> params) {
		logger.info("生产入库查询");
		DataTable<SheetDetail> dt = new DataTable<SheetDetail>();
		try{dt.setData(produceEntryService.getSheetDetails(params));
		} catch (Exception e) {
			logger.error("getSheetDetails error" + request);
		}
		dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		return dt;
	}

	// 单据审核表显示
	@ResponseBody
	@RequestMapping(value = "/getAllSheetsVerify", method = { RequestMethod.GET })
	public DataTable<SheetInfo> sheetsVerifyList(@RequestParam Map<String, Object> params, HttpServletRequest request) {
		logger.info("显示生产入库审核单据");
		List<SheetInfo> list = new ArrayList<>();
		DataTable<SheetInfo> dt = new DataTable<SheetInfo>();
		try{int total = 0;
		System.out.println(request.getParameter("start"));
		System.out.println(request.getParameter("length"));
		params.put("start", request.getParameter("start"));
		params.put("length", request.getParameter("length"));
		total = produceEntryService.getAllSheetsVerifyCount(params);
		list = produceEntryService.getAllSheetsVerify(params);
		dt.setRecordsTotal(total);
		dt.setRecordsFiltered(total);
		dt.setData(list);
		} catch (Exception e) {
			logger.error("getAllSheetsVerify error" + request);
		}
		dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		return dt;
	}

	// 生产入库单据审核
	@ResponseBody
	@RequestMapping(value = "/modifyVerify", method = { RequestMethod.POST })
	public Map<String, Object> SheetVerify(@RequestBody SheetInfo sheetInfo) {
		logger.info("生产入库单据审核");
		int code = 0;
		List<StockInfo1> list = new ArrayList<StockInfo1>();
		try{if (sheetInfo != null) {
			if (sheetInfo.getReceipt_verify_flag() != 1) {
				Date date = new Date();
				Timestamp verify_date = new Timestamp(date.getTime());
				sheetInfo.setReceipt_verify_date(verify_date);
			}
			/**
			 * 审核通过批量更新库存
			 */
			if (sheetInfo.getReceipt_verify_flag() == (short) VerifyFlagType.VERIFIED.getId()) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("eqSheetId", sheetInfo.getSheet_id());

				// 根据sheetId查询预采购配件详情
				List<com.kthw.pmis.entiy.SheetDetail> sheetDetails = sheetDetailMapper.selectByMap(params);

				// 添加到stockInfo库存数组中
				for (com.kthw.pmis.entiy.SheetDetail sheetDetail : sheetDetails) {
					StockInfo1 stockInfo = new StockInfo1();
					System.out.println("sheetDeatail" + sheetDetail.getPartCode());
					stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
					stockInfo.setStorehouseId((short) StoreHouseEnum.TEST_PRODUCE.getId());
					stockInfo.setPartIdSeq(sheetDetail.getPartId());
					stockInfo.setAssetAttributesId(sheetDetail.getAssetAttributesId());
					stockInfo.setEnabled((short) 1);
					list.add(stockInfo);

				}
				// 批量更新库存
				stockInfoMapper.batchUpdate(list);
			}
			code = produceEntryService.updateSheetVerify(sheetInfo);
		} else {
			code = ErrCode.DB_ERROR;
		}
		} catch (Exception e) {
			logger.error("modifyVerify error" + sheetInfo);
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}

	@ResponseBody
	@RequestMapping(value = "/getMaxSheetId", method = { RequestMethod.GET })
	public String getMaxSheetId(@RequestParam String sheet_id, HttpServletRequest request) {
		logger.info("获取最大sheet_id");
		try{String maxSheet_id = this.produceEntryService.getMaxSheetId(sheet_id);
		if (null == maxSheet_id) {
			maxSheet_id = sheet_id + "00000001";
			return maxSheet_id;

		} else {
			Integer serial = Integer.valueOf(maxSheet_id.substring(7)) + 1;
			String str = String.valueOf(serial);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < 8 - str.length(); ++i) {
				sb.append("0");
			}
			sb.append(serial);

			String sheet_id2 = sheet_id + sb;

			return sheet_id2;

		}
		} catch (Exception e) {
			logger.error("getMaxSheetId error" + request);
		}
		return "";
	}

	@ResponseBody
	@RequestMapping(value = "/getAllAssetAttributes", method = { RequestMethod.GET })
	public DataTable<AssetAttribute> assetAttributesList(@RequestParam Map<String, Object> params,
			HttpServletRequest request) {
		logger.info("查询资产属性");
		List<AssetAttribute> list = new ArrayList<>();
		DataTable<AssetAttribute> dt = new DataTable<AssetAttribute>();
		try{list = produceEntryService.getAllAssetAttributes(params);
		} catch (Exception e) {
			logger.error("getAllAssetAttributesz error" + request);
		}
		dt.setData(list);
		dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		return dt;
	}

	@SuppressWarnings("null")
	@ResponseBody
	@RequestMapping(value = "/getMaxPartId", method = { RequestMethod.GET })
	public String getMaxPartId(HttpServletRequest request, HttpSession httpSession) {
		logger.info("获取最大配件编码");
		Map<String, Object> params = new HashMap<String, Object>();
		try{String partId = request.getParameter("partId");
		params.put("partId", partId);
		String maxPartId = "";
		maxPartId = produceEntryService.getMaxPartId(params);
		if (maxPartId == null || maxPartId == "" || maxPartId.length() < 9) {
			maxPartId = partId.substring(1, 6) + "3000";
		} else {
			Integer serial = Integer.valueOf(maxPartId.substring(5));
			if (serial < 3000) {
				maxPartId = maxPartId.substring(0, 5) + "3000";
			} else {
				maxPartId = maxPartId.substring(0, 5) + String.valueOf(serial + 1);
			}
		}
		return maxPartId;
		} catch (Exception e) {
			logger.error("getMaxPartId error" + request);
		}
		return null;
	}

}
