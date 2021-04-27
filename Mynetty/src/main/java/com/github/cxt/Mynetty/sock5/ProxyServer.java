package com.github.cxt.Mynetty.sock5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.socksx.v5.Socks5CommandRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5ServerEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.internal.SystemPropertyUtil;

//https://github.com/line521356/socks5-netty.git
public class ProxyServer {

	private EventLoopGroup bossGroup=new NioEventLoopGroup();

	public EventLoopGroup getBossGroup() {
		return bossGroup;
	}

	private static final Logger logger = LoggerFactory.getLogger(ProxyServer.class);
	
	private int port;
	
	private boolean auth;
	
	private String username;
	
	private String password;
	
	private boolean logging;
	
	private ProxyServer(int port) {
		this.port = port;
	}
	
	public static ProxyServer create(int port) {
		return new ProxyServer(port);
	}
	
	public ProxyServer auth(String username, String password) {
		this.auth = true;
		this.username = username;
		this.password = password;
		return this;
	}
	
	public ProxyServer logging(boolean logging) {
		this.logging = logging;
		return this;
	}
	
	public boolean isAuth() {
		return auth;
	}

	public boolean isLogging() {
		return logging;
	}

	public void start() throws Exception {
		EventLoopGroup boss = new NioEventLoopGroup(2);
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(boss, worker)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 1024)
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
//					流量统计
					ch.pipeline().addLast(
							ProxyChannelTrafficShapingHandler.PROXY_TRAFFIC, 
							new ProxyChannelTrafficShapingHandler(3000)
							);
					//netty日志
					if(logging) {
						ch.pipeline().addLast(new LoggingHandler());
					}
					//Socks5MessagByteBuf
					ch.pipeline().addLast(Socks5ServerEncoder.DEFAULT);
					//sock5 init
					ch.pipeline().addLast(new Socks5InitialRequestDecoder());
					//sock5 init
					ch.pipeline().addLast(new Socks5InitialRequestHandler(ProxyServer.this));
					if(isAuth()) {
						//socks auth
						ch.pipeline().addLast(new Socks5PasswordAuthRequestDecoder());
						//socks auth
						ch.pipeline().addLast(new Socks5PasswordAuthRequestHandler(ProxyServer.this.username, ProxyServer.this.password));
					}
					//socks connection
					ch.pipeline().addLast(new Socks5CommandRequestDecoder());
					//Socks connection
					ch.pipeline().addLast(new Socks5CommandRequestHandler(ProxyServer.this.getBossGroup()));
				}
			});
			
			ChannelFuture future = bootstrap.bind(port).sync();
			logger.debug("bind port : " + port);
			future.channel().closeFuture().sync();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception {
		int port = SystemPropertyUtil.getInt("server.port", 1081);
		ProxyServer.create(port).logging(true)
		.auth("test", "test")
		.start();
	}
}

