package com.kthw.pmis.controller.entryAndOut;

import java.io.OutputStream;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kthw.pmis.entiy.PartsZtree;
import com.kthw.pmis.helper.SheetDetailHelper;
import com.kthw.pmis.mapper.common.PartsZtreeMapper;
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
import com.kthw.common.ExcelUtil;
import com.kthw.common.base.ErrCode;
import com.kthw.pmis._enum.SheetInfoType;
import com.kthw.pmis._enum.StoreHouseEnum;
import com.kthw.pmis._enum.VerifyFlagType;
import com.kthw.pmis.entiy.SheetDetail;
import com.kthw.pmis.entiy.SheetInfo;
import com.kthw.pmis.entiy.StockInfo1;
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import com.kthw.pmis.entiy.dto.StockInfoDTO;
import com.kthw.pmis.entiy.ext.SheetInfoExt;
import com.kthw.pmis.mapper.common.SheetDetailMapper;
import com.kthw.pmis.mapper.common.SheetInfoMapper;
import com.kthw.pmis.mapper.common.StockInfo1Mapper;
import com.kthw.pmis.model.entryAndOut.AssetAttribute;
import com.kthw.pmis.model.entryAndOut.Supplier;
import com.kthw.pmis.service.entryAndOut.PurchasePartsService;


/**
 * 描述:采购入库Controller.
 */
@Transactional
@Controller
@RequestMapping(value = "/entryAndOut/purchaseParts")
public class PurchasePartsController {

    private static Logger logger = LoggerFactory.getLogger(PurchasePartsController.class);

    @Autowired
    private PurchasePartsService purchasePartsService;
    @Autowired
    private StockInfo1Mapper stockInfoMapper;
    @Autowired
    private SheetDetailMapper sheetDetailMapper;
    @Autowired
    private SheetInfoMapper sheetInfoMapper;
    @Autowired
    private SheetDetailHelper sheetDetailHelper;
    @Autowired
    private PartsZtreeMapper partsZtreeMapper;
    private final String[] ZEROS = {"000", "00", "0", ""};

    @ResponseBody
    @RequestMapping(value = "/getAllSheets", method = {RequestMethod.GET})
    public DataTable<SheetInfoExt> sheetsList(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        logger.info("显示单据");
        String start = request.getParameter("start");
        String length = request.getParameter("length");
        List<SheetInfoExt> list = new ArrayList<>();
        DataTable<SheetInfoExt> dt = new DataTable<SheetInfoExt>();
        int total = 0;
        try{params.put("sourceStoreHouseId", request.getParameter("sourceStoreHouseId"));
        params.put("queryTime", request.getParameter("queryTime"));
        params.put("queryTime2", request.getParameter("queryTime2"));
        params.put("sheetId", request.getParameter("sheetId"));
        params.put("receiptVerifyFlag", request.getParameter("receiptVerifyFlag"));
        params.put("sheetType", (short) SheetInfoType.PURCHASE.getId());
        params.put("orderByClause", "receipt_verify_flag asc,add_date desc LIMIT " + length + " OFFSET " + start);
        total = sheetInfoMapper.getSheetParamCount(params);// 根据条件获取单据总数
        dt.setRecordsTotal(total);
        dt.setRecordsFiltered(total);
        list = sheetInfoMapper.getAllSheets(params);
        } catch (Exception e) {
            logger.error("getAllSheets error" + request);
        }
        dt.setData(list);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }

