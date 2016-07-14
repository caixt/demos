package com.github.cxt.MySpring.proxy.cglib;

import org.junit.Test;

public class Main {

	@Test
	public void test1(){
		BookServiceBean service = BookServiceFactory.getInstance();
		service.create();
		service.update();
		service.query();
		service.delete();
	}
	
	@Test
	public void test2(){
		BookServiceBean service = BookServiceFactory.getProxyInstance(new MyCglibProxy("boss"));
		service.create();
		service.query();
		BookServiceBean service2 = BookServiceFactory.getProxyInstance(new MyCglibProxy("john"));
		service2.create();
		service2.query();
	}
	
	
	@Test
	public void test3(){
		BookServiceBean service = BookServiceFactory.getAuthInstanceByFilter(new MyCglibProxy("boss"));
		service.create();
		service.query();
		BookServiceBean service2 = BookServiceFactory.getAuthInstanceByFilter(new MyCglibProxy("john"));
		service2.create();
		service2.query();
	}
}
