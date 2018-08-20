package com.github.cxt.MyJavaAgent;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

import com.github.cxt.MyJavaAgent.tracespan.Span;
import com.github.cxt.MyJavaAgent.tracespan.Tracer;

public class AgentClassLoad extends URLClassLoader {
	
	private static final List<String> EXCLUDE = Arrays.asList(LogTraceConfig.class.getName(), 
			Span.class.getName(), Tracer.class.getName()); 
	

	public AgentClassLoad(URL... urls) {
		super(urls);
	}

	@Override
	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		Class<?> clazz = null;
		if(!EXCLUDE.contains(name)){
			try{
				clazz = findClass(name);
			}catch(ClassNotFoundException ignore){}
		}
		
		if(clazz == null){
			clazz = Class.forName(name, false, getParent());
		}
		if(clazz != null){
			if(resolve){
				resolveClass(clazz);
			}
			return clazz;
		}
		throw new ClassNotFoundException();
	}
}
