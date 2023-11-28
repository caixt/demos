package com.github.cxt.springboot2.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.github.cxt.springboot2.entities.R;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
	
	@Autowired
	MessageSource messageSource;
	
	
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<R> handler(Exception e, HttpServletRequest request ,HttpServletResponse response) {
		String requestUser = (String) request.getAttribute(HttpServletRequestWrapper.REQUEST_USER);
		String requestURL = (String) request.getAttribute(HttpServletRequestWrapper.REQUEST_URL);
		String requestMethod = (String) request.getAttribute(HttpServletRequestWrapper.REQUEST_METHOD);
		String requestBody = (String) request.getAttribute(HttpServletRequestWrapper.REQUEST_BODY);
		String clientIp = (String) request.getAttribute(HttpServletRequestWrapper.REQUEST_CLIENT_IP);
		String message = null;
		Locale locale = LocaleContextHolder.getLocale();
		if (e instanceof BindException) {
			BindException validException = (BindException) e;
			List<String> errs = new ArrayList<>();
			for(FieldError err : validException.getFieldErrors()) {
				errs.add(String.format("%s:%s", err.getField(), messageSource.getMessage(err, locale)));
			}
			message = errs.stream().collect(Collectors.joining(";"));
			log.error("{},请求异常,url:{},method:{},client:{},body:{},message:{}", requestUser, requestURL, requestMethod, clientIp, requestBody, message);
		}
		else if (e instanceof ConstraintViolationException) {
			ConstraintViolationException validException = (ConstraintViolationException) e;
			List<String> errs = new ArrayList<>();
			for(ConstraintViolation<?> temp : validException.getConstraintViolations()) {
				Iterator<Node> iterator = temp.getPropertyPath().iterator();
				String name = null;
				while(iterator.hasNext()) {
					name = iterator.next().getName();
				}
				errs.add(String.format("%s:%s", name, temp.getMessage()));
			}
			message = errs.stream().collect(Collectors.joining(";"));
			log.error("{},请求异常,url:{},method:{},client:{},body:{},message:{}", requestUser, requestURL, requestMethod, clientIp, requestBody, message);
		} else {
			message = e.getMessage();
			log.error("{},请求异常,url:{},method:{},client:{},body:{}", requestUser, requestURL, requestMethod, clientIp, requestBody, e);
		}
    	return new ResponseEntity<R>(R.fail(message), HttpStatus.BAD_REQUEST);
    }
}
