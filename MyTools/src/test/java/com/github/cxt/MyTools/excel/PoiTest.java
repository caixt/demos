package com.github.cxt.MyTools.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.record.cf.FontFormatting;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


//http://www.tuicool.com/articles/emqaEf6
public class PoiTest {
	private HSSFSheet sheet = null;
	private HSSFWorkbook workbook = null;
	
	@Before
	public void before(){
		workbook = new HSSFWorkbook();//创建Excel文件(Workbook)
		sheet = workbook.createSheet("Test");//创建工作表(Sheet)
	}
	
	@After
	public void after() throws IOException{
		String filePath="sample.xls";//文件路径
		FileOutputStream out = new FileOutputStream(filePath);
		workbook.write(out);//保存Excel文件
		out.close();//关闭文件流
		System.out.println("OK!");
	}

	
	@Test
	public void test1() {
		HSSFRow row = sheet.createRow(0);// 创建行,从0开始
		HSSFCell cell = row.createCell(0);// 创建行的单元格,也是从0开始
		cell.setCellValue("caixt");// 设置单元格内容
		row.createCell(1).setCellValue(false);// 设置单元格内容,重载
		row.createCell(2).setCellValue(new Date());// 设置单元格内容,重载
		row.createCell(3).setCellValue(12.345);// 设置单元格内容,重载
	}
	
	
	@Test
	public void test2() {
		//创建批注
		HSSFPatriarch patr = sheet.createDrawingPatriarch();
		HSSFClientAnchor anchor = patr.createAnchor(0, 0, 0, 0, 5, 1, 8, 3);//创建批注位置
		HSSFComment comment = patr.createCellComment(anchor);//创建批注
		comment.setString(new HSSFRichTextString("这是一个批注段落！"));//设置批注内容
		comment.setAuthor("李志伟");//设置批注作者
		comment.setVisible(true);//设置批注默认显示
		HSSFCell cell = sheet.createRow(2).createCell(1);
		cell.setCellValue("测试");
		cell.setCellComment(comment);//把批注赋值给单元格
	}
	
	@Test
	public void test3() {
		workbook.createInformationProperties();//创建文档信息
		DocumentSummaryInformation dsi= workbook.getDocumentSummaryInformation();//摘要信息
		dsi.setCategory("类别:Excel文件");//类别
		dsi.setManager("管理者:caixt");//管理者
		dsi.setCompany("公司:--");//公司
		SummaryInformation si = workbook.getSummaryInformation();//摘要信息
		si.setSubject("主题:--");//主题
		si.setTitle("标题:测试文档");//标题
		si.setAuthor("作者:caixt");//作者
		si.setComments("备注:POI测试文档");//备注
		
		//创建页眉和页脚
		HSSFHeader header =sheet.getHeader();//得到页眉
		header.setLeft("页眉左边");
		header.setRight("页眉右边");
		header.setCenter("页眉中间");
		HSSFFooter footer =sheet.getFooter();//得到页脚
		footer.setLeft("页脚左边");
		footer.setRight("页脚右边");
		footer.setCenter("页脚中间");
	}
		
	@Test
	public void test4() {
		HSSFRow row=sheet.createRow(0);
		//设置日期格式--使用Excel内嵌的格式
		HSSFCell cell=row.createCell(0);
		cell.setCellValue(new Date());
		HSSFCellStyle style=workbook.createCellStyle();
		style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
		cell.setCellStyle(style);
		//设置保留2位小数--使用Excel内嵌的格式
		cell=row.createCell(1);
		cell.setCellValue(12.3456789);
		style=workbook.createCellStyle();
		style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
		cell.setCellStyle(style);
		//设置货币格式--使用自定义的格式
		cell=row.createCell(2);
		cell.setCellValue(12345.6789);
		style=workbook.createCellStyle();
		style.setDataFormat(workbook.createDataFormat().getFormat("￥#,##0"));
		cell.setCellStyle(style);
		//设置百分比格式--使用自定义的格式
		cell=row.createCell(3);
		cell.setCellValue(0.123456789);
		style=workbook.createCellStyle();
		style.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
		cell.setCellStyle(style);
		//设置中文大写格式--使用自定义的格式
		cell=row.createCell(4);
		cell.setCellValue(12345);
		style=workbook.createCellStyle();
		style.setDataFormat(workbook.createDataFormat().getFormat("[DbNum2][$-804]0"));
		cell.setCellStyle(style);
		//设置科学计数法格式--使用自定义的格式
		cell=row.createCell(5);
		cell.setCellValue(12345);
		style=workbook.createCellStyle();
		style.setDataFormat(workbook.createDataFormat().getFormat("0.00E+00"));
		cell.setCellStyle(style);
		
		
		CellRangeAddress region = new CellRangeAddress(0, 0, 1, 2);
		sheet.addMergedRegion(region);
	}
		
	@Test
	public void test5() {
		HSSFRow row=sheet.createRow(0);
		//合并列
		HSSFCell cell=row.createCell(0);
		cell.setCellValue("合并列");
		CellRangeAddress region=new CellRangeAddress(0, 0, 0, 5);
		sheet.addMergedRegion(region);
		//合并行
		cell=row.createCell(6);
		cell.setCellValue("合并行");
		region=new CellRangeAddress(0, 5, 6, 6);
		sheet.addMergedRegion(region);
	}
		
		
	@Test
	public void test6() {	
		HSSFRow row=sheet.createRow(0);
		HSSFCell cell=row.createCell(0);
		cell.setCellValue("单元格对齐");
		HSSFCellStyle style=workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		style.setWrapText(true);//自动换行
		style.setIndention((short)5);//缩进
		style.setRotation((short)60);//文本旋转，这里的取值是从-90到90，而不是0-180度。
		cell.setCellStyle(style);
	}
	
	@Test
	public void test7(){
		HSSFRow row=sheet.createRow(0);
		HSSFCell cell=row.createCell(1);
		cell.setCellValue("设置边框");
		HSSFCellStyle style=workbook.createCellStyle();
		style.setBorderTop(HSSFCellStyle.BORDER_DOTTED);//上边框
		style.setBorderBottom(HSSFCellStyle.BORDER_THICK);//下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_DOUBLE);//左边框
		style.setBorderRight(HSSFCellStyle.BORDER_SLANTED_DASH_DOT);//右边框
		style.setTopBorderColor(HSSFColor.RED.index);//上边框颜色
		style.setBottomBorderColor(HSSFColor.BLUE.index);//下边框颜色
		style.setLeftBorderColor(HSSFColor.GREEN.index);//左边框颜色
		style.setRightBorderColor(HSSFColor.PINK.index);//右边框颜色
		cell.setCellStyle(style);
		
		cell = row.createCell(2);
		cell.setCellValue("设置字体");
		style = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setFontName("华文行楷");//设置字体名称
		font.setFontHeightInPoints((short)28);//设置字号
		font.setColor(HSSFColor.RED.index);//设置字体颜色
		font.setUnderline(FontFormatting.U_SINGLE);//设置下划线
		font.setTypeOffset(FontFormatting.SS_SUPER);//设置上标下标
		font.setStrikeout(true);//设置删除线
		style.setFont(font);
		cell.setCellStyle(style);
	}
}
