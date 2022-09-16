package com.github.cxt.MyTools.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.itextpdf.text.pdf.codec.Base64;

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
	
	@Test
	public void test4() throws IOException, URISyntaxException {
		OutputStream out = new FileOutputStream(new File("test.pdf"));
		PDFUtils2.htmlToPDF(Main.class.getResourceAsStream("image.html"), out);
		out.close();
	}
	
	
	@Test
	public void test0() throws IOException {
		String text = getImgBase64Str(Main.class.getResourceAsStream("1.jpg"));
		FileUtils.write(new File("1.txt"), text);
		System.out.println(text);
	}
	
	public static String getImgBase64Str(InputStream in) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        // 读取图片字节数组
        try {
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        return Base64.encodeBytes(data);
    }

}
