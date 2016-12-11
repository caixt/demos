package com.github.cxt.Myactiviti;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class Cxt2 implements TaskListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask delegateTask) {
		System.out.println(delegateTask.getTaskDefinitionKey() + "---------------------start");
		System.out.println(delegateTask.getEventName());
		System.out.println("参数1:" + delegateTask.getVariables());
		System.out.println("参数2:" + delegateTask.getVariablesLocal());
		System.out.println(delegateTask.getTaskDefinitionKey() + "---------------------end");
	}
}

