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

import com.github.cxt.Mybatis.Criterions;
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
		System.out.println(userDao.selectByUserId(1L));
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
		user.setClasses(new String[]{"a","b"});
		
		System.out.println(userDao.insert(user));
	}

	@Test
	public void test8(){
		System.out.println(userDao.selectByUserUuid("BD429E84626246B89C29BB1C52E8DD95"));
	}
	
	@Test
	public void test9(){
		//mybatis 3.4.0以上
		String uuid = userDao.selectUUidByUserId(1l);
		System.out.println(uuid);
	}
	
	@Test
	public void test10(){
		//貌似不支持 #{}的格式
		System.out.println(userDao.selectByStatic());
	}

	
	@Test
	public void test11(){
		Criterions criterions = new Criterions();
		criterions.createCriteria().andCustom("name like '%a%' or name like ?", "%b%");
		System.out.println(userDao.selectByCriterions(criterions).size());
	}
	
	@Test
	public void test12(){
		Criterions criterions1 = new Criterions();
		criterions1.createCriteria().andColumnEqualTo("name", "aaa");
		Criterions criterions2 = new Criterions();
		criterions2.createCriteria().andColumnEqualTo("user_type", 1);
		System.out.println(userDao.selectByTwoCriterions(criterions1, criterions2).size());
	}
	
	@Test
	public void test13(){
		//id column 的值相同，或不存在该配置。并且包含association
		//存在查询值和结果数据量不相同的情况。mybatis处理的bug
		System.out.println(userDao.selectNumberError().size());
	}
}
