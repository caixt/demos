package com.github.cxt.Myactiviti;

import java.util.List;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx =  new ClassPathXmlApplicationContext("spring-context.xml");
		RepositoryService repositoryService =  ctx.getBean(RepositoryService.class);
	    DeploymentBuilder builder = repositoryService.createDeployment();  
	 
	    builder.addClasspathResource("start.bpmn");  
	    builder.deploy();  
	    
	    RuntimeService runtimeService =  ctx.getBean(RuntimeService.class);
	    ProcessInstance pi = runtimeService.startProcessInstanceByKey("demo");
		System.out.println(pi.getId());
		
		TaskService taskService =  ctx.getBean(TaskService.class);
		List<Task> tasks = null;
		while( (tasks = taskService.createTaskQuery().processInstanceId(pi.getId()).orderByTaskName().asc().list()).size() > 0){
			for(Task task : tasks) {
				System.out.println(task.getId());
				taskService.complete(task.getId());
			}
		}
		System.out.println("end");
		ctx.close();
	}

}
