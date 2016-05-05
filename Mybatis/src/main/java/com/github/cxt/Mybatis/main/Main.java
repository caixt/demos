package com.github.cxt.Mybatis.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.github.cxt.Mybatis.dao.UserDao;
import com.github.cxt.Mybatis.entity.User;


public class Main {

	public static void main(String[] args) {
		ApplicationContext ctx =  new ClassPathXmlApplicationContext("spring-context.xml");
		UserDao userDao = ctx.getBean(UserDao.class);
		System.out.println(userDao.selectByUserId(1L));
		System.out.println(userDao.selectAll().size());
		Map<String, Object> map = new HashMap<>();
		User user = new User();
		user.setName("aaa");
		user.setUserType(1);
		userDao.insert(user);
		System.out.println(user.getId());
		List<User> users = new ArrayList<>();
		users.add(user);
		user = new User();
		user.setName("bbb");
		user.setUserType(1);
		users.add(user);
		userDao.insertBatch(users);
		map.put("id", 1l);
		System.out.println(userDao.selectByMap(map).size());
		System.out.println(userDao.selectCascadeByUserId(1L));
	}

}
