package com.github.cxt.MyJavaAgent.injector.base;

import com.github.cxt.MyJavaAgent.injector.Method;
import com.github.cxt.MyJavaAgent.injector.MethodCallInjector;

public abstract class AbsMethodCallInjector extends SpanCallInjector implements MethodCallInjector{

	@Override
	public String[][] getMethodVariables(String className, Method method) {
		return null;
	}

	@Override
	public String getMethodProcessStart(String className, Method method) {
		return null;
	}

	@Override
	public String getMethodProcessReturn(String className, Method method) {
		return null;
	}

	@Override
	public String getMethodProcessException(String className, Method method) {
		return null;
	}

	@Override
	public String getMethodProcessFinally(String className, Method method) {
		return null;
	}
}
