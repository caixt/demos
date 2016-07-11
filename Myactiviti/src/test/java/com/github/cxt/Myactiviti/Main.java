package com.github.cxt.Myactiviti;

import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
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
	
	private static String KEY = "demo";
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	private ProcessInstance pi;
	
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	
	@Before
	public void initAndStart(){
		 DeploymentBuilder builder = repositoryService.createDeployment();  
		 builder.addClasspathResource("start.bpmn");  
		 builder.deploy();  
		 pi = runtimeService.startProcessInstanceByKey(KEY);
	}
	
	/**
	 * 所有的都模拟完成
	 */
	@Test
	public void allComplete(){
		List<Task> tasks = null;
		while( (tasks = taskService.createTaskQuery().processInstanceId(pi.getId()).orderByTaskName().asc().list()).size() > 0){
			for(Task task : tasks) {
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + task.getId());
				taskService.complete(task.getId());
			}
		}
		System.out.println("end");
	}
	
	/**
	 * 删除实例
	 */
	@Test
	public void deletenstance(){
		runtimeService.deleteProcessInstance(pi.getId(), null);
	}
	
	/**
	 * 强制删除流程
	 */
	@Test
	public void forcedeltebuilder(){
		List<ProcessDefinition>  list = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(KEY).list();
		for(ProcessDefinition processDefinition : list){
			repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
		}
	}
	
	/**
	 * 删除流程
	 */
	@Test
	public void deltebuilder(){
		List<ProcessDefinition>  list = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey("c40410dda9b54c10a1d7375bbb0ec705").list();
		for(ProcessDefinition processDefinition : list){
			repositoryService.deleteDeployment(processDefinition.getDeploymentId());
		}
	}
	
	/**
	 * 模拟启动后，停止，删除实例，删除流程
	 */
	@Test
	public void test1(){
		
		List<Task> tasks = null;
		while( (tasks = taskService.createTaskQuery().processInstanceId(pi.getId()).orderByTaskName().asc().list()).size() > 0){
			for(Task task : tasks) {
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + task.getId());
				taskService.complete(task.getId());
			}
			break;
		}
		runtimeService.deleteProcessInstance(pi.getId(), null);
		
		List<ProcessDefinition>  list = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(KEY).list();
		for(ProcessDefinition processDefinition : list){
			repositoryService.deleteDeployment(processDefinition.getDeploymentId());
		}
	}
}
