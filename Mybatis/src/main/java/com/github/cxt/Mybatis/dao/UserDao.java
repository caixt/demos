package com.github.cxt.Mybatis.dao;

import java.util.List;
import java.util.Map;
import com.github.cxt.Mybatis.Criterions;
import com.github.cxt.Mybatis.entity.User;

public interface UserDao {

	public User selectByUserId(Long id);
	
	public List<Map<String, Object>> selectByCriterions(Criterions criterions);
	
	public List<Map<String, Object>> selectAll();
	
	public List<Map<String, Object>> selectByMap(Map<String, Object> map);
	
	public User selectCascadeByUserId(Long id);
	
	public int insertBatch(List<User> users);
	
	public int insert(User user);
	
	public User selectByUserUuid(String uuid);
	
	public String selectUUidByUserId(Long id);
	
	public List<User> selectByStatic();
	
	public List<User> selectByTwoCriterions(Criterions criterions1, Criterions criterions2);

	public  List<User> selectNumberError();
}
