package com.github.cxt.Mybatis.dao;

import java.util.List;

import com.github.cxt.Mybatis.entity.User;

public interface UserDataDao {

	public List<User> selectRightList();
	
	public List<User> selectErrorList();
}
