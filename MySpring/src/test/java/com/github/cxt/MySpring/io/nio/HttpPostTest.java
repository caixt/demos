package com.github.cxt.MySpring.io.nio;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.junit.Test;

public class HttpPostTest {
	
	
	@Test
	public void testPost1() throws Exception {
		 Socket s = new Socket("127.0.0.1", 8080);
		 byte[] body = "{\"name\":\"test\"}".getBytes();
		 int bodyLen = body.length;
		 StringBuilder msg = new StringBuilder()
				 .append("POST /servlet/ServletDemo HTTP/1.0").append("\r\n")
				 .append("Host: 127.0.0.1:8080").append("\r\n")
				 .append("Accept: */*").append("\r\n")
				 .append("content-length: ").append(body.length).append("\r\n")
				 .append("Connection: close").append("\r\n")
				 .append("\r\n");
		 byte[] head = msg.toString().getBytes();
		 int dataLen = head.length + bodyLen;
		 
		 ByteBuffer buffer = ByteBuffer.allocate(dataLen);
		 buffer.put(head);
		 buffer.put(body);
		 s.getOutputStream().write(buffer.array());
		 		 
		 Tools.printlt(s.getInputStream());
		 s.close();
	}
	
	@Test
	public void testPost2() throws Exception {
		 Socket s = new Socket("127.0.0.1", 8080);
		 byte[] body = "{\"name\":\"test\"}".getBytes();
		 StringBuilder msg = new StringBuilder()
				 .append("POST /servlet/ServletDemo HTTP/1.0").append("\r\n")
				 .append("Host: 127.0.0.1:8080").append("\r\n")
				 .append("Accept: */*").append("\r\n")
				 .append("content-length: ").append(body.length).append("\r\n")
				 .append("Connection: close").append("\r\n")
				 .append("\r\n");
		 byte[] head = msg.toString().getBytes();
		 
		 s.getOutputStream().write(head);
		 s.getOutputStream().write(body);
		 		 
		 Tools.printlt(s.getInputStream());
		 s.close();
	}
	
	
	@Test
	public void testPost3() throws Exception {
		 Socket s = new Socket("127.0.0.1", 8080);
		 byte[] body = "{\"name\":\"test\"}".getBytes();
		 StringBuilder msg = new StringBuilder()
				 .append("POST /servlet/ServletDemo HTTP/1.0").append("\r\n")
				 .append("Host: 127.0.0.1:8080").append("\r\n")
				 .append("Accept: */*").append("\r\n")
				 .append("content-length: ").append(body.length).append("\r\n")
				 .append("Connection: close").append("\r\n")
				 .append("\r\n");
		 byte[] head = msg.toString().getBytes();
		 
		 s.getOutputStream().write(head);
		 
		 int middle = body.length / 2;
		 s.getOutputStream().write(Arrays.copyOfRange(body, 0, middle));
		 s.getOutputStream().write(Arrays.copyOfRange(body, middle, body.length));
		 		 
		 Tools.printlt(s.getInputStream());
		 s.close();
	}
	
	
	@Test
	public void testPost4() throws Exception {
		 Socket s = new Socket("127.0.0.1", 8080);
		 byte[] body = "{\"name\":\"test\"}".getBytes();
		 StringBuilder msg = new StringBuilder()
				 .append("POST /servlet/ServletDemo HTTP/1.0").append("\r\n")
				 .append("Host: 127.0.0.1:8080").append("\r\n")
				 .append("Accept: */*").append("\r\n")
				 .append("content-length: ").append(body.length).append("\r\n")
				 .append("Connection: close").append("\r\n")
				 .append("\r\n");
		 byte[] head = msg.toString().getBytes();
		 
		 int middle = body.length / 2;
		 ByteBuffer buffer = ByteBuffer.allocate(head.length + middle);
		 buffer.put(head);
		 buffer.put(Arrays.copyOfRange(body, 0, middle));
		 
		 s.getOutputStream().write(buffer.array());
		 s.getOutputStream().write(Arrays.copyOfRange(body, middle, body.length));
		 		 
		 Tools.printlt(s.getInputStream());
		 s.close();
	}
	
	@Test
	public void testPost5() throws Exception {
		 Socket s = new Socket("127.0.0.1", 8080);
		 byte[] body = "xxxxxxxxxxxxxxxaaaaaaaabbbbbbbccccdddd%%%".getBytes();
		 StringBuilder msg = new StringBuilder()
				 .append("POST /servlet/ServletDemo HTTP/1.0").append("\r\n")
				 .append("Host: 127.0.0.1:8080").append("\r\n")
				 .append("Accept: */*").append("\r\n")
				 .append("content-length: ").append(body.length).append("\r\n")
				 .append("Connection: close").append("\r\n")
				 .append("\r\n");
		 byte[] head = msg.toString().getBytes();
		 
		 int middle = head.length / 2;
		 s.getOutputStream().write(Arrays.copyOfRange(head, 0, middle));
		 s.getOutputStream().write(Arrays.copyOfRange(head, middle, head.length));
		 
		 middle = body.length / 2;
		 s.getOutputStream().write(Arrays.copyOfRange(body, 0, middle));
		 s.getOutputStream().write(Arrays.copyOfRange(body, middle, body.length));
		 		 
		 Tools.printlt(s.getInputStream());
		 s.close();
	}
}
