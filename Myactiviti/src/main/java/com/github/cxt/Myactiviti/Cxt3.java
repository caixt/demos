package com.github.cxt.Myactiviti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class Cxt3 implements ExecutionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		System.out.println(execution.getCurrentActivityId() + "---------------------start");
		System.out.println(execution.getEventName());
		System.out.println("参数1:" + execution.getVariables());
		System.out.println("参数2:" + execution.getVariablesLocal());
		System.out.println(execution.getCurrentActivityId() + "---------------------end");
		
	}

}
