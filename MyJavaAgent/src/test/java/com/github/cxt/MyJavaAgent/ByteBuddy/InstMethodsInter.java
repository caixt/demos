package com.github.cxt.MyJavaAgent.ByteBuddy;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import com.github.cxt.MyJavaAgent.AroundInterceptor;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

public class InstMethodsInter {
	
	private AroundInterceptor aroundInterceptor;
	
	public InstMethodsInter(AroundInterceptor aroundInterceptor){
		this.aroundInterceptor = aroundInterceptor;
	}

	@RuntimeType
	public Object intercept(@This Object obj,
	        @AllArguments Object[] allArguments,
	        @SuperCall Callable<?> zuper,
	        @Origin Method method) throws Exception {
	   try {
		   aroundInterceptor.before(obj, allArguments);
        } catch (Throwable t) {
            t.printStackTrace();
        }
	    try {
	        Object result = zuper.call();
	        aroundInterceptor.after(obj, allArguments, result, null);
	        return result;
	    } catch (Throwable t) {
	    	aroundInterceptor.after(obj, allArguments, null, t);
	    	throw t;
	    }
	}
}
