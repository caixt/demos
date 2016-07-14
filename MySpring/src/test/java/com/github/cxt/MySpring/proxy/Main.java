package com.github.cxt.MySpring.proxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/com/github/cxt/MySpring/proxy/spring-content.xml")
public class Main {

	@Autowired
	private Subject subject;
	
	@Test
	public void test1(){
		//在实现类上是否有@Transactional来观察
		//以及<aop:aspectj-autoproxy proxy-target-class="true"/>
		subject.doSomething();
		System.out.println(subject);
	}
}
