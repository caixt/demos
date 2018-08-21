package com.github.cxt.MyJavaAgent.injector.base;

import com.github.cxt.MyJavaAgent.injector.Method;

public class CustomMethodInjector extends SpanMethodInjector{
	
	protected final static String NAME = "CUSTOM";
	
	private String className;
	
	private String methodName;
	
    public CustomMethodInjector(String className, String methodName) {
		super();
		this.className = className;
		this.methodName = methodName;
		super.setName(NAME);
	}

	@Override
	public boolean isNeedProcessInject(String className, Method method) {
		return this.className.equals(className) && this.methodName.equals(method.getName());
	}
}
