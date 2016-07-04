package com.github.cxt.MySpring.transaction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/com/github/cxt/MySpring/transaction/spring-transaction.xml")
public class Main {
	
	@Autowired
	private Table1Server table1Server;
	
	@Test
	public void test1(){
		Table table = new Table();
		table.setColumn1("column1");
		table.setColumn1("column2");
		table1Server.save(table);
	}

}
