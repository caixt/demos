package com.github.cxt.MyJavaAgent.test;


public class Main {
	
	public static void main(String[] args) throws Exception{
	  Demo.test("aaa");
	  Demo demo = new Demo();
	  demo.print("bbb");
	  demo.printJDBCdata();
	  System.out.println(demo.toString());
	}
}