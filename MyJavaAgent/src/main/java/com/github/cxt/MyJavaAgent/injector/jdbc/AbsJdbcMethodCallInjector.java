package com.github.cxt.MyJavaAgent.injector.jdbc;

import com.github.cxt.MyJavaAgent.injector.Method;
import com.github.cxt.MyJavaAgent.injector.base.AbsMethodCallInjector;

public abstract class AbsJdbcMethodCallInjector extends AbsMethodCallInjector{

	//驱动的内部实现调用的需要过滤点。
	//先过滤mysql的
	@Override
	public boolean isNeedProcessInject(String className, Method method) {
		return !className.startsWith("com.mysql.jdbc");
	}
}
