package com.github.cxt.Mynetty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.SystemPropertyUtil;

public class HttpServer {


	private final int port;
	private EventLoopGroup boss = new NioEventLoopGroup(1);
	private EventLoopGroup worker = new NioEventLoopGroup(1);

	public HttpServer(int port) {
		this.port = port;
	}

	public void run() throws Exception {
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(boss, worker).channel(NioServerSocketChannel.class).childHandler(
				new HttpServerInitializer());
			setChannelOptions(bootstrap);

			Channel ch = bootstrap.bind(port).sync().channel();

			ch.closeFuture().sync(); // blocked

		} finally {
			shutdown();
		}
	}

	protected void shutdown() {
		boss.shutdownGracefully();
		worker.shutdownGracefully();
	}

	protected void setChannelOptions(ServerBootstrap bootstrap) {
		bootstrap.childOption(ChannelOption.MAX_MESSAGES_PER_READ, 36);
	}

	public static void main(String[] args) {
		try {
			new HttpServer(SystemPropertyUtil.getInt("server.port", 11111)).run();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
