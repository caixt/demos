package com.github.cxt.springboot2.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.github.cxt.springboot2.entities.R;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
	
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<R> handler(Exception e, HttpServletRequest request ,HttpServletResponse response) {
		String requestUser = (String) request.getAttribute(HttpServletRequestWrapper.REQUEST_USER);
		String requestURL = (String) request.getAttribute(HttpServletRequestWrapper.REQUEST_URL);
		String requestMethod = (String) request.getAttribute(HttpServletRequestWrapper.REQUEST_METHOD);
		String requestBody = (String) request.getAttribute(HttpServletRequestWrapper.REQUEST_BODY);
		String clientIp = (String) request.getAttribute(HttpServletRequestWrapper.REQUEST_CLIENT_IP);
		log.error("{},请求异常,url:{},method:{},client:{},body:{}", requestUser, requestURL, requestMethod, clientIp, requestBody, e);
    	return new ResponseEntity<R>(R.fail(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
