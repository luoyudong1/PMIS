package com.kthw.pmis.controller.stock;

import java.io.OutputStream;
import java.rmi.MarshalledObject;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kthw.pmis.entiy.*;
import com.kthw.pmis.helper.DepotHelper;
import com.kthw.pmis.mapper.common.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.kthw.common.DataTable;
import com.kthw.common.ExcelUtil;
import com.kthw.common.base.ErrCode;
import com.kthw.pmis.entiy.dto.DetectPartsDTO;
import com.kthw.pmis.entiy.dto.StockInfoDTO;
import com.kthw.pmis.model.stock.StockInfo;
import com.kthw.pmis.model.system.User;
import com.kthw.pmis.service.stock.StockInfoService;

/**
 * 描述:配件库存信息Controller.
 */
@Transactional
@Controller
@RequestMapping(value = "/stock/stockInfo")
public class StockInfoController {

    private static Logger logger = LoggerFactory.getLogger(StockInfoController.class);
    @Autowired
    private DetectDeviceMapper detectDeviceMapper;
    @Autowired
    private SheetDetailMapper sheetDetailMapper;
    @Autowired
    private DetectPartsMapper detectPartsMapper;
    @Autowired
    private StockInfoService stockInfoService;
    @Autowired
    private StockInfo1Mapper stockInfoMapper;
    @Autowired
    private StoreHouseMapper storeHouseMapper;
    @Autowired
    private PartIdConfigMapper partIdConfigMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private PartsZtreeMapper partsZtreeMapper;
    @Autowired
    private SheetInfoMapper sheetInfoMapper;
    @Autowired
    private DepotHelper depotHelper;
    private User user = null;
    private final String[] ZEROS = {"000", "00", "0", ""};

