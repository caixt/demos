package com.github.cxt.Mybatis.databaseId;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;

public class MyDatavbaseIdProvider implements DatabaseIdProvider {
	Properties props = null;
	@Override
	public void setProperties(Properties p) {
		//p代表mybatis中针对databaseIdProvider配置的属性
		props = p;
	}
 
	@Override
	public String getDatabaseId(DataSource dataSource) throws SQLException {
		return "xxx";
	}
}
