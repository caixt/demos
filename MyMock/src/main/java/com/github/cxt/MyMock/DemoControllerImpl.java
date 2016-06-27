package com.github.cxt.MyMock;

import org.springframework.beans.factory.annotation.Autowired;

public class DemoControllerImpl implements DemoController{
	
	@Autowired
	private DemoService demoService;

	public String test1() {
		return demoService.doSth();
		
	}
}
