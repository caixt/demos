package com.github.cxt.Myactiviti;

import java.util.List;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
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
public class Listener {
	
	private static String KEY = "listener";
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	
	@Test
	public void test(){
		 DeploymentBuilder builder = repositoryService.createDeployment();
		 builder.addClasspathResource("listener.bpmn");  
		 builder.deploy();  
		 ProcessInstance pi = runtimeService.startProcessInstanceByKey(KEY);
		 
		 List<Task> tasks = null;
			while( (tasks = taskService.createTaskQuery().processInstanceId(pi.getId()).orderByTaskName().asc().list()).size() > 0){
				System.out.println(tasks.size());
				for(Task task : tasks) {
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + task.getId());
					taskService.complete(task.getId());
				}
			}
			System.out.println("end");
	}
}
