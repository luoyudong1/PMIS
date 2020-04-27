package com.kthw.pmis.controller.detectManage;


import java.io.OutputStream;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kthw.pmis.entiy.*;
import com.kthw.pmis.entiy.dto.DetectDeviceDTO;
import com.kthw.pmis.entiy.dto.DetectPartsDTO;
import com.kthw.pmis.entiy.ext.ViewParts;
import com.kthw.pmis.helper.DepotHelper;
import com.kthw.pmis.mapper.common.*;
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
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import com.kthw.pmis.entiy.ext.SheetInfoExt;

/**
 * 描述:探测站管理Controller.
 */
@Transactional
@Controller
@RequestMapping(value = "/detectManage/detectManage")
public class DetectManageController {
    private static Logger logger = LoggerFactory.getLogger(DetectManageController.class);
    @Autowired
    private SheetInfoMapper sheetInfoMapper;
    @Autowired
    private SheetDetailMapper sheetDetailMapper;
    @Autowired
    private StockInfo1Mapper stockInfoMapper;
    @Autowired
    private PartsZtreeMapper partsZtreeMapper;
    @Autowired
    private DepotHelper depotHelper;
    @Autowired
    private DetectDeviceMapper detectDeviceMapper;
    @Autowired
    private DetectPartsMapper detectPartsMapper;
    @Autowired
    private DeviceModelMapper deviceModelMapper;
    @Autowired
    private StoreHouseMapper storeHouseMapper;
    @Autowired
    private PartsDictMapper partsDictMapper;
    @ResponseBody
    @RequestMapping(value = "/listDetect", method = {RequestMethod.GET})
    public DataTable<DetectDeviceDTO> sheetsList(HttpServletRequest request) {
        logger.info("显示探测站");
        Map<String, Object> params=new HashMap<>();
        String depotId = request.getParameter("depotId");
        String faultEnable = request.getParameter("faultEnable");
        logger.info("显示探测站"+faultEnable);
        List<DetectDeviceDTO> list = new ArrayList<>();
        DataTable<DetectDeviceDTO> dt = new DataTable<DetectDeviceDTO>();
        int total = 0;
        try {
        //获取子部门
            if (StringUtils.isNotBlank(depotId)) {
                List<Depot> childrens = depotHelper.getChildrens(Long.valueOf(depotId));
                params.put("depotIdList", childrens);
            } if (StringUtils.isNotBlank(faultEnable)) {
                params.put("eqFaultEnable", Integer.valueOf(faultEnable));
            }
            params.put("orderByClause","ddd.create_time desc");
            list=detectDeviceMapper.listDetectDevice(params);
            dt.setRecordsTotal(total);
            dt.setRecordsFiltered(total);
        } catch (Exception e) {
            logger.error("listDetect error" + request);
        }
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
    @RequestMapping(value = "/listDetectParts", method = {RequestMethod.POST})
    public DataTable<DetectPartsDTO> SheetDetailsList(HttpServletRequest request,
                                                      @RequestParam Map<String, Object> params) {
        logger.info("显示探测站详情");
        DataTable<DetectPartsDTO> dt = new DataTable<DetectPartsDTO>();
        List<DetectPartsDTO> data = new ArrayList<DetectPartsDTO>();
        try {
            String detectDeviceId = request.getParameter("detectDeviceId");
            // 为空直接返回
            if (StringUtils.isNotBlank(detectDeviceId)) {
                params.put("eqDetectDeviceId", Integer.valueOf(detectDeviceId));
                data = detectPartsMapper.listDetectParts(params);
            }
        } catch (Exception e) {
            logger.error("listDetectParts error" + request);
        }
        dt.setData(data);
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
    @RequestMapping(value = "/createDetect", method = {RequestMethod.POST})
    public Map<String, Object> createDetect(@RequestBody DetectDevice detectDevice) {
        logger.info("新增探测站");
        int code = 0;
        Map<String,Object> params=new HashMap<>();
        try {
             if(detectDevice!=null){
                 Integer maxDetectId=detectDeviceMapper.getMaxDetectId();
                 detectDevice.setDetectDeviceId(maxDetectId+1);
                 params.put("eqDepotId",detectDevice.getDepotId());
                 params.put("eqType",(short)8);
                 List<StoreHouse> list=storeHouseMapper.selectByMap(params);
                 detectDevice.setStorehouseId(list.get(0).getStorehouseId());
                 detectDeviceMapper.insert(detectDevice);
             }


        } catch (Exception e) {
            logger.error("createDetect error" + detectDevice);
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }
    @ResponseBody
    @RequestMapping(value = "/detectPartsCreate", method = {RequestMethod.POST})
    public Map<String, Object> detectPartsCreate(@RequestBody DetectParts detectParts) {
        logger.info("新增探测站");
        int code = 0;
        Map<String,Object> params=new HashMap<>();
        try {
            if(detectParts!=null){
                Integer maxDetectId=detectPartsMapper.getMaxDetectId();
                detectParts.setDetectPartsId(maxDetectId+1);
                detectPartsMapper.insert(detectParts);
            }


        } catch (Exception e) {
            logger.error("detectPartsCreate error" + detectParts);
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }
    @ResponseBody
    @RequestMapping(value = "/detectPartsDelete", method = {RequestMethod.POST})
    public Map<String, Object> detectPartsDelete(@RequestBody DetectParts detectParts) {
        logger.info("删除探测站配件");
        int code = 0;
        try {
            if(detectParts!=null){

                detectPartsMapper.deleteByPrimaryKey(detectParts.getDetectPartsId());
            }


        } catch (Exception e) {
            logger.error("detectPartsDelete error" + detectParts);
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
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getDeviceModel", method = {RequestMethod.GET})
    public List<DeviceModel> getDeviceModel(HttpServletRequest request) {
        logger.info("获取设备型号");

        List<DeviceModel> list = new ArrayList<DeviceModel>();
        try {
            list = deviceModelMapper.selectByMap(null);
        } catch (Exception e) {
            logger.error("getDeviceModel error" + request);
        }
        return list;
    }
    /**
     * 根据sheetId获取单据详情
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getManageDepot", method = {RequestMethod.GET})
    public List<Depot> getManageDepot(HttpServletRequest request) {
        logger.info("获取车间班组");
        Depot depot=new Depot();
        List<Depot> list = new ArrayList<Depot>();
        String depotId=request.getParameter("depotId");
        if(StringUtils.isNotBlank(depotId)){
            depot.setDepotId(Long.valueOf(depotId));

        try {
            list = depotHelper.getChildDepots(depot);
        } catch (Exception e) {
            logger.error("getManageDepot error" + request);
        }
        }
        return list;
    }
    /**
     * 根据sheetId获取单据详情
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getViewParts", method = {RequestMethod.GET})
    public DataTable<ViewParts> getViewParts(HttpServletRequest request) {
        logger.info("根据型号厂家获取配件");
        Depot depot=new Depot();
        List<ViewParts> list = new ArrayList<ViewParts>();
        DataTable<ViewParts> dt = new DataTable<ViewParts>();
        Map<String,Object> parms=new HashMap<>();
        String supplierName=request.getParameter("supplierName");
        String deviceModelName=request.getParameter("deviceModelName");
        if(StringUtils.isNotBlank(supplierName)&&StringUtils.isNotBlank(deviceModelName)){
            parms.put("deviceModelName",deviceModelName);
            parms.put("supplierName",supplierName);

            try {
                list = partsDictMapper.getPartsByModel(parms);
            } catch (Exception e) {
                logger.error("/getViewParts error" + request);
            }
        }
        dt.setData(list);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }
    @ResponseBody
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public Map<String, Object> delete(@RequestBody DetectDevice detectDevice) {
        logger.info("删除探测站");
        int code = 0;
        try{
            List<DetectParts> detectParts=new ArrayList<>();
            Integer detecDeviceId=detectDevice.getDetectDeviceId();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("eqDetectDeviceId", detecDeviceId);
            params.put("eqIfMoved", (short)0);
            detectParts=detectPartsMapper.selectByMap(params);
            if (detectParts.size() > 0) {
                code=ErrCode.DETECT_PARTS_NO_MOVED;

            }else {
                 //删除探测站
                detectDeviceMapper.deleteByPrimaryKey(detecDeviceId);
                detectPartsMapper.deleteByDetectDeviceId(detecDeviceId);
            }
        } catch (Exception e) {
            logger.error("delete error" + detectDevice);
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
