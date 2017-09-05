package com.github.cxt.MySpring.interceptor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class MyMethodInterceptorAnnoation {


	
	@Pointcut("execution(public * com.github.cxt.MySpring.interceptor.*.*(..))")  
	private void anyMethod(){}//定义一个切入点
	 
	@After(value = "anyMethod()")
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
