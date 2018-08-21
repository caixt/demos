package com.github.cxt.MyJavaAgent.injector.base;

import com.github.cxt.MyJavaAgent.injector.Method;
import com.github.cxt.MyJavaAgent.injector.MethodInjector;


public abstract class SpanMethodInjector implements MethodInjector {
	
	protected final String DEFAULT_NAME = "CALL";
	
	private String name = DEFAULT_NAME;
    
	public SpanMethodInjector(){
		setBeforeStartString("");
	}
	
    protected void setName(String name) {
    	this.name = name;
    }
	
	private String methodProcessStart = null;
                                             
	private String methodProcessReturn
	    = "  { \n"
	    + "    _$tracer.success();\n"
	    + "  } \n";
	                                                
	//异常获取不到_$tracer
	private String methodProcessException
	    = "  { \n"
	    + "    com.github.cxt.MyJavaAgent.tracespan.Tracer.getTracer().fail();\n"
	    + "    throw _$e;  \n"
	    + "  } \n";
	                                                
	private String methodProcessFinally  = "";
	
	
    protected void setBeforeStartString(String  beforeStart){
    	String str 
    	    = "  { \n"
		    + "    _$tracer = com.github.cxt.MyJavaAgent.tracespan.Tracer.getTracer(); \n"
		    + "    _$name = \"%1$s\"; \n"
		    + "    _$desc =\"%2$s\";\n"
		    +  beforeStart
	        + "    _$tracer.begin(_$name, _$desc);\n"
		    + "  } \n";
    	this.methodProcessStart = str;
    }
	
	
	
	@Override
	public String[][] getMethodVariables(String className, Method method) {
		return new String[][]{
			{"com.github.cxt.MyJavaAgent.tracespan.Tracer",  "_$tracer"}, 
			{"java.lang.String", "_$name"}, 
			{"java.lang.String", "_$desc"}
		};
	}

	@Override
	public String getMethodProcessStart(String className, Method method) {
		return String.format(methodProcessStart, name, className + "." + method.getName());
	}

	@Override
	public String getMethodProcessReturn(String className, Method method) {
		return methodProcessReturn;
	}

	@Override
	public String getMethodProcessException(String className, Method method) {
		return methodProcessException;
	}

	@Override
	public String getMethodProcessFinally(String className, Method method) {
		return methodProcessFinally;
	}
}
