package com.github.cxt.MySpring.interceptor;

import java.util.HashMap;
import java.util.Map;


public class Holder {
	
	private final Map<Class<?>, ProxyFactory<?>> knownMappers = new HashMap<Class<?>, ProxyFactory<?>>();
	
	
	public <T> void add(Class<T> type){
		 knownMappers.put(type, new ProxyFactory<T>(type));
	}
	
	public <T> T getTarget(Class<T> type){
		return (T) knownMappers.get(type).newInstance();
	}
	
}
