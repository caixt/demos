import java.sql.Connection;  
import java.sql.DriverManager;

Connection connection = null;
try{
	Class.forName(driver);
	connection = DriverManager.getConnection(url, username, passwd);
	System.out.println(connection);
}finally{
	if(connection !=null){
		connection.close();
	}
}