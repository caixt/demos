package com.github.cxt.MySpring.schema;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/systemTestContext.xml")
public class Main {
	
	@Autowired()
	private ApplicationContext applicationContext;

    @Test
    public void test() {
    	System.out.println(applicationContext.getBean(WorkManager.class));
    	System.out.println(applicationContext.getBean("workerUuid"));
    }

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("systemTestContext.xml");
		System.out.println(applicationContext.getBean(WorkManager.class));
		System.out.println(applicationContext.getBean("workerUuid"));
		
//		System.out.println(ctx.getBeansOfType(People.class));
//
//		System.out.println(ctx.getBeansOfType(Work.class));
//		
//		System.out.println(ctx.getBean(WorkManager.class));
	}

}
