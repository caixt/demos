package uyun.boltdog.sysconf.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * saltstack的rest调用
 * @author wangzf
 * @date 2015-10-10 下午4:51:51
 *
 */
public class HttpRestUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpRestUtil.class);

	private static final String COOKIE_KEY = "Set-Cookie";

	public static String get(String address, int port, String path, String cookie)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String result = null;
		HttpHost target = new HttpHost(address, port);

		HttpGet getRequest = new HttpGet(path);
		getRequest.setHeader("Cookie", cookie);
		getRequest.setHeader("Content-Type", "application/json;charset=UTF-8");
		logger.info("executing request to " + target);

		HttpResponse httpResponse = httpclient.execute(target, getRequest);
		HttpEntity entity = httpResponse.getEntity();

		if (entity != null) {
			result = EntityUtils.toString(entity);
			logger.info(result);
		}

		return result;
	}

	public static String login(String address, int port, String path) throws ParseException, IOException {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		String cookie = null;
		HttpHost target = new HttpHost(address, port);

		HttpPost postRequest = new HttpPost(path);

		logger.info("executing request to " + target);

		HttpResponse httpResponse = httpclient.execute(target, postRequest);
		HttpEntity entity = httpResponse.getEntity();

		StatusLine statusLine = httpResponse.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		logger.info("login code: " + statusCode);
		if (200 != statusCode)
			return null;
		Header[] headers = httpResponse.getAllHeaders();
		for (int i = 0; i < headers.length; i++) {
			if (headers[i].getName().equals(COOKIE_KEY)) {
				cookie = headers[i].getValue();
				logger.info("登录cookie：" + headers[i]);
			}
		}

		if (entity != null) {
			String result = EntityUtils.toString(entity);
			logger.info("登录结果：" + result);
			JSONObject jsonObj = JSONObject.parseObject(result);
			String success = jsonObj.getString("success");
			if (!"true".equals(success)) {
				return null;
			}
		}

		return cookie;
	}

	public static String post(String address, int port, String path, String cookie)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String result = null;
		HttpHost target = new HttpHost(address, port);

		HttpPost postRequest = new HttpPost(path);
		postRequest.setHeader("Cookie", cookie);
		postRequest.setHeader("Content-Type", "application/json;charset=UTF-8");

		logger.info("executing request to " + target);

		HttpResponse httpResponse = httpclient.execute(target, postRequest);
		HttpEntity entity = httpResponse.getEntity();

		if (entity != null) {
			result = EntityUtils.toString(entity);
			logger.info(result);
		}

		return result;
	}

	public static String postBody(String address, int port, String path, String cookie, Map<String, Object> data)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String result = null;
		HttpHost target = new HttpHost(address, port);

		HttpPost postRequest = new HttpPost(path);
		postRequest.setHeader("Cookie", cookie);

		if (data != null) {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setCharset(Charset.forName("UTF-8"));
			ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
			for (Entry<String, Object> entry : data.entrySet()) {
				builder.addTextBody(entry.getKey().toString(), entry.getValue().toString(), contentType);//增加文本内容

			}
			postRequest.setEntity(builder.build());
		}

		logger.info("executing request to " + target);

		HttpResponse httpResponse = httpclient.execute(target, postRequest);
		HttpEntity entity = httpResponse.getEntity();

		if (entity != null) {
			result = EntityUtils.toString(entity);
			logger.info(result);
		}

		return result;
	}
	
	public static String putBody(String address, int port, String path, String cookie, Map<String, Object> data)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String result = null;
		HttpHost target = new HttpHost(address, port);

		HttpPut putRequest = new HttpPut(path);
		putRequest.setHeader("Cookie", cookie);

		if (data != null) {
						List<BasicNameValuePair> parameters = new ArrayList<>();
						
			for (Entry<String, Object> entry : data.entrySet()) {
				parameters.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,"UTF-8");
			putRequest.setEntity(entity);
		}

		logger.info("executing request to " + target);

		HttpResponse httpResponse = httpclient.execute(target, putRequest);
		HttpEntity entity = httpResponse.getEntity();

		if (entity != null) {
			result = EntityUtils.toString(entity);
			logger.info(result);
		}

		return result;
	}
	
}
