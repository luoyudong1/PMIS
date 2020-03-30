package com.kthw.pmis.util.excel;

import com.kthw.common.ExcelUtil;
import com.kthw.pmis.entiy.Depot;
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import com.kthw.pmis.entiy.dto.StockInfoDTO;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class ExportExcelUtil {

    /**
     * 导出预采购单据
     *
     * @param sheetName
     * @param title
     * @param list
     * @param sheetInfo
     * @param wb
     * @return
     */
    public static HSSFWorkbook exportExcel(String sheetName, String[] title,
                                           List<SheetDetailDTO> list, com.kthw.pmis.entiy.SheetInfo sheetInfo, HSSFWorkbook wb) {
        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        HSSFSheet sheet = wb.createSheet(sheetName);
        // 设置样式信息
        HSSFCellStyle title_style = ExcelUtil.cellStyle(wb, 1);
        HSSFCellStyle top_style = ExcelUtil.cellStyle(wb, 2);
        HSSFCellStyle cell_style = ExcelUtil.cellStyle(wb, 3);

        sheet.setColumnWidth(0, 256 * 5); // 序号
        sheet.setColumnWidth(1, 256 * 25); // 关键配件名称
        sheet.setColumnWidth(4, 256 * 20); // 生产厂家
        sheet.setColumnWidth(5, 256 * 25); // 关键配件出厂编码
        sheet.setColumnWidth(6, 256 * 15); // 配件二维码
        sheet.setColumnWidth(7, 256 * 25); // 资产配属
        sheet.setColumnWidth(8, 256 * 15); // 数量
        sheet.setColumnWidth(9, 256 * 15); // 单价
        sheet.setColumnWidth(10, 256 * 20); // 备注
        // 设置标题
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) 750);
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, title.length - 1);
        sheet.addMergedRegion(cra); // 合并单元格
        HSSFCell cell;
        HSSFFont font = wb.createFont();
        HSSFCellStyle style = wb.createCellStyle();
        cell = row.createCell(0);
        cell.setCellValue(sheetName);
        HSSFFont font4 = wb.createFont();
        HSSFCellStyle style4 = wb.createCellStyle();
        font4.setFontName("宋体");//设置字体名称
        font4.setFontHeightInPoints((short) 14);//设置字号
        font4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style4.setFont(font4);
        cell.setCellStyle(style4);
        style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        // 设置表头
        row = sheet.createRow(1);
        row.setHeight((short) 600);
        cell = row.createCell(0);
        HSSFFont font6 = wb.createFont();
        HSSFCellStyle style6 = wb.createCellStyle();
        font6.setFontName("宋体");//设置字体名称
        font6.setFontHeightInPoints((short) 11);//设置字号
        font6.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style6.setFont(font6);
        cell.setCellStyle(style6);
        cell.setCellValue("单据编号：" + sheetInfo.getSheetId());
        row = sheet.createRow(2);
        row.setHeight((short) 600);
        row = sheet.createRow(3);
        row.setHeight((short) 550);
        cell = row.createCell(0);
        cell.setCellValue("源仓库：" + list.get(0).getSourceStoreHouseName());
        HSSFFont font3 = wb.createFont();
        HSSFCellStyle style3 = wb.createCellStyle();
        font3.setFontName("宋体");//设置字体名称
        font3.setFontHeightInPoints((short) 11);//设置字号
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style3.setFont(font3);
        cell.setCellStyle(style3);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        cell = row.createCell(6);
        cell.setCellValue("日期：");
        font.setFontName("宋体");//设置字体名称
        font.setFontHeightInPoints((short) 11);//设置字号
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style.setFont(font);
        cell.setCellStyle(style);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        cell = row.createCell(7);
        cell.setCellValue(new SimpleDateFormat("yyyy年MM月dd日").format(sheetInfo.getAddDate()));
        font.setFontName("宋体");//设置字体名称
        font.setFontHeightInPoints((short) 11);//设置字号
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style.setFont(font);
        cell.setCellStyle(style);
        CellRangeAddress cr = new CellRangeAddress(1, 1, 8, title.length - 1);
        sheet.addMergedRegion(cr); // 合并单元格
        cell = row.createCell(10);
        cell.setCellValue("目的仓库：" + list.get(0).getObjectStoreHouseName());
        font.setFontName("宋体");//设置字体名称
        font.setFontHeightInPoints((short) 11);//设置字号
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style.setFont(font);
        cell.setCellStyle(style);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中

        row = sheet.createRow(4);
        row.setHeight((short) 550);
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(top_style);
        }
        SheetDetailDTO si = null;
        int quantityNum = 0;
        double priceNum = 0;
        for (int i = 0; i < list.size(); i++) {
            si = list.get(i);
            row = sheet.createRow(i + 5);
            row.setHeight((short) 550);
            si.setQuantity((short) 1);
            quantityNum += si.getQuantity();
            if (si.getUnitPrice() != null) {
                priceNum += si.getUnitPrice().doubleValue();
            }
            // 编号
            cell = row.createCell(0);
            cell.setCellValue(i + 1);
            cell.setCellStyle(cell_style);
            // 关键配件名称
            cell = row.createCell(1);
            cell.setCellValue(si.getDevicePartsName());
            cell.setCellStyle(cell_style);
            // 设备型号
            cell = row.createCell(2);
            cell.setCellValue(si.getDeviceModelName());
            cell.setCellStyle(cell_style);
            // 设备类型
            cell = row.createCell(3);
            cell.setCellValue(si.getDeviceTypeName());
            cell.setCellStyle(cell_style);
            // 生成厂家
            cell = row.createCell(4);
            cell.setCellValue(si.getSupplierName());
            cell.setCellStyle(cell_style);
            // 配件出厂编码
            cell = row.createCell(5);
            cell.setCellValue(si.getPartCode());
            cell.setCellStyle(cell_style);
            // 配件二维码
            cell = row.createCell(6);
            cell.setCellValue(si.getPartId());
            cell.setCellStyle(cell_style);
            // 资产配属
            cell = row.createCell(7);
            cell.setCellValue(si.getAssetAttributesName());
            cell.setCellStyle(cell_style);
            // 数量
            cell = row.createCell(8);
            cell.setCellValue(si.getQuantity());
            cell.setCellStyle(cell_style);
            // 单价
            cell = row.createCell(9);
            if (si.getPurchaseOrRepairedPrice() != null) {
                cell.setCellValue(si.getPurchaseOrRepairedPrice().doubleValue());
            }
            cell.setCellStyle(cell_style);
            // 备注
            cell = row.createCell(10);
            cell.setCellValue(si.getRemark());
            cell.setCellStyle(cell_style);
        }
        // 设置总计
        row = sheet.createRow(list.size() + 5);
        row.setHeight((short) 550);
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue("");
            cell.setCellStyle(cell_style);
        }
        CellRangeAddress cra2 = new CellRangeAddress(list.size() + 6, list.size() + 7, 0, title.length - 4);
        sheet.addMergedRegion(cra2); // 合并单元格
        cell = row.createCell(0);
        cell.setCellValue("总计");
        cell.setCellStyle(cell_style);
        cell = row.createCell(8);
        cell.setCellValue(quantityNum);
        cell.setCellStyle(cell_style);
        cell = row.createCell(9);
        cell.setCellValue(priceNum);
        cell.setCellStyle(cell_style);
        // 设置表未
        row = sheet.createRow(list.size() + 6);
        row.setHeight((short) 550);
        CellRangeAddress cra3 = new CellRangeAddress(list.size() + 7, list.size() + 8, 0, title.length - 1);
        sheet.addMergedRegion(cra3); // 合并单元格
        cell = row.createCell(0);
        cell.setCellValue("经办人：    " + sheetInfo.getSendOperatorName() + "              审核人：    " + sheetInfo.getSendVerifyName());
        HSSFFont font2 = wb.createFont();
        HSSFCellStyle style2 = wb.createCellStyle();
        font2.setFontName("宋体");//设置字体名称
        font2.setFontHeightInPoints((short) 11);//设置字号
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style2.setFont(font2);
        cell.setCellStyle(style2);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);//左对齐
        return wb;
    }

    /**
     * 导出sheetDetail
     *
     * @param sheetName
     * @param title
     * @param list
     * @param wb
     * @return
     */
    public static HSSFWorkbook exportStatisticsExcel(String sheetName, String[] title,
                                                     List<SheetDetailDTO> list, Depot sourceDepot, Depot object, HSSFWorkbook wb) {
        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        HSSFSheet sheet = wb.createSheet(sheetName);
        // 设置样式信息
        HSSFCellStyle title_style = ExcelUtil.cellStyle(wb, 1);
        HSSFCellStyle top_style = ExcelUtil.cellStyle(wb, 2);
        HSSFCellStyle cell_style = ExcelUtil.cellStyle(wb, 3);

        sheet.setColumnWidth(0, 256 * 5); // 序号
        sheet.setColumnWidth(1, 256 * 25); // 关键配件名称
        sheet.setColumnWidth(4, 256 * 20); // 生产厂家
        sheet.setColumnWidth(5, 256 * 25); // 关键配件出厂编码
        sheet.setColumnWidth(6, 256 * 15); // 配件二维码
        sheet.setColumnWidth(7, 256 * 25); // 资产配属
        sheet.setColumnWidth(8, 256 * 15); // 数量
        sheet.setColumnWidth(9, 256 * 15); // 单价
        sheet.setColumnWidth(10, 256 * 20); // 备注
        // 设置标题
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) 750);
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, title.length - 1);
        sheet.addMergedRegion(cra); // 合并单元格
        HSSFCell cell;
        HSSFFont font = wb.createFont();
        HSSFCellStyle style = wb.createCellStyle();
        cell = row.createCell(0);
        cell.setCellValue(sheetName);
        HSSFFont font4 = wb.createFont();
        HSSFCellStyle style4 = wb.createCellStyle();
        font4.setFontName("宋体");//设置字体名称
        font4.setFontHeightInPoints((short) 14);//设置字号
        font4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style4.setFont(font4);
        cell.setCellStyle(style4);
        style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        // 设置表头
        row = sheet.createRow(1);
        row.setHeight((short) 550);
        cell = row.createCell(0);
        cell.setCellValue("发送单位：" + sourceDepot.getDepotName());
        HSSFFont font3 = wb.createFont();
        HSSFCellStyle style3 = wb.createCellStyle();
        font3.setFontName("宋体");//设置字体名称
        font3.setFontHeightInPoints((short) 11);//设置字号
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style3.setFont(font3);
        cell.setCellStyle(style3);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        cell = row.createCell(10);
        cell.setCellValue("接收单位：" + object.getDepotName());
        font.setFontName("宋体");//设置字体名称
        font.setFontHeightInPoints((short) 11);//设置字号
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style.setFont(font);
        cell.setCellStyle(style);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中

        row = sheet.createRow(2);
        row.setHeight((short) 550);
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(top_style);
        }
        SheetDetailDTO si = null;
        int quantityNum = 0;
        double priceNum = 0;
        for (int i = 0; i < list.size(); i++) {
            si = list.get(i);
            row = sheet.createRow(i + 5);
            row.setHeight((short) 550);
            si.setQuantity((short) 1);
            quantityNum += si.getQuantity();
            if (si.getUnitPrice() != null) {
                priceNum += si.getUnitPrice().doubleValue();
            }
            // 编号
            cell = row.createCell(0);
            cell.setCellValue(i + 1);
            cell.setCellStyle(cell_style);
            // 关键配件名称
            cell = row.createCell(1);
            cell.setCellValue(si.getDevicePartsName());
            cell.setCellStyle(cell_style);
            // 设备型号
            cell = row.createCell(2);
            cell.setCellValue(si.getDeviceModelName());
            cell.setCellStyle(cell_style);
            // 设备类型
            cell = row.createCell(3);
            cell.setCellValue(si.getDeviceTypeName());
            cell.setCellStyle(cell_style);
            // 生产厂家
            cell = row.createCell(4);
            cell.setCellValue(si.getSupplierName());
            cell.setCellStyle(cell_style);
            // 配件出厂编码
            cell = row.createCell(5);
            cell.setCellValue(si.getPartCode());
            cell.setCellStyle(cell_style);
            // 配件二维码
            cell = row.createCell(6);
            cell.setCellValue(si.getPartId());
            cell.setCellStyle(cell_style);
            // 资产配属
            cell = row.createCell(7);
            cell.setCellValue(si.getAssetAttributesName());
            cell.setCellStyle(cell_style);
            // 数量
            cell = row.createCell(8);
            cell.setCellValue(si.getQuantity());
            cell.setCellStyle(cell_style);
            // 新购或修复单价
            cell = row.createCell(9);
            if (si.getPurchaseOrRepairedPrice() != null) {
                cell.setCellValue(si.getPurchaseOrRepairedPrice().doubleValue());
            }
            cell.setCellStyle(cell_style);
            // 备注
            cell = row.createCell(10);
            cell.setCellValue(si.getRemark());
            cell.setCellStyle(cell_style);
        }
        // 设置总计
        row = sheet.createRow(list.size() + 5);
        row.setHeight((short) 550);
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue("");
            cell.setCellStyle(cell_style);
        }
        CellRangeAddress cra2 = new CellRangeAddress(list.size() + 6, list.size() + 7, 0, title.length - 4);
        sheet.addMergedRegion(cra2); // 合并单元格
        cell = row.createCell(0);
        cell.setCellValue("总计");
        cell.setCellStyle(cell_style);
        cell = row.createCell(8);
        cell.setCellValue(quantityNum);
        cell.setCellStyle(cell_style);
        cell = row.createCell(9);
        cell.setCellValue(priceNum);
        cell.setCellStyle(cell_style);
        return wb;
    }

    /**
     * 导出sheetDetail
     *
     * @param sheetName
     * @param title
     * @param list
     * @param wb
     * @return
     */
    public static HSSFWorkbook exportCheckExcel(String sheetName, String[] title,
                                                List<SheetDetailDTO> list, com.kthw.pmis.entiy.SheetInfo sheetInfo, HSSFWorkbook wb) {
        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        HSSFSheet sheet = wb.createSheet(sheetName);
        // 设置样式信息
        HSSFCellStyle title_style = ExcelUtil.cellStyle(wb, 1);
        HSSFCellStyle top_style = ExcelUtil.cellStyle(wb, 2);
        HSSFCellStyle cell_style = ExcelUtil.cellStyle(wb, 3);

        sheet.setColumnWidth(0, 256 * 5); // 序号
        sheet.setColumnWidth(1, 256 * 25); // 关键配件名称
        sheet.setColumnWidth(4, 256 * 20); // 生产厂家
        sheet.setColumnWidth(5, 256 * 25); // 关键配件出厂编码
        sheet.setColumnWidth(6, 256 * 15); // 配件二维码
        sheet.setColumnWidth(7, 256 * 25); // 资产配属
        sheet.setColumnWidth(8, 256 * 15); // 配件属性
        sheet.setColumnWidth(9, 256 * 15); // 故障现象
        sheet.setColumnWidth(10, 256 * 20); // 上级检测
        sheet.setColumnWidth(11, 256 * 20); // 检测计算机检测
        sheet.setColumnWidth(12, 256 * 20); // 更换元件情况
        sheet.setColumnWidth(13, 256 * 20); // 拷机开始时间
        sheet.setColumnWidth(14, 256 * 20); //  拷机结束时间
        sheet.setColumnWidth(15, 256 * 20); //  拷机检测情况
        sheet.setColumnWidth(16, 256 * 20); //检修价格
        sheet.setColumnWidth(17, 256 * 20); // 报废原因
        sheet.setColumnWidth(18, 256 * 20); // 检测结论
        // 设置标题
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) 750);
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, title.length - 1);
        sheet.addMergedRegion(cra); // 合并单元格
        HSSFCell cell;
        HSSFFont font = wb.createFont();
        HSSFCellStyle style = wb.createCellStyle();
        cell = row.createCell(0);
        cell.setCellValue(sheetName);
        HSSFFont font4 = wb.createFont();
        HSSFCellStyle style4 = wb.createCellStyle();
        font4.setFontName("宋体");//设置字体名称
        font4.setFontHeightInPoints((short) 14);//设置字号
        font4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style4.setFont(font4);
        cell.setCellStyle(style4);
        style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        // 设置表头
        row = sheet.createRow(1);
        row.setHeight((short) 600);
        cell = row.createCell(0);
        HSSFFont font6 = wb.createFont();
        HSSFCellStyle style6 = wb.createCellStyle();
        font6.setFontName("宋体");//设置字体名称
        font6.setFontHeightInPoints((short) 11);//设置字号
        font6.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style6.setFont(font6);
        cell.setCellStyle(style6);
        cell.setCellValue("单据编号：" + sheetInfo.getSheetId());
        row = sheet.createRow(2);
        row.setHeight((short) 600);
        row = sheet.createRow(3);
        row.setHeight((short) 550);
        cell = row.createCell(0);
        cell.setCellValue("源仓库：" + list.get(0).getSourceStoreHouseName());
        HSSFFont font3 = wb.createFont();
        HSSFCellStyle style3 = wb.createCellStyle();
        font3.setFontName("宋体");//设置字体名称
        font3.setFontHeightInPoints((short) 11);//设置字号
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style3.setFont(font3);
        cell.setCellStyle(style3);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        cell = row.createCell(6);
        cell.setCellValue("日期：");
        font.setFontName("宋体");//设置字体名称
        font.setFontHeightInPoints((short) 11);//设置字号
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style.setFont(font);
        cell.setCellStyle(style);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        cell = row.createCell(7);
        cell.setCellValue(new SimpleDateFormat("yyyy年MM月dd日").format(sheetInfo.getAddDate()));
        font.setFontName("宋体");//设置字体名称
        font.setFontHeightInPoints((short) 11);//设置字号
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style.setFont(font);
        cell.setCellStyle(style);
        CellRangeAddress cr = new CellRangeAddress(1, 1, 8, title.length - 1);
        sheet.addMergedRegion(cr); // 合并单元格
        cell = row.createCell(10);
        cell.setCellValue("目的仓库：" + list.get(0).getObjectStoreHouseName());
        font.setFontName("宋体");//设置字体名称
        font.setFontHeightInPoints((short) 11);//设置字号
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style.setFont(font);
        cell.setCellStyle(style);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中

        row = sheet.createRow(4);
        row.setHeight((short) 550);
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(top_style);
        }
        SheetDetailDTO si = null;
        int quantityNum = 0;
        for (int i = 0; i < list.size(); i++) {
            si = list.get(i);
            row = sheet.createRow(i + 5);
            row.setHeight((short) 550);
            si.setQuantity((short) 1);
            quantityNum += si.getQuantity();
            // 编号
            cell = row.createCell(0);
            cell.setCellValue(i + 1);
            cell.setCellStyle(cell_style);
            // 关键配件名称
            cell = row.createCell(1);
            cell.setCellValue(si.getDevicePartsName());
            cell.setCellStyle(cell_style);
            // 设备型号
            cell = row.createCell(2);
            cell.setCellValue(si.getDeviceModelName());
            cell.setCellStyle(cell_style);
            // 设备类型
            cell = row.createCell(3);
            cell.setCellValue(si.getDeviceTypeName());
            cell.setCellStyle(cell_style);
            // 生产厂家
            cell = row.createCell(4);
            cell.setCellValue(si.getSupplierName());
            cell.setCellStyle(cell_style);
            // 配件出厂编码
            cell = row.createCell(5);
            cell.setCellValue(si.getPartCode());
            cell.setCellStyle(cell_style);
            // 配件二维码
            cell = row.createCell(6);
            cell.setCellValue(si.getPartId());
            cell.setCellStyle(cell_style);
            // 资产配属
            cell = row.createCell(7);
            cell.setCellValue(si.getAssetAttributesName());
            cell.setCellStyle(cell_style);
            // 配件属性
            cell = row.createCell(8);
            if (si.getPartState() == null) {
                cell.setCellValue("-");
            } else if (si.getPartState() == 1) {
                cell.setCellValue("新购");
            } else if (si.getPartState() == 2) {
                cell.setCellValue("送修");
            } else if (si.getPartState() == 3) {
                cell.setCellValue("初始化在探测站");
            } else if (si.getPartState() == 4) {
                cell.setCellValue("初始化在备品库");
            } else if (si.getPartState() == 5) {
                cell.setCellValue("初始化在送修库");
            }
            cell.setCellStyle(cell_style);
            // 故障现象
            cell = row.createCell(9);
            cell.setCellValue(si.getFaultInfo());

            cell.setCellStyle(cell_style);
            // 上机预检
            cell = row.createCell(10);
            cell.setCellValue(si.getPrepareCheck());
            cell.setCellStyle(cell_style);
            //检测计算机检测
            cell = row.createCell(11);
            cell.setCellValue(si.getMachineCheck());
            cell.setCellStyle(cell_style);
            // 更换元件情况
            cell = row.createCell(12);
            cell.setCellValue(si.getReplaceComponentCheck());
            cell.setCellStyle(cell_style);
            // 拷机开始时间
            cell = row.createCell(13);
            if (si.getCopyMachineStartTime() != null) {
                DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
                String format = dft.format(si.getCopyMachineStartTime());
                cell.setCellValue(format);
            }
            cell.setCellStyle(cell_style);
            // 拷机结束时间
            cell = row.createCell(14);
            if (si.getCopyMachineEndTime() != null) {
                DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
                String format = dft.format(si.getCopyMachineEndTime());
                cell.setCellValue(format);
            }
            cell.setCellStyle(cell_style);
            // 拷机检测情况
            cell = row.createCell(15);
            cell.setCellValue(si.getCopyMachineCheck());
            cell.setCellStyle(cell_style);
            // 检修价格
            cell = row.createCell(16);
            if (si.getCheckedPrice() != null) {
                cell.setCellValue(si.getCheckedPrice().doubleValue());
            }
            // 报废原因
            cell = row.createCell(17);
                cell.setCellValue(si.getScrapReason());
            cell.setCellStyle(cell_style);
            // 检测结论
            cell = row.createCell(18);
            if(si.getRepaireState()==null) {
                cell.setCellValue("-");
            }else if(si.getRepaireState()==0){
                cell.setCellValue("不合格");
            }else if(si.getRepaireState()==1){
                cell.setCellValue("合格");
            }else if(si.getRepaireState()==2){
                cell.setCellValue("报废");
            }
            cell.setCellStyle(cell_style);
        }
        // 设置总计
        row = sheet.createRow(list.size() + 5);
        row.setHeight((short) 550);
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue("");
            cell.setCellStyle(cell_style);
        }
        CellRangeAddress cra2 = new CellRangeAddress(list.size() + 6, list.size() + 7, 0, title.length - 4);
        sheet.addMergedRegion(cra2); // 合并单元格
        cell = row.createCell(0);
        cell.setCellValue("总计");
        cell.setCellStyle(cell_style);
        cell = row.createCell(8);
        cell.setCellValue(quantityNum);
        cell.setCellStyle(cell_style);
        return wb;
    }
}
