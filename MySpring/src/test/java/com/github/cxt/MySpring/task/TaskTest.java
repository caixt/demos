package com.github.cxt.MySpring.task;

import java.util.Date;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.ScheduledMethodRunnable;

public class TaskTest {

	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws NoSuchMethodException {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("com/github/cxt/MySpring/task/spring-task-content.xml");
		
		TaskScheduler scheduler = applicationContext.getBean(TaskScheduler.class);
		Demo demo = applicationContext.getBean(Demo.class);
		ScheduledMethodRunnable task = new ScheduledMethodRunnable(demo, "test1");
		
		scheduler.schedule(task, new Date(System.currentTimeMillis() + 1000 * 1));

	}
}
