package com.kthw.pmis.serviceimpl.excel;

import com.kthw.pmis.entiy.FaultHandle;
import com.kthw.pmis.service.excel.JxlsExcelService;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JxlsExcelServiceImpl implements JxlsExcelService {

    @Override
    public void createMultiSheetExcel(InputStream inputStream, OutputStream outputStream, String filename, List<String> sheetNamelist, ArrayList<List> objects, String tagname, List<List<FaultHandle>> list)throws IOException {
        XLSTransformer transformer = new XLSTransformer();
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("list", list);
        Workbook workbook = transformer.transformMultipleSheetsList(inputStream, objects, sheetNamelist, tagname, param, 0);
        workbook.removeSheetAt(3);
        workbook.setSheetName(0, "所有字段Sheet");
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }
}
