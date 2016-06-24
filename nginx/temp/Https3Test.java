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
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Https3Test {
	
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
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		InputStream fis = new FileInputStream(new File("D:\\nginx-1.6.3\\conf\\sslkey\\example_com.crt"));
		Certificate cert = cf.generateCertificate(fis);
		
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
	    keyStore.load(null, null);
	    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	    keyStore.setCertificateEntry("CA", cert);
	    tmf.init(keyStore);
	    
	    SSLContext ctx = SSLContext.getInstance("SSLv3");
        ctx.init(null, tmf.getTrustManagers(), null);
	    
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx);
        return  HttpClientBuilder.create().setSSLSocketFactory(sslsf).build();
	}
	

}
