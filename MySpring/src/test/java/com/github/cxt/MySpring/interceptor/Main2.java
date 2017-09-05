package com.github.cxt.MySpring.interceptor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.cxt.MySpring.schema.People;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Main2.Configurator.class})
public class Main2 {
	
	@Autowired
	private Server server;

	@Test
	public void test0() {
		server.test1();
		server.test2();
		People p = new People();
		p.setName("tes111t");
		server.test3(p);
		System.out.println(server);
	}
	
	//使用@EnableAspectJAutoProxy,或AnnotationAwareAspectJAutoProxyCreator
	@EnableAspectJAutoProxy
	static class Configurator{
		
		@Bean
		public Server server(){
			return new ServerImpl();
		}
		
		@Bean
		MyMethodInterceptorAnnoation myInterceptor(){
			return new MyMethodInterceptorAnnoation();
		}

//		@Bean
//		AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator(){
//			return new AnnotationAwareAspectJAutoProxyCreator();
//		}
	}
}
