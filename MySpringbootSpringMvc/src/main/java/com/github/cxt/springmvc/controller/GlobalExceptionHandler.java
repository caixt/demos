package com.github.cxt.springmvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Message> handler(RuntimeException e, HttpServletRequest request ,HttpServletResponse response) {
    	e.printStackTrace();
    	return new ResponseEntity<Message>(new Message("test"), HttpStatus.BAD_REQUEST);
    }
}

class Message{
	private String msg;
	
	public Message(String msg){
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
