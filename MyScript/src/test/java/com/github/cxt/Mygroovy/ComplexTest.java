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
		
		GroovyClassLoader classLoader = engine.getClassLoader();
		classLoader.addClasspath("jar/mysql-connector-java-5.1.34.jar");
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
