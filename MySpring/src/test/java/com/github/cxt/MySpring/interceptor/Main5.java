package com.github.cxt.MySpring.interceptor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Main5.Configurator.class})
public class Main5 {
	
	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void test0() {
		System.out.println("!");
		System.out.println(applicationContext.getBean("server"));
	}
	
	
	static class Configurator{
		
		@Bean
		@Scope("prototype")
		public Server server(){
			return new ServerImpl();
		}
		
		
		@Bean
		BeanPostProcessor beanPostProcessor1(){
			return new BeanPostPrcessorImpl();
		}
		
		@Bean
		BeanPostProcessor beanPostProcessor2(){
			return new BeanPostPrcessorImpl();
		}
	}
}
