package com.github.cxt.MyMock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MockTest.Context.class)
public class MockTest {
	
    @Autowired
	private DemoService demoService;

	@Autowired
	private DemoController controller;
	

	@Test
	public void testA(){
		System.out.println(controller.test1());
	}
	
	
	@ImportResource("classpath:/test-spring-context.xml")
	public static class Context {
		
		@Bean
		DemoService demoService(){
			DemoService demoService = mock(DemoService.class);
			when(demoService.doSth()).thenReturn("aaaa");
			return demoService;		
		}
	}
}
