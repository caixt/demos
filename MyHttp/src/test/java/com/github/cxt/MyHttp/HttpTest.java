package com.github.cxt.MyHttp;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpTest {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	private HttpHost host = new HttpHost("127.0.0.1", 8088);
	
	@Test
	public void testGet() throws ClientProtocolException, IOException{
		HttpClient httpclient = HttpClients.createDefault();
		String result = null;
		
		HttpGet getRequest = new HttpGet("/jersey/user/1");
		
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
		
		HttpPost postRequest = new HttpPost("/jersey/user");
		postRequest.setEntity(new StringEntity("{\"name\":\"aaa\",\"age\":1}", "UTF-8"));
		
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
		
		HttpPut putRequest = new HttpPut("/jersey/user/3");
		putRequest.setEntity(new StringEntity("{\"name\":\"aaa\",\"age\":1}", "UTF-8"));
		
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
		
		HttpDelete deleteRequest = new HttpDelete("/jersey/user/3");
		
		deleteRequest.setHeader("Content-Type", "application/json;charset=UTF-8");
		HttpResponse httpResponse = httpclient.execute(host, deleteRequest);
		
		HttpEntity entity = httpResponse.getEntity();
		logger.info(httpResponse.getStatusLine().getStatusCode() + "");
		if (entity != null) {
			result = EntityUtils.toString(entity);
			logger.info(result);
		}
	}

}
