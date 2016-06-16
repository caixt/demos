package com.github.cxt.Myactiviti;

import java.util.List;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-context.xml")
public class Main {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	
	@Test
	public void init(){
		 DeploymentBuilder builder = repositoryService.createDeployment();  
		 builder.addClasspathResource("start.bpmn");  
		 Deployment deployment =  builder.deploy();  
		 System.out.println(deployment.toString());
	}
	
	@Test
	public void start(){
		 ProcessInstance pi = runtimeService.startProcessInstanceByKey("demo");
		 System.out.println(pi.getId());
	}
	
	
	@Test
	public void complete(){
		String pid = "12501";
		List<Task> tasks = null;
		while( (tasks = taskService.createTaskQuery().processInstanceId(pid).orderByTaskName().asc().list()).size() > 0){
			for(Task task : tasks) {
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + task.getId());
				taskService.complete(task.getId());
				break;
			}
		}
		System.out.println("end");
	}
	
	@Test
	public void deletenstance(){
		String pid = "30001";
		runtimeService.deleteProcessInstance(pid, null);
		
	}
	
	
	@Test
	public void deltebuilder(){
		
	}
}
