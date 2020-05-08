package com.kthw.pmis.controller.ExportExcel;

import com.kthw.pmis.entiy.Depot;
import com.kthw.pmis.entiy.FaultHandle;
import com.kthw.pmis.entiy.dto.StockInfoCountDTO;
import com.kthw.pmis.helper.DepotHelper;
import com.kthw.pmis.mapper.common.FaultHandleMapper;
import com.kthw.pmis.mapper.common.StockInfo1Mapper;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.lang.StringUtils;
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
    private static final String CONTENT_TYPE = "application/vnd.ms-excel;charset=utf-8";

    private static final String templatePath = "excel/template.xls";
    private static final List<String> shetetNames=new ArrayList<>();

    private static final String exportFileName = "template.xls";
    private static final String exportStockInfoCountFileName = "库存信息统计.xls";
    private static final String exportFaultHandelFileName = "故障预报单据.xls";
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
}
