package com.github.cxt.MySpring.io.nio;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class HttpClient {
	
	private static String BLANK = " ";
	private static String CR = "\r";
	private static String LF = "\n";

	public static void main(String[] args) throws Exception {
		get();
		post();
	}

	 public static void get() throws Exception {  
		 Socket s = new Socket("127.0.0.1", 8888);
		 StringBuilder sb = new StringBuilder();
		 sb.append("GET").append(BLANK).append("/testGet").append(BLANK).append("HTTP/1.0").append(CR).append(LF)
		 .append("Host:").append(BLANK).append("127.0.0.1:8888").append(CR).append(LF)
		 .append(CR).append(LF)
		 .append(BLANK);
		 System.out.println(sb.toString());
		 byte[] bytes = sb.toString().getBytes();
		 s.getOutputStream().write(bytes);
		 
		 InputStream is = s.getInputStream();
		 BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		 
		 String line = null;
		 while((line = br.readLine()) != null){
			 System.out.println(line);
		 }
		 System.out.println("test get end...");
		 s.close();
	 } 
	 
	 
	 public static void post() throws Exception {  
		 Socket s = new Socket("127.0.0.1", 8888);
		 String body = "{\"x\":1}";
		 StringBuilder sb = new StringBuilder();
		 sb.append("POST").append(BLANK).append("/testPost").append(BLANK).append("HTTP/1.0").append(CR).append(LF)
		 .append("Host:").append(BLANK).append("127.0.0.1:80").append(CR).append(LF)
		 .append("Content-Type:").append(BLANK).append("application/json").append(CR).append(LF)
		 .append("Content-Length:").append(BLANK).append(body.getBytes().length).append(CR).append(LF)
		 .append(CR).append(LF)
		 .append(body);
		 System.out.println(sb.toString());
		 byte[] bytes = sb.toString().getBytes();
		 s.getOutputStream().write(bytes);
		 
		 
		 InputStream is = s.getInputStream();
		 BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		 
		 String line = null;
		 while((line = br.readLine()) != null){
			 System.out.println(line);
		 }
		 
		 System.out.println("test post end...");
		 s.close();
	 } 
}
