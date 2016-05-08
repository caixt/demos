package com.github.cxt.Myjersey.jerseycore;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
/**
 * 统一异常处理器
 */
@Provider
public class ExceptionMapperSupport implements ExceptionMapper<Exception> {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Context 
	HttpServletRequest request;
	/**
	 * 异常处理
	 * 
	 * @param exception
	 * @return 异常处理后的Response对象
	 */
	public Response toResponse(Exception exception) {
		String msg = "url:" +  request.getRequestURI() + ",method:" + request.getMethod();
		logger.error(msg, exception);
		return Response.ok("service exception", MediaType.TEXT_PLAIN).status(500)
				.build();
	}
}
