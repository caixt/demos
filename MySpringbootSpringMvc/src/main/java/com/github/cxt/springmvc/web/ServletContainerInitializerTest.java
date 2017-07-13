package com.github.cxt.springmvc.web;

import java.util.Set;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;


public class ServletContainerInitializerTest implements ServletContainerInitializer{

	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		System.err.println("--------------------------------haha---------------------------------------------------------");
		
	}

}
