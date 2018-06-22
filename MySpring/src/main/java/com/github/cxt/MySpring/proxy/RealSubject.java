package com.github.cxt.MySpring.proxy;

import org.springframework.stereotype.Component;
import com.github.cxt.MySpring.transaction.custom.Custom;

@Custom
@Component
public class RealSubject implements Subject {
	
	public void doSomething() {
		System.out.println("call doSomething()");
	}
}