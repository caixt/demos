package com.github.cxt.MySpring.task;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TaskTest {

	
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("com/github/cxt/MySpring/task/spring-task-content.xml");

	}
}