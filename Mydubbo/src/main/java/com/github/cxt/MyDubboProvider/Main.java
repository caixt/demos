package com.github.cxt.MyDubboProvider;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	public static void main(String[] args) {
		try {
			// 初始化Spring
			ApplicationContext ctx = new ClassPathXmlApplicationContext(
					"spring-provider.xml");
			System.out.println("dubbo provider is running...");
			System.in.read();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
