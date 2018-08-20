package com.github.cxt.MyJavaAgent.injector.jdbc;

import com.github.cxt.MyJavaAgent.injector.base.SpanCallInjector;

public class JdbcExecuteInjector extends SpanCallInjector {


	protected final static String NAME = "JDBC";
	
	public JdbcExecuteInjector(){
		super.setName(NAME);
		String str = "if($args.length>0 && $args[0]!=null)_$desc=$args[0].toString();\n"
				+ "else { \n"
				+ "  Object _$v = _$tracer.getAttachmentValue($0);\n"
				+ "  if(_$v != null){_$desc=(String)_$v;}"
				+ "}";
		super.setInitAndStartString(str);
		
	}
	
	public boolean isNeedInject(String className) {
		return "java.sql.Statement".equals(className) || "java.sql.PreparedStatement".equals(className) 
				|| "java.sql.CallableStatement".equals(className);
	}
	
	@Override
	public boolean isNeedCallInject(String className, String methodName){
		return isNeedInject(className) && 
			("execute".equals(methodName) || "executeQuery".equals(methodName) 
						|| "executeUpdate".equals(methodName) || "executeBatch".equals(methodName));
	}
}
