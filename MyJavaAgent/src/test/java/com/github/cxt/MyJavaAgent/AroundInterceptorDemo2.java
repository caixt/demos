package com.github.cxt.MyJavaAgent;

public class AroundInterceptorDemo2 implements AroundInterceptor {

	@Override
	public void before(Object target, Object[] args) {
		System.out.println("!11");
		
	}

	@Override
	public void after(Object target, Object[] args, Object result, Throwable throwable) {
		System.out.println("!22");
		
	}

}
