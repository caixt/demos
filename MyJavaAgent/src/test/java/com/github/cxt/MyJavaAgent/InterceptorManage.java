package com.github.cxt.MyJavaAgent;

import java.util.concurrent.atomic.AtomicInteger;

public final class InterceptorManage {

    private final static int DEFAULT_MAX = 8192;
    private final static AtomicInteger id = new AtomicInteger(0);
    private final static WeakAtomicReferenceArray<AroundInterceptor> index = new WeakAtomicReferenceArray<AroundInterceptor>(DEFAULT_MAX, AroundInterceptor.class);

    private InterceptorManage() {
    }
	
	public static int add(AroundInterceptor interceptor){
		if(interceptor == null){
			throw new NullPointerException();
		}
		int newId = id.getAndIncrement();
		index.set(newId, interceptor);
		return newId;
	}
	
	public static AroundInterceptor get(int id){
		return index.get(id);
	}
}
