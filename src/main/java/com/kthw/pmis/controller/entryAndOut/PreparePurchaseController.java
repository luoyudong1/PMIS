package com.kthw.pmis.controller.entryAndOut;


import java.io.OutputStream;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.kthw.pmis.entiy.PartsZtree;
import com.kthw.pmis.entiy.SheetDetail;
import com.kthw.pmis.entiy.SheetInfo;
import com.kthw.pmis.entiy.StockInfo1;
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import com.kthw.pmis.entiy.ext.SheetInfoExt;
import com.kthw.pmis.mapper.common.PartsZtreeMapper;
import com.kthw.pmis.mapper.common.SheetDetailMapper;
import com.kthw.pmis.mapper.common.SheetInfoMapper;
import com.kthw.pmis.mapper.common.StockInfo1Mapper;

/**
 * 描述:预采购入库Controller.
 */
@Transactional
@Controller
@RequestMapping(value = "/entryAndOut/preparePurchase")
public class PreparePurchaseController {
    private static Logger logger = LoggerFactory.getLogger(PreparePurchaseController.class);
    @Autowired
    private SheetInfoMapper sheetInfoMapper;
    @Autowired
    private SheetDetailMapper sheetDetailMapper;
    @Autowired
    private StockInfo1Mapper stockInfoMapper;
    @Autowired
    private PartsZtreeMapper partsZtreeMapper;

