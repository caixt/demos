package com.github.cxt.MySpring.io.nio;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import org.junit.Test;


public class HttpGetTest {
	
	
	@Test
	public void testGet1() throws Exception {
		 Socket s = new Socket("127.0.0.1", 8080);
		 StringBuilder msg = new StringBuilder("GET /servlet/ServletDemo HTTP/1.0").append("\r\n")
				 .append("Host: 127.0.0.1:8080").append("\r\n")
				 .append("Accept: */*").append("\r\n")
				 .append("content-length: 0").append("\r\n")
				 .append("Connection: close").append("\r\n")
				 .append("\r\n");
		 byte[] bytes = msg.toString().getBytes();
		 
		 s.getOutputStream().write(bytes);
		 
		 InputStream in = s.getInputStream();
		 BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		 String line = null;
		 while((line = br.readLine()) != null){
			 System.out.println(line);
		 }
		 s.close();
	}
	
	@Test
	public void testGet2() throws Exception {
		 Socket s = new Socket("127.0.0.1", 8080);
		 StringBuilder msg = new StringBuilder("GET /servlet/ServletDemo HTTP/1.1").append("\r\n")
				 .append("Host: 127.0.0.1:8080").append("\r\n")
				 .append("Accept: */*").append("\r\n")
				 .append("content-length: 0").append("\r\n")
				 .append("Connection: close").append("\r\n")
				 .append("\r\n");
		 byte[] bytes = msg.toString().getBytes();
		 
		 s.getOutputStream().write(bytes);
		 
		 InputStream in = s.getInputStream();
		 BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		 String line = null;
		 while((line = br.readLine()) != null){
			 System.out.println(line);
		 }
		 s.close();
	}
	
	@Test
	public void testGet3() throws Exception {
		 Socket s = new Socket("127.0.0.1", 8080);
		 StringBuilder msg = new StringBuilder("GET /servlet/ServletDemo HTTP/1.0").append("\r\n")
				 .append("Host: 127.0.0.1:8080").append("\r\n")
				 .append("Accept: */*").append("\r\n")
				 .append("content-length: 0").append("\r\n")
				 .append("Connection: close").append("\r\n")
				 .append("\r\n");
		 byte[] bytes = msg.toString().getBytes();
		 int dataLen = bytes.length;
		 int middle = dataLen / 2;
		 s.getOutputStream().write(Arrays.copyOfRange(bytes, 0, middle));
		 s.getOutputStream().write(Arrays.copyOfRange(bytes, middle, bytes.length));
		 
		 InputStream in = s.getInputStream();
		 BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		 String line = null;
		 while((line = br.readLine()) != null){
			 System.out.println(line);
		 }
		 	 		 
		 s.close();
	}
}
