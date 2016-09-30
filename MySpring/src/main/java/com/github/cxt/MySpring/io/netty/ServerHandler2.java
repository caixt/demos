package com.github.cxt.MySpring.io.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.UnsupportedEncodingException;

/**
 * Handles a server-side channel.
 */
public class ServerHandler2 extends ChannelInboundHandlerAdapter {

	/**
	 * 收到客户端消息
	 * @throws UnsupportedEncodingException 
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
		//System.out.println("!!!!!");
		String calrResult = (String) msg;
		ctx.write(Unpooled.copiedBuffer(calrResult.getBytes()));
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	/**
	 * 异常处理
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}