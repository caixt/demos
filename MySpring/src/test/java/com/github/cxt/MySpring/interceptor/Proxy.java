package com.github.cxt.MySpring.interceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Proxy<T> implements InvocationHandler {

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (Object.class.equals(method.getDeclaringClass())) {
			return method.invoke(this, args);
		}
		System.out.println("method:" + method);
		return null;
	}

}
