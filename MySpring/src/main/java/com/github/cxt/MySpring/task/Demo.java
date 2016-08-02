package com.github.cxt.MySpring.task;

import java.util.Date;

public class Demo {

	public void test1(){
		System.out.println("!" + new Date().toLocaleString());
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("?" + new Date().toLocaleString());
	}
}
