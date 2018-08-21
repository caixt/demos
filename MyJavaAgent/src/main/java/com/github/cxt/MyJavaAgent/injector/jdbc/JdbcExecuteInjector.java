package com.github.cxt.MyJavaAgent.injector.jdbc;

import com.github.cxt.MyJavaAgent.injector.Method;
import com.github.cxt.MyJavaAgent.injector.base.SpanCallInjector;

public class JdbcExecuteInjector extends SpanCallInjector {


	protected final static String NAME = "JDBC";
	
	public JdbcExecuteInjector(){
		super.name = NAME;
		String str = "if($args.length>0 && $args[0]!=null)_$desc=$args[0].toString();\n"
				+ "else { \n"
				+ "  Object _$v = _$tracer.getAttachmentValue($0);\n"
				+ "  if(_$v != null){_$desc=(String)_$v;}"
				+ "}";
		super.setInitAndStartString(str);
		
	}
	
	public boolean isNeedInject(String callClassName) {
		return "java.sql.Statement".equals(callClassName) || "java.sql.PreparedStatement".equals(callClassName) 
				|| "java.sql.CallableStatement".equals(callClassName);
	}
	
	@Override
	public boolean isNeedCallInject(String callClassName, Method method){
		String callMethodName = method.getName();
		return isNeedInject(callClassName) && 
			("execute".equals(callMethodName) || "executeQuery".equals(callMethodName) 
						|| "executeUpdate".equals(callMethodName) || "executeBatch".equals(callMethodName));
	}
}
