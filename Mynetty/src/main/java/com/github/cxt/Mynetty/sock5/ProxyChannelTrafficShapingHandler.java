package com.github.cxt.Mynetty.sock5;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;

public class ProxyChannelTrafficShapingHandler extends ChannelTrafficShapingHandler {
	
	public static final String PROXY_TRAFFIC = "ProxyChannelTrafficShapingHandler";
	
	private long beginTime;
	
	private long endTime;
	
	private String username = "anonymous";
	
	public static ProxyChannelTrafficShapingHandler get(ChannelHandlerContext ctx) {
		return (ProxyChannelTrafficShapingHandler)ctx.pipeline().get(PROXY_TRAFFIC);
	}
	
	public ProxyChannelTrafficShapingHandler(long checkInterval) {
		super(checkInterval);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		beginTime = System.currentTimeMillis();
		System.out.println("Active");
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		endTime = System.currentTimeMillis();
		System.out.println("Inactive");
		super.channelInactive(ctx);
	}

	public long getBeginTime() {
		return beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public static void username(ChannelHandlerContext ctx, String username) {
		get(ctx).username = username;
	}
	
	public String getUsername() {
		return username;
	}

}
