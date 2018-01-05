println '!!!!?????!!!'


import java.sql.Connection;  
import java.sql.DriverManager;
import groovy.sql.Sql;


def driver = "com.mysql.jdbc.Driver";  
def url = "jdbc:mysql://127.0.0.1:3306/mybatis?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8";  
def name = "root";  
def passwd = "12345678";  


Connection connection = null;
try{
	Class.forName(driver);
	connection = DriverManager.getConnection(url, name, passwd);
	System.out.println(connection);
}finally{
	if(connection !=null){
		connection.close();
	}
}