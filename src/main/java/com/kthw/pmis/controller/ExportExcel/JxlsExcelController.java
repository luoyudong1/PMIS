package com.kthw.pmis.controller.ExportExcel;

import com.kthw.pmis.entiy.FaultHandle;
import com.kthw.pmis.mapper.common.FaultHandleMapper;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    private static final String CONTENT_TYPE = "application/vnd.ms-excel";

    private static final String templatePath = "excel/template.xls";

    private static final String exportFileName = "template.xls";

    @RequestMapping(value = "/export", method = {RequestMethod.GET})
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Running Object Collection demo");
        //模拟数据
        try {
            List<FaultHandle> list= faultHandleMapper.selectByMap(null);
            Map<String,Object> map=new HashMap<>();
            map.put("list",list);
            XLSTransformer transformer = new XLSTransformer();
            OutputStream out=null;
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((exportFileName).getBytes(), "iso-8859-1"));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("excel/template.xls");   //得到文档的路径
//            in=new BufferedInputStream(new FileInputStream(templatePath));
            Workbook workbook=transformer.transformXLS(in, map);
            out=response.getOutputStream();
            //将内容写入输出流并把缓存的内容全部发出去
            workbook.write(out);
            out.flush();
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
