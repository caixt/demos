package com.github.cxt.Mybatis.main;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import com.github.cxt.Mybatis.dao.UserDataDao;

/**
 * 代码可调试DefaultResultSetHandler的方法handleRowValues
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-context.xml")
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class UserDataDaoTest {
	
	@Autowired
	private UserDataDao userDataDao;
	
	@Before
	public void before(){
		SLF4JBridgeHandler.install();
	}
	
	@Test
	public void testSelectRightList(){
		System.out.println(userDataDao.selectRightList().size());
	}
	
	@Test
	public void testSelectErrorList(){
		System.out.println(userDataDao.selectErrorList().size());
	}
	
}
