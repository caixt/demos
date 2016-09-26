package com.github.cxt.MySpring.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class Server {
    static int PORT = 8080;
    static int BUFFER_SIZE = 1024;
    static String CHARSET = "utf-8"; //默认编码
    static CharsetDecoder decoder = Charset.forName(CHARSET).newDecoder(); //解码

    AsynchronousServerSocketChannel serverChannel;

    public Server() throws IOException {
    }

    private void listen(int port) throws Exception {

        //打开一个服务通道
        //绑定服务端口
        this.serverChannel = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port), 100);
        this.serverChannel.accept(this, new AcceptHandler());

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println("运行中...");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();

    }


    /**
     * accept到一个请求时的回调
     */
    private class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Server> {
        @Override
        public void completed(final AsynchronousSocketChannel client, Server attachment) {
        	attachment.serverChannel.accept(attachment, this);// 监听新的请求，递归调用。
            try {
                System.out.println("远程地址：" + client.getRemoteAddress());
                if (client.isOpen()) {
                    System.out.println("client.isOpen：" + client.getRemoteAddress());
                    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                    client.read(buffer, client, new ReadHandler(buffer));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void failed(Throwable exc, Server attachment) {
        	attachment.serverChannel.accept(attachment, this);// 监听新的请求，递归调用。
            exc.printStackTrace();
        }
    }

    /**
     * Read到请求数据的回调
     */
    private class ReadHandler implements CompletionHandler<Integer, AsynchronousSocketChannel> {
    	private ByteBuffer buffer;
    	
    	public ReadHandler(ByteBuffer buffer) {
			this.buffer = buffer;
		}

        @Override
        public void completed(Integer result, AsynchronousSocketChannel attachment) {
            try {
                if (result < 0) {// 客户端关闭了连接
                    Server.close(attachment);
                } else if (result == 0) {
                    System.out.println("空数据"); // 处理空数据
                } else {
                    // 读取请求，处理客户端发送的数据
                    buffer.flip();
                    CharBuffer charBuffer = Server.decoder.decode(buffer);
                    System.out.println(charBuffer.toString()); //接收请求

                    //响应操作，服务器响应结果
                    buffer.clear();
                    String res = "服务器端的消息:" + charBuffer.toString();
                    buffer = ByteBuffer.wrap(res.getBytes());
                    attachment.write(buffer, attachment, new WriteHandler());//Response：响应。
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
            exc.printStackTrace();
            Server.close(attachment);
        }
    }

    /**
     * Write响应完请求的回调
     */
    private class WriteHandler implements CompletionHandler<Integer, AsynchronousSocketChannel> {

        @Override
        public void completed(Integer result, AsynchronousSocketChannel attachment) {
        	ByteBuffer readBuffer = ByteBuffer.allocate(1024);
//        	readBuffer.clear();
//        	readBuffer.flip();
        	attachment.read(readBuffer, attachment, new ReadHandler(readBuffer));
        }

        @Override
        public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
            exc.printStackTrace();
            Server.close(attachment);
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("正在启动服务...");
            Server server = new Server();
            server.listen(PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void close(AsynchronousSocketChannel client) {
        try {
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
