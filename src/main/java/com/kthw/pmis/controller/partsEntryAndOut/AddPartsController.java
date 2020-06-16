package com.kthw.pmis.controller.partsEntryAndOut;

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

import com.kthw.pmis.entiy.*;
import com.kthw.pmis.helper.DepotHelper;
import com.kthw.pmis.helper.SheetDetailHelper;
import com.kthw.pmis.mapper.common.*;
import com.kthw.pmis.model.entryAndOut.AssetAttribute;
import com.kthw.pmis.model.system.Parts;
import com.kthw.pmis.service.system.PartsService;
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
import com.kthw.pmis._enum.VerifyFlagType;
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import com.kthw.pmis.entiy.ext.SheetInfoExt;


/**
 * 描述：配件新增Controller.
 */
@Transactional
@Controller
@RequestMapping(value = "/partsEntryAndOut/addParts")
public class AddPartsController {

    private static Logger logger = LoggerFactory.getLogger(AddPartsController.class);
    @Autowired
    private PartsService partsService;
    @Autowired
    private DepotMapper depotMapper;
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
    @Autowired
    private StoreHouseMapper storeHouseMapper;
    @Autowired
    private PartIdConfigMapper partIdConfigMapper;
    @Autowired
    private DepotHelper depotHelper;
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
        try {
            params.put("objectStoreHouseId", request.getParameter("objectStoreHouseId"));
            params.put("queryTime", request.getParameter("queryTime"));
            params.put("queryTime2", request.getParameter("queryTime2"));
            params.put("sheetId", request.getParameter("sheetId"));
            params.put("receiptVerifyFlag", request.getParameter("receiptVerifyFlag"));
            params.put("sheetType", (short) SheetInfoType.PARTS_ADD.getId());
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

    @ResponseBody
    @RequestMapping(value = "/getPartIpConfig", method = {RequestMethod.POST})
    public List<PartIdConfig> getPartIpConfig(@RequestBody PartIdConfig partIdConfig) {
        logger.info("获取车间配件范围");
        List<PartIdConfig> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        Long workShopDepotId=depotHelper.getWorkShop(partIdConfig.getDepotId());
        params.put("eqDepotId", workShopDepotId);
        list = partIdConfigMapper.selectByMap(params);
        return list;
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
        try {
            if (sheetInfo != null && StringUtils.isNotBlank(sheetInfo.getSheetId())) {
                SheetInfo info = sheetInfoMapper.selectByPrimaryKey(sheetInfo.getSheetId());
                if (info != null) {
                    code = ErrCode.SHEET_ALREADY_EXISTS;
                } else {
                    AssetAttributes assetAttribute = depotHelper.getAssetAttribute(sheetInfo.getDepotId());
                    sheetInfo.setSheetType((short) SheetInfoType.PARTS_ADD.getId());
                    sheetInfo.setAssetAttributesId(assetAttribute.getAssetAttributesId());
                    sheetInfo.setReceiptVerifyFlag((short) VerifyFlagType.NOVERIFY.getId());
                    sheetInfoMapper.insert(sheetInfo);
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
        try {
            if (sheetInfo != null) {
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
        try {
            if (sheetInfo != null && sheetId != null) {
                SheetInfo info = sheetInfoMapper.selectByPrimaryKey(sheetId);
                if (info.getReceiptVerifyFlag() != (short) VerifyFlagType.VERIFIED.getId()) {
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("eqSheetId", sheetInfo.getSheetId());
                    sheetInfoMapper.deleteByPrimaryKey(sheetId);
                    List<SheetDetail> sd = sheetDetailMapper.selectByMap(params);
                    for (SheetDetail sheetDetail : sd) {
                        StockInfo1 stockInfo = new StockInfo1();
                        stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
                        list.add(stockInfo);
                    }
                    stockInfoMapper.batchDelete(list);
                }
                sheetDetailMapper.deleteBySheetId(sheetInfo.getSheetId());
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
    public Map<String, Object> sheetDetailModify(@RequestBody SheetDetailDTO sheetDetail) {
        logger.info("修改采购入库配件");
        int code = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        Date date = new Date();
        try {
            if (sheetDetail.getSheetId() != null
                    && sheetDetail.getSourcePartCode() != null
                    && sheetDetail.getPartCode() != null) {
                params.put("sourcePartCode", sheetDetail.getSourcePartCode());
                params.put("sheetId", sheetDetail.getSheetId());
                params.put("partCode", sheetDetail.getPartCode());
                params.put("remark", sheetDetail.getRemark());
                params.put("updateTime", date);
                sheetDetailMapper.updatePartCode(params);


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
        try {
            if (sheetId == null || sheetId == "") {
                dt.setData(data);
                dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);

                return dt;
            }
            params.put("eqSheetId", sheetId);
            params.put("sheetType", (short) SheetInfoType.PARTS_ADD.getId());
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
        Map<String, Object> ret = new HashMap<String, Object>();
        Date date = new Date();
        try {
            //获取单据
            SheetInfo info = sheetInfoMapper.selectByPrimaryKey(sheetDetail.getSheetId());

            params.put("eqPartIdSeq", sheetDetail.getPartId());
            List<StockInfo1> records = stockInfoMapper.selectByMap(params);
            //查看是否存在出厂编码
            params.clear();
            params.put("eqPartCode", sheetDetail.getPartCode());


            List<SheetDetail> sheetDetailDTOs = sheetDetailMapper.selectByMap(params);
            if (sheetDetailDTOs.size() > 0) {
                code = ErrCode.PARTCODE_ALREADY_EXISTS;
                ret.put("code", code);
                ret.put("msg", ErrCode.getMessage(code));
                return ret;
            }
            if (records.size() > 0) {
                code = ErrCode.PART_ID_IS_EXIST;
                ret.put("code", code);
                ret.put("msg", ErrCode.getMessage(code));
                return ret;
            }
            if (sheetDetail != null && StringUtils.isNotBlank(sheetDetail.getSheetId())
                    && StringUtils.isNotBlank(sheetDetail.getPartId())
                    && StringUtils.isNotBlank(sheetDetail.getPartCode())) {

                //插入sheetDetail
                sheetDetail.setPurchaseDate(new Date());
                sheetDetailMapper.insert(sheetDetail);
                //插入库存
                StockInfo1 stockInfo = new StockInfo1();
                stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
                stockInfo.setStorehouseId(info.getObjectStoreHouseId());//采购库
                stockInfo.setPartIdSeq(sheetDetail.getPartId());
                stockInfo.setSheetId(sheetDetail.getSheetId());
                stockInfo.setPurchaseDate(date);
                if(info.getAssetAttributesId()!=null)
                stockInfo.setAssetAttributesId(info.getAssetAttributesId());
                if(sheetDetail.getUnitPrice()!=null)
                stockInfo.setPurchasePrice(sheetDetail.getUnitPrice());
                stockInfo.setPartsState((short) 1);//配件属性为新购
                stockInfo.setWarranty(sheetDetail.getWarranty());//新购是质保期
                stockInfo.setEnabled((short) 0);//可用
                //插入库存
                stockInfoMapper.insert(stockInfo);
                ret.put("code", code);
                ret.put("msg", ErrCode.getMessage(code));
                return ret;
            }
            code = ErrCode.INCOMPLETE_INFO;


        } catch (Exception e) {
            logger.error("SheetDetailCreate error" + sheetDetail);
        }
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    @ResponseBody
    @RequestMapping(value = "/SheetDetailModify", method = {RequestMethod.POST})
    public Map<String, Object> SheetDetailEdit(@RequestBody SheetDetail sheetDetail) {
        logger.info("修改入库配件");
        int code = 0;
        try {
            if (sheetDetail != null && StringUtils.isNotBlank(sheetDetail.getSheetId())
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

        try {
            //获取sheetInfo
            if (sheetDetail != null) {
                SheetInfo sheetInfo = sheetInfoMapper.selectByPrimaryKey(sheetDetail.getSheetId());

                //如果审核未通过，回退库存到预采购库
                if (sheetInfo.getReceiptVerifyFlag() != (short) VerifyFlagType.VERIFIED.getId()) {
                    stockInfoMapper.deleteByPrimaryKey(sheetDetail.getPartCode());
                    sheetDetailMapper.deleteByPrimaryKey(sheetDetail);

                }
            }
        } catch (Exception e) {
            logger.error("SheetDetailDeleteByCode error" + sheetDetail);
        }

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    /**
     * 查询页面用
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/searchSheets", method = {RequestMethod.GET})
    public DataTable<SheetInfoExt> searchSheets(HttpServletRequest request) {
        logger.info("显示配件拆卸单据");
        String start = request.getParameter("start");
        String length = request.getParameter("length");
        List<SheetInfoExt> list = new ArrayList<>();
        DataTable<SheetInfoExt> dt = new DataTable<SheetInfoExt>();
        int total = 0;
        Map<String, Object> params = new HashMap<>();
        params.put("queryTime", request.getParameter("queryTime"));
        params.put("queryTime2", request.getParameter("queryTime2"));
        params.put("sheetId", request.getParameter("sheetId"));
        params.put("sheetType", (short) SheetInfoType.PARTS_ADD.getId());
        params.put("orderByClause", "receipt_verify_flag asc,add_date desc LIMIT " + length + " OFFSET " + start);
        String objectDepotId = request.getParameter("objectDepotId");
        //获取子部门
        if (objectDepotId != null) {
            List<Depot> childrens = depotHelper.getChildrens(Long.valueOf(objectDepotId));
            params.put("objectDepotIdIn", childrens);
            total = sheetInfoMapper.getSheetParamCount(params);// 根据条件获取单据总数

            dt.setRecordsTotal(total);
            dt.setRecordsFiltered(total);
            list = sheetInfoMapper.getAllSheets(params);
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
        try {
            dt.setData(sheetDetailMapper.selectWithParts(params));
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
        Map<String, Object> params = new HashMap<String, Object>();
        List<StockInfo1> list = new ArrayList<StockInfo1>();
        Date date = new Date();
        try {
            if (sheetInfo != null) {
                params.put("eqSheetId", sheetInfo.getSheetId());
                // 根据sheetId查询预采购配件详情
                List<SheetDetail> sheetDetails = sheetDetailMapper.selectByMap(params);
                if (sheetDetails.size() > 0) {

                    if (sheetInfo.getReceiptVerifyFlag() != null && sheetInfo.getReceiptVerifyFlag()
                            == (short) VerifyFlagType.VERIFIED.getId()) {
                        // 添加到stockInfo库存数组中
                        for (SheetDetail sheetDetail : sheetDetails) {
                            StockInfo1 stockInfo = new StockInfo1();
                            stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
                            stockInfo.setEnabled((short) 1);//可用
                            list.add(stockInfo);

                        }
                        // 批量插入库存
                        stockInfoMapper.batchUpdate(list);
                    }
                    //更新sheetInfo
                    sheetInfoMapper.updateByPrimaryKeySelective(sheetInfo);

                    //sheetDetail为空返回，不执行更新操作
                } else {
                    code = ErrCode.SHEETDETAIL_IS_NONE;
                }

            } else {
                code = ErrCode.DB_ERROR;
            }
        } catch (Exception e) {
            logger.error("sheetInfoModify error" + sheetInfo);
        }
        params.clear();
        params.put("code", code);
        params.put("msg", ErrCode.getMessage(code));
        return params;
    }


    // ExcelUtil导出报表
    @RequestMapping(value = "exportSheetInfo", method = {RequestMethod.GET})
    @ResponseBody
    public void exportSheetInfo(@RequestParam Map<String, Object> params, HttpServletRequest request,
                                HttpServletResponse response) {
        logger.info("新增配件---ExcelUtil导出报表");
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
    /**
     * 根据sheetId获取单据详情
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getPartsByPartIdConfig", method = {RequestMethod.GET})
    public DataTable<Parts> getPartsByPartIdConfig(HttpServletRequest request) {
        logger.info("根据配置配件查询配件列表");
        String depotId=request.getParameter("depotId");
        Map<String, Object> params = new HashMap<>();
        List<PartIdConfig> list = new ArrayList<PartIdConfig>();
        List<Parts> parts=new ArrayList<>();
        DataTable<Parts> dt = new DataTable<Parts>();
        if(StringUtils.isNotBlank(depotId)){
            Long workShopDepotId=depotHelper.getWorkShop(Long.valueOf(depotId));
            params.put("eqDepotId",Long.valueOf(workShopDepotId));
            list=partIdConfigMapper.selectByMap(params);
            for (PartIdConfig partIdConfig:list){
                params.clear();
                params.put("deviceModelName",partIdConfig.getDeviceModelName());
                params.put("supplierName",partIdConfig.getSupplierName());
                parts.addAll(partsService.getAllParts(params));
            }
        }
        dt.setData(parts);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }
    /**
     * 获取对应库存的配件信息
     *
     * @param
     * @param request
     * @return StockInfoDTO
     */
    @ResponseBody
    @RequestMapping(value = "/getAllObjectStoreHouse", method = {RequestMethod.POST})
    public DataTable<StoreHouse> getAllReceiptStoreHouse(
            HttpServletRequest request, @RequestBody StoreHouse storeHouse) {
        logger.info("获取所有收货库信息");
        List<StoreHouse> list = new ArrayList<StoreHouse>();
        DataTable<StoreHouse> dt = new DataTable<StoreHouse>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("eqType", storeHouse.getType());//收货库类型
        params.put("eqDepotId", storeHouse.getDepotId());//收货库类型
        //获取收货库
        list = storeHouseMapper.selectByMap(params);
        //返回dataTable参数
        dt.setData(list);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }

}
