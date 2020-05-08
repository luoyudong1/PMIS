package com.kthw.pmis.service.excel;

import com.kthw.pmis.entiy.FaultHandle;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public interface JxlsExcelService {

    void createMultiSheetExcel(InputStream inputStream, OutputStream outputStream, String filename, List<String> sheetNamelist, ArrayList<List> objects, String tagname, List<List<FaultHandle>> list)throws IOException;

}
