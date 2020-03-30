package com.kthw.common;

import java.math.BigDecimal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import com.kthw.pmis.entiy.StockInfo1;
import com.kthw.pmis.entiy.dto.StockInfoDTO;
import com.kthw.pmis.entiy.ext.SheetInfoExt;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import com.kthw.pmis.entiy.SheetInfo;
import com.kthw.pmis.entiy.dto.SheetDetailDTO;

public class ExcelUtil {


    /**
     * 导出配件详细信息Excel文件
     *
     * @param sheetName
     * @param title
     * @param list
     * @param wb
     * @return
     */
    public static HSSFWorkbook exportStockInfosAsExcel(String sheetName, String[] title, List<StockInfoDTO> list,
                                                       HSSFWorkbook wb) {
        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        HSSFSheet sheet = wb.createSheet(sheetName);
        // 设置样式信息
        HSSFCellStyle title_style = cellStyle(wb, 1);
        HSSFCellStyle top_style = cellStyle(wb, 2);
        HSSFCellStyle cell_style = cellStyle(wb, 3);

        sheet.setColumnWidth(0, 256 * 5); // 序号
        sheet.setColumnWidth(1, 256 * 15); // 配件编号
        sheet.setColumnWidth(2, 256 * 25); // 配件条形码编号
        sheet.setColumnWidth(3, 256 * 25); // 配件名称
        sheet.setColumnWidth(4, 256 * 18); // 规格型号
        sheet.setColumnWidth(5, 256 * 18); // 数量
        sheet.setColumnWidth(6, 256 * 18); // 单价
        sheet.setColumnWidth(7, 256 * 18); // 存放位置
        sheet.setColumnWidth(8, 256 * 18); // 添加时间
        sheet.setColumnWidth(9, 256 * 20); // 备注
        // 设置标题
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) 750);
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, title.length - 1);
        sheet.addMergedRegion(cra); // 合并单元格
        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue(sheetName);
        cell.setCellStyle(title_style);
        // 设置表头
        row = sheet.createRow(1);
        row.setHeight((short) 550);
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(top_style);
        }
        row = sheet.createRow(2);
        row.setHeight((short) 550);
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(top_style);
        }
        StockInfoDTO si = null;
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < list.size(); i++) {
            si = list.get(i);
            row = sheet.createRow(i + 2);
            row.setHeight((short) 550);
            // 序号
            cell = row.createCell(0);
            cell.setCellValue(i + 1);
            cell.setCellStyle(cell_style);
            // 配件编号
            cell = row.createCell(1);
            cell.setCellValue(si.getPartIdSeq());
            cell.setCellStyle(cell_style);
            // 配件条形码编号
            cell = row.createCell(2);
            cell.setCellValue(si.getFactoryPartsCode());
            cell.setCellStyle(cell_style);
            // 配件名称
            cell = row.createCell(3);
            cell.setCellValue(si.getDevicePartsName());
            cell.setCellStyle(cell_style);
            // 规格型号
            cell = row.createCell(4);
            cell.setCellValue(si.getDeviceModelName());
            cell.setCellStyle(cell_style);
            // 数量
            cell = row.createCell(5);
            cell.setCellValue(1);
            cell.setCellStyle(cell_style);
            // 单价
            cell = row.createCell(6);
            if (si.getPurchasePrice() != null)
                cell.setCellValue(si.getPurchasePrice().doubleValue());
            cell.setCellStyle(cell_style);
            // 存放位置
            cell = row.createCell(7);
            cell.setCellValue(si.getStorehouseName());
            cell.setCellStyle(cell_style);
            // 添加时间
            cell = row.createCell(8);
            if (si.getPurchaseDate() != null) {
                String format = dft.format(si.getPurchaseDate());
                cell.setCellValue(format);
            }
            cell.setCellStyle(cell_style);
            // 备注
            cell = row.createCell(9);
            cell.setCellValue(si.getRemark());
            cell.setCellStyle(cell_style);
        }
        return wb;
    }

    /**
     * 导出采购入库信息Excel文件
     *
     * @param sheetName
     * @param title
     * @param list
     * @param wb
     * @return
     */
    public static HSSFWorkbook exportSheetInfoAsExcel(String sheetName, String[] title, List<SheetDetailDTO> list, SheetInfo sheetInfo,
                                                      HSSFWorkbook wb) {
        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        HSSFSheet sheet = wb.createSheet(sheetName);
        // 设置样式信息
        HSSFCellStyle title_style = cellStyle(wb, 1);
        HSSFCellStyle top_style = cellStyle(wb, 2);
        HSSFCellStyle cell_style = cellStyle(wb, 3);

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
        cell.setCellValue("购置厂家：" + sheetInfo.getSupplierName());
        HSSFFont font3 = wb.createFont();
        HSSFCellStyle style3 = wb.createCellStyle();
        font3.setFontName("宋体");//设置字体名称
        font3.setFontHeightInPoints((short) 11);//设置字号
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style3.setFont(font3);
        cell.setCellStyle(style3);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        cell = row.createCell(4);
        cell.setCellValue("日期：");
        font.setFontName("宋体");//设置字体名称
        font.setFontHeightInPoints((short) 11);//设置字号
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style.setFont(font);
        cell.setCellStyle(style);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        cell = row.createCell(5);
        cell.setCellValue(new SimpleDateFormat("yyyy年MM月dd日").format(sheetInfo.getAddDate()));
        font.setFontName("宋体");//设置字体名称
        font.setFontHeightInPoints((short) 11);//设置字号
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style.setFont(font);
        cell.setCellStyle(style);
        CellRangeAddress cr = new CellRangeAddress(1, 1, 8, title.length - 1);
        sheet.addMergedRegion(cr); // 合并单元格
        cell = row.createCell(8);
        cell.setCellValue("接收单位：广铁机辆检测所");
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
            row = sheet.createRow(i + 3);
            row.setHeight((short) 550);
            quantityNum += 1;
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
            cell.setCellValue(1);
            cell.setCellStyle(cell_style);
            // 单价
            cell = row.createCell(9);
            if (si.getUnitPrice() != null) {
                cell.setCellValue(si.getUnitPrice().doubleValue());
            }
            cell.setCellStyle(cell_style);
            // 备注
            cell = row.createCell(10);
            cell.setCellValue(si.getRemark());
            cell.setCellStyle(cell_style);
        }
        // 设置总计
        row = sheet.createRow(list.size() + 3);
        row.setHeight((short) 550);
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue("");
            cell.setCellStyle(cell_style);
        }
        CellRangeAddress cra2 = new CellRangeAddress(list.size() + 4, list.size() + 5, 0, title.length - 4);
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
        row = sheet.createRow(list.size() + 4);
        row.setHeight((short) 550);
        CellRangeAddress cra3 = new CellRangeAddress(list.size() + 4, list.size() + 4, 0, title.length - 1);
        sheet.addMergedRegion(cra3); // 合并单元格
        cell = row.createCell(0);
        cell.setCellValue("经办人：    " + sheetInfo.getReceiptVerifyName() + "              审核人：    " + sheetInfo.getReceiptVerifyName());
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

    public static HSSFCellStyle cellStyle(HSSFWorkbook wb, int type) {
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平


        style.setWrapText(true);

        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 6);

        if (type == 1) {
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
            font.setColor(HSSFColor.BLACK.index);
            font.setFontHeight((short) 400);
            style.setFont(font);
        } else if (type == 2) {
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
            font.setColor(HSSFColor.BLACK.index);
            font.setFontHeight((short) 200);
            style.setFont(font);
        } else if (type == 3) {
            font.setColor(HSSFColor.BLACK.index);
            font.setFontHeight((short) 200);
            style.setFont(font);
        }
        return style;
    }

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
    public static HSSFWorkbook exportPreparePurchaseSheetInfoAsExcel(String sheetName, String[] title,
                                                                     List<SheetDetailDTO> list, com.kthw.pmis.entiy.SheetInfo sheetInfo, HSSFWorkbook wb) {
        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        HSSFSheet sheet = wb.createSheet(sheetName);
        // 设置样式信息
        HSSFCellStyle title_style = cellStyle(wb, 1);
        HSSFCellStyle top_style = cellStyle(wb, 2);
        HSSFCellStyle cell_style = cellStyle(wb, 3);

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
        cell = row.createCell(0);
        HSSFFont font1 = wb.createFont();
        HSSFCellStyle style1 = wb.createCellStyle();
        font1.setFontName("宋体");//设置字体名称
        font1.setFontHeightInPoints((short) 11);//设置字号
        font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style1.setFont(font1);
        cell.setCellStyle(style1);
        cell.setCellValue("物流单号：" + sheetInfo.getTrackingNumber());
        row = sheet.createRow(3);
        row.setHeight((short) 550);
        cell = row.createCell(0);
        cell.setCellValue("购置厂家：" + sheetInfo.getSupplierName());
        HSSFFont font3 = wb.createFont();
        HSSFCellStyle style3 = wb.createCellStyle();
        font3.setFontName("宋体");//设置字体名称
        font3.setFontHeightInPoints((short) 11);//设置字号
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style3.setFont(font3);
        cell.setCellStyle(style3);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        cell = row.createCell(8);
        cell.setCellValue("日期：");
        font.setFontName("宋体");//设置字体名称
        font.setFontHeightInPoints((short) 11);//设置字号
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style.setFont(font);
        cell.setCellStyle(style);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        cell = row.createCell(12);
        cell.setCellValue(new SimpleDateFormat("yyyy年MM月dd日").format(sheetInfo.getAddDate()));
        font.setFontName("宋体");//设置字体名称
        font.setFontHeightInPoints((short) 11);//设置字号
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style.setFont(font);
        cell.setCellStyle(style);
        CellRangeAddress cr = new CellRangeAddress(1, 1, 8, title.length - 1);
        sheet.addMergedRegion(cr); // 合并单元格
        cell = row.createCell(8);
        cell.setCellValue("接收单位：广铁机辆检测所");
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
            if (si.getUnitPrice() != null) {
                cell.setCellValue(si.getUnitPrice().doubleValue());
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
     * 导出返修单据
     *
     * @param sheetName
     * @param title
     * @param list
     * @param sheetInfo
     * @param wb
     * @return
     */
    public static HSSFWorkbook exportDeoptToTest(String sheetName, String[] title,
                                                 List<SheetDetailDTO> list, SheetInfoExt sheetInfo, HSSFWorkbook wb) {
        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        HSSFSheet sheet = wb.createSheet(sheetName);
        // 设置样式信息
        HSSFCellStyle title_style = cellStyle(wb, 1);
        HSSFCellStyle top_style = cellStyle(wb, 2);
        HSSFCellStyle cell_style = cellStyle(wb, 3);

        sheet.setColumnWidth(0, 256 * 5); // 序号
        sheet.setColumnWidth(1, 256 * 25); // 关键配件名称
        sheet.setColumnWidth(2, 256 * 20); // 设备型号
        sheet.setColumnWidth(3, 256 * 25); // 设备名称
        sheet.setColumnWidth(4, 256 * 15); // 生产厂家
        sheet.setColumnWidth(5, 256 * 25); // 配件出厂编码
        sheet.setColumnWidth(6, 256 * 15); // 配件二维码
        sheet.setColumnWidth(7, 256 * 25); // 资产配属
        sheet.setColumnWidth(8, 256 * 15); // 使用探测站
        sheet.setColumnWidth(9, 256 * 15); // 故障发生日期
        sheet.setColumnWidth(10, 256 * 15); // 配件故障现象
        sheet.setColumnWidth(11, 256 * 15); // 是否质保期
        sheet.setColumnWidth(12, 256 * 20); // 备注
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
        cell = row.createCell(0);
        HSSFFont font1 = wb.createFont();
        HSSFCellStyle style1 = wb.createCellStyle();
        font1.setFontName("宋体");//设置字体名称
        font1.setFontHeightInPoints((short) 11);//设置字号
        font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style1.setFont(font1);
        cell.setCellStyle(style1);
        cell.setCellValue("物流单号：" + sheetInfo.getTrackingNumber());
        row = sheet.createRow(3);
        row.setHeight((short) 550);
        cell = row.createCell(0);
        cell.setCellValue("配送单位：" + sheetInfo.getSourceStorehouseName());
        HSSFFont font3 = wb.createFont();
        HSSFCellStyle style3 = wb.createCellStyle();
        font3.setFontName("宋体");//设置字体名称
        font3.setFontHeightInPoints((short) 11);//设置字号
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style3.setFont(font3);
        cell.setCellStyle(style3);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        cell = row.createCell(4);
        cell.setCellValue("日期：");
        font.setFontName("宋体");//设置字体名称
        font.setFontHeightInPoints((short) 11);//设置字号
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style.setFont(font);
        cell.setCellStyle(style);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        cell = row.createCell(5);
        cell.setCellValue(new SimpleDateFormat("yyyy年MM月dd日").format(sheetInfo.getAddDate()));
        font.setFontName("宋体");//设置字体名称
        font.setFontHeightInPoints((short) 11);//设置字号
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style.setFont(font);
        cell.setCellStyle(style);
        CellRangeAddress cr = new CellRangeAddress(1, 1, 8, title.length - 1);
        sheet.addMergedRegion(cr); // 合并单元格
        cell = row.createCell(8);
        cell.setCellValue("接收单位：" + sheetInfo.getObjectStorehouseName());
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

        for (int i = 0; i < list.size(); i++) {
            si = list.get(i);
            row = sheet.createRow(i + 5);
            row.setHeight((short) 550);

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
            //生产厂家
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
            if (si.getAssetAttributesName() != null) {
                cell.setCellValue(si.getAssetAttributesName());
            }
            cell.setCellStyle(cell_style);
            // 使用探测站
            cell = row.createCell(8);
            if (si.getUsedStationName() != null) {
                cell.setCellValue(si.getUsedStationName());
            }
            cell.setCellStyle(cell_style);
            // 故障发生日期
            cell = row.createCell(9);
            if (si.getFaultDate() != null) {
                DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
                String format = dft.format(si.getFaultDate());
                cell.setCellValue(format);
            }
            cell.setCellStyle(cell_style);
            // 配件故障现象
            cell = row.createCell(10);
            if (si.getFaultInfo() != null) {
                cell.setCellValue(si.getFaultInfo());
            }
            cell.setCellStyle(cell_style);
            // 是否质保期
            cell = row.createCell(11);
            if (si.getWarranty() != null && si.getWarranty() == 0) {
                cell.setCellValue("否");
            } else if (si.getWarranty() != null && si.getWarranty() == 1) {
                cell.setCellValue("是");
            }
            cell.setCellStyle(cell_style);
            // 备注
            cell = row.createCell(12);
            cell.setCellValue(si.getRemark());
            cell.setCellStyle(cell_style);
        }

        // 设置表未
        //发送方
        row = sheet.createRow(list.size() + 5);
        row.setHeight((short) 550);
        CellRangeAddress cra3 = new CellRangeAddress(list.size() + 6, list.size() + 7, 0, title.length - 1);
        sheet.addMergedRegion(cra3); // 合并单元格
        cell = row.createCell(0);
        cell.setCellValue("发送方经办人：    " + sheetInfo.getSendOperatorName() + "              发送方审核人：    " + sheetInfo.getSendVerifyName());
        HSSFFont font2 = wb.createFont();
        HSSFCellStyle style2 = wb.createCellStyle();
        font2.setFontName("宋体");//设置字体名称
        font2.setFontHeightInPoints((short) 11);//设置字号
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style2.setFont(font2);
        cell.setCellStyle(style2);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);//左对齐
//接收方
        row = sheet.createRow(list.size() + 6);
        row.setHeight((short) 550);
        CellRangeAddress cra4 = new CellRangeAddress(list.size() + 6, list.size() + 7, 0, title.length - 1);
        sheet.addMergedRegion(cra4); // 合并单元格
        cell = row.createCell(0);
        cell.setCellValue("接收方经办人：    " + sheetInfo.getReceiptOperatorName() + "              接收方审核人：    " + sheetInfo.getReceiptVerifyName());
        HSSFFont font5 = wb.createFont();
        HSSFCellStyle style5 = wb.createCellStyle();
        font2.setFontName("宋体");//设置字体名称
        font2.setFontHeightInPoints((short) 11);//设置字号
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style2.setFont(font5);
        cell.setCellStyle(style5);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);//左对齐
        return wb;
    }

    /**
     * 导出配送单据
     *
     * @param sheetName
     * @param title
     * @param list
     * @param sheetInfo
     * @param wb
     * @return
     */
    public static HSSFWorkbook exportTestToDeot(String sheetName, String[] title,
                                                List<SheetDetailDTO> list, SheetInfoExt sheetInfo, HSSFWorkbook wb) {
        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        HSSFSheet sheet = wb.createSheet(sheetName);
        // 设置样式信息
        HSSFCellStyle title_style = cellStyle(wb, 1);
        HSSFCellStyle top_style = cellStyle(wb, 2);
        HSSFCellStyle cell_style = cellStyle(wb, 3);

        sheet.setColumnWidth(0, 256 * 5); // 序号
        sheet.setColumnWidth(1, 256 * 25); // 关键配件名称
        sheet.setColumnWidth(2, 256 * 20); // 设备型号
        sheet.setColumnWidth(3, 256 * 25); // 设备名称
        sheet.setColumnWidth(4, 256 * 15); // 生产厂家
        sheet.setColumnWidth(5, 256 * 25); // 配件出厂编码
        sheet.setColumnWidth(6, 256 * 15); // 配件二维码
        sheet.setColumnWidth(7, 256 * 25); // 资产配属
        sheet.setColumnWidth(8, 256 * 15); // 配件属性
        sheet.setColumnWidth(9, 256 * 15); // 是否质保期
        sheet.setColumnWidth(10, 256 * 15); // 数量
        sheet.setColumnWidth(11, 256 * 15); // 新购或修复单价
        sheet.setColumnWidth(12, 256 * 15); // 新购或修复日期
        sheet.setColumnWidth(13, 256 * 15); // 检修备注
        sheet.setColumnWidth(14, 256 * 20); // 备注
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
        cell = row.createCell(0);
        HSSFFont font1 = wb.createFont();
        HSSFCellStyle style1 = wb.createCellStyle();
        font1.setFontName("宋体");//设置字体名称
        font1.setFontHeightInPoints((short) 11);//设置字号
        font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style1.setFont(font1);
        cell.setCellStyle(style1);
        cell.setCellValue("物流单号：" + sheetInfo.getTrackingNumber());
        row = sheet.createRow(3);
        row.setHeight((short) 550);
        cell = row.createCell(0);
        cell.setCellValue("配送单位：" + sheetInfo.getSourceStorehouseName());
        HSSFFont font3 = wb.createFont();
        HSSFCellStyle style3 = wb.createCellStyle();
        font3.setFontName("宋体");//设置字体名称
        font3.setFontHeightInPoints((short) 11);//设置字号
        font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style3.setFont(font3);
        cell.setCellStyle(style3);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        cell = row.createCell(4);
        cell.setCellValue("日期：");
        font.setFontName("宋体");//设置字体名称
        font.setFontHeightInPoints((short) 11);//设置字号
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style.setFont(font);
        cell.setCellStyle(style);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        cell = row.createCell(5);
        cell.setCellValue(new SimpleDateFormat("yyyy年MM月dd日").format(sheetInfo.getAddDate()));
        font.setFontName("宋体");//设置字体名称
        font.setFontHeightInPoints((short) 11);//设置字号
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
        style.setFont(font);
        cell.setCellStyle(style);
        CellRangeAddress cr = new CellRangeAddress(1, 1, 8, title.length - 1);
        sheet.addMergedRegion(cr); // 合并单元格
        cell = row.createCell(8);
        cell.setCellValue("接收单位：" + sheetInfo.getObjectStorehouseName());
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
        Integer priceNum = 0;
        for (int i = 0; i < list.size(); i++) {
            si = list.get(i);
            row = sheet.createRow(i + 5);
            row.setHeight((short) 550);
            si.setQuantity((short) 1);
            quantityNum += si.getQuantity();
            if (si.getPurchaseOrRepairedPrice() != null) {
                priceNum += si.getPurchaseOrRepairedPrice();
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
            //生产厂家
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
            // 是否质保期
            cell = row.createCell(9);
            if (si.getWarranty() == null) {
                cell.setCellValue("-");
            } else if (si.getWarranty() == 0) {
                cell.setCellValue("否");
            } else if (si.getWarranty() == 1) {
                cell.setCellValue("是");
            }
                cell.setCellStyle(cell_style);
                // 数量
                cell = row.createCell(10);
                cell.setCellValue(si.getQuantity());
                cell.setCellStyle(cell_style);
                // 新购或修复单价
                cell = row.createCell(11);
                if (si.getPurchaseOrRepairedPrice() != null) {
                    cell.setCellValue(si.getPurchaseOrRepairedPrice());
                }
                cell.setCellStyle(cell_style);
                // 新购或修复日期
                cell = row.createCell(12);
                if (si.getPurchaseOrRepairedDate() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String format = sdf.format(si.getPurchaseOrRepairedDate());
                    cell.setCellValue(format);
                }
                cell.setCellStyle(cell_style);
                // 检修备注
                cell = row.createCell(13);
                cell.setCellValue(si.getRemark());
                cell.setCellStyle(cell_style);
                // 备注
                cell = row.createCell(14);
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
            cell = row.createCell(10);
            cell.setCellValue(quantityNum);
            cell.setCellStyle(cell_style);
            cell = row.createCell(11);
            cell.setCellValue(priceNum);
            cell.setCellStyle(cell_style);
            // 设置表未
            row = sheet.createRow(list.size() + 6);
            row.setHeight((short) 550);
            CellRangeAddress cra3 = new CellRangeAddress(list.size() + 7, list.size() + 8, 0, title.length - 1);
            sheet.addMergedRegion(cra3); // 合并单元格
            cell = row.createCell(0);
            cell.setCellValue("发送方经办人：    " + sheetInfo.getSendOperatorName() + "              发送方审核人：    " + sheetInfo.getSendVerifyName());
            HSSFFont font2 = wb.createFont();
            HSSFCellStyle style2 = wb.createCellStyle();
            font2.setFontName("宋体");//设置字体名称
            font2.setFontHeightInPoints((short) 11);//设置字号
            font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
            style2.setFont(font2);
            cell.setCellStyle(style2);
            style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
            style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);//左对齐
            //接收方
            row = sheet.createRow(list.size() + 7);
            row.setHeight((short) 550);
            CellRangeAddress cra4 = new CellRangeAddress(list.size() + 7, list.size() + 8, 0, title.length - 1);
            sheet.addMergedRegion(cra4); // 合并单元格
            cell = row.createCell(0);
            cell.setCellValue("接收方经办人：    " + sheetInfo.getReceiptOperatorName() + "              接收方审核人：    " + sheetInfo.getReceiptVerifyName());
            HSSFFont font5 = wb.createFont();
            HSSFCellStyle style5 = wb.createCellStyle();
            font2.setFontName("宋体");//设置字体名称
            font2.setFontHeightInPoints((short) 11);//设置字号
            font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体增粗
            style2.setFont(font5);
            cell.setCellStyle(style5);
            style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
            style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);//左对齐
            return wb;
        }
    }