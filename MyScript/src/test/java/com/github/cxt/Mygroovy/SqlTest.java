package com.github.cxt.Mygroovy;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.net.URLClassLoader;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.codehaus.groovy.jsr223.GroovyScriptEngineImpl;
import org.junit.Test;


public class SqlTest {
	
	//模拟加载其他应用,并且其他应用的class相互对立不相互不影响(只有groovy-all的jar是公用的)
	@Test
	public void test() throws Exception{
		URL grovvy = GroovyScriptEngineImpl.class.getProtectionDomain().getCodeSource().getLocation();
		File mysql = new File("jar/mysql-connector-java-5.1.34.jar");
		URL[] urls = new URL[]{grovvy, new URL("file", null, mysql.getCanonicalPath())};
		URLClassLoader cl = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader().getParent()); 
        Thread.currentThread().setContextClassLoader(cl);
        
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("groovy");
        
        System.out.println(engine.getClass());
        //classloader不一样所以是false
        System.out.println(engine instanceof GroovyScriptEngineImpl);
		
        File source = new File("src/test/resources/sql.groovy");
        FileReader fr = new FileReader(source);
		
        
        Bindings bindings = engine.createBindings();
        bindings.put("driver", "com.mysql.jdbc.Driver");
        bindings.put("url", "jdbc:mysql://127.0.0.1:3306/test?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8");
        bindings.put("username", "root");
        bindings.put("passwd", "12345678");
        Object o = engine.eval(fr, bindings);
        System.out.println(o);
        System.out.println(bindings.get("output"));
		
		
	}
}
