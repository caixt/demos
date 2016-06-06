package com.github.cxt.MyMock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;
import javax.annotation.Resource;  


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Beans.Context.class)
public class MockTest2 {
	
    @Autowired
	private DemoService demoService;

	@Autowired
	private DemoController controller;
	
	 @Before  
     public void setUp() {  
         MockitoAnnotations.initMocks(this);  
     }  

	@Test
	public void testA(){
		
		
		when(demoService.doSth()).thenReturn("aaaa");
		System.out.println(controller.test1());
		
		System.out.println(A.doA("aaa"));
	}
}
