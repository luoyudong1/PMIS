package com.kthw.pmis.controller.repairManage;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.kthw.pmis.entiy.SheetDetail;
import com.kthw.pmis.entiy.SheetDetailKey;
import com.kthw.pmis.entiy.SheetInfo;
import com.kthw.pmis.entiy.StockInfo1;
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import com.kthw.pmis.entiy.ext.SheetInfoExt;
import com.kthw.pmis.mapper.common.SheetDetailMapper;
import com.kthw.pmis.mapper.common.SheetInfoMapper;
import com.kthw.pmis.mapper.common.StockInfo1Mapper;
import com.kthw.pmis.model.system.StoreHouseInfo;
import com.kthw.pmis.model.system.User;
import com.kthw.pmis.service.repairManage.RepairService;

/**
 * 描述:所内送修Controller.
 */
@Transactional
@Controller
@RequestMapping(value = "/repairManage/repair")
public class CheckInController {

    private static Logger logger = LoggerFactory.getLogger(CheckInController.class);
    @Autowired
    private SheetDetailHelper sheetDetailHelper;
    @Autowired
    private RepairService RepairService;
    @Autowired
    private SheetInfoMapper sheetInfoMapper;
    @Autowired
    private SheetDetailMapper sheetDetailMapper;
    @Autowired
    private StockInfo1Mapper stockInfoMapper;
    private User user = null;

    @ResponseBody
    @RequestMapping(value = "/getAllSheets", method = {RequestMethod.GET})
    public DataTable<SheetInfoExt> sheetsList(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        logger.info("显示所内送修单据");
        String start = request.getParameter("start");
        String length = request.getParameter("length");
        List<SheetInfoExt> list = new ArrayList<>();
        DataTable<SheetInfoExt> dt = new DataTable<SheetInfoExt>();
        int total = 0;
        params.put("sheetType", (short) SheetInfoType.CHECKIN.getId());
        params.put("orderByClause", "send_verify_flag asc,add_date desc LIMIT " + length + " OFFSET " + start);
        total = sheetInfoMapper.getSheetParamCount(params);// 根据条件获取单据总数
        dt.setRecordsTotal(total);
        dt.setRecordsFiltered(total);
        list = sheetInfoMapper.getAllSheets(params);
        dt.setData(list);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;

    }

