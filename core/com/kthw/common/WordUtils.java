package com.kthw.common;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

//import com.lowagie.text.Cell;
//import com.lowagie.text.Document;
//import com.lowagie.text.DocumentException;
//import com.lowagie.text.Element;
//import com.lowagie.text.Font;
//import com.lowagie.text.HeaderFooter;
//import com.lowagie.text.Image;
//import com.lowagie.text.PageSize;
//import com.lowagie.text.Paragraph;
//import com.lowagie.text.Phrase;
//import com.lowagie.text.Rectangle;
//import com.lowagie.text.Table;
//import com.lowagie.text.pdf.BaseFont;
//import com.lowagie.text.pdf.PdfPCell;
//import com.lowagie.text.pdf.PdfPTable;
//import com.lowagie.text.pdf.PdfWriter;
//import com.lowagie.text.rtf.RtfWriter2;

public class WordUtils {
	// 确认界面的导出word
	// public static void ExportToWord(String file, String img_path)
	// throws DocumentException, IOException {
	// Document document = new Document(PageSize.A4);
	// // 建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中
	// RtfWriter2.getInstance(document, new FileOutputStream(file));
	// document.open();
	// // 设置中文字体
	// BaseFont bfChinese = BaseFont.createFont(
	// "C:/WINDOWS/Fonts/SIMFANG.TTF", BaseFont.IDENTITY_H,
	// BaseFont.NOT_EMBEDDED);
	// // 标题字体风格
	// Font titleFont = new Font(bfChinese, 16, Font.BOLD);
	// // 正文字体风格
	// Font contextFont = new Font(bfChinese, 14, Font.NORMAL);
	// Paragraph title = new Paragraph("TVDS故障通知反馈表");
	// // 设置标题格式对齐方式
	// title.setAlignment(Element.ALIGN_CENTER);
	// title.setFont(titleFont);
	// document.add(title);
	// // 添加table
	// Table aTable = new Table(7);
	// int width[] = { 20, 20, 10, 10, 20, 10, 10 };
	// aTable.setWidths(width);// 设置每列所占比例
	// aTable.setWidth(100); // 占页面宽度 90%
	//
	// aTable.setAlignment(Element.ALIGN_CENTER);// 居中显示
	// aTable.setAlignment(Element.ALIGN_MIDDLE);// 纵向居中显示
	// aTable.setAutoFillEmptyCells(true); // 自动填满
	// aTable.setBorderWidth(1); // 边框宽度
	// // aTable.setBorderColor(new Color(0, 125, 255)); //边框颜色
	// aTable.setPadding(0);// 衬距，看效果就知道什么意思了
	// aTable.setSpacing(0);// 即单元格之间的间距
	// aTable.setBorder(1);// 边框
	//
	// // 添加第一行的7列
	// aTable.addCell(CellAlignCenter("反馈日期时间"));
	// aTable.addCell(CellAlignCenter("处理时间"));
	// aTable.addCell(CellAlignCenter("反馈车次"));
	// aTable.addCell(CellAlignCenter("反馈辆序车号"));
	// aTable.addCell(CellAlignCenter("处理班组"));
	// aTable.addCell(CellAlignCenter("处理人"));
	// aTable.addCell(CellAlignCenter("确认人"));
	// //重新添加第二行数据为空
	// for(int i =0 ;i<7;i++){
	// aTable.addCell(CellAlignCenter("\n\n"));
	// }
	// // 添加处理情况
	// aTable.addCell(CellAlignCenter("\n处理情况\n"));
	// Cell cell3 = new Cell();
	// cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);
	// cell3.setColspan(6);
	// cell3.add(new Paragraph("\n\n"));
	// aTable.addCell(cell3);
	// // 添加故障图片
	// // 添加图片
	// Image img = Image.getInstance(img_path);
	// // Image img = Image.getInstance("D:/1.jpg");
	// img.setAbsolutePosition(0, 0);
	// img.setAlignment(Image.RIGHT);// 设置图片显示位置
	// img.scaleAbsolute(12, 35);// 直接设定显示尺寸
	// img.scalePercent(35);// 表示显示的大小为原尺寸的50%
	// // img.scalePercent(25, 12);//图像高宽的显示比例
	// // img.setRotation(30);//图像旋转一定角度
	// //
	// Cell cellImg = new Cell();
	// cellImg.setColspan(7);
	// Font fontSmall = new Font(bfChinese, 2, Font.NORMAL, Color.BLACK);
	// cellImg.setHorizontalAlignment(Element.ALIGN_CENTER);
	// Paragraph p1 = new Paragraph("   故障图片：");
	// p1.setAlignment(Element.ALIGN_LEFT);
	// cellImg.add(p1);
	// // cellImg.add(new Paragraph("\n",fontSmall));
	// // cellImg.setWidth(700);
	// cellImg.add(img);
	// aTable.addCell(cellImg);
	// document.add(aTable);
	// document.close();
	// }
//	public static void ExportToWord(String file, Map map) throws DocumentException, IOException {
//		Document document = new Document(PageSize.A4);
//		// 建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中
//		RtfWriter2.getInstance(document, new FileOutputStream(file));
//		document.open();
//		// 设置中文字体
//		BaseFont bfChinese = BaseFont.createFont("C:/WINDOWS/Fonts/SIMFANG.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//		// 标题字体风格
//		Font titleFont = new Font(bfChinese, 16, Font.BOLD);
//		// 正文字体风格
//		Font contextFont = new Font(bfChinese, 14, Font.NORMAL);
//		Paragraph title = new Paragraph("出入库操作二维码信息");
//		// 设置标题格式对齐方式
//		title.setAlignment(Element.ALIGN_CENTER);
//		title.setFont(titleFont);
//		document.add(title);
//		// 添加table
//		Table aTable = new Table(9);
//		int width[] = { 10, 20, 10, 10, 10, 10, 10, 10, 10 };
//		aTable.setWidths(width);// 设置每列所占比例
//		aTable.setWidth(100); // 占页面宽度 90%
//
//		aTable.setAlignment(Element.ALIGN_CENTER);// 居中显示
//		aTable.setAlignment(Element.ALIGN_MIDDLE);// 纵向居中显示
//		aTable.setAutoFillEmptyCells(true); // 自动填满
//		aTable.setBorderWidth(1); // 边框宽度
//		// aTable.setBorderColor(new Color(0, 125, 255)); //边框颜色
//		aTable.setPadding(0);// 衬距，看效果就知道什么意思了
//		aTable.setSpacing(0);// 即单元格之间的间距
//		aTable.setBorder(1);// 边框
//
//		// 添加第一行的9列
//		aTable.addCell(CellAlignCenter("存取标志"));
//		aTable.addCell(CellAlignCenter("货物编码"));
//		aTable.addCell(CellAlignCenter("数量"));
//		aTable.addCell(CellAlignCenter("实际数量"));
//		aTable.addCell(CellAlignCenter("完成时间"));
//		aTable.addCell(CellAlignCenter("货柜号"));
//		aTable.addCell(CellAlignCenter("托盘号"));
//		aTable.addCell(CellAlignCenter("财务状态"));
//		aTable.addCell(CellAlignCenter("入库类型"));
//		for (int i = 0; i < 9; i++) {
//			aTable.addCell(CellAlignCenter("\n\n"));
//		}
//		// 添加处理情况
//		aTable.addCell(CellAlignCenter("\n处理情况\n"));
//		Cell cell3 = new Cell();
//		cell3.setHorizontalAlignment(Element.ALIGN_MIDDLE);
//		cell3.setColspan(8);
//		cell3.add(new Paragraph("\n\n"));
//		aTable.addCell(cell3);
//		// 添加故障图片
//		// 添加图片
//		Image img = Image.getInstance(map.get("image_path").toString());
//		img.setAbsolutePosition(0, 0);
//		img.setAlignment(Image.RIGHT);// 设置图片显示位置
//		img.scaleAbsolute(12, 35);// 直接设定显示尺寸
//		img.scalePercent(35);// 表示显示的大小为原尺寸的50%
//		// img.scalePercent(25, 12);//图像高宽的显示比例
//		// img.setRotation(30);//图像旋转一定角度
//		//
//		Cell cellImg = new Cell();
//		cellImg.setColspan(9);
//		Font fontSmall = new Font(bfChinese, 2, Font.NORMAL, Color.BLACK);
//		cellImg.setHorizontalAlignment(Element.ALIGN_CENTER);
//		// chengliang 2012.12.28
////		String trainId = map.get("train_id").toString();//"车次";
////		String carSeq = map.get("vehicle_order").toString();//"辆序";
////		String trainNumber = map.get("train_number").toString();//"车号";
////		String faultDes = map.get("fault_desc").toString();//"故障描述信息";
////		String detector = map.get("detector").toString();
////		String detected_time = map.get("detected_time").toString();
////		String site_name = map.get("site_name").toString();
////		//新加一个table
////		aTable.addCell(CellAlignCenter("探测站"));
////		aTable.addCell(CellAlignCenter(site_name));
////		
////		aTable.addCell(CellAlignCenterWithSpan("发现时间",2));
////		aTable.addCell(CellAlignCenterWithSpan(detected_time,3));
////		aTable.addCell(CellAlignCenter("车次"));
////		aTable.addCell(CellAlignCenter(trainId));
////		aTable.addCell(CellAlignCenterWithSpan("车号",2));
////		aTable.addCell(CellAlignCenterWithSpan(trainNumber,3));
////		aTable.addCell(CellAlignCenter("发现人"));
////		aTable.addCell(CellAlignCenter(detector));
////		aTable.addCell(CellAlignCenterWithSpan("辆序",2));
////		aTable.addCell(CellAlignCenterWithSpan(carSeq,3));
////		aTable.addCell(CellAlignCenter("故障描述"));
////		aTable.addCell(CellAlignCenterWithSpan(faultDes,6));
//		//end
////		Paragraph p1 = new Paragraph("   故障信息：");
////		p1.setAlignment(Element.ALIGN_LEFT);
////		cellImg.add(p1);
//		// cellImg.add(new Paragraph("\n",fontSmall));
//		// cellImg.setWidth(700);
//		cellImg.add(img);
//		aTable.addCell(cellImg);
//		document.add(aTable);
//		document.close();
//	}
	
