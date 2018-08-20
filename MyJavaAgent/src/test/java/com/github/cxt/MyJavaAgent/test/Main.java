package com.github.cxt.MyJavaAgent.test;


public class Main {
	
	public static void main(String[] args) throws Exception{
		
//	  System.out.println(Thread.currentThread().getContextClassLoader());
//	  
//	  System.out.println(Demo.class.getClassLoader());
//	  System.out.println(URLStreamHandlerFactory.class.getClassLoader());
//	  
//	  Demo.test("aaa");
	  Demo demo = new Demo();
//	  demo.print("bbb");
	  demo.printJDBCdata();
//	  System.out.println(demo.toString());
	}
}