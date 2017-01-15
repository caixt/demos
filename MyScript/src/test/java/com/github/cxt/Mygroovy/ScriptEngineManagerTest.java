package com.github.cxt.Mygroovy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintWriter;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.junit.Test;

public class ScriptEngineManagerTest {
	
	public static int testabc(int a){
		return a*a;
	}

	@Test
	public void test() throws ScriptException, NoSuchMethodException, IOException{
		 ScriptEngineManager factory = new ScriptEngineManager();
         ScriptEngine engine = factory.getEngineByName("groovy");
         
         Bindings bindings = engine.createBindings();
         bindings.put("x", 10);
         
         String str = "println x; x +=1; abc=111;def bcd=111;0";
         System.out.println(engine.eval(str, bindings));
         System.out.println((int)bindings.get("x") == 11);
         System.out.println((int)bindings.get("abc") == 111);
         System.out.println(bindings.get("bcd") == null);
	}
	
	
	@Test
	public void test2() throws Exception{
		 ScriptEngineManager factory= new ScriptEngineManager();
	     ScriptEngine engine =factory.getEngineByName("groovy");
	     ScriptContext context = engine.getContext();
	     
	     PipedReader pr = new PipedReader();
	     PipedWriter pw = new PipedWriter(pr);
	     PrintWriter writer = new PrintWriter(pw, true);
	     context.setWriter(writer);
	     //改成10后缓存区满了
	     String str ="def test(a){a+a}; def s = \"a\"; println(s);for(i=0;i<9;i++){s=test(s);}; println(s);";
	     engine.eval(str);
	     writer.close();
		 BufferedReader br = new BufferedReader(pr);
		 String s = null;
         while((s = br.readLine()) != null){
         	 System.out.println(s);
         }


	}
	
	
	@Test
	public void test3() throws Exception {
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("groovy");
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
						System.out.println(s);
					}
				}catch(Exception e){
					e.printStackTrace();
				}

			}
		}).start();

		context.setWriter(writer);
		String str = "def test(a){a+a}; def s = \"a\"; println(s);for(i=0;i<15;i++){s=test(s);println(i + \"-\" + s.length() + \"-\" + s);};";
		engine.eval(str);
		writer.close();
	}

	@Test
	public void test4() throws Exception{
		String s = "a"; 
		System.out.println(s);
		for(int i=0;i<15;i++){
			s=s+s;
			if(i >11){
				System.out.println("!");
			}
			System.out.println(i + "-" + s.length() + "-" + s);
		}
		
	}
}