	 public static void main(String[] args) {
		 Map<String, Object> map = new HashMap<String,Object>();
		 map.put("image_path","D://QRCODE//201512046.png" ); 
         mergePdfFiles("ww", map);
 } /*
          * *   Merge pdf files  * * @param files   Array of files to be merged  (  Absolute path, such as  { "e:\\1.pdf", "e:\\2.pdf" ,
          * "e:\\3.pdf"}) * @param newfile
          *   Documents produced after the merger the new absolute path such as  e:\\temp.pdf, Please delete your files no longer in use after the use of your   * @return boolean
          *   Produce successful return true, otherwise  false
          */

	 public static boolean mergePdfFiles(String file, Map map) {
	         boolean retValue = false;
//	         Document document = null;
//	         try {
//	                 document = new Document();
//	                 PdfWriter.getInstance(document, new FileOutputStream(file));
//	                 document.open();
//	                 document.add(new Paragraph("Hello World"));
//	                 Image jpg = Image.getInstance(map.get("image_path").toString());
//	                 jpg.setAlignment(Image.ALIGN_LEFT);
//	                 document.add(jpg);
//	                 retValue = true;
//	         } catch (Exception e) {
//	                 e.printStackTrace();
//	         } finally {
//	                 document.close();
//	         }
	         return retValue;
	 }
	
	
	// 将表格数据居中对齐，并返回该单元格对象
//	private static Cell CellAlignCenter(String str) {
//		Cell c1 = new Cell(str);
//		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
//		return c1;
//	}
//	
//	private static Cell CellAlignCenterWithSpan(String str,int span) {
//		Cell c1 = new Cell(str);
//		c1.setColspan(span);
//		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
//		return c1;
//	}

}
