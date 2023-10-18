package com.github.cxt.Mybatis.main;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.github.cxt.Mybatis.Service.UserService;

//jdbc的配置里必须加rewriteBatchedStatements=true  不然test1和test3都会慢
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-context.xml")
//@Transactional
//@TransactionConfiguration(defaultRollback = false)
public class BatchTest {
	@Autowired
	private UserService userService;
	
	@Test
	public void test1(){
		long start = System.currentTimeMillis();
		userService.test1();
		System.out.println(System.currentTimeMillis() - start);
	}
	
	//(但sql长度有可能限制)
	@Test
	public void test2(){
		long start = System.currentTimeMillis();
		userService.test2();
		System.out.println(System.currentTimeMillis() - start);
	}
	
	@Test
	public void test3(){
		long start = System.currentTimeMillis();
		userService.test3();
		System.out.println(System.currentTimeMillis() - start);
	}
		
}
