package com.github.cxt.MySpring.transaction.custom;

import java.lang.reflect.Method;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

public class ProxyCustomStaticMethodMatcherPointcut extends StaticMethodMatcherPointcut{
	
	private CustomAttributeSource customAttributeSource;

	@Override
	public boolean matches(Method method, Class<?> targetClass) {
		return customAttributeSource.getAttribute(method, targetClass) != null;
	}

	public void setCustomAttributeSource(CustomAttributeSource customAttributeSource) {
		this.customAttributeSource = customAttributeSource;
	}
}
