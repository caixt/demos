package com.github.cxt.MyJavaAgent;

public class AroundInterceptorDemo1 implements AroundInterceptor {

	@Override
	public void before(Object target, Object[] args) {
		System.out.println("!");
		
	}

	@Override
	public void after(Object target, Object[] args, Object result, Throwable throwable) {
		System.out.println("2");
		
	}

}
