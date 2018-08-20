package com.github.cxt.MyJavaAgent.test;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

public class Demo {

	public static void test(String str) {
		Tools.traceMethodCall();
		Tools.traceMethodProcess();
		long now = System.currentTimeMillis();
		System.out.println("now:" + now);
		long _$startTime = System.currentTimeMillis() + 500;
		System.out.println("_$startTime:" + _$startTime);
//        System.out.println("this is DiyObject1111111！");
//        try {
//			Thread.sleep(600);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//        System.out.println(System.currentTimeMillis());
//        System.out.println(str);
    }
 
	public void print(String str) {
        System.out.println("this is DiyObject22222222222！");
        try {
			Thread.sleep(700);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.println(str);
    }
 
    public String toString() {
    	try {
			URL localURL = new URL("http://10.1.50.130:8888/");
			String str = IOUtils.toString(localURL.openStream(), Charsets.toCharset("UTF-8"));
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
    
    	
        return "this is DiyObject........";
    }
    
    
    
	public void printJDBCdata() {
		System.out.println(Arrays.asList(1, 2, 3).stream().filter(e -> e <= 1).collect(Collectors.toList()).size());
		
		String driver = "com.mysql.jdbc.Driver";
	    String url = "jdbc:mysql://localhost:3306/test";
	    String username = "root";
	    String password = "12345678";
	    Connection conn = null;
	    try {
	        Class.forName(driver);
	        conn = (Connection) DriverManager.getConnection(url, username, password);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    String sql = "select msg from data";
	    PreparedStatement pstmt;
	    try {
	    	
	        pstmt = conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery("select sleep(1)");
	        int col = rs.getMetaData().getColumnCount();
	        System.out.println("============================");
	        while (rs.next()) {
	            for (int i = 1; i <= col; i++) {
	                System.out.print(rs.getString(i) + "\t");
	             }
	            System.out.println("");
	        }
	        System.out.println("============================");
	        rs.close();
	        pstmt.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    try {
	        pstmt = conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        int col = rs.getMetaData().getColumnCount();
	        System.out.println("============================");
	        while (rs.next()) {
	            for (int i = 1; i <= col; i++) {
	                System.out.print(rs.getString(i) + "\t");
	             }
	            System.out.println("");
	        }
	        System.out.println("============================");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
}
