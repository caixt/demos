package com.github.cxt.MyHttp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpsTest {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@Test
	public void testTrusted() throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException{
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
             //信任所有
             public boolean isTrusted(X509Certificate[] chain,
                             String authType) throws CertificateException {
                 return true;
             } 

        }).build();
         
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        HttpClient httpclient = HttpClientBuilder.create().setSSLSocketFactory(sslsf).build();
        doGet(httpclient, "https://127.0.0.1:8443/api/jersey/user/1");
	}
	
	
	@Test
	public void testKeystore() throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException{
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(new File("../MyDocker/images/nginx/ssl/example_com.keystore"), "12345678".toCharArray()).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
        HttpClient httpclient = HttpClientBuilder.create().setSSLSocketFactory(sslsf).build();
        //example.com 在本地host里添加映射
        doGet(httpclient, "https://example.com:8443/api/jersey/user/1");
	}
	
	@Test
	public void testCrt() throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException{
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		InputStream fis = new FileInputStream(new File("../MyDocker/images/nginx/ssl/example_com.crt"));
		Certificate cert = cf.generateCertificate(fis);
		
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
	    keyStore.load(null, null);
	    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	    keyStore.setCertificateEntry("CA", cert);
	    tmf.init(keyStore);
	    
	    SSLContext ctx = SSLContext.getInstance("SSLv3");
        ctx.init(null, tmf.getTrustManagers(), null);
	    
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx);
        HttpClient httpclient = HttpClientBuilder.create().setSSLSocketFactory(sslsf).build();
        //example.com 在本地host里添加映射
        doGet(httpclient, "https://example.com:8443/api/jersey/user/1");
	}
	
	
	private void doGet(HttpClient httpclient, String url) throws ClientProtocolException, IOException{
		HttpGet getRequest = new HttpGet(url);
		getRequest.setHeader("Connection", "close");
		getRequest.setHeader("Content-Type", "application/json;charset=UTF-8");

		HttpResponse httpResponse = httpclient.execute(getRequest);
		
		HttpEntity entity = httpResponse.getEntity();
		logger.info(httpResponse.getStatusLine().getStatusCode() + "");
		if (entity != null) {
			logger.info(EntityUtils.toString(entity));
		}
	}
}
