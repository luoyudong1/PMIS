
package com.kthw.pmis.controller.detectProduce;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kthw.pmis.entiy.dto.DetectDeviceDTO;
import com.kthw.pmis.model.stock.StockInfo;
import com.kthw.pmis.service.detectManage.DetectManageService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import com.kthw.pmis.entiy.Depot;
import com.kthw.pmis.entiy.DetectDevice;
import com.kthw.pmis.entiy.DetectParts;
import com.kthw.pmis.entiy.SheetDetail;
import com.kthw.pmis.entiy.SheetInfo;
import com.kthw.pmis.entiy.StockInfo1;
import com.kthw.pmis.entiy.StoreHouse;
import com.kthw.pmis.entiy.dto.DetectPartsDTO;
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import com.kthw.pmis.entiy.ext.SheetInfoExt;
import com.kthw.pmis.helper.DepotHelper;
import com.kthw.pmis.mapper.common.DepotMapper;
import com.kthw.pmis.mapper.common.DetectDeviceMapper;
import com.kthw.pmis.mapper.common.DetectPartsMapper;
import com.kthw.pmis.mapper.common.SheetDetailMapper;
import com.kthw.pmis.mapper.common.SheetInfoMapper;
import com.kthw.pmis.mapper.common.StockInfo1Mapper;
import com.kthw.pmis.mapper.common.StoreHouseMapper;

@Controller
@RequestMapping(value = "/detectProduce/partsUnstall")
public class PartsUnstallController {
    private static Logger logger = LoggerFactory.getLogger(PartsUnstallController.class);
    @Autowired
    private SheetInfoMapper sheetInfoMapper;
    @Autowired
    private SheetDetailMapper sheetDetailMapper;
    @Autowired
    private StockInfo1Mapper stockInfoMapper;
    @Autowired
    private StoreHouseMapper storeHouseMapper;
    @Autowired
    private DetectDeviceMapper detectDeviceMapper;
    @Autowired
    private DetectPartsMapper detectPartsMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private DepotHelper depotHelper;
    @Autowired
    private DetectManageService detectManageService;

