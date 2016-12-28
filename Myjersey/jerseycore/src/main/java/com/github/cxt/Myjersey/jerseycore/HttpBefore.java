package com.github.cxt.Myjersey.jerseycore;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


@javax.ws.rs.ext.Provider
public class HttpBefore implements ContainerRequestFilter{
	
	public static ThreadLocal<HttpInfo> body = new ThreadLocal<>();
	
	@Context
	private HttpServletRequest request;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String method = requestContext.getMethod();
		String url = requestContext.getUriInfo().getRequestUri().toString();
    	String contentType = requestContext.getHeaderString("Content-Type");
		if("post".equalsIgnoreCase(method) && contentType != null && contentType.startsWith(MediaType.APPLICATION_JSON)){
			InputStream inputStream = requestContext.getEntityStream();
			StringBuilder sb = new StringBuilder();
			Charset charset = getCharSet();
			InputStreamReader input = new InputStreamReader(inputStream, charset);
			BufferedReader reader = new BufferedReader(input);
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			requestContext.setEntityStream(new ByteArrayInputStream(sb.toString().getBytes(charset)));
			body.set(new HttpInfo(url, method, sb.toString()));
		}
		else{
			body.set(new HttpInfo(url, method, null));
		}
	}
	
	
	private Charset getCharSet(){
		String charset = request.getCharacterEncoding();
		if(charset == null || charset.trim().equals("")){
			return Charset.defaultCharset();
		}
		return Charset.forName(charset);
	}
	
	
	public static class HttpInfo{
		private String url;
		private String method;
		private String body;
		
		public HttpInfo(String url, String method, String body) {
			super();
			this.url = url;
			this.method = method;
			this.body = body;
		}
		@Override
		public String toString() {
			return "HttpInfo [url=" + url + ", method=" + method + ", body=" + body + "]";
		}
		
		
	}
}
