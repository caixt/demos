package com.github.cxt.MyHttp;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpsTest {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@Test
	public void testGet() throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException{
		HttpClient httpclient = createSSLClientDefault();
		String result = null;
		
		HttpGet getRequest = new HttpGet("https://127.0.0.1:8443/jersey/user/1");
		
		getRequest.setHeader("Content-Type", "application/json;charset=UTF-8");

		HttpResponse httpResponse = httpclient.execute(getRequest);
		
		HttpEntity entity = httpResponse.getEntity();
		logger.info(httpResponse.getStatusLine().getStatusCode() + "");
		if (entity != null) {
			result = EntityUtils.toString(entity);
			logger.info(result);
		}
	}
	
	
	public static CloseableHttpClient createSSLClientDefault() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, SSLException{
         SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
             //信任所有
             public boolean isTrusted(X509Certificate[] chain,
                             String authType) throws CertificateException {
                 return true;
             } 

         }).build();
         
         SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
         return  HttpClientBuilder.create().setSSLSocketFactory(sslsf).build();
	}
	

}