    /**
     * 查询配件詳情信息
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public DataTable<StockInfoDTO> stockInfoList(HttpServletRequest request, HttpSession httpSession) {
        user = (User) httpSession.getAttribute("AUTH_USER");
        logger.info("用户" + user.getUser_id() + " 查询全部单个配件信息;");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("start", request.getParameter("start"));
        params.put("length", request.getParameter("length"));
        params.put("storeHouseId", Integer.valueOf(request.getParameter("storehouse_id")));
        params.put("partId", '%'+request.getParameter("part_id")+'%');//使用中
        params.put("partCode", '%'+request.getParameter("part_code")+'%');//使用中
        params.put("part_name", request.getParameter("part_name"));
        params.put("begin_time", request.getParameter("begin_time"));
        params.put("end_time", request.getParameter("end_time"));
        params.put("enabled", request.getParameter("enabled"));
        if (request.getParameter("detect_device_id") != null && request.getParameter("detect_device_id") != "") {
            params.put("detectDeviceId", Integer.valueOf(request.getParameter("detect_device_id")));
        }
        DataTable<StockInfoDTO> dt = new DataTable<StockInfoDTO>();
        int total = 0;
        total = stockInfoMapper.getPartsParamCount(params);
        dt.setRecordsTotal(total);
        dt.setRecordsFiltered(total);
        dt.setData(stockInfoMapper.selectWithParts(params));
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
        String storeHouseId = request.getParameter("storeHouseId");
        String detectDeviceId = request.getParameter("detectDeviceId");
        if (StringUtils.isNotBlank(storeHouseId)) {
            params.put("storeHouseId", storeHouseId);
            params.put("enabled", 1);
            if (StringUtils.isNotBlank(detectDeviceId)) {
                params.put("detectDeviceId", detectDeviceId);
            }
            list = partsZtreeMapper.getPartCount(params);
        }
        return list;
    }

    /**
     * 删除配件詳情信息
     */
    @ResponseBody
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public Map<String, Object> stockInfoDelete(@RequestBody StockInfo1 stockInfo, HttpServletRequest request, HttpSession httpSession) {
        user = (User) httpSession.getAttribute("AUTH_USER");
        logger.info("用户" + user.getUser_id() + " 删除配件编号为" + stockInfo.getPartIdSeq() + "的信息");
        Map<String, Object> params = new HashMap<>();
        int code = 0;
        if (stockInfo.getFactoryPartsCode() != null && stockInfo.getFactoryPartsCode() != "") {
            params.put("eqPartCode", stockInfo.getFactoryPartsCode());
            List<SheetDetail> list = sheetDetailMapper.selectByMap(params);
            if (list.size() ==1) {//新增配件
                params.clear();
                params.put("eqSheetId",list.get(0).getSheetId());
                List<SheetDetail> sheetDetailList=sheetDetailMapper.selectByMap(params);
                if(sheetDetailList.size()==1) {
                sheetInfoMapper.deleteByPrimaryKey(list.get(0).getSheetId());
                }
                sheetDetailMapper.deleteByPartCode(stockInfo.getFactoryPartsCode());
                int row = stockInfoMapper.deleteByPrimaryKey(stockInfo.getFactoryPartsCode());
                if (row != 1) {
                    code = ErrCode.INCOMPLETE_INFO;
                }
            }else if(list.size() ==0){//初始化配件
                int row = stockInfoMapper.deleteByPrimaryKey(stockInfo.getFactoryPartsCode());
                if (row != 1) {
                    code = ErrCode.INCOMPLETE_INFO;
                }
            } else{
                code = ErrCode.DELETE_PARTS_NO_ALLOWED;
            }
        }

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    /**
     * 删除入库单据下的配件詳情信息
     */
    @ResponseBody
    @RequestMapping(value = "/deleteSheet", method = {RequestMethod.POST})
    public Map<String, Object> stockInfoDeleteBySheetId(@RequestParam(value = "sheet_id") String sheet_id, HttpSession httpSession) {
        user = (User) httpSession.getAttribute("AUTH_USER");
        logger.info("用户" + user.getUser_id() + " 删除配件入库单据为" + sheet_id + "的信息");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sheet_id", sheet_id);
        int code = stockInfoService.deleteStockInfoBySheetId(params);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    /**
     * 添加配件詳情信息
     */
    @ResponseBody
    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    public synchronized Map<String, Object> addStockInfo(@RequestBody StockInfoDTO stockInfo, HttpSession httpSession) {
        user = (User) httpSession.getAttribute("AUTH_USER");
        logger.info("用户" + user.getUser_id() + " 添加配件编号为 " + stockInfo.getPartIdSeq() + " 的信息");
        int code = 0;
        Map<String, Object> params = new HashMap<>();
        params.put("eqPartIdSeq", stockInfo.getPartIdSeq());
        List<StockInfo1> records = stockInfoMapper.selectByMap(params);
        if (records.size() > 0) {
            code = ErrCode.PART_ID_IS_EXIST;
        } else {
            AssetAttributes assetAttributes = depotHelper.getAssetAttribute(stockInfo.getDepotId());
            stockInfo.setAssetAttributesId(assetAttributes.getAssetAttributesId());
            if (stockInfoMapper.insert(stockInfo) != 1) {
                code = ErrCode.INCOMPLETE_INFO;
            }
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    /**
     * 修改配件詳情信息
     */
    @ResponseBody
    @RequestMapping(value = "/modify", method = {RequestMethod.POST})
    public Map<String, Object> modifyStockInfo(@RequestBody StockInfo1 stockInfo, HttpSession httpSession) {
        user = (User) httpSession.getAttribute("AUTH_USER");
        logger.info("用户" + user.getUser_id() + " 修改配件条码编号为 " + stockInfo.getPartIdSeq() + " 的信息");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sourcePartCode", stockInfo.getSourcePartCode());
        params.put("partCode", stockInfo.getFactoryPartsCode());
        params.put("updateTime", new Date());

        int code = 0;
        if (stockInfoMapper.updatePartCode(params) != 1) {
            code = ErrCode.INCOMPLETE_INFO;
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }


    /**
     * 导出检修预警详情
     */
    @RequestMapping(value = "exportStockInfos", method = {RequestMethod.GET})
    @ResponseBody
    public void exportStockInfos(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
        logger.info("导出配件详情信息Excel");
        Map<String, Object> map = new HashMap<String, Object>();
        List<StockInfoDTO> list = new ArrayList<>();
        String storeHouseId = (String) params.get("storeHouseId");
        String detectDeviceId = (String) params.get("detectDeviceId");
        if (StringUtils.isNotBlank(storeHouseId)) {

            if (StringUtils.isNotBlank(detectDeviceId)) {
                map.put("detectDeviceId", Integer.valueOf(detectDeviceId));//查询条件：探测站id
            }
            map.put("storeHouseId", Integer.valueOf(storeHouseId));//查询条件：仓库id
            map.put("eqEnabled", 1);//查询条件：仓库id
            list = stockInfoMapper.selectWithParts(map);
        }
        String file_name = "配件详情信息";
        String[] title = {"序号", "配件编号", "配件出厂编号", "配件名称", "规格型号", "数量", "单价", "存放位置", "购买时间", "备注"};
        HSSFWorkbook wb = ExcelUtil.exportStockInfosAsExcel(file_name, title, list, null);
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((file_name + ".xls").getBytes(), "iso-8859-1"));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            logger.error("exportStockInfos ERROR :" + e);
            e.printStackTrace();
        }
    }

    /**
     * 配件审核
     */
    @ResponseBody
    @RequestMapping(value = "/modifyVerify", method = {RequestMethod.POST})
    public Map<String, Object> modifyVerify(@RequestParam Map<String, Object> params, HttpSession httpSession) {
        user = (User) httpSession.getAttribute("AUTH_USER");
        logger.info("用户" + user.getUser_id() + " 审核编号为 " + params.get("sheet_id") + " 的信息");

        params.put("operator_id", user.getUser_id());
        params.put("operator_name", user.getUser_name());
        String sheet_id = (String) params.get("sheet_id");
        params.remove("sheet_id");
        params.put("sheet_id", sheet_id);

        int storage_state = Integer.valueOf(String.valueOf(params.get("storage_state")));
        params.remove("storage_state");
        params.put("storage_state", storage_state);

        int code = stockInfoService.modifyVerify(params);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    /**
     * 仓库查询
     */
    @ResponseBody
    @RequestMapping(value = "/getStorehouseByDepotId", method = {RequestMethod.POST})
    public DataTable<StoreHouse> getUserStorehouseByUserId(@RequestBody StoreHouse storeHouse, HttpServletRequest request) {
        logger.info("获取部门对应仓库信息");
        List<StoreHouse> list = new ArrayList<StoreHouse>();
        DataTable<StoreHouse> dt = new DataTable<StoreHouse>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("eqEnabled", (short) 1);
        params.put("orderByClause", "depot_id asc");
        if (storeHouse.getDepotId() != 0) {
            Depot depot = depotMapper.selectByPrimaryKey((long) storeHouse.getDepotId());
            //集团获取全部仓库
            if (depot.getDepotLevel() <= 3) {//最高级别
                list = storeHouseMapper.selectByMap(params);
            }

            //车间获取车间及其班组库
            else if (depot.getDepotLevel() == 4) {//车间
                params.put("depotId", storeHouse.getDepotId());
                list.addAll(storeHouseMapper.getDepotReceiptStoreHouse(params));
                params.clear();
                params.put("eqEnabled", (short) 1);
                params.put("eqDepotId", storeHouse.getDepotId());
                list.addAll(storeHouseMapper.selectByMap(params));


                //班组只获取自己的库
            } else if (depot.getDepotLevel() == 5) {//班组
                params.put("eqDepotId", storeHouse.getDepotId());//部门id
                list = storeHouseMapper.selectByMap(params);

            }
        }
        dt.setData(list);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
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
        params.put("eqStorehouseId", detectDevice.getStorehouseId());//仓库id
        //获取收货库
        list = detectDeviceMapper.selectByMap(params);
        //返回dataTable参数
        dt.setData(list);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }

    /**
     * getPartsZtreeDetails
     */
    @ResponseBody
    @RequestMapping(value = "/getPartsZtreeDetails", method = {RequestMethod.GET})
    public DataTable<StockInfoDTO> getPartsZtreeDetails(
            HttpServletRequest request) {
        logger.info("获取配件树形表查询条件");
        List<StockInfoDTO> list = new ArrayList<StockInfoDTO>();
        List<StockInfoDTO> storeHouses = new ArrayList<StockInfoDTO>();
        DataTable<StockInfoDTO> dt = new DataTable<StockInfoDTO>();
        Map<String, Object> params = new HashMap<String, Object>();
        String storeHouseId = request.getParameter("storeHouseId");
        String partsZtreeId = request.getParameter("partsZtreeId");
        String detectDeviceId = request.getParameter("detectDeviceId");
        String partId = request.getParameter("partId");
        String partCode = request.getParameter("partCode");
        if (StringUtils.isNotBlank(storeHouseId)) {
            if (StringUtils.isNotBlank(partsZtreeId)) {
                PartsZtree partsZtree = partsZtreeMapper.selectByPrimaryKey(Integer.valueOf(partsZtreeId));
                //判断是否是3级配件格式
                if (partsZtree.getFuncLevel() == 3 && partsZtree.getPartId() != null) {
                    partId = partsZtree.getPartId();
                    params.put("subPartId", partId);//查询条件：配件编码前缀
                }
            } else if (StringUtils.isNotBlank(partCode) || StringUtils.isNotBlank(partId)) {
                params.put("eqPartId", partId);//查询条件：配件编号
                params.put("eqPartCode", partCode);//查询条件：配件出厂编码
            }
            if (StringUtils.isNotBlank(detectDeviceId)) {
                params.put("detectDeviceId", Integer.valueOf(detectDeviceId));//查询条件：探测站id
            }
            params.put("storeHouseId", Integer.valueOf(storeHouseId));//查询条件：仓库id
            params.put("eqEnabled", 1);//查询条件：仓库id
            list = stockInfoMapper.selectWithParts(params);
        }
        //返回dataTable参数
        dt.setData(list);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }


    /**
     * 获取对应备品库
     *
     * @param
     * @param request
     * @return StockInfoDTO
     */
    @ResponseBody
    @RequestMapping(value = "/getAllSourceStoreHouse", method = {RequestMethod.POST})
    public DataTable<StoreHouse> getAllReceiptStoreHouse(
            HttpServletRequest request, @RequestBody StoreHouse storeHouse) {
        logger.info("获取源仓库信息");
        List<StoreHouse> list = new ArrayList<StoreHouse>();
        List<StoreHouse> storeHouses = new ArrayList<StoreHouse>();
        DataTable<StoreHouse> dt = new DataTable<StoreHouse>();
        Map<String, Object> params = new HashMap<String, Object>();
        if (storeHouse != null && storeHouse.getDepotId() > 3) {//非车辆检测所
            params.put("eqType", (short) 13);//备品库类型

            params.put("eqDepotId", storeHouse.getDepotId());//收货库类型
        }
        params.put("eqEnabled", (short) 1);//可用
        storeHouses = storeHouseMapper.selectByMap(params);
        if (storeHouses.size() < 1) {
            params.put("eqType", (short) 6);
            storeHouses = storeHouseMapper.selectByMap(params);
        }
        //获取收货库
        list.addAll(storeHouses);
        params.put("eqType", (short) 7);//送修库类型
        list.addAll(storeHouseMapper.selectByMap(params));
        //返回dataTable参数
        dt.setData(list);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }

    /**
     * 获取编码范围
     *
     * @param request
     * @param httpSession
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMaxPartId", method = {RequestMethod.GET})
    public synchronized String getMaxPartId(HttpServletRequest request, HttpSession httpSession) {
        logger.info("获取最大配件编码");
        Map<String, Object> params = new HashMap<String, Object>();
        String partId = request.getParameter("part_id");
        String depotIdString = request.getParameter("depot_id");
        String supplierName = request.getParameter("supplier_name");
        String deviceModelName = request.getParameter("device_model_name");
        Long depotId = depotHelper.getWorkShop(Long.valueOf(depotIdString));
        if (depotId != null) {
            params.put("eqDepotId", depotId);
        } else {
            return "";
        }
        params.put("eqSupplierName", supplierName);
        params.put("eqDeviceModelName", deviceModelName);
        if (StringUtils.isBlank(partId) || partId.length() != 5) {//配件编码前缀为5
            return "";
        }
        List<PartIdConfig> partIdConfigs = new ArrayList<PartIdConfig>();
        partIdConfigs = partIdConfigMapper.selectByMap(params);
        //获取配置的partId序列范围
        if (partIdConfigs.size() > 0) {
            PartIdConfig partIdConfig = partIdConfigs.get(0);
            String maxPartId = partId.substring(0, 5) + partIdConfig.getMaxPartId();
            String minPartId = partId.substring(0, 5) + partIdConfig.getMinPartId();

            //获取范围内的最大partId
            params.clear();
            params.put("maxPartId", maxPartId);
            params.put("minPartId", minPartId);
            String result = stockInfoMapper.getMaxPartId(params);
            if (StringUtils.isBlank(result) || result.length() < 9) {
                maxPartId = minPartId;

            } else if (maxPartId.equals(result)) {
                return "";

            } else {

                //获取范围内的最大partId+1,拼接序列号
                Integer serial = Integer.valueOf(result.substring(5));
                serial++;
                if (serial < 10) {
                    maxPartId = maxPartId.substring(0, 5) + "000" + String.valueOf(serial);
                } else if (serial < 100) {
                    maxPartId = maxPartId.substring(0, 5) + "00" + String.valueOf(serial);
                } else if (serial < 1000) {
                    maxPartId = maxPartId.substring(0, 5) + "0" + String.valueOf(serial);
                } else {
                    maxPartId = maxPartId.substring(0, 5) + String.valueOf(serial);
                }

            }

            return maxPartId;
        } else {
            return "";
        }

    }
}
