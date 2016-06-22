package com.github.cxt.MyHttp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
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
	public void testGet() throws ClientProtocolException, IOException{
		HttpClient httpclient = HttpClients.createDefault();
		String result = null;
		
		HttpGet getRequest = new HttpGet("/api/jersey/user/1");
		
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

}
