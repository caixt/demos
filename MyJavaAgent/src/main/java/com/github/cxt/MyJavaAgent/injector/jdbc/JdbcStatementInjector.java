package com.github.cxt.MyJavaAgent.injector.jdbc;

import com.github.cxt.MyJavaAgent.injector.CallInjector;

public class JdbcStatementInjector implements CallInjector {
	
	protected  String methodCallBefore 
		    = "  com.github.cxt.MyJavaAgent.tracespan.Tracer _$tracer; \n"
		    + "  com.github.cxt.MyJavaAgent.tracespan.Span _$span ; \n"
		    + " _$tracer = com.github.cxt.MyJavaAgent.tracespan.Tracer.getTracer(); \n";
	
	protected   String methodCallAfter  
		    = "  if($args.length>0 && $args[0]!=null) _$tracer.addAttachment($_, $args[0].toString());"; 


	@Override
	public String getMethodCallBefore(String className, String methodName) {
        return methodCallBefore;
	}

	@Override
	public String getMethodCallAfter(String className, String methodName) {
		return methodCallAfter;
	}
	
	public boolean isNeedInject(String className) {
		return "java.sql.Connection".equals(className);
	}
	
	@Override
	public boolean isNeedCallInject(String className, String methodName){
		return isNeedInject(className) && 
				("prepareStatement".equals(methodName) || "prepareCall".equals(methodName));
	}
	
	

}
