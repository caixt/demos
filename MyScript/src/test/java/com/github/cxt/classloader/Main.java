package com.github.cxt.classloader;

import java.io.File;
import java.net.URL;

import org.junit.Test;

public class Main {

	@Test
	public void test1() throws Exception{
		File file = new File("jar/mytest.jar");
		URL[] urls = new URL[]{new URL("file", null, file.getCanonicalPath())};
        String className = "com.github.cxt.classloader.ClassLoaderSimple";
        MyClassLoad cl1 = new MyClassLoad(urls, this.getClass().getClassLoader()); 
        MyClassLoad cl2 = new MyClassLoad(urls, this.getClass().getClassLoader()); 
        Class<?> clazz1 = cl1.loadClass(className);  
        Class<?> clazz2 = cl2.loadClass(className);  
        Object obj1 = clazz1.newInstance();  
        Object obj2 = clazz2.newInstance();  
        clazz1.getMethod("setClassLoaderSimple", Object.class).invoke(obj1, obj2);  
	}
	
	
	@Test
	public void test2() throws Exception{
		File file = new File("jar/mytest.jar");
		URL[] urls = new URL[]{new URL("file", null, file.getCanonicalPath())};
        String className = "com.github.cxt.classloader.TestSimpleImpl";
        MyClassLoad cl1 = new MyClassLoad(urls, this.getClass().getClassLoader()); 
        Class clazz = cl1.loadClass(className);  
        SimpleInterface simple = (SimpleInterface) clazz.newInstance();  
        SimpleData data = new SimpleData();
        data.setKey("key");
        data.setValue("value");
        simple.doSomething(data);
        System.out.println(simple.doSomething(data));
	}
}
