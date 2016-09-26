package com.github.cxt.MySpring.io.aio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;


public class Client implements Runnable{
	private static String DEFAULT_HOST = "127.0.0.1";
	private static int DEFAULT_PORT = 8080;
	private AsynchronousSocketChannel clientChannel;
	
	public Client() throws IOException{
		clientChannel = AsynchronousSocketChannel.open();
		//发起异步连接操作，回调参数就是这个类本身，如果连接成功会回调completed方法
		clientChannel.connect(new InetSocketAddress(DEFAULT_HOST, DEFAULT_PORT));
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception{
		Client client = new Client();
		new Thread(client).start();
		while(true){
			System.out.println("请输入需要发送懂服务器的消息");
			client.sendMsg(new Scanner(System.in).nextLine());
		}
	}
	
	
	private void sendMsg(String msg) {
		try {
			ByteBuffer readBuffer = ByteBuffer.wrap(msg.getBytes());
			clientChannel.write(readBuffer).get();
			read();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
	}




	private void read() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		try {
//			clientChannel.read(buffer).get();
//			buffer.flip();
//			byte[] bytes = new byte[buffer.remaining()];
//			buffer.get(bytes);
//			System.out.println(new String(bytes));
			
			int count = clientChannel.read(buffer).get();
			System.out.println(new String(buffer.array(), 0,
					count));
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void run() {
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}