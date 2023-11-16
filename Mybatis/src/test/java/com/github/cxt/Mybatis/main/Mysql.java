package com.github.cxt.Mybatis.main;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-context.xml")
public class Mysql {
	
	private Logger logger = LoggerFactory.getLogger(Mysql.class);
	
	@Autowired
	private DataSource ds;

	@Test
	public void test() throws SQLException {
		Connection con = ds.getConnection();
		DatabaseMetaData metaData = con.getMetaData();
		
		logger.info("判断出数据库厂家:{}", metaData.getDatabaseProductName());
		
		ResultSet rs = metaData.getCatalogs();// metaData.getSchemas();
        logger.info("数据库信息:\n{}",  ResultSetUtil.resultString(rs, true));
		
		PreparedStatement ps = con.prepareStatement("SELECT TABLE_SCHEMA,NULL,TABLE_NAME, TABLE_TYPE FROM information_schema.tables WHERE table_schema='mybatis' AND TABLE_TYPE LIKE '%TABLE%'");
		rs = ps.executeQuery();
		logger.info("库里的所有表:\n{}",  ResultSetUtil.resultString(rs, true));
		
		ps = con.prepareStatement("SELECT TABLE_SCHEMA,NULL,TABLE_NAME,'VIEW' FROM information_schema.views WHERE table_schema='kb'");
		rs = ps.executeQuery();
		logger.info("库里的所有视图:\n{}",  ResultSetUtil.resultString(rs, true));
		
		rs = metaData.getTables(null, "mybatis", "user", new String[]{"TABLE","GLOBAL TEMPORARY"});
		//jdbc里要加 useInformationSchema=true 或者 SELECT table_name,table_comment FROM information_schema.tables WHERE table_schema = 'mybatis' AND table_name='user'
		logger.info("表备注:\n{}",  ResultSetUtil.resultString(rs, true));
		
		
		ps = con.prepareStatement("SELECT column_name,column_type,NUMERIC_PRECISION,NUMERIC_SCALE,IS_NULLABLE,COLUMN_DEFAULT,COLUMN_COMMENT,extra,CHARACTER_MAXIMUM_LENGTH FROM " 
				+ "information_schema.COLUMNS WHERE table_schema = 'mybatis'  AND table_name = 'user' ORDER BY ordinal_position ASC ");
		rs = ps.executeQuery();
		logger.info("表结构:\n{}",  ResultSetUtil.resultString(rs, true));
		
		
		ps = con.prepareStatement("SELECT a.table_schema,a.TABLE_NAME,a.CONSTRAINT_name,b.constraint_type,a.column_name,a.REFERENCED_TABLE_NAME,REFERENCED_COLUMN_NAME,c.UPDATE_RULE,c.DELETE_RULE," 
				+ "CASE CONSTRAINT_TYPE WHEN 'PRIMARY KEY' THEN 0 WHEN 'FOREIGN KEY'  THEN 1 WHEN 'UNIQUE' THEN 2 WHEN 'CHECK' THEN 3 ELSE 4 END, a.ORDINAL_POSITION "
				+ "FROM (SELECT table_schema, TABLE_NAME, CONSTRAINT_name, UPPER(GROUP_CONCAT(DISTINCT column_name)) AS column_name, REFERENCED_TABLE_NAME,"
				+ "UPPER(GROUP_CONCAT(DISTINCT REFERENCED_COLUMN_NAME)) AS REFERENCED_COLUMN_NAME, ORDINAL_POSITION FROM information_schema.KEY_COLUMN_USAGE "
				+ "WHERE table_schema = 'mybatis' AND table_name = 'user' GROUP BY table_schema, TABLE_NAME, REFERENCED_TABLE_NAME, CONSTRAINT_name, ORDINAL_POSITION) a "
				+ "INNER JOIN (SELECT TABLE_NAME,CONSTRAINT_NAME,constraint_type FROM information_schema.TABLE_CONSTRAINTS WHERE TABLE_SCHEMA = 'mybatis' AND TABLE_NAME = 'user') b "
				+ "ON a.constraint_name = b.constraint_name AND a.table_name = b.table_name LEFT JOIN (SELECT CONSTRAINT_NAME, UPDATE_RULE, DELETE_RULE FROM information_schema.REFERENTIAL_CONSTRAINTS "
				+ "WHERE TABLE_NAME = 'user') c ON a.CONSTRAINT_NAME = c.CONSTRAINT_NAME ORDER BY a.CONSTRAINT_name");
		rs = ps.executeQuery();
		logger.info("表的主键:\n{}",  ResultSetUtil.resultString(rs, true));
	}
	

}
