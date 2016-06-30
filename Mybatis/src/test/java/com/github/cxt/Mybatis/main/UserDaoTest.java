package com.github.cxt.Mybatis.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
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
public class UserDaoTest {
	
	@Autowired
	private UserDao userDao;
	
	@Before
	public void before(){
		SLF4JBridgeHandler.removeHandlersForRootLogger();  
		SLF4JBridgeHandler.install();
	}
	
	@Test
	public void test1(){
		System.out.println(userDao.selectByUserId(1L).toString());
	}
	
	@Test
	public void test2(){
		System.out.println(userDao.selectAll().size());
	}
	
	@Test
	public void test3(){
		Map<String, Object> map = new HashMap<>();
		map.put("id", 1L);
		System.out.println(userDao.selectByMap(map).size());
	}
	
	@Test
	public void test4(){
		Map<String, Object> map = new HashMap<>();
		map.put("id", 1L);
		System.out.println(userDao.selectCascadeByUserId(1L));
	}
	
	@Test
	public void test5(){
		Map<String, Object> map = new HashMap<>();
		map.put("id", 1L);
		System.out.println(userDao.selectCascadeByUserId(1L));
	}
	
	@Test
	public void test6(){
		List<User> users = new ArrayList<>();
		User user = new User();
		user.setName("aaa");
		user.setUserType(1);
		users.add(user);
		user = new User();
		user.setName("bbb");
		user.setUserType(1);
		users.add(user);
		userDao.insertBatch(users);
	}
	
	@Test
	public void test7(){
		User user = new User();
		user.setName("aaa");
		user.setUserType(1);
		System.out.println(userDao.insert(user));
	}

	@Test
	public void test8(){
		System.out.println(userDao.selectByUserUuid("BD429E84626246B89C29BB1C52E8DD95"));
	}
	
	@Test
	public void test9(){
		Byte[] b = (Byte[]) userDao.selectUUidByUserId(1l);
		System.out.println(b);
	}

}
