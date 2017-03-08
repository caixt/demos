package com.github.cxt.cassandra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Test;

public class Main {

	@Test
	public void test() throws ClassNotFoundException, SQLException{
		Class.forName("org.apache.cassandra.cql.jdbc.CassandraDriver");  
		//直接调用getConnection去连接cassandra，如果是有用户名密码，需要跟上参数user/passwd如下。keyspace是数据库对应的space  
		Connection con = DriverManager.getConnection("jdbc:cassandra://10.1.51.115/test", "", "");  
		//测试一条简单的查询语句  
		String t = "SELECT id,name,age,male FROM test_table";  
		  
		Statement statement = con.createStatement();  
		ResultSet rs = statement.executeQuery(t);//执行查询语句  
		  
		while (rs.next()) {//打印查询到数据条目内容  
		    System.out.println(rs.getInt(1) + "!" + rs.getString(2) + "!" + rs.getInt(3) + "!" + rs.getBoolean(4));  
		}  
		  
		rs.close();  
		statement.close();  
			
	}
}
