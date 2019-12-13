package com.github.cxt.MySpring.base;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;


public class DemoBean implements InitializingBean, DisposableBean{

	@PostConstruct
	public void postConstructInit(){
		System.out.println("PostConstruct init...");
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("InitializingBean afterPropertiesSet...");
	}
	
	public void init(){
		System.out.println("bean init...");
	}
	
	@PreDestroy
	public void preDestroy(){
		System.out.println("preDestroy destroy...");
	}
	
	@Override
	public void destroy(){
		System.out.println("disposableBean destroy...");
	}
	
	public void destroyX(){
		System.out.println("bean destroy...");
	}
}
