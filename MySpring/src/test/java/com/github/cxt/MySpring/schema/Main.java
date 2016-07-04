package com.github.cxt.MySpring.schema;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * junit 和 main运行有差异(先去掉配置文件中的<context:annotation-config />)
 * @author caixt@broada.com
 * @Description:
 * @date 2016年7月1日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/com/github/cxt/MySpring/schema/systemTestContext.xml")
public class Main {
	
	@Autowired()
	private ApplicationContext applicationContext;

    @Test
    public void test() {
    	System.out.println(applicationContext.getBean(WorkManager.class));
    	System.out.println(applicationContext.getBeansOfType(Integer.class));
		System.out.println(applicationContext.getBeansOfType(People.class));
		System.out.println(applicationContext.getBeansOfType(Work.class));
		System.out.println(applicationContext.getBean(WorkManager.class));
    }

	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("com/github/cxt/MySpring/schema/systemTestContext.xml");
		
		System.out.println(applicationContext.getBeansOfType(Integer.class));
		System.out.println(applicationContext.getBeansOfType(People.class));
		System.out.println(applicationContext.getBeansOfType(Work.class));
		System.out.println(applicationContext.getBean(WorkManager.class));
		
		applicationContext.close();
	}

}
