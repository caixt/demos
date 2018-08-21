package com.github.cxt.MyJavaAgent.injector;

public interface CallInjector {
	
	
	public boolean isNeedCallInject(String callClassName, Method callMethod);
	public String getMethodCallBefore(String callClassName, Method callMethod);
	public String getMethodCallAfter(String callClassName, Method callMethod);
	
}