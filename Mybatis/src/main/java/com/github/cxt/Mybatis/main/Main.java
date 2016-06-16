package com.github.cxt.Mybatis.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.BinaryRefAddr;

import org.apache.ibatis.type.BlobTypeHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.github.cxt.Mybatis.UUIDTypeHandler;
import com.github.cxt.Mybatis.dao.UserDao;
import com.github.cxt.Mybatis.entity.User;


public class Main {

	public static void main(String[] args) {
		ApplicationContext ctx =  new ClassPathXmlApplicationContext("spring-context.xml");
		UserDao userDao = ctx.getBean(UserDao.class);
		Byte[] b = (Byte[]) userDao.selectUUidByUserId(45l);
		System.out.println(b);
//		System.out.println(userDao.selectByUserId(1L));
//		System.out.println(userDao.selectAll().size());
//		Map<String, Object> map = new HashMap<>();
//		String uuid = UUIDTypeHandler.createUUID();
//		User user = new User();
//		user.setName("aaa");
//		user.setUserType(1);
//		user.setUuid(uuid);
//		user.setInfo(JSONObject.parseObject("{\"aaa\":123}"));
//		userDao.insert(user);
//		user = userDao.selectByUserUuid(uuid);
//		System.out.println(user);
//		System.out.println(user.getId());
//		List<User> users = new ArrayList<>();
//		users.add(user);
//		user = new User();
//		user.setName("bbb");
//		user.setUserType(1);
//		users.add(user);
//		userDao.insertBatch(users);
//		map.put("id", 1l);
//		System.out.println(userDao.selectByMap(map).size());
//		System.out.println(userDao.selectCascadeByUserId(1L));
	}

}
