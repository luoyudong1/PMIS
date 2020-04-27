package com.kthw.pmis.util.excel;

import com.kthw.pmis.entiy.SheetInfo;
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;

public interface ExportSheet {

    HSSFWorkbook export(String sheetName, String[] title, List<SheetDetailDTO> list, SheetInfo sheetInfo,
                         HSSFWorkbook wb);
}
