package com.github.cxt.MySpring.transaction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.cxt.MySpring.transaction.mybatis.Table;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/com/github/cxt/MySpring/transaction/spring-transaction.xml")
public class Main {
	
	@Autowired
	private Table1Server table1Server;
	
	/**
	 * 一个插入,结束后模拟异常
	 */
	@Test
	public void test1(){
		Table table = new Table();
		table.setColumn1("column1");
		table.setColumn2("column2");
		table1Server.save1(table);
	}
	
	/**
	 * 二个插入,第二个结束后模拟异常
	 * 第二个属性 NOT_SUPPORTED：以非事务方式执行操作，如果当前存在事务，就把当前事务挂起
	 */
	@Test
	public void test2(){
		Table table = new Table();
		table.setColumn1("column1");
		table.setColumn2("column2");
		table1Server.save2(table);
	}
	
	/**
	 * 二个插入,第二个结束后模拟异常
	 * 第二个属性 REQUIRED：支持当前事务，如果当前没有事务，就新建一个事务(默认)
	 * 即两个失败
	 */
	@Test
	public void test3(){
		Table table = new Table();
		table.setColumn1("column1");
		table.setColumn2("column2");
		table1Server.save3(table);
	}
	
	/**
	 * 二个插入,第二个结束后模拟异常
	 * 第二个属性 NOT_SUPPORTED：以非事务方式执行操作，如果当前存在事务，就把当前事务挂起
	 * 即第一个保存失败,第二个成功
	 */
	@Test
	public void test4(){
		Table table = new Table();
		table.setColumn1("column1");
		table.setColumn2("column2");
		table1Server.save4(table);
	}
	
	
	/**
	 * 二个插入,第二个结束后模拟异常
	 * 第二个属性 REQUIRES_NEW：新建事务，如果当前存在事务，把当前事务挂起
	 * 把后一个抛出的异常捕获
	 * 即第一个保存成功,第二个是吧
	 */
	@Test
	public void test5(){
		Table table = new Table();
		table.setColumn1("column1");
		table.setColumn2("column2");
		table1Server.save5(table);
	}
	
	/**
	 * 二个插入,第二个结束后模拟异常
	 * 第二个属性MANDATORY：支持当前事务，如果当前没有事务，就抛出异常。
	 * 即两个失败
	 */
	@Test
	public void test6(){
		Table table = new Table();
		table.setColumn1("column1");
		table.setColumn2("column2");
		table1Server.save6(table);
	}

	
	/**
	 * 二个插入,第二个结束后模拟异常
	 * 第二个属性MANDATORY：支持当前事务，如果当前没有事务，就以非事务方式执行。
	 * 第一个不加事务,即两个成功
	 */
	@Test
	public void test7(){
		Table table = new Table();
		table.setColumn1("column1");
		table.setColumn2("column2");
		table1Server.save7(table);
	}
	
	/**
	 * 二个插入,第二个结束后模拟异常
	 * 第二个属性MANDATORY：支持当前事务，如果当前没有事务，就以非事务方式执行。
	 * 第一个加事务,即两个失败
	 */
	@Test
	public void test8(){
		Table table = new Table();
		table.setColumn1("column1");
		table.setColumn2("column2");
		table1Server.save8(table);
	}
	
	/**
	 * 二个插入,第二个结束后模拟异常
	 * 第二个属性MANDATORY：支持当前事务，如果当前事务存在，则执行一个嵌套事务，如果当前没有事务，就新建一个事务
	 * 在第个方法捕获异常,即第一个成功,第二个失败,如果该用默认事务抛出  "Transaction rolled back because it has been marked as rollback-only"
	 */
	@Test
	public void test9(){
		Table table = new Table();
		table.setColumn1("column1");
		table.setColumn2("column2");
		table1Server.save9(table);
	}

}
