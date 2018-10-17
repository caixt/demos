package com.github.cxt.MyJavaAgent.javassist;

public class Demo {
	
	
	@Override
	public String toString() {
		return "haha";
	}
	
	public void test(){
		
	}
	
	public Integer abc(String str) { 
		int a = 1;
		int b = 1;
		System.out.println(a + b);
		return str.length();
	}
}