    /**
     * 修改所内送修配件详情表
     *
     * @param sheetDetail
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/SheetDetailModify", method = {RequestMethod.POST})
    public Map<String, Object> SheetDetailEdit(@RequestBody SheetDetail sheetDetail) {
        logger.info("修改检修入库配件");
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
    @RequestMapping(value = "/createSheetInfo", method = {RequestMethod.POST})
    public Map<String, Object> createSheet(@RequestBody SheetInfo sheetInfo) {
        logger.info("新增送修单据");
        int code = 0;
        if (sheetInfo != null && StringUtils.isNotBlank(sheetInfo.getSheetId())) {
            if (sheetInfoMapper.selectByPrimaryKey(sheetInfo.getSheetId()) != null) {
                code = ErrCode.SHEET_ALREADY_EXISTS;
            } else {
                sheetInfo.setSheetType((short) SheetInfoType.CHECKIN.getId());
                sheetInfo.setObjectStoreHouseId((short) StoreHouseEnum.TEST_OVERHAUL.getId());
                sheetInfo.setSendVerifyFlag((short) VerifyFlagType.NOVERIFY.getId());
                sheetInfoMapper.insert(sheetInfo);
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
    @RequestMapping(value = "/modify", method = {RequestMethod.POST})
    public Map<String, Object> modify(@RequestBody SheetInfo sheetInfo) {
        logger.info("修改送修单据");
        int code = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        List<StockInfo1> list = new ArrayList<StockInfo1>();

        if (sheetInfo != null) {


            if (sheetInfo.getSendVerifyFlag() != null &&
                    sheetInfo.getSendVerifyFlag() == (short) VerifyFlagType.VERIFIED.getId()) {
                params.put("eqSheetId", sheetInfo.getSheetId());

                // 根据sheetId查询预采购配件详情
                List<SheetDetail> sheetDetails = sheetDetailMapper.selectByMap(params);

                // 添加到stockInfo库存数组中
                for (SheetDetail sheetDetail : sheetDetails) {
                    StockInfo1 stockInfo = new StockInfo1();
                    stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
                    stockInfo.setStorehouseId((short) StoreHouseEnum.TEST_UNCHECK.getId());
                    stockInfo.setPartIdSeq(sheetDetail.getPartId());
                    stockInfo.setSheetId(sheetDetail.getSheetId());
                    stockInfo.setEnabled((short) 1);
                    list.add(stockInfo);

                }

                // 批量插入库存
                stockInfoMapper.batchUpdate(list);
            }
            int row = sheetInfoMapper.updateByPrimaryKeySelective(sheetInfo);
            if (row == 0) {
                code = ErrCode.MODIFY_ERROR;
            }

        } else {
            code = ErrCode.DB_ERROR;
        }
        params.clear();
        params.put("code", code);
        params.put("msg", ErrCode.getMessage(code));
        return params;
    }

    /**
     * 删除单据下的单个配件
     */
    @ResponseBody
    @RequestMapping(value = "/SheetDetailDeleteByCode", method = {RequestMethod.POST})
    public Map<String, Object> SheetDetailDeleteByCode(@RequestBody SheetDetail sheetDetail) {
        logger.info("删除所内送修入库配件");
        int code = 0;
        if (sheetDetail.getSheetId() != null) {
            SheetInfo sheetInfo = sheetInfoMapper.selectByPrimaryKey(sheetDetail.getSheetId());
            if (sheetInfo.getSendVerifyFlag() != VerifyFlagType.VERIFIED.getId()) {
                StockInfo1 stockInfo = new StockInfo1();
                stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
                stockInfo.setStorehouseId((short) sheetInfo.getSourceStoreHouseId());
                stockInfo.setPartIdSeq(sheetDetail.getPartId());
                stockInfo.setEnabled((short) 1);// 可用
                stockInfoMapper.updateByPrimaryKeySelective(stockInfo);
                sheetDetailMapper.deleteByPrimaryKey(sheetDetail);
            }
        } else {
            code = ErrCode.INCOMPLETE_INFO;
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    /**
     * "删除送修入库单据下的所有配件
     */
    @ResponseBody
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public Map<String, Object> delete(@RequestBody SheetInfo sheetInfo) {
        logger.info("删除送修入库单据下的配件");
        int code = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        List<StockInfo1> list = new ArrayList<StockInfo1>();
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
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    @SuppressWarnings("null")
    @ResponseBody
    @RequestMapping(value = "/insertSheetDetail", method = {RequestMethod.POST})
    public Map<String, Object> addRepairParts(@RequestBody Map<String, Object> params, HttpServletRequest request,
                                              HttpSession httpSession) {
        logger.info("增加返修配件");
        int code = 0;
        Map<String, Object> ret = new HashMap<String, Object>();
        // 获取
        String partCode = (String) params.get("partCode");
        String sourceSheetId = (String) params.get("sourceSheetId");
        String sheetId = (String) params.get("sheetId");
        SheetDetailKey sheetDetailKey = new SheetDetailKey();
        sheetDetailKey.setSheetId(sourceSheetId);
        sheetDetailKey.setPartCode(partCode);
        SheetDetail sheetDetail = sheetDetailMapper.selectByPrimaryKey(sheetDetailKey);
        //sheeDetail不为空
        if (sheetDetail != null) {

            sheetDetail.setSheetId(sheetId);
            // 更新库存
            StockInfo1 stockInfo = new StockInfo1();
            stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
            stockInfo.setStorehouseId((short) StoreHouseEnum.TEST_UNCHECK.getId());
            stockInfo.setPartIdSeq(sheetDetail.getPartId());
            stockInfo.setEnabled((short) 0);// 不可用
            stockInfoMapper.updateByPrimaryKeySelective(stockInfo);

            // 插入sheetDetail
            sheetDetailMapper.insertSelective(sheetDetail);
        }
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
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
        logger.info("批量增加所内送修入库配件");
        int code = 0;
        Map<String, Object> ret = new HashMap<String, Object>();
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

                    sheetDetail=sheetDetailHelper.clearCheckedRecord(sheetDetail);
                    sheetDetailMapper.insert(sheetDetail);


                }
            }
        } else {
            code = ErrCode.EMPTY_RESULT;
        }
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    @ResponseBody
    @RequestMapping(value = "/modifyVerify", method = {RequestMethod.POST})
    public Map<String, Object> partsModifyVerify(@RequestBody Map<String, Object> params, HttpServletRequest request,
                                                 HttpSession httpSession) {
        logger.info("审核配件");
        user = (User) httpSession.getAttribute("AUTH_USER");
        List<String> part_id_seqs = (List<String>) params.get("check");
        int code = 0;
        if (part_id_seqs.size() > 0) {
            for (String part_id_seq : part_id_seqs) {
                params.put("part_id_seq", part_id_seq);
                if ((int) params.get("verify_flag") == 2) {
                    params.put("verify_id", user.getUser_id());
                    params.put("verify_name", user.getUser_name());
                }
                code = RepairService.partsModifyVerify(params);
            }
        } else {
            code = ErrCode.DB_ERROR;
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    /**
     * 获取所内送检单据配件详情
     *
     * @param request
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAllSheetDetails", method = {RequestMethod.POST})
    public DataTable<SheetDetailDTO> SheetDetailsList(HttpServletRequest request,
                                                      @RequestParam Map<String, Object> params) {
        logger.info("显示所内送检单据配件详情");
        DataTable<SheetDetailDTO> dt = new DataTable<SheetDetailDTO>();
        List<SheetDetailDTO> data = new ArrayList<SheetDetailDTO>();
        String sheetId = request.getParameter("sheetId");
        System.out.println("显示所内送检单据配件详情" + sheetId);
        // 为空直接返回
        if (sheetId == null || sheetId == "") {
            dt.setData(data);
            dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
            ;
            return dt;
        }
        params.put("eqSheetId", sheetId);
        params.put("sheetType", (short) SheetInfoType.CHECKIN.getId());
        dt.setData(sheetDetailMapper.selectWithParts(params));
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;

    }

    @ResponseBody
    @RequestMapping(value = "/getStorehouse", method = {RequestMethod.GET})
    public DataTable<StoreHouseInfo> getStorehouse(@RequestParam Map<String, Object> params,
                                                   HttpServletRequest request) {
        logger.info("查询仓库");
        List<StoreHouseInfo> list = new ArrayList<>();
        DataTable<StoreHouseInfo> dt = new DataTable<StoreHouseInfo>();
        list = RepairService.getStorehouse(params);
        dt.setData(list);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }

    /**
     * 获取对应库存的配件信息
     *
     * @param request
     * @return StockInfoDTO
     */
    @ResponseBody
    @RequestMapping(value = "/getAllPartsByStock", method = {RequestMethod.GET})
    public DataTable<SheetDetailDTO> getAllPartsByStock(
            HttpServletRequest request) {
        logger.info("获取库存配件信息");
        List<SheetDetailDTO> list = new ArrayList<SheetDetailDTO>();
        DataTable<SheetDetailDTO> dt = new DataTable<SheetDetailDTO>();
        int total = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        String partCode = request.getParameter("partCode");
        String partId = request.getParameter("partId");
        String sheetId = request.getParameter("sheetId");
        String storeHouseId = request.getParameter("storeHouseId");
        if (storeHouseId != null && storeHouseId != "") {
            params.put("eqStorehouseId", Short.valueOf(storeHouseId));
            params.put("partIdSeq", partId);
            params.put("eqSheetId",sheetId);
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

    @ResponseBody
    @RequestMapping(value = "/search", method = {RequestMethod.POST})
    public DataTable<SheetDetailDTO> searchPreparePurchase(HttpServletRequest request,
                                                           @RequestParam Map<String, Object> params) {
        logger.info("所内送修入库查询");
        DataTable<SheetDetailDTO> dt = new DataTable<SheetDetailDTO>();
        params.put("sheetType", (short) SheetInfoType.CHECKIN.getId());
        dt.setData(sheetDetailMapper.selectWithParts(params));
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }
}
