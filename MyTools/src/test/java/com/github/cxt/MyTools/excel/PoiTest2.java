package com.github.cxt.MyTools.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.junit.Test;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PoiTest2 {

	//https://wenku.baidu.com/view/083641e3a900b52acfc789eb172ded630a1c984f.html
	@Test
	public void test8() throws Exception{
		FileInputStream in = new FileInputStream(new File("templete.xlsx"));
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(in);
		XSSFSheet userSheet = xssfWorkbook.getSheetAt(0);
		

		XSSFRow row = userSheet.getRow(0);
		XSSFCell xhCell = row.createCell(0);
		xhCell.setCellValue(11);
		xhCell = row.createCell(1);
		xhCell.setCellValue(22);
	
		xssfWorkbook.setForceFormulaRecalculation(true);
//		xssfWorkbook.getCreationHelper().createFormulaEvaluator().evaluateFormulaCell(row.getCell(2));
		
		FileOutputStream fileOutputStream = new FileOutputStream(new File("result.xlsx"));
		xssfWorkbook.write(fileOutputStream);
		xssfWorkbook.close();
	}
}
