package com.github.cxt.MyJavaAgent.javassist;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;

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
}

