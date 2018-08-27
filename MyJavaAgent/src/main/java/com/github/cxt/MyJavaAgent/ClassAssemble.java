package com.github.cxt.MyJavaAgent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.github.cxt.MyJavaAgent.injector.CallInjector;
import com.github.cxt.MyJavaAgent.injector.Method;
import com.github.cxt.MyJavaAgent.injector.MethodCallInjector;
import com.github.cxt.MyJavaAgent.injector.MethodInjector;
import com.github.cxt.MyJavaAgent.injector.base.CustomMethodInjector;
import com.github.cxt.MyJavaAgent.injector.jdbc.JdbcExecuteInjector;
import com.github.cxt.MyJavaAgent.injector.jdbc.JdbcStatementInjector;
import com.github.cxt.MyJavaAgent.injector.servlert.ServletInjector;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.MethodInfo;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class ClassAssemble {
	
	public ClassAssemble(LogTraceConfig config){
		initInjectors(config);
	}
	
	
	private Set<CallInjector> callInjectors = new HashSet<CallInjector>();
	private Set<MethodInjector> methodInjectors = new HashSet<MethodInjector>();
	private Set<MethodCallInjector> methodCallInjectors = new HashSet<MethodCallInjector>();
	
	
	private void initInjectors(LogTraceConfig config) {
		if(config.isEnableJdbcTrace()){
			JdbcStatementInjector jdbcStmtInjector = new JdbcStatementInjector();
			JdbcExecuteInjector jdbcExeInjector = new JdbcExecuteInjector();
			methodCallInjectors.add(jdbcStmtInjector);
			methodCallInjectors.add(jdbcExeInjector);
		}
		if(config.isEnableServletTrace()){
			ServletInjector servletInjector = new ServletInjector();
			methodCallInjectors.add(servletInjector);
		}
		List<String> customMethods = config.getCustomMethods();
		if(customMethods.size() > 0){
			CustomMethodInjector customMethodInjector = null;
			for(int i = 0; i< customMethods.size(); i++){
				String str = customMethods.get(i);
				int index = str.lastIndexOf(".");
				String className = str.substring(0, index);
				String methodName = str.substring(index + 1);
				index = methodName.lastIndexOf("#");
				if(index > 0){
					String name = methodName.substring(index + 1);
					methodName = methodName.substring(0, index);
					customMethodInjector = new CustomMethodInjector(className, methodName, name);
				}
				else{
					customMethodInjector = new CustomMethodInjector(className, methodName, i);
				}
				methodInjectors.add(customMethodInjector);
			}
		}
	}

	public byte[] assembleClass(String className, InputStream inputStream){
    	ClassPool classPool = ClassPool.getDefault();
    	
    	CtClass ctclass;
		try {
			ctclass = classPool.makeClass(inputStream);
			return assembleClass(className, ctclass);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public byte[] assembleClass(String className, byte[] classfileBuffer){
		className = className.replace("/", ".");
		if(className.startsWith("sun.")){
			return classfileBuffer;
		}
		InputStream inputStream = new ByteArrayInputStream(classfileBuffer);
		return assembleClass(className, inputStream);
	}
	
	
	public byte[] assembleClass(String className, CtClass ctclass){
		ctclass.setName(className);
		try {
			for (CtMethod ctmethod : ctclass.getDeclaredMethods()) {
				for(CallInjector injector : callInjectors){
					interceptCall(ctmethod, injector);
				}
				for(MethodInjector injector : methodInjectors){
					interceptMethod(ctmethod, injector);
				}
				for(MethodCallInjector injector : methodCallInjectors){
					if(interceptMethod(ctmethod, injector)){
						interceptCall(ctmethod, injector);
					}
				}
            }
			byte[] b = ctclass.toBytecode();
			return b;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (CannotCompileException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private boolean cannotInject(CtMethod ctmethod){
		if (ctmethod.isEmpty() || Modifier.isNative(ctmethod.getModifiers()) || Modifier.isAbstract(ctmethod.getModifiers())) return true;
		return false;
	}
	
	private CtMethod interceptCall(CtMethod ctmethod, final CallInjector injector){
		if (cannotInject(ctmethod)) return ctmethod;
		final String className = ctmethod.getDeclaringClass().getName();
		final String methodName = ctmethod.getName();
		try{
			ctmethod.instrument(
				new ExprEditor() {
					public void edit(MethodCall m)
								  throws CannotCompileException{
						String callClassName = m.getClassName();
						Method callMethod = null;
						try{
							MethodInfo callMethodInfo =  m.getMethod().getMethodInfo2();
							callMethod = new Method(callMethodInfo.getName(), callMethodInfo.getDescriptor());
						}catch(NotFoundException ne){
							callMethod = new Method(m.getMethodName(), null);
						}
						if (injector.isNeedCallInject(m.getClassName(), callMethod)){
							String wrap =  String.format("{\n%1$s\n\t  $_ = $proceed($$); \n%2$s\n}",  
										injector.getMethodCallBefore(callClassName, callMethod),
										injector.getMethodCallAfter(callClassName, callMethod));
							m.replace(wrap);
						}
					}
				});
		}catch(CannotCompileException ce){
			System.out.println(ce + ", method: " + className +"." + methodName + " injector: " + injector);
		}
		return ctmethod;
	}
	
	private boolean interceptMethod(CtMethod ctmethod, MethodInjector injector){
		if (cannotInject(ctmethod)) return false;
		
		String className = ctmethod.getDeclaringClass().getName();
		String methodName = ctmethod.getName();
		MethodInfo methodInfo = ctmethod.getMethodInfo();
		Method method = new Method(methodInfo.getName(), methodInfo.getDescriptor());
		boolean needTraceInject = injector.isNeedProcessInject(className, method);					
		if (!needTraceInject)  return false;
		ClassPool classPool = ClassPool.getDefault();
		try{
			String[][] vars = injector.getMethodVariables(className, method);
			if (vars != null && vars.length > 0) for (String[] var : vars){
				String type = var[0];
				CtClass cttype;
				if ("boolean".equals(type)){
					cttype = CtClass.booleanType;
				}else if("byte".equals(type)){
					cttype = CtClass.	byteType;
				}else if("char".equals(type)){
					cttype = CtClass.charType;
				}else if("double".equals(type)){
					cttype = CtClass.doubleType;
				}else if("float".equals(type)){
					cttype = CtClass.floatType;
				}else if("int".equals(type)){
					cttype = CtClass.intType;
				}else if("long".equals(type)){
					cttype = CtClass.longType;
				}else if("short".equals(type)){
					cttype = CtClass.shortType;
				}else if("void".equals(type)){
					cttype = CtClass.voidType;
				}else{
					cttype = classPool.get(type);
				}
				
				ctmethod.addLocalVariable(var[1], cttype);
			}
			String start = injector.getMethodProcessStart(className, method);
			String end = injector.getMethodProcessReturn(className, method);
			String ex = injector.getMethodProcessException(className, method);
			String fin = injector.getMethodProcessFinally(className, method);
			
			if (start!=null && start.trim().length()>0) ctmethod.insertBefore(start);
			if (end!=null && end.trim().length()>0) ctmethod.insertAfter(end);
			if (ex!=null && ex.trim().length()>0) ctmethod.addCatch(ex, classPool.get("java.lang.Exception"), "_$e"); 
			if (fin!=null && fin.trim().length()>0) ctmethod.insertAfter(fin, true);
		}catch(NotFoundException ne){
			System.out.println(ne + " method: " + className +"." + methodName + " injector: " + injector);
		}catch(CannotCompileException ce){
			System.out.println(ce + " method: " + className +"." + methodName + " injector: " + injector);
		}catch(Exception ex){
			System.out.println(ex + " method: " + className +"." + methodName + " injector: " + injector);
		}
		return true;
	}
}
