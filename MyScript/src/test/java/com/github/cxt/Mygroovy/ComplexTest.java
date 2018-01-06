package com.github.cxt.Mygroovy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintWriter;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineManager;
import org.codehaus.groovy.jsr223.GroovyScriptEngineImpl;
import org.junit.Test;
import groovy.lang.GroovyClassLoader;


public class ComplexTest {
	
	
	@Test
	public void test() throws Exception{
		ScriptEngineManager factory = new ScriptEngineManager();
		GroovyScriptEngineImpl engine = (GroovyScriptEngineImpl) factory.getEngineByName("groovy");
		
		ScriptContext context = engine.getContext();

		final PipedReader pr = new PipedReader();
		PipedWriter pw = new PipedWriter(pr);
		PrintWriter writer = new PrintWriter(pw, true);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try{
					BufferedReader br = new BufferedReader(pr);
					String s = null;
					while ((s = br.readLine()) != null) {
						System.out.println("日志输出:" + s);
					}
				}catch(Exception e){
					e.printStackTrace();
				}

			}
		}).start();

		context.setWriter(writer);
		//1.script脚本执行的时候的classloader为CallSiteClassLoader。
		//2.再通过class的classloader获取到GroovyClassLoader 所以能加载到额外指定的jar
		//3.GroovyClassLoader的父classloader为appclassloader 所以能加载到项目启动时的jar
		//4.如果还是找不到class，那就按当前CallSiteClassLoader.class对应的classLoader来加载
		//但是mysql的驱动如果是在2这个步骤添加的,并且按照传统的方法来获取连接,将找不到驱动，原因
		//1.驱动获取是通过DriverManager来加载的,但这个class的classloader为BootStrapClassLoader
		//2.并且CallSiteClassLoader.class的classLoader为appclassloader。
		//所以1,2都不能获取到mysql的连接
		GroovyClassLoader classLoader = engine.getClassLoader();
		classLoader.addClasspath("jar/commons-codec-1.9.jar");
		classLoader.addClasspath("jar/commons-logging-1.2.jar");
		classLoader.addClasspath("jar/httpclient-4.5.2.jar");
		classLoader.addClasspath("jar/httpcore-4.4.4.jar");
        Bindings bindings = engine.createBindings();
        bindings.put("url", "http://www.126.com/index.htm");
        bindings.put("classLoader", classLoader);
        
        File source = new File(Thread.currentThread().getContextClassLoader().getResource("complextest.groovy").toURI());
        FileReader fr = new FileReader(source);
        
        engine.eval(fr, bindings);
        
        System.out.println(bindings.get("output"));
        
	}
	

}
