package com.github.cxt.MyJavaAgent.javassist;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import com.github.cxt.MyJavaAgent.AroundInterceptorDemo1;
import com.github.cxt.MyJavaAgent.AroundInterceptorDemo2;
import com.github.cxt.MyJavaAgent.InterceptorManage;
import com.github.cxt.MyJavaAgent.NamedClassPool;

import javassist.ByteArrayClassPath;
import javassist.CannotCompileException;
import javassist.ClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;


public class Main {

	@Test
	public void test1() throws Exception {
		String name = Main.class.getPackage().getName() + "." + "Demo";
		
		ClassPool cp = ClassPool.getDefault();
		CtClass cc = cp.get(name);
		
		
		CtField f = CtField.make("public int z = 0;", cc);
		cc.addField(f);
		
		CtMethod ctMethod = cc.getDeclaredMethod("test");
	    ctMethod.setBody("System.out.println(\"this method is changed dynamically!\");");
	    CtMethod mthd = CtNewMethod.make("public void testAddFun() { System.out.println(\"haha\"); }", cc);
	    cc.addMethod(mthd);
	    
	    ctMethod = cc.getDeclaredMethod("abc");
	    ctMethod.addLocalVariable("charchar", CtClass.charType);
	    
	    ctMethod.insertBefore("{ charchar = 'a'; System.out.println(\"method before\");\n}");
	    ctMethod.insertAfter("{ System.out.println(\"method after \" + charchar);\n}");
	    ctMethod.insertAfter("{ System.out.println(\"method finally\");\n}", true);
	    ctMethod.addCatch("{ System.out.println(\"method error\");\n throw _$e;\n}", cp.get("java.lang.Exception"), "_$e");

	    cc.writeFile("testClasses");
	    cc.toClass();
	    
	    
		Demo demo = new Demo();
		demo.test();
		
		System.out.println(demo.abc("eeee"));
		System.out.println(demo.abc(null));
		
		Method method = Demo.class.getMethod("testAddFun");
		Object o = method.invoke(demo);
		System.out.println(o);
	}

	
	@Test
	public void test2() throws Exception {
		String name = Main.class.getPackage().getName() + "." + "Demo";
		
		ClassPool cp = ClassPool.getDefault();
		byte[] classfileBuffer = FileUtils.readFileToByteArray(new File("classDir\\Demo.class_1"));
		InputStream inputStream = null;
		CtClass ctclass = null;
		
		inputStream = new ByteArrayInputStream(classfileBuffer);
		ctclass = cp.makeClass(inputStream);
		ctclass.toBytecode();
		
		inputStream = new ByteArrayInputStream(classfileBuffer);
		cp.makeClass(inputStream);
		ctclass = cp.makeClass(inputStream);
		ctclass.toBytecode();
		
		CtClass cc = cp.get(name);
		cc.writeFile("testClasses");
		System.out.println(cc);
	}
	
	
	
	@Test
	public void test3() throws Exception {
		String name = Main.class.getPackage().getName() + "." + "Demo";
		
		ClassPool cp = ClassPool.getDefault();
		CtClass ctclass = null;
		ByteArrayClassPath byteArrayClassPath = null;
		
		byteArrayClassPath = new ByteArrayClassPath(name, FileUtils.readFileToByteArray(new File("classDir\\Demo.class_1")));
		cp.insertClassPath(byteArrayClassPath);
		ctclass = cp.get(name);
		ctclass.toBytecode();
		ctclass.writeFile("testClasses1");
		
		byteArrayClassPath = new ByteArrayClassPath(name, FileUtils.readFileToByteArray(new File("classDir\\Demo.class_2")));
		ClassPool child = new NamedClassPool(cp, "x1");
		child.insertClassPath(byteArrayClassPath);
		child.childFirstLookup = true;
		ctclass = child.get(name);
		ctclass.toBytecode();
		ctclass.writeFile("testClasses2");
		
	}
	
	
	@Test
	public void test4() throws Exception {
		Class<?> clazz = null;
		ClassLoader classLoader = new URLClassLoader(new URL[]{}, Main.class.getClassLoader());
		
		String name = Main.class.getPackage().getName() + "." + "Demo";
		
		ClassPool root = new NamedClassPool("root");
//		root.appendSystemPath();
		ClassPath classPath = new LoaderClassPath(Main.class.getClassLoader());
		root.appendClassPath(classPath);
		
		System.out.println(root.get("com.github.cxt.MyJavaAgent.InterceptorManage"));
		
		ByteArrayClassPath byteArrayClassPath = new ByteArrayClassPath(name, FileUtils.readFileToByteArray(new File("classDir\\Demo.class")));
		root.insertClassPath(byteArrayClassPath);
		CtClass ctclass = root.get(name);
		
		CtMethod ctMethod = ctclass.getDeclaredMethod("test");
		CtClass throwable = root.get("java.lang.Throwable");
		int key1 = InterceptorManage.add(new AroundInterceptorDemo1());
		methodAddInterceptor(ctMethod, key1, throwable);
		int key2 = InterceptorManage.add(new AroundInterceptorDemo2());
		methodAddInterceptor(ctMethod, key2, throwable);
		
		clazz = ctclass.toClass(classLoader);
		System.out.println(clazz.getClassLoader());
		
		Object obj = clazz.newInstance();
		Method method  = clazz.getMethod("test");
		
		System.out.println(method.invoke(obj));
		
	}
	
	private void methodAddInterceptor(CtMethod ctMethod, int key, CtClass throwable) throws CannotCompileException{
		String beforeCode = String.format("{com.github.cxt.MyJavaAgent.InterceptorManage.get(%d).before(this, $args);}", key);
		String afterCode = String.format("{com.github.cxt.MyJavaAgent.InterceptorManage.get(%d).after(this, $args, ($w)$_, null);}", key);
		String catchCode = String.format("{com.github.cxt.MyJavaAgent.InterceptorManage.get(%d).after(this, $args, null, $e); throw $e;}", key);
		
		ctMethod.insertBefore(beforeCode);
		ctMethod.insertAfter(afterCode);
		ctMethod.addCatch(catchCode, throwable, "$e");
	}
}

