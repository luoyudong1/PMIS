package com.kthw.pmis.controller.partsEntryAndOut;

import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.kthw.pmis.entiy.*;
import com.kthw.pmis.helper.DepotHelper;
import com.kthw.pmis.helper.SheetDetailHelper;
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
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import com.kthw.pmis.entiy.ext.SheetInfoExt;
import com.kthw.pmis.mapper.common.SheetDetailMapper;
import com.kthw.pmis.mapper.common.SheetInfoMapper;
import com.kthw.pmis.mapper.common.StockInfo1Mapper;
import com.kthw.pmis.mapper.common.StoreHouseMapper;

/**
 * 描述:返修出库Controller.
 */
@Transactional
@Controller
@RequestMapping(value = "/partsEntryAndOut/repairFactory")
public class RepairFactoryController {
    private static Logger logger = LoggerFactory.getLogger(RepairFactoryController.class);
    @Autowired
    private StockInfo1Mapper stockInfoMapper;
    @Autowired
    private StoreHouseMapper storeHouseMapper;
    @Autowired
    private SheetDetailMapper sheetDetailMapper;
    @Autowired
    private SheetInfoMapper sheetInfoMapper;
    @Autowired
    private SheetDetailHelper sheetDetailHelper;
    @Autowired
    private DepotHelper depotHelper;

