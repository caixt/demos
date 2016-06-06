package com.github.cxt.MyMock;


import org.springframework.context.annotation.Bean;
import static org.mockito.Mockito.mock;

public class Beans {
	
	//@ImportResource("classpath:/test-spring-context.xml")
	public static class Context {
		
		@Bean
		DemoController DemoController() {
			return new DemoControllerImpl();
		}
		
		
		@Bean
		DemoService demoService(){
			return mock(DemoService.class);		
		}
	}
}
