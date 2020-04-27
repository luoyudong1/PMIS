package com.kthw.pmis.controller.repairManage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.kthw.pmis.entiy.*;
import com.kthw.pmis.helper.CheckHelper;
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
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import com.kthw.pmis.entiy.dto.StockInfoDTO;
import com.kthw.pmis.entiy.ext.SheetInfoExt;
import com.kthw.pmis.model.system.StoreHouseInfo;
import com.kthw.pmis.service.repairManage.RepairService;

/**
 * 描述:所内检修Controller.
 */
@Transactional
@Controller
@RequestMapping(value = "/repairManage/check")
public class CheckController {

    private static Logger logger = LoggerFactory.getLogger(CheckController.class);

    @Autowired
    private RepairService RepairService;
    @Autowired
    private SheetInfoMapper sheetInfoMapper;
    @Autowired
    private SheetDetailMapper sheetDetailMapper;
    @Autowired
    private StockInfo1Mapper stockInfoMapper;
    @Autowired
    private PartsZtreeMapper partsZtreeMapper;
    @Autowired
    private PartsDictMapper partsMapper;
    @Autowired
    private CheckHelper checkHelper;

    @ResponseBody
    @RequestMapping(value = "/getAllSheets", method = {RequestMethod.GET})
    public DataTable<SheetInfoExt> sheetsList(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        logger.info("显示单据");
        String start = request.getParameter("start");
        String length = request.getParameter("length");
        List<SheetInfoExt> list = new ArrayList<>();
        DataTable<SheetInfoExt> dt = new DataTable<SheetInfoExt>();
        int total = 0;
       try{ params.put("sheetType", (short) SheetInfoType.CHECKIN.getId());
        params.put("orderByClause", " receipt_verify_flag asc LIMIT " + length + " OFFSET " + start);
        params.put("sendVerifyFlag", String.valueOf(VerifyFlagType.VERIFIED.getId()));
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
    public Map<String, Object> modify(@RequestBody SheetDetail sheetDetail) {
        logger.info("修改检修记录单据");
        int code = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        SheetInfo sheetInfo = sheetInfoMapper.selectByPrimaryKey(sheetDetail.getSheetId());
        SheetDetailKey sheetDetailKey = new SheetDetailKey();
        if (StringUtils.isNotBlank(sheetDetail.getSheetId()) && StringUtils.isNotBlank(sheetDetail.getPartCode())) {
            sheetDetailKey.setSheetId(sheetDetail.getSheetId());
            sheetDetailKey.setPartCode(sheetDetail.getPartCode());
            sheetDetail = sheetDetailMapper.selectByPrimaryKey(sheetDetailKey);
            sheetDetail = checkHelper.setCheckedRemark(sheetInfo, sheetDetail);
            // 添加到stockInfo库存数组中
            StockInfo1 stockInfo = new StockInfo1();
            stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
            stockInfo.setStorehouseId((short) StoreHouseEnum.TEST_OVERHAUL.getId());
            stockInfo.setPartIdSeq(sheetDetail.getPartId());
            stockInfo.setSheetId(sheetDetail.getSheetId());
            stockInfo.setEnabled((short) 1);
//更新sheetDetail
            sheetDetailMapper.updateByPrimaryKeySelective(sheetDetail);
            // 批量插入库存
            int row = stockInfoMapper.updateByPrimaryKeySelective(stockInfo);
            if (row == 0) {
                code = ErrCode.MODIFY_ERROR;

            }
        } else {
            code = ErrCode.INCOMPLETE_INFO;
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
        logger.info("删除预采购入库配件");
        int code = 0;
        if (sheetDetail != null) {
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

    /**
     * "删除送修入库单据下的所有配件
     */
    @ResponseBody
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public Map<String, Object> delete(@RequestBody SheetInfo sheetInfo) {
        logger.info("删除送修入库单据下的配件");
        int code = 0;
        String sheetId = sheetInfo.getSheetId();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("eqSheetId", sheetId);
        sheetInfoMapper.deleteByPrimaryKey(sheetId);

        List<SheetDetail> list = sheetDetailMapper.selectByMap(params);
        if (list.size() > 0) {
            sheetDetailMapper.deleteBySheetId(sheetId);

        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }


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

    /**
     * 修改所内送修配件详情表
     *
     * @param sheetDetail
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/SheetDetailModify", method = {RequestMethod.POST})
    public Map<String, Object> SheetDetailEdit(@RequestBody SheetDetail sheetDetail) {
        logger.info("修改所内检修记录单");
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
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    /**
     * 根据待检测库获取数量
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
        String storeHouseId = request.getParameter("storeHouseId");
        if (StringUtils.isNotBlank(storeHouseId)) {
            params.put("storeHouseId", storeHouseId);

            list = partsZtreeMapper.getPartCount(params);
        }
        return list;
    }

    /**
     * getPartsZtreeDetails
     */
    @ResponseBody
    @RequestMapping(value = "/getPartsZtreeDetails", method = {RequestMethod.GET})
    public DataTable<SheetDetailDTO> getPartsZtreeDetails(
            HttpServletRequest request) {
        logger.info("获取配件树形表查询条件");
        List<SheetDetailDTO> list = new ArrayList<SheetDetailDTO>();
        DataTable<SheetDetailDTO> dt = new DataTable<SheetDetailDTO>();
        Map<String, Object> params = new HashMap<String, Object>();
        String storeHouseId = request.getParameter("storeHouseId");
        String partsZtreeId = request.getParameter("partsZtreeId");
        if (StringUtils.isNotBlank(storeHouseId) && StringUtils.isNotBlank(partsZtreeId)) {
            PartsZtree partsZtree = partsZtreeMapper.selectByPrimaryKey(Integer.valueOf(partsZtreeId));

            //判断是否是3级配件格式
            if (partsZtree.getFuncLevel() == 3 && partsZtree.getPartId() != null) {
                String partId = partsZtree.getPartId();
                params.put("subPartId", partId);//查询条件：配件编码前缀
                if (StringUtils.isNotBlank(storeHouseId)) {
                    params.put("eqStorehouseId", Integer.valueOf(storeHouseId));//查询条件：仓库id
                }
                list = sheetDetailMapper.selectStockWithSheetId(params);
            }
        }
        //返回dataTable参数
        dt.setData(list);
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
     * @param
     * @param request
     * @return StockInfoDTO
     */
    @ResponseBody
    @RequestMapping(value = "/getAllPartsByStock", method = {RequestMethod.GET})
    public DataTable<StockInfoDTO> getAllPartsByStock(@RequestParam Map<String, Object> params,
                                                      HttpServletRequest request) {
        logger.info("获取库存配件信息");
        List<StockInfoDTO> list = new ArrayList<StockInfoDTO>();
        DataTable<StockInfoDTO> dt = new DataTable<StockInfoDTO>();
        int total = 0;
        params.put("start", request.getParameter("start"));
        params.put("length", request.getParameter("length"));
        //params.put("storeHouseId", request.getParameter("storeHouseId"));
        params.put("storeHouseId", 14);
        //获取配件数量
        total = stockInfoMapper.getPartsParamCount(params);
        //获取配件详细信息
        if (total != 0) {
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
     * 获取检修价格
     */
    @ResponseBody
    @RequestMapping(value = "/getCheckPrice", method = {RequestMethod.GET})
    public BigDecimal getCheckPrice(@RequestParam Map<String, Object> params,
                                    HttpServletRequest request) {
        logger.info("获取检修价格");
        String partId = request.getParameter("partId");
        String sheetId = request.getParameter("sheetId");
        if (StringUtils.isNotBlank(partId) && StringUtils.isNotBlank(sheetId)) {
            //获取配件基本信息
            params.put("eqPartsCode", partId.substring(0, 5));
            List<PartsDict> list = partsMapper.selectByMap(params);

            //
            SheetInfo sheetInfo = sheetInfoMapper.selectByPrimaryKey(sheetId);
            if (sheetInfo != null && sheetInfo.getSourceStoreHouseId() == 4) {//送修库
                PartsDict parts = list.get(0);
                BigDecimal detectPrice = parts.getDetectPrice();
                BigDecimal fixMaxPric = parts.getFixMaxPric();
                return fixMaxPric.add(detectPrice);
            }
        }
        return null;
    }
}