    @ResponseBody
    @RequestMapping(value = "/getAllSheets", method = {RequestMethod.GET})
    public DataTable<SheetInfoExt> sheetsList(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        logger.info("显示返修出库单据");
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
            params.put("sheetType", (short) SheetInfoType.REPAIR_FACTORY.getId());
            params.put("orderByClause", " send_verify_flag asc,add_date desc LIMIT " + length + " OFFSET " + start);
            String sourceDepotId = request.getParameter("sourceDepotIdIn");
            //获取子部门
            if (sourceDepotId != null) {
                List<Depot> childrens = depotHelper.getChildrens(Long.valueOf(sourceDepotId));
                params.put("sourceDepotIdIn", childrens);
            }
            total = sheetInfoMapper.getSheetParamCount(params);// 根据条件获取单据总数
            dt.setRecordsTotal(total);
            dt.setRecordsFiltered(total);
            list = sheetInfoMapper.getAllSheets(params);
            dt.setData(list);
        } catch (Exception e) {
            logger.error("getAllSheets error: ", e);
        }
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;


    }

    @ResponseBody
    @RequestMapping(value = "/createSheetInfo", method = {RequestMethod.POST})
    public Map<String, Object> createSheet(@RequestBody SheetInfo sheetInfo) {
        logger.info("新增返修出库单据");
        int code = 0;
        try {
            if (sheetInfo != null && StringUtils.isNotBlank(sheetInfo.getSheetId())) {
                if (sheetInfoMapper.selectByPrimaryKey(sheetInfo.getSheetId()) != null) {
                    code = ErrCode.SHEET_ALREADY_EXISTS;
                } else {
                    sheetInfo.setSheetType((short) SheetInfoType.REPAIR_FACTORY.getId());
                    sheetInfoMapper.insert(sheetInfo);
                }

            } else {
                code = ErrCode.INCOMPLETE_INFO;
            }
        } catch (Exception e) {
            logger.error("createSheetInfo error: ", sheetInfo);
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));

        return ret;
    }


    /**
     * 根据sheetId获取单据详情
     *
     * @param request
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAllSheetDetails", method = {RequestMethod.GET})
    public DataTable<SheetDetailDTO> SheetDetailsList(HttpServletRequest request,
                                                      @RequestParam Map<String, Object> params) {
        logger.info("显示配件详情");
        DataTable<SheetDetailDTO> dt = new DataTable<SheetDetailDTO>();
        List<SheetDetailDTO> data = new ArrayList<SheetDetailDTO>();
        try {
            String sheetId = request.getParameter("sheetId");
            // 为空直接返回
            if (sheetId == null || sheetId == "") {
                dt.setData(data);
                dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);

                return dt;
            }
            params.put("eqSheetId", sheetId);
            params.put("sheetType", (short) SheetInfoType.REPAIR_FACTORY.getId());
            dt.setData(sheetDetailMapper.selectWithParts(params));
        } catch (Exception e) {
            logger.error("getAllSheetDetails error: ", e);
        }
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
        logger.info("增加段调拨到所入库配件");
        int code = 0;
        Map<String, Object> ret = new HashMap<String, Object>();
        try {
            if (list.size() > 0) {
                SheetInfo sheetInfo = sheetInfoMapper.selectByPrimaryKey(list.get(0).getSheetId());
                if (sheetInfo != null) {
                    for (SheetDetail sheetDetail : list) {
                        try {
                            sheetDetail = sheetDetailHelper.repalce(sheetDetail);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        StockInfo1 stockInfo = new StockInfo1();
                        stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
                        stockInfo.setStorehouseId(sheetInfo.getSourceStoreHouseId());
                        stockInfo.setPartIdSeq(sheetDetail.getPartId());
                        stockInfo.setEnabled((short) 0);//不可用
                        stockInfoMapper.updateByPrimaryKeySelective(stockInfo);

                        sheetDetailMapper.insert(sheetDetail);


                    }
                }
            } else {
                code = ErrCode.EMPTY_RESULT;
            }
        } catch (Exception e) {
            logger.error("sheetDetailBatchCreate error: ", e);
        }
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    /**
     * 增加单个配件
     *
     * @param sheetDetail
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/SheetDetailCreate", method = {RequestMethod.POST})
    public Map<String, Object> SheetDetailCreate(@RequestBody SheetDetail sheetDetail) {
        logger.info("增加返修出库入库配件");
        int code = 0;
        Map<String, Object> ret = new HashMap<String, Object>();
        try {
            if (sheetDetail.getSheetId() != null) {
                SheetInfo sheetInfo = sheetInfoMapper.selectByPrimaryKey(sheetDetail.getSheetId());
                if (sheetDetailMapper.selectByPrimaryKey(sheetDetail) != null) {
                    code = ErrCode.SHEETDETAIL_ALREADY_EXISTS;
                } else {
                    try {
                        sheetDetail = sheetDetailHelper.repalce(sheetDetail);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    StockInfo1 stockInfo = new StockInfo1();
                    stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
                    stockInfo.setStorehouseId(sheetInfo.getSourceStoreHouseId());
                    stockInfo.setPartIdSeq(sheetDetail.getPartId());
                    stockInfo.setEnabled((short) 0);// 不可用
                    stockInfoMapper.updateByPrimaryKeySelective(stockInfo);

                    // 插入sheetDetail
                    sheetDetailMapper.insertSelective(sheetDetail);
                }
            }
        } catch (Exception e) {
            logger.error("SheetDetailCreate error: ", e);
        }
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }


    @ResponseBody
    @RequestMapping(value = "/getAllParts", method = {RequestMethod.GET})
    public DataTable<SheetDetailDTO> partsManageList(HttpServletRequest request, HttpSession httpSession) {
        logger.info("显示返修库库配件");
        List<SheetDetailDTO> list = new ArrayList<SheetDetailDTO>();
        DataTable<SheetDetailDTO> dt = new DataTable<SheetDetailDTO>();
        int total = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            String partId = request.getParameter("partId");
            String partCode = request.getParameter("partCode");
            String sheetId = request.getParameter("sheetId");
            String storeHouseId = request.getParameter("storeHouseId");
            params.put("partIdSeq", partId);
            params.put("eqFactoryPartsCode", partCode);
            params.put("eqSheetId", sheetId);
            params.put("eqStorehouseId",Integer.valueOf(storeHouseId));
            params.put("eqWarranty",(short)1);
            params.put("supplierName", request.getParameter("supplierName"));


            //获取配件详细信息
            list = sheetDetailMapper.selectStockWithSheetId(params);

            //返回dataTable参数
            dt.setRecordsTotal(total);
            dt.setRecordsFiltered(total);
            dt.setData(list);
        } catch (Exception e) {
            logger.error("getAllParts error: ", e);
        }
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }

    @ResponseBody
    @RequestMapping(value = "/SheetDetailDeleteByCode", method = {RequestMethod.POST})
    public Map<String, Object> SheetDetailDeleteByCode(@RequestBody SheetDetail sheetDetail) {
        logger.info("删除返修出库入库配件");
        int code = 0;
        try {
            if (sheetDetail.getSheetId() != null) {
                SheetInfo sheetInfo = sheetInfoMapper.selectByPrimaryKey(sheetDetail.getSheetId());
                if (sheetInfo.getSendVerifyFlag() != VerifyFlagType.VERIFIED.getId()) {
                    StockInfo1 stockInfo = new StockInfo1();
                    stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
                    stockInfo.setStorehouseId(sheetInfo.getSourceStoreHouseId());
                    stockInfo.setPartIdSeq(sheetDetail.getPartId());
                    stockInfo.setEnabled((short) 1);// 可用
                    stockInfoMapper.updateByPrimaryKeySelective(stockInfo);
                    sheetDetailMapper.deleteByPrimaryKey(sheetDetail);
                }
            } else {
                code = ErrCode.INCOMPLETE_INFO;
            }
        } catch (Exception e) {
            logger.error("SheetDetailDeleteByCode error: ", e);
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public Map<String, Object> delete(@RequestBody SheetInfo sheetInfo) {
        logger.info("删除单据");
        int code = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        List<StockInfo1> list = new ArrayList<StockInfo1>();
        try {
            if (sheetInfo != null) {
                String sheetId = sheetInfo.getSheetId();
                params.put("eqSheetId", sheetId);
                SheetInfo info = sheetInfoMapper.selectByPrimaryKey(sheetInfo.getSheetId());

                if (info.getSendVerifyFlag() != (short) VerifyFlagType.VERIFIED.getId()) {
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

                    }
                    // 批量更新库存
                    stockInfoMapper.batchUpdate(list);
                }

                sheetInfoMapper.deleteByPrimaryKey(sheetId);

                List<SheetDetail> sheetDetaillist = sheetDetailMapper.selectByMap(params);
                if (sheetDetaillist.size() > 0) {
                    sheetDetailMapper.deleteBySheetId(sheetId);

                }
            } else {
                code = ErrCode.INCOMPLETE_INFO;
            }
        } catch (Exception e) {
            logger.error("delete error: ", e);
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    @ResponseBody
    @RequestMapping(value = "/modifyVerify", method = {RequestMethod.POST})
    public Map<String, Object> SheetVerify(@RequestBody SheetInfo sheetInfo) {
        logger.info("返修出库单据审核");
        int code = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        List<StockInfo1> list = new ArrayList<StockInfo1>();
        try {
            if (sheetInfo.getSheetId() != null) {

                SheetInfo info = sheetInfoMapper.selectByPrimaryKey(sheetInfo.getSheetId());


                if (sheetInfo.getSendVerifyFlag() != null &&
                        sheetInfo.getSendVerifyFlag() == (short) VerifyFlagType.VERIFIED.getId()) {
                    params.put("eqSheetId", sheetInfo.getSheetId());

                    // 根据sheetId查询所调拨配送到段配件详情
                    List<SheetDetail> sheetDetails = sheetDetailMapper.selectByMap(params);
                    // 添加到stockInfo库存数组中
                    for (SheetDetail sheetDetail : sheetDetails) {
                        StockInfo1 stockInfo = new StockInfo1();
                        stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
                        stockInfo.setStorehouseId(info.getObjectStoreHouseId());
                        stockInfo.setPartIdSeq(sheetDetail.getPartId());
                        stockInfo.setSheetId(sheetDetail.getSheetId());
                        stockInfo.setEnabled((short) 1);
                        list.add(stockInfo);

                    }
                    // 批量更新库存
                    stockInfoMapper.batchUpdate(list);
                }
                sheetInfoMapper.updateByPrimaryKeySelective(sheetInfo);
            }
        } catch (Exception e) {
            logger.error("modifyVerify error: ", e);
        }
        params.clear();
        params.put("code", code);
        params.put("msg", ErrCode.getMessage(code));
        return params;
    }



    // 返修出库查询
    @ResponseBody
    @RequestMapping(value = "/getSheetDetails", method = {RequestMethod.GET})
    public DataTable<SheetDetailDTO> FindSheetDetailsList(HttpServletRequest request,
                                                          @RequestParam Map<String, Object> params) {
        logger.info("返修出库查询");
        DataTable<SheetDetailDTO> dt = new DataTable<SheetDetailDTO>();
        try{params.put("sheetType", (short) SheetInfoType.REPAIR_FACTORY.getId());
            dt.setData(sheetDetailMapper.selectWithParts(params));
        } catch (Exception e) {
            logger.error("getSheetDetails error: ", e);
        }
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
    @RequestMapping(value = "/getAllSourceStoreHouse", method = { RequestMethod.GET })
    public StoreHouse getAllSourceStoreHouse(HttpServletRequest request) {
        logger.info("获取源仓库信息详情");
        List<StoreHouse> list = new ArrayList<StoreHouse>();
        Map<String, Object> params=new HashMap<String, Object>();
        String depotIdString=request.getParameter("depotId");
        if(depotIdString!=null) {
            params.put("eqType", 7);//收货库类型
            params.put("eqDepotId", Long.valueOf(depotIdString));//收货库类型
            //获取收货库
            list = storeHouseMapper.selectByMap(params);
        }
        if(list.size()>0) {
            return list.get(0);
        }else {
            return null;
        }
    }
    /**
     * 获取所有收货库信息
     *
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
        try{params.put("eqType", (short) 11);//厂家库类型
            //获取收货库
            list = storeHouseMapper.selectByMap(params);
            //返回dataTable参数
            dt.setData(list);
        } catch (Exception e) {
            logger.error("getAllReceiptStoreHouse error: ", e);
        }
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }
}
