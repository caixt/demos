package com.github.cxt.Myactiviti;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class Sub {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;

	
	/**
	 * 内嵌子流程
	 */
	@Test
	public void sub1Test() {
		DeploymentBuilder builder = repositoryService.createDeployment();
		builder.addClasspathResource("sub1.bpmn");
		builder.deploy();
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("sub1");

		List<Task> tasks = null;
		while ((tasks = taskService.createTaskQuery().processInstanceId(pi.getId()).orderByTaskName().asc().list()).size() > 0) {
			System.out.println(tasks.size());
			for (Task task : tasks) {
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + task.getId());
				taskService.complete(task.getId());
			}
		}
		System.out.println("end");
	}
	
	/**
	 * 引用子流程
	 */
	@Test
	public void sub2Test() {
		DeploymentBuilder builder = repositoryService.createDeployment();
		builder.addClasspathResource("parent.bpmn");
		builder.deploy();

		builder = repositoryService.createDeployment();
		builder.addClasspathResource("sub2.bpmn");
		builder.deploy();

		Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("param1", "p1");
        variables.put("param2", "p2");
        variables.put("param3", "p3");
         
        // start process instance
        ProcessInstance ppi = runtimeService.startProcessInstanceByKey("sub2", variables);
        
        List<Task> tasks = null;
        System.out.println("ppi:" + ppi.getId());
        
		while ((tasks = taskService.createTaskQuery().processInstanceId(ppi.getId()).orderByTaskName().asc().list()).size() > 0) {
			System.out.println(tasks.size());
			for (Task task : tasks) {
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + task.getId());
				taskService.complete(task.getId());
			}
		}
		
		ProcessInstance spi = runtimeService.createProcessInstanceQuery().superProcessInstanceId(ppi.getId()).singleResult();
		
        System.out.println("sppi:" + spi.getId());
        
        //子的
		while ((tasks = taskService.createTaskQuery().processInstanceId(spi.getId()).orderByTaskName().asc().list()).size() > 0) {
			System.out.println(tasks.size());
			for (Task task : tasks) {
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + task.getId());
				taskService.complete(task.getId());
			}
		}
		
		while ((tasks = taskService.createTaskQuery().processInstanceId(ppi.getId()).orderByTaskName().asc().list()).size() > 0) {
			System.out.println(tasks.size());
			for (Task task : tasks) {
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + task.getId());
				taskService.complete(task.getId());
			}
		}
		
		System.out.println("end");
	}
}
