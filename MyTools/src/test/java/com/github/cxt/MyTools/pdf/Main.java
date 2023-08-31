package com.github.cxt.MyTools.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class Main {
	
	@Test
	public void test1() throws IOException, URISyntaxException {
		OutputStream out = new FileOutputStream(new File("test.pdf"));
		File file = new File(Main.class.getResource("test.md").toURI());
		MdToPdfUtils.md2Ptf(file, out);
		out.close();
	}
	
	//跟md转pdf相比 这个样式好看多了
	@Test
	public void test2() throws IOException, URISyntaxException {
		OutputStream out = new FileOutputStream(new File("test.pdf"));
		File file = new File(Main.class.getResource("test.html").toURI());
		String html = FileUtils.readFileToString(file);
		PDFUtils.html2pdf(html, out, file.getParentFile());
		out.close();
	}
	
	//plainHeight,plainWidth
	@Test
	public void test3() throws IOException, URISyntaxException {
		OutputStream out = new FileOutputStream(new File("test.pdf"));
		File file = new File(Main.class.getResource("image.html").toURI());
		String html = FileUtils.readFileToString(file);
		PDFUtils.html2pdf(html, out, file.getParentFile());
		out.close();
	}

}
