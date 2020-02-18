package com.github.cxt.Mybatis.main;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.github.cxt.Mybatis.dao.UserDao;
import com.github.cxt.Mybatis.entity.User;

//整体来说test2的性能最快
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-context.xml")
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class BatchTest {
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	@Autowired
	private UserDao userDao;

	//执行快，但事务提交慢
	@Test
	public void test1(){
		UserDao testBatch = sqlSessionTemplate.getMapper(UserDao.class);
		User user = null;
		for(int i = 0; i < 100000; i++){
			user = new User();
			user.setName("aaa");
			user.setUserType(1);
			testBatch.insert(user);
		}
	}
	
	//执行慢，但事务提交快,(但sql长度有限制)
	@Test
	public void test2(){
		List<User> users = new ArrayList<>();
		User user = null;
		for(int i = 0; i < 100000; i++){
			user = new User();
			user.setName("aaa");
			user.setUserType(1);
			users.add(user);
		}
		userDao.insertBatch(users);
	}
	
	//整体性能和test1接近
	@Test
	public void test3(){
		List<Object[]> batchArgs = new ArrayList<>();
		for(int i = 0; i < 100000; i++){
			Object[] obj = new Object[2];
			obj[0] = "aaa";
			obj[1] = 1;
			batchArgs.add(obj);
		}
		jdbc.batchUpdate("insert into user (name, user_type) values (?,?)", batchArgs);
	}
		
}