    /**
     * 新建单据
     *
     * @param sheetInfo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public Map<String, Object> sheetNew(@RequestBody SheetInfo sheetInfo) {
        logger.info("增加单据");
        int code = 0;
        try{if (sheetInfo != null && StringUtils.isNotBlank(sheetInfo.getSheetId())) {
            if (sheetInfoMapper.selectByPrimaryKey(sheetInfo.getSheetId()) != null) {
                code = ErrCode.SHEET_ALREADY_EXISTS;
            } else {
                sheetInfo.setSourceStoreHouseId((short) StoreHouseEnum.TEST_PREPARE_PURCHASE.getId());
                sheetInfo.setSheetType((short) SheetInfoType.PURCHASE.getId());
                sheetInfo.setObjectStoreHouseId((short) StoreHouseEnum.TEST_PURCHASE.getId());
                sheetInfo.setReceiptVerifyFlag((short) VerifyFlagType.NOVERIFY.getId());
                sheetInfoMapper.insert(sheetInfo);
                code = ErrCode.SUCCESS;
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

    /**
     * 修改单据
     */
    @ResponseBody
    @RequestMapping(value = "/modify", method = {RequestMethod.POST})
    public Map<String, Object> partsManageEdit(@RequestBody SheetInfo sheetInfo) {
        logger.info("修改单据");
        int code = 0;
        try{if (sheetInfo != null) {
            sheetInfoMapper.updateByPrimaryKeySelective(sheetInfo);
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

    /**
     * 删除单据
     *
     * @param sheetInfo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public Map<String, Object> partsManageDelete(@RequestBody SheetInfo sheetInfo) {
        logger.info("删除单据");
        int code = 0;
        List<StockInfo1> list = new ArrayList<StockInfo1>();
        String sheetId = sheetInfo.getSheetId();
        try{if (sheetInfo != null && sheetId != null) {
            SheetInfo info = sheetInfoMapper.selectByPrimaryKey(sheetId);
            if (info.getReceiptVerifyFlag() != (short) VerifyFlagType.VERIFIED.getId()) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("eqSheetId", sheetInfo.getSheetId());
                // 根据sheetId查询预采购配件详情
                List<SheetDetail> sheetDetails = sheetDetailMapper.selectByMap(params);

                // 添加到stockInfo库存数组中
                for (SheetDetail sheetDetail : sheetDetails) {
                    StockInfo1 stockInfo = new StockInfo1();
                    stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
                    stockInfo.setStorehouseId((short) StoreHouseEnum.TEST_PREPARE_PURCHASE.getId());
                    stockInfo.setPartIdSeq(sheetDetail.getPartId().substring(0, 5));

                    stockInfo.setEnabled((short) 1);
                    list.add(stockInfo);

                }
                // 批量更新库存
                stockInfoMapper.batchUpdate(list);


                sheetInfoMapper.deleteByPrimaryKey(sheetId);
                List<SheetDetail> sd = sheetDetailMapper.selectByMap(params);
                if (sd.size() > 0) {
                    sheetDetailMapper.deleteBySheetId(sheetId);

                }
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

    /**
     * 修改配件
     *
     * @param sheetDetail
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sheetDetailModify", method = {RequestMethod.POST})
    public Map<String, Object> sheetDetailModify(@RequestBody SheetDetail sheetDetail) {
        logger.info("修改采购入库配件");
        int code = 0;
        try{if (sheetDetail.getSheetId() != null
                && sheetDetail.getPartCode() != null) {
            sheetDetailMapper.updateByPrimaryKeySelective(sheetDetail);


        } else {
            code = ErrCode.INCOMPLETE_INFO;
        }
        } catch (Exception e) {
            logger.error("sheetDetailModify error" + sheetDetail);
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    @ResponseBody
    @RequestMapping(value = "/getAllSheetDetails", method = {RequestMethod.GET})
    public DataTable<SheetDetailDTO> SheetDetailsList(HttpServletRequest request,
                                                      @RequestParam Map<String, Object> params) {
        logger.info("显示配件详情");
        DataTable<SheetDetailDTO> dt = new DataTable<SheetDetailDTO>();
        List<SheetDetailDTO> data = new ArrayList<SheetDetailDTO>();
        String sheetId = request.getParameter("sheetId");
        // 为空直接返回
        try{if (sheetId == null || sheetId == "") {
            dt.setData(data);
            dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);

            return dt;
        }
        params.put("eqSheetId", sheetId);
        params.put("sheetType", (short) SheetInfoType.PURCHASE.getId());
        dt.setData(sheetDetailMapper.selectWithParts(params));
        } catch (Exception e) {
            logger.error("getAllSheetDetails error" + request);
        }
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }

    @ResponseBody
    @RequestMapping(value = "/SheetDetailCreate", method = {RequestMethod.POST})
    public synchronized Map<String, Object> SheetDetailNew(@RequestBody SheetDetail sheetDetail) {
        logger.info("增加入库配件");
        int code = 0;
        Map<String, Object> params = new HashMap<>();
        params.put("eqPartIdSeq", sheetDetail.getPartId());
        try{List<StockInfo1> records = stockInfoMapper.selectByMap(params);
        if (records.size() > 0) {
            code = ErrCode.PART_ID_IS_EXIST;
        } else {

            if (sheetDetail != null && StringUtils.isNotBlank(sheetDetail.getSheetId())
                    && StringUtils.isNotBlank(sheetDetail.getPartId())
                    && StringUtils.isNotBlank(sheetDetail.getPartCode())) {

                StockInfo1 stockInfo = new StockInfo1();
                stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
                stockInfo.setStorehouseId((short) StoreHouseEnum.TEST_PURCHASE.getId());
                stockInfo.setPartIdSeq(sheetDetail.getPartId());
                stockInfo.setEnabled((short) 0);//不可用
                stockInfoMapper.updateByPrimaryKeySelective(stockInfo);

                //插入sheetDetail
                try {
                    sheetDetail = sheetDetailHelper.repalce(sheetDetail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sheetDetail.setPurchaseDate(new Date());
                sheetDetail.setWarranty((short) 1);//质保期内
                sheetDetail.setPartState((short) 1);//配件属性为新购
                sheetDetailMapper.insert(sheetDetail);

            } else {
                code = ErrCode.INCOMPLETE_INFO;
            }
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
    @RequestMapping(value = "/SheetDetailModify", method = {RequestMethod.POST})
    public Map<String, Object> SheetDetailEdit(@RequestBody SheetDetail sheetDetail) {
        logger.info("修改入库配件");
        int code = 0;
       try{ if (sheetDetail != null && StringUtils.isNotBlank(sheetDetail.getSheetId())
                && StringUtils.isNotBlank(sheetDetail.getPartId())) {
            sheetDetailMapper.updateByPrimaryKeySelective(sheetDetail);
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
    @RequestMapping(value = "/SheetDetailDeleteByCode", method = {RequestMethod.POST})
    public Map<String, Object> SheetDetailDeleteByCode(@RequestBody SheetDetail sheetDetail) {
        logger.info("删除入库配件");
        int code = 0;

try{
        //获取sheetInfo
        if (sheetDetail != null) {
            SheetInfo sheetInfo = sheetInfoMapper.selectByPrimaryKey(sheetDetail.getSheetId());

            //如果审核未通过，回退库存到预采购库
            if (sheetInfo.getReceiptVerifyFlag() != (short) VerifyFlagType.VERIFIED.getId()) {
                if (sheetDetail != null && StringUtils.isNotBlank(sheetDetail.getSheetId())
                        && StringUtils.isNotBlank(sheetDetail.getPartId())
                        && StringUtils.isNotBlank(sheetDetail.getPartCode())) {
                    StockInfo1 stockInfo = new StockInfo1();
                    stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
                    stockInfo.setStorehouseId((short) StoreHouseEnum.TEST_PREPARE_PURCHASE.getId());
                    stockInfo.setPartIdSeq(sheetDetail.getPartId().substring(0, 5));
                    stockInfo.setEnabled((short) 1);//可用
                    stockInfoMapper.updateByPrimaryKeySelective(stockInfo);


                    //删除sheetDetail

                }
            }


            sheetDetailMapper.deleteByPrimaryKey(sheetDetail);

        }
} catch (Exception e) {
    logger.error("SheetDetailDeleteByCode error" + sheetDetail);
}

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    @ResponseBody
    @RequestMapping(value = "/SheetDetailDeleteById", method = {RequestMethod.POST})
    public Map<String, Object> SheetDetailDeleteById(@RequestBody SheetDetail sheetDetail) {
        logger.info("删除入库单据下的配件");
        int code = 0;
        List<StockInfo1> list = new ArrayList<StockInfo1>();
        String sheetId = sheetDetail.getSheetId();
        try{if (sheetDetail != null) {

            SheetInfo sheetInfo = sheetInfoMapper.selectByPrimaryKey(sheetId);
            if (sheetInfo.getReceiptVerifyFlag() != (short) VerifyFlagType.VERIFIED.getId()) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("eqSheetId", sheetInfo.getSheetId());
                // 根据sheetId查询预采购配件详情
                List<SheetDetail> sheetDetails = sheetDetailMapper.selectByMap(params);

                // 添加到stockInfo库存数组中
                for (SheetDetail sheetDetailDTO : sheetDetails) {
                    StockInfo1 stockInfo = new StockInfo1();
                    stockInfo.setFactoryPartsCode(sheetDetailDTO.getPartCode());
                    stockInfo.setStorehouseId((short) StoreHouseEnum.TEST_PREPARE_PURCHASE.getId());
                    stockInfo.setPartIdSeq(sheetDetailDTO.getPartId().substring(0, 5));
                    stockInfo.setEnabled((short) 1);
                    list.add(stockInfo);

                }
                // 批量更新库存
                stockInfoMapper.batchUpdate(list);

                //删除单据下的所有sheetDetail
                List<SheetDetail> sd = sheetDetailMapper.selectByMap(params);
                if (sd.size() > 0) {
                    sheetDetailMapper.deleteBySheetId(sheetId);


                }
            } else {
                code = ErrCode.INCOMPLETE_INFO;
            }
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
    @RequestMapping(value = "/getAllsuppliers", method = {RequestMethod.GET})
    public DataTable<Supplier> suppliersList(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        logger.info("查询供货商");
        List<Supplier> list = new ArrayList<>();
        DataTable<Supplier> dt = new DataTable<Supplier>();
        try{list = purchasePartsService.getAllSuppliers(params);
        } catch (Exception e) {
            logger.error("getAllsuppliers error" + request);
        }
        dt.setData(list);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }

    // 采购入库查询
    @ResponseBody
    @RequestMapping(value = "/getSheetDetails", method = {RequestMethod.GET})
    public DataTable<SheetDetailDTO> FindSheetDetailsList(HttpServletRequest request,
                                                          @RequestParam Map<String, Object> params) {
        logger.info("采购入库查询");
        DataTable<SheetDetailDTO> dt = new DataTable<SheetDetailDTO>();
        try{dt.setData(sheetDetailMapper.selectWithParts(params));
        } catch (Exception e) {
            logger.error("getSheetDetails error" + request);
        }
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }


    // 采购单据审核
    @ResponseBody
    @RequestMapping(value = "/modifyVerify", method = {RequestMethod.POST})
    public Map<String, Object> SheetVerify(@RequestBody SheetInfo sheetInfo) {
        logger.info("采购单据审核");
        int code = 0;
        List<StockInfo1> list = new ArrayList<StockInfo1>();
        try{if (sheetInfo != null) {
            if (sheetInfo.getReceiptVerifyFlag() != 1) {
                Date date = new Date();
                Timestamp verify_date = new Timestamp(date.getTime());
                sheetInfo.setReceiptVerifyDate(verify_date);
            }

            /**
             * 审核通过批量更新库存
             */
            if (sheetInfo.getReceiptVerifyFlag() != null &&
                    sheetInfo.getReceiptVerifyFlag() == (short) VerifyFlagType.VERIFIED.getId()) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("eqSheetId", sheetInfo.getSheetId());

                // 根据sheetId查询预采购配件详情
                List<SheetDetail> sheetDetails = sheetDetailMapper.selectByMap(params);

                // 添加到stockInfo库存数组中
                for (SheetDetail sheetDetail : sheetDetails) {
                    StockInfo1 stockInfo = new StockInfo1();
                    stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
                    stockInfo.setStorehouseId((short) StoreHouseEnum.TEST_PURCHASE.getId());
                    stockInfo.setPartIdSeq(sheetDetail.getPartId());
                    stockInfo.setSheetId(sheetDetail.getSheetId());
                    stockInfo.setPartsState((short) 1);//配件属性为新购
                    stockInfo.setEnabled((short) 1);//可用
                    stockInfo.setPurchasePrice(sheetDetail.getUnitPrice());//新购单价
                    stockInfo.setPurchaseDate(sheetDetail.getPurchaseDate());//新购时间
                    list.add(stockInfo);

                }
                // 批量更新库存
                stockInfoMapper.batchUpdate(list);
            }
            sheetInfoMapper.updateByPrimaryKeySelective(sheetInfo);
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
    @RequestMapping(value = "/getMaxSheetId", method = {RequestMethod.GET})
    public synchronized String getMaxSheetId(@RequestParam String sheet_id, HttpServletRequest request) {
        logger.info("获取最大sheet_id");
        String maxSheet_id = this.purchasePartsService.getMaxSheetId(sheet_id);
        try{if (null == maxSheet_id) {
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
        return  null;
    }

    @ResponseBody
    @RequestMapping(value = "/getAllAssetAttributes", method = {RequestMethod.GET})
    public DataTable<AssetAttribute> assetAttributesList(@RequestParam Map<String, Object> params,
                                                         HttpServletRequest request) {
        logger.info("查询资产属性");
        List<AssetAttribute> list = new ArrayList<>();
        DataTable<AssetAttribute> dt = new DataTable<AssetAttribute>();
       try{list = purchasePartsService.getAllAssetAttributes(params);
       } catch (Exception e) {
           logger.error("getAllAssetAttributes error" + request);
       }
        dt.setData(list);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }

    // ExcelUtil导出报表
    @RequestMapping(value = "exportSheetInfo", method = {RequestMethod.GET})
    @ResponseBody
    public void exportSheetInfo(@RequestParam Map<String, Object> params, HttpServletRequest request,
                                HttpServletResponse response) {
        logger.info("新购关键配件交接单---ExcelUtil导出报表");
        System.out.println("sssssssss");
        SheetInfo sheetInfo = sheetInfoMapper.selectByPrimaryKey((String) params.get("sheetId"));// 获取单据信息
        List<SheetDetailDTO> sheetDetailList = sheetDetailMapper.selectWithParts(params);// 获取单据对应的配件详情
        System.out.println("sssssssss");
        String file_name = "车辆运行安全监控系统设备新购关键配件交接单";
        String[] title = {"编号", "关键配件名称", "设备型号", "设备类型", "生产厂家", "配件出厂编码", "配件二维码", "资产配属", "数量", "单价", "备注"};
        HSSFWorkbook wb = ExcelUtil.exportSheetInfoAsExcel(file_name, title, sheetDetailList, sheetInfo, null);
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
            logger.error("exportSheetInfo ERROR :" + e);
            e.printStackTrace();
        }
    }


    @ResponseBody
    @RequestMapping(value = "/getAllParts", method = {RequestMethod.GET})
    public DataTable<SheetDetailDTO> partsManageList(HttpServletRequest request, HttpSession httpSession) {
        logger.info("显示预购库配件");
        List<SheetDetailDTO> list = new ArrayList<SheetDetailDTO>();
        DataTable<SheetDetailDTO> dt = new DataTable<SheetDetailDTO>();
        int total = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        String partId = request.getParameter("partId");
        String partCode = request.getParameter("partCode");
        String devicePartsName = request.getParameter("devicePartsName");
        String deviceModelName = request.getParameter("deviceModelName");
        String deviceTypeName = request.getParameter("deviceTypeName");
        String supplierName = request.getParameter("supplierName");
        params.put("eqStorehouseId", (short) StoreHouseEnum.TEST_PREPARE_PURCHASE.getId());
        params.put("partIdSeq", partId);
        params.put("eqFactoryPartsCode", partCode);
        params.put("devicePartsName", devicePartsName);
        params.put("deviceModelName", deviceModelName);
        params.put("supplierName", supplierName);
        params.put("deviceTypeName", deviceTypeName);
        //获取配件数量
        //total=stockInfoMapper.getPartsParamCount(params);
        //获取配件详细信息

        list = sheetDetailMapper.selectStockWithSheetId(params);

        //返回dataTable参数
        dt.setRecordsTotal(total);
        dt.setRecordsFiltered(total);
        dt.setData(list);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }

    /**
     * 根据sheetId获取单据详情
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getPartsZtree", method = {RequestMethod.GET})
    public List<PartsZtree> getPartsZtree(HttpServletRequest request) {
        logger.info("配件查询树形表");

        Map<String, Object> params = new HashMap<>();
        List<PartsZtree> list = new ArrayList<PartsZtree>();
        params.put("storeHouseId", "14");//预采购库
        list = partsZtreeMapper.getPartCount(params);

        return list;
    }

    @ResponseBody
    @RequestMapping(value = "/getMaxPartId", method = {RequestMethod.POST})
    public synchronized String getMaxPartId(HttpServletRequest request,@RequestBody SheetDetail sheetDetail) {
        logger.info("获取最大配件编码");
        if(sheetDetail!=null&&sheetDetail.getPartId()!=null) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("partId", sheetDetail.getPartId());
            String maxPartId = "";
            System.out.println(maxPartId);
            maxPartId = purchasePartsService.getMaxPartId(params);
            System.out.println(maxPartId);
            if (maxPartId == null || maxPartId == "" || maxPartId.length() < 9) {
                maxPartId = sheetDetail.getPartId().substring(0, 5) + "3001";

            } else {
                Integer serial = Integer.valueOf(maxPartId.substring(5));
                if (serial <= 3000) {
                    maxPartId = maxPartId.substring(0, 5) + "3001";
                } else {
                    maxPartId = maxPartId.substring(0, 5) + String.valueOf(serial + 1);
                }
            }

            return maxPartId;
        }
        return null;

    }
}
