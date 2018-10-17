package com.github.cxt.MyJavaAgent.ByteBuddy;

import static net.bytebuddy.matcher.ElementMatchers.named;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import org.junit.Test;

import com.github.cxt.MyJavaAgent.AroundInterceptorDemo1;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.jar.asm.Opcodes;

public class Main {

	@Test
	public void test() throws Exception {
		DynamicType.Unloaded<?> dynamicType = new ByteBuddy().with(new NamingStrategy.AbstractBase() {
			protected String name(TypeDescription typeDescription) {
				return "i.love.ByteBuddy." + typeDescription.getSimpleName();
			}

		}).subclass(Object.class).make();
		
		System.out.println(dynamicType.getTypeDescription().getCanonicalName());
		System.out.println(dynamicType.load(ClassLoader.getSystemClassLoader()).getLoaded().newInstance());
		
		
		MemoryDatabase loggingDatabase = new ByteBuddy()
				  .subclass(MemoryDatabase.class)
				  .method(named("load")).intercept(MethodDelegation.to(InterceptorUtil.class).andThen(SuperMethodCall.INSTANCE))
				  .make()
				  .load(ClassLoader.getSystemClassLoader())
				  .getLoaded()
				  .newInstance();
		System.out.println(loggingDatabase.load("aaa"));
	}
	
	
	@Test
	public void test2() throws Exception {
		String CONTEXT_ATTR_NAME = "$abcd";
		EnhancedInstance x = (EnhancedInstance) new ByteBuddy().with(new NamingStrategy.AbstractBase() {
			protected String name(TypeDescription typeDescription) {
				return "i.love.ByteBuddy." + typeDescription.getSimpleName();
			}

		}).subclass(Object.class)
		.defineField(CONTEXT_ATTR_NAME, Object.class, Opcodes.ACC_PRIVATE)
	    .implement(EnhancedInstance.class)
	    .intercept(FieldAccessor.ofField(CONTEXT_ATTR_NAME))
	    .make()
	    .load(ClassLoader.getSystemClassLoader())
		.getLoaded()
		.newInstance();
		x.setTestDynamicField(1);
		System.out.println(x.getTestDynamicField());
	}
	
	@Test
	public void test3() throws Exception {
		MemoryDatabase loggingDatabase = new ByteBuddy()
				  .subclass(MemoryDatabase.class)
				  .method(named("load")).intercept(MethodDelegation.withDefaultConfiguration().to(new InstMethodsInter(new AroundInterceptorDemo1())))
				  .make()
				  .load(ClassLoader.getSystemClassLoader())
				  .getLoaded()
				  .newInstance();
		System.out.println(loggingDatabase.load("aaa"));
	}
}


class InterceptorUtil {
	
	@RuntimeType
	public static Object intercept(@This Object obj,
	        @AllArguments Object[] allArguments,
	        @SuperCall Callable<?> zuper,
	        @Origin Method method) throws Exception {
	    long start = System.currentTimeMillis();
	    try {
	        // 原有函数执行
	        return zuper.call();
	    } finally {
	        System.out.println(method + ": took " + (System.currentTimeMillis() - start) + "ms");
	    }
	}
}
