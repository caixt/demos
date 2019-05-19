package com.github.cxt.MySpring.interceptor;

import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class Main6 {

	
	@Test
	public void test(){
		ApplicationContext context = new AnnotationConfigApplicationContext(Main6.Content.class);
		Server demo = context.getBean(Server.class);
		demo.test1();
	}
	
	
	public static class Content{
		@Bean
		Holder holder(){
			Holder holder = new Holder();
			holder.add(Server.class);
			return holder;
		}
		
		@Bean
		BeanDefinitionRegistryPostProcessor beanDefinitionRegistryPostProcessor(){
			return new BeanDefinitionRegistryPostProcessorImpl();
		}
	}
}
