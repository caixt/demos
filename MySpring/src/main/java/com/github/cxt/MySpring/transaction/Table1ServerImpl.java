package com.github.cxt.MySpring.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionStatus;
import com.github.cxt.MySpring.transaction.mybatis.Table;
import com.github.cxt.MySpring.transaction.mybatis.TableDao;

public class Table1ServerImpl implements Table1Server{

	@Autowired
	private TableDao tableDao;
	@Autowired
	private Table2Server table2Server;
	
	@Autowired
	private ApplicationEventPublisher publisher;

	
	@Transactional
	@Override
	public void save1(Table table) {
		try{
			tableDao.save1(table);
			//事务的部分核心代码
			DefaultTransactionStatus status = (DefaultTransactionStatus) TransactionAspectSupport.currentTransactionStatus();
			System.out.println(status);
			throw new RuntimeException("测试");
		}finally{
			publisher.publishEvent(1);
			System.out.println("?????????");
		}
	}
	
	@Transactional
	@Override
	public void save2(Table table) {
		tableDao.save1(table);
		table2Server.save1(table);
	}
	
	@Transactional
	@Override
	public void save3(Table table) {
		tableDao.save1(table);
		table2Server.save2(table);
	}

	@Transactional
	@Override
	public void save4(Table table) {
		tableDao.save1(table);
		table2Server.save3(table);
	}

	@Transactional
	@Override
	public void save5(Table table) {
		tableDao.save1(table);
		try{
			table2Server.save4(table);
		}catch(RuntimeException e){
			e.printStackTrace();
		}
	}

	@Transactional
	@Override
	public void save6(Table table) {
		tableDao.save1(table);
		table2Server.save5(table);
	}

	@Override
	public void save7(Table table) {
		tableDao.save1(table);
		table2Server.save6(table);
	}

	@Transactional
	@Override
	public void save8(Table table) {
		tableDao.save1(table);
		table2Server.save6(table);
	}

	@Transactional
	@Override
	public void save9(Table table) {
		tableDao.save1(table);
		try{
		table2Server.save7(table);
		}catch(RuntimeException e){
			e.printStackTrace();
		}
	}
}
