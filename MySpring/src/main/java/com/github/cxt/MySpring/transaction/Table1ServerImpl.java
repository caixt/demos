package com.github.cxt.MySpring.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.cxt.MySpring.transaction.mybatis.Table;
import com.github.cxt.MySpring.transaction.mybatis.TableDao;

@Service
public class Table1ServerImpl implements Table1Server{

	@Autowired
	private TableDao tableDao;
	
	@Override
	public void save(Table table) {
		tableDao.save1(table);
	}
}
