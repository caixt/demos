package com.github.cxt.MySpring.proxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.github.cxt.MySpring.transaction.custom.EnableCustomManagement;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Main.Context.class })
public class Main {
	
	@Autowired
	private Subject subject;
	
	@Test
	public void test(){
		System.out.println(subject.getClass());
		subject.doSomething();
	}

	
	@EnableCustomManagement(proxyTargetClass=true)
	public static class Context{
	    
		@Bean
		Subject subject(){
			return new RealSubject();
		}
		
	}
}
