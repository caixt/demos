package com.github.cxt.MyJavaAgent.injector.jdbc;

import com.github.cxt.MyJavaAgent.injector.Method;

public class JdbcStatementInjector extends AbsJdbcMethodCallInjector {
	
	protected  String methodCallBefore 
		    = "  com.github.cxt.MyJavaAgent.tracespan.Tracer _$tracer; \n"
		    + "  com.github.cxt.MyJavaAgent.tracespan.Span _$span ; \n"
		    + " _$tracer = com.github.cxt.MyJavaAgent.tracespan.Tracer.getTracer(); \n";
	
	protected   String methodCallAfter  
		    = "  if($args.length>0 && $args[0]!=null) _$tracer.addAttachment($_, $args[0].toString());"; 


	@Override
	public String getMethodCallBefore(String callClassName, Method callMethod) {
        return methodCallBefore;
	}

	@Override
	public String getMethodCallAfter(String callClassName, Method callMethod) {
		return methodCallAfter;
	}
	
	public boolean isNeedInject(String callClassName) {
		return "java.sql.Connection".equals(callClassName);
	}
	
	@Override
	public boolean isNeedCallInject(String callClassName, Method callMethod){
		String callMethodName = callMethod.getName();
		return isNeedInject(callClassName) && 
				("prepareStatement".equals(callMethodName) || "prepareCall".equals(callMethodName));
	}
}
