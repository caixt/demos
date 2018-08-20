package com.github.cxt.MyJavaAgent.injector.base;

import com.github.cxt.MyJavaAgent.injector.CallInjector;

public abstract class SpanCallInjector implements CallInjector{
	
	
	public SpanCallInjector(){
		setInitAndStartString("");
	}

	protected final String DEFAULT_NAME = "CALL";

	private String methodCallBefore = null;
    
	private String methodCallAfter  
	    = " _$tracer.success();\n"
        + "  }catch(Exception _$e){ \n"
        + "    _$tracer.fail();\n"
        + "    throw _$e;  \n"
        + "  }";
                            
	private String name = DEFAULT_NAME;
	    
    protected void setName(String name) {
    	this.name = name;
    }
    
    protected void setInitAndStartString(String initAndStart){
    	String str 
	    	= "  com.github.cxt.MyJavaAgent.tracespan.Tracer _$tracer; \n"
	        + "  com.github.cxt.MyJavaAgent.tracespan.Span _$span ; \n"
	        + "  _$tracer = com.github.cxt.MyJavaAgent.tracespan.Tracer.getTracer(); \n"
	        + "  String _$name = \"%1$s\", _$desc =\"%2$s\";\n"
	        + initAndStart
	        + "  _$tracer.begin(_$name, _$desc);\n"
	        + "  try{ \n";
    	this.methodCallBefore = str;
    }
    
    
    @Override
    public  String getMethodCallBefore(String className, String methodName){
        return String.format(methodCallBefore, name, className + "." + methodName);
    }
    
    @Override
    public  String getMethodCallAfter(String className, String methodName){
        return methodCallAfter;
    }    
}