    @ResponseBody
    @RequestMapping(value = "/getAllSheets", method = {RequestMethod.GET})
    public DataTable<SheetInfoExt> sheetsList(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        logger.info("显示预购单据");
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
            params.put("sheetType", (short) SheetInfoType.PREPATEPURCHASE.getId());
            params.put("orderByClause", " send_verify_flag asc,add_date desc LIMIT " + length + " OFFSET " + start);

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
        logger.info("新增预购单据");
        int code = 0;
        try {
            if (sheetInfo != null && StringUtils.isNotBlank(sheetInfo.getSheetId())) {
                if (sheetInfoMapper.selectByPrimaryKey(sheetInfo.getSheetId()) != null) {
                    code = ErrCode.SHEET_ALREADY_EXISTS;
                } else {
                    sheetInfo.setSourceStoreHouseId((short) StoreHouseEnum.TEST_PREPARE_PURCHASE.getId());
                    sheetInfo.setSheetType((short) SheetInfoType.PREPATEPURCHASE.getId());
                    sheetInfo.setObjectStoreHouseId((short) StoreHouseEnum.TEST_PREPARE_PURCHASE.getId());
                    sheetInfo.setSendVerifyFlag((short) VerifyFlagType.NOVERIFY.getId());
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
    @RequestMapping(value = "/sheetInfoModify", method = {RequestMethod.POST})
    public Map<String, Object> sheetInfoModify(@RequestBody SheetInfo sheetInfo) {
        logger.info("修改预购单据");
        int code = 0;
        try {
            if (sheetInfo != null && StringUtils.isNotBlank(sheetInfo.getSheetId())) {
                if (sheetInfoMapper.selectByPrimaryKey(sheetInfo.getSheetId()) == null) {
                    code = ErrCode.SHEET_NOT_EXISTS;
                } else {
                    sheetInfoMapper.updateByPrimaryKeySelective(sheetInfo);
                }

            } else {
                code = ErrCode.INCOMPLETE_INFO;
            }
        } catch (Exception e) {
            logger.error("sheetInfoModify error" + sheetInfo);
        }

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    @ResponseBody
    @RequestMapping(value = "/modify", method = {RequestMethod.POST})
    public Map<String, Object> modify(@RequestBody SheetInfo sheetInfo) {
        logger.info("修改预购单据");
        int code = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        List<StockInfo1> list = new ArrayList<StockInfo1>();
        try {
            if (sheetInfo != null) {
                params.put("eqSheetId", sheetInfo.getSheetId());

                // 根据sheetId查询预采购配件详情
                List<SheetDetail> sheetDetails = sheetDetailMapper.selectByMap(params);
                if (sheetDetails.size() > 0) {

                    if (sheetInfo.getSendVerifyFlag() != null && sheetInfo.getSendVerifyFlag()
                            == (short) VerifyFlagType.VERIFIED.getId()) {
                        // 添加到stockInfo库存数组中
                        for (SheetDetail sheetDetail : sheetDetails) {
                            StockInfo1 stockInfo = new StockInfo1();
                            stockInfo.setFactoryPartsCode(sheetDetail.getPartCode());
                            stockInfo.setStorehouseId((short) StoreHouseEnum.TEST_PREPARE_PURCHASE.getId());//采购库
                            stockInfo.setPartIdSeq(sheetDetail.getPartId());
                            stockInfo.setSheetId(sheetDetail.getSheetId());
                            stockInfo.setPartsState((short) 1);//配件属性为新购
                            stockInfo.setWarranty((short) 1);//新购是质保期
                            stockInfo.setEnabled((short) 1);//可用
                            list.add(stockInfo);

                        }
                        // 批量插入库存
                        stockInfoMapper.batchInsert(list);
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
            if (sheetId == null || sheetId == "") {
                dt.setData(data);
                dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
                ;
                return dt;
            }
            params.put("eqSheetId", sheetId);
            params.put("sheetType", (short) SheetInfoType.PREPATEPURCHASE.getId());
            dt.setData(sheetDetailMapper.selectWithParts(params));
        } catch (Exception e) {
            logger.error("getAllSheetDetails error" + request);
        }
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
    @RequestMapping(value = "/searchPreparePurchase", method = {RequestMethod.POST})
    public DataTable<SheetDetailDTO> searchPreparePurchase(HttpServletRequest request,
                                                           @RequestParam Map<String, Object> params) {
        logger.info("预采购入库查询");
        DataTable<SheetDetailDTO> dt = new DataTable<SheetDetailDTO>();
        try {
            String sheetId = request.getParameter("sheetId");
            params.put("eqSheetId", sheetId);
            params.put("sheetType", (short) SheetInfoType.PREPATEPURCHASE.getId());
            dt.setData(sheetDetailMapper.selectWithParts(params));
        } catch (Exception e) {
            logger.error("searchPreparePurchase error" + request);
        }
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
        logger.info("预采购树形表入库查询");

        List<PartsZtree> list = new ArrayList<PartsZtree>();
        try {
            list = partsZtreeMapper.selectByMap(null);
        } catch (Exception e) {
            logger.error("getPartsZtree error" + request);
        }
        return list;
    }

    @ResponseBody
    @RequestMapping(value = "/SheetDetailCreate", method = {RequestMethod.POST})
    public Map<String, Object> SheetDetailNew(@RequestBody SheetDetail sheetDetail) {
        logger.info("增加预采购入库配件");
        int code = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            if (sheetDetail != null && StringUtils.isNotBlank(sheetDetail.getSheetId())
                    && StringUtils.isNotBlank(sheetDetail.getPartCode())) {
                //查看是否存在出厂编码
                params.put("eqPartCode", sheetDetail.getPartCode());
                List<SheetDetail> sheetDetailDTOs = sheetDetailMapper.selectByMap(params);
                if (sheetDetailDTOs.size() > 0) {
                    code = ErrCode.SHEETDETAIL_ALREADY_EXISTS;
                } else {
                    sheetDetail.setWarranty((short) 1);//在质保期内
                    sheetDetail.setPartState((short) 1);//配件属性为新购
                    sheetDetailMapper.insert(sheetDetail);
                }
            }
        } catch (Exception e) {
            logger.error("getPartsZtree error" + sheetDetail);
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    @ResponseBody
    @RequestMapping(value = "/sheetDetailModify", method = {RequestMethod.POST})
    public Map<String, Object> sheetDetailModify(@RequestBody SheetDetailDTO sheetDetail) {
        logger.info("修改预采购入库配件");
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
    @RequestMapping(value = "/SheetDetailDeleteByCode", method = {RequestMethod.POST})
    public Map<String, Object> SheetDetailDeleteByCode(@RequestBody SheetDetail sheetDetail) {
        logger.info("删除预采购入库配件");
        int code = 0;
        try {
            if (sheetDetail != null) {
                sheetDetailMapper.deleteByPrimaryKey(sheetDetail);

            } else {
                code = ErrCode.INCOMPLETE_INFO;
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
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public Map<String, Object> delete(@RequestBody SheetInfo sheetInfo) {
        logger.info("删除入库单据下的配件");
        int code = 0;
       try{ String sheetId = sheetInfo.getSheetId();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("eqSheetId", sheetId);
        sheetInfoMapper.deleteByPrimaryKey(sheetId);

        List<SheetDetail> list = sheetDetailMapper.selectByMap(params);
        if (list.size() > 0) {
            sheetDetailMapper.deleteBySheetId(sheetId);

        }
       } catch (Exception e) {
           logger.error("delete error" + sheetInfo);
       }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
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
        String file_name = "车辆运行安全监控系统设备预采购关键配件交接单";
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
