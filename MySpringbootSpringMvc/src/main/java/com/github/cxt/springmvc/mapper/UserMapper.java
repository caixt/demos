package com.github.cxt.springmvc.mapper;

import com.github.cxt.springmvc.entity.User;
import com.github.pagehelper.Page;

public interface UserMapper {

	public Page<User> selectPage();
	
	public User selectByUserPrimaryKey(String id);
	
	public int insert(User user);
	
	public int updateByPrimaryKey(User user);
	
	public int deleteByPrimaryKey(String id);
}