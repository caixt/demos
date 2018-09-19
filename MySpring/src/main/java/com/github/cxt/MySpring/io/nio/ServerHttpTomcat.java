package com.github.cxt.MySpring.io.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerHttpTomcat {
	
	private BlockPoller block;
	private Acceptor acceptor;
	private Poller[] pollers;
	private ExecutorService executor;
	
	
	public ServerHttpTomcat(int port) throws Exception{
		acceptor = new Acceptor(port);
		int pollerSize = 2;
		pollers = new Poller[pollerSize];
		block = new BlockPoller();
		for(int i = 0; i < pollerSize; i++){
			pollers[i] = new Poller();
		}
		executor = Executors.newFixedThreadPool(10); 
	}
	
	public void start() {
		new Thread(block, "block").start();
		int pollerSize = pollers.length;
		for(int i = 0; i < pollerSize; i++){
			new Thread(pollers[i], "pollers-" + (i + 1)).start();
		}
		new Thread(acceptor, "acceptor").start();
	}
	
	
	public static void main(String[] args) throws Exception {
		new ServerHttpTomcat(8080).start();
	}
	
	
	private class Poller implements Runnable{
		
		private Queue<SocketChannel> events;
		private Selector selector = null;
		
		public Poller() throws IOException{
			events = new ArrayBlockingQueue<>(10);
			selector = Selector.open();
		}
		
		
		private void register(SocketChannel socketChannel){
			register(new Attachment(socketChannel));
		}
		
		private void register(Attachment attachment){
			try {
				attachment.channel.register(selector, SelectionKey.OP_READ, attachment);
			} catch (ClosedChannelException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			while(true){
				int keyCount = 0;
				try {
					keyCount = selector.select(1000);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(keyCount == 0){
					events();
				}
				else{
					Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
	                while (iterator != null && iterator.hasNext()) {
	                    SelectionKey sk = iterator.next();
	                    Attachment attachment = (Attachment)sk.attachment();
	                    if (attachment == null) {
	                        iterator.remove();
	                    } else {
	                        iterator.remove();
	                        processKey(sk, attachment);
	                    }
	                }
				}
			}
			
		}

		private void processKey(SelectionKey sk, Attachment attachment) {
			try{
				if (sk.isReadable() || sk.isWritable()) {
					sk.interestOps(sk.interestOps() & (~ sk.readyOps()));
					if (sk.isReadable()) {
						processSocket(attachment);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		private void processSocket(final Attachment attachment) throws IOException {
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 4);
			int len = attachment.channel.read(byteBuffer);
			if(len == -1){
				System.out.println(attachment.channel.getLocalAddress() + "[close]");
				return ;
			}
			byteBuffer.flip();
			System.arraycopy(byteBuffer.array(), 0, attachment.headByte, attachment.size, len);
			attachment.size += len;
			if(!parseHead(attachment)){
				register(attachment);
				return ;
			}
			executor.submit(new Runnable() {
				
				@Override
				public void run() {
					try{
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
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});
		}

		private static final byte LF = (byte) 10; 
		private static final byte CR = (byte) 13; 
		
		private boolean parseHead(Attachment attachment) {
			int index = attachment.headerIndex;
			boolean success = false;
			byte[] bytes = attachment.headByte;
			for(; index < attachment.size; index ++){
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
			attachment.headerIndex = index;
			if(success){
				attachment.format();
			}
			return success;
		}

		private void events() {
			SocketChannel socketChannel;
			 while ( (socketChannel = events.poll()) != null ) {
                register(socketChannel);
            }
		}
	}
	
	
	private class BlockPoller implements Runnable{
		private Queue<Runnable> events;
		private Selector selector = null;
		protected final AtomicInteger wakeupCounter = new AtomicInteger(0);
		
		public BlockPoller() throws IOException{
			events = new ArrayBlockingQueue<>(10);
			selector = Selector.open();
		}
		
		public void add(final Attachment key, final int ops) {
            if ( key == null ) return;
            SocketChannel ch = key.channel;
            if ( ch == null ) return;

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    SelectionKey sk = ch.keyFor(selector);
                    if (sk == null) {
                        try {
							sk = ch.register(selector, ops, key);
						} catch (ClosedChannelException e) {
							e.printStackTrace();
						}
                    } else {
                        sk.interestOps(sk.interestOps() | ops);
                    }
                }
            };
            events.offer(r);
        }

		 public void run() {
            while (true) {
            	try{
	            	events();
	                int keyCount = 0;
	                int i = wakeupCounter.get();
	                if (i>0)
	                    keyCount = selector.selectNow();
	                else {
	                    wakeupCounter.set(-1);
	                    keyCount = selector.select(1000);
	                }
	                wakeupCounter.set(0);
	
	                Iterator<SelectionKey> iterator = keyCount > 0 ? selector.selectedKeys().iterator() : null;
	
	                while (iterator != null && iterator.hasNext()) {
	                    SelectionKey sk = iterator.next();
	                    Attachment attachment = (Attachment)sk.attachment();
	                    iterator.remove();
	                    sk.interestOps(sk.interestOps() & (~sk.readyOps()));
	                    if ( sk.isReadable() ) {
	                        attachment.getReadLatch().countDown();
	                    }
	                }
            	}catch(Exception e){
            		e.printStackTrace();
            	}
            }
        }
		 
		private boolean events() {
			boolean result = false;
            Runnable r = null;
            result = (events.size() > 0);
            while ( (r = events.poll()) != null ) {
                r.run();
                result = true;
            }
            return result;
		}
	}
	
	
	private class Acceptor implements Runnable{
		
		private AtomicInteger pollerRotater = new AtomicInteger(0);
		private ServerSocketChannel serverSocketChannel;
		
		public Acceptor (int port) throws Exception{
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(true);
			ServerSocket serverSocket = serverSocketChannel.socket();
			serverSocket.bind(new InetSocketAddress(port));
		}

		@Override
		public void run() {
			while (true) {
				try {
					SocketChannel socketChannel = serverSocketChannel.accept();
					socketChannel.configureBlocking(false);
					getPoller0().events.offer(socketChannel);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		public Poller getPoller0() {
	        int idx = Math.abs(pollerRotater.incrementAndGet()) % pollers.length;
	        return pollers[idx];
	    }
	}
	
	
	private class Attachment {
		
		SocketChannel channel;
		private CountDownLatch readLatch = null;
		int size;
		
		int headerIndex = 3;
		
		byte[] headByte;
		
		byte[] bodyPart;
		int bodyPartMark;
		
		String url;
		Map<String, String> header;
		int contentLength = 0;
		int bodyReadIndex = 0;
		
		public Attachment(SocketChannel channel){
			this.channel = channel;
			this.headByte = new byte[1024 * 1024 * 1]; //1M 
			this.size = 0;
		}
		
		private void format(){
			byte[] temp = headByte;
			int headSize = headerIndex + 1;
			headByte = new byte[headSize];
			System.arraycopy(temp, 0, headByte, 0, headSize);
			int bodyPartSize = size - headSize;
			if(bodyPartSize > 0){
				bodyPart = new byte[bodyPartSize];
				System.arraycopy(temp, headSize, bodyPart, 0, bodyPartSize);
			}
			header = new HashMap<>();
			String headStr = new String(headByte, 0, headSize);
			String[] str = headStr.split("\r\n");
			url = str[0];
			for(int i = 1; i < str.length; i++){
				String[] s = str[i].split(": ");
				header.put(s[0], s[1]);
				if(s[0].equalsIgnoreCase("Content-Length")){
					contentLength = Integer.parseInt(s[1]);
				}
			}
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
		
		public CountDownLatch getReadLatch() { return readLatch; }
		public void startReadLatch() { readLatch = new CountDownLatch(1);}
        public void awaitReadLatch(long timeout, TimeUnit unit) throws InterruptedException { readLatch.await(timeout,unit);}
        public void resetReadLatch() { readLatch = null; }
		
		public InputStream getBodyInput(){
			return new InputStream() {
				
				@Override
				public int read() throws IOException {
					byte b = 0;
					if(bodyReadIndex >= contentLength){
						return -1;
					}
					int index = bodyReadIndex - bodyPartMark;
					if(bodyPart == null){
						bodyPart = bolckRead();
					}
					if(index < bodyPart.length){
						b = bodyPart[index];
						if(index == bodyPart.length - 1){
							bodyPartMark += bodyPart.length;
							bodyPart = null;
						}
					}
					else {
						b = -1;
					}
					bodyReadIndex ++;
					return b;
				}
			};
		}
		
		
		private int readTimeout = 60000;
		private byte[] bolckRead() throws IOException{
			ByteBuffer buffer = ByteBuffer.allocate(1024 * 4);
			int read = bolckRead(buffer);
			buffer.flip();
			byte[] bytes = new byte[read];
			System.arraycopy(buffer.array(), 0, bytes, 0, read);
			return bytes;
		}
		
		private int bolckRead(ByteBuffer buf) throws IOException{
			boolean timedout = false;
			long time = System.currentTimeMillis();
			int read = 0;
			int keycount = 1;
			while(!timedout){
				if (keycount > 0) {
					read = channel.read(buf);
	                if (read != 0) {
	                    break;
	                }
				}
				try{
					startReadLatch();
					block.add(this,SelectionKey.OP_READ);
		            if (readTimeout < 0) {
		            	awaitReadLatch(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		            } else {
		                awaitReadLatch(readTimeout, TimeUnit.MILLISECONDS);
		            }
				} catch (InterruptedException ignore) {}
				
				if ( getReadLatch()!=null && getReadLatch().getCount()> 0) {
                    keycount = 0;
                }else {
                    keycount = 1;
                    resetReadLatch();
                }
                if (readTimeout >= 0 && (keycount == 0)){
                	timedout = (System.currentTimeMillis() - time) >= readTimeout;
                }
			}
			if (timedout){
                throw new SocketTimeoutException();
			}
			return read;
		}
	}
}
