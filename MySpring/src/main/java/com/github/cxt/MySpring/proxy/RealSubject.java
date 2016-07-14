package com.github.cxt.MySpring.proxy;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RealSubject implements Subject {
	
	@Transactional
	public void doSomething() {
		System.out.println("call doSomething()");
	}
}