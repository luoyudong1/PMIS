package com.kthw.pmis.controller.ExportExcel;

import com.kthw.common.ExcelUtil;
import com.kthw.pmis.controller.entryAndOut.PreparePurchaseController;
import com.kthw.pmis.entiy.PlanCheck;
import com.kthw.pmis.entiy.PlanCheckSheet;
import com.kthw.pmis.entiy.SheetInfo;
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import com.kthw.pmis.mapper.common.*;
import com.kthw.pmis.util.excel.ExportExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Controller
@RequestMapping(value = "/exportExcel")
public class ExportExelController {
    private static Logger logger = LoggerFactory.getLogger(ExportExelController.class);
    @Autowired
    private SheetInfoMapper sheetInfoMapper;
    @Autowired
    private SheetDetailMapper sheetDetailMapper;
    @Autowired
    private PlanCheckSheetMapper planCheckSheetMapper;
    @Autowired
    private PlanCheckMapper planCheckMapper;

    // ExcelUtil导出报表
    @RequestMapping(value = "/export", method = {RequestMethod.GET})
    @ResponseBody
    public void exportSheetInfo(@RequestParam Map<String, Object> params, HttpServletRequest request,
                                HttpServletResponse response) {
        logger.info("ExcelUtil导出报表");
        String sheetId = (String) params.get("sheetId");
        SheetInfo sheetInfo = sheetInfoMapper.selectByPrimaryKey(sheetId);// 获取单据信息
        params.put("eqSheetId", sheetId);// 获取单据信息
        List<SheetDetailDTO> sheetDetailList = sheetDetailMapper.selectWithParts(params);// 获取单据对应的配件详情
        String file_name = (String) params.get("fileName");
        String[] title = {"编号", "关键配件名称", "设备型号", "设备类型", "生产厂家", "配件出厂编码", "配件二维码", "资产配属", "数量", "单价", "备注"};
        HSSFWorkbook wb = ExportExcelUtil.exportExcel(file_name, title, sheetDetailList, sheetInfo, null);
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

    // ExcelUtil导出报表
    @RequestMapping(value = "/exportCheckInfo", method = {RequestMethod.GET})
    @ResponseBody
    public void exportCheckInfo(@RequestParam Map<String, Object> params, HttpServletRequest request,
                                HttpServletResponse response) {
        logger.info("ExcelUtil导出报表");
        String sheetId = (String) params.get("sheetId");
        SheetInfo sheetInfo = sheetInfoMapper.selectByPrimaryKey(sheetId);// 获取单据信息
        params.put("eqSheetId", sheetId);// 获取单据信息
        List<SheetDetailDTO> sheetDetailList = sheetDetailMapper.selectWithParts(params);// 获取单据对应的配件详情
        String file_name = (String) params.get("fileName");
        String[] title = {"编号", "关键配件名称", "设备型号", "设备类型", "生产厂家", "配件出厂编码", "配件二维码", "资产配属", "配件属性", "故障现象", "上机检测", "检测计算机检测",
                "更换元件情况", "拷机开始时间", "拷机结束时间", "拷机检测情况", "检修价格", "报废原因", "检测结论"};
        HSSFWorkbook wb = ExportExcelUtil.exportCheckExcel(file_name, title, sheetDetailList, sheetInfo, null);
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
                    "attachment;filename=" + new String(("xxx" + ".xls").getBytes(), "iso-8859-1"));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            logger.error("exportTest ERROR :" + e);
            e.printStackTrace();
        }
    }
}
