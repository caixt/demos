package com.github.cxt.MySpring.schema;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("systemTestContext.xml");
		System.out.println(ctx);
		
		People p =  ctx.getBean(People.class);  
		System.out.println(p.getId());  
		System.out.println(p.getName());  
		System.out.println(p.getAge()); 

		System.out.println(ctx.getBean("aaaa"));
	}

}
