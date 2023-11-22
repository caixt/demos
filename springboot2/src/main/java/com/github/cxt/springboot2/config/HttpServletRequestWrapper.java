package com.github.cxt.springboot2.config;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;


public class HttpServletRequestWrapper extends javax.servlet.http.HttpServletRequestWrapper {
	
	public static final String REQUEST_URL = "REQUEST-URL";
	public static final String REQUEST_USER = "REQUEST-USER";
	public static final String REQUEST_METHOD = "REQUEST-METHOD";
	public static final String REQUEST_BODY = "REQUEST-BODY";
	public static final String REQUEST_CLIENT_IP = "REQUEST-CLENT-IP";
	
	
	private boolean wapper = false;

    private byte[] body;

    public HttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        StringBuffer url = new StringBuffer(request.getRequestURI());
        String params = request.getQueryString();
        if(StringUtils.isNotBlank(params)) {
        	url.append("?").append(params);
        }
        String bodyContent = null;
        String method = request.getMethod();
		String contentType = request.getContentType();
		if (StringUtils.equalsIgnoreCase("post", method) && StringUtils
				.startsWith(contentType, MediaType.APPLICATION_JSON_VALUE)) {
			body = IOUtils.toByteArray(request.getInputStream());
			wapper = true;
			
			Charset charset = StringUtils.isBlank(request.getCharacterEncoding()) ?
					Charset.forName("UTF-8") :
					Charset.forName(request.getCharacterEncoding());
			bodyContent = new String(body, charset);
		}
		request.setAttribute(REQUEST_URL, url.toString());
		request.setAttribute(REQUEST_METHOD, method);
		request.setAttribute(REQUEST_BODY, bodyContent);
		request.setAttribute(REQUEST_CLIENT_IP, getIpAddress(request));
    }

    /**
     * 重新包装输入流
     * @return
     * @throws IOException
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
    	if(wapper) {
	        InputStream bodyStream = new ByteArrayInputStream(body);
	        return new ServletInputStream() {
	
	            @Override
	            public int read() throws IOException {
	                return bodyStream.read();
	            }
	
	            /**
	             * 下面的方法一般情况下不会被使用，如果你引入了一些需要使用ServletInputStream的外部组件，可以重点关注一下。
	             * @return
	             */
	            @Override
	            public boolean isFinished() {
	                return false;
	            }
	
	            @Override
	            public boolean isReady() {
	                return true;
	            }
	
	            @Override
	            public void setReadListener(ReadListener readListener) {
	
	            }
	        };
    	}
    	return super.getInputStream();
    }
    
	private static boolean ipIsUnknown(String ip) {
		return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
	}
    
    @Override
    public BufferedReader getReader() throws IOException {
    	if(wapper) {
    		return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(body)));
    	}
    	return super.getReader();
    }
    
	private static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		if (ipIsUnknown(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipIsUnknown(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ipIsUnknown(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ipIsUnknown(ip)) {
			ip = request.getRemoteAddr();
		}
		} else if (ip.length() > 15) {
			String[] ips = ip.split(",");
			for (int index = 0; index < ips.length; index++) {
				String strIp = (String) ips[index];
				if (!("unknown".equalsIgnoreCase(strIp))) {
					ip = strIp;
					break;
				}
			}
		}
		return ip;
	}
}