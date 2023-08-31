package com.github.cxt.MyTools.pdf;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class MdToPdfUtils {
	
	public static void md2Ptf(File mdFile, OutputStream out) throws IOException {
		String htmlContent = md2html(mdFile);
		System.out.println(htmlContent);
		PDFUtils.html2pdf(htmlContent, out, mdFile.getParentFile());
		
	}

	private static String md2html(File file) throws IOException {
		HttpClient httpclient = HttpClients.createDefault();
		String result = null;
		
		HttpPost postRequest = new HttpPost("https://api.github.com/markdown");
		postRequest.setHeader("Connection", "close");  
		

		String md = FileUtils.readFileToString(file);
		md = md.replace("\\", "\\\\");
		md = md.replace("\r\n", "\\r\\n");
		md = md.replace("\t", "\\t");
		md = md.replace("\"", "\\\"");
		
		postRequest.setEntity(new StringEntity("{\"text\":\"" + md + "\"}", "UTF-8"));
		

		HttpResponse httpResponse = httpclient.execute(postRequest);
		
		HttpEntity entity = httpResponse.getEntity();
		if(httpResponse.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException();
		}
		if (entity != null) {
			result = EntityUtils.toString(entity);
			return format(result);
		}
		return null;
	}



	private static String format(String result) throws IOException {
		result = result.replace("<hr>", "<hr/>");
		result = result.replaceAll("<p>\\W(\\t+)", "<p><span>$1</span>");
		result = result.replaceAll("<p>((?:<a.+?><img .+?></a>\n?)+?)</p>", "$1");
		result = result.replaceAll("<a.+?>(<img .+?)></a>", "<p>$1/></p>");
		
		String html = IOUtils.toString(MdToPdfUtils.class.getResourceAsStream("pdf.templete"));
		return html.replace("#{content}", result);
	}
}
