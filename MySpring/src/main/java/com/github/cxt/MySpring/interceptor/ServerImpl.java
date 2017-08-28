package com.github.cxt.MySpring.interceptor;

import com.github.cxt.MySpring.schema.People;

public class ServerImpl implements Server{

	@TestMethod(desc="aaa")
	public void test1() {
		System.out.println("test1");
	}

	@Override
	public void test2() {
		System.out.println("test2");
		
	}

	@TestMethod(desc="${name}")
	public People test3(People pop) {
		System.out.println("test3");
		People p = new People();
		p.setName("haha");
		return p;
	}

}
