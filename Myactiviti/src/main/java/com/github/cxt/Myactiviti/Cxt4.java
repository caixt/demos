package com.github.cxt.Myactiviti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class Cxt4 implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println(execution.getCurrentActivityId() + "---------------------start");
		System.out.println(execution.getEventName());
		System.out.println("参数1:" + execution.getVariables());
		System.out.println("参数2:" + execution.getVariablesLocal());
		System.out.println(execution.getCurrentActivityId() + "---------------------end");
	}

}
