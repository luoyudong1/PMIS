package com.kthw.pmis.controller.ExportExcel;

import com.kthw.pmis.entiy.*;
import com.kthw.pmis.entiy.bo.PlanCheckExportBO;
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import com.kthw.pmis.entiy.dto.StockInfoCountDTO;
import com.kthw.pmis.helper.DepotHelper;
import com.kthw.pmis.mapper.common.*;
import com.kthw.pmis.util.excel.ExportExcelUtil;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class JxlsExcelController {
    private static Logger logger = LoggerFactory.getLogger(JxlsExcelController.class);
    @Autowired
    private FaultHandleMapper faultHandleMapper;
    @Autowired
    private StockInfo1Mapper stockInfoMapper;
    @Autowired
    private DepotHelper depotHelper;
    @Autowired
    private PlanCheckSheetMapper planCheckSheetMapper;
    @Autowired
    private PlanCheckMapper planCheckMapper;
    @Autowired
    private SheetDetailMapper sheetDetailMapper;
    private static final String CONTENT_TYPE = "application/vnd.ms-excel;charset=utf-8";

    private static final String templatePath = "excel/template.xls";
    private static final List<String> shetetNames=new ArrayList<>();

    private static final String exportFileName = "template.xls";
    private static final String exportStockInfoCountFileName = "库存信息统计.xls";
    private static final String exportFaultHandelFileName = "故障预报单据.xls";
    private static final String exportPlanCheckFileName = "检修计划单据.xls";
    private static final String exportCheckRepairFileName = "所内检修记录.xls";
    static {
        shetetNames.add("设备故障");
        shetetNames.add("2故障");
        shetetNames.add("3故障");
        shetetNames.add("4故障");
    }
    @RequestMapping(value = "/export", method = {RequestMethod.GET})
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Running Object Collection demo");
        //模拟数据
        try {
            List<FaultHandle> list = faultHandleMapper.selectByMap(null);
            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            XLSTransformer transformer = new XLSTransformer();
            OutputStream out = null;
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((exportFileName).getBytes(), "iso-8859-1"));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("excel/template.xls");   //得到文档的路径
//            in=new BufferedInputStream(new FileInputStream(templatePath));
            Workbook workbook = transformer.transformXLS(in, map);
            out = response.getOutputStream();
            //将内容写入输出流并把缓存的内容全部发出去
            workbook.write(out);
            out.flush();
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/stockInfo/exportCount/{storeHouseId}", method = {RequestMethod.GET})
    @ResponseBody
    public void exportCount(HttpServletRequest request, @PathVariable("storeHouseId") Integer storeHouseId, HttpServletResponse response) {
        logger.info("导出仓库配件数量");
        //模拟数据
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("storeHouseId", storeHouseId);
            List<StockInfoCountDTO> list = stockInfoMapper.countPartsByStoreHouse(params);
            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            XLSTransformer transformer = new XLSTransformer();
            OutputStream out = null;
            InputStream in = null;
            response.setContentType(CONTENT_TYPE);
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((exportStockInfoCountFileName).getBytes(), "iso-8859-1"));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            in = this.getClass().getClassLoader().getResourceAsStream("excel/exportStockInfoCount.xls");   //得到文档的路径
            Workbook workbook = transformer.transformXLS(in, map);
            out = response.getOutputStream();
            //将内容写入输出流并把缓存的内容全部发出去
            workbook.write(out);
            out.flush();
            out.close();
            in.close();
        } catch (Exception e) {
            logger.error("导出仓库配件数量错误");
        }
    }
    @RequestMapping(value = "/stockInfo/export", method = {RequestMethod.GET})
    @ResponseBody
    public void exportCount(HttpServletRequest request, HttpServletResponse response) {
        logger.info("导出仓库配件数量");
        //模拟数据
        try {
            Map<String, Object> params = new HashMap<>();
            List<StockInfoCountDTO> list = stockInfoMapper.countParts(params);
            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            XLSTransformer transformer = new XLSTransformer();
            OutputStream out = null;
            InputStream in = null;
            response.setContentType(CONTENT_TYPE);
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((exportStockInfoCountFileName).getBytes(), "iso-8859-1"));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            in = this.getClass().getClassLoader().getResourceAsStream("excel/exportStockInfoCount.xls");   //得到文档的路径
            Workbook workbook = transformer.transformXLS(in, map);
            out = response.getOutputStream();
            //将内容写入输出流并把缓存的内容全部发出去
            workbook.write(out);
            out.flush();
            out.close();
            in.close();
        } catch (Exception e) {
            logger.error("导出仓库配件数量错误");
        }
    }
    @RequestMapping(value = "/faultHandle/exportExcel/{depotId}", method = {RequestMethod.GET})
    @ResponseBody
    public void exportFaultHandle(HttpServletRequest request, @PathVariable("depotId") Long depotId, HttpServletResponse response) {
        logger.info("导出故障预报");
        //模拟数据
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("eqFinished", (short)0);
            //获取faultHandle表
            if (depotId!=null) {
                List<Depot> childrens = depotHelper.getChildrens(depotId);
                params.put("depotIdList", childrens);
            }
            params.put("eqType", "设备故障");
            List<FaultHandle> list_device = faultHandleMapper.selectByMap(params);
            params.put("eqType", "电力故障");
            List<FaultHandle> list_electric = faultHandleMapper.selectByMap(params);
            params.put("eqType", "通信故障");
            List<FaultHandle> list_net = faultHandleMapper.selectByMap(params);
            params.put("eqType", "信息故障");
            List<FaultHandle> list_info = faultHandleMapper.selectByMap(params);
            Map<String,List<FaultHandle>> map = new HashMap<>();
            ArrayList<List> objects = new ArrayList<List>();//添加数据到objects中
            map.put("list", list_device);
            map.put("list1", list_electric);
            map.put("list2", list_net);
            map.put("list3", list_info);
            objects.add(list_device);
            objects.add(list_electric);
            objects.add(list_net);
            objects.add(list_info);
            XLSTransformer transformer = new XLSTransformer();
            OutputStream out = null;
            InputStream in = null;
            response.setContentType(CONTENT_TYPE);
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((exportFaultHandelFileName).getBytes(), "iso-8859-1"));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            in = this.getClass().getClassLoader().getResourceAsStream("excel/faultHandle.xls");   //得到文档的路径
            //一个xls文件多个sheet
            Workbook workbook = transformer.transformMultipleSheetsList(in,objects,shetetNames,"xxx",map,0);
            workbook.removeSheetAt(4);
            workbook.removeSheetAt(4);
            workbook.removeSheetAt(4);
            out = response.getOutputStream();
            //将内容写入输出流并把缓存的内容全部发出去
            workbook.write(out);
            out.flush();
            out.close();
            in.close();
        } catch (Exception e) {
            logger.error("导出导出故障预报错误");
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/checkPlan/sheet/export/{sheetId}", method = {RequestMethod.GET})
    @ResponseBody
    public void exportCheckPlan(HttpServletRequest request, @PathVariable("sheetId") String sheetId, HttpServletResponse response) {
        logger.info("导出检修计划");
        Map<String, Object> params = new HashMap<>();
        params.put("eqSheetId", sheetId);// 获取单据信息
        PlanCheckSheet planCheckSheet=planCheckSheetMapper.selectByPrimaryKey(sheetId);
        List<PlanCheck> planChecks = new ArrayList<>();// 获取单据对应的配件详情
        planChecks = planCheckMapper.selectByMap(params);
        String sheetName=planCheckSheet.getDepotName()+planCheckSheet.getYear()+"年"+planCheckSheet.getMonth()+"月"+"检修计划表";
        try {
            HSSFWorkbook wb = ExportExcelUtil.exportPlanCheckExcel(sheetName,  planChecks, planCheckSheet, null);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((exportPlanCheckFileName + ".xls").getBytes(), "iso-8859-1"));
            response.addHeader("Cache-Control", "no-cache");
            response.addHeader("Pargam", "no-cache");
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            logger.error("导出检修计划出错 :" + e);
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/repairManage/check/export/{sheetId}/{partCode}", method = {RequestMethod.GET})
    @ResponseBody
    public void exportCheckRepair(HttpServletRequest request, @PathVariable("sheetId") String sheetId,@PathVariable("partCode") String partCode, HttpServletResponse response) {
        logger.info("导出所内检修记录");
        Map<String, Object> params = new HashMap<>();
        params.put("eqSheetId", sheetId);// 获取单据信息
        params.put("eqPartCode", partCode);// 获取单据信息
        try {
            SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            List<SheetDetailDTO> list =new ArrayList<>();
            list=sheetDetailMapper.selectWithParts(params);
            SheetDetailDTO sheetDetail=list.get(0);
            Map<String,Object> map = new HashMap<>();
            map.put("checkedDate", sd.format(sheetDetail.getCheckedDate()));
            map.put("devicePartsName", sheetDetail.getDevicePartsName());
            map.put("deviceModelName", sheetDetail.getDeviceModelName());
            map.put("deviceTypeName", sheetDetail.getDeviceTypeName());
            map.put("supplierName", sheetDetail.getSupplierName());
            map.put("partId", sheetDetail.getPartId());
            map.put("partCode", sheetDetail.getPartCode());
            map.put("assetAttributesName", sheetDetail.getAssetAttributesName());
            if(sheetDetail.getPartState()==1){
                map.put("partState", "新购");
            }else if(sheetDetail.getPartState()==2){
                map.put("partState", "送修");
            }else if(sheetDetail.getPartState()==3){
                map.put("partState", "初始化在探测站");
            }else if(sheetDetail.getPartState()==4){
                map.put("partState", "初始化在备品库");
            }else if(sheetDetail.getPartState()==5){
                map.put("partState", "初始化在送修库");
            }
            map.put("faultInfo", sheetDetail.getFaultInfo());
            map.put("prepareCheck", sheetDetail.getPrepareCheck());
            map.put("machineCheck", sheetDetail.getMachineCheck());
            map.put("replaceComponentCheck", sheetDetail.getReplaceComponentCheck());
            map.put("copyMachineStartTime", sd.format(sheetDetail.getCopyMachineStartTime()));
            map.put("copyMachineEndTime", sd.format(sheetDetail.getCopyMachineEndTime()));
            map.put("copyMachineCheck", sheetDetail.getCopyMachineCheck());
            if(sheetDetail.getRepaireState()==0){
                map.put("repaireState", "不合格");
            }else  if(sheetDetail.getRepaireState()==1){
                map.put("repaireState", "合格");
            }else  if(sheetDetail.getRepaireState()==2){
                map.put("repaireState", "报废");
            }
            map.put("checkedUserId", sheetDetail.getCheckedUserId());
            XLSTransformer transformer = new XLSTransformer();
            OutputStream out = null;
            InputStream in = null;
            response.setContentType(CONTENT_TYPE);
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((exportCheckRepairFileName).getBytes(), "iso-8859-1"));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            in = this.getClass().getClassLoader().getResourceAsStream("excel/checkRepair.xls");   //得到文档的路径
            Workbook workbook = transformer.transformXLS(in, map);
            out = response.getOutputStream();
            //将内容写入输出流并把缓存的内容全部发出去
            workbook.write(out);
            out.flush();
            out.close();
            in.close();
        } catch (Exception e) {
            logger.error("导出所内检修记录出错 :" + e);
            e.printStackTrace();
        }
    }
}
