package com.github.cxt.Mybatis.Service;

import java.util.ArrayList;
import java.util.List;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.cxt.Mybatis.dao.UserDao;
import com.github.cxt.Mybatis.entity.User;

@Service
public class UserService {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	@Autowired
	private UserDao userDao;
	@Autowired
	private JdbcTemplate jdbc;
	
	@Transactional
	public void test1() {
		UserDao testBatch = sqlSessionTemplate.getMapper(UserDao.class);
		User user = null;
		for(int i = 0; i < 10000; i++){
			user = new User();
			user.setName("aaa");
			user.setUserType(1);
			testBatch.insert(user);
		}
	}
	
	@Transactional
	public void test2() {
		List<User> users = new ArrayList<>();
		User user = null;
		for(int i = 0; i < 10000; i++){
			user = new User();
			user.setName("aaa");
			user.setUserType(1);
			users.add(user);
		}
		userDao.insertBatch(users);
	}
	
	@Transactional
	public void test3() {
		List<Object[]> batchArgs = new ArrayList<>();
		for(int i = 0; i < 10000; i++){
			Object[] obj = new Object[2];
			obj[0] = "aaa";
			obj[1] = 1;
			batchArgs.add(obj);
		}
		jdbc.batchUpdate("insert into user (name, user_type) values (?,?)", batchArgs);
	}
}
