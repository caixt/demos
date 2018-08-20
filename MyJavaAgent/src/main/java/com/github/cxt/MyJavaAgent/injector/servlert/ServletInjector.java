package com.github.cxt.MyJavaAgent.injector.servlert;

import com.github.cxt.MyJavaAgent.injector.OverrideAccept;
import com.github.cxt.MyJavaAgent.injector.base.CustomMethodInjector;

public class ServletInjector extends CustomMethodInjector implements OverrideAccept{
	
	private static final String classnMame = "javax.servlet.http.HttpServlet";
	private static final String methodName = "service";
	private static final String methodDefine = "(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;)V";


	public ServletInjector() {
		super(classnMame, methodName);
		super.setName("SERVLET");
		
		String str = "if(_$tracer.getAttachmentValue($args[0])==null){ \n"
				+ "  javax.servlet.http.HttpServletRequest request = (javax.servlet.http.HttpServletRequest)$args[0];\n"
				+ "  _$desc=\"url:\" + request.getRequestURL().toString() + \",method:\" + request.getMethod();\n"
				+ "   _$tracer.addAttachment($args[0], _$desc);\n"
				+ "}";
		super.setBeforeStartString(str);
	}


	@Override
	public boolean accept(String methodDefine) {
		boolean b = ServletInjector.methodDefine.equals(methodDefine);
		if(b){
			System.out.println("!");
		}
		return b;
	}
	
	
	
}
