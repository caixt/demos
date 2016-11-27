package com.github.cxt.Mynetty.base;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<ByteBuf>{
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx)throws Exception{
	    ctx.flush();   // 它的作用是把消息发送队列中的消息写入SocketChannel中发送给对方
	    // 为了防止频繁的唤醒Selector进行消息发送，Netty的write方法，并不直接将消息写入SocketChannel中
	    // 调用write方法只是把待发送的消息发到缓冲区中，再调用flush，将发送缓冲区中的消息
	    // 全部写到SocketChannel中。
	}

	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
		cause.printStackTrace();
	    ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
	    byte[]req = new byte[msg.readableBytes()];
	    msg.readBytes(req);
	    String body = new String(req, "UTF-8");
	    System.out.println(body);
	    ctx.close();
	}

}

