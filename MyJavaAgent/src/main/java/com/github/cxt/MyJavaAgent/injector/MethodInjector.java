package com.github.cxt.MyJavaAgent.injector;

public interface MethodInjector {
	
	public boolean isNeedProcessInject(String className, Method method);
	public String[][] getMethodVariables(String className, Method method);
	public String getMethodProcessStart(String className, Method method);
	public String getMethodProcessReturn(String className, Method method);
	public String getMethodProcessException(String className, Method method);
	public String getMethodProcessFinally(String className, Method method);
}