package com.github.cxt.Mynetty.serialization;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;


public class Client {

	private final static int MAX_OBJECT_SIZE = 1024 * 1024;
	
	private final String host;
	private final int port;

	public Client(String host, int port){
	    this.host = host;
	    this.port = port;
	}

	public void run() throws Exception{
	    EventLoopGroup group = new NioEventLoopGroup();
	    try {
	        Bootstrap bootstrap  = new Bootstrap()
	                .group(group)
	                .channel(NioSocketChannel.class)
		            .handler(new ChannelInitializer<SocketChannel>(){

						@Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new ObjectDecoder(MAX_OBJECT_SIZE,
									ClassResolvers.weakCachingConcurrentResolver(this.getClass()
											.getClassLoader())));
							pipeline.addLast(new ObjectEncoder());
							pipeline.addLast(new ClientHandler());
						}
		            	
		            });
	        
	        Channel channel = bootstrap.connect(host, port).sync().channel();
			// 等待连接关闭
			channel.closeFuture().sync();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        group.shutdownGracefully();
	    }
	}
    
    public static void main(String[] args) throws Exception{
        new Client("127.0.0.1", 11111).run();
    }

}
