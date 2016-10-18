package com.github.cxt.MySpring.io;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	 public static void main(String[] args) {  
	        try{  
	            ServerSocket ss = new ServerSocket(2222);  
	            System.out.println("Server已经启动。。。");  
	            while(true){  
	                Socket so = ss.accept();//等待客户连接  
	                new ServerThread(so).start();  
	            }  
	        }catch(Exception ee){  
	            ee.printStackTrace();  
	        }  
	    } 
}
