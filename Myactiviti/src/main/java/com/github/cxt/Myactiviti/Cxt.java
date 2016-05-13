package com.github.cxt.Myactiviti;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;

public class Cxt implements TaskListener{
	
	@Autowired
	private TaskService taskService;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask delegateTask) {
		System.out.println(taskService);
		
	}
}

