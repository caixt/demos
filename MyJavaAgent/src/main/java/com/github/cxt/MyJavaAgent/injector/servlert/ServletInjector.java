package com.github.cxt.MyJavaAgent.injector.servlert;

import com.github.cxt.MyJavaAgent.injector.Method;
import com.github.cxt.MyJavaAgent.injector.base.AbsMethodCallInjector;

public class ServletInjector extends AbsMethodCallInjector {
	
	private static final String classnMame = "javax.servlet.http.HttpServlet";
	private static final String methodName = "service";
	private static final String methodDefine = "(Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;)V";

	public boolean isNeedProcessInject(String className, Method method) {
		return ServletInjector.classnMame.equals(className) && ServletInjector.methodName.equals(method.getName()) && ServletInjector.methodDefine.equals(method.getDescriptor());
	}

	protected final static String NAME = "SERVLET";
	
	public ServletInjector(){
		String str = "if($args.length>0 && $args[0]!=null && _$tracer.getAttachmentValue($args[0])==null){\n"
				+ "  javax.servlet.http.HttpServletRequest request = (javax.servlet.http.HttpServletRequest)$args[0];"
				+ "  _$desc=\"url:\" + request.getRequestURL().toString() + \",method:\" + request.getMethod();\n"
				+ "  _$tracer.addAttachment($args[0], _$desc);"
				+ "}";
		super.setInitAndStartString(str);
		
	}

	@Override
	public boolean isNeedCallInject(String callClassName, Method callMethod) {
		return ServletInjector.classnMame.equals(callClassName) && ServletInjector.methodName.equals(callMethod.getName());
	}

	@Override
	public String getMethodCallBefore(String callClassName, Method callMethod) {
		return String.format(methodCallBefore, NAME, "\"+ this.getClass() + \"" );
	}
}
