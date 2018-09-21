package com.github.cxt.MySpring.io.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ServerHttpNetty {
	
	private static final byte LF = (byte) 10; 
	private static final byte CR = (byte) 13; 
	private static final String SPLIT = new String(new byte[]{CR, LF});
	
	private Boss boss;
	private Work work;
	
	
	public ServerHttpNetty(int port) throws Exception{
		InetSocketAddress localAddress = new InetSocketAddress(port);
		boss = new Boss();
		boss.doRegister();
		boss.doBind(localAddress);
		boss.doBeginRead();
		work = new Work();
	}
	
	public void start() {
		new Thread(boss, "boss").start();
		new Thread(work, "work").start();
	}
	
	
	public static void main(String[] args) throws Exception {
		new ServerHttpNetty(8080).start();
	}
	
	private class Boss implements Runnable{
		
		private Selector selector;
		private SelectionKey selectionKey;
		private ServerSocketChannel serverSocketChannel;
		private int readInterestOp;
		
		public Boss() throws Exception{
			SelectorProvider.provider().openSelector();
		}
		
		public void doRegister() throws IOException{
			selector = SelectorProvider.provider().openSelector();
			serverSocketChannel = SelectorProvider.provider().openServerSocketChannel();
			serverSocketChannel.configureBlocking(false);
			selectionKey = serverSocketChannel.register(selector, 0, serverSocketChannel);
			this.readInterestOp = SelectionKey.OP_ACCEPT;
		}
		
		public void doBind(InetSocketAddress localAddress) throws IOException{
			serverSocketChannel.bind(localAddress);
		}
		
		public void doBeginRead() throws Exception {
	        final SelectionKey selectionKey = this.selectionKey;
	        if (!selectionKey.isValid()) {
	            return;
	        }

	        final int interestOps = selectionKey.interestOps();
	        if ((interestOps & readInterestOp) == 0) {
	            selectionKey.interestOps(interestOps | readInterestOp);
	        }
	    }
		
		private void doRegisterClent(SelectionKey key){
			try {
				ServerSocketChannel server = (ServerSocketChannel) key.channel();
				SocketChannel client = server.accept();
				client.configureBlocking(false);
				work.doRegister(client);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		

		@Override
		public void run() {
			while (true) {
                int keyCount = 0;
				try {
					keyCount = selector.select(1000);
				} catch (IOException e) {
					e.printStackTrace();
				}
                Iterator<SelectionKey> iterator = keyCount > 0 ? selector.selectedKeys().iterator() : null;
                while (iterator != null && iterator.hasNext()) {
                    SelectionKey k = iterator.next();
                    iterator.remove();
                    int readyOps = k.readyOps();
                    if ((readyOps & (SelectionKey.OP_READ | SelectionKey.OP_ACCEPT)) != 0 || readyOps == 0) {
                    	doRegisterClent(k);
                    }
                }
			}
		}
	}
	
	
	class Work implements Runnable{
		
		private Selector selector;
		
		public Work() throws IOException {
			selector = SelectorProvider.provider().openSelector();
		}
		
		public void doRegister(SocketChannel client){
			doRegister(client, new Attachment(client));
		}
		
		public void doRegister(SocketChannel client, Attachment attachment){
			try {
				client.register(selector, SelectionKey.OP_READ, attachment);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			while (true) {
				int keyCount = 0;
				try {
					keyCount = selector.select(1000);
				} catch (IOException e) {
					e.printStackTrace();
				}
                Iterator<SelectionKey> iterator = keyCount > 0 ? selector.selectedKeys().iterator() : null;
				
                while (iterator != null && iterator.hasNext()) {
					SelectionKey selectionKey = iterator.next();
					iterator.remove();
					try {
						handleKey((Attachment) selectionKey.attachment());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		
		private void handleKey(Attachment attachment) throws IOException {
			ByteBuffer receivebuffer = ByteBuffer.allocate(1024 * 4);
			receivebuffer.clear();
			int len = attachment.channel.read(receivebuffer);
			if (len > 0) {
				if(len == -1){
					System.out.println(attachment.channel.getLocalAddress() + "[close]");
					return ;
				}
				receivebuffer.flip();
				System.arraycopy(receivebuffer.array(), 0, attachment.bytes, attachment.size, len);
				attachment.size += len;
				if(!attachment.format()){
					doRegister(attachment.channel, attachment);
					return ;
				}
				System.out.println(attachment.toHttpInfo());
				StringBuilder sb = new StringBuilder();
				String body = "hello world";
				sb.append("HTTP/1.1 200 OK\r\n");
				sb.append("Content-Type:text/txt;charset=").append("UTF-8").append("\r\n");
				sb.append("Content-Length:").append(body.getBytes("UTF-8").length).append("\r\n");
				sb.append("\r\n");
				sb.append(body);
				ByteBuffer sendbuffer = ByteBuffer.allocate(1024);
				String sendText = sb.toString();
				sendbuffer.put(sendText.getBytes("UTF-8"));
				sendbuffer.flip();
				attachment.channel.write(sendbuffer);
				attachment.channel.close();
			}
		}
	}
	
	
	private class Attachment {
		
		SocketChannel channel;

		int size;
		int headerIndex = 3;
		
		byte[] bytes;
		
		String url;
		Map<String, String> header;
		int contentLength = -1;
		
		public Attachment(SocketChannel channel){
			this.channel = channel;
			this.bytes = new byte[1024 * 1024 * 3]; //3M 
			this.size = 0;
		}
		
		private int getHeadSize(){
			return headerIndex + 1;
		}
		
		public boolean format(){
			if(contentLength == -1){
				int index = headerIndex;
				boolean success = false;
				for(; index < size; index ++){
					if(
						bytes[index - 3] == bytes[index - 1] &&
						bytes[index - 1] == CR &&
						bytes[index - 2] == bytes[index - 0] &&	
						bytes[index - 0] == LF
					){
						success = true;
						break;
					}
				}
				headerIndex = index;
				if(success){
					String headStr = new String(bytes, 0, getHeadSize() - 4);
					String[] str = headStr.split(SPLIT);
					url = str[0];
					header = new HashMap<>();
					for(int i = 1; i < str.length; i++){
						String[] s = str[i].split(": ");
						header.put(s[0], s[1]);
						if(s[0].equalsIgnoreCase("Content-Length")){
							contentLength = Integer.parseInt(s[1]);
						}
					}
					if(contentLength == -1){
						contentLength = 0;
					}
				}
			}
			
			if(contentLength == -1){
				return false;
			}
			if(getHeadSize() + contentLength > size){
				return false;
			}
			return true;
		}
		
		
		public String toHttpInfo(){
			StringBuilder sb = new StringBuilder();
			sb.append("url:" + url).append("\r\n");
			for(Map.Entry<String, String> entry : header.entrySet()){
				sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
			}
			if(contentLength > 0){
				sb.append("<body>:").append("\r\n");
				BufferedReader reader = new BufferedReader(new InputStreamReader(getBodyInput(), Charset.forName("UTF-8")));
				String line = null;
				try {
					while((line = reader.readLine()) != null){
						sb.append(line).append("\r\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}finally {
					try {
						reader.close();
					} catch (IOException ignore) {
					}
				}
			}
			return sb.toString();
		}
		
		
		public InputStream getBodyInput(){
			return new InputStream() {
				
				int index = 0;
				
				@Override
				public int read() throws IOException {
					int n = getHeadSize() + (index ++);
					if(n >= size){
						return -1;
					}
					return bytes[n];
				}
			};
		}
	}
}
