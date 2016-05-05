package com.github.cxt.Mybatis.dao;

import java.util.List;
import java.util.Map;

import com.github.cxt.Mybatis.entity.User;

public interface UserDao {

	public User selectByUserId(Long id);
	
	public List<Map<String, Object>> selectAll();
	
	public List<Map<String, Object>> selectByMap(Map<String, Object> map);
	
	public User selectCascadeByUserId(Long id);
	
	public int insertBatch(List<User> users);
	
	public int insert(User user);
}
