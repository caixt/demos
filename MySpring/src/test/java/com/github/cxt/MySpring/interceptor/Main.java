package com.github.cxt.MySpring.interceptor;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.github.cxt.MySpring.schema.People;

//http://blog.csdn.net/chyohn/article/details/54946004
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
		System.out.println("!!!!!!!!!!!!!");
        for(String str : applicationContext.getBeanDefinitionNames()){
        	System.err.println(str + "???" + applicationContext.getBean(str));
        }
        System.err.println(server);
        applicationContext.close();
	}
}
