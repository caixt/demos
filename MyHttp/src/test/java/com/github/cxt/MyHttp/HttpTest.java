package com.github.cxt.MyHttp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpTest {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	private HttpHost host = new HttpHost("127.0.0.1", 8088);
	
	@Test
	public void testGet() throws ClientProtocolException, IOException, CloneNotSupportedException{
		HttpClient httpclient = HttpClients.createDefault();
		String result = null;
		
		HttpGet getRequest = new HttpGet("/api/jersey/user/1");
		getRequest.setHeader("Connection", "close");  
		getRequest.setHeader("Content-Type", "application/json;charset=UTF-8");

		HttpResponse httpResponse = httpclient.execute(host, getRequest);
		
		HttpEntity entity = httpResponse.getEntity();
		logger.info(httpResponse.getStatusLine().getStatusCode() + "");
		if (entity != null) {
			result = EntityUtils.toString(entity);
			logger.info(result);
		}
	}
	
	@Test
	public void testPost() throws ClientProtocolException, IOException{
		HttpClient httpclient = HttpClients.createDefault();
		String result = null;
		
		HttpPost postRequest = new HttpPost("/api/jersey/user");
		postRequest.setEntity(new StringEntity("{\"name\":\"aaa\",\"age\":1}", "UTF-8"));
		
		postRequest.setHeader("Connection", "close");  
		postRequest.setHeader("Content-Type", "application/json;charset=UTF-8");

		HttpResponse httpResponse = httpclient.execute(host, postRequest);
		
		HttpEntity entity = httpResponse.getEntity();
		logger.info(httpResponse.getStatusLine().getStatusCode() + "");
		if (entity != null) {
			result = EntityUtils.toString(entity);
			logger.info(result);
		}
	}
	
	@Test
	public void testPut() throws ClientProtocolException, IOException{
		HttpClient httpclient = HttpClients.createDefault();
		String result = null;
		
		HttpPut putRequest = new HttpPut("/api/jersey/user/3");
		putRequest.setEntity(new StringEntity("{\"name\":\"aaa\",\"age\":1}", "UTF-8"));
		putRequest.setHeader("Connection", "close");  
		putRequest.setHeader("Content-Type", "application/json;charset=UTF-8");

		HttpResponse httpResponse = httpclient.execute(host, putRequest);
		
		HttpEntity entity = httpResponse.getEntity();
		logger.info(httpResponse.getStatusLine().getStatusCode() + "");
		if (entity != null) {
			result = EntityUtils.toString(entity);
			logger.info(result);
		}
	}
	
	@Test
	public void testDelete() throws ClientProtocolException, IOException{
		HttpClient httpclient = HttpClients.createDefault();
		String result = null;
		
		HttpDelete deleteRequest = new HttpDelete("/api/jersey/user/3");
		deleteRequest.setHeader("Connection", "close");  
		deleteRequest.setHeader("Content-Type", "application/json;charset=UTF-8");
		HttpResponse httpResponse = httpclient.execute(host, deleteRequest);
		
		HttpEntity entity = httpResponse.getEntity();
		logger.info(httpResponse.getStatusLine().getStatusCode() + "");
		if (entity != null) {
			result = EntityUtils.toString(entity);
			logger.info(result);
		}
	}
	
	
	@Test
	public void testNoRestPost() throws ClientProtocolException, IOException{
		HttpClient httpclient = HttpClients.createDefault();
		String result = null;
		
		HttpPost postRequest = new HttpPost("/api/jersey/olduser");
		postRequest.setHeader("Connection", "close");  
		
		List<BasicNameValuePair> parameters =  new ArrayList<BasicNameValuePair>();
		
	    parameters.add(new BasicNameValuePair("a", "1".toString()));
	    
	    parameters.add(new BasicNameValuePair("b", "1".toString()));
	
		postRequest.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
		
		//postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");

		HttpResponse httpResponse = httpclient.execute(host, postRequest);
		
		HttpEntity entity = httpResponse.getEntity();
		logger.info(httpResponse.getStatusLine().getStatusCode() + "");
		if (entity != null) {
			result = EntityUtils.toString(entity);
			logger.info(result);
		}
	}
	
	
	@Test
	public void testDownload() throws ClientProtocolException, IOException{
		//String url = "http://mirrors.aliyun.com/centos/7/os/x86_64/Packages/389-ds-base-1.3.4.0-19.el7.x86_64.rpm";
		String url = "/api/file/download";
		HttpClient client = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);  
        httpget.setHeader("Connection", "close");
        HttpResponse response = client.execute(host, httpget);  

        HttpEntity entity = response.getEntity();  
        InputStream is = entity.getContent();  
        
        String fileName = getFileName(response);
        if(fileName == null){
        	fileName = url.substring(url.lastIndexOf('/') + 1);
        }
        File file = new File("temp" + File.separator + UUID.randomUUID() + File.separator + fileName);  
        FileUtils.copyInputStreamToFile(is, file);
	}
	
	
	@Test
	public void testUpload() throws ClientProtocolException, IOException{		
		HttpClient httpclient = HttpClients.createDefault();
		String result = null;
		
		HttpPost postRequest = new HttpPost("/api/file/upload");
		postRequest.setHeader("Connection", "close");  
		
		MultipartEntity part = //new MultipartEntity();
		new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
		FileBody file = new FileBody(new File("README.md"));
		part.addPart("file", file);
		postRequest.setEntity(part);
		HttpResponse httpResponse = httpclient.execute(host, postRequest);
		
		HttpEntity entity = httpResponse.getEntity();
		logger.info(httpResponse.getStatusLine().getStatusCode() + "");
		if (entity != null) {
			result = EntityUtils.toString(entity);
			logger.info(result);
		}
	}
	
	
	private static String getFileName(HttpResponse response) {  
        Header contentHeader = response.getFirstHeader("Content-Disposition");  
        String filename = null;  
        if (contentHeader != null) {  
            HeaderElement[] values = contentHeader.getElements();  
            if (values.length == 1) {  
                NameValuePair param = values[0].getParameterByName("filename");  
                if (param != null) {  
                    //filename = new String(param.getValue().toString().getBytes(), "utf-8");  
                    //filename=URLDecoder.decode(param.getValue(),"utf-8");  
                    filename = param.getValue();  
   
                }  
            }  
        } 
        return filename;  
    }

}
