
package com.kthw.pmis.controller.detectProduce;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kthw.pmis.helper.DepotHelper;
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
import com.kthw.pmis.mapper.common.DepotMapper;
import com.kthw.pmis.mapper.common.DetectDeviceMapper;
import com.kthw.pmis.mapper.common.DetectPartsMapper;
import com.kthw.pmis.mapper.common.SheetDetailMapper;
import com.kthw.pmis.mapper.common.SheetInfoMapper;
import com.kthw.pmis.mapper.common.StockInfo1Mapper;
import com.kthw.pmis.mapper.common.StoreHouseMapper;

@Transactional
@Controller
@RequestMapping(value = "/detectProduce/partsConsume")
public class PartsConsumeController {
    private static Logger logger = LoggerFactory.getLogger(PartsConsumeController.class);
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
            params.put("sheetType", (short) SheetInfoType.PARTS_CONSUME.getId());
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
                    sheetInfo.setSheetType((short) SheetInfoType.PARTS_CONSUME.getId());
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
        logger.info("修改或审核配件使用安装单据");
        int code = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        List<StockInfo1> list = new ArrayList<StockInfo1>();
        try {
            if (sheetInfo != null) {
                SheetInfo info = sheetInfoMapper.selectByPrimaryKey(sheetInfo.getSheetId());

                if (info.getSendVerifyFlag() != null && info.getSendVerifyFlag() == (short) VerifyFlagType.VERIFIED.getId()) {
                    params.put("eqSheetId", info.getSheetId());

                    // 根据sheetId查询所调拨配送到段配件详情
                    List<SheetDetail> sheetDetails = sheetDetailMapper.selectByMap(params);
                    // 添加到stockInfo库存数组中
                    for (SheetDetail sheetDetail : sheetDetails) {
                        StockInfo1 stockInfo = new StockInfo1();
                        stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
                        stockInfo.setStorehouseId(info.getObjectStoreHouseId());
                        stockInfo.setPartIdSeq(sheetDetail.getPartId());
                        stockInfo.setSheetId(sheetDetail.getSheetId());
                        stockInfo.setDeviceId(info.getDeviceId());
                        stockInfo.setEnabled((short) 1);
                        list.add(stockInfo);

                    }
                    // 批量更新库存
                    stockInfoMapper.batchUpdate(list);
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
    @RequestMapping(value = "/find", method = {RequestMethod.GET})
    public DataTable<SheetInfo> getPartsById(HttpServletRequest request, HttpSession httpSession) {
        logger.info("按id查询单据");
        Map<String, Object> params = new HashMap<String, Object>();
        String sheetId = request.getParameter("sheet_id");
        params.put("sheetId", sheetId);
        DataTable<SheetInfo> dt = new DataTable<SheetInfo>();
        List<SheetInfo> list = new ArrayList<SheetInfo>();
        try {
            list.add(sheetInfoMapper.selectByPrimaryKey(sheetId));
        } catch (Exception e) {
            logger.error("find error");
        }
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
    @RequestMapping(value = "/getAllSheetDetails", method = {RequestMethod.POST})
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
            params.put("sheetType", (short) SheetInfoType.PARTS_CONSUME.getId());
            dt.setData(sheetDetailMapper.selectWithParts(params));
        } catch (Exception e) {
            logger.error("getAllSheetDetails error");
        }
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }

    /**
     * 根据sheetId获取单据详情
     *
     * @param request
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/search", method = {RequestMethod.GET})
    public DataTable<SheetInfoExt> searchSheets(HttpServletRequest request) {
        logger.info("显示配件拆卸单据");
        String start = request.getParameter("start");
        String length = request.getParameter("length");
        List<SheetInfoExt> list = new ArrayList<>();
        DataTable<SheetInfoExt> dt = new DataTable<SheetInfoExt>();
        int total = 0;
        Map<String, Object> params = new HashMap<>();
        try {
            params.put("sourceStoreHouseId", request.getParameter("sourceStoreHouseId"));

            params.put("queryTime", request.getParameter("queryTime"));
            params.put("queryTime2", request.getParameter("queryTime2"));
            params.put("sheetId", request.getParameter("sheetId"));
            params.put("sheetType", (short) SheetInfoType.PARTS_CONSUME.getId());
            params.put("orderByClause", " sheet_id desc LIMIT " + length + " OFFSET " + start);
            String sourceDepotId = request.getParameter("sourceDepotId");
            String objectDepotId = request.getParameter("objectDepotId");
            //获取子部门
            if (sourceDepotId != null) {
                List<Depot> childrens = depotHelper.getChildrens(Long.valueOf(sourceDepotId));
                params.put("sourceDepotIdIn", childrens);
            }
            if (objectDepotId != null) {
                List<Depot> childrens = depotHelper.getChildrens(Long.valueOf(sourceDepotId));
                params.put("objectDepotIdIn", childrens);
            }
            total = sheetInfoMapper.getSheetParamCount(params);// 根据条件获取单据总数

            dt.setRecordsTotal(total);
            dt.setRecordsFiltered(total);
            list = sheetInfoMapper.getAllSheets(params);
        } catch (Exception e) {
            logger.error("search error" + request);
        }
        dt.setData(list);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;

    }

    @ResponseBody
    @RequestMapping(value = "/SheetDetailCreate", method = {RequestMethod.POST})
    public Map<String, Object> SheetDetailNew(@RequestBody SheetDetail sheetDetail) {
        logger.info("增加配件使用安装入库配件");
        int code = 0;
        DetectParts detectParts = new DetectParts();
        try {
            SheetInfo sheetInfo = sheetInfoMapper.selectByPrimaryKey(sheetDetail.getSheetId());

            if (sheetDetail != null && StringUtils.isNotBlank(sheetDetail.getSheetId())
                    && StringUtils.isNotBlank(sheetDetail.getPartId())) {

                StockInfo1 stockInfo = new StockInfo1();
                stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
                stockInfo.setStorehouseId(sheetInfo.getObjectStoreHouseId());
                stockInfo.setPartIdSeq(sheetDetail.getPartId());
                stockInfo.setEnabled((short) 0);//不可用

                stockInfoMapper.updateByPrimaryKeySelective(stockInfo);

                sheetDetailMapper.insert(sheetDetail);


                //修改探测站相关配件
                detectParts.setDetectPartsId(sheetDetail.getDetectPartsId());
                detectParts.setPartId(sheetDetail.getPartId());
                detectParts.setPartsCode(sheetDetail.getPartCode());
                detectParts.setIfMoved((short) 0);
                detectPartsMapper.updateByPrimaryKeySelective(detectParts);

            }
        } catch (Exception e) {
            logger.error("sheetDetailCreate error" + sheetDetail);
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    @ResponseBody
    @RequestMapping(value = "/SheetDetailModify", method = {RequestMethod.POST})
    public Map<String, Object> SheetDetailEdit(@RequestBody SheetDetail sheetDetail) {
        logger.info("修改所调拨配送到段入库配件");
        int code = 0;
        try {
            if (sheetDetail != null && StringUtils.isNotBlank(sheetDetail.getSheetId())
                    && StringUtils.isNotBlank(sheetDetail.getPartCode())) {
                int row = sheetDetailMapper.updateByPrimaryKeySelective(sheetDetail);
                if (row == 0) {
                    code = ErrCode.MODIFY_ERROR;
                }

            } else {
                code = ErrCode.INCOMPLETE_INFO;
            }
        } catch (Exception e) {
            logger.error("SheetDetailModify error" + sheetDetail);
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
        try {
            if (sheetDetail != null) {
                //如果审核未通过，回退库存到预采购库
                SheetInfo sheetInfo = sheetInfoMapper.selectByPrimaryKey(sheetDetail.getSheetId());

                sheetDetail = sheetDetailMapper.selectByPrimaryKey(sheetDetail);
                if (sheetInfo.getSendVerifyFlag() != (short) VerifyFlagType.VERIFIED.getId()) {
                    if (sheetDetail != null && StringUtils.isNotBlank(sheetDetail.getSheetId())
                            && StringUtils.isNotBlank(sheetDetail.getPartId())
                            && StringUtils.isNotBlank(sheetDetail.getPartCode())) {
                        StockInfo1 stockInfo = new StockInfo1();
                        stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
                        stockInfo.setStorehouseId(sheetInfo.getSourceStoreHouseId());
                        stockInfo.setPartIdSeq(sheetDetail.getPartId());
                        stockInfo.setEnabled((short) 1);//可用
                        stockInfoMapper.updateByPrimaryKeySelective(stockInfo);

                        //修改探测站相关配件
                        detectParts.setDetectPartsId(sheetDetail.getDetectPartsId());
                        detectParts.setIfMoved((short) 1);
                        detectPartsMapper.updateByPrimaryKeySelective(detectParts);


                    }
                }
                //删除sheetDetail
                sheetDetailMapper.deleteByPrimaryKey(sheetDetail);

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
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public Map<String, Object> delete(@RequestBody SheetInfo sheetInfo) {
        logger.info("删除入库单据下的配件");
        int code = 0;
        List<StockInfo1> list = new ArrayList<StockInfo1>();
        List<DetectParts> detectParts = new ArrayList<DetectParts>();
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            if (sheetInfo != null && sheetInfo.getSheetId() != null) {
                SheetInfo info = sheetInfoMapper.selectByPrimaryKey(sheetInfo.getSheetId());
                if (info.getSendVerifyFlag() != (short) VerifyFlagType.VERIFIED.getId()) {

                    params.put("eqSheetId", sheetInfo.getSheetId());
                    // 根据sheetId查询预采购配件详情
                    List<SheetDetail> sheetDetails = sheetDetailMapper.selectByMap(params);

                    // 添加到stockInfo库存数组中
                    for (SheetDetail sheetDetailDTO : sheetDetails) {
                        StockInfo1 stockInfo = new StockInfo1();
                        stockInfo.setFactoryPartsCode(sheetDetailDTO.getPartCode());
                        stockInfo.setStorehouseId(info.getSourceStoreHouseId());
                        stockInfo.setPartIdSeq(sheetDetailDTO.getPartId());
                        stockInfo.setEnabled((short) 1);
                        list.add(stockInfo);


                        //添加探测站回退信息
                        DetectParts detectPart = new DetectParts();
                        detectPart.setDetectPartsId(sheetDetailDTO.getDetectPartsId());
                        detectPart.setIfMoved((short) 1);
                        detectParts.add(detectPart);


                    }
                    // 批量更新库存
                    stockInfoMapper.batchUpdate(list);

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

        } catch (Exception e) {
            logger.error("SheetDetailModify error" + sheetInfo);
        }
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
        logger.info("获取备品库库存配件信息");
        List<SheetDetailDTO> list = new ArrayList<SheetDetailDTO>();
        DataTable<SheetDetailDTO> dt = new DataTable<SheetDetailDTO>();
        int total = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        String partId = request.getParameter("partId");
        String storeHouseId = request.getParameter("storeHouseId");
        String partCode = request.getParameter("partCode");
        String eqSheetId = request.getParameter("eqSheetId");//查询库存用
        try {
            //获取对应仓库
            if (StringUtils.isNotBlank(storeHouseId)) {
                params.put("eqStorehouseId", Integer.valueOf(storeHouseId));

                if(StringUtils.isNotBlank(partId)) {
                    params.put("partIdSeq", partId);
                }
                if(StringUtils.isNotBlank(eqSheetId)) {
                    params.put("eqSheetId", eqSheetId);//stock_info当前的sheetId
                }
                if(StringUtils.isNotBlank(partCode)) {
                    params.put("eqFactoryPartsCode", partCode);
                }
                params.put("isConsume", (short) 1);//易耗品
                //获取配件详细信息

                list = sheetDetailMapper.selectStockWithSheetId(params);
            }
        } catch (Exception e) {
            logger.error("getAllPartsByStock error" + request);
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
    @RequestMapping(value = "/getAllReceiptStoreHouse", method = {RequestMethod.GET})
    public DataTable<StoreHouse> getAllReceiptStoreHouse(
            HttpServletRequest request) {
        logger.info("获取所有收货库信息");
        List<StoreHouse> list = new ArrayList<StoreHouse>();
        DataTable<StoreHouse> dt = new DataTable<StoreHouse>();
        Map<String, Object> params = new HashMap<String, Object>();
        try{
            params.put("eqType", (short) 10);//收货库类型
        //获取收货库
        list = storeHouseMapper.selectByMap(params);
        //返回dataTable参数
        } catch (Exception e) {
            logger.error("getAllReceiptStoreHouse error"+request);
        }
        dt.setData(list);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }

    /**
     * 批量增加sheetDetail
     *
     * @param request
     * @param list
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sheetDetailBatchCreate", method = {RequestMethod.POST})
    public Map<String, Object> sheetDetailBatchCreate(HttpServletRequest request, @RequestBody List<SheetDetail> list) {
        logger.info("增加班组消耗配件");
        int code = 0;
        Map<String, Object> ret = new HashMap<String, Object>();
       try{ if (list.size() > 0) {
            SheetInfo sheetInfo = sheetInfoMapper.selectByPrimaryKey(list.get(0).getSheetId());
            if (sheetInfo != null) {
                for (SheetDetail sheetDetail : list) {

                    StockInfo1 stockInfo = new StockInfo1();
                    stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
                    stockInfo.setStorehouseId(sheetInfo.getSourceStoreHouseId());
                    stockInfo.setPartIdSeq(sheetDetail.getPartId());
                    stockInfo.setEnabled((short) 0);//不可用
                    stockInfoMapper.updateByPrimaryKeySelective(stockInfo);

                    sheetDetail.setAssetAttributesId(sheetInfo.getAssetAttributesId());
                    sheetDetailMapper.insert(sheetDetail);


                }
            }
        } else {
            code = ErrCode.EMPTY_RESULT;
        }
       } catch (Exception e) {
           logger.error("sheetDetailBatchCreate error"+request);
       }
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    /**
     * 根据部门获取用户所拥有的探测站
     *
     * @param
     * @param request
     * @return StockInfoDTO
     */
    @ResponseBody
    @RequestMapping(value = "/getAllDetectByDepot", method = {RequestMethod.POST})
    public DataTable<DetectDevice> getAllDetectByDepot(
            HttpServletRequest request, @RequestBody DetectDevice detectDevice) {
        logger.info("获取所有探测站信息");
        List<DetectDevice> list = new ArrayList<DetectDevice>();
        DataTable<DetectDevice> dt = new DataTable<DetectDevice>();
        Map<String, Object> params = new HashMap<String, Object>();
        try{params.put("eqDepotId", detectDevice.getDepotId());//仓库类型
        //获取收货库
        list = detectDeviceMapper.selectByMap(params);
        } catch (Exception e) {
            logger.error("getAllDetectByDepot error"+request);
        }
        //返回dataTable参数
        dt.setData(list);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
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
        Depot depot=new Depot();
        String depotId = request.getParameter("depotId");
        try{depot = depotMapper.selectByPrimaryKey(Long.valueOf(depotId));
        //获取收货库
        } catch (Exception e) {
            logger.error("getAllDetectByDepot error"+request);
        }
        return depot;
    }

    /**
     * 根据部门id获取仓库信息
     *
     * @param
     * @param request
     * @return StockInfoDTO
     */
    @ResponseBody
    @RequestMapping(value = "/getStoreHouseByDepotId", method = {RequestMethod.POST})
    public StoreHouse getStoreHouseByDepotId(
            HttpServletRequest request, @RequestBody StoreHouse storeHouse) {
        logger.info("获取用户部门仓库信息");
        Map<String, Object> params = new HashMap<String, Object>();
       try{ params.put("eqDepotId", storeHouse.getDepotId());
        params.put("eqType", storeHouse.getType());
        List<StoreHouse> storeHouses = storeHouseMapper.selectByMap(params);
        //获取收货库
        return storeHouses.get(0);
       } catch (Exception e) {
           logger.error("getStoreHouseByDepotId error"+request);
       }
       return null;
    }

    @ResponseBody
    @RequestMapping(value = "/getDetectDetail", method = {RequestMethod.GET})
    public DataTable<DetectParts> getDetectDetail(
            HttpServletRequest request) {
        logger.info("获取单个探测站详情");
        List<DetectParts> list = new ArrayList<DetectParts>();
        DataTable<DetectParts> dt = new DataTable<DetectParts>();
        try{Map<String, Object> params = new HashMap<String, Object>();
        String detectDeviceId = request.getParameter("deviceId");
        params.put("eqDetectDeviceId", detectDeviceId);//探测站id
        //探测站id对应信息
        list = detectPartsMapper.selectByMap(params);
        //返回dataTable参数
        } catch (Exception e) {
            logger.error("getDetectDetail error"+request);
        }
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
        try{String detectDeviceId = request.getParameter("deviceId");
        String ifMoved = request.getParameter("ifMoved");
        String enabled = request.getParameter("enabled");
        if (detectDeviceId != null && detectDeviceId != "") {
            params.put("eqDetectDeviceId", Integer.valueOf(detectDeviceId));//探测站id
        }
        if (ifMoved != null && ifMoved != "") {
            params.put("eqIfMoved", Short.valueOf(ifMoved));//是否取走
        }
        if (enabled != null && enabled != "") {
            params.put("eqEnabled", Short.valueOf(enabled));//是否可用
        }
        //探测站id对应信息
        list = detectPartsMapper.selectWithParts(params);
        } catch (Exception e) {
            logger.error("getDetectParts error"+request);
        }
        //返回dataTable参数
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
        String file_name = "车辆运行安全监控系统设备使用单";
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
