package com.github.cxt.MyHttp;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Https2Test {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@Test
	public void testGet() throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException{
		HttpClient httpclient = createSSLClientDefault();
		String result = null;
		
		HttpGet getRequest = new HttpGet("https://example.com:8443/jersey/user/1");
		
		getRequest.setHeader("Content-Type", "application/json;charset=UTF-8");

		HttpResponse httpResponse = httpclient.execute(getRequest);
		
		HttpEntity entity = httpResponse.getEntity();
		logger.info(httpResponse.getStatusLine().getStatusCode() + "");
		if (entity != null) {
			result = EntityUtils.toString(entity);
			logger.info(result);
		}
	}
	
	
	public static CloseableHttpClient createSSLClientDefault() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException{
         SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(new File("D:\\nginx-1.6.3\\conf\\sslkey\\example_com.keystore"), "12345678".toCharArray()).build();
         
         SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
         return  HttpClientBuilder.create().setSSLSocketFactory(sslsf).build();
	}
	

}
