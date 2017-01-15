package com.github.cxt.Mygroovy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.junit.Test;

public class HttpTest {
	
	
	@Test
	public void test() throws ScriptException, URISyntaxException, FileNotFoundException{
		ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("groovy");
        
        Bindings bindings = engine.createBindings();
        bindings.put("url", "http://www.126.com/index.htm");
        
        File source = new File(Thread.currentThread().getContextClassLoader().getResource("test.groovy").toURI());
        FileReader fr = new FileReader(source);
        
        engine.eval(fr, bindings);
        
        System.out.println(bindings.get("output"));

		
	}
	

}
