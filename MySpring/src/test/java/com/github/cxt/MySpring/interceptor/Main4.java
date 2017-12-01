package com.github.cxt.MySpring.interceptor;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main4 {


	@Test
	public void test0() {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("com/github/cxt/MySpring/interceptor/spring-content2.xml");
		System.out.println("!!!!!!!!");
		System.out.println(applicationContext.getBean("server"));  
	}
	
}
