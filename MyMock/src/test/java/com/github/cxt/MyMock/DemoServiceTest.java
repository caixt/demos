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
		
		Mockito.doThrow(new RuntimeException()).when(demoService).pluckFruits();
		try{
			demoService.pluckFruits();
		}catch(Exception e ){
			System.out.println("!");
		}
	}
	
	@Test
	public void test2(){
		DemoService demoService = mock(DemoService.class);
		when(demoService.doEntity(Mockito.anyString())).then(new Answer<Entity>() {

			@Override
			public Entity answer(InvocationOnMock invocation) throws Throwable {
				String id = invocation.getArgumentAt(0, String.class);
				Entity entity = mock(Entity.class);
				when(entity.getId()).thenReturn(id);
				when(entity.getName()).thenReturn("name");
				return entity;
			}
		});
		Entity entity = demoService.doEntity("id");
		Assert.assertEquals("id", entity.getId());
		Assert.assertEquals("name", entity.getName());
	}
}
