package com.github.cxt.Mynetty.tty;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.ScheduledFuture;
import io.termd.core.http.netty.HttpRequestHandler;
import io.termd.core.http.netty.TtyWebSocketFrameHandler;
import io.termd.core.telnet.TelnetHandler;
import io.termd.core.telnet.netty.TelnetChannelHandler;
import io.termd.core.tty.TtyConnection;

public class ProtocolDetectHandler extends ChannelInboundHandlerAdapter {
    private ChannelGroup channelGroup;
    private Supplier<TelnetHandler> handlerFactory;
    private Consumer<TtyConnection> ttyConnectionFactory;
    
	public ProtocolDetectHandler(ChannelGroup channelGroup, final Supplier<TelnetHandler> handlerFactory,
			Consumer<TtyConnection> ttyConnectionFactory) {
		this.channelGroup = channelGroup;
		this.handlerFactory = handlerFactory;
		this.ttyConnectionFactory = ttyConnectionFactory;
	}

    private ScheduledFuture<?> detectTelnetFuture;

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        detectTelnetFuture = ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                channelGroup.add(ctx.channel());
                TelnetChannelHandler handler = new TelnetChannelHandler(handlerFactory);
                ChannelPipeline pipeline = ctx.pipeline();
                pipeline.addLast(handler);
                pipeline.remove(ProtocolDetectHandler.this);
                ctx.fireChannelActive(); // trigger TelnetChannelHandler init
            }

        }, 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        if (in.readableBytes() < 3) {
            return;
        }

        if (detectTelnetFuture != null && detectTelnetFuture.isCancellable()) {
            detectTelnetFuture.cancel(false);
        }

        byte[] bytes = new byte[3];
        in.getBytes(0, bytes);
        String httpHeader = new String(bytes);
        ChannelPipeline pipeline = ctx.pipeline();
        if (!"GET".equalsIgnoreCase(httpHeader)) { // telnet
            channelGroup.add(ctx.channel());
            TelnetChannelHandler handler = new TelnetChannelHandler(handlerFactory);
            pipeline.addLast(handler);
            ctx.fireChannelActive(); // trigger TelnetChannelHandler init
        } else {
        	System.out.println(channelGroup.size());
        	
            pipeline.addLast(new HttpServerCodec());
            pipeline.addLast(new ChunkedWriteHandler());
            pipeline.addLast(new HttpObjectAggregator(64 * 1024));
            pipeline.addLast(new HttpRequestHandler("/ws"));
            pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
            pipeline.addLast(new TtyWebSocketFrameHandler(channelGroup, ttyConnectionFactory));
            ctx.fireChannelActive();
        }
        pipeline.remove(this);
        ctx.fireChannelRead(in);
    }

}