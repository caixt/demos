package com.github.cxt.MyMock;

import mockit.Mock;
import mockit.MockUp;
import mockit.NonStrictExpectations;
import org.junit.Assert;
import org.junit.Test;


/**
 * 兼容jacoco
 * @author caixt@broada.com
 * @Description:
 * @date 2016年7月13日
 */
public class JmockitMockTest {

	@Test
	public void test1() {
		new NonStrictExpectations(DemoStatic.class) {
			{
				DemoStatic.test(1);
				returns(123);
			}
		};
		Assert.assertEquals(123, DemoStatic.test(1));
	}
	
	@Test
	public void test2() {
		new MockUp<DemoStatic>() {
			@Mock
			public int test(int s){
				return s * 3;
			}
		};
		Assert.assertEquals(3, DemoStatic.test(1));
	}
}
