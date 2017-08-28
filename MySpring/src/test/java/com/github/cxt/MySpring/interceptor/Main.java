package com.github.cxt.MySpring.interceptor;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.cxt.MySpring.schema.People;

public class Main {

	@Test
	public void test0() {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("com/github/cxt/MySpring/interceptor/spring-content.xml");
		
		Server server = applicationContext.getBean(Server.class);
		server.test1();
		server.test2();
		People p = new People();
		p.setName("tes111t");
		server.test3(p);
		applicationContext.close();
	}
}
