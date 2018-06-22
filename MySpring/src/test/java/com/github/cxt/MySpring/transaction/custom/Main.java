package com.github.cxt.MySpring.transaction.custom;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Main.Context.class })
public class Main {
	
	@Autowired
	private CustomDemoInterface customDemo;
	
	@Test
	public void test(){
		System.out.println(customDemo.getClass());
		customDemo.test1();
		customDemo.test2();
		customDemo.test3();
		customDemo.test4();
	}

	
	@EnableCustomManagement
	public static class Context{
	    
		@Bean
		CustomDemoInterface CustomDemoInterface(){
			return new CustomDemoImpl();
		}
		
	}
}
