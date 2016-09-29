package com.github.cxt.Mybatis.main;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.github.cxt.Mybatis.dao.UserDao;
import com.github.cxt.Mybatis.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-context.xml")
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class BatchTest {

	@Autowired
	private UserDao testBatch;

	@Test
	public void test(){
		List<User> users = new ArrayList<>();
		User user = new User();
		user.setName("aaa");
		user.setUserType(1);
		users.add(user);
		user = new User();
		user.setName("bbb");
		user.setUserType(1);
		users.add(user);
		
		
		testBatch.insert(user);
		testBatch.insert(user);
	}
}
