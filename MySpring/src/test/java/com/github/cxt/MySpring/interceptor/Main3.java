package com.github.cxt.MySpring.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.cxt.MySpring.schema.People;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Main3.Configurator.class})
public class Main3 {
	
	@Autowired
	private Server server;

	@Test
	public void test0() {
		server.test1();
		server.test2();
		People p = new People();
		p.setName("tes111t");
		server.test3(p);
		System.out.println("!!!!!!!!!!!!!");
	}
	
	
	static class Configurator{
		
		@Bean
		public Server server(){
			return new ServerImpl();
		}
		
		@Bean
		MethodInterceptor myInterceptor(){
			return new MyMethodInterceptor();
		}
		
		@Bean
		AspectJExpressionPointcut testPoint(){
			AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
			pointcut.setExpression("execution(public * com.github.cxt.MySpring.interceptor.*.*(..))");
			return pointcut;
		}
		
		@Bean
		DefaultBeanFactoryPointcutAdvisor advisor(AspectJExpressionPointcut pointcut, MethodInterceptor methodInterceptor){
			DefaultBeanFactoryPointcutAdvisor advisor = new DefaultBeanFactoryPointcutAdvisor();
			advisor.setPointcut(pointcut);
			advisor.setAdvice(methodInterceptor);
			return advisor;
		}

		@Bean
		AspectJAwareAdvisorAutoProxyCreator aspectJAwareAdvisorAutoProxyCreator(){
			return new AspectJAwareAdvisorAutoProxyCreator();
		}
	}
}
