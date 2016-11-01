package com.github.cxt.MySpring.task;

import java.util.Date;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.ScheduledMethodRunnable;

public class Main {

	public static void main(String[] args) throws NoSuchMethodException {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(3);
		scheduler.initialize();
		Demo demo = new Demo();
		
		ScheduledMethodRunnable task = new ScheduledMethodRunnable(demo, "test1");
		scheduler.scheduleWithFixedDelay(task, new Date(System.currentTimeMillis() + 1000 * 3), 2000);
		
		scheduler.schedule(task, new Date(System.currentTimeMillis() + 1000 * 3));
	}
}