    @ResponseBody
    @RequestMapping(value = "/getAllSheets", method = {RequestMethod.GET})
    public DataTable<SheetInfoExt> sheetsList(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        logger.info("显示配件使用安装单据");
        String start = request.getParameter("start");
        String length = request.getParameter("length");
        List<SheetInfoExt> list = new ArrayList<>();
        DataTable<SheetInfoExt> dt = new DataTable<SheetInfoExt>();
        int total = 0;
        try {
            params.put("sourceStoreHouseId", request.getParameter("sourceStoreHouseId"));
            params.put("queryTime", request.getParameter("queryTime"));
            params.put("queryTime2", request.getParameter("queryTime2"));
            params.put("sheetId", request.getParameter("sheetId"));
            params.put("sendVerifyFlag", request.getParameter("sendVerifyFlag"));
            params.put("sheetType", (short) SheetInfoType.FAULTREMOVEL.getId());
            params.put("orderByClause", "send_verify_flag asc,add_date desc LIMIT " + length + " OFFSET " + start);
            String sourceDepotId = request.getParameter("sourceDepotId");
            String objectDepotId = request.getParameter("objectDepotId");
            if (sourceDepotId != null) {
                params.put("sourceDepotId", Short.valueOf(sourceDepotId));
            }
            if (objectDepotId != null) {
                params.put("objectDepotId", Short.valueOf(objectDepotId));
            }
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
    @RequestMapping(value = "/createSheetInfo", method = {RequestMethod.POST})
    public Map<String, Object> createSheet(@RequestBody SheetInfo sheetInfo) {
        logger.info("新增配件使用安装单据");
        int code = 0;
        try {
            if (sheetInfo != null && StringUtils.isNotBlank(sheetInfo.getSheetId())) {
                SheetInfo info = sheetInfoMapper.selectByPrimaryKey(sheetInfo.getSheetId());
                if (info != null) {
                    code = ErrCode.SHEET_ALREADY_EXISTS;
                } else {
                    sheetInfo.setSheetType((short) SheetInfoType.FAULTREMOVEL.getId());
                    sheetInfo.setSourceStoreHouseId(sheetInfo.getSourceStoreHouseId());
                    sheetInfo.setObjectStoreHouseId(sheetInfo.getObjectStoreHouseId());
                    sheetInfo.setSendVerifyFlag((short) VerifyFlagType.NOVERIFY.getId());
                    sheetInfo.setDeviceId(sheetInfo.getDeviceId());
                    sheetInfo.setDeviceName(sheetInfo.getDeviceName());
                    sheetInfoMapper.insert(sheetInfo);
                }

            } else {
                code = ErrCode.INCOMPLETE_INFO;
            }
        } catch (Exception e) {
            logger.error("createSheetInfo error" + sheetInfo);
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    @ResponseBody
    @RequestMapping(value = "/modify", method = {RequestMethod.POST})
    public Map<String, Object> modify(@RequestBody SheetInfo sheetInfo) {
        logger.info("修改或审核配件拆卸单据");
        int code = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        List<StockInfo1> list = new ArrayList<StockInfo1>();
        List<DetectParts> detectPartsList = new ArrayList<DetectParts>();
        try {
            if (sheetInfo != null) {
                SheetInfo info = sheetInfoMapper.selectByPrimaryKey(sheetInfo.getSheetId());

                if (sheetInfo.getSendVerifyFlag() != null && sheetInfo.getSendVerifyFlag() == (short) VerifyFlagType.VERIFIED.getId()) {
                    params.put("eqSheetId", info.getSheetId());

                    // 根据sheetId查询所调拨配送到段配件详情
                    List<SheetDetail> sheetDetails = sheetDetailMapper.selectByMap(params);
                    // 添加到stockInfo库存数组中
                    for (SheetDetail sheetDetail : sheetDetails) {
                        StockInfo1 stockInfo = new StockInfo1();
                        stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
                        stockInfo.setStorehouseId(info.getObjectStoreHouseId());
                        stockInfo.setPartIdSeq(sheetDetail.getPartId());
                        stockInfo.setSheetId(info.getSheetId());
                        stockInfo.setPartsState((short) 2);//配件属性为送修
                        stockInfo.setWarranty(sheetDetail.getWarranty());//是否质保期
                        stockInfo.setEnabled((short) 1);
                        list.add(stockInfo);
                        //更新探测站设备状态
                        DetectParts detectPart=new DetectParts();
                        detectPart.setDetectPartsId(sheetDetail.getDetectPartsId());
                        detectPart.setEnabled((short)1);
                        detectPart.setIfMoved((short)1);
                        detectPartsList.add(detectPart);

                    }
                    //更新探测站设备状态
                    detectPartsMapper.batchUpdate(detectPartsList);
                    // 批量更新库存
                    stockInfoMapper.batchUpdateByPartsId(list);
                    //新建安装单据
                }
                sheetInfoMapper.updateByPrimaryKeySelective(sheetInfo);
            }
        } catch (Exception e) {
            logger.error("modify error" + sheetInfo);
        }
        params.clear();
        params.put("code", code);
        params.put("msg", ErrCode.getMessage(code));
        return params;
    }

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
        params.put("sheetType", (short) SheetInfoType.FAULTREMOVEL.getId());
        params.put("orderByClause", "send_verify_flag asc,send_verify_date asc LIMIT " + length + " OFFSET " + start);
        String objectDepotId = request.getParameter("objectDepotId");
        //获取子部门
        if (objectDepotId != null) {
            List<Depot> childrens = depotHelper.getChildrens(Long.valueOf(objectDepotId));
            params.put("objectDepotIdIn", childrens);
        }
        total = sheetInfoMapper.getSheetParamCount(params);// 根据条件获取单据总数

        dt.setRecordsTotal(total);
        dt.setRecordsFiltered(total);
        list = sheetInfoMapper.getAllSheets(params);
        dt.setData(list);
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
    @RequestMapping(value = "/getAllSheetDetails", method = {RequestMethod.POST})
    public DataTable<SheetDetailDTO> SheetDetailsList(HttpServletRequest request,
                                                      @RequestParam Map<String, Object> params) {
        logger.info("显示配件详情");
        DataTable<SheetDetailDTO> dt = new DataTable<SheetDetailDTO>();
        List<SheetDetailDTO> data = new ArrayList<SheetDetailDTO>();
       try {
           String sheetId = request.getParameter("sheetId");
           // 为空直接返回
           if (StringUtils.isBlank(sheetId)) {
               dt.setData(data);
               dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
               return dt;
           }
           params.put("eqSheetId", sheetId);
           params.put("sheetType", (short) SheetInfoType.FAULTREMOVEL.getId());
           data = sheetDetailMapper.selectPartsUnstall(params);
           dt.setData(data);
           dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
       }catch (Exception e){
           logger.error("getAllSheetDetails"+request);
       }
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
    @RequestMapping(value = "/search", method = {RequestMethod.POST})
    public DataTable<SheetDetailDTO> searchPreparePurchase(HttpServletRequest request,
                                                           @RequestParam Map<String, Object> params) {
        logger.info("配件使用安装查询");
        DataTable<SheetDetailDTO> dt = new DataTable<SheetDetailDTO>();
        String sheetId = request.getParameter("sheetId");
        params.put("eqSheetId", sheetId);
        params.put("sheetType", (short) SheetInfoType.FAULTREMOVEL.getId());
        params.put("sendVerifyFlag", (short) VerifyFlagType.VERIFIED.getId());
        dt.setData(sheetDetailMapper.selectWithParts(params));
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }

    @ResponseBody
    @RequestMapping(value = "/SheetDetailCreate", method = {RequestMethod.POST})
    public Map<String, Object> SheetDetailNew(@RequestBody SheetDetail sheetDetail) {
        logger.info("增加故障拆卸配件");
        int code = 0;
        DetectParts detectParts = new DetectParts();
        //
        if (sheetDetail != null && StringUtils.isNotBlank(sheetDetail.getSheetId()) &&
                StringUtils.isNotBlank(sheetDetail.getPartId()) && StringUtils.isNotBlank(sheetDetail.getPartCode())
        ) {
            //如果库存存在同样的出厂编码，返回错误
            StockInfo1 stockInfo = stockInfoMapper.selectByPrimaryKey(sheetDetail.getPartCode());
            if (stockInfo == null || stockInfo.getPartIdSeq().equals(sheetDetail.getPartId())) {
                sheetDetailMapper.insert(sheetDetail);
                //修改探测站相关配件
                detectParts.setDetectPartsId(sheetDetail.getDetectPartsId());
                detectParts.setEnabled((short)0);
                detectPartsMapper.updateByPrimaryKeySelective(detectParts);
                //如果库存存在同样的出厂编码，返回错误
            } else {
                code = ErrCode.INCOMPLETE_INFO;
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
    @RequestMapping(value = "/SheetDetailModify", method = {RequestMethod.POST})
    public Map<String, Object> SheetDetailEdit(@RequestBody SheetDetail sheetDetail) {
        logger.info("修改所拆卸配件");
        int code = 0;
        if (sheetDetail != null && StringUtils.isNotBlank(sheetDetail.getSheetId())
                && StringUtils.isNotBlank(sheetDetail.getPartCode())) {
            int row = sheetDetailMapper.updateByPrimaryKeySelective(sheetDetail);
            if (row == 0) {
                code = ErrCode.MODIFY_ERROR;
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
    @RequestMapping(value = "/SheetDetailDeleteByCode", method = {RequestMethod.POST})
    public Map<String, Object> SheetDetailDeleteByCode(@RequestBody SheetDetail sheetDetail) {
        logger.info("删除配件使用安装配件");
        int code = 0;
        DetectParts detectParts = new DetectParts();
        if (sheetDetail != null) {
            //如果审核未通过，回退库存到预采购库
            SheetInfo sheetInfo = sheetInfoMapper.selectByPrimaryKey(sheetDetail.getSheetId());

            sheetDetail = sheetDetailMapper.selectByPrimaryKey(sheetDetail);
            if (sheetInfo.getSendVerifyFlag() != (short) VerifyFlagType.VERIFIED.getId()) {
                if (sheetDetail != null && StringUtils.isNotBlank(sheetDetail.getSheetId())
                        && StringUtils.isNotBlank(sheetDetail.getPartId())
                        && StringUtils.isNotBlank(sheetDetail.getPartCode())) {

                    //修改探测站相关配件
                    detectParts.setDetectPartsId(sheetDetail.getDetectPartsId());
                    detectParts.setEnabled((short)1);
                    detectPartsMapper.updateByPrimaryKeySelective(detectParts);


                }
            }
            //删除sheetDetail
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
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public Map<String, Object> delete(@RequestBody SheetInfo sheetInfo) {
        logger.info("删除入库单据下的配件");
        int code = 0;
        List<StockInfo1> list = new ArrayList<StockInfo1>();
        List<DetectParts> detectParts = new ArrayList<DetectParts>();
        Map<String, Object> params = new HashMap<String, Object>();
        if (sheetInfo != null && sheetInfo.getSheetId() != null) {
            SheetInfo info = sheetInfoMapper.selectByPrimaryKey(sheetInfo.getSheetId());
            if (info.getSendVerifyFlag() != (short) VerifyFlagType.VERIFIED.getId()) {

                params.put("eqSheetId", sheetInfo.getSheetId());
                // 根据sheetId查询预采购配件详情
                List<SheetDetail> sheetDetails = sheetDetailMapper.selectByMap(params);

                // 添加到stockInfo库存数组中
                for (SheetDetail sheetDetailDTO : sheetDetails) {


                    //添加探测站回退信息
                    DetectParts detectPart = new DetectParts();
                    detectPart.setDetectPartsId(sheetDetailDTO.getDetectPartsId());
                    detectPart.setEnabled((short) 1);
                    detectParts.add(detectPart);


                }

                // 批量更新探测站信息
                detectPartsMapper.batchUpdate(detectParts);

            }
            sheetInfoMapper.deleteByPrimaryKey(sheetInfo.getSheetId());

            List<SheetDetail> sheetDetaillist = sheetDetailMapper.selectByMap(params);
            if (sheetDetaillist.size() > 0) {
                sheetDetailMapper.deleteBySheetId(sheetInfo.getSheetId());

            }
        } else {
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
     *
     * @param
     * @param request
     * @return StockInfoDTO
     */
    @ResponseBody
    @RequestMapping(value = "/getAllPartsByStock", method = {RequestMethod.GET})
    public DataTable<SheetDetailDTO> getAllPartsByStock(
            HttpServletRequest request) {
        logger.info("获取使用库库存配件信息");
        List<SheetDetailDTO> list = new ArrayList<SheetDetailDTO>();
        DataTable<SheetDetailDTO> dt = new DataTable<SheetDetailDTO>();
        int total = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        String partsCode = request.getParameter("partsCode");
        String storeHouseId = request.getParameter("storeHouseId");
        if (storeHouseId != null && storeHouseId != "") {
            params.put("eqStorehouseId", Short.valueOf(storeHouseId));
            params.put("partIdSeq", partsCode);
            //获取配件数量
            //total=stockInfoMapper.getPartsParamCount(params);
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
     *
     * @param
     * @param request
     * @return StockInfoDTO
     */
    @ResponseBody
    @RequestMapping(value = "/getAllReceiptStoreHouse", method = {RequestMethod.POST})
    public StoreHouse getAllReceiptStoreHouse(
            HttpServletRequest request, @RequestBody StoreHouse storeHouse) {
        logger.info("获取所有收货库信息");
        List<StoreHouse> list = new ArrayList<StoreHouse>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", storeHouse.getType());//车间备品库类型
        params.put("depotId", storeHouse.getDepotId());//车间备品库类型
        //获取收货库
        list = storeHouseMapper.getWorkShopReceiptStoreHouse(params);
        //返回dataTable参数


        return list.get(0);
    }


    /**
     * 根据部门id获取用户部门信息
     *
     * @param
     * @param request
     * @return StockInfoDTO
     */
    @ResponseBody
    @RequestMapping(value = "/getDepotById", method = {RequestMethod.GET})
    public Depot getDepotById(
            HttpServletRequest request) {
        logger.info("获取用户部门信息");
        String depotId = request.getParameter("depotId");
        Depot depot = depotMapper.selectByPrimaryKey(Long.valueOf(depotId));
        //获取收货库
        return depot;
    }

    /**
     * 根据部门id获取仓库信息
     *
     * @param storeHouse
     * @return StockInfoDTO
     */
    @ResponseBody
    @RequestMapping(value = "/getStoreHouseByDepotId", method = {RequestMethod.POST})
    public StoreHouse getStoreHouseByDepotId(@RequestBody StoreHouse storeHouse) {
        logger.info("获取用户部门仓库信息");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("eqDepotId", storeHouse.getDepotId());
        params.put("eqType", storeHouse.getType());
        List<StoreHouse> storeHouses = storeHouseMapper.selectByMap(params);
        //获取收货库
        return storeHouses.get(0);
    }

    @ResponseBody
    @RequestMapping(value = "/getDetectDetail", method = {RequestMethod.GET})
    public DataTable<DetectParts> getDetectDetail(
            HttpServletRequest request) {
        logger.info("获取单个探测站");
        List<DetectParts> list = new ArrayList<DetectParts>();
        DataTable<DetectParts> dt = new DataTable<DetectParts>();
        Map<String, Object> params = new HashMap<String, Object>();
        String detectDeviceId = request.getParameter("deviceId");
        params.put("eqDetectDeviceId", detectDeviceId);//探测站id
        //探测站id对应信息
        list = detectPartsMapper.selectByMap(params);
        //返回dataTable参数
        dt.setData(list);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }

    @ResponseBody
    @RequestMapping(value = "/getDetectParts", method = {RequestMethod.GET})
    public DataTable<DetectPartsDTO> getDetectParts(
            HttpServletRequest request) {
        logger.info("获取单个探测站配件详情");
        List<DetectPartsDTO> list = new ArrayList<DetectPartsDTO>();
        DataTable<DetectPartsDTO> dt = new DataTable<DetectPartsDTO>();
        Map<String, Object> params = new HashMap<String, Object>();
        String detectDeviceId = request.getParameter("deviceId");
        String ifMoved = request.getParameter("ifMoved");
        String enabled = request.getParameter("enabled");
        if (detectDeviceId != null && detectDeviceId != "") {
            params.put("eqDetectDeviceId", Short.valueOf(detectDeviceId));//探测站id

            if (ifMoved != null && ifMoved != "") {
                params.put("eqIfMoved", Short.valueOf(ifMoved));//是否取走
            }
            if (enabled != null && enabled != "") {
                params.put("eqEnabled", Short.valueOf(enabled));//是否可用
            }
            //探测站id对应信息
            list = detectPartsMapper.selectWithParts(params);
        }
        //返回dataTable参数
        dt.setData(list);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }

    /**
     * 获取非AEI探测站
     * @param request
     * @param depotId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listDetect", method = {RequestMethod.GET})
    public DataTable<DetectDevice> sheetsList(HttpServletRequest request, Long depotId) {
        logger.info("显示班组故障拆卸探测站");
        Map<String, Object> params=new HashMap<>();
        List<DetectDevice> list = new ArrayList<>();
        DataTable<DetectDevice> dt = new DataTable<DetectDevice>();
        int total = 0;
        try {
            params.put("eqDepotId",depotId);
            params.put("eqDeviceTypeId",1);
            //获取部门下属探测站
            list=detectManageService.selectByMap(params);
            dt.setRecordsTotal(total);
            dt.setRecordsFiltered(total);
        } catch (Exception e) {
            logger.error("listDetect error" + request);
        }
        dt.setData(list);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;

    }
    // ExcelUtil导出报表
    @RequestMapping(value = "/exportSheetInfo", method = {RequestMethod.GET})
    @ResponseBody
    public void exportSheetInfo(@RequestParam Map<String, Object> params, HttpServletRequest request,
                                HttpServletResponse response) {
        logger.info("新购关键配件交接单---ExcelUtil导出报表");
        String sheetId = (String) params.get("sheetId");
        SheetInfo sheetInfo = sheetInfoMapper.selectByPrimaryKey(sheetId);// 获取单据信息
        params.put("eqSheetId", sheetId);// 获取单据信息
        List<SheetDetailDTO> sheetDetailList = sheetDetailMapper.selectWithParts(params);// 获取单据对应的配件详情
        String file_name = "车辆运行安全监控系统设备拆卸单";
        String[] title = {"编号", "关键配件名称", "设备型号", "设备类型", "生产厂家", "配件出厂编码", "配件二维码", "资产配属", "数量", "单价", "备注"};
        HSSFWorkbook wb = ExcelUtil.exportPreparePurchaseSheetInfoAsExcel(file_name, title, sheetDetailList, sheetInfo, null);
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
}
