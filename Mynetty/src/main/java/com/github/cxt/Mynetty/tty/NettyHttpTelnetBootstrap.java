package com.github.cxt.Mynetty.tty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ImmediateEventExecutor;
import io.termd.core.telnet.TelnetTtyConnection;
import io.termd.core.tty.TtyConnection;
import io.termd.core.util.Helper;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


public class NettyHttpTelnetBootstrap {

	private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
	private String host;
	private int port;
	private EventLoopGroup group;
	private Channel channel;

	public NettyHttpTelnetBootstrap() {
		this.host = "localhost";
		this.port = 8080;
		this.group = new NioEventLoopGroup(2);
	}

	public String getHost() {
		return host;
	}

	public NettyHttpTelnetBootstrap setHost(String host) {
		this.host = host;
		return this;
	}

	public int getPort() {
		return port;
	}

	public NettyHttpTelnetBootstrap setPort(int port) {
		this.port = port;
		return this;
	}

	public void start(Consumer<TtyConnection> handler, Consumer<Throwable> doneHandler) {
		ServerBootstrap boostrap = new ServerBootstrap();
        boostrap.group(group).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100)
        	.handler(new LoggingHandler(LogLevel.INFO))
        	.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProtocolDetectHandler(channelGroup, () -> new TelnetTtyConnection(false, false, StandardCharsets.UTF_8, handler), handler));
                }
            });

        ChannelFuture f = boostrap.bind(getPort());
        f.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                	channel = f.channel();
                    doneHandler.accept(null);
                } else {
                    doneHandler.accept(future.cause());
                }
            }
        });
	}

	public CompletableFuture<Void> start(Consumer<TtyConnection> handler) throws Exception {
		CompletableFuture<Void> fut = new CompletableFuture<>();
		start(handler, Helper.startedHandler(fut));
		return fut;
	}

	public void stop(Consumer<Throwable> doneHandler) {
		if (channel != null) {
			channel.close();
		}
		channelGroup.close().addListener((Future<Void> f) -> doneHandler.accept(f.cause()));
	}

	public CompletableFuture<Void> stop() throws InterruptedException {
		CompletableFuture<Void> fut = new CompletableFuture<>();
		stop(Helper.stoppedHandler(fut));
		return fut;
	}
}