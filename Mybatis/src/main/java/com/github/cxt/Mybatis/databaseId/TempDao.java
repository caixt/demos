package com.github.cxt.Mybatis.databaseId;

import org.apache.ibatis.annotations.Param;

public interface TempDao {
	
	public Temp selectById(@Param("id") Integer id);
	
}
