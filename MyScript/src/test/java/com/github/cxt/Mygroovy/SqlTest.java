package com.github.cxt.Mygroovy;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.junit.Test;


public class SqlTest {

	
	@Test
	public void test() throws Exception{
		File f1 = new File("jar/groovy-all-2.4.7.jar");
		File f2 = new File("jar/mysql-connector-java-5.1.34.jar");
		URL[] urls = new URL[]{new URL("file", null, f1.getCanonicalPath()), new URL("file", null, f2.getCanonicalPath())};
        MyClassLoad cl = new MyClassLoad(urls, Thread.currentThread().getContextClassLoader().getParent()); 
        Thread.currentThread().setContextClassLoader(cl);
        
        ScriptEngineManager factory = new ScriptEngineManager();
        
        System.out.println("!?"+factory.getClass().getClassLoader());
        ScriptEngine engine = factory.getEngineByName("groovy");
        System.out.println("!?"+engine.getClass().getClassLoader());
		
        File source = new File("src/test/resources/sql.groovy");
        FileReader fr = new FileReader(source);
        
        Bindings bindings = engine.createBindings();
        bindings.put("url", "http://www.126.com/index.htm");
        Object o = engine.eval(fr, bindings);
        System.out.println(o);
        System.out.println(bindings.get("output"));
		
		
	}
}
