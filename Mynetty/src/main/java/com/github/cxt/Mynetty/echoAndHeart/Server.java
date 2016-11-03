package com.github.cxt.Mynetty.echoAndHeart;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class Server {
	
	private static final int READ_IDEL_TIME_OUT = 4; // 读超时
	private static final int WRITE_IDEL_TIME_OUT = 5;// 写超时
	private static final int ALL_IDEL_TIME_OUT = 7; // 所有超时
	
	public void bind(int port)throws Exception{
        /* 配置服务端的NIO线程组 */
        // NioEventLoopGroup类 是个线程组，包含一组NIO线程，用于网络事件的处理
        // （实际上它就是Reactor线程组）。
        // 创建的2个线程组，1个是服务端接收客户端的连接，另一个是进行SocketChannel的
        // 网络读写
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup WorkerGroup = new NioEventLoopGroup();

        try {
            // ServerBootstrap 类，是启动NIO服务器的辅助启动类
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,WorkerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChildChannelHandler());

            // 绑定端口,同步等待成功
            ChannelFuture f= b.bind(port).sync();

            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        }finally {
            // 释放线程池资源
            bossGroup.shutdownGracefully();
            WorkerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{
        @Override
        protected  void initChannel(SocketChannel ch)throws Exception{
        	ch.pipeline().addLast(new IdleStateHandler(READ_IDEL_TIME_OUT,
    				WRITE_IDEL_TIME_OUT, ALL_IDEL_TIME_OUT, TimeUnit.SECONDS));
        	ch.pipeline().addLast(new ServerHandler());
        }
    }

    public static void main(String[]args)throws Exception{
        new Server().bind(11111);
    }
}
