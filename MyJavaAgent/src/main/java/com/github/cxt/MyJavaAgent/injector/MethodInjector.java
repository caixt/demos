package com.github.cxt.MyJavaAgent.injector;


public interface MethodInjector {
	
	public boolean isNeedProcessInject(String className, String methodName);
	public String[][] getMethodVariables(String className, String methodName);
	public String getMethodProcessStart(String className, String methodName);
	public String getMethodProcessReturn(String className, String methodName);
	public String getMethodProcessException(String className, String methodName);
	public String getMethodProcessFinally(String className, String methodName);
}