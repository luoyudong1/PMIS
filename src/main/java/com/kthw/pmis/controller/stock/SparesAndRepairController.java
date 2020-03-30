package com.kthw.pmis.controller.stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.kthw.pmis.entiy.*;
import com.kthw.pmis.helper.DepotHelper;
import com.kthw.pmis.helper.SheetDetailHelper;
import com.kthw.pmis.mapper.common.*;
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
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import com.kthw.pmis.entiy.ext.SheetInfoExt;
@Transactional
@Controller
@RequestMapping(value = "/stock/sparesAndRepair")
public class SparesAndRepairController {
    private static Logger logger = LoggerFactory.getLogger(SparesAndRepairController.class);
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
    private DepotMapper depotMapper;
    @Autowired
    private DepotHelper depotHelper;
    @Autowired
    private DetectDeviceMapper detectDeviceMapper;
    @ResponseBody
    @RequestMapping(value = "/getAllSheets", method = { RequestMethod.GET })
    public DataTable<SheetInfoExt> sheetsList(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        logger.info("显示备品库和送修库单据");
        String start = request.getParameter("start");
        String length = request.getParameter("length");
        List<SheetInfoExt> list = new ArrayList<>();
        DataTable<SheetInfoExt> dt = new DataTable<SheetInfoExt>();
        int total = 0;
        params.put("sourceStoreHouseId", request.getParameter("sourceStoreHouseId"));
        String sourceDepotId=request.getParameter("sourceDepotId");
        String objectDepotId=request.getParameter("objectDepotId");
        if(sourceDepotId!=null) {
            params.put("sourceDepotId",Short.valueOf(sourceDepotId));
        }
        if(objectDepotId!=null) {
            params.put("objectDepotId",Short.valueOf(objectDepotId));
        }
        params.put("queryTime", request.getParameter("queryTime"));
        params.put("queryTime2", request.getParameter("queryTime2"));
        params.put("sheetId", request.getParameter("sheetId"));
        params.put("sendVerifyFlag", request.getParameter("sendVerifyFlag"));
        params.put("sheetType", (short) SheetInfoType.SPARES_AND_REPAIR.getId());
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
        logger.info("新增备品库和送修库单据");
        int code = 0;
        if (sheetInfo != null && StringUtils.isNotBlank(sheetInfo.getSheetId())) {
            if (sheetInfoMapper.selectByPrimaryKey(sheetInfo.getSheetId()) != null) {
                code = ErrCode.SHEET_ALREADY_EXISTS;
            } else {
                sheetInfo.setSheetType((short) SheetInfoType.SPARES_AND_REPAIR.getId());
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
        logger.info("修改或审核备品库和送修库调拨单据");
        int code = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        List<StockInfo1> list = new ArrayList<StockInfo1>();

        if (sheetInfo != null) {
            SheetInfo info=sheetInfoMapper.selectByPrimaryKey(sheetInfo.getSheetId());

            if (sheetInfo.getSendVerifyFlag() !=null&&sheetInfo.getSendVerifyFlag() == (short) VerifyFlagType.VERIFIED.getId()) {
                params.put("eqSheetId", sheetInfo.getSheetId());

                // 根据sheetId查询车间调拨到班组配件详情
                List<SheetDetail> sheetDetails = sheetDetailMapper.selectByMap(params);
                // 添加到stockInfo库存数组中
                for (SheetDetail sheetDetail : sheetDetails) {
                    StockInfo1 stockInfo = new StockInfo1();
                    stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
                    stockInfo.setStorehouseId( info.getObjectStoreHouseId());
                    stockInfo.setPartIdSeq(sheetDetail.getPartId());
                    stockInfo.setSheetId(sheetDetail.getSheetId());
                    stockInfo.setEnabled((short) 1);
                    stockInfo.setDeviceId(sheetDetail.getUsedStationId());
                    stockInfo.setWarranty(sheetDetail.getWarranty());
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
     * 查询页面使用
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/searchSheets", method = {RequestMethod.GET})
    public DataTable<SheetInfoExt> searchSheets(HttpServletRequest request) {
        logger.info("显示备品库和送修库单据");
        String start = request.getParameter("start");
        String length = request.getParameter("length");
        List<SheetInfoExt> list = new ArrayList<>();
        DataTable<SheetInfoExt> dt = new DataTable<SheetInfoExt>();
        int total = 0;
        Map<String,Object> params=new HashMap<>();
        params.put("sourceStoreHouseId", request.getParameter("sourceStoreHouseId"));
        params.put("queryTime", request.getParameter("queryTime"));
        params.put("queryTime2", request.getParameter("queryTime2"));
        params.put("sheetId", request.getParameter("sheetId"));
        params.put("sheetType", (short) SheetInfoType.SPARES_AND_REPAIR.getId());
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
        dt.setData(list);
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
    @RequestMapping(value = "/getAllSheetDetails", method = { RequestMethod.POST })
    public DataTable<SheetDetailDTO> SheetDetailsList(HttpServletRequest request) {
        logger.info("显示备品库和送修库配件详情");
        DataTable<SheetDetailDTO> dt = new DataTable<SheetDetailDTO>();
        List<SheetDetailDTO> data = new ArrayList<SheetDetailDTO>();
        Map<String, Object> params=new HashMap<String, Object>();
        String sheetId = request.getParameter("sheetId");
        // 为空直接返回
        if (sheetId == null || sheetId == "") {
            dt.setData(data);
            dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
            ;
            return dt;
        }
        params.put("eqSheetId", sheetId);
        params.put("sheetType", (short) SheetInfoType.SPARES_AND_REPAIR.getId());
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
        logger.info("备品库和送修库查询");
        DataTable<SheetDetailDTO> dt = new DataTable<SheetDetailDTO>();
        String sheetId = request.getParameter("sheetId");
        params.put("eqSheetId", sheetId);
        params.put("sheetType", (short) SheetInfoType.SPARES_AND_REPAIR.getId());
        params.put("sendVerifyFlag", (short) VerifyFlagType.VERIFIED.getId());
        dt.setData(sheetDetailMapper.selectWithParts(params));
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }

    @ResponseBody
    @RequestMapping(value = "/SheetDetailCreate", method = { RequestMethod.POST })
    public Map<String, Object> SheetDetailNew(@RequestBody SheetDetail sheetDetail) {
        logger.info("增加备品库和送修库配件");
        int code = 0;
        if (sheetDetail != null && StringUtils.isNotBlank(sheetDetail.getSheetId())
                && StringUtils.isNotBlank(sheetDetail.getPartCode())) {

            SheetInfo sheetInfo =sheetInfoMapper.selectByPrimaryKey(sheetDetail.getSheetId());
            if(sheetInfo!=null) {
                try {
                    sheetDetail=sheetDetailHelper.repalce(sheetDetail);
                }catch (Exception e){
                    e.printStackTrace();
                }
                StockInfo1 stockInfo = new StockInfo1();
                stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
                stockInfo.setStorehouseId((short) sheetInfo.getSourceStoreHouseId());
                stockInfo.setPartIdSeq(sheetDetail.getPartId());
                stockInfo.setEnabled((short)0);//不可用
                stockInfoMapper.updateByPrimaryKeySelective(stockInfo);
                sheetDetailMapper.insert(sheetDetail);
            }else {
                code=ErrCode.SHEET_NOT_EXISTS;
            }
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }
    /**
     * 批量增加
     * @param request
     * @param list
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sheetDetailBatchCreate", method = { RequestMethod.POST })
    public Map<String, Object> sheetDetailBatchCreate(HttpServletRequest request,@RequestBody List<SheetDetail> list) {
        logger.info("增加备品库和送修库入库配件");
        int code = 0;
        Map<String, Object> ret = new HashMap<String, Object>();
        if(list.size()>0) {
            SheetInfo sheetInfo=sheetInfoMapper.selectByPrimaryKey(list.get(0).getSheetId());
            if(sheetInfo!=null) {
                for(SheetDetail sheetDetail:list) {
                    StockInfo1 stockInfo = new StockInfo1();
                    stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
                    stockInfo.setStorehouseId((short) sheetInfo.getSourceStoreHouseId());
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
    @ResponseBody
    @RequestMapping(value = "/SheetDetailModify", method = { RequestMethod.POST })
    public Map<String, Object> SheetDetailEdit(@RequestBody SheetDetail sheetDetail) {
        logger.info("修改备品库和送修库配件");
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
        logger.info("删除备品库和送修库配件");
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
                    stockInfo.setStorehouseId((short) sheetInfo.getSourceStoreHouseId());
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
        logger.info("删除备品库和送修库单据下的配件");
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
                    stockInfo.setStorehouseId(info.getSourceStoreHouseId());
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
        logger.info("获取备品库和送修库配件信息");
        List<SheetDetailDTO> list = new ArrayList<SheetDetailDTO>();
        DataTable<SheetDetailDTO> dt = new DataTable<SheetDetailDTO>();
        int total=0;
        Map<String, Object> params=new HashMap<String, Object>();
        String partId=request.getParameter("partId");
        String partCode=request.getParameter("partCode");
        String storeHouseId=request.getParameter("storeHouseId");
        if(storeHouseId!=null&&storeHouseId!="") {
            params.put("eqStorehouseId",Short.valueOf(storeHouseId));
            params.put("partIdSeq", partId);
            params.put("eqFactoryPartsCode",partCode);
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
     *获取车间有收货库信息
     * @param
     * @param request
     * @return StockInfoDTO
     */
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
        logger.info("获取源仓库信息");
        List<StoreHouse> list = new ArrayList<StoreHouse>();
        DataTable<StoreHouse> dt = new DataTable<StoreHouse>();
        Map<String, Object> params=new HashMap<String, Object>();
        Depot depot=depotMapper.selectByPrimaryKey(Long.valueOf(storeHouse.getDepotId()));
        if(depot.getDepotLevel()<5) {//车间用户
            params.put("eqType", 13);//备品库类型
            params.put("eqDepotId", storeHouse.getDepotId());//部门ID
            //获取收货库
            list.addAll(storeHouseMapper.selectByMap(params));
        }else if(depot.getDepotLevel()==5){//班组用户
            params.put("eqType", 6);//备品库类型
            params.put("eqDepotId", storeHouse.getDepotId());//部门ID
            //获取收货库
            list.addAll(storeHouseMapper.selectByMap(params));

        }


        //返回dataTable参数
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
    @RequestMapping(value = "/getDetectDevice", method = { RequestMethod.POST })
    public List<DetectDevice> getDetectDevice(
            HttpServletRequest request,@RequestBody StoreHouse storeHouse) {
        logger.info("获取探测站信息");
        List<DetectDevice> list = new ArrayList<DetectDevice>();
        Map<String, Object> params=new HashMap<String, Object>();
        Depot depot=depotMapper.selectByPrimaryKey(Long.valueOf(storeHouse.getDepotId()));
        if(depot.getDepotLevel()<5) {//车间用户
            List<Depot> listDepot=depotHelper.getChildDepots(depot);
            params.put("eqEnabled", 1);
            params.put("depotIdList", listDepot);//部门ID
            //获取收货库
            list.addAll(detectDeviceMapper.listDetectDeviceByWorkShop(params));
        }else if(depot.getDepotLevel()==5){//班组用户
            params.put("eqEnable", 1);
            params.put("eqDepotId", storeHouse.getDepotId());//部门ID
            //获取收货库
            list.addAll(detectDeviceMapper.selectByMap(params));
        }


        //返回
        return  list;
    }
}
