package com.github.cxt.MySpring.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.cxt.MySpring.cache.Work;

@Component
public class RealSubject implements Subject {
	@Autowired
	private Work work;
	
	public void doSomething() {
		System.out.println("call doSomething()" + work);
	}
}