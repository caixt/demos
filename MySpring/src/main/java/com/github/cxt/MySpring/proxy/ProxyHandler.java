package com.github.cxt.MySpring.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class ProxyHandler implements InvocationHandler{
	private Object proxied;

	public ProxyHandler(Object proxied) {
		this.proxied = proxied;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// 在转调具体目标对象之前，可以执行一些功能处理

		// 转调具体目标对象的方法
		return method.invoke(proxied, args);

		// 在转调具体目标对象之后，可以执行一些功能处理
	}
}
