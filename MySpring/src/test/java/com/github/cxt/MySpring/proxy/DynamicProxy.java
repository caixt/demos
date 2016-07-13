package com.github.cxt.MySpring.proxy;

import java.lang.reflect.Proxy;

import org.junit.Test;

public class DynamicProxy {
	
	@Test
	public void test1() {
		RealSubject real = new RealSubject();
		Subject proxySubject = (Subject) Proxy.newProxyInstance(Subject.class.getClassLoader(),
				new Class[] { Subject.class }, new ProxyHandler(real));

		proxySubject.doSomething();
	}

}
