package com.github.cxt.Mybatis.main;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.github.cxt.Mybatis.databaseId.Temp;
import com.github.cxt.Mybatis.databaseId.TempDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-context-databaseId.xml")
public class DatabaseIdTest {

	
	@Autowired
	private TempDao tempDao;
	
	@Test
	public void test() throws Exception {
		Temp temp = tempDao.selectById(1);
		System.err.println(temp);
		
	}
	
}
