package com.github.cxt.Myjersey.jerseycore;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

/**
 * 支持跨域的配置
 * @author caixt
 * @Description:
 * @date 2017年4月24日
 */
@javax.ws.rs.ext.Provider
public class CrossControlFilter implements ContainerResponseFilter {
	private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	private static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
	private static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
	private static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	private static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";

	
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
		responseContext.getHeaders().add(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		responseContext.getHeaders().add(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
		responseContext.getHeaders().add(ACCESS_CONTROL_ALLOW_METHODS,
				"GET, POST, PUT, DELETE, UPDATE, OPTIONS, HEAD, PROPFIND");
		responseContext.getHeaders().add(ACCESS_CONTROL_ALLOW_HEADERS,
				"Overwrite, Destination, Content-Type, Depth, User-Agent, X-File-Size, X-Requested-With, If-Modified-Since, X-File-Name, Cache-Control");
		responseContext.getHeaders().add(ACCESS_CONTROL_MAX_AGE, "1728000");
	}
}