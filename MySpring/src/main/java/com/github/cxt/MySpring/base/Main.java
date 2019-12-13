package com.github.cxt.MySpring.base;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class, MainConfig.class);
		System.out.println("--------------------");
		String[] namesForType = applicationContext.getBeanDefinitionNames();
		for (String name : namesForType) {
			System.out.println(name);
		}
		System.out.println("--------------------");
		Object o = applicationContext.getBean(DemoBean.class);
		System.out.println(o);
		System.out.println("--------------------");
		o = applicationContext.getBean("demoFactoryBean");
		System.out.println(o);
		System.out.println("--------------------");
		applicationContext.close();
	}

}
