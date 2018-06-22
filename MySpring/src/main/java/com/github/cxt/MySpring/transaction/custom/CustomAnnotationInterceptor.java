package com.github.cxt.MySpring.transaction.custom;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;

public class CustomAnnotationInterceptor implements MethodInterceptor{
	
	public static ThreadLocal<String> localInfo = new ThreadLocal<>();
	
	private CustomAttributeSource customAttributeSource;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
		return invokeWithinTransaction(invocation.getMethod(), targetClass, new InvocationCallback() {
			@Override
			public Object proceedWithInvocation() throws Throwable {
				return invocation.proceed();
			}
		});
	}
	
	public static String getInfo(){
		return localInfo.get();
	}
	
	
	private Object invokeWithinTransaction(Method method, Class<?> targetClass, InvocationCallback invocationCallback) throws Throwable {
		AnnotationCustomAttributeSource attr = customAttributeSource.getAttribute(method, targetClass);
		localInfo.set(attr.getName());
		try{
			return invocationCallback.proceedWithInvocation();
		}finally{
			localInfo.remove();
		}
	}
	

	protected interface InvocationCallback {
		Object proceedWithInvocation() throws Throwable;
	}


	public void setCustomAttributeSource(CustomAttributeSource customAttributeSource) {
		this.customAttributeSource = customAttributeSource;
	}
}
