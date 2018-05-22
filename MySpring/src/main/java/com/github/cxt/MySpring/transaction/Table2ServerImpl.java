package com.github.cxt.MySpring.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.cxt.MySpring.transaction.mybatis.Table;
import com.github.cxt.MySpring.transaction.mybatis.TableDao;

public class Table2ServerImpl implements Table2Server{

	@Autowired
	private TableDao tableDao;
	
	@Transactional
	public void save1(Table table) {
		tableDao.save2(table);
		throw new RuntimeException("测试");
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public void save2(Table table) {
		tableDao.save2(table);
		throw new RuntimeException("测试");
	}

	@Transactional(propagation=Propagation.NEVER)
	public void save3(Table table) {
		tableDao.save2(table);
		throw new RuntimeException("测试");
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void save4(Table table) {
		tableDao.save2(table);
		throw new RuntimeException("测试");
	}

	@Transactional(propagation=Propagation.MANDATORY)
	public void save5(Table table) {
		tableDao.save2(table);
		throw new RuntimeException("测试");
	}
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public void save6(Table table) {
		tableDao.save2(table);
		throw new RuntimeException("测试");
	}
	
	@Transactional(propagation=Propagation.NESTED)
	public void save7(Table table) {
		tableDao.save2(table);
		throw new RuntimeException("测试");
	}
}
