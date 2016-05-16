package com.github.cxt.Myactiviti;

import org.activiti.engine.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-context.xml")
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class MainTest {
	@Autowired
	private TaskService taskService;

	@Test
	public void testTask(){
		System.out.println(taskService);
		taskService.complete("10003");
	}
	
}
