package com.github.cxt.springboot2.task;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.cxt.springboot2.lock.DistLock;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HellowordTask {

	@Async
	@Scheduled(cron = "0 0/1 *  * * ?")
	@DistLock
	public void say() {
		log.info("hello world");
	}
}
