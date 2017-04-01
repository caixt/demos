package com.github.cxt.MyHttp;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class HttpMockTest {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Rule
    public MockServerRule server = new MockServerRule(this, 8080);
	
	@Before
	public void before(){
		MockServerClient mockClient = new MockServerClient("127.0.0.1", 8080);
        mockClient.when(
                request()
                        .withPath("/api/v1/login")
                        .withMethod("POST")
                        
        ).respond(
                response()
                        .withStatusCode(200)
                        .withBody("{\"success\": \"true\"}")
        );
	}
	
	@Test
	public void test() throws ClientProtocolException, IOException{
		HttpClient httpclient = HttpClients.createDefault();
		String result = null;
		
		HttpPost method = new HttpPost("http://127.0.0.1:8080/api/v1/login");
		method.setEntity(new StringEntity("{\"name\":\"aaa\",\"passwd\":\"123456\"}", "UTF-8"));
		
		method.setHeader("Connection", "close");  
		method.setHeader("Content-Type", "application/json;charset=UTF-8");

		HttpResponse httpResponse = httpclient.execute(method);
		
		HttpEntity entity = httpResponse.getEntity();
		logger.info(httpResponse.getStatusLine().getStatusCode() + "");
		if (entity != null) {
			result = EntityUtils.toString(entity);
			logger.info(result);
		}
	}
	
}
