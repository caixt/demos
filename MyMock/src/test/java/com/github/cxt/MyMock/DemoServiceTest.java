package com.github.cxt.MyMock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class DemoServiceTest {

	@Test
	public void test1(){
		DemoService demoService = mock(DemoService.class);
		when(demoService.doSth1(Mockito.eq("aaa"), Mockito.anyInt())).thenReturn("result");
		Assert.assertEquals("result", demoService.doSth1("aaa", 1));
		verify(demoService, Mockito.atLeast(1)).doSth1("aaa", 1);
	}
	
	@Test
	public void test2(){
		DemoService demoService = mock(DemoService.class);
		when(demoService.doSth2(Mockito.anyString(), Mockito.anyString())).then(new Answer<String>() {

			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				String a0 = invocation.getArgumentAt(0, String.class);
				String a1 = invocation.getArgumentAt(1, String.class);
				return a0 + a1;
			}
		});
		Assert.assertEquals("aaabbb", demoService.doSth2("aaa", "bbb"));
	}
}
