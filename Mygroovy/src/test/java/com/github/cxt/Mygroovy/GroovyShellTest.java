package com.github.cxt.Mygroovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.junit.Test;

public class GroovyShellTest {

	@Test
	public void testEvaluate(){
	   Binding binding = new Binding();
	   binding.setProperty("foo", new Integer(5));
	   GroovyShell shell = new GroovyShell(binding);
	   
	   System.out.println(shell.evaluate("def run(foo) { println foo;foo * foo };  foo+=10; run foo"));
	   System.out.println(shell.evaluate("def d = new Date();"));
	   System.out.println(shell.evaluate("foo"));
	}
	
	
	@Test
	public void testMain(){
	   Binding binding = new Binding(new String[]{"java2", "2"});
	   GroovyShell shell = new GroovyShell(binding);
	   shell.evaluate("static void main(String[] args){if(args.length != 2) return;println('Hello,I am ' + args[0] + ',age ' + args[1])}");  
	}
	
	@Test
	public void evalScriptTextFull() throws Exception{  
	    StringBuffer buffer = new StringBuffer();   
	    //define API  
	    buffer.append("class User{")  
	            .append("String name;Integer age;")  
	            .append("String sayHello(){return 'Hello,I am ' + name + ',age ' + age;}}\n");  
	    //Usage  
	    buffer.append("def user = new User(name:'zhangsan',age:1);")  
	            .append("user.sayHello();");  
	    //groovy.lang.Binding  
	    Binding binding = new Binding();  
	    GroovyShell shell = new GroovyShell(binding); 
	    System.out.println(shell.evaluate(buffer.toString()));  
	    shell.evaluate("static void main(String[] args){def user = new User(name:'lisi1',age:12);println(user.sayHello());}");
	    shell.evaluate("static void main(String[] args){def user = new User(name:'lisi2',age:12);println(user.sayHello());}");  
	}
}
