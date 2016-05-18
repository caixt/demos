package com.github.cxt.Mybatis.main;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.github.cxt.Mybatis.dao.UserDao;
import com.github.cxt.Mybatis.entity.User;
import com.github.cxt.Mybatis.entity.UserType;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-context.xml")
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class SampleTest {
	
	@Autowired
	private UserDao userDao;
	
	@Test
	public void test(){
		User user = new User();
		user.setName("aa");
		user.setUserType(1);
		userDao.insert(user);
		
	}

}
