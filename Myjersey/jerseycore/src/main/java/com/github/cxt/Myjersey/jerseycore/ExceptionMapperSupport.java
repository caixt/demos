package com.github.cxt.Myjersey.jerseycore;

import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import javax.inject.Provider;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
/**
 * 统一异常处理器
 */
@javax.ws.rs.ext.Provider
public class ExceptionMapperSupport implements ExceptionMapper<RuntimeException> {
	private static final String CONTEXT_ATTRIBUTE = WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Context
    private Provider<Request> provider;
    @Context
    private Configuration config;
    @Context
    private ServletContext servletContext;
	@Context 
	private HttpServletRequest request;
	/**
	 * 异常处理
	 * 
	 * @param exception
	 * @return 异常处理后的Response对象
	 */
	public Response toResponse(RuntimeException exception) {
    	WebApplicationContext context = (WebApplicationContext) servletContext.getAttribute(CONTEXT_ATTRIBUTE);
    	String message = context.getMessage(exception.getMessage(), null, request.getLocale());
		logger.warn(message + "," + HttpBefore.body.get());
		final Response.ResponseBuilder response = Response.serverError().type(MediaType.TEXT_PLAIN_TYPE)
				.entity(message);
        return response.build();
	}
}
