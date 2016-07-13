package com.github.cxt.MyMock;

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
	public void testA() {
		new NonStrictExpectations(DemoStatic.class) {
			{
				DemoStatic.test();
				returns(123);
			}
		};
		Assert.assertEquals(123, DemoStatic.test());
	}
}
