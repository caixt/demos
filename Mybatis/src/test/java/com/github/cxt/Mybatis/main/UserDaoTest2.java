package com.github.cxt.Mybatis.main;

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
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Configurator.class)
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class UserDaoTest2 {
	
	@Autowired
	private UserDao userDao;
	
	@Before
	public void before(){
		SLF4JBridgeHandler.removeHandlersForRootLogger();  
		SLF4JBridgeHandler.install();
	}
	
	
	@Test
	public void test1(){
		//selectAll_COUNT 伪造了数量
		PageHelper.startPage(2, 3);
		List<Map<String, Object>> data = userDao.selectAll();
	    PageInfo<Map<String, Object>> p = new PageInfo<Map<String, Object>>(data);
	    System.out.println(p);
	}
}
