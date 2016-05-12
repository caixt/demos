package com.github.cxt.Myactiviti;


import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {

	public static void main(String[] args) {
		ApplicationContext ctx =  new ClassPathXmlApplicationContext("spring-context.xml");
		RepositoryService repositoryService =  ctx.getBean(RepositoryService.class);
	    DeploymentBuilder builder = repositoryService.createDeployment();  
	 
	    builder.addClasspathResource("start.bpmn");  
	    builder.deploy();  
	}

}
