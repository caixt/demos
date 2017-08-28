package com.github.cxt.MySpring.interceptor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

public class MyMethodInterceptor implements MethodInterceptor{

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		System.out.println("??????????????????");
		Method method = invocation.getThis().getClass().getDeclaredMethod(invocation.getMethod().getName(), 
				invocation.getMethod().getParameterTypes());
		
        if(method.isAnnotationPresent(TestMethod.class)){  
            TestMethod testMethod = method.getAnnotation(TestMethod.class);
            String desc = testMethod.desc();
            
            Parameter[] parameter =  method.getParameters();
            for(int i = 0; i < parameter.length; i++){
            	Parameter p = parameter[i];
            	System.out.println(p.getName() + "!" + invocation.getArguments()[i]);
            }
            System.out.println(desc);
        }
		return invocation.proceed();
	}
	
	
    public void after(JoinPoint joinPoint) throws NoSuchMethodException {
    	System.out.println("--------------------------------");
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();// 代理对象执行的方法
        Method realMethod = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(), targetMethod.getParameterTypes());
        
        System.out.println(joinPoint.getTarget().getClass().getSimpleName());
        System.out.println(ServerImpl.class.getName());
        
        
        if (realMethod.isAnnotationPresent(TestMethod.class)) {
        	TestMethod annotation = realMethod.getAnnotation(TestMethod.class);
            System.out.println(annotation.desc());
            
            Parameter[] parameter =  realMethod.getParameters();
            for(int i = 0; i < parameter.length; i++){
            	Parameter p = parameter[i];
            	
            	System.out.println(p.getName() + "!" + joinPoint.getArgs()[0]);
            }
        }
    }

}
