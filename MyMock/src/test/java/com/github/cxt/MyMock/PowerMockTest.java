package com.github.cxt.MyMock;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * 不兼容jacoco
 * @author caixt@broada.com
 * @Description:
 * @date 2016年7月13日
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class )
@ContextConfiguration(classes = PowerMockTest.Context.class)
@PrepareForTest({ DemoStatic.class})
public class PowerMockTest {
	
    @Autowired
	private DemoService demoService;

	@Autowired
	private DemoController controller;
	

	@Test
	public void testA() {
		PowerMockito.mockStatic(DemoStatic.class);
		PowerMockito.when(DemoStatic.test()).thenReturn(123);
		Assert.assertEquals(123, DemoStatic.test());
		Assert.assertEquals("aaaa", controller.test1());
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
