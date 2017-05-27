package com.github.cxt.MyTools.excel;

import java.io.File;
import java.io.FileFilter;
import org.junit.Test;
import jxl.Workbook;
import jxl.write.WritableWorkbook;


public class JxlTest {

	@Test
	public void test() throws Exception{
		File dir = new File("./");
		File[] files = dir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				String name = pathname.getName();
				if(pathname.isFile() && name.indexOf(".") > 0 && 
						name.substring(name.lastIndexOf(".") + 1).equals("xls") && !name.equals("result.xls")){ 
					return true;
				}
				return false;
			}
		});
		
		if(files.length > 0){
			int i = 0;
			WritableWorkbook newWb = Workbook.createWorkbook(new File("result.xls"));
			for(File file  : files){
				Workbook wb = Workbook.getWorkbook(file);
				newWb.importSheet(i++ +"", 0, wb.getSheet(0));  
				wb.close();
			}
	        newWb.write();
	        newWb.close();
		}
	}
}